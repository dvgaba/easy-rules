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
package org.jeasy.rules.support.reader;

import org.yaml.snakeyaml.Yaml;

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Rule definition reader based on <a href="https://github.com/FasterXML/jackson-dataformats-text/tree/master/yaml">Jackson Yaml</a>.
 *
 * This reader expects a collection of rule definitions as input even for a single rule. For example:
 *
 * <pre>
 *     rule1
 *     ---
 *     rule2
 * </pre>
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
@SuppressWarnings("unchecked")
public class YamlRuleDefinitionReader extends AbstractRuleDefinitionReader {

    private final Yaml yaml;

    /**
     * Create a new {@link YamlRuleDefinitionReader}.
     */
    public YamlRuleDefinitionReader() {
        this(new Yaml());
    }

    /**
     * Create a new {@link YamlRuleDefinitionReader}.
     *
     * @param yaml to use to read rule definitions
     */
    public YamlRuleDefinitionReader(Yaml yaml) {
        this.yaml = yaml;
    }

    @Override
    protected Iterable<Map<String, Object>> loadRules(Reader reader) {
        List<Map<String, Object>> rulesList = new ArrayList<>();
        Iterable<Object> rules = yaml.loadAll(reader);
        for (Object rule : rules) {
            rulesList.add((Map<String, Object>) rule);
        }
        return rulesList;
    }
}
