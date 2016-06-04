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

import com.jellyfish.jfgonyx.onyx.constants.OnyxConst;
import com.jellyfish.jfgonyx.onyx.entities.OnyxMove;
import com.jellyfish.jfgonyx.onyx.entities.OnyxPos;
import com.jellyfish.jfgonyx.onyx.entities.collections.OnyxPosCollection;
import com.jellyfish.jfgonyx.onyx.abstractions.AbstractSubroutine;
import com.jellyfish.jfgonyx.onyx.search.searchutils.OnyxPositionUtils;
import com.jellyfish.jfgonyx.ui.OnyxBoard;

/**
 * Find sub tails meaning tails that do not start on borders.
 * @author thw
 */
public class SubTailConnectionSubroutine extends TailConnectionSubroutine {

    /**
     * Minimum and maximum x/y values of sub bourders.
     * @see OnyxPositionUtils class getSubBordersByColor method.
     */
    private final float minX, minY, maxX, maxY;    
    
    public SubTailConnectionSubroutine(final OnyxPosCollection c, final OnyxConst.COLOR color, 
            final OnyxBoard board, final float minX, final float minY, final float maxX, final float maxY) {
        super(c, color, board);
        this.type = AbstractSubroutine.SUBROUTINE_TYPE.COUNTER_SUBTAIL;
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;
    }

    @Override
    protected final void score() {
        
        final OnyxPos p = getCounterPos(tail);
        candidate = p != null ? 
                new OnyxMove(p, ((float) links) * OnyxConst.SCORE.SUB_TAIL.getValue()) : null;
        candidates.add(candidate);
    }
    
    private OnyxPos getCounterPos(final OnyxPos t) {
        
        if (t == null) return t;
        OnyxPos tmp = null;
        
        for (String k : t.connections) {
            
            tmp = c.getPosition(k);
            
            if (!tmp.isOccupied() && !tmp.isDiamondCenter()) {
                
                if ((color.bool && isAtTailEnd(tmp) && t.y == tmp.y) || 
                    (!color.bool && isAtTailEnd(tmp) && t.x == tmp.x)) {
                    return tmp;
                }
            }
        }
        
        return null;
    }
    
    private boolean isAtTailEnd(final OnyxPos t) {
        
        /**
         * Avoid returning sub tail counter positions that are between
         * max/min tail/start position of the sub tail (depending on color 
         * and start position + found tail posiion).
         */
        
        if (color.bool) {
            return t.x <= minX || t.x >= maxX;
        } else if (!color.bool) {
            return t.y <= minY || t.y >= maxY;
        }
        
        return false;
    }
    
}

