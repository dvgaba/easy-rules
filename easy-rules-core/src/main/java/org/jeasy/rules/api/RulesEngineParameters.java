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

import org.jeasy.rules.core.DefaultRulesEngine;
import org.jeasy.rules.core.InferenceRulesEngine;

/**
 * Parameters of a rules engine.
 *
 * <ul>
 *   <li>When parameters are used with a {@link DefaultRulesEngine}, they are applied on <strong>all
 *       registered rules</strong>.
 *   <li>When parameters are used with a {@link InferenceRulesEngine}, they are applied on
 *       <strong>candidate rules in each iteration</strong>.
 * </ul>
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class RulesEngineParameters {

  /** Parameter to define annotation used to mark facts optional, for example javax.annotation.Nullable */
  private static Class<?> OPTIONAL_PARAMETER_ANNOTATION = null;

  /** Default rule priority threshold. */
  public static final int DEFAULT_RULE_PRIORITY_THRESHOLD = Integer.MAX_VALUE;

  /** Parameter to skip next applicable rules when a rule is applied. */
  private boolean skipOnFirstAppliedRule;

  /** Parameter to skip next applicable rules when a rule is non triggered */
  private boolean skipOnFirstNonTriggeredRule;

  /** Parameter to skip next applicable rules when a rule has failed. */
  private boolean skipOnFirstFailedRule;

  /** Parameter to skip next applicable rules when a rule has failed. */
  private boolean failsOnException;

  /** Parameter to skip next rules if priority exceeds a user defined threshold. */
  private int priorityThreshold;

  /** Create a new {@link RulesEngineParameters} with default values. */
  public RulesEngineParameters() {
    this.priorityThreshold = RulesEngineParameters.DEFAULT_RULE_PRIORITY_THRESHOLD;
  }

  /**
   * Create a new {@link RulesEngineParameters}.
   *
   * @param skipOnFirstAppliedRule parameter to skip next applicable rules on first applied rule.
   * @param skipOnFirstFailedRule parameter to skip next applicable rules on first failed rule.
   * @param skipOnFirstNonTriggeredRule parameter to skip next applicable rules on first non
   *     triggered rule.
   * @param priorityThreshold threshold after which rules should be skipped.
   */
  public RulesEngineParameters(
      final boolean skipOnFirstAppliedRule,
      final boolean skipOnFirstFailedRule,
      final boolean skipOnFirstNonTriggeredRule,
      final int priorityThreshold,
      final boolean failsOnException) {
    this.skipOnFirstAppliedRule = skipOnFirstAppliedRule;
    this.skipOnFirstFailedRule = skipOnFirstFailedRule;
    this.skipOnFirstNonTriggeredRule = skipOnFirstNonTriggeredRule;
    this.priorityThreshold = priorityThreshold;
    this.failsOnException = failsOnException;
  }

  public int getPriorityThreshold() {
    return priorityThreshold;
  }

  public void setPriorityThreshold(final int priorityThreshold) {
    this.priorityThreshold = priorityThreshold;
  }

  public RulesEngineParameters priorityThreshold(final int priorityThreshold) {
    setPriorityThreshold(priorityThreshold);
    return this;
  }

  public boolean isSkipOnFirstAppliedRule() {
    return skipOnFirstAppliedRule;
  }

  public void setSkipOnFirstAppliedRule(final boolean skipOnFirstAppliedRule) {
    this.skipOnFirstAppliedRule = skipOnFirstAppliedRule;
  }

  public RulesEngineParameters skipOnFirstAppliedRule(final boolean skipOnFirstAppliedRule) {
    setSkipOnFirstAppliedRule(skipOnFirstAppliedRule);
    return this;
  }

  public boolean isSkipOnFirstNonTriggeredRule() {
    return skipOnFirstNonTriggeredRule;
  }

  public void setSkipOnFirstNonTriggeredRule(final boolean skipOnFirstNonTriggeredRule) {
    this.skipOnFirstNonTriggeredRule = skipOnFirstNonTriggeredRule;
  }

  public RulesEngineParameters skipOnFirstNonTriggeredRule(
      final boolean skipOnFirstNonTriggeredRule) {
    setSkipOnFirstNonTriggeredRule(skipOnFirstNonTriggeredRule);
    return this;
  }

  public boolean isSkipOnFirstFailedRule() {
    return skipOnFirstFailedRule;
  }

  public void setSkipOnFirstFailedRule(final boolean skipOnFirstFailedRule) {
    this.skipOnFirstFailedRule = skipOnFirstFailedRule;
  }

  public boolean isFailsOnException() {
    return failsOnException;
  }

  public void setFailsOnException(boolean failsOnException) {
    this.failsOnException = failsOnException;
  }

  public RulesEngineParameters skipOnFirstFailedRule(final boolean skipOnFirstFailedRule) {
    setSkipOnFirstFailedRule(skipOnFirstFailedRule);
    return this;
  }

  public RulesEngineParameters failsOnException(final boolean failsOnException) {
    setFailsOnException(failsOnException);
    return this;
  }

  public static boolean hasOptionalParameterAnnotation(Class<?> methodAnnotation) {
    return OPTIONAL_PARAMETER_ANNOTATION != null && OPTIONAL_PARAMETER_ANNOTATION.equals(methodAnnotation);
  }

  public static void setOptionalParameterAnnotation(Class<?> optionalParameterAnnotation) {
    OPTIONAL_PARAMETER_ANNOTATION = optionalParameterAnnotation;
  }

  @Override
  public String toString() {
    return "Engine parameters { "
        + "skipOnFirstAppliedRule = "
        + skipOnFirstAppliedRule
        + ", skipOnFirstNonTriggeredRule = "
        + skipOnFirstNonTriggeredRule
        + ", skipOnFirstFailedRule = "
        + skipOnFirstFailedRule
        + ", priorityThreshold = "
        + priorityThreshold
        + " }";
  }
}
