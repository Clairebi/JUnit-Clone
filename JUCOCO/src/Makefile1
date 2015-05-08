JAVA:= java
JAVAC:= javac
BUILD_DIR:=../bin
LIB_DIR:=../lib
SRC_DIR:=.

TEST_SUITE_NAME:=ClassATest
TEST_SUITE:=$(BUILD_DIR)/$(TEST_SUITE_NAME).class
CLASS_FILES:= $(BUILD_DIR)/ExecDump.class $(BUILD_DIR)/PerTestCaseCoverageRunner.class $(BUILD_DIR)/JacocoListener.class $(BUILD_DIR)/ExecAnalyze.class $(TEST_SUITE)  $(BUILD_DIR)/PerJUnitTestCaseVisitor.class

JUNIT:=$(LIB_DIR)/junit-4.11.jar
JACOCO_AGENT:=$(LIB_DIR)/jacocoagent.jar
JACOCO_CORE:=$(LIB_DIR)/org.jacoco.core-0.6.2.20130126-2101.jar
HAMCREST:=$(LIB_DIR)/hamcrest-core-1.3.jar
ASM:=$(LIB_DIR)/asm-all-4.1.jar

JAVAAGENT_OPTIONS:=destfile=$(BUILD_DIR)/jacoco.exec,dumponexit=false,excludes=*org/jacoco*:*org/junit*:*com/sun*:ExecDump
JAVAAGENT:=$(LIB_DIR)/jacocoagent.jar

.PHONY: all run dump clean analyze

# Rule for compiling the .class files, but does not run anything
all: $(BUILD_DIR) $(CLASS_FILES)

$(BUILD_DIR)/%.class: %.java
	$(JAVAC) -d $(BUILD_DIR) -cp $(SRC_DIR):$(JUNIT):$(JACOCO_AGENT):$(JACOCO_CORE) $<

$(BUILD_DIR):
	mkdir -p $(BUILD_DIR)

# Rule for running the PerTestCaseCoverageRunner program to generate the jacoco.exec file
run: $(BUILD_DIR) $(CLASS_FILES)
	$(JAVA) -javaagent:$(JAVAAGENT)=$(JAVAAGENT_OPTIONS) -cp $(BUILD_DIR):$(JUNIT):$(HAMCREST):$(JACOCO_AGENT) PerTestCaseCoverageRunner $(TEST_SUITE_NAME)

# Rule for running the ExecDump program with an expected jacoco.exec file in the build directory. Runs the program to generate the jacoco.exec file if it does not exist.
dump: $(BUILD_DIR) $(CLASS_FILES) $(BUILD_DIR)/jacoco.exec
	$(JAVA) -cp $(BUILD_DIR):$(JACOCO_CORE) ExecDump $(BUILD_DIR)/jacoco.exec

$(BUILD_DIR)/jacoco.exec: $(CLASS_FILES)
	$(JAVA) -javaagent:$(JAVAAGENT)=$(JAVAAGENT_OPTIONS) -cp $(BUILD_DIR):$(JUNIT):$(HAMCREST):$(JACOCO_AGENT) PerTestCaseCoverageRunner $(TEST_SUITE_NAME)

# Rule for removing the entire build directory.
clean:
	rm -r $(BUILD_DIR)

analyze: $(BUILD_DIR) $(CLASS_FILES)
	$(JAVA) -cp $(BUILD_DIR):$(JACOCO_CORE):$(ASM) ExecAnalyze
