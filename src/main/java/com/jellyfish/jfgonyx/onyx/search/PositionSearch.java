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

import com.jellyfish.jfgonyx.onyx.search.subroutines.positionsearch.SearchNeighbourPosition;
import com.jellyfish.jfgonyx.onyx.search.subroutines.positionsearch.SearchCounterPosition;
import com.jellyfish.jfgonyx.onyx.search.subroutines.positionsearch.SearchTakePosition;
import com.jellyfish.jfgonyx.constants.GraphicsConst;
import com.jellyfish.jfgonyx.onyx.entities.OnyxMove;
import com.jellyfish.jfgonyx.onyx.abstractions.AbstractOnyxSearch;
import com.jellyfish.jfgonyx.onyx.entities.OnyxPos;
import com.jellyfish.jfgonyx.onyx.interfaces.search.OnyxPositionSearchable;
import com.jellyfish.jfgonyx.onyx.entities.collections.OnyxPosCollection;
import com.jellyfish.jfgonyx.onyx.exceptions.InvalidOnyxPositionException;
import com.jellyfish.jfgonyx.onyx.exceptions.NoValidOnyxPositionsFoundException;
import com.jellyfish.jfgonyx.onyx.search.subroutines.positionsearch.SearchAttackPosition;
import com.jellyfish.jfgonyx.onyx.search.subroutines.positionsearch.SearchCenterPosition;
import com.jellyfish.jfgonyx.ui.OnyxBoard;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author thw
 */
public class PositionSearch extends AbstractOnyxSearch implements OnyxPositionSearchable {
    
    /*
     * Look for all possible take moves
     * IF diamond has 2 corners set to != color
     *   Search for neighbour diamonds with similar configuration
     *   IF found then double take
     * ELSE Look for counter attacks
     *   Prevent full set diamonds where != color
     * ELSE play neighbour != color positions
     *   Find forward positions depending on color
     */
    @Override
    public OnyxMove search(final OnyxPosCollection c, final OnyxBoard board, final GraphicsConst.COLOR color) 
            throws NoValidOnyxPositionsFoundException {
    
        List<OnyxPos> posSet = null;
        final Set<OnyxMove> moves = new HashSet<>();
        
        try {
            
            final OnyxMove capture = new SearchTakePosition().getTakePos(c, board, color.bitColor);
            moves.add(capture);
            moves.add(new SearchTakePosition().getTakePos(c, board, color.bitColor));
            moves.add(new SearchCounterPosition().getCounterPos(c, board, color.bitColor));
            moves.add(new SearchNeighbourPosition().getNeighbourPos(c, board, color.bitColor));
            moves.add(new SearchAttackPosition().getAttackPos(c, board, color.bitColor));
            moves.add(new SearchCenterPosition().getCenterPos(c, board.getDiamondCollection()));
            
            if (capture != null) {
                posSet = c.getTakePositions(capture.getPos().getKey(), color.bitColor, board);
                posSet = c.performTake(capture.getPos().getKey(), color.bitColor, board);
                return capture;
            }
            
            OnyxMove tmp = null;
            for (OnyxMove m : moves) {
                if (tmp == null) tmp = m;
                else if (m != null && m.getScore() > tmp.getScore()) tmp = m;
            }
            
            if (tmp == null) throw new NoValidOnyxPositionsFoundException();
            if (c.getPosition(tmp.getPos().getKey()).isOccupied()) {
                throw new NoValidOnyxPositionsFoundException();
            }
            
            return tmp;
        
        } catch (final InvalidOnyxPositionException Iopex) {
            Logger.getLogger(PositionSearch.class.getName()).log(Level.SEVERE, null, Iopex);
        }
        
        throw new NoValidOnyxPositionsFoundException();
    }
    
    @Override
    public boolean isWin(final OnyxPosCollection c, final GraphicsConst.COLOR color) 
            throws NoValidOnyxPositionsFoundException {
        throw new UnsupportedOperationException();
    }
    
}
