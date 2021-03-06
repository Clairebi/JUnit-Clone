package org.spideruci.tacoco.reporting;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.jacoco.core.analysis.Analyzer;
import org.jacoco.core.analysis.CoverageBuilder;
import org.jacoco.core.analysis.IBundleCoverage;
import org.jacoco.core.data.ExecutionData;
import org.jacoco.core.data.ExecutionDataStore;
import org.jacoco.core.data.IExecutionDataVisitor;


public class ExecutionDataParser implements IExecutionDataVisitor {
  
  private final File classesDirectory;
  private final ExecDataPrintManager printManager;
  private String coverageTitle;
  private ExecutionDataStore execDataStore = new ExecutionDataStore();
  
  public ExecutionDataParser(final File projectDirectory,
      final ExecDataPrintManager printManager) {
    this.coverageTitle = projectDirectory.getName();
    File tempDirRef = new File(projectDirectory, "target/classes");
    if(!tempDirRef.exists() || !tempDirRef.isDirectory()) {
      tempDirRef = new File(projectDirectory, "bin");
      if(!tempDirRef.exists() || !tempDirRef.isDirectory()) {
        throw new RuntimeException("unable to find `target/classes/` or `bin/` "
            + "directories in the specified project-directory:" 
            + projectDirectory.getPath());
      }
    }
    
    this.classesDirectory = tempDirRef;
    this.printManager = printManager;
    tempDirRef = null;
  }

  public void visitClassExecution(final ExecutionData data) {
    if(data == null) return;
    System.out.printf("adding exec-data for: %s %d%n", 
        data.getName(), 
        getHitCount(data.getProbes()));
    execDataStore.put(data);
  }
  
  public void resetExecDataStore() {
    if(execDataStore.getContents().size() == 0) {
      execDataStore = new ExecutionDataStore();
      return;
    }
    
    try {
      System.out.printf("analyzing exec-data for: %s%n", coverageTitle);
      IBundleCoverage coverage = this.analyzeStructure(execDataStore);
      printCoverage(coverage);
    } catch (IOException e) {
      e.printStackTrace();
    }
    
    execDataStore = new ExecutionDataStore();
  }
  
  private int count = 0; 
  private void printCoverage(IBundleCoverage coverage) {
    ICoveragePrintable printer = 
        new CoverageJsonPrinter(coverage, printManager.jsonOut(),
            printManager.isPrettyPrint(), printManager.format());
    printer.printCoverageTitle();
    printer.printCoverage();
    System.out.printf("completed printing coverage bundle for %s.%n", coverage.getName());
    System.out.printf("completed printing %d coverage bundle(s).%n%n", ++count);
  }
  
  public void setCoverageTitle(final String title) {
    this.coverageTitle = title;
  }
  
  private IBundleCoverage analyzeStructure(final ExecutionDataStore data) throws IOException {
    final CoverageBuilder coverageBuilder = new CoverageBuilder();
    final Analyzer analyzer = new Analyzer(data, coverageBuilder);
    analyzer.analyzeAll(classesDirectory);
    return coverageBuilder.getBundle(coverageTitle);
  }
  
  private int getHitCount(final boolean[] data) {
    int count = 0;
    for (final boolean hit : data) {
      if (hit) {
        count++;
      }
    }
    return count;
  }

}
