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

import com.jellyfish.jfgonyx.onyx.constants.OnyxConst;
import com.jellyfish.jfgonyx.onyx.OnyxGameImpl;
import com.jellyfish.jfgonyx.onyx.entities.OnyxMove;
import com.jellyfish.jfgonyx.onyx.entities.OnyxPos;
import com.jellyfish.jfgonyx.onyx.entities.collections.OnyxPosCollection;
import com.jellyfish.jfgonyx.onyx.exceptions.InvalidOnyxPositionException;
import com.jellyfish.jfgonyx.onyx.exceptions.NoValidOnyxPositionsFoundException;
import com.jellyfish.jfgonyx.onyx.search.searchutils.OnyxMoveUtils;
import com.jellyfish.jfgonyx.onyx.search.searchutils.OnyxPositionUtils;
import com.jellyfish.jfgonyx.onyx.abstractions.AbstractSubroutine;
import com.jellyfish.jfgonyx.onyx.entities.OnyxDiamond;
import com.jellyfish.jfgonyx.onyx.search.subroutines.connectionsearch.SubTailConnectionSubroutine;
import com.jellyfish.jfgonyx.onyx.search.subroutines.connectionsearch.TailConnectionSubroutine;
import com.jellyfish.jfgonyx.onyx.vars.GraphicsVars;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

/**
 * @author thw
 */
public class CounterPositionSubroutine extends AbstractSubroutine {

    /**
     * @param game
     * @param color COLOR.
     * @return Strongest counter move found to prevent sealing, take positions &
     * tail counter positions else NULL if no such position has been found.
     * @throws com.jellyfish.jfgonyx.onyx.exceptions.NoValidOnyxPositionsFoundException if shit hits the fan.
     * @throws com.jellyfish.jfgonyx.onyx.exceptions.InvalidOnyxPositionException if shit hits the fan.
     */
    public final OnyxMove getCounterPos(final OnyxGameImpl game, final OnyxConst.COLOR color) throws NoValidOnyxPositionsFoundException, InvalidOnyxPositionException {
        
        final List<OnyxMove> candidates = new ArrayList<>();
        candidates.add(counterPos(game.getPosCollection(), game, color));
        candidates.add(bigLock(game.getPosCollection(), game, color));
        
        move = trim(candidates, game, color, .1f);
        
        return move;
    }
    
    private OnyxMove counterPos(final OnyxPosCollection c, final OnyxGameImpl game, 
            final OnyxConst.COLOR color) throws NoValidOnyxPositionsFoundException, InvalidOnyxPositionException {
        
        final OnyxConst.COLOR opColor = OnyxConst.COLOR.getOposite(color.bool);
        final List<OnyxPos> pos = OnyxPositionUtils.getAllExternalBordersByColor(
                OnyxPositionUtils.getBordersByColor(c, opColor), opColor);
        final List<OnyxMove> cnx = new ArrayList<>();
        OnyxMove tmp = null;

        for (OnyxPos p : pos) cnx.addAll(new TailConnectionSubroutine(opColor, game).getTailMoves(p, true));
        for (OnyxPos p : pos) {
            cnx.addAll(new SubTailConnectionSubroutine(opColor, game, 1f, 1f, 
            GraphicsVars.getInstance().BOARD_SIDE_POS_COUNT - 1f, 
            GraphicsVars.getInstance().BOARD_SIDE_POS_COUNT - 1f).getTailMoves(p, true));
        }
        
        tmp = trim(cnx, game, opColor, .01f);
        if (OnyxMoveUtils.isNotMove(tmp) || !tmp.hasPosition()) return null;
        
       game.updateTailTendency(tmp);

        return new OnyxMove(tmp.getPos(), tmp.getPiece(), tmp.getScore());
    }
    
    /**
     * This lock will counter a take position that oponent could play resulting
     * in a take or double take outcome.
     * @param c onyx position collection.
     * @param b onyx board.
     * @param color color to search for.
     * @return OnyxMove to play if found.
     * @throws NoValidOnyxPositionsFoundException if shit hits the fan.
     * @throws InvalidOnyxPositionException if shit hits the fan.
     */
    private OnyxMove bigLock(final OnyxPosCollection c, final OnyxGameImpl game, 
            final OnyxConst.COLOR color) throws NoValidOnyxPositionsFoundException, InvalidOnyxPositionException {
        
        int i, j;
        OnyxPos pos = null;
        String key = StringUtils.EMPTY;
        
        for (OnyxDiamond d : game.getDiamondCollection().getDiamonds().values()) {
            
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
                c.getPosition(key).posHelper.isSubjectToTake(
                game.getDiamondCollection(), game.getPosCollection(), color)) {
                
                return new OnyxMove(c.getPosition(key), OnyxConst.SCORE.BIG_LOCK.getValue());
            }
        }        
        
        return null;
    }
    
}
