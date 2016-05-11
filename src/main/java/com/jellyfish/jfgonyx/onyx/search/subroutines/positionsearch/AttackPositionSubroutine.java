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
import com.jellyfish.jfgonyx.onyx.entities.OnyxDiamond;
import com.jellyfish.jfgonyx.onyx.entities.OnyxMove;
import com.jellyfish.jfgonyx.onyx.entities.OnyxPos;
import com.jellyfish.jfgonyx.onyx.entities.collections.OnyxPosCollection;
import com.jellyfish.jfgonyx.onyx.search.searchutils.MoveUtils;
import com.jellyfish.jfgonyx.onyx.abstractions.AbstractSubroutine;
import com.jellyfish.jfgonyx.ui.OnyxBoard;

/**
 *
 * @author thw
 */
public class AttackPositionSubroutine extends AbstractSubroutine {

    /**
     * @param c Onyx position collection.
     * @param b Onyx board instance.
     * @param bitColor the color to play's bit value (0=white, 1=black).
     * @return Strongest counter attack move found (to allow take on next move) 
     * or NULL if no such position has been found.
     */
    public final OnyxMove getAttackPos(final OnyxPosCollection c, final OnyxBoard b, final int bitColor) {

        int[] iPos = null; 
        OnyxPos tmp = null;
        String[] keys = null;
        
        for (OnyxDiamond d : b.getDiamondCollection().getDiamonds().values()) {
            
            iPos = new int[] { 0, 0, 0, 0 };
            keys = d.getCornerKeys();
            for (int i = 0; i < keys.length; ++i) {
                tmp = c.getPosition(keys[i]);
                iPos[i] = tmp.isOccupied() && tmp.getPiece().color.bit != bitColor ? 
                        1 : 0;
            }
            
            if (iPos[0] + iPos[2] == 2 && iPos[1] + iPos[3] == 0 && !c.getPosition(keys[1]).isOccupied()) {
                this.move = new OnyxMove(c.getPosition(keys[1]), OnyxConst.SCORE.ATTACK.getValue());
                break;
            }
            
            if (iPos[1] + iPos[3] == 2 && iPos[0] + iPos[2] == 0 && !c.getPosition(keys[0]).isOccupied()) {
                this.move = new OnyxMove(c.getPosition(keys[0]), OnyxConst.SCORE.ATTACK.getValue());
                break;
            }
        }
        
        if (MoveUtils.isMove(this.move)) print(AbstractSubroutine.BEST_CANDIDATE, 
                AbstractSubroutine.SUBROUTINE_TYPE.ATTACK, 
                bitColor == 0 ? OnyxConst.COLOR.WHITE : OnyxConst.COLOR.BLACK, 
                this.move.getPos().getKey());
        
        return this.move;
    }
    
}
