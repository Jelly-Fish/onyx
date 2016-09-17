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

import com.jellyfish.jfgonyx.onyx.OnyxGame;
import com.jellyfish.jfgonyx.onyx.constants.OnyxConst;
import com.jellyfish.jfgonyx.onyx.entities.OnyxDiamond;
import com.jellyfish.jfgonyx.onyx.entities.OnyxMove;
import com.jellyfish.jfgonyx.onyx.entities.OnyxPos;
import com.jellyfish.jfgonyx.onyx.exceptions.NoValidOnyxPositionsFoundException;
import com.jellyfish.jfgonyx.onyx.search.searchutils.OnyxMoveUtils;
import com.jellyfish.jfgonyx.onyx.abstractions.AbstractSubroutine;

/**
 *
 * @author thw
 */
public class NeighbourPositionSubroutine extends AbstractSubroutine {
    
    /**
     * @param game
     * @param bitColor the color to play's bit value (0=white, 1=black).
     * @return Neighbor move found or NULL if no such position has been found.
     * @throws com.jellyfish.jfgonyx.onyx.exceptions.NoValidOnyxPositionsFoundException
     */
    public final OnyxMove getNeighbourPos(final OnyxGame game, 
            final int bitColor) throws NoValidOnyxPositionsFoundException {
        
        OnyxPos pos = null;
        
        for (OnyxDiamond d : game.getDiamondCollection().getDiamonds().values()) {
            
            for (String k : d.getCornerKeys()) {
                
                pos = game.getPosCollection().getPosition(k);
                if (pos.isOccupied() && pos.getPiece().color.bit == bitColor) {
                    
                    for (String cnxK : pos.connections) {
                        
                        if (!game.getPosCollection().getPosition(cnxK).isDiamondCenter() && 
                            !game.getPosCollection().getPosition(cnxK).isOccupied()) {
                            
                            move = new OnyxMove(game.getPosCollection().getPosition(cnxK), 
                                OnyxConst.SCORE.NEIGHBOUR.getValue());                            
                            if (OnyxMoveUtils.isMove(move)) return move;
                        }
                    }
                }
            }
        }      
        
        return null;
    }
    
}
