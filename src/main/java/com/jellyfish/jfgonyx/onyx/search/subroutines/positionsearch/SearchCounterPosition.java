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
package com.jellyfish.jfgonyx.onyx.search.subroutines.positionsearch;

import com.jellyfish.jfgonyx.constants.OnyxConst;
import com.jellyfish.jfgonyx.onyx.entities.OnyxDiamond;
import com.jellyfish.jfgonyx.onyx.entities.OnyxMove;
import com.jellyfish.jfgonyx.onyx.entities.OnyxPos;
import com.jellyfish.jfgonyx.onyx.entities.collections.OnyxPosCollection;
import com.jellyfish.jfgonyx.onyx.search.subroutines.abstractions.AbstractSubroutine;
import com.jellyfish.jfgonyx.ui.OnyxBoard;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author thw
 */
public class SearchCounterPosition extends AbstractSubroutine {
    
    private final static String BEST_CANDIDATE = " :: Counter position [%s]";
    
    /**
     * @param c Onyx position collection.
     * @param b Onyx board instance.
     * @param bitColor the color to play's bit value (0=white, 1=black).
     * @return Strongest counter move found to prevent sealing & take positions
     * or NULL if no such position has been found.
     */
    public final OnyxMove getCounterPos(final OnyxPosCollection c, final OnyxBoard b, final int bitColor) {

        /**
         * FIXME : improve this, re-evaluate score value.
         * counter pos must also block tail progressions.
         */
        int i, j;
        OnyxPos pos = null;
        String key = StringUtils.EMPTY;
        for (OnyxDiamond d : b.getDiamondCollection().getDiamonds().values()) {
            
            i = 0; j = 0;
            for (String k : d.getCornerKeys()) {
                pos = c.getPosition(k);
                if (pos.isOccupied()) {
                    if (pos.getPiece().color.bitColor == bitColor) {
                        ++i;
                    } else if (pos.getPiece().color.bitColor != bitColor) {
                        ++j;
                    }
                } else {
                    key = k;
                }
            }
            
            if (i == 2 && j == 1 && !c.getPosition(key).isOccupied()) {
                move = new OnyxMove(c.getPosition(key), OnyxConst.SCORE.COUNTER_POS.getValue());
            }
        }
        
        if (move != null) print(OnyxConst.POS_MAP.get(move.getPos().getKey()), BEST_CANDIDATE);
        
        return move;
    }
    
}
