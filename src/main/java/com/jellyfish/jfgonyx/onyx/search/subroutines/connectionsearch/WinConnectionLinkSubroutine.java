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

import com.jellyfish.jfgonyx.constants.GraphicsConst;
import com.jellyfish.jfgonyx.constants.OnyxConst;
import com.jellyfish.jfgonyx.onyx.entities.OnyxMove;
import com.jellyfish.jfgonyx.onyx.entities.OnyxPiece;
import com.jellyfish.jfgonyx.onyx.entities.OnyxPos;
import com.jellyfish.jfgonyx.onyx.entities.collections.OnyxPosCollection;
import com.jellyfish.jfgonyx.onyx.search.searchutils.OnyxPositionUtils;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author thw
 */
public class WinConnectionLinkSubroutine extends WinConnectionSubroutine {
    
    private final static String WIN_MOVE = "[!] Win link position found @ %s";
    
    public WinConnectionLinkSubroutine(final OnyxPosCollection c, final GraphicsConst.COLOR color) {
        super(c, color);
    }
    
    public OnyxMove connectionLink(final List<OnyxMove> tails) {       
        
        final List<OnyxPos> borders = OnyxPositionUtils.trimByBorderStartPositionsByColor(
                OnyxPositionUtils.getBorders(this.c, this.color), this.color);
        
        OnyxPiece tmp = null;
        WinConnectionSubroutine search = null;
        for (OnyxMove m : tails) {
            
            tmp = new OnyxPiece(this.color);
            this.c.getPositions().get(m.getPos().getKey()).setPiece(tmp);

            for (OnyxPos p : borders) {
                search = new WinConnectionSubroutine(this.c, this.color);
                search.connection(p, p.getKey());
                if (search.isWin()) {
                    print(OnyxConst.POS_MAP.get(m.getPos().getKey()), WIN_MOVE);
                    this.c.getPositions().get(m.getPos().getKey()).setPiece(null);
                    return new OnyxMove(m.getPos(), OnyxConst.SCORE.WIN_LINK.getValue());
                }
            }
            
            this.c.getPositions().get(m.getPos().getKey()).setPiece(null);
        }
        
        return null;
    }
    
}
