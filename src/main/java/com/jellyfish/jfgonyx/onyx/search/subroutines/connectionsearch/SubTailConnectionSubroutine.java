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

import com.jellyfish.jfgonyx.constants.GraphicsConst;
import com.jellyfish.jfgonyx.constants.OnyxConst;
import com.jellyfish.jfgonyx.onyx.entities.OnyxMove;
import com.jellyfish.jfgonyx.onyx.entities.OnyxPos;
import com.jellyfish.jfgonyx.onyx.entities.collections.OnyxPosCollection;
import com.jellyfish.jfgonyx.ui.OnyxBoard;

/**
 * Find sub tails meaning tails that do not start on borders.
 * @author thw
 */
public class SubTailConnectionSubroutine extends TailConnectionSubroutine {

    public SubTailConnectionSubroutine(final OnyxPosCollection c, final GraphicsConst.COLOR color, 
            final OnyxBoard board) {
        super(c, color, board);
    }
    
    @Override
    protected final void score() {

        this.candidate = new OnyxMove(this.getCounterPos(this.tail), 
                ((float) (this.links * 10)) * OnyxConst.SCORE.SUB_TAIL.getValue());
        this.candidates.add(candidate);
    }
    
    public OnyxPos getCounterPos(final OnyxPos p) {
        
        /**
         * FIXME : tot tired to finish - counter sub tails by trimming on x OR y... 
         */
        
        for (String k : p.connections) {
            
            if (!this.c.getPosition(k).isOccupied()) {
                
                if ((this.color.bool && c.getPosition(k).x == this.c.getPosition(k).x) || 
                        (!this.color.bool && c.getPosition(k).y == this.c.getPosition(k).y)) {
                    return c.getPosition(k);
                }
                
                return c.getPosition(k);
            }
        }
        
        return null;
    }
    
}

