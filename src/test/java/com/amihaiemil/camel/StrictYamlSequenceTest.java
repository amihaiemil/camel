/**
 * Copyright (c) 2016-2017, Mihai Emil Andronache
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
package com.amihaiemil.camel;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.LinkedList;
import java.util.List;

/**
 * Unit tests for {@link StrictYamlSequence}.
 * @author Salavat.Yalalov (s.yalalov@gmail.com)
 * @version $Id$
 * @since 1.0.0
 */
public final class StrictYamlSequenceTest {
    /**
     * StrictYamlSequence can fetch its children.
     */
    @Test
    public void fetchesChildren() {
        List<YamlNode> elements = new LinkedList<>();
        elements.add(new Scalar("key1"));
        elements.add(new Scalar("key2"));
        elements.add(new Scalar("key3"));
        YamlSequence sequence = new StrictYamlSequence(
            new RtYamlSequence(elements)
        );

        MatcherAssert.assertThat(
            sequence.children().size(),
            Matchers.equalTo(3)
        );
    }

    /**
     * StringYamlSequence can throw YamlNodeNotFoundException
     * when the demanded YamlMapping is not found.
     */
    @Test (expected = YamlNodeNotFoundException.class)
    public void exceptionOnNullMapping() {
        YamlSequence origin = Mockito.mock(YamlSequence.class);
        Mockito.when(origin.yamlMapping(1)).thenReturn(null);
        YamlSequence strict = new StrictYamlSequence(origin);
        strict.yamlMapping(1);
    }

    /**
     * StringYamlSequence can throw YamlNodeNotFoundException
     * when the demanded YamlSequence is not found.
     */
    @Test (expected = YamlNodeNotFoundException.class)
    public void exceptionOnNullSequence() {
        YamlSequence origin = Mockito.mock(YamlSequence.class);
        Mockito.when(origin.yamlSequence(1)).thenReturn(null);
        YamlSequence strict = new StrictYamlSequence(origin);
        strict.yamlSequence(1);
    }

    /**
     * StringYamlSequence can throw YamlNodeNotFoundException
     * when the demanded String is not found.
     */
    @Test (expected = YamlNodeNotFoundException.class)
    public void exceptionOnNullString() {
        YamlSequence origin = Mockito.mock(YamlSequence.class);
        Mockito.when(origin.string(1)).thenReturn(null);
        YamlSequence strict = new StrictYamlSequence(origin);
        strict.string(1);
    }

    /**
     * StrictYamlSequence can return the number of elements in a sequence.
     */
    @Test
    public void returnsSize() {
        List<YamlNode> elements = new LinkedList<>();
        elements.add(new Scalar("key1"));
        elements.add(new Scalar("key2"));
        YamlSequence sequence = new StrictYamlSequence(
            new RtYamlSequence(elements)
        );

        MatcherAssert.assertThat(
            sequence.size(),
            Matchers.equalTo(2)
        );
    }

    /**
     * StringYamlSequence can fetch a YamlMapping based in its index.
     */
    @Test
    public void returnsMapping() {
        YamlSequence origin = Mockito.mock(YamlSequence.class);
        YamlMapping found = Mockito.mock(YamlMapping.class);
        Mockito.when(origin.yamlMapping(1)).thenReturn(found);
        YamlSequence strict = new StrictYamlSequence(origin);
        MatcherAssert.assertThat(
            strict.yamlMapping(1), Matchers.equalTo(found)
        );
    }

    /**
     * StringYamlSequence can fetch a YamlSequence based in its index.
     */
    @Test
    public void returnsSequence() {
        YamlSequence origin = Mockito.mock(YamlSequence.class);
        YamlSequence found = Mockito.mock(YamlSequence.class);
        Mockito.when(origin.yamlSequence(1)).thenReturn(found);
        YamlSequence strict = new StrictYamlSequence(origin);
        MatcherAssert.assertThat(
            strict.yamlSequence(1), Matchers.equalTo(found)
        );
    }

    /**
     * StringYamlSequence can fetch a String based in its index.
     */
    @Test
    public void returnsString() {
        YamlSequence origin = Mockito.mock(YamlSequence.class);
        Mockito.when(origin.string(1)).thenReturn("found");
        YamlSequence strict = new StrictYamlSequence(origin);
        MatcherAssert.assertThat(
                strict.string(1), Matchers.equalTo("found")
        );
    }

}
