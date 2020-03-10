/**
 * Copyright (c) 2016-2020, Mihai Emil Andronache
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 * Redistributions of source code must retain the above copyright notice, this
 *  list of conditions and the following disclaimer.
 *  Redistributions in binary form must reproduce the above copyright notice,
 *  this list of conditions and the following disclaimer in the documentation
 *  and/or other materials provided with the distribution.
 * Neither the name of the copyright holder nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 */
package com.amihaiemil.eoyaml;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;

/**
 * Unit tests for {@link ReadYamlMapping}.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 1.0.0
 *
 */
public final class ReadYamlMappingTest {

    /**
     * ReadYamlMapping can return the YamlMapping mapped to a
     * String key.
     */
    @Test
    public void returnsYamlMappingWithStringKey(){
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("first: somethingElse", 0));
        lines.add(new RtYamlLine("second: ", 1));
        lines.add(new RtYamlLine("  fourth: some", 2));
        lines.add(new RtYamlLine("  fifth: values", 3));
        lines.add(new RtYamlLine("third: something", 4));
        final YamlMapping map = new ReadYamlMapping(new AllYamlLines(lines));
        System.out.println(map);
        final YamlMapping second = map.yamlMapping("second");
        MatcherAssert.assertThat(second, Matchers.notNullValue());
        MatcherAssert.assertThat(
            second, Matchers.instanceOf(YamlMapping.class)
        );
        MatcherAssert.assertThat(
            second.string("fifth"), Matchers.equalTo("values")
        );
    }

    /**
     * ReadYamlMapping can return its keys. The keys should
     * be ordered.
     */
    @Test
    public void returnsOrderedKeys(){
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("zkey: somethingElse", 0));
        lines.add(new RtYamlLine("bkey: ", 1));
        lines.add(new RtYamlLine("  fourth: some", 2));
        lines.add(new RtYamlLine("  fifth: values", 3));
        lines.add(new RtYamlLine("akey: something", 4));
        final YamlMapping map = new ReadYamlMapping(new AllYamlLines(lines));
        final Set<YamlNode> keys = map.keys();
        MatcherAssert.assertThat(
            keys, Matchers.not(Matchers.emptyIterable())
        );
        MatcherAssert.assertThat(keys.size(), Matchers.equalTo(3));
        final Iterator<YamlNode> iterator = keys.iterator();
        MatcherAssert.assertThat(
            iterator.next(),
            Matchers.equalTo(new Scalar("akey"))
        );
        MatcherAssert.assertThat(
            iterator.next(),
            Matchers.equalTo(new Scalar("bkey"))
        );
        MatcherAssert.assertThat(
            iterator.next(),
            Matchers.equalTo(new Scalar("zkey"))
        );
    }
    
    /**
     * ReadYamlMapping can return its children.
     */
    @Test
    public void returnsChildrenOfStringKeys(){
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("zkey: somethingElse", 0));
        lines.add(new RtYamlLine("bkey: ", 1));
        lines.add(new RtYamlLine("  bchildkey1: some", 2));
        lines.add(new RtYamlLine("  bchildkey2: values", 3));
        lines.add(new RtYamlLine("akey: something", 4));
        lines.add(new RtYamlLine("ukey: ", 5));
        lines.add(new RtYamlLine("  - seq", 6));
        lines.add(new RtYamlLine("  - value", 7));

        final YamlMapping map = new ReadYamlMapping(new AllYamlLines(lines));
        final Collection<YamlNode> children = map.children();
        MatcherAssert.assertThat(
            children, Matchers.iterableWithSize(4)
        );
        System.out.println(children);
        final Iterator<YamlNode> iterator = children.iterator();
        MatcherAssert.assertThat(
            iterator.next(),
            Matchers.equalTo(new Scalar("something"))
        );
        MatcherAssert.assertThat(
            iterator.next(),
            Matchers.equalTo(
                Yaml.createYamlMappingBuilder()
                    .add("bchildkey1", "some")
                    .add("bchildkey2", "values")
                    .build()
            )
        );
        MatcherAssert.assertThat(
            iterator.next(),
            Matchers.equalTo(
                Yaml.createYamlSequenceBuilder()
                    .add("seq")
                    .add("value")
                    .build()
            )
        );
        MatcherAssert.assertThat(
            iterator.next(),
            Matchers.equalTo(new Scalar("somethingElse"))
        );
    }
    
    /**
     * ReadYamlMapping can return its children.
     * @checkstyle ExecutableStatementCount (100 lines)
     */
    @Test
    public void returnsChildrenOfStringAndComplexKeys(){
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("first: somethingElse", 0));
        lines.add(new RtYamlLine("? ", 1));
        lines.add(new RtYamlLine("  complex1: mapping1", 2));
        lines.add(new RtYamlLine("  complex2: mapping2", 3));
        lines.add(new RtYamlLine(": ", 4));
        lines.add(new RtYamlLine("  map: value", 5));
        lines.add(new RtYamlLine("second: something ", 7));
        lines.add(new RtYamlLine("third: ", 8));
        lines.add(new RtYamlLine("  - singleSeq", 9));
        lines.add(new RtYamlLine("? ", 10));
        lines.add(new RtYamlLine("  - sequence", 11));
        lines.add(new RtYamlLine("  - key", 12));
        lines.add(new RtYamlLine(": simpleValue", 13));

        final YamlMapping map = new ReadYamlMapping(new AllYamlLines(lines));
        final Collection<YamlNode> children = map.children();
        MatcherAssert.assertThat(
            children, Matchers.iterableWithSize(5)
        );
        final Iterator<YamlNode> iterator = children.iterator();
        MatcherAssert.assertThat(
            iterator.next(),
            Matchers.equalTo(new Scalar("somethingElse"))
        );
        MatcherAssert.assertThat(
            iterator.next(),
            Matchers.equalTo(
                Yaml.createYamlMappingBuilder()
                    .add("map", "value")
                    .build()
            )
        );
        MatcherAssert.assertThat(
            iterator.next(),
            Matchers.equalTo(new Scalar("something"))
        );
        MatcherAssert.assertThat(
            iterator.next(),
            Matchers.equalTo(
                Yaml.createYamlSequenceBuilder()
                    .add("singleSeq")
                    .build()
            )
        );
        MatcherAssert.assertThat(
            iterator.next(),
            Matchers.equalTo(new Scalar("simpleValue"))
        );
    }

    /**
     * ReadYamlMapping can return the YamlMapping mapped to a
     * YamlMapping key.
     */
    @Test
    public void returnsYamlMappingWithYamlMappingKey(){
        final YamlMapping key = new RtYamlMappingBuilder()
            .add("complex1", "mapping1").add("complex2", "mapping2").build();
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("first: something", 0));
        lines.add(new RtYamlLine("? ", 1));
        lines.add(new RtYamlLine("  complex1: mapping1", 2));
        lines.add(new RtYamlLine("  complex2: mapping2", 3));
        lines.add(new RtYamlLine(": ", 4));
        lines.add(new RtYamlLine("  map: value", 5));
        lines.add(new RtYamlLine("second: something", 6));
        final YamlMapping map = new ReadYamlMapping(new AllYamlLines(lines));
        final YamlMapping value = map.yamlMapping(key);
        MatcherAssert.assertThat(value, Matchers.notNullValue());
        MatcherAssert.assertThat(
            value, Matchers.instanceOf(YamlMapping.class)
        );
        MatcherAssert.assertThat(
            value.string("map"), Matchers.equalTo("value")
        );
    }

    /**
     * ReadYamlMapping can return the YamlSequence mapped to a
     * String key.
     */
    @Test
    public void returnsYamlSequenceWithStringKey(){
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("first: somethingElse", 0));
        lines.add(new RtYamlLine("second: ", 1));
        lines.add(new RtYamlLine("  - some", 2));
        lines.add(new RtYamlLine("  - sequence", 3));
        lines.add(new RtYamlLine("third: something", 4));
        final YamlMapping map = new ReadYamlMapping(new AllYamlLines(lines));
        final YamlSequence second = map.yamlSequence("second");
        MatcherAssert.assertThat(second, Matchers.notNullValue());
        MatcherAssert.assertThat(
            second, Matchers.instanceOf(YamlSequence.class)
        );
    }

    /**
     * ReadYamlMapping can return the YamlMapping mapped to a
     * YamlMapping key.
     */
    @Test
    public void returnsStringWithYamlMappingKey(){
        final YamlMapping key = new RtYamlMappingBuilder()
            .add("complex1", "mapping1").add("complex2", "mapping2").build();
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("first: something", 0));
        lines.add(new RtYamlLine("? ", 1));
        lines.add(new RtYamlLine("  complex1: mapping1", 2));
        lines.add(new RtYamlLine("  complex2: mapping2", 3));
        lines.add(new RtYamlLine(": value", 4));
        lines.add(new RtYamlLine("second: something", 6));
        final YamlMapping map = new ReadYamlMapping(new AllYamlLines(lines));
        System.out.println(map);
        final String value = map.string(key);
        MatcherAssert.assertThat(value, Matchers.notNullValue());
        MatcherAssert.assertThat(value, Matchers.equalTo("value"));
    }

    /**
     * ReadYamlMapping can return the String mapped to a
     * String key.
     */
    @Test
    public void returnsStringWithStringKey(){
        final List<YamlLine> lines = new ArrayList<>();
        lines.add(new RtYamlLine("first: somethingElse", 0));
        lines.add(new RtYamlLine("second: ", 1));
        lines.add(new RtYamlLine("  - some", 2));
        lines.add(new RtYamlLine("  - sequence", 3));
        lines.add(new RtYamlLine("third: something", 4));
        final YamlMapping map = new ReadYamlMapping(new AllYamlLines(lines));
        System.out.println(map);
        final String third = map.string("third");
        MatcherAssert.assertThat(third, Matchers.notNullValue());
        MatcherAssert.assertThat(
            third, Matchers.equalTo("something")
        );
    }

    /**
     * An empty ReadYamlMapping can be printed.
     * @throws Exception if something goes wrong
     */
    @Test
    public void printsEmptyYaml() throws Exception {
        final YamlMapping map = new ReadYamlMapping(
            new AllYamlLines(new ArrayList<YamlLine>())
        );
        MatcherAssert.assertThat(map.toString(), Matchers.isEmptyString());
    }
}
