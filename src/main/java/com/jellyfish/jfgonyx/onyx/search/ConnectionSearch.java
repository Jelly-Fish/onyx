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

import com.jellyfish.jfgonyx.onyx.constants.OnyxConst;
import com.jellyfish.jfgonyx.onyx.OnyxGame;
import com.jellyfish.jfgonyx.onyx.abstractions.AbstractOnyxSearch;
import com.jellyfish.jfgonyx.onyx.entities.OnyxMove;
import com.jellyfish.jfgonyx.onyx.entities.OnyxPos;
import com.jellyfish.jfgonyx.onyx.entities.collections.OnyxPosCollection;
import com.jellyfish.jfgonyx.onyx.exceptions.InvalidOnyxPositionException;
import com.jellyfish.jfgonyx.onyx.exceptions.NoValidOnyxPositionsFoundException;
import com.jellyfish.jfgonyx.onyx.interfaces.search.OnyxConnectionSearchable;
import com.jellyfish.jfgonyx.onyx.search.searchutils.MoveUtils;
import com.jellyfish.jfgonyx.onyx.search.searchutils.OnyxPositionUtils;
import com.jellyfish.jfgonyx.onyx.search.searchutils.SearchUtils;
import com.jellyfish.jfgonyx.onyx.search.subroutines.connectionsearch.SubTailConnectionSubroutine;
import com.jellyfish.jfgonyx.onyx.search.subroutines.connectionsearch.TailConnectionSubroutine;
import com.jellyfish.jfgonyx.onyx.search.subroutines.connectionsearch.WinConnectionSubroutine;
import com.jellyfish.jfgonyx.onyx.search.subroutines.connectionsearch.WinConnectionLinkSubroutine;
import com.jellyfish.jfgonyx.onyx.search.subroutines.positionsearch.TakePositionSubroutine;
import com.jellyfish.jfgonyx.ui.OnyxBoard;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Connection search taking advantage of OnyxPosCollection connection tree :
 * @see OnyxPosCollection
 * @OnyxPos connections (all keys that are playable connections).
 * @author thw
 */
public class ConnectionSearch extends AbstractOnyxSearch implements OnyxConnectionSearchable {

    private final List<OnyxMove> cnxTmpMoves = new ArrayList<>();
    private final Set<String> checkedKeys = new HashSet();
    private final List<OnyxMove> cnxMoves = new ArrayList<>();
    
    @Override
    public OnyxMove search(final OnyxPosCollection c, final OnyxBoard board, final OnyxConst.COLOR color) 
            throws NoValidOnyxPositionsFoundException, InvalidOnyxPositionException {
        
        this.init();
        
        final OnyxMove tail = this.getTailMove(c, board, color);
        this.cnxMoves.add(tail);
        this.cnxMoves.add(this.searchWinMove(c, color));
        this.cnxMoves.add(this.searchCounterWinLink(c, board, color));
        final OnyxMove counterTail = 
                this.getSubTailCounterMove(c, board, OnyxConst.COLOR.getOposite(color.bool));
        this.cnxMoves.add(counterTail);
                
        return this.trim(this.cnxMoves, board, c, color);
    }
    
    private void init() {
        this.cnxTmpMoves.clear();
        this.checkedKeys.clear();
        this.cnxMoves.clear();
    }
    
    @Override
    public boolean isWin(final OnyxPosCollection c, final OnyxConst.COLOR color) 
            throws NoValidOnyxPositionsFoundException {
        
        final List<OnyxPos> borders = OnyxPositionUtils.trimByBorderStartPositionsAndColor(
                OnyxPositionUtils.getBordersByColor(c, color), color);
        
        WinConnectionSubroutine search = null;
        for (OnyxPos p : borders) {
            search = new WinConnectionSubroutine(c, color, true);
            search.connection(p, p.getKey());
            if (search.isWin()) return true;
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
            final OnyxConst.COLOR color) throws NoValidOnyxPositionsFoundException, InvalidOnyxPositionException {
        
        final List<OnyxPos> pos = OnyxPositionUtils.trimAllExternalBordersByColor(
                OnyxPositionUtils.getBordersByColor(c, color), color);
        TailConnectionSubroutine sub;
        
        for (OnyxPos p : pos) {
            sub = new TailConnectionSubroutine(c, color, board);
            this.cnxTmpMoves.add(sub.getTail(p, false));
            this.checkedKeys.addAll(sub.getCheckedKeys());
        }
        
        final OnyxMove tmp = this.trim(this.cnxTmpMoves, board, c, color);
        
        return MoveUtils.isMove(tmp) ? new OnyxMove(tmp.getPos(), tmp.getPiece(),
            SearchUtils.calibrateTailMoves(OnyxGame.getInstance(), tmp.getScore())) : null;
    }
    
    private OnyxMove getSubTailCounterMove(final OnyxPosCollection c, final OnyxBoard board, 
            final OnyxConst.COLOR color) throws NoValidOnyxPositionsFoundException, InvalidOnyxPositionException {
        
        List<OnyxPos> posSet = null;
        final List<OnyxMove> moves = new ArrayList<>();
        final List<OnyxPos> pos = OnyxPositionUtils.trimBorderByColorWithExceptions(
                OnyxPositionUtils.getSubBordersByColor(c, color), color, this.checkedKeys);
        pos.addAll(OnyxPositionUtils.getSubCounterBordersByColor(c, color));
        
        float minX = OnyxConst.BOARD_SIDE_POS_COUNT, minY = OnyxConst.BOARD_SIDE_POS_COUNT,
            maxX = 0f, maxY = 0f;
        
        for (OnyxPos p : pos) {
            minX = p.x < minX ? p.x : minX;
            minY = p.y < minY ? p.y : minY;
            maxX = p.x > maxX ? p.x : maxX;
            maxY = p.y > maxY ? p.y : maxY;
        }
        
        for (OnyxPos p : pos) {
            moves.add(new SubTailConnectionSubroutine(c, color, board, minX, minY, maxX, maxY).getTail(p, true));
        }
        
        final OnyxMove tmp = this.trim(moves, board, c, OnyxConst.COLOR.getOposite(color.bool));

        // Take possibility - if so then apply captures.
        final OnyxConst.COLOR opColor = OnyxConst.COLOR.getOposite(color.bool);
        final OnyxMove capture = new TakePositionSubroutine().getTakePos(c, board, opColor.bit);
        if (MoveUtils.isMove(capture)) posSet = c.getTakePositions(capture.getPos().getKey(), opColor.bit, board);
        if (MoveUtils.isMove(capture, tmp) && posSet != null && tmp.getPos().equals(capture.getPos())) {
            tmp.setCaptured(new ArrayList<OnyxPos>());
            tmp.getCaptured().addAll(posSet);
        }
        
        return MoveUtils.isMove(tmp) ? tmp : null;
    }
    
    /**
     * @param c collection of unique Onyx positions - positions are independent from OnyxDiamond instances.
     * @param color the color to check for win position.
     * @return winning onyx connection or null.
     */
    private OnyxMove searchCounterWinLink(final OnyxPosCollection c, final OnyxBoard board, 
            final OnyxConst.COLOR color) throws NoValidOnyxPositionsFoundException, InvalidOnyxPositionException {   

        int count = 0;
        List<OnyxPos> posSet = null;
        final List<OnyxMove> moves = new ArrayList<>();
        OnyxMove move = null;
        
        final OnyxConst.COLOR opColor = OnyxConst.COLOR.getOposite(color.bool);
        moves.add(this.getTailMove(c, board, opColor));
        moves.add(this.searchWinMove(c, moves, opColor));
        
        for (OnyxMove m : moves) count = MoveUtils.isMove(m) ? ++count : count;
        
        if (count > 0) {
            
            move = new WinConnectionLinkSubroutine(c, opColor).connectionLink(moves);
            
            // Take possibility & if true apply captures :
            final OnyxMove capture = new TakePositionSubroutine().getTakePos(c, board, color.bit);
            if (MoveUtils.isMove(capture)) posSet = c.getTakePositions(capture.getPos().getKey(), color.bit, board);

            if (MoveUtils.isMove(capture, move) && posSet != null && move.getPos().equals(capture.getPos())) {
                move.setScore(OnyxConst.SCORE.COUNTER_WIN_LINK.getValue());
                move.setCaptured(new ArrayList<OnyxPos>());
                move.getCaptured().addAll(posSet);
            }
        }

        return move;
    }
        
    /**
     * @param c collection of unique Onyx positions - positions are independent from OnyxDiamond instances.
     * @param color the color to check for win position.
     * @return winning onyx connection or null.
     * @throws NoValidOnyxPositionsFoundException 
     */
    private OnyxMove searchWinMove(final OnyxPosCollection c, final OnyxConst.COLOR color) 
        throws NoValidOnyxPositionsFoundException {      
        return new WinConnectionLinkSubroutine(c, color).connectionLink(this.cnxTmpMoves);
    }

    /**
     * Win move search used for countering oponent win move.
     * @param c collection of unique Onyx positions - positions are independent from OnyxDiamond instances.
     * @param tails cnx tails found for current configuration.
     * @param color to search for.
     * @return winning onyx connection or null.
     * @throws NoValidOnyxPositionsFoundException 
     */
    private OnyxMove searchWinMove(final OnyxPosCollection c, final List<OnyxMove> tails, 
            final OnyxConst.COLOR color) 
            throws NoValidOnyxPositionsFoundException {      
        return new WinConnectionLinkSubroutine(c, color).connectionLink(tails);
    }
    
}
