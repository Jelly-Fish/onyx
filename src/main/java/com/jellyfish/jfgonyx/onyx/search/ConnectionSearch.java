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
import com.jellyfish.jfgonyx.onyx.abstractions.AbstractOnyxSearch;
import com.jellyfish.jfgonyx.onyx.entities.OnyxMove;
import com.jellyfish.jfgonyx.onyx.entities.OnyxPos;
import com.jellyfish.jfgonyx.onyx.entities.collections.OnyxPosCollection;
import com.jellyfish.jfgonyx.onyx.exceptions.InvalidOnyxPositionException;
import com.jellyfish.jfgonyx.onyx.exceptions.NoValidOnyxPositionsFoundException;
import com.jellyfish.jfgonyx.onyx.interfaces.search.OnyxConnectionSearchable;
import com.jellyfish.jfgonyx.onyx.search.searchutils.OnyxPositionUtils;
import com.jellyfish.jfgonyx.onyx.search.subroutines.SearchTailConnection;
import com.jellyfish.jfgonyx.onyx.search.subroutines.SearchWinConnection;
import com.jellyfish.jfgonyx.ui.OnyxBoard;
import java.util.ArrayList;
import java.util.List;

/**
 * Connection search taking advantage of OnyxPosCollection connection tree :
 * @see OnyxPosCollection
 * @OnyxPos connections (all keys that are playable connections).
 * @author thw
 */
public class ConnectionSearch extends AbstractOnyxSearch implements OnyxConnectionSearchable {

    @Override
    public OnyxMove search(final OnyxPosCollection c, final OnyxBoard board, final GraphicsConst.COLOR color) 
            throws NoValidOnyxPositionsFoundException, InvalidOnyxPositionException {
        return getTailMove(c, board, color);
    }
    
    @Override
    public boolean isWin(final OnyxPosCollection c, final GraphicsConst.COLOR color) 
            throws NoValidOnyxPositionsFoundException {
        
        final List<OnyxPos> borders = OnyxPositionUtils.trimByBorderStartPositionsAndColor(
                OnyxPositionUtils.getBorders(c, color), color);
        
        SearchWinConnection search = null;
        for (OnyxPos p : borders) {
            search = new SearchWinConnection(c, color);
            search.connection(p, p.getKey());
            if (search.isWin()) {
                return true;
            }
        }
        
        return false;
    }

    /**
     * @param c collection of unique Onyx positions - positions are independent from OnyxDiamond instances.
     * @param color the color to check for win position.
     * @param board onyx board instance.
     * @return best onyx connection search move as a non win end of tail position move.
     */
    @SuppressWarnings("null")
    private OnyxMove getTailMove(final OnyxPosCollection c, final OnyxBoard board, 
            final GraphicsConst.COLOR color) throws NoValidOnyxPositionsFoundException {
        
        final List<OnyxPos> pos = OnyxPositionUtils.trimByAllBorderPositionsAndColor(
                OnyxPositionUtils.getBorders(c, color), color);
        final List<OnyxMove> candidates = new ArrayList<>();
        
        for (OnyxPos p : pos) {
            candidates.addAll(new SearchTailConnection(c, color, board).getTails(p, p.getKey()));
        }
        
        OnyxMove tmp = null;
        for (OnyxMove m : candidates) {
            if (tmp == null) tmp = m;
            if (!m.isLambda() && m.getScore() >= tmp.getScore()) {
                tmp = m;
            }
        }
        
        return new OnyxMove(tmp.getPos(), tmp.getPiece(), tmp.getScore());
    }
    
}
