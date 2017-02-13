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

/**
 * Iterable yaml lines.
 * @author Mihai Andronache (amihaiemil@gmail.com)
 * @version $Id: 347c9a14f6bf5d3cf6c6c6e8d76a80ee062373f8 $
 * @since 1.0.0
 * @todo #52:1h Implement and unit test this interface. It should
 *  be named RtYamlLines and be a default-accessible class.
 */
interface YamlLines extends Iterable<YamlLine> {

    /**
     * Lines which are contained within the current YamlLine (lines which are 
     * <br> indented by 2 or more spaces beneath it).
     * @return YamlLines
     */
    YamlLines contained();

    /**
     * Turn these lines into a Yaml mapping.
     * @return YamlMapping.
     */
    YamlMapping toYamlMapping();

    /**
     * Turn these lines into a Yaml sequence.
     * @return YamlSequence.
     */
    YamlSequence toYamlSequence();
    
    
}
