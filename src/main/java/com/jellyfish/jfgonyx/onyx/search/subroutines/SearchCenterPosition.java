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
package com.jellyfish.jfgonyx.onyx.search.subroutines;

import com.jellyfish.jfgonyx.constants.OnyxConst;
import com.jellyfish.jfgonyx.onyx.entities.OnyxDiamond;
import com.jellyfish.jfgonyx.onyx.entities.OnyxMove;
import com.jellyfish.jfgonyx.onyx.entities.collections.OnyxPosCollection;
import com.jellyfish.jfgonyx.onyx.exceptions.InvalidOnyxPositionException;
import com.jellyfish.jfgonyx.ui.OnyxBoard;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author thw
 */
public class SearchCenterPosition {
    
    /**
     * Get playable diamond center position nearest to board center.
     * @param c position collection.
     * @param b onyx board.
     * @return Best OnyxMove instance or null.
     * @throws com.jellyfish.jfgonyx.onyx.exceptions.InvalidOnyxPositionException if a position is invalid.
     */
    public static OnyxMove getCenterPos(final OnyxPosCollection c, final OnyxBoard b) throws InvalidOnyxPositionException {

        final float center = (OnyxConst.BOARD_SIDE_SQUARE_COUNT + 1) / 2f;
        OnyxDiamond tmp = null;
        final Set<OnyxDiamond> d = new HashSet<>();
        int i = 0;
        
        for (OnyxDiamond dmd : b.getDiamondCollection().getDiamonds().values()) {
            
            if (dmd.isFivePosDiamond()) {
                
                i = 0;
                for (String k : dmd.getAllKeys()) {
                    if (!c.getPosition(k).isOccupied()) ++i;
                }
                
                if (i == 5) d.add(dmd);
            }
        }
        
        /**
         * FIXME : fix this, tired after work...
         */
        
        float x, y;
        for (OnyxDiamond dmd : d) {
            if (tmp == null) tmp = dmd;
            x = +(dmd.getCenterPos().x - center);
            y = +(dmd.getCenterPos().y - center);
            if (x > center && x < tmp.getCenterPos().x || y > center && y < tmp.getCenterPos().y) {
                tmp = dmd;
            }
        }
        
        if (tmp == null) return null;
        
        //return new OnyxMove(tmp.getCenterPos(), OnyxConst.SCORE.CENTER.getValue());
        return null;
    }
    
}
