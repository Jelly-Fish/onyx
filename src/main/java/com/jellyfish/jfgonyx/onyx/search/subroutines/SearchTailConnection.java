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
package com.jellyfish.jfgonyx.onyx.search.subroutines;

import com.jellyfish.jfgonyx.constants.GraphicsConst;
import com.jellyfish.jfgonyx.constants.OnyxConst;
import com.jellyfish.jfgonyx.onyx.entities.OnyxMove;
import com.jellyfish.jfgonyx.onyx.entities.OnyxPos;
import com.jellyfish.jfgonyx.onyx.entities.collections.OnyxPosCollection;
import com.jellyfish.jfgonyx.ui.MainFrame;
import com.jellyfish.jfgonyx.ui.OnyxBoard;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author thw
 */
public class SearchTailConnection {
    
    private final OnyxPosCollection c;
    private final OnyxBoard board;
    private final GraphicsConst.COLOR color;
    private final List<OnyxMove> candidates = new ArrayList<>();
    private final Set<String> keyCandidates = new HashSet();
    private final Set<String> checked = new HashSet();
    
    public SearchTailConnection(final OnyxPosCollection c, final GraphicsConst.COLOR color, final OnyxBoard board) {
        this.c = c;
        this.color = color;
        this.board = board;
    }
    
    public List<OnyxMove> getTails(final OnyxPos p, final String kEx) {
        
        this.findTailPos(p, kEx);
        
        for (String k : this.keyCandidates) {
            this.candidates.add(new OnyxMove(c.getPosition(k)));
        }

        this.print(p.getKey(), candidates);
        
        return this.candidates;
    }

    private void findTailPos(final OnyxPos p, final String kEx) {       
        
        this.checked.add(p.getKey());
        OnyxPos tmp = null;
        for (String k : p.connections) {
            if (!k.equals(kEx)) {
                tmp = c.getPosition(k);
                if (!tmp.isOccupied() && c.isValidMove(tmp, board, color)) {
                    this.keyCandidates.add(k);
                }
            } 
        }
        
        for (String k : p.connections) {
            if (!k.equals(kEx)) {
                tmp = c.getPosition(k);
                if (tmp.isOccupied() && tmp.getPiece().color.bitColor == this.color.bitColor 
                        && !this.checked.contains(tmp.getKey())) {
                    this.findTailPos(c.getPosition(k), k);
                }
            }
        }
    }
    
    private void print(final String sK, final List<OnyxMove> candidates) {
        for (OnyxMove m : candidates) {
            MainFrame.print(String.format(">> Candidate for start @ %s : [%s]",
                    OnyxConst.POS_MAP.get(sK), OnyxConst.POS_MAP.get(m.getPos().getKey())));
        }
    }
    
}
