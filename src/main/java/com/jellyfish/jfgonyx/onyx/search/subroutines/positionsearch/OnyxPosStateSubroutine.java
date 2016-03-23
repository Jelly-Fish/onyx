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

import com.jellyfish.jfgonyx.constants.GraphicsConst;
import com.jellyfish.jfgonyx.onyx.entities.OnyxDiamond;
import com.jellyfish.jfgonyx.onyx.entities.OnyxPos;
import com.jellyfish.jfgonyx.onyx.entities.collections.OnyxPosCollection;
import com.jellyfish.jfgonyx.onyx.search.subroutines.abstractions.AbstractSubroutine;
import com.jellyfish.jfgonyx.ui.OnyxBoard;

/**
 * @author thw
 */
public class OnyxPosStateSubroutine extends AbstractSubroutine {
    
    private final OnyxPos pos;

    public OnyxPosStateSubroutine(final OnyxPos pos) {
        this.pos = pos;
    }
    
    /**
     * Is this position, if played occupied/played, will result in a take for
     * oponent Color - oposite to isSubjectToTake search. 
     * @param board Onyx board instance.
     * @param c onyx position collection instance.
     * @param color the color that is potentially will enable a take move if played.
     * @return true if position will enable take for oponent.
     */
    public final boolean willEnableTake(final OnyxBoard board, final OnyxPosCollection c, 
            final GraphicsConst.COLOR color) {
        
        /**
         * FIXME : shitty, fucks up counter position search, setting oponent
         * take moves are not prohibited.
         */
        
        if (this.pos.isDiamondCenter()) return false;
        
        final GraphicsConst.COLOR oC = GraphicsConst.COLOR.getOposite(color.bool);
        String[] keys = null;
        int k = -1, l = 0, j = 0, m = -1; 

        for (OnyxDiamond d : board.getDiamondCollection().getDiamondsByPosKey(this.pos.getKey())) {

            keys = d.getCornerKeys();
            for (int i = 0; i < keys.length; ++i) {
                
                if (this.pos.getKey().equals(c.getPosition(keys[i]).getKey()) &&
                        !this.pos.isOccupied()) {
                    m = i;
                } else {
                    
                    if (c.getPosition(keys[i]).isOccupied() && 
                            c.getPosition(keys[i]).getPiece().color.bit == oC.bit) {
                        ++l;
                    }

                    if (c.getPosition(keys[i]).isOccupied(color.bit) && 
                            c.getPosition(keys[i]).getPiece().color.bit == color.bit) {
                        ++j;
                        k = i;
                    }
                }
            }

            if (l == 1 && j == 1 && k > -1 && m > -1 && m != k) {
                if ((k == 0 && m == 2) || (k == 1 && m == 3) || (k == 2 && m == 0) ||
                        (k == 3 && m == 1)) {
                    return true;
                }
            }

            k = -1; l = 0; j = 0; m = -1;
        }

        return false;
    }
    
    /**
     * Is this position, if not occupied/played, will result in a take for
     * oponent Color.
     * @param board Onyx board instance.
     * @param c onyx position collection instance.
     * @param color the color that is potentially subject to take move.
     * @return true if position is subject to take.
     */
    public final boolean isSubjectToTake(final OnyxBoard board, final OnyxPosCollection c, 
            final GraphicsConst.COLOR color) {
        
        /**
         * FIXME : shitty, fucks up - Does not always block take positions by
         * oponent - must block 100% of possible take situations for oponent.
         */
        
        if (this.pos.isDiamondCenter()) return false;
        
        final GraphicsConst.COLOR oC = GraphicsConst.COLOR.getOposite(color.bool);
        String[] keys = null;
        int k = -1, l = 0, j = 0, m = -1; 

        for (OnyxDiamond d : board.getDiamondCollection().getDiamondsByPosKey(this.pos.getKey())) {

            keys = d.getCornerKeys();
            for (int i = 0; i < keys.length; ++i) {
                
                if (this.pos.getKey().equals(c.getPosition(keys[i]).getKey()) &&
                        !this.pos.isOccupied()) {
                    m = i;
                } else {
                    
                    if (c.getPosition(keys[i]).isOccupied() && 
                            c.getPosition(keys[i]).getPiece().color.bit == oC.bit &&
                            !this.pos.getKey().equals(c.getPosition(keys[i]).getKey())) {
                        ++l;
                    } 

                    if (c.getPosition(keys[i]).isOccupied(color.bit) && 
                            c.getPosition(keys[i]).getPiece().color.bit == color.bit &&
                            !this.pos.getKey().equals(c.getPosition(keys[i]).getKey())) {
                        ++j;
                        k = i;
                    }
                }
            }

            if (l == 2 && j == 1 && k > -1 && m > -1 && m != k) {
                if ((k == 0 && m == 2) || (k == 1 && m == 3) || (k == 2 && m == 0) ||
                        (k == 3 && m == 1)) {
                    return true;
                }
            }

            k = -1; l = 0; j = 0; m = -1;
        }

        return false;
    }
    
}
