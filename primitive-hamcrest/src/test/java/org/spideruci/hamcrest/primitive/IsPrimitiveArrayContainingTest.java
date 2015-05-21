package org.spideruci.hamcrest.primitive;

import static org.spideruci.hamcrest.AbstractMatcherTest.*;
import static org.spideruci.hamcrest.primitive.IsIntArrayContaining.hasInt;
import static org.hamcrest.core.IsEqual.equalTo;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import org.junit.Test;

public class IsPrimitiveArrayContainingTest {

  @Test
  public void copesWithNullsAndUnknownTypes() {
    Matcher<?> matcher = hasInt(equalTo((Integer)null));
    
    assertNullSafe(matcher);
    assertUnknownTypeSafe(matcher);
  }
  
  @Test 
  public void matchesAnArrayThatContainsAnElementForTheGivenMatcher() {
      final Matcher<int[]> itemMatcher = hasInt(equalTo(1));

      assertMatches("array containing 1", itemMatcher, new int[] {1, 2, 3});
  }
  
  @Test 
  public void doesNotMatchArrayWithoutAnElementForGivenMatcher() {
      final Matcher<int[]> matcher = hasInt(mismatchable(Integer.valueOf(1)));
      
      assertMismatchDescription("mismatches were: [mismatched: 2, mismatched: 3]", matcher, new int[] {2, 3});
      assertMismatchDescription("was empty", matcher, new int[0]);
  }

  @Test 
  public void doesNotMatchNull() {
      assertDoesNotMatch("doesn't match null", hasInt(equalTo(1)), null);
  }

  @Test 
  public void hasAReadableDescription() {
      assertDescription("a primitive integer array (int[]) containing mismatchable: 1", hasInt(mismatchable(1)));
  }
    
  private static Matcher<Integer> mismatchable(final Integer integer) {
    return new TypeSafeDiagnosingMatcher<Integer>() {
        @Override
        protected boolean matchesSafely(Integer item, Description mismatchDescription) {
            if (integer.equals(item)) 
                return true;

            mismatchDescription.appendText("mismatched: " + item);
            return false;
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("mismatchable: " + integer);
        }
    };
}

}
