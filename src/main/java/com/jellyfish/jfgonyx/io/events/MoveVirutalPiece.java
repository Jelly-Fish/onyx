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

import com.jellyfish.jfgonyx.constants.GraphicsConst;
import com.jellyfish.jfgonyx.entities.OnyxPiece;
import com.jellyfish.jfgonyx.entities.OnyxPos;
import com.jellyfish.jfgonyx.entities.OnyxPosCollection;
import com.jellyfish.jfgonyx.entities.OnyxVirtualPiece;
import com.jellyfish.jfgonyx.onyx.exceptions.InvalidOnyxPositionException;
import com.jellyfish.jfgonyx.onyx.interfaces.OnyxExecutable;
import com.jellyfish.jfgonyx.ui.OnyxBoard;
import java.awt.event.KeyEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author thw
 */
public class MoveVirutalPiece implements OnyxExecutable {

    @Override
    public boolean exec(final int e, final OnyxBoard board) {

        final OnyxVirtualPiece v = board.getPosCollection().getVirtualPiece();
        if (v == null) return false;
        final float x = v.getTmpOnyxPosition().x;
        final float y = v.getTmpOnyxPosition().y;
        boolean moved = false;
        
        /**
         * FIXME 2-1-2016 :
         * Due to 180° board rotation, X coordinates are inverted; 
         * therefor, left move = ++x and right --x - the x=0 y=0 position
         * being in the right bottom corner (and not left bottom).
         */

        switch (e) {
            case KeyEvent.VK_LEFT:
                this.move(x, y, x + 1f, y, board, v);
                break;
            case KeyEvent.VK_UP:
                this.move(x, y, x, y + 1f, board, v);
                break;
            case KeyEvent.VK_RIGHT:
                this.move(x, y, x - 1f, y, board, v);
                break;
            case KeyEvent.VK_DOWN:
                this.move(x, y, x, y - 1f, board, v);
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

    private void move(final float x, final float y, final float nX, final float nY,
            final OnyxBoard board, final OnyxVirtualPiece v) {
        
        if (v.getTmpOnyxPosition().diamond.isCenterPosUsable(v.getTmpOnyxPosition())) {
            this.moveToDiamondCenter(v);
            return;
        }
        
        final String k = String.format(OnyxPosCollection.KEY_FORMAT, nX, nY);
        final String oldK = String.format(OnyxPosCollection.KEY_FORMAT, x, y);

        if (board.getPosCollection().positions.containsKey(k)) {
            v.setTmpOnyxPosition(board.getPosCollection().positions.get(k));
            board.getPosCollection().positions.get(k).setVirtualPiece(v);
            board.getPosCollection().positions.get(oldK).setVirtualPiece(null);
        }
    }
    
    private boolean validateMove(final OnyxBoard board, final OnyxVirtualPiece v) {
               
        final String k = v.getTmpOnyxPosition().getKey();
        if (board.getPosCollection().getPosition(k).isOccupied()) return false;
        board.getPosCollection().getPosition(k).setPiece(
                new OnyxPiece(v.color.boolColor ? GraphicsConst.COLOR.BLACK : GraphicsConst.COLOR.WHITE)
        );
        board.getPosCollection().getPosition(k).setVirtualPiece(null);
        
        return true;
    }
    
    private void moveToDiamondCenter(final OnyxVirtualPiece v) {
        
        try {
            final OnyxPos center = v.getTmpOnyxPosition().diamond.getCenterPos();
            if (center == null || !center.diamond.isCenterPosUsable(center) || center.isOccupied()) {
                return;
            }
            v.getTmpOnyxPosition().diamond.getCenterPos().setVirtualPiece(v);
        } catch (final InvalidOnyxPositionException Iopex) {
            Logger.getLogger(MoveVirutalPiece.class.getName()).log(Level.SEVERE, null, Iopex);
        }
    }

}
