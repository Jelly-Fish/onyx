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
import com.jellyfish.jfgonyx.onyx.abstractions.AbstractOnyxSearch;
import com.jellyfish.jfgonyx.onyx.entities.OnyxMove;
import com.jellyfish.jfgonyx.onyx.entities.OnyxPos;
import com.jellyfish.jfgonyx.onyx.entities.collections.OnyxPosCollection;
import com.jellyfish.jfgonyx.onyx.exceptions.InvalidOnyxPositionException;
import com.jellyfish.jfgonyx.onyx.exceptions.NoValidOnyxPositionsFoundException;
import com.jellyfish.jfgonyx.onyx.interfaces.search.OnyxConnectionSearchable;
import com.jellyfish.jfgonyx.onyx.search.searchutils.MoveUtils;
import com.jellyfish.jfgonyx.onyx.search.searchutils.OnyxPositionUtils;
import com.jellyfish.jfgonyx.onyx.search.subroutines.connectionsearch.TailConnectionSubroutine;
import com.jellyfish.jfgonyx.onyx.search.subroutines.connectionsearch.WinConnectionSubroutine;
import com.jellyfish.jfgonyx.onyx.search.subroutines.connectionsearch.WinConnectionLinkSubroutine;
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
        this.cnxMoves.add(this.searchWinMove(c, color));
        this.cnxMoves.add(this.searchCounterWinLink(c, board, color));
                
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
     * @throws com.jellyfish.jfgonyx.onyx.exceptions.InvalidOnyxPositionException
     */
    protected OnyxMove getTailMove(final OnyxPosCollection c, final OnyxBoard board, 
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
        final List<OnyxMove> moves = new ArrayList<>();
        OnyxMove move = null;
        
        final OnyxConst.COLOR opColor = OnyxConst.COLOR.getOposite(color.bool);
        moves.add(this.getTailMove(c, board, opColor));
        moves.add(this.searchWinMove(c, moves, opColor));
        
        for (OnyxMove m : moves) count = MoveUtils.isMove(m) ? ++count : count;
        
        if (count > 0) {            
            move = new WinConnectionLinkSubroutine(c, opColor).connectionLink(moves);
            move = this.assertCapture(move, board, c, color);
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
