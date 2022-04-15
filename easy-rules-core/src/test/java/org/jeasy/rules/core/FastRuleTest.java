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

import org.jeasy.rules.BasicRuleTestImpl;
import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rule;
import org.jeasy.rules.api.Rules;
import org.jeasy.rules.api.RulesEngine;
import org.junit.Test;

public class FastRuleTest {

  private static class FastDummyRule extends FastRule {

    public FastDummyRule(String name, String description, int priority) {
      super(name, description, priority);
    }

    @Override
    protected boolean when(Facts facts) {
      String data = facts.get("data");
      return data.equals("data");
    }

    @Override
    protected void then(Facts facts) {
      facts.put("result", "done");
    }

    @Override
    public int compareTo(final Rule rule) {
      return Integer.compare(this.getPriority(), rule.getPriority());
    }
  }

  @Test
  public void evaluate() {
    RulesEngine rulesEngine = new DefaultRulesEngine();
    Rules rules = new Rules();

    rules.register(new BasicRuleTestImpl());
    rules.register(new FastDummyRule("Rule", "Rule", 0));
    Facts facts = new Facts();
    facts.put("data", "data");
    rulesEngine.fire(rules, facts);
    assertThat((String)facts.get("result")).isEqualTo("done");
  }
}
