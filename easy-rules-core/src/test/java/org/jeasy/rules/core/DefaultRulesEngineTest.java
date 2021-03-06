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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.lenient;

import java.util.List;
import java.util.Map;
import org.assertj.core.api.Assertions;
import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Priority;
import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.RuleListener;
import org.jeasy.rules.api.Rules;
import org.jeasy.rules.api.RulesEngineListener;
import org.jeasy.rules.api.RulesEngineParameters;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mock;

public class DefaultRulesEngineTest extends AbstractTest {

  @Mock private RuleListener ruleListener;

  @Mock private RulesEngineListener rulesEngineListener;

  private AnnotatedRule annotatedRule;

  @BeforeEach
  public void setup() throws Exception {
    super.setup();
    lenient().when(rule1.getName()).thenReturn("r");
    lenient().when(rule1.getPriority()).thenReturn(1);
    annotatedRule = new AnnotatedRule();
  }

  @Test
  void whenFireRules_thenNullRulesShouldNotBeAccepted() {
    assertThrows(NullPointerException.class, () -> {
      rulesEngine.fire(null, new Facts());
    });
  }

  @Test
  void whenFireRules_thenNullFactsShouldNotBeAccepted() {
    assertThrows(NullPointerException.class, () -> {
      rulesEngine.fire(new Rules(), null);
    });
  }

  @Test
  void whenCheckRules_thenNullRulesShouldNotBeAccepted() {
    assertThrows(NullPointerException.class, () -> {
      rulesEngine.check(null, new Facts());
    });
  }

  @Test
  void whenCheckRules_thenNullFactsShouldNotBeAccepted() {
    assertThrows(NullPointerException.class, () -> {
      rulesEngine.check(new Rules(), null);
    });
  }

  @Test
  void whenConditionIsTrue_thenActionShouldBeExecuted() throws Exception {
    // Given
    when(rule1.evaluate(facts)).thenReturn(true);
    rules.register(rule1);

    // When
    rulesEngine.fire(rules, facts);

    // Then
    verify(rule1).execute(facts);
  }

  @Test
  void whenConditionIsFalse_thenActionShouldNotBeExecuted() throws Exception {
    // Given
    when(rule1.evaluate(facts)).thenReturn(false);
    rules.register(rule1);

    // When
    rulesEngine.fire(rules, facts);

    // Then
    verify(rule1, never()).execute(facts);
  }

  @Test
  void rulesMustBeTriggeredInTheirNaturalOrder() throws Exception {
    // Given
    when(rule1.evaluate(facts)).thenReturn(true);
    when(rule2.evaluate(facts)).thenReturn(true);
    when(rule2.compareTo(rule1)).thenReturn(1);
    rules.register(rule1);
    rules.register(rule2);

    // When
    rulesEngine.fire(rules, facts);

    // Then
    InOrder inOrder = inOrder(rule1, rule2);
    inOrder.verify(rule1).execute(facts);
    inOrder.verify(rule2).execute(facts);
  }

  @Test
  void rulesMustBeCheckedInTheirNaturalOrder() {
    // Given
    when(rule1.evaluate(facts)).thenReturn(true);
    when(rule2.evaluate(facts)).thenReturn(true);
    when(rule2.compareTo(rule1)).thenReturn(1);
    rules.register(rule1);
    rules.register(rule2);

    // When
    rulesEngine.check(rules, facts);

    // Then
    InOrder inOrder = inOrder(rule1, rule2);
    inOrder.verify(rule1).evaluate(facts);
    inOrder.verify(rule2).evaluate(facts);
  }

  @Test
  void actionsMustBeExecutedInTheDefinedOrder() {
    // Given
    rules.register(annotatedRule);
    rulesEngine = new DefaultRulesEngine(new RulesEngineParameters().failsOnException(true));
    // When
    rulesEngine.fire(rules, facts);

    // Then
    assertEquals("012", annotatedRule.getActionSequence());
  }

  @Test
  void doesntThrowExceptionForExceptionInRule() {
    // Given
    rules.register(new ExceptionRule(true, true));
    rulesEngine = new DefaultRulesEngine();
    // When
    Assertions.assertThatNoException().isThrownBy(() -> rulesEngine.fire(rules, facts));
  }

  @Test
  void doesThrowExceptionForExceptionInRule() {
    // Given
    rules.register(new ExceptionRule(true, true));
    rulesEngine = new DefaultRulesEngine(new RulesEngineParameters().failsOnException(true));
    // When
    Assertions.assertThatThrownBy(() -> rulesEngine.fire(rules, facts));
  }

  @Test
  void annotatedRulesAndNonAnnotatedRulesShouldBeUsableTogether() throws Exception {
    // Given
    when(rule1.evaluate(facts)).thenReturn(true);
    rules.register(rule1);
    rules.register(annotatedRule);

    // When
    rulesEngine.fire(rules, facts);

    // Then
    verify(rule1).execute(facts);
    assertThat(annotatedRule.isExecuted()).isTrue();
  }

  @Test
  void whenRuleNameIsNotSpecified_thenItShouldBeEqualToClassNameByDefault() {
    org.jeasy.rules.api.Rule rule = RuleProxy.asRule(new DummyRule());
    assertThat(rule.getName()).isEqualTo("DummyRule");
  }

  @Test
  void whenRuleDescriptionIsNotSpecified_thenItShouldBeEqualToConditionNameFollowedByActionsNames() {
    org.jeasy.rules.api.Rule rule = RuleProxy.asRule(new DummyRule());
    assertThat(rule.getDescription()).isEqualTo("when condition then action1,action2");
  }

  @Test
  void testCheckRules() {
    // Given
    when(rule1.evaluate(facts)).thenReturn(true);
    rules.register(rule1);
    rules.register(annotatedRule);

    // When
    Map<org.jeasy.rules.api.Rule, Boolean> result = rulesEngine.check(rules, facts);

    // Then
    assertThat(result).hasSize(2);
    for (org.jeasy.rules.api.Rule r : rules) {
      assertThat(result.get(r)).isTrue();
    }
  }

  @Test
  void listenerShouldBeInvokedBeforeCheckingRules() {
    // Given
    when(rule1.evaluate(facts)).thenReturn(true);
    when(ruleListener.beforeEvaluate(rule1, facts)).thenReturn(true);
    DefaultRulesEngine rulesEngine = new DefaultRulesEngine();
    rulesEngine.registerRuleListener(ruleListener);
    rules.register(rule1);

    // When
    rulesEngine.check(rules, facts);

    // Then
    verify(ruleListener).beforeEvaluate(rule1, facts);
  }

  @Test
  void getParametersShouldReturnACopyOfTheParameters() {
    // Given
    RulesEngineParameters parameters =
        new RulesEngineParameters()
            .skipOnFirstAppliedRule(true)
            .skipOnFirstFailedRule(true)
            .skipOnFirstNonTriggeredRule(true)
            .priorityThreshold(42);
    DefaultRulesEngine rulesEngine = new DefaultRulesEngine(parameters);

    // When
    RulesEngineParameters engineParameters = rulesEngine.getParameters();

    // Then
    Assertions.assertThat(engineParameters).isNotSameAs(parameters);
    Assertions.assertThat(engineParameters).usingRecursiveComparison().isEqualTo(parameters);
  }

  @Test
  void testGetRuleListeners() {
    // Given
    DefaultRulesEngine rulesEngine = new DefaultRulesEngine();
    rulesEngine.registerRuleListener(ruleListener);

    // When
    List<RuleListener> ruleListeners = rulesEngine.getRuleListeners();

    // Then
    assertThat(ruleListeners).contains(ruleListener);
  }

  @Test
  void getRuleListenersShouldReturnAnUnmodifiableList() {
    // Given
    DefaultRulesEngine rulesEngine = new DefaultRulesEngine();
    rulesEngine.registerRuleListener(ruleListener);

    // When
    List<RuleListener> ruleListeners = rulesEngine.getRuleListeners();

    // Then
    assertThatThrownBy(ruleListeners::clear).isInstanceOf(UnsupportedOperationException.class);
  }

  @Test
  void testGetRulesEngineListeners() {
    // Given
    DefaultRulesEngine rulesEngine = new DefaultRulesEngine();
    rulesEngine.registerRulesEngineListener(rulesEngineListener);

    // When
    List<RulesEngineListener> rulesEngineListeners = rulesEngine.getRulesEngineListeners();

    // Then
    assertThat(rulesEngineListeners).contains(rulesEngineListener);
  }

  @Test
  void getRulesEngineListenersShouldReturnAnUnmodifiableList() {
    // Given
    DefaultRulesEngine rulesEngine = new DefaultRulesEngine();
    rulesEngine.registerRulesEngineListener(rulesEngineListener);

    // When
    List<RulesEngineListener> rulesEngineListeners = rulesEngine.getRulesEngineListeners();

    // Then
    assertThatThrownBy(rulesEngineListeners::clear)
        .isInstanceOf(UnsupportedOperationException.class);
  }

  @AfterEach
  public void clearRules() {
    rules.clear();
  }

  @org.jeasy.rules.annotation.Rule(name = "myRule", description = "my rule description")
  public static class AnnotatedRule {

    private boolean executed;

    private String actionSequence = "";

    @Condition
    public boolean when() {
      return true;
    }

    @Action
    public void then0() {

      actionSequence += "0";
    }

    @Action(order = 1)
    public void then1() {
      actionSequence += "1";
    }

    @Action(order = 2)
    public void then2() {
      actionSequence += "2";
      executed = true;
    }

    @Priority
    public int getPriority() {
      return 0;
    }

    public boolean isExecuted() {
      return executed;
    }

    public String getActionSequence() {
      return actionSequence;
    }
  }

  @org.jeasy.rules.annotation.Rule
  public static class DummyRule {

    @Condition
    public boolean condition() {
      return true;
    }

    @Action(order = 1)
    public void action1() {
      // no op
    }

    @Action(order = 2)
    public void action2() {
      // no op
    }
  }

  @org.jeasy.rules.annotation.Rule(name = "myRule", description = "my rule description")
  public static class ExceptionRule {

    private boolean exceptionInWhen;
    private boolean exceptionInThen;

    public ExceptionRule(boolean exceptionInWhen, boolean exceptionInThen) {
      this.exceptionInWhen = exceptionInWhen;
      this.exceptionInThen = exceptionInThen;
    }

    private String actionSequence = "";

    @Condition
    public boolean when() {
      if (exceptionInWhen) {
        throw new RuntimeException("ERROR");
      }
      return true;
    }

    @Action
    public void then() {
      if (exceptionInThen) {
        throw new RuntimeException("ERROR");
      }
    }

    @Priority
    public int getPriority() {
      return 0;
    }
  }
}
