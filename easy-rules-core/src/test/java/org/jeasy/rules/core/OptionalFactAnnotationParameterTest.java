/*
 * The MIT License
 *
 *  Copyright (c) 2022, Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
 */
package org.jeasy.rules.core;

import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Fact;
import org.jeasy.rules.annotation.Rule;
import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rules;
import org.jeasy.rules.api.RulesEngineParameters;
import org.jeasy.rules.core.MissingFactAnnotationParameterTest.AnnotatedParametersRule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.annotation.Nullable;
import java.util.Map;
import org.mockito.internal.matchers.Null;

/**
 * Null facts are not accepted by design, a declared fact can be missing though.
 */
public class OptionalFactAnnotationParameterTest extends AbstractTest {

  @BeforeEach
  public void setup() throws Exception {
    facts = new Facts();
    facts.put("fact1", fact1);
    facts.put("fact2", fact2);
    rules = new Rules();

    RulesEngineParameters rulesEngineParameters = new RulesEngineParameters();
    rulesEngineParameters.setFailsOnException(true);
    rulesEngine = new DefaultRulesEngine(rulesEngineParameters);
  }

  @AfterEach
  void cleanup() {
    RulesEngineParameters.setOptionalParameterAnnotation(null);
  }

  @Test
  public void testMissingFact() {
    RulesEngineParameters.setOptionalParameterAnnotation(Nullable.class);
    Rules rules = new Rules();
    rules.register(new AnnotatedParametersRule());

    Facts facts = new Facts();
    facts.put("fact1", new Object());

    Map<org.jeasy.rules.api.Rule, Boolean> results = rulesEngine.check(rules, facts);

    for (boolean b : results.values()) {
      Assertions.assertTrue(b);
    }
  }

  @Test
  public void testMissingFactNotAnnotated() {

    Rules rules = new Rules();


      new AnnotatedParametersRule();
      rules.register(new AnnotatedParametersRule());

    Facts facts = new Facts();
    facts.put("fact1", new Object());

    Map<org.jeasy.rules.api.Rule, Boolean> results = rulesEngine.check(rules, facts);

    for (boolean b : results.values()) {
      Assertions.assertFalse(b);
    }
  }

  @Test
  public void testNoMissingFact() {
    Rules rules = new Rules();
    rules.register(new AnnotatedParametersRule() {
      @Condition
      public boolean when(@Fact("fact1") Object fact1, @Nullable @Fact("fact2") Object fact2) {
        return fact1 != null && fact2 != null;
      }
    });
    Facts facts = new Facts();
    facts.put("fact1", new Object());
    facts.put("fact2", new Object());

    Map<org.jeasy.rules.api.Rule, Boolean> results = rulesEngine.check(rules, facts);

    for (boolean b : results.values()) {
      Assertions.assertTrue(b);
    }
  }

  @Rule
  public static class AnnotatedParametersRule {

    @Condition
    public boolean when(@Fact("fact1") Object fact1, @Nullable @Fact("fact2") Object fact2) {
      return fact1 != null && fact2 == null;
    }

    @Action
    public void then(@Fact("fact1") Object fact1, @Nullable @Fact("fact2") Object fact2) {
    }

  }
}