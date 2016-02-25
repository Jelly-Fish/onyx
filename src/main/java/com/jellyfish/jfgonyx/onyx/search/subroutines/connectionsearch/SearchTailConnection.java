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
import com.jellyfish.jfgonyx.onyx.entities.OnyxPos;
import com.jellyfish.jfgonyx.onyx.entities.collections.OnyxPosCollection;
import com.jellyfish.jfgonyx.onyx.search.subroutines.abstractions.AbstractSubroutine;
import com.jellyfish.jfgonyx.ui.OnyxBoard;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * @author thw
 */
public class SearchTailConnection extends AbstractSubroutine {
    
    private final static String LAMBDA_CANDIDATE = " :: Lambda candidate for start @ %s : [%s]";
    private final static String BEST_CANDIDATE = " :: Candidate for start @ %s : [%s]";
    private final OnyxPosCollection c;
    private final OnyxBoard board;
    private final GraphicsConst.COLOR color;
    private final LinkedList<OnyxMove> candidates = new LinkedList<>();
    private final Set<String> keyCandidates = new HashSet();
    private final Set<String> checked = new HashSet();
    private OnyxPos startPos;
    private OnyxMove candidate;
    
    public SearchTailConnection(final OnyxPosCollection c, final GraphicsConst.COLOR color, final OnyxBoard board) {
        this.c = c;
        this.color = color;
        this.board = board;
    }
    
    public List<OnyxMove> getTails(final OnyxPos p, final String kEx) {
        
        this.startPos = p;
        this.findTailPos(p, kEx);
        this.trim();
        print(p.getKey(), candidates, LAMBDA_CANDIDATE);
        print(p.getKey(), this.candidate, BEST_CANDIDATE);
        
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
    
    private void trim() {

        float score = -1f;
        OnyxPos tmp = null, pos = null;
        for (String k : this.keyCandidates) {
            
            this.candidates.add(new OnyxMove(c.getPosition(k), true));
            if (tmp == null) tmp = c.getPosition(k);            
            pos = c.getPosition(k);
            
            if (this.color.boolColor && ((this.startPos.isLowXBorder() && pos.x >= tmp.x) ||
                    (this.startPos.isHighXBorder() && pos.x <= tmp.x))) {
                tmp = pos;
                score = this.startPos.isLowXBorder() ? pos.x :
                        Math.abs((pos.x - OnyxConst.BOARD_SIDE_SQUARE_COUNT + 1));
            }
            
            if (!this.color.boolColor && ((this.startPos.isLowYBorder() && pos.y >= tmp.y) ||
                    (this.startPos.isHighYBorder() && pos.y <= tmp.y))) {
                tmp = pos;
                score = this.startPos.isLowYBorder() ? pos.y :
                        Math.abs((pos.y - OnyxConst.BOARD_SIDE_SQUARE_COUNT + 1));
            }
        }

        this.candidate = new OnyxMove(tmp, false, score * OnyxConst.SCORE.TAIL.getValue());
        this.candidates.add(candidate);
    }
    
}
