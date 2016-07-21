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
import com.jellyfish.jfgonyx.onyx.search.subroutines.connectionsearch.VirtualTailCrossSearchResultsSubroutine;
import com.jellyfish.jfgonyx.onyx.search.subroutines.connectionsearch.VirtualConnectionSubroutine;
import com.jellyfish.jfgonyx.ui.OnyxBoard;
import java.util.List;

/**
 * @author thw
 */
public class VirtualConnetionSearch extends ConnectionSearch implements OnyxConnectionSearchable {

    @Override
    public OnyxMove search(final OnyxPosCollection c, final OnyxBoard board, 
            final OnyxConst.COLOR color) throws NoValidOnyxPositionsFoundException, InvalidOnyxPositionException {

        final OnyxConst.COLOR opColor = OnyxConst.COLOR.getOposite(color.bool);
        final List<OnyxPos> pos = OnyxPositionUtils.getAllExternalBordersByColor(
                OnyxPositionUtils.getBordersByColor(c, color), color);        
        final List<OnyxPos> opPos = OnyxPositionUtils.getAllExternalBordersByColor(
                OnyxPositionUtils.getBordersByColor(c, opColor), opColor);
        
        /**
         * FIXME : border positions are discarded.
         */
        
        final VirtualConnectionSubroutine vCnx = new VirtualConnectionSubroutine(c, color, board);
        vCnx.buildTails(pos);
        final OnyxTail onyxTail = vCnx.getTail();
        
        final VirtualConnectionSubroutine opVCnx = new VirtualConnectionSubroutine(c, opColor, board);
        opVCnx.buildTails(opPos);
        final OnyxTail oponentTail = opVCnx.getTail();       
        
        final OnyxPos res = new VirtualTailCrossSearchResultsSubroutine().crossTailSearch(
            onyxTail, vCnx.getTails(), oponentTail, opVCnx.getTails(), board, c, color, opColor);
        
        if (res != null) {
            return initCaptures(new OnyxMove(res, OnyxConst.SCORE.VTAIL.getValue()), board, c, color);
        }
        
        return null;
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
