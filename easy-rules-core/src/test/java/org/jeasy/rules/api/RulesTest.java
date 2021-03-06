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
package org.jeasy.rules.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.HashSet;
import java.util.Set;
import org.jeasy.rules.BasicRuleTestImpl;
import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.junit.jupiter.api.Test;

public class RulesTest {

  private Rules rules = new Rules();

  @Test
  void register() {
    rules.register(new DummyRule());

    assertThat(rules).hasSize(1);
  }

  @Test
  void rulesMustHaveUniqueName() {
    Rule r1 = new BasicRuleTestImpl("rule");
    Rule r2 = new BasicRuleTestImpl("rule");
    Set<Rule> ruleSet = new HashSet<>();
    ruleSet.add(r1);
    ruleSet.add(r2);

    rules = new Rules(ruleSet);

    assertThat(rules).hasSize(1);
  }

  @Test
  void unregister() {
    DummyRule rule = new DummyRule();
    rules.register(rule);
    rules.unregister(rule);

    assertThat(rules).isEmpty();
  }

  @Test
  void unregisterByName() {
    Rule r1 = new BasicRuleTestImpl("rule1");
    Rule r2 = new BasicRuleTestImpl("rule2");
    Set<Rule> ruleSet = new HashSet<>();
    ruleSet.add(r1);
    ruleSet.add(r2);

    rules = new Rules(ruleSet);
    rules.unregister("rule2");

    assertThat(rules).hasSize(1).containsExactly(r1);
  }

  @Test
  void unregisterByNameNonExistingRule() {
    Rule r1 = new BasicRuleTestImpl("rule1");
    Set<Rule> ruleSet = new HashSet<>();
    ruleSet.add(r1);

    rules = new Rules(ruleSet);
    rules.unregister("rule2");

    assertThat(rules).hasSize(1).containsExactly(r1);
  }

  @Test
  void isEmpty() {
    assertThat(rules.isEmpty()).isTrue();
  }

  @Test
  void clear() {
    rules.register(new DummyRule());
    rules.clear();

    assertThat(rules).isEmpty();
  }

  @Test
  void sort() {
    Rule r1 = new BasicRuleTestImpl("rule", "", 1);
    Rule r2 = new BasicRuleTestImpl("rule", "", Integer.MAX_VALUE);
    DummyRule r3 = new DummyRule();

    rules.register(r3);
    rules.register(r1);
    rules.register(r2);

    assertThat(rules).startsWith(r1).endsWith(r2);
  }

  @Test
  void size() {
    assertThat(rules.size()).isZero();

    rules.register(new DummyRule());
    assertThat(rules.size()).isEqualTo(1);

    rules.unregister(new DummyRule());
    assertThat(rules.size()).isZero();
  }

  @Test
  void register_multiple() {
    rules.register(new BasicRuleTestImpl("ruleA"), new BasicRuleTestImpl("ruleB"));
    assertThat(rules.size()).isEqualTo(2);
  }

  @Test
  void unregister_noneLeft() {
    rules.register(new BasicRuleTestImpl("ruleA"), new BasicRuleTestImpl("ruleB"));
    assertThat(rules.size()).isEqualTo(2);

    rules.unregister(new BasicRuleTestImpl("ruleA"), new BasicRuleTestImpl("ruleB"));
    assertThat(rules.size()).isZero();
  }

  @Test
  void unregister_oneLeft() {
    rules.register(new BasicRuleTestImpl("ruleA"), new BasicRuleTestImpl("ruleB"));
    assertThat(rules.size()).isEqualTo(2);

    rules.unregister(new BasicRuleTestImpl("ruleA"));
    assertThat(rules.size()).isEqualTo(1);
  }

  @Test
  void whenRegisterNullRule_thenShouldThrowNullPointerException() {
    assertThrows(NullPointerException.class, () -> {
      rules.register(null);
    });
  }

  @org.jeasy.rules.annotation.Rule
  static class DummyRule {
    @Condition
    public boolean when() {
      return true;
    }

    @Action
    public void then() {}
  }
}
