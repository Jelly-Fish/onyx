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
package com.jellyfish.jfgonyx.onyx.search.subroutines.connectionsearch;

import com.jellyfish.jfgonyx.onyx.constants.OnyxConst;
import com.jellyfish.jfgonyx.onyx.entities.OnyxMove;
import com.jellyfish.jfgonyx.onyx.entities.OnyxPiece;
import com.jellyfish.jfgonyx.onyx.entities.OnyxPos;
import com.jellyfish.jfgonyx.onyx.entities.collections.OnyxPosCollection;
import com.jellyfish.jfgonyx.onyx.search.searchutils.MoveUtils;
import com.jellyfish.jfgonyx.onyx.search.searchutils.OnyxPositionUtils;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author thw
 */
public class WinConnectionLinkSubroutine extends WinConnectionSubroutine {
        
    public WinConnectionLinkSubroutine(final OnyxPosCollection c, final OnyxConst.COLOR color) {
        super(c, color);
    }
    
    public OnyxMove connectionLink(final List<OnyxMove> tails) {       
        
        final List<OnyxPos> borderTails = new ArrayList<>();
        final List<OnyxPos> borders = OnyxPositionUtils.getByBorderStartPositionsAndColor(
                OnyxPositionUtils.getBordersByColor(c, color), color);
        OnyxPiece tmp = null;
        WinConnectionSubroutine search = null;
       
        /**
         * If tail is on a start border then add to borders - connection link
         * search is uses only low borders for search : if tail is a low
         * border (if black & x=1f or white & y=1y) then add to boder collection
         * local final List<OnyxPos> borders.
         */
        for (OnyxMove m : tails) {
            if ((color.bool && MoveUtils.isMove(m) && m.getPos().x < 1.1f) || 
                (!color.bool && MoveUtils.isMove(m) && m.getPos().y < 1.1f)) {
                borders.add(m.getPos());
                borderTails.add(m.getPos());
            }
        }

        for (OnyxMove m : tails) {
            
            if (MoveUtils.isNotMove(m) || !m.hasPosition()) continue;

            tmp = new OnyxPiece(color);
            c.getPositions().get(m.getPos().getKey()).setPiece(tmp);

            for (OnyxPos p : borders) {
                
                search = new WinConnectionSubroutine(c, color, false);
                search.connection(p, p.getKey());
                if (search.isWin() && !borderTails.contains(p)) {
                    c.getPositions().get(m.getPos().getKey()).setPiece(null);
                    return new OnyxMove(m.getPos(), OnyxConst.SCORE.WIN_LINK.getValue(), true);
                } else if (search.isWin() && borderTails.contains(p)) {
                    
                    /**
                     * If border tail (meaning the tails that on a low border
                     * (y OR x strictly smaller than 1.1f)) then the tmp position
                     * must be set to null to remove link simulation BUT the 
                     * position to return is not in tail list (tails here) but
                     * is the border position.
                     */
                    c.getPositions().get(m.getPos().getKey()).setPiece(null);
                    return new OnyxMove(p, OnyxConst.SCORE.WIN_LINK.getValue(), true);
                }
            }
            
            c.getPositions().get(m.getPos().getKey()).setPiece(null);
        }
        
        return null;
    }
    
}
