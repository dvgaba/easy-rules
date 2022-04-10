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
package org.jeasy.rules.spel;

import org.jeasy.rules.api.Action;
import org.jeasy.rules.api.Facts;
import org.junit.Test;
import org.springframework.expression.ParserContext;
import org.springframework.expression.common.TemplateParserContext;

import static com.github.stefanbirkner.systemlambda.SystemLambda.tapSystemOutNormalized;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class SpELActionTest {

    @Test
    public void testSpELActionExecution() throws Exception {
        // given
        Action markAsAdult = new SpELAction("#{ ['person'].setAdult(true) }");
        Facts facts = new Facts();
        Person foo = new Person("foo", 20);
        facts.put("person", foo);

        // when
        markAsAdult.execute(facts);

        // then
        assertThat(foo.isAdult()).isTrue();
    }

    @Test
    public void testSpELFunctionExecution() throws Exception {
        // given
        Action printAction = new SpELAction("#{ T(org.jeasy.rules.spel.Person).sayHello() }");
        Facts facts = new Facts();

        // when
        String output = tapSystemOutNormalized(
                () -> printAction.execute(facts));

        // then
        assertThat(output).isEqualTo("hello\n");
    }

    @Test
    public void testSpELActionExecutionWithFailure() {
        // given
        Action action = new SpELAction("#{ T(org.jeasy.rules.spel.Person).sayHi() }");
        Facts facts = new Facts();
        Person foo = new Person("foo", 20);
        facts.put("person", foo);

        // when
        assertThatThrownBy(() -> action.execute(facts))
                // then
                .isInstanceOf(Exception.class)
                .hasMessage("EL1004E: Method call: Method sayHi() cannot be found on type org.jeasy.rules.spel.Person");
    }

    @Test
    public void testSpELActionWithExpressionAndParserContext() throws Exception {
        // given
        ParserContext context = new TemplateParserContext("%{", "}");
        Action printAction = new SpELAction("%{ T(org.jeasy.rules.spel.Person).sayHello() }", context);
        Facts facts = new Facts();

        // when
        String output = tapSystemOutNormalized(
                () -> printAction.execute(facts));

        // then
        assertThat(output).isEqualTo("hello\n");

    }
}
