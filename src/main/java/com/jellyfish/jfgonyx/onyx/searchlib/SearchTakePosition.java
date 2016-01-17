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
package com.jellyfish.jfgonyx.onyx.searchlib;

import com.jellyfish.jfgonyx.onyx.entities.OnyxDiamond;
import com.jellyfish.jfgonyx.onyx.entities.OnyxPos;
import com.jellyfish.jfgonyx.onyx.entities.OnyxPosCollection;
import com.jellyfish.jfgonyx.onyx.exceptions.InvalidOnyxPositionException;
import com.jellyfish.jfgonyx.ui.OnyxBoard;
import java.util.ArrayList;
import java.util.List;

/**
 * @author thw
 */
public class SearchTakePosition {
    
    /**
     * @param c Onyx position collection.
     * @param b Onyx board instance.
     * @param bitColor the color to play's bit value (0=white, 1=black).
     * @return Strongest take move key found or NULL if no such position has been found.
     * @throws com.jellyfish.jfgonyx.onyx.exceptions.InvalidOnyxPositionException
     */
    public static String getTakePos(final OnyxPosCollection c, final OnyxBoard b, final int bitColor) throws InvalidOnyxPositionException {
        
        int count, i;
        OnyxPos[] positions = new OnyxPos[4];
        final List<OnyxPos> posSet = new ArrayList<>();
        
        for (OnyxDiamond d : b.getDiamondCollection().getDiamonds().values()) {
            
            if (d.isFivePosDiamond() && c.getPosition(d.getCenterPos().getKey()).isOccupied()) {
                continue;
            }
            
            count = 0;
            i = 0;
            for (String k : d.getCornerKeys()) {
                positions[i] = c.getPosition(k);
                count = positions[i].isOccupied() ? ++count : count;
                ++i;
            }
            
            if (positions[0].isOccupied() && positions[2].isOccupied() &&
                    positions[0].getPiece().color.bitColor != bitColor && 
                    positions[2].getPiece().color.bitColor != bitColor) { 
                
                if (positions[3].isOccupied() && positions[3].getPiece().color.bitColor == bitColor &&
                        !positions[1].isOccupied()) {
                    posSet.add(positions[1]);
                } else if (positions[1].isOccupied() && positions[1].getPiece().color.bitColor == bitColor &&
                        !positions[3].isOccupied()) {
                    posSet.add(positions[3]);
                }
            } else if (positions[1].isOccupied() && positions[3].isOccupied() &&
                    positions[1].getPiece().color.bitColor != bitColor && 
                    positions[3].getPiece().color.bitColor != bitColor) {
                
                if (positions[2].isOccupied() && positions[2].getPiece().color.bitColor == bitColor &&
                        !positions[0].isOccupied()) {
                    posSet.add(positions[0]);
                } else if (positions[0].isOccupied() && positions[0].getPiece().color.bitColor == bitColor &&
                        !positions[2].isOccupied()) {
                    posSet.add(positions[2]);
                }
            }
        }
        
        if (posSet.size() <= 0) return null;
        if (posSet.size() == 1) return posSet.get(0).getKey();
        
        count = 0;
        i = -1;
        int tmpCount = 0;
        for (OnyxPos p : posSet) {
            tmpCount = p.occursCount(p, posSet);
            if (tmpCount > count) {
                count = tmpCount;
                i = posSet.indexOf(p);
            }            
        }
        
        if (i > -1) return posSet.get(i).getKey();
        
        return null;
    }
    
}
