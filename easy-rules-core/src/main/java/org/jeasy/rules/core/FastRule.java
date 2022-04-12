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

import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rule;

/**
 * Basic rule implementation class that provides common methods.
 *
 * <p>You can extend this class and override {@link FastRule#when(Facts)} and {@link
 * FastRule#then(Facts)} to provide rule conditions and actions logic. This is very similar to
 * {@link BasicRule}
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public abstract class FastRule implements Rule {

  /** Rule name. */
  protected String name;

  /** Rule description. */
  protected String description;

  /** Rule priority. */
  protected int priority;

  protected FastRule(String name, String description, int priority) {
    this.name = name;
    this.description = description;
    this.priority = priority;
  }

  protected FastRule(String name) {
    this.name = name;
  }

  protected abstract boolean when(Facts facts);

  protected abstract void then(Facts facts);

  @Override
  public final boolean evaluate(Facts facts) {
    return this.when(facts);
  }

  @Override
  public final void execute(Facts facts) throws Exception {
    this.then(facts);
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String getDescription() {
    return description;
  }

  @Override
  public int getPriority() {
    return priority;
  }
}
