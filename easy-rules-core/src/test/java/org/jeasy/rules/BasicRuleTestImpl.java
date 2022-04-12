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
package org.jeasy.rules;

import org.jeasy.rules.api.Rule;
import org.jeasy.rules.core.BasicRule;

public class BasicRuleTestImpl extends BasicRule {
  /** Create a new {@link BasicRule}. */
  public BasicRuleTestImpl() {
    this(Rule.DEFAULT_NAME, Rule.DEFAULT_DESCRIPTION, Rule.DEFAULT_PRIORITY);
  }

  /**
   * Create a new {@link BasicRule}.
   *
   * @param name rule name
   */
  public BasicRuleTestImpl(final String name) {
    this(name, Rule.DEFAULT_DESCRIPTION, Rule.DEFAULT_PRIORITY);
  }

  /**
   * Create a new {@link BasicRule}.
   *
   * @param name rule name
   * @param description rule description
   */
  public BasicRuleTestImpl(final String name, final String description) {
    this(name, description, Rule.DEFAULT_PRIORITY);
  }

  /**
   * Create a new {@link BasicRule}.
   *
   * @param name rule name
   * @param description rule description
   * @param priority rule priority
   */
  public BasicRuleTestImpl(final String name, final String description, final int priority) {
    super(name, description, priority);
  }

  @Override
  public boolean equals(Object o) {
    return super.equals(o);
  }

  @Override
  public int hashCode() {
    return super.hashCode();
  }

  @Override
  public int compareTo(Rule rule) {
    return super.compareTo(rule);
  }
}
