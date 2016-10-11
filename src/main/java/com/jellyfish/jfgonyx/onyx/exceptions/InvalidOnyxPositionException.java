/**
 * *****************************************************************************
 * Copyright (c) 2015, 2016, 2017, Thomas.H Warner. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its contributors
 * may be used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE. 
 ******************************************************************************
 */
package com.jellyfish.jfgonyx.onyx.exceptions;

import com.jellyfish.jfgonyx.onyx.vars.OnyxCommonVars;

/**
 *
 * @author thw
 */
public class InvalidOnyxPositionException extends OnyxException {

    public static final String MSG_STRING_ARGS = "Onyx position %s-%s is invalid.";
    public static final String MSG_FLOAT_ARGS = "Onyx position %f-%f is invalid.";
    public static final String INVALID_CENTER_POS = "Onyx diamond %s does not include a center position.";
    public static final String DEFAULT_MSG = "Position throws InvalidOnyxPositionException for color %s."; 
    public static final String MSG = 
        String.format("invalid onyx position - format must be <n,nf-n,nf> where n >= 1 and n <= %d, %s", 
            OnyxCommonVars.getInstance().BOARD_SIDE_SQUARE_COUNT + 1, "center positions use n,5f notation."); 
    
    public InvalidOnyxPositionException() { }

    public InvalidOnyxPositionException(final String message) {
        super(message);
    }
    
}
