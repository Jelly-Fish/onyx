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
import com.jellyfish.jfgonyx.onyx.abstractions.AbstractOnyxSearch;
import com.jellyfish.jfgonyx.onyx.entities.OnyxMove;
import com.jellyfish.jfgonyx.onyx.entities.OnyxPos;
import com.jellyfish.jfgonyx.onyx.entities.collections.OnyxPosCollection;
import com.jellyfish.jfgonyx.onyx.exceptions.InvalidOnyxPositionException;
import com.jellyfish.jfgonyx.onyx.exceptions.NoValidOnyxPositionsFoundException;
import com.jellyfish.jfgonyx.onyx.interfaces.search.OnyxConnectionSearchable;
import com.jellyfish.jfgonyx.ui.OnyxBoard;
import java.util.ArrayList;
import java.util.List;

/**
 * Connection search taking advantage of OnyxPos connections :
 * @see OnyxPosCollection
 * @author thw
 */
public class ConnectionSearch extends AbstractOnyxSearch implements OnyxConnectionSearchable {

    @Override
    public OnyxMove search(final OnyxPosCollection c, final OnyxBoard board, final GraphicsConst.COLOR color) 
            throws NoValidOnyxPositionsFoundException, InvalidOnyxPositionException {
        /**
         * FIXME gl&hf
         */
        return null;
    }
    
    @Override
    public boolean isWin(final OnyxPosCollection c, final GraphicsConst.COLOR color) 
            throws NoValidOnyxPositionsFoundException {
        /**
         * Black borders are on x1.0 & x12.0 (Y from 1 to 12)
         * White borders are on y1.0 & y12.0 (x from 1 to 10)
         * 
         * Get all occupied positions by color (x 1.0 > 12.00 for black y for whites)
         *     recursively go through all position P for opposite border position
         *     @see ConnectionWinSearch
         *       IF no break THEN win true
         *       ELSE false.
         */
        final List<OnyxPos> borders = trimByColor(getBorders(c, color), color);
        
        for (OnyxPos p : borders) {
            if (new ConnectionWinSearch(c, color, p).hasConnection(p)) {
                return true;
            }
        }
        return false;
    }
    
    private List<OnyxPos> getBorders(final OnyxPosCollection c, final GraphicsConst.COLOR color) {
        
        final List<OnyxPos> borders = new ArrayList<>();
        if (color.boolColor) {
            for (int i = 0; i <= OnyxConst.BOARD_SIDE_SQUARE_COUNT; ++i) {
                borders.add(c.getPosition(
                    String.format(OnyxPosCollection.KEY_FORMAT, (float) 1, (float) i + 1)));
                borders.add(c.getPosition(
                    String.format(OnyxPosCollection.KEY_FORMAT, (float) 12, (float) i + 1)));
            }
        } else if (!color.boolColor) {
            for (int i = 0; i <= OnyxConst.BOARD_SIDE_SQUARE_COUNT; ++i) {
                borders.add(c.getPosition(
                    String.format(OnyxPosCollection.KEY_FORMAT, (float) 1 + 1, (float) 1)));
                borders.add(c.getPosition(
                    String.format(OnyxPosCollection.KEY_FORMAT, (float) i + 1, (float) 12)));
            }
        }
        return borders;
    }

    private List<OnyxPos> trimByColor(final List<OnyxPos> pos, final GraphicsConst.COLOR color) {
        
        final List<OnyxPos> positions = new ArrayList<>();
        for (OnyxPos p : pos) {
            if (p.isOccupied() && p.getPiece().color.bitColor == color.bitColor) {
                if (color.boolColor && ((int) p.x) == 1) {
                    positions.add(p);
                } else if (!color.boolColor && ((int) p.y) == 1) {
                    positions.add(p);
                }
            }
        }
        
        return positions;
    }
    
}
