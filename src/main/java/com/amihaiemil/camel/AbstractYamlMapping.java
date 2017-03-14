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

import java.util.Iterator;
import java.util.Set;

/**
 * AbstractYamlMapping implementing methods which should be the same across 
 * all final implementations of YamlMapping.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id$
 * @since 1.0.0
 */
abstract class AbstractYamlMapping implements YamlMapping{

    /**
     * Return the keys' set of this mapping.
     * @return Set of YamlNode keys.
     */
    abstract Set<YamlNode> keys();

    @Override
    public int hashCode() {
        int hash = 0;
        for(final YamlNode key : this.keys()) {
            hash += key.hashCode();
        }
        for(final YamlNode value : this.children()) {
            hash += value.hashCode();
        }
        return hash;
    }

    /**
     * Equals method for YamlMapping. It returns true if the compareTo(...)
     * method returns 0.
     * @param other The YamlMapping to which this is compared.
     * @return True or false.
     */
    @Override
    public boolean equals(final Object other) {
        final boolean result;
        if (other == null || !(other instanceof YamlMapping)) {
            result = false;
        } else if (this == other) {
            result = true;
        } else {
            result = this.compareTo((YamlMapping) other) == 0;
        }
        return result;
    }

    /**
     * Compare this Mapping to another node.<br><br>
     * 
     * A Mapping is always considered greater than a Scalar or a Sequence.<br>
     * 
     * If o is a Mapping, their integer lengths are compared - the one with
     * the greater length is considered greater. If the lengths are equal,
     * then the 2 Mappings are equal if all elements are equal (K==K and V==V).
     * If the elements are not identical, the comparison of the first unequal
     * elements is returned.
     * 
     * @param other The other AbstractNode.
     * @checkstyle NestedIfDepth (100 lines)
     * @checkstyle ExecutableStatementCount (100 lines)
     * @return
     *  a value < 0 if this < o <br>
     *   0 if this == o or <br>
     *  a value > 0 if this > o
     */
    @Override
    public int compareTo(final YamlNode other) {
        int result = 0;
        if (other == null || !(other instanceof YamlMapping)) {
            result = 1;
        } else if (this != other) {
            final AbstractYamlMapping map = (AbstractYamlMapping) other;
            final Set<YamlNode> keys = this.keys();
            final Set<YamlNode> otherKeys = map.keys();
            if(keys.size() > otherKeys.size()) {
                result = 1;
            } else if (keys.size() < otherKeys.size()) {
                result = -1;
            } else {
                final Iterator<YamlNode> keysIt = keys.iterator();
                final Iterator<YamlNode> otherKeysIt = otherKeys.iterator();
                final Iterator<YamlNode> values = this.children().iterator();
                final Iterator<YamlNode> otherVals = map.children().iterator();
                int keysComparison;
                int valuesComparison;
                while(values.hasNext()) {
                    keysComparison = keysIt.next()
                        .compareTo(otherKeysIt.next());
                    valuesComparison = values.next()
                        .compareTo(otherVals.next());
                    if(keysComparison != 0) {
                        result = keysComparison;
                        break;
                    }
                    if(valuesComparison != 0) {
                        result = valuesComparison;
                        break;
                    }
                }
            }
        }
        return result;
    }

}
