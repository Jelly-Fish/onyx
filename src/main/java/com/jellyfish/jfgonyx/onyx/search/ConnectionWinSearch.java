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
package com.jellyfish.jfgonyx.onyx.search;

import com.jellyfish.jfgonyx.constants.GraphicsConst;
import com.jellyfish.jfgonyx.constants.OnyxConst;
import com.jellyfish.jfgonyx.onyx.entities.OnyxPos;
import com.jellyfish.jfgonyx.onyx.entities.collections.OnyxPosCollection;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author thw
 */
class ConnectionWinSearch {
    
    private final OnyxPosCollection c;
    private final GraphicsConst.COLOR color;
    private final OnyxPos startPos;
    private final int max = OnyxConst.BOARD_SIDE_SQUARE_COUNT + 1;
    private final List<String> checkedKeys = new ArrayList<>();

    ConnectionWinSearch(final OnyxPosCollection c, final GraphicsConst.COLOR color, 
            final OnyxPos startPos) {
        this.c = c;
        this.color = color;
        this.startPos = startPos;
    }
    
    boolean hasFullConnection(final OnyxPos p) {
        
        if (!p.isOccupied() || p.getPiece().color.bitColor != this.color.bitColor) return false;
        
        /**
         * FIXME : to tired to finish. 
         *       
        for (String k : p.connections) {
            if (c.getPosition(k).isOccupied() || c.getPosition(k).getPiece().color.bitColor != this.color.bitColor) {
                this.checkedKeys.add(k);
                continue;
            }
            if (!this.checkedKeys.contains(k) && c.getPosition(k).isOccupied() && 
                c.getPosition(k).getPiece().color.bitColor == this.color.bitColor) {
                if (hasFullConnection(c.getPosition(k))) {
                    
                } else {
                    this.checkedKeys.add(k);
                }
            }
        }
         */
        
        return false;
    }
    
    private String hasConnection(final OnyxPos p) {
        
        for (String k : p.connections) {
            if (c.getPosition(k).isOccupied() || c.getPosition(k).getPiece().color.bitColor == this.color.bitColor) {
                this.checkedKeys.add(k);
                return k;
            }
        }
        
        return null;
    }
    
}
