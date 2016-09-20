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

import com.jellyfish.jfgonyx.onyx.OnyxGameImpl;
import com.jellyfish.jfgonyx.onyx.constants.OnyxConst;
import com.jellyfish.jfgonyx.onyx.entities.OnyxDiamond;
import com.jellyfish.jfgonyx.onyx.entities.OnyxMove;
import com.jellyfish.jfgonyx.onyx.entities.OnyxPos;
import com.jellyfish.jfgonyx.onyx.exceptions.InvalidOnyxPositionException;
import com.jellyfish.jfgonyx.onyx.abstractions.AbstractSubroutine;
import java.util.ArrayList;
import java.util.List;

/**
 * @author thw
 */
public class TakePositionSubroutine extends AbstractSubroutine {
    
    /**
     * @param game
     * @param bitColor the color to play's bit value (0=white, 1=black).
     * @return Strongest take move key found or NULL if no such position has been found.
     * @throws com.jellyfish.jfgonyx.onyx.exceptions.InvalidOnyxPositionException
     */
    public final OnyxMove getTakePos(final OnyxGameImpl game, 
            final int bitColor) throws InvalidOnyxPositionException {
        
        int count, i;
        OnyxPos[] positions = new OnyxPos[4];
        final List<OnyxPos> posSet = new ArrayList<>();
        
        for (OnyxDiamond d : game.getDiamondCollection().getDiamonds().values()) {
            
            if (d.isFivePosDiamond() && game.getPosCollection().getPosition(
                d.getCenterPos().getKey()).isOccupied()) {
                continue;
            }
            
            count = 0;
            i = 0;
            for (String k : d.getCornerKeys()) {
                positions[i] = game.getPosCollection().getPosition(k);
                count = game.getPosCollection().getPosition(positions[i].getKey()).isOccupied() ? ++count : count;
                ++i;
            }
            
            if (game.getPosCollection().getPosition(positions[0].getKey()).isOccupied() && 
                    game.getPosCollection().getPosition(positions[2].getKey()).isOccupied() &&
                    game.getPosCollection().getPosition(positions[0].getKey()).getPiece().color.bit != bitColor && 
                    game.getPosCollection().getPosition(positions[2].getKey()).getPiece().color.bit != bitColor) { 
                
                if (game.getPosCollection().getPosition(positions[3].getKey()).isOccupied() && 
                        game.getPosCollection().getPosition(positions[3].getKey()).getPiece().color.bit == bitColor &&
                        !game.getPosCollection().getPosition(positions[1].getKey()).isOccupied()) {
                    posSet.add(positions[1]);
                } else if (game.getPosCollection().getPosition(positions[1].getKey()).isOccupied() && 
                        game.getPosCollection().getPosition(positions[1].getKey()).getPiece().color.bit == bitColor &&
                        !game.getPosCollection().getPosition(positions[3].getKey()).isOccupied()) {
                    posSet.add(game.getPosCollection().getPosition(positions[3].getKey()));
                }
            } else if (game.getPosCollection().getPosition(positions[1].getKey()).isOccupied() && 
                    game.getPosCollection().getPosition(positions[3].getKey()).isOccupied() &&
                    game.getPosCollection().getPosition(positions[1].getKey()).getPiece().color.bit != bitColor && 
                    game.getPosCollection().getPosition(positions[3].getKey()).getPiece().color.bit != bitColor) {
                
                if (game.getPosCollection().getPosition(positions[2].getKey()).isOccupied() && 
                    game.getPosCollection().getPosition(positions[2].getKey()).getPiece().color.bit == bitColor &&
                        !game.getPosCollection().getPosition(positions[0].getKey()).isOccupied()) {
                    posSet.add(game.getPosCollection().getPosition(positions[0].getKey()));
                } else if (game.getPosCollection().getPosition(positions[0].getKey()).isOccupied() && 
                        game.getPosCollection().getPosition(positions[0].getKey()).getPiece().color.bit == bitColor &&
                        !game.getPosCollection().getPosition(positions[2].getKey()).isOccupied()) {
                    posSet.add(game.getPosCollection().getPosition(positions[2].getKey()));
                }
            }
        }
        
        if (posSet.size() <= 0) return null;
        if (posSet.size() == 1) move = new OnyxMove(posSet.get(0), 
                posSet.get(0).getPiece(), posSet, OnyxConst.SCORE.TAKE.getValue());
        
        count = 0;
        i = -1;
        int tmpCount = 0;
        for (OnyxPos p : posSet) {
            tmpCount = p.occursCount(p, posSet);
            if (tmpCount > count) {
                count = tmpCount;
                i = posSet.indexOf(p);
            }            
        }
        
        if (i > -1) move = new OnyxMove(posSet.get(i), posSet.get(i).getPiece(), 
                posSet, OnyxConst.SCORE.TAKE.getValue());
        
        return move;
    }
    
}
