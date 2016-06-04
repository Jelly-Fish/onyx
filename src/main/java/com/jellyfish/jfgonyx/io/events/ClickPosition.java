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
package com.jellyfish.jfgonyx.io.events;

import com.jellyfish.jfgonyx.vars.GraphicsVars;
import com.jellyfish.jfgonyx.onyx.constants.OnyxConst;
import com.jellyfish.jfgonyx.helpers.HTMLDisplayHelper;
import com.jellyfish.jfgonyx.onyx.OnyxGame;
import com.jellyfish.jfgonyx.onyx.entities.OnyxMove;
import com.jellyfish.jfgonyx.onyx.entities.OnyxPiece;
import com.jellyfish.jfgonyx.onyx.entities.OnyxPos;
import com.jellyfish.jfgonyx.onyx.entities.OnyxVirtualPiece;
import com.jellyfish.jfgonyx.onyx.exceptions.InvalidOnyxPositionException;
import com.jellyfish.jfgonyx.onyx.interfaces.OnyxExecutable;
import com.jellyfish.jfgonyx.ui.OnyxBoard;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * @author thw
 */
public class ClickPosition implements OnyxExecutable {

    private static ClickPosition instance = null;
    private OnyxVirtualPiece vBackup = null;
    
    @Override
    public boolean exec(final InputEvent e, final OnyxBoard board) throws InvalidOnyxPositionException {

        final MouseEvent mE = (MouseEvent) e;
        String k = null, oldK = null;
        final OnyxVirtualPiece v = board.getPosCollection().getVirtualPiece() == null ?
            vBackup : board.getPosCollection().getVirtualPiece();
        
        vBackup = v;
        
        for (OnyxPos p : board.getPosCollection().getPositions().values()) {

            if (p.rectangle.contains((float) mE.getX(), (float) mE.getY())) {
                
                k = p.getKey();
                
                if (board.getPosCollection().getPositions().containsKey(k)) {
                   
                    oldK = v.getTmpOnyxPosition().getKey();
                    
                    if (board.getPosCollection().getPosition(k).isOccupied() ||
                            (board.getPosCollection().getPosition(k).isDiamondCenter() && 
                            !board.isCenterPosPlayable(k))) {
                        return false;
                    } 
                    
                    if (v.getTmpOnyxPosition().getKey().equals(k)) {
                        validateMove(board, v);
                        board.repaint();
                        return true;
                    }
                    
                    v.setTmpOnyxPosition(board.getPosCollection().getPositions().get(k));
                    board.getPosCollection().getPosition(k).setVirtualPiece(v);
                    board.getPosCollection().getPosition(oldK).setVirtualPiece(null);
                    board.repaint();
                    
                    return false;
                }
            }
        }
        
        return false;
    }
    
    public static ClickPosition getInstance() {
        
        if (instance == null) {
            instance = new ClickPosition();
        }
        return instance;
    }
    
    private boolean validateMove(final OnyxBoard board, final OnyxVirtualPiece v) throws InvalidOnyxPositionException {

        List<OnyxPos> posSet = null;
        final String k = v.getTmpOnyxPosition().getKey();
        final OnyxPos tmpPos = board.getPosCollection().getPosition(k);
        if (board.isDiamondCenter(k) && !board.isCenterPosPlayable(k)) return false;
        if (tmpPos.isOccupied()) return false;
        
        board.getPosCollection().getPosition(k).setPiece(
            new OnyxPiece(v.color.bool ? OnyxConst.COLOR.BLACK : OnyxConst.COLOR.WHITE)
        );
        
        posSet = board.getPosCollection().getTakePositions(k, v.color.bit, board);
                
        board.getPosCollection().getPosition(k).setVirtualPiece(null);
        OnyxMove m = null;
        if (posSet != null && !OnyxGame.getInstance().isGameEnd()) {
            posSet = board.getPosCollection().performTake(k, v.color.bit, board);
            m = new OnyxMove(board.getPosCollection().getPosition(k), 
                board.getPosCollection().getPosition(k).getPiece(), posSet, 
                posSet.size() * OnyxConst.SCORE.TAKE.getValue());
        } else {
            m = new OnyxMove(board.getPosCollection().getPosition(k), 
                board.getPosCollection().getPosition(k).getPiece());
        }
        
        OnyxGame.getInstance().appendMove(m);
        board.notifyMove(m, HTMLDisplayHelper.AQUA_TURQUOISE);
        
        return true;
    }
    
}
