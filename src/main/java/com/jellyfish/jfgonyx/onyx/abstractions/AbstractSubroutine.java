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
package com.jellyfish.jfgonyx.onyx.abstractions;

import com.jellyfish.jfgonyx.onyx.entities.OnyxMove;

/**
 *
 * @author thw
 */
public abstract class AbstractSubroutine extends AbstractOnyxSearch {

    public final static String CANDIDATE_TAIL_FORMAT = "Candidate for %s %s start @ %s : [%s] score: %f";
    public final static String BEST_CANDIDATE_TAIL_FORMAT = "Candidate for %s %s start @ %s : [%s] score: %f";
    public final static String BEST_CANDIDATE_COUNTER_FORMAT = "Candidate for %s %s : [%s] score: %f";
    public final static String BEST_CANDIDATE = "Candidate for %s %s : [%s]";
    public final static String BEST_TAKE_CANDIDATE = "Candidate for %s %s : [%s] score %f";
    public final static String VTAIL_CANDIDATE_FORMAT = "VXNX tail for %s %s lenght [%s] : <br />[%s]";
    public final static String VTAIL_CANDIDATE_RES = "Virtual tail result for %s %s tail lenght [%s] : <br />[%s]";
    
    public static enum SUBROUTINE_TYPE {
    
        TAIL("{tail search}"),
        TAILS("{tails search}"),
        VCNX("{virtual connection search}"),
        COUNTER_SUBTAIL("{sub-tail search}"),
        CENTER_POS("{center position search}"),
        COUNTER_POS("{counter position search}"),
        TAKE("{capture position search}"),
        NEIGHBOUR("{neighbour position search}"),
        ATTACK("{attack position search}");
        
        private final String desc;
        
        SUBROUTINE_TYPE(final String desc) {
            this.desc = desc;
        }
        
        public String getDesc() {
            return this.desc;
        }
        
    }
    
    /**
     * Search resulat as Onyx move istance.
     * @see OnyxMove
     */
    protected OnyxMove move = null;
    
}
