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
package com.jellyfish.jfgonyx.onyx.search.subroutines.positionsearch;

import com.jellyfish.jfgonyx.constants.GraphicsConst;
import com.jellyfish.jfgonyx.constants.OnyxConst;
import com.jellyfish.jfgonyx.onyx.entities.OnyxDiamond;
import com.jellyfish.jfgonyx.onyx.entities.OnyxMove;
import com.jellyfish.jfgonyx.onyx.entities.OnyxPos;
import com.jellyfish.jfgonyx.onyx.entities.collections.OnyxPosCollection;
import com.jellyfish.jfgonyx.onyx.exceptions.NoValidOnyxPositionsFoundException;
import com.jellyfish.jfgonyx.onyx.search.searchutils.OnyxPositionUtils;
import com.jellyfish.jfgonyx.onyx.search.subroutines.abstractions.AbstractSubroutine;
import com.jellyfish.jfgonyx.onyx.search.subroutines.connectionsearch.TailConnectionSubroutine;
import com.jellyfish.jfgonyx.ui.OnyxBoard;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author thw
 */
public class CounterPositionSubroutine extends AbstractSubroutine {
    
    private final static String BEST_CANDIDATE = "Counter position for %s [%s] score: %s";
    
    /**
     * @param c Onyx position collection.
     * @param b Onyx board instance.
     * @param color COLOR.
     * @return Strongest counter move found to prevent sealing, take positions &
     * tail counter positions else NULL if no such position has been found.
     * @throws com.jellyfish.jfgonyx.onyx.exceptions.NoValidOnyxPositionsFoundException
     */
    public final OnyxMove getCounterPos(final OnyxPosCollection c, final OnyxBoard b, 
            final GraphicsConst.COLOR color) 
            throws NoValidOnyxPositionsFoundException {
        
        final List<OnyxMove> candidates = new ArrayList<>();
        candidates.add(this.smallLock(c, b, color.bit));
        candidates.add(this.counterPos(c, b, GraphicsConst.COLOR.getOposite(color.bool)));
        candidates.add(this.bigLock(c, b, color));
        
        for (OnyxMove m : candidates) {
            if (m != null) this.move = 
                (this.move == null || m.getScore() > this.move.getScore()) ? m : this.move;
        }
        
        if (this.move != null && this.move.hasPosition()) {
            print(color.str, this.move.getPos().getKey(), 
                    String.valueOf(this.move.getScore()),BEST_CANDIDATE);
        }
        
        return this.move;
    }
    
    private OnyxMove counterPos(final OnyxPosCollection c, final OnyxBoard b, 
            final GraphicsConst.COLOR color) throws NoValidOnyxPositionsFoundException {
        
        final List<OnyxPos> pos = OnyxPositionUtils.trimByAllBorderPositionsByColor(
                OnyxPositionUtils.getBorders(c, color), color);
        final List<OnyxMove> cnx = new ArrayList<>();
        final Set<String> sTt = new HashSet<>();
        OnyxMove tmp = null;
        
        for (OnyxPos p : pos) cnx.addAll(new TailConnectionSubroutine(c, color, b).getTails(p, p.getKey()));
        for (OnyxMove m : cnx) {
            if (m != null && m.hasPosition() && m.getPos().isSubjectToTake(b, c, color)) {
                sTt.add(m.getPos().getKey());
            }
            else tmp = m;
        }
        
        if (tmp == null) return null;
        
        for (OnyxMove m : cnx) {
            if (m != null && m.hasPosition() && !sTt.contains(m.getPos().getKey()) && 
                    m.getScore() >= tmp.getScore()) {
                tmp = m;
            }
        }
        
        return new OnyxMove(tmp.getPos(), tmp.getPiece(), OnyxConst.SCORE.COUNTER_POS.getValue());
    }
    
    private OnyxMove smallLock(final OnyxPosCollection c, final OnyxBoard b, final int bitColor) {
        
        int i, j;
        OnyxPos pos = null;
        String key = StringUtils.EMPTY;
        
        for (OnyxDiamond d : b.getDiamondCollection().getDiamonds().values()) {
            
            i = 0; j = 0;
            for (String k : d.getCornerKeys()) {
                pos = c.getPosition(k);
                if (pos.isOccupied()) {
                    if (pos.getPiece().color.bit == bitColor) {
                        ++i;
                    } else if (pos.getPiece().color.bit != bitColor) {
                        ++j;
                    }
                } else {
                    key = k;
                }
            }
            
            if (i == 2 && j == 1 && !c.getPosition(key).isOccupied()) {
                return new OnyxMove(c.getPosition(key), OnyxConst.SCORE.SMALL_LOCK.getValue());
            }
        }        
        
        return null;
    }
    
    private OnyxMove bigLock(final OnyxPosCollection c, final OnyxBoard b, 
            final GraphicsConst.COLOR color) throws NoValidOnyxPositionsFoundException {
        
        int i, j;
        OnyxPos pos = null;
        String key = StringUtils.EMPTY;
        
        for (OnyxDiamond d : b.getDiamondCollection().getDiamonds().values()) {
            
            i = 0; j = 0;
            for (String k : d.getCornerKeys()) {
                pos = c.getPosition(k);
                if (pos.isOccupied()) {
                    if (pos.getPiece().color.bit == color.bit) {
                        ++i;
                    } else if (pos.getPiece().color.bit != color.bit) {
                        ++j;
                    }
                } else {
                    key = k;
                }
            }
            
            if (i == 2 && j == 1 && !c.getPosition(key).isOccupied() && 
                    c.getPosition(key).isSubjectToTake(b, c, color)) {
                return new OnyxMove(c.getPosition(key), OnyxConst.SCORE.BIG_LOCK.getValue());
            }
        }        
        
        return null;
    }
    
}
