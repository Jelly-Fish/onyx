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
import com.jellyfish.jfgonyx.onyx.OnyxGame;
import com.jellyfish.jfgonyx.onyx.entities.OnyxMove;
import com.jellyfish.jfgonyx.onyx.entities.OnyxPos;
import com.jellyfish.jfgonyx.onyx.entities.collections.OnyxPosCollection;
import com.jellyfish.jfgonyx.onyx.search.searchutils.SearchUtils;
import com.jellyfish.jfgonyx.onyx.search.subroutines.abstractions.AbstractSubroutine;
import com.jellyfish.jfgonyx.ui.OnyxBoard;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author thw
 */
public class TailConnectionSubroutine extends AbstractSubroutine {
    
    private final static String BEST_CANDIDATE = "Candidate for start @ %s : [%s] score: %f";
    private final OnyxPosCollection c;
    private final OnyxBoard board;
    private final GraphicsConst.COLOR color;
    private final List<OnyxMove> candidates = new ArrayList<>();
    private final Set<String> keyCandidates = new HashSet();
    private final Set<String> checked = new HashSet();
    private OnyxPos startPos;
    private OnyxMove candidate;
    
    public TailConnectionSubroutine(final OnyxPosCollection c, final GraphicsConst.COLOR color, 
            final OnyxBoard board) {
        this.c = c;
        this.color = color;
        this.board = board;
    }
    
    public OnyxMove getTail(final OnyxPos p, final String kEx) {
        
        this.startPos = p;
        this.findTailPos(p, kEx);
        this.score();
        this.trim();
        if (this.candidate != null) print(p.getKey(), this.candidate, BEST_CANDIDATE);
        return this.candidate;
    }
    
    public List<OnyxMove> getTails(final OnyxPos p, final String kEx) {
        
        this.startPos = p;
        this.findTailPos(p, kEx);
        this.score();
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

    private void score() {

        final float boardLength = ((float) OnyxConst.BOARD_SIDE_SQUARE_COUNT) + 1f;
        float score = -1f;
        OnyxPos tmp = null, pos = null;

        for (String k : this.keyCandidates) {

            pos = c.getPosition(k);    
            if (tmp == null) {
                
                /**
                 * If first blood, then init tmp & score : /!\ must be done /!\ :
                 * If score stay's at -1f and 1st iteration is best move then it
                 * will be overrided by weaker move.
                 */
                tmp = pos;
                if (this.color.boolColor) {
                    if (this.startPos.isLowXBorder()) score = tmp.x;
                    else if (this.startPos.isHighXBorder()) score = boardLength - tmp.x;
                }
                
                if (!this.color.boolColor) {
                    if (this.startPos.isLowYBorder()) score = tmp.y;
                    else if (this.startPos.isHighYBorder()) score = boardLength - tmp.y;
                }
            } 

            if (this.color.boolColor) {
                if (this.startPos.isLowXBorder() && pos.x > tmp.x) {
                    score = pos.x;
                    tmp = pos;
                } else if (this.startPos.isHighXBorder() && pos.x < tmp.x) {
                    score = boardLength - pos.x;
                    tmp = pos;
                }
            }

            if (!this.color.boolColor) {
                if (this.startPos.isLowYBorder() && pos.y > tmp.y) {
                    score = pos.y;
                    tmp = pos;
                } else if (this.startPos.isHighYBorder() && pos.y < tmp.y) {
                    score = boardLength - pos.y;
                    tmp = pos;
                }
            }
        }
        
        this.candidate = new OnyxMove(tmp, score * OnyxConst.SCORE.TAIL.getValue());
        this.candidates.add(candidate);
    }
    
    private void trim() {
        
        OnyxMove tmp = null;
        for (OnyxMove m : this.candidates) {
            if (tmp == null) tmp = m;
            if (m.getScore() > tmp.getScore()) tmp = m;
        }
        this.candidate = tmp;
    }
    
    public OnyxMove getCandidate() {
        return candidate;
    }
    
}
