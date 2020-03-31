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

import com.amihaiemil.eoyaml.exceptions.YamlReadingException;
import java.util.*;

/**
 * YamlMapping read from somewhere. YAML directives and
 * document start/end markers are ignored. This is assumed
 * to be a plain YAML mapping.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 1.0.0
 */
final class ReadYamlMapping extends BaseYamlMapping {

    /**
     * Lines read.
     */
    private YamlLines lines;

    /**
     * Ctor.
     * @param lines Given lines.
     */
    ReadYamlMapping(final AllYamlLines lines) {
        this.lines = new SameIndentationLevel(
            new WellIndented(
                new NoDirectivesOrMarkers(
                    lines
                )
            )
        );
    }

    @Override
    public Collection<YamlNode> values() {
        final List<YamlNode> values = new LinkedList<>();
        for(final YamlNode key : this.keys()) {
            values.add(this.value(key));
        }
        return values;
    }

    @Override
    public YamlMapping yamlMapping(final YamlNode key) {
        final YamlMapping found;
        if(key instanceof Scalar) {
            found = this.yamlMapping(((Scalar) key).value());
        } else {
            final YamlNode value = this.valueOfNodeKey(key);
            if(value instanceof YamlMapping) {
                found = (YamlMapping) value;
            } else {
                found = null;
            }
        }
        return found;
    }

    @Override
    public YamlMapping yamlMapping(final String key) {
        final YamlMapping found;
        final YamlNode value = this.valueOfStringKey(key);
        if(value instanceof YamlMapping) {
            found = (YamlMapping) value;
        } else {
            found = null;
        }
        return found;
    }

    @Override
    public YamlSequence yamlSequence(final YamlNode key) {
        final YamlSequence found;
        if(key instanceof Scalar) {
            found = this.yamlSequence(((Scalar) key).value());
        } else {
            final YamlNode value = this.valueOfNodeKey(key);
            if(value instanceof YamlSequence) {
                found = (YamlSequence) value;
            } else {
                found = null;
            }
        }
        return found;
    }

    @Override
    public YamlSequence yamlSequence(final String key) {
        final YamlSequence found;
        final YamlNode value = this.valueOfStringKey(key);
        if(value instanceof YamlSequence) {
            found = (YamlSequence) value;
        } else {
            found = null;
        }
        return found;
    }

    @Override
    public String string(final YamlNode key) {
        String value = null;
        if(key instanceof Scalar) {
            value = this.string(((Scalar) key).value());
        } else {
            boolean foundComplexKey = false;
            for (final YamlLine line : this.lines) {
                final String trimmed = line.trimmed();
                if("?".equals(trimmed)) {
                    final YamlNode keyNode = this.lines.nested(line.number())
                            .toYamlNode(line);
                    if(keyNode.equals(key)) {
                        foundComplexKey = true;
                        continue;
                    }
                }
                if(foundComplexKey) {
                    if(trimmed.endsWith(":")) {
                        break;
                    }
                    if(trimmed.startsWith(":")) {
                        value = new ReadPlainScalar(line).value();
                        break;
                    }
                }
            }
        }
        return value;
    }

    @Override
    public String string(final String key) {
        String value = null;
        for (final YamlLine line : this.lines) {
            final String trimmed = line.trimmed();
            if(trimmed.endsWith(key + ":")) {
                continue;
            }
            if(trimmed.startsWith(key + ":")) {
                value = new ReadPlainScalar(line).value();
            }
        }
        return value;
    }

    /**
     * The YamlNode value associated with a String (scalar) key.
     * @param key String key.
     * @return YamlNode.
     */
    private YamlNode valueOfStringKey(final String key) {
        YamlNode value = null;
        for (final YamlLine line : this.lines) {
            final String trimmed = line.trimmed();
            if(trimmed.endsWith(key + ":")) {
                value = this.lines.nested(line.number()).toYamlNode(line);
            }
        }
        return value;
    }

    /**
     * The YamlNode value associated with a YamlNode key
     * (a "complex" key starting with '?').
     * @param key YamlNode key.
     * @return YamlNode.
     */
    private YamlNode valueOfNodeKey(final YamlNode key) {
        YamlNode value = null;
        for (final YamlLine line : this.lines) {
            final String trimmed = line.trimmed();
            if("?".equals(trimmed)) {
                final YamlLines keyLines = this.lines.nested(
                    line.number()
                );
                final YamlNode keyNode = keyLines.toYamlNode(line);
                if(keyNode.equals(key)) {
                    final YamlLine colonLine = this.lines.line(
                        line.number() + keyLines.lines().size() + 1
                    );
                    if(":".equals(colonLine.trimmed())) {
                        value = this.lines.nested(colonLine.number())
                                    .toYamlNode(colonLine);
                    }
                }
            }
        }
        return value;
    }

    @Override
    public Set<YamlNode> keys() {
        final Set<YamlNode> keys = new TreeSet<>();
        for (final YamlLine line : this.lines) {
            final String trimmed = line.trimmed();
            if(trimmed.startsWith(":")) {
                continue;
            } else if ("?".equals(trimmed)) {
                keys.add(this.lines.nested(line.number()).toYamlNode(line));
            } else {
                if(!trimmed.contains(":")) {
                    throw new YamlReadingException(
                        "Expected scalar key on line " 
                        + (line.number() + 1) + "."
                        + " The line should have the format " 
                        + "'key: value' or 'key:'. "
                        + "Instead, the line is: "
                        + "[" + line.trimmed() + "]."
                    );
                }
                final String key = trimmed.substring(
                        0, trimmed.indexOf(":")).trim();
                if(!key.isEmpty()) {
                    keys.add(new PlainStringScalar(key));
                }
            }
        }
        return keys;
    }

    @Override
    public YamlNode value(final YamlNode key) {
        YamlNode value = this.yamlMapping(key);
        if(value == null) {
            value = this.yamlSequence(key);
            if(value == null) {
                final String val = this.string(key);
                if(val != null) {
                    value = new PlainStringScalar(val);
                }
            }
        }
        return value;
    }
}
