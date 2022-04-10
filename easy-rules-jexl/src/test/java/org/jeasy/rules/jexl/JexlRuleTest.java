/*
 * The MIT License
 *
 *  Copyright (c) 2020, Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
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
package org.jeasy.rules.jexl;

import static org.assertj.core.api.Assertions.assertThat;

import org.jeasy.rules.api.Facts;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Lauri Kimmel
 * @author Mahmoud Ben Hassine
 */
public class JexlRuleTest {

    private final Facts facts = new Facts();
    private final JexlRule jexlRule = new JexlRule();

    @Before
    public void setUp() {
        jexlRule.when("person.age > 18");
        jexlRule.then("person.setAdult(true);");
    }

    @Test
    public void whenTheRuleIsTriggered_thenConditionShouldBeEvaluated() {
        // given
        facts.put("person", new Person("foo", 20));

        // when
        boolean evaluationResult = jexlRule.evaluate(facts);

        // then
        assertThat(evaluationResult).isTrue();
    }

    @Test
    public void whenTheConditionIsTrue_thenActionsShouldBeExecuted() throws Exception {
        // given
        Person foo = new Person("foo", 20);
        facts.put("person", foo);

        // when
        jexlRule.execute(facts);

        // then
        assertThat(foo.isAdult()).isTrue();
    }
}
