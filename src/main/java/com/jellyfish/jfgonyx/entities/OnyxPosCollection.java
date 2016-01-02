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
package com.jellyfish.jfgonyx.entities;

import com.jellyfish.jfgonyx.constants.GraphicsConst;
import java.util.HashMap;
import org.apache.commons.lang3.StringUtils;

/**
 * @author thw
 */
public class OnyxPosCollection {
    
    /**
     * intmap mtx width & height int value = 24.
     */
    public static final int MTX_WH = 24;
    public static final String KEY_FORMAT = "%.1f-%.1f";
    public final HashMap<String, OnyxPos> positions = new HashMap<>();
    
    public void initStartPosition(final OnyxDiamondCollection c) {
        
        for (OnyxDiamond d : c.diamonds.values()) {
            for (OnyxPos p : d.positions) {
                if (!this.positions.containsValue(p)) {
                    this.positions.put(String.format(OnyxPosCollection.KEY_FORMAT, p.x, p.y), p);
                }
            }
        }
    }
    
    public void spawnVirtualPiece(final GraphicsConst.COLOR c) {
        
        this.positions.get(String.format(OnyxPosCollection.KEY_FORMAT, 6f, 6f)).setVirtualPiece(
                new OnyxVirtualPiece(c)
        );
    }
    
    public OnyxPos getPosition(final String k) {
        return this.positions.get(k);
    }
    
    public int[][] getMatrix() {
        
        OnyxPos p = null;
        String k = StringUtils.EMPTY;
        int x = 0, y = 0;
        final int[][] matrix = new int[OnyxPosCollection.MTX_WH][OnyxPosCollection.MTX_WH];
        for (float i = 0f; i < 24f; i += 0.5f) {
            for (float j = 0f; j < 24f; j += 0.5f) {
                k = String.format(OnyxPosCollection.KEY_FORMAT, i, j);
                p = this.positions.get(k);
                matrix[x][y] = p == null ? 2 : p.getPiece().color.bitColor;     
                ++y;
            }
            y = 0;
            ++x;
        }
        
        return matrix;
    }
    
    public OnyxVirtualPiece getVirtualPiece() {
        
        OnyxVirtualPiece vP = null;
        for (OnyxPos p : this.positions.values()) {
            if (p.isVirtuallyOccupied()) {
                vP = (OnyxVirtualPiece) p.getVirtualPiece();
                vP.setTmpOnyxPosition(p);
                return vP;
            }
        }
        
        return null;
    }
    
    public boolean hasVirtualPiece() {
        return this.getVirtualPiece() != null;
    }
    
}