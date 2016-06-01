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
package com.jellyfish.jfgonyx.onyx.search.subroutines.connectionsearch;

import com.jellyfish.jfgonyx.onyx.entities.OnyxPos;
import com.jellyfish.jfgonyx.onyx.entities.OnyxTail;
import java.util.ArrayList;
import java.util.List;

/**
 * @author thw
 */
class VirtualConnectionTailTrimSubroutine {
    
    private final OnyxTail tail;
    private final List<OnyxPos> discarded = new ArrayList<>();

    VirtualConnectionTailTrimSubroutine(final OnyxTail t) {
        this.tail = t;
    }
    
    OnyxTail trim() {
        this.trimTail();
        return this.tail;
    }
    
    /**
     * Remove/trim all position that are dead sub-tail affluents - mainstream 
     * tail must not contain any sub-tail excrescences/growths inherited from
     * left to right hesitations be main algo buildTail method.
     * @param t OnyxTail to trim.
     */
    private void trimTail() {

        int count = 0;
        boolean trimed = false;
        
        for (OnyxPos p : this.tail.getPositions()) {
            if (this.tail.isTailEnd(p.getKey()) || this.tail.isTailStart(p.getKey())) continue;
            for (String k : p.connections) if (this.tail.contains(k)) ++count;
            if (count < 2) {
                this.discarded.add(p);
                trimed = true;
            }
            count = 0;
        }
        
        for (OnyxPos p : this.discarded) this.tail.getPositions().remove(p);
        
        if (trimed) this.trimTail();
    }
    
}
