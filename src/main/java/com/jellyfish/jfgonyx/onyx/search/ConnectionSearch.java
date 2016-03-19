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
import com.jellyfish.jfgonyx.onyx.OnyxGame;
import com.jellyfish.jfgonyx.onyx.abstractions.AbstractOnyxSearch;
import com.jellyfish.jfgonyx.onyx.entities.OnyxMove;
import com.jellyfish.jfgonyx.onyx.entities.OnyxPos;
import com.jellyfish.jfgonyx.onyx.entities.collections.OnyxPosCollection;
import com.jellyfish.jfgonyx.onyx.exceptions.InvalidOnyxPositionException;
import com.jellyfish.jfgonyx.onyx.exceptions.NoValidOnyxPositionsFoundException;
import com.jellyfish.jfgonyx.onyx.interfaces.search.OnyxConnectionSearchable;
import com.jellyfish.jfgonyx.onyx.search.searchutils.OnyxPositionUtils;
import com.jellyfish.jfgonyx.onyx.search.searchutils.SearchUtils;
import com.jellyfish.jfgonyx.onyx.search.subroutines.connectionsearch.TailConnectionSubroutine;
import com.jellyfish.jfgonyx.onyx.search.subroutines.connectionsearch.WinConnectionSubroutine;
import com.jellyfish.jfgonyx.onyx.search.subroutines.connectionsearch.WinConnectionLinkSubroutine;
import com.jellyfish.jfgonyx.onyx.search.subroutines.positionsearch.TakePositionSubroutine;
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

    private final List<OnyxMove> cnxPos = new ArrayList<>();
    
    @Override
    public OnyxMove search(final OnyxPosCollection c, final OnyxBoard board, final GraphicsConst.COLOR color) 
            throws NoValidOnyxPositionsFoundException, InvalidOnyxPositionException {
        
        this.cnxPos.clear();
        final OnyxMove[] cnxMoves = new OnyxMove[3];
        cnxMoves[0] = this.getTailMove(c, board, color);
        cnxMoves[1] = this.searchWinMove(c, color);
        cnxMoves[2] = this.searchCounterWinLink(c, board, color);
        
        OnyxMove tmp = null;
        for (OnyxMove m : cnxMoves) {
            if (tmp == null) tmp = m;
            else if (m != null && m.getScore() > tmp.getScore()) tmp = m;
        }
        
        return tmp;
    }
    
    @Override
    public boolean isWin(final OnyxPosCollection c, final GraphicsConst.COLOR color) 
            throws NoValidOnyxPositionsFoundException {
        
        final List<OnyxPos> borders = OnyxPositionUtils.trimByBorderStartPositionsByColor(
                OnyxPositionUtils.getBorders(c, color), color);
        
        WinConnectionSubroutine search = null;
        for (OnyxPos p : borders) {
            search = new WinConnectionSubroutine(c, color, true);
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
     * @throws com.jellyfish.jfgonyx.onyx.exceptions.NoValidOnyxPositionsFoundException
     */
    private OnyxMove getTailMove(final OnyxPosCollection c, final OnyxBoard board, 
            final GraphicsConst.COLOR color) throws NoValidOnyxPositionsFoundException {
        
        final List<OnyxPos> pos = OnyxPositionUtils.trimByAllBorderPositionsByColor(
                OnyxPositionUtils.getBorders(c, color), color);
        
        for (OnyxPos p : pos) {
            this.cnxPos.add(new TailConnectionSubroutine(c, color, board).getTail(p, p.getKey()));
        }
        
        OnyxMove tmp = null;
        for (OnyxMove m : this.cnxPos) {
            if (tmp == null) tmp = m;
            if (m.getScore() >= tmp.getScore()) {
                tmp = m;
            }
        }

        return tmp != null ? new OnyxMove(tmp.getPos(), tmp.getPiece(),
            SearchUtils.calibrateTailMoves(OnyxGame.getInstance(), tmp.getScore())) : null;
    }
    
    /**
     * @param c collection of unique Onyx positions - positions are independent from OnyxDiamond instances.
     * @param color the color to check for win position.
     * @return winning onyx connection or null.
     */
    private OnyxMove searchWinMove(final OnyxPosCollection c, final GraphicsConst.COLOR color) 
            throws NoValidOnyxPositionsFoundException {      
        return new WinConnectionLinkSubroutine(c, color).connectionLink(this.cnxPos);
    }
    
    /**
     * @param c collection of unique Onyx positions - positions are independent from OnyxDiamond instances.
     * @param color the color to check for win position.
     * @return winning onyx connection or null.
     */
    private OnyxMove searchCounterWinLink(final OnyxPosCollection c, final OnyxBoard board, 
            final GraphicsConst.COLOR color) throws NoValidOnyxPositionsFoundException, InvalidOnyxPositionException {   

        int count = 0;
        List<OnyxPos> posSet = null;
        final List<OnyxMove> moves = new ArrayList<>();
        OnyxMove move = null;
        
        final GraphicsConst.COLOR opColor = GraphicsConst.COLOR.getOposite(color.bool);
        moves.add(this.getTailMove(c, board, opColor));
        moves.add(this.searchWinMove(c, opColor));
        for (OnyxMove m : moves) count = m == null ? count : ++count;
        
        if (count > 0) {
            
            move = new WinConnectionLinkSubroutine(c, opColor).connectionLink(moves);
            final OnyxMove capture = new TakePositionSubroutine().getTakePos(c, board, color.bit);
            if (capture != null) posSet = c.getTakePositions(capture.getPos().getKey(), color.bit, board);

            if (capture != null && posSet != null && move != null && move.getPos().equals(capture.getPos())) {
                move.setScore(OnyxConst.SCORE.COUNTER_WIN_LINK.getValue());
                move.setCaptured(new ArrayList<OnyxPos>());
                move.getCaptured().addAll(posSet);
            }
        }

        return move;
    }
    
}
