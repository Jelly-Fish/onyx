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
import com.jellyfish.jfgonyx.onyx.OnyxGameImpl;
import com.jellyfish.jfgonyx.onyx.entities.OnyxMove;
import com.jellyfish.jfgonyx.onyx.entities.OnyxPos;
import com.jellyfish.jfgonyx.onyx.entities.collections.OnyxPosCollection;
import com.jellyfish.jfgonyx.onyx.exceptions.InvalidOnyxPositionException;
import com.jellyfish.jfgonyx.onyx.search.searchutils.OnyxMoveUtils;
import com.jellyfish.jfgonyx.onyx.abstractions.AbstractSubroutine;
import com.jellyfish.jfgonyx.onyx.search.searchutils.OnyxPositionUtils;
import com.jellyfish.jfgonyx.onyx.vars.OnyxCommonVars;
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
    protected final OnyxGameImpl game;
    protected final OnyxConst.COLOR color;
    protected final List<OnyxMove> candidates = new ArrayList<>();
    protected final Set<String> keyCandidates = new HashSet();
    private final Set<String> checkedKeys = new HashSet();
    protected OnyxPos startPos;
    protected OnyxMove candidate;
    protected int links = 0;
    protected OnyxPos tail = null;
    protected boolean counterSearch = false;
    
    public TailConnectionSubroutine(final OnyxConst.COLOR color, 
            final OnyxGameImpl game) {
        
        this.c = game.getPosCollection();
        this.color = color;
        this.game = game;
        this.type = AbstractSubroutine.SUBROUTINE_TYPE.TAIL;
    }
    
    /**
     * @param p tail search start position, this position will be used to
     * determinate start position region.
     * @param counterSearch
     * @return OnyxMove instances.
     * @throws com.jellyfish.jfgonyx.onyx.exceptions.InvalidOnyxPositionException
     */
    public OnyxMove getTailMove(final OnyxPos p, final boolean counterSearch) throws InvalidOnyxPositionException {
        
        startPos = p;
        this.counterSearch = counterSearch;
        tail = findTailMove(p, p.getKey());
        score();
        candidate = trim(candidates, game, color);
        
        return candidate;
    }
    
    /**
     * @param p tail search start position, this position will be used to
     * determinate start position region.
     * @param counterSearch
     * @return list of OnyxMove instances.
     * @throws com.jellyfish.jfgonyx.onyx.exceptions.InvalidOnyxPositionException
     * @see OnyxMove
     */
    public List<OnyxMove> getTailMoves(final OnyxPos p, final boolean counterSearch) throws InvalidOnyxPositionException {
        
        startPos = p;
        this.counterSearch = counterSearch;
        tail = findTailMove(p, p.getKey());
        score();
        addStartPositionToCandidates();
        
        if (counterSearch) {
            candidate = trim(candidates, game, color);
        }
        
        return candidates;
    }

    private OnyxPos findTailMove(final OnyxPos p, final String kEx) {       
        
        ++links;
        checkedKeys.add(p.getKey());
        OnyxPos tmp = null;
        
        for (String k : p.connections) {
            
            if (!k.equals(kEx)) {
                
                tmp = c.getPosition(k);
                
                if (!tmp.isOccupied() && c.isValidMove(tmp, game.getDiamondCollection())) keyCandidates.add(k);
                
                if (tmp.isOccupied() && tmp.getPiece().color.bit == color.bit 
                        && !checkedKeys.contains(tmp.getKey())) {
                    findTailMove(c.getPosition(k), k);
                }
            } 
        }
        
        return p;
    }

    protected void score() {

        final boolean lowBorderTendency = game.getLowBorderTendency(
            OnyxConst.COLOR.getOposite(startPos.getPiece().color.bool));
        
        final float gameLength = ((float) OnyxCommonVars.getInstance().BOARD_SIDE_SQUARE_COUNT) + 1f;
        float score = 0f;
        OnyxPos tmp = null, pos = null;

        for (String k : keyCandidates) {
            
            score = 0f;
            pos = c.getPosition(k);    
            if (tmp == null) tmp = pos;

            if (color.bool) {
                
                if (startPos.isLowXBorder() && pos.x >= tmp.x) {
                    score = pos.x;
                    tmp = pos;
                    score = lowBorderTendency ? (tmp.y > (gameLength / 2) ? (score + 1f) : score) : score;
                } else if (startPos.isHighXBorder() && pos.x <= tmp.x) {
                    score = gameLength - pos.x;
                    tmp = pos;
                    score = lowBorderTendency ? (tmp.y > (gameLength / 2) ? (score + 1f) : score) : score;
                }
                
                /**
                 * FIXME : not obvious - figure out best way to upgrade best move.
                 */
                if (counterSearch && pos.x == tmp.x) {
                    score *= (1.4f + 
                        (pos.hasNeighbour(c, OnyxConst.COLOR.getOposite(color.bool)) ? .6f : 0f));
                }
                
            } else if (!color.bool) {

                if (startPos.isLowYBorder() && pos.y >= tmp.y) {
                    score = pos.y;
                    tmp = pos;
                    score = lowBorderTendency ? (tmp.x > (gameLength / 2) ? (score + 1f) : score) : score;
                } else if (startPos.isHighYBorder() && pos.y <= tmp.y) {
                    score = gameLength - pos.y;
                    tmp = pos;
                    score = lowBorderTendency ? (tmp.x > (gameLength / 2) ? (score + 1f) : score) : score;
                }      
                
                /**
                 * FIXME : not obvious - figure out best way to upgrade best move.
                 */
                if (counterSearch && pos.y == tmp.y) {
                    score *= (1.4f + (pos.hasNeighbour(c, OnyxConst.COLOR.getOposite(color.bool)) ? .6f : 0f));
                }
            }
            
            if (OnyxPositionUtils.isPosition(tmp)) candidates.add(
                    new OnyxMove(tmp, score * OnyxConst.SCORE.TAIL.getValue()));
        }
    }
    
    private void addStartPositionToCandidates() {
        for (OnyxMove m : candidates) {
            if (OnyxMoveUtils.isMove(m)) m.setTailStartPos(startPos);
        }
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
