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

import com.jellyfish.jfgonyx.constants.OnyxConst;
import com.jellyfish.jfgonyx.onyx.OnyxGame;
import com.jellyfish.jfgonyx.onyx.entities.OnyxMove;
import com.jellyfish.jfgonyx.onyx.entities.OnyxPos;
import com.jellyfish.jfgonyx.onyx.entities.collections.OnyxPosCollection;
import com.jellyfish.jfgonyx.onyx.exceptions.InvalidOnyxPositionException;
import com.jellyfish.jfgonyx.onyx.search.searchutils.MoveUtils;
import com.jellyfish.jfgonyx.onyx.abstractions.AbstractSubroutine;
import com.jellyfish.jfgonyx.onyx.search.subroutines.positionsearch.OnyxPosStateSubroutine;
import com.jellyfish.jfgonyx.ui.OnyxBoard;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author thw
 */
public class TailConnectionSubroutine extends AbstractSubroutine {
    
    protected AbstractSubroutine.SUBROUTINE_TYPE type;
    protected final OnyxPosCollection c;
    protected final OnyxBoard board;
    protected final OnyxConst.COLOR color;
    protected final List<OnyxMove> candidates = new ArrayList<>();
    protected final Set<String> keyCandidates = new HashSet();
    private final Set<String> checkedKeys = new HashSet();
    protected OnyxPos startPos;
    protected OnyxMove candidate;
    protected int links = 0;
    protected OnyxPos tail = null;
    
    public TailConnectionSubroutine(final OnyxPosCollection c, final OnyxConst.COLOR color, 
            final OnyxBoard board) {
        this.c = c;
        this.color = color;
        this.board = board;
        this.type = AbstractSubroutine.SUBROUTINE_TYPE.TAIL;
    }
    
    /**
     * @param p tail search start position, this position will be used to
     * determinate start position region.
     * @return OnyxMove instances.
     * @throws com.jellyfish.jfgonyx.onyx.exceptions.InvalidOnyxPositionException
     */
    public OnyxMove getTail(final OnyxPos p) throws InvalidOnyxPositionException {
        
        this.startPos = p;
        this.tail = this.findTail(p, p.getKey());
        this.score();
        if (MoveUtils.isMove(this.candidate)) print(AbstractSubroutine.BEST_CANDIDATE_TAIL_FORMAT, 
                p.getKey(), this.type, this.color.str, this.candidate);
        return this.candidate;
    }
    
    /**
     * @param p tail search start position, this position will be used to
     * determinate start position region.
     * @return list of OnyxMove instances.
     * @see OnyxMove
     */
    public List<OnyxMove> getTails(final OnyxPos p) {
        
        this.startPos = p;
        this.tail = this.findTail(p, p.getKey());
        this.score();
        this.addStartPositionToCandidates();
        return this.candidates;
    }

    private OnyxPos findTail(final OnyxPos p, final String kEx) {       
        
        ++this.links;
        this.checkedKeys.add(p.getKey());
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
                if (tmp.isOccupied() && tmp.getPiece().color.bit == this.color.bit 
                        && !this.checkedKeys.contains(tmp.getKey())) {
                    this.findTail(c.getPosition(k), k);
                }
            }
        }
        
        return p;
    }

    protected void score() {

        final boolean lowBorderTendency = 
                OnyxGame.getInstance().getLowBorderTendency(
                        OnyxConst.COLOR.getOposite(this.startPos.getPiece().color.bool));
        
        final float boardLength = ((float) OnyxConst.BOARD_SIDE_SQUARE_COUNT) + 1f;
        float score = -1f;
        OnyxPos tmp = null, pos = null;

        for (String k : this.keyCandidates) {
            
            pos = this.c.getPosition(k);    
            if (tmp == null) {
                
                /**
                 * If first blood, then init tmp & score : /!\ must be done /!\ :
                 * If score stay's at -1f and 1st iteration is best move then it
                 * will be overrided by weaker move.
                 */
                tmp = pos;
                
                if (this.color.bool) {                   
                    if (this.startPos.isLowXBorder()) score = tmp.x;
                    else if (this.startPos.isHighXBorder()) score = boardLength - tmp.x;
                    score = lowBorderTendency ? (tmp.y > (boardLength / 2) ? (score + 1f) : score) : score; 
                }
                
                if (!this.color.bool) {
                    if (this.startPos.isLowYBorder()) score = tmp.y;
                    else if (this.startPos.isHighYBorder()) score = boardLength - tmp.y;
                    score = lowBorderTendency ? (tmp.x > (boardLength / 2) ? (score + 1f) : score) : score;
                }
            } else {

                if (this.color.bool) {

                    if (this.startPos.isLowXBorder() && pos.x >= tmp.x) {
                        score = pos.x;
                        tmp = pos;
                        score = lowBorderTendency ? (tmp.y > (boardLength / 2) ? (score + 1f) : score) : score;
                    } else if (this.startPos.isHighXBorder() && pos.x <= tmp.x) {
                        score = boardLength - pos.x;
                        tmp = pos;
                        score = lowBorderTendency ? (tmp.y > (boardLength / 2) ? (score + 1f) : score) : score;
                    }
                }

                if (!this.color.bool) {
                    
                    if (this.startPos.isLowYBorder() && pos.y >= tmp.y) {
                        score = pos.y;
                        tmp = pos;
                        score = lowBorderTendency ? (tmp.x > (boardLength / 2) ? (score + 1f) : score) : score;
                    } else if (this.startPos.isHighYBorder() && pos.y <= tmp.y) {
                        score = boardLength - pos.y;
                        tmp = pos;
                        score = lowBorderTendency ? (tmp.x > (boardLength / 2) ? (score + 1f) : score) : score;
                    }                    
                }
            }
        }
        
        this.candidate = new OnyxMove(tmp, score * OnyxConst.SCORE.TAIL.getValue());
        this.candidates.add(candidate);
    }
    
    private void addStartPositionToCandidates() {
        for (OnyxMove m : this.candidates) m.setTailStartPos(this.startPos);
    }
    
    public OnyxMove getCandidate() {
        return candidate;
    }
    
    public Set<String> getCheckedKeys() {
        return checkedKeys;
    }
    
    public int getLinks() {
        return links;
    }
    
}
