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
 * *****************************************************************************
 */
package com.jellyfish.jfgonyx.io.events;

import com.jellyfish.jfgonyx.vars.GraphicsVars;
import com.jellyfish.jfgonyx.constants.OnyxConst;
import com.jellyfish.jfgonyx.helpers.HTMLDisplayHelper;
import com.jellyfish.jfgonyx.onyx.OnyxGame;
import com.jellyfish.jfgonyx.onyx.entities.OnyxMove;
import com.jellyfish.jfgonyx.onyx.entities.OnyxPiece;
import com.jellyfish.jfgonyx.onyx.entities.OnyxPos;
import com.jellyfish.jfgonyx.onyx.entities.collections.OnyxPosCollection;
import com.jellyfish.jfgonyx.onyx.entities.OnyxVirtualPiece;
import com.jellyfish.jfgonyx.onyx.exceptions.InvalidOnyxPositionException;
import com.jellyfish.jfgonyx.onyx.interfaces.OnyxExecutable;
import com.jellyfish.jfgonyx.ui.OnyxBoard;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author thw
 */
public class KeyMoveVirutalPiece implements OnyxExecutable {

    @Override
    public boolean exec(final InputEvent e, final OnyxBoard board) throws InvalidOnyxPositionException {

        final OnyxVirtualPiece v = board.getPosCollection().getVirtualPiece();
        if (v == null) return false;
        final float x = v.getTmpOnyxPosition().x;
        final float y = v.getTmpOnyxPosition().y;
        boolean moved = false;

        switch (((KeyEvent) e).getKeyCode()) {
            case KeyEvent.VK_LEFT:
                this.move(x, y, x - .5f, y - .5f, board, v, KeyEvent.VK_LEFT);
                break;
            case KeyEvent.VK_UP:
                this.move(x, y, x + .5f, y - .5f, board, v, KeyEvent.VK_UP);
                break;
            case KeyEvent.VK_RIGHT:
                this.move(x, y, x + .5f, y + .5f, board, v, KeyEvent.VK_RIGHT);
                break;
            case KeyEvent.VK_DOWN:
                this.move(x, y, x - .5f, y + .5f, board, v, KeyEvent.VK_DOWN);
                break;
            case KeyEvent.VK_ENTER:
                moved = this.validateMove(board, v);
                break;
            default:
                break;
        }

        board.repaint();
        return moved;
    }

    /**
     * Move piece from positions - if position is not found (diagonal on 4 point
     * diamond) then persue serach by incrementing by .5f the move value.
     * @param x X exiting position coordinate.
     * @param y Y exiting position coordinate.
     * @param nX X new position coordinate.
     * @param nY Y new position coordinate.
     * @param board OnyxBoard onyx board instance.
     * @param v OnyxVirtualPiece virtual piece instance.
     * @param keyEvt KeyEvent constant integer value.
     */
    private void move(final float x, final float y, final float nX, final float nY,
            final OnyxBoard board, final OnyxVirtualPiece v, final int keyEvt) throws InvalidOnyxPositionException {
        
        String k = String.format(OnyxPosCollection.KEY_FORMAT, nX, nY);
        final String oldK = String.format(OnyxPosCollection.KEY_FORMAT, x, y);
        if (!this.applyMove(k, oldK, v, board)) {
            this.applyMove(this.forwardMove(nX, nY, keyEvt), oldK, v, board);
        }
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

    private boolean applyMove(final String k, final String oldK, final OnyxVirtualPiece v, 
            final OnyxBoard board) throws InvalidOnyxPositionException {
        
        if (board.getPosCollection().getPositions().containsKey(k)) {
            v.setTmpOnyxPosition(board.getPosCollection().getPositions().get(k));
            board.getPosCollection().getPosition(k).setVirtualPiece(v);
            board.getPosCollection().getPosition(oldK).setVirtualPiece(null);
            
            return true;
        }
        return false;
    }

    private String forwardMove(final float nX, final float nY, final int keyEvt) {
        
        switch (keyEvt) {
            case KeyEvent.VK_LEFT:
                return String.format(OnyxPosCollection.KEY_FORMAT, nX - .5f, nY - .5f);
            case KeyEvent.VK_UP:
                return String.format(OnyxPosCollection.KEY_FORMAT, nX + .5f, nY - .5f);
            case KeyEvent.VK_RIGHT:
                return String.format(OnyxPosCollection.KEY_FORMAT, nX + .5f, nY + .5f);
            case KeyEvent.VK_DOWN:
                return String.format(OnyxPosCollection.KEY_FORMAT, nX - .5f, nY + .5f);
            default:
                return StringUtils.EMPTY;
        }
    }

}
