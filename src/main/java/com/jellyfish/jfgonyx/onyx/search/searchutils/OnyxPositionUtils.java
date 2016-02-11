/**
 * *****************************************************************************
 * Copyright (c) 2015, 2016, 2017, Thomas.H Warner. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 * p list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * p list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its contributors
 * may be used to endorse or promote products derived from p software without
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
package com.jellyfish.jfgonyx.onyx.search.searchutils;

import com.jellyfish.jfgonyx.constants.GraphicsConst;
import com.jellyfish.jfgonyx.constants.OnyxConst;
import com.jellyfish.jfgonyx.onyx.entities.OnyxPos;
import com.jellyfish.jfgonyx.onyx.entities.collections.OnyxPosCollection;
import com.jellyfish.jfgonyx.onyx.exceptions.InvalidOnyxPositionException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author thw
 */
public class OnyxPositionUtils {
    
    public static List<OnyxPos> getBorders(final OnyxPosCollection c, final GraphicsConst.COLOR color) {
        
        final List<OnyxPos> borders = new ArrayList<>();
        if (color.boolColor) {
            for (int i = 1; i <= OnyxConst.BOARD_SIDE_SQUARE_COUNT + 1; ++i) {
                borders.add(c.getPosition(
                    String.format(OnyxPosCollection.KEY_FORMAT, 1f, (float) i)));
                borders.add(c.getPosition(
                    String.format(OnyxPosCollection.KEY_FORMAT, 12f, (float) i)));
            }
        } else if (!color.boolColor) {
            for (int i = 1; i <= OnyxConst.BOARD_SIDE_SQUARE_COUNT + 1; ++i) {
                borders.add(c.getPosition(
                    String.format(OnyxPosCollection.KEY_FORMAT, (float) i, 1f)));
                borders.add(c.getPosition(
                    String.format(OnyxPosCollection.KEY_FORMAT, (float) i, 12f)));
            }
        }
        return borders;
    }

    public static List<OnyxPos> trimByBorderStartPositionsAndColor(final List<OnyxPos> pos, final GraphicsConst.COLOR color) {
        
        final List<OnyxPos> positions = new ArrayList<>();
        for (OnyxPos p : pos) {
            if (p.isOccupied() && p.getPiece().color.bitColor == color.bitColor) {
                if (color.boolColor && ((int) p.x) == 1) { // Black position.
                    positions.add(p);
                } else if (!color.boolColor && ((int) p.y) == 1) { // Else white position.
                    positions.add(p);
                }
            }
        }
        
        return positions;
    }
    
    public static List<OnyxPos> trimByAllBorderPositionsAndColor(final List<OnyxPos> pos, final GraphicsConst.COLOR color) {
        
        final List<OnyxPos> positions = new ArrayList<>();
        for (OnyxPos p : pos) {
            if (p.isOccupied() && p.getPiece().color.bitColor == color.bitColor) {
                if (color.boolColor) {
                    if (((int) p.x) == 1 || ((int) p.x) == OnyxConst.BOARD_SIDE_SQUARE_COUNT + 1) {
                        positions.add(p);
                    }
                } else if (!color.boolColor) {
                    if (((int) p.y) == 1 || ((int) p.y) == OnyxConst.BOARD_SIDE_SQUARE_COUNT + 1) {
                        positions.add(p);
                    }
                }
            }
        }
        
        return positions;
    }
    
    public static final String[] getConnectionCandidates(final OnyxPos p) {
        
        final String[] candidates = new String[16];
        candidates[0] = String.format(OnyxPosCollection.KEY_FORMAT, p.x, p.y - .5f);
        candidates[1] = String.format(OnyxPosCollection.KEY_FORMAT, p.x + .5f, p.y - .5f);
        candidates[2] = String.format(OnyxPosCollection.KEY_FORMAT, p.x + .5f, p.y);
        candidates[3] = String.format(OnyxPosCollection.KEY_FORMAT, p.x + .5f, p.y + .5f);
        candidates[4] = String.format(OnyxPosCollection.KEY_FORMAT, p.x, p.y + .5f);
        candidates[5] = String.format(OnyxPosCollection.KEY_FORMAT, p.x - .5f, p.y + .5f);
        candidates[6] = String.format(OnyxPosCollection.KEY_FORMAT, p.x - .5f, p.y);
        candidates[7] = String.format(OnyxPosCollection.KEY_FORMAT, p.x - .5f, p.y - .5f);
        candidates[8] = String.format(OnyxPosCollection.KEY_FORMAT, p.x, p.y - 1f);
        candidates[9] = String.format(OnyxPosCollection.KEY_FORMAT, p.x + 1f, p.y - 1f);
        candidates[10] = String.format(OnyxPosCollection.KEY_FORMAT, p.x + 1f, p.y);
        candidates[11] = String.format(OnyxPosCollection.KEY_FORMAT, p.x + 1f, p.y + 1f);
        candidates[12] = String.format(OnyxPosCollection.KEY_FORMAT, p.x, p.y + 1f);
        candidates[13] = String.format(OnyxPosCollection.KEY_FORMAT, p.x - 1f, p.y + 1f);
        candidates[14] = String.format(OnyxPosCollection.KEY_FORMAT, p.x - 1f, p.y);
        candidates[15] = String.format(OnyxPosCollection.KEY_FORMAT, p.x - 1f, p.y - 1f);
        
        return candidates;
    }
    
    public static final String[] getCenterPosConnectionCandidates(final OnyxPos p) 
            throws InvalidOnyxPositionException {
        
        if (!p.isDiamondCenter()) throw new InvalidOnyxPositionException();
        
        final String[] candidates = new String[8];
        candidates[0] = String.format(OnyxPosCollection.KEY_FORMAT, p.x, p.y - .5f);
        candidates[1] = String.format(OnyxPosCollection.KEY_FORMAT, p.x + .5f, p.y - .5f);
        candidates[2] = String.format(OnyxPosCollection.KEY_FORMAT, p.x + .5f, p.y);
        candidates[3] = String.format(OnyxPosCollection.KEY_FORMAT, p.x + .5f, p.y + .5f);
        candidates[4] = String.format(OnyxPosCollection.KEY_FORMAT, p.x, p.y + .5f);
        candidates[5] = String.format(OnyxPosCollection.KEY_FORMAT, p.x - .5f, p.y + .5f);
        candidates[6] = String.format(OnyxPosCollection.KEY_FORMAT, p.x - .5f, p.y);
        candidates[7] = String.format(OnyxPosCollection.KEY_FORMAT, p.x - .5f, p.y - .5f);
        
        return candidates;
    }
    
}
