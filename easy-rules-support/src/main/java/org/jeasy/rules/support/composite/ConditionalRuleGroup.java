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
package org.jeasy.rules.support.composite;

import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rule;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.TreeSet;
import java.util.List;
import java.util.Set;

/**
 * A conditional rule group is a composite rule where the rule with the highest 
 * priority acts as a condition: if the rule with the highest priority evaluates
 * to true, then we try to evaluate the rest of the rules and execute the ones
 * that evaluate to true.
 * 
 * <strong>This class is not thread-safe.</strong>
 *
 * @author Dag Framstad (dagframstad@gmail.com)
 */
public class ConditionalRuleGroup extends CompositeRule {

    private Set<Rule> successfulEvaluations;
    private Rule conditionalRule;

    /**
     * Create a conditional rule group.
     */
    public ConditionalRuleGroup() {
    }

    /**
     * Create a conditional rule group.
     *
     * @param name of the conditional rule
     */
    public ConditionalRuleGroup(String name) {
        super(name);
    }

    /**
     * Create a conditional rule group.
     *
     * @param name        of the conditional rule
     * @param description of the conditional rule
     */
    public ConditionalRuleGroup(String name, String description) {
        super(name, description);
    }

    /**
     * Create a conditional rule group.
     *
     * @param name        of the conditional rule
     * @param description of the conditional rule
     * @param priority    of the composite rule
     */
    public ConditionalRuleGroup(String name, String description, int priority) {
        super(name, description, priority);
    }

    /**
     * A conditional rule group will trigger all its composing rules if the condition
     * of the rule with highest priority evaluates to true.
     * 
     * @param facts The facts.
     * @return true if the conditions of all composing rules evaluate to true
     */
    @Override
    public boolean evaluate(Facts facts) {
        successfulEvaluations = new HashSet<>();
        conditionalRule = getRuleWithHighestPriority();
        if (conditionalRule.evaluate(facts)) {
            for (Rule rule : rules) {
                if (rule != conditionalRule && rule.evaluate(facts)) {
                    successfulEvaluations.add(rule);
                }
            }
            return true;
        }
        return false;
    }

    /**
     * When a conditional rule group is executed, all rules that evaluated to true
     * are performed in their natural order, but with the conditional rule 
     * (the one with the highest priority) first.
     *
     * @param facts The facts.
     *
     * @throws Exception thrown if an exception occurs during actions performing
     */
    @Override
    public void execute(Facts facts) throws Exception {
        conditionalRule.execute(facts);
        for (Rule rule : sort(successfulEvaluations)) {
            rule.execute(facts);
        }
    }

    private Rule getRuleWithHighestPriority() {
        List<Rule> copy = sort(rules);
        // make sure we only have one rule with the highest priority
        Rule highest = copy.get(0);
        if (copy.size() > 1 && copy.get(1).getPriority() == highest.getPriority()) {
           throw new IllegalArgumentException("Only one rule can have highest priority");
        }
        return highest;
    }

    private List<Rule> sort(Set<Rule> rules) {
        return new ArrayList<>(new TreeSet<>(rules));
    }

}
