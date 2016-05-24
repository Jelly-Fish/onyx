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
import com.jellyfish.jfgonyx.onyx.entities.OnyxMove;
import com.jellyfish.jfgonyx.onyx.entities.OnyxPos;
import com.jellyfish.jfgonyx.onyx.entities.OnyxTail;
import com.jellyfish.jfgonyx.onyx.entities.collections.OnyxPosCollection;
import com.jellyfish.jfgonyx.onyx.exceptions.InvalidOnyxPositionException;
import com.jellyfish.jfgonyx.onyx.exceptions.NoValidOnyxPositionsFoundException;
import com.jellyfish.jfgonyx.onyx.interfaces.search.OnyxConnectionSearchable;
import com.jellyfish.jfgonyx.onyx.search.searchutils.OnyxPositionUtils;
import com.jellyfish.jfgonyx.onyx.search.subroutines.connectionsearch.VirtualConnectionSubroutine;
import com.jellyfish.jfgonyx.onyx.search.subroutines.positionsearch.OnyxPosStateSubroutine;
import com.jellyfish.jfgonyx.ui.OnyxBoard;
import java.util.List;

/**
 * @author thw
 */
public class VirtualConnetionSearch extends ConnectionSearch implements OnyxConnectionSearchable {

    @Override
    public OnyxMove search(final OnyxPosCollection c, final OnyxBoard board, 
            final OnyxConst.COLOR color) throws NoValidOnyxPositionsFoundException, InvalidOnyxPositionException {
        
        /**
         * FIXME : still to improve.
         * crossTailSearches method must take advantage on oponent tail search.
         * When comparing tails, if oponent tail pos if present if Onyx tail
         * position collection then Override with scoring.
         */
        
        final OnyxConst.COLOR opColor = OnyxConst.COLOR.getOposite(color.bool);
        final List<OnyxPos> pos = OnyxPositionUtils.trimAllExternalBordersByColor(
                OnyxPositionUtils.getBordersByColor(c, color), color);
        final List<OnyxPos> opPos = OnyxPositionUtils.trimAllExternalBordersByColor(
                OnyxPositionUtils.getBordersByColor(c, opColor), opColor);
        VirtualConnectionSubroutine vCnx = new VirtualConnectionSubroutine(c, color, board);
        vCnx.buildTails(pos);
        final OnyxTail onyxTail = vCnx.getTail();
        vCnx = new VirtualConnectionSubroutine(c, opColor, board);
        vCnx.buildTails(opPos);
        final OnyxTail oponentTail = vCnx.getTail();       
        
        final OnyxPos res = this.crossTailSearch(onyxTail, oponentTail, board, 
                c, color, this.getTailMove(c, board, opColor));
        
        if (res != null) {
            return assertCapture(new OnyxMove(res, 666.66f), board, c, color);
        }
        
        return null;
    }
    
    /**
     * @param sT tail to search position for
     * @param oT oponent tail
     * @param b onyx board
     * @param c position collection
     * @param color color to serach for
     * @param opTailMove oponent tail best move (depending on quality of
     * super class's search...)
     * @return first OnyxPos found that belongs to both sT & oT tails
     */
    private OnyxPos crossTailSearch(final OnyxTail sT, final OnyxTail oT, final OnyxBoard b,
            final OnyxPosCollection c, final OnyxConst.COLOR color, final OnyxMove opTailMove) throws InvalidOnyxPositionException {

        if (sT == null || oT == null) return null;
        OnyxPos pos = null;
        
        for (OnyxPos pOT : oT.getPositions()) {
            for (OnyxPos sOT : sT.getPositions()) {
                if (new OnyxPosStateSubroutine(sOT).willEnableTake(b, c, color) 
                    || sOT.isOccupied()) continue;
                if (sOT.getKey().equals(opTailMove.getPos().getKey())) pos = sOT;
                if (sOT.getKey().equals(pOT.getKey())) pos = sOT;               
            }
        }
        
        if (pos == null) {
            for (OnyxPos sOT : sT.getPositions()) {
                if (!sOT.isOccupied()) {
                    pos = sOT;
                    break;
                }
            }
        }
                
        return pos;
    }

    /**
     * @throws com.jellyfish.jfgonyx.onyx.exceptions.NoValidOnyxPositionsFoundException if used.
     * @deprecated for this search routine : goal is not to seek win move.
     */
    @Override
    public boolean isWin(final OnyxPosCollection c, final OnyxConst.COLOR color) 
        throws NoValidOnyxPositionsFoundException {
        throw new UnsupportedOperationException();
    }
    
}
