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
package org.jeasy.rules.spel;

import static org.assertj.core.api.Assertions.assertThat;

import org.jeasy.rules.api.Facts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SpELRuleTest {

  private Facts facts;
  private SpELRule spelRule;

  @BeforeEach
  public void setUp() {
    facts = new Facts();
    spelRule =
        new SpELRule()
            .name("spel rule")
            .description("rule using SpEL")
            .priority(1)
            .when("#{ ['person'].age > 18 }")
            .then("#{ ['person'].setAdult(true) }");
  }

  @Test
  void whenTheRuleIsTriggered_thenConditionShouldBeEvaluated() {
    // given
    Person person = new Person("foo", 20);
    facts.put("person", person);

    // when
    boolean evaluationResult = spelRule.evaluate(facts);

    // then
    assertThat(evaluationResult).isTrue();
  }

  @Test
  void whenTheConditionIsTrue_thenActionsShouldBeExecuted() throws Exception {
    // given
    Person foo = new Person("foo", 20);
    facts.put("person", foo);

    // when
    spelRule.execute(facts);

    // then
    assertThat(foo.isAdult()).isTrue();
  }

  @Test
  void testRuleWithRootVariable() throws Exception {
    // given
    Person foo = new Person("foo", 20);
    facts.put("person", foo);
    spelRule =
        new SpELRule()
            .name("rn")
            .description("rd")
            .priority(1)
            .when("#{ #root['person'].age > 18 }")
            .then("#{ #root['person'].setAdult(true) }");

    // when
    spelRule.execute(facts);

    // then
    assertThat(foo.isAdult()).isTrue();
  }
}
