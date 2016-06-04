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
package com.jellyfish.jfgonyx.io;

import com.jellyfish.jfgonyx.onyx.constants.OnyxConst;
import com.jellyfish.jfgonyx.io.events.BoardDragger;
import com.jellyfish.jfgonyx.io.events.ClickPosition;
import com.jellyfish.jfgonyx.onyx.OnyxGame;
import com.jellyfish.jfgonyx.onyx.exceptions.InvalidOnyxPositionException;
import com.jellyfish.jfgonyx.onyx.exceptions.NoValidOnyxPositionsFoundException;
import com.jellyfish.jfgonyx.onyx.exceptions.OnyxGameSyncException;
import com.jellyfish.jfgonyx.onyx.interfaces.OnyxExecutable;
import com.jellyfish.jfgonyx.ui.OnyxBoard;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author thw
 */
public class MouseInput implements MouseListener, MouseMotionListener {

    public boolean mouseDownOnBorder = false;
    public boolean mouseDown = false;
    private OnyxBoard board = null;
    private final HashMap<MouseInput.EVENT, OnyxExecutable> ops = new HashMap<>();
    private final Cursor GRAB_CURSOR = new Cursor(Cursor.MOVE_CURSOR);
    private final Cursor HAND_CURSOR = new Cursor(Cursor.HAND_CURSOR);
    
    public static enum EVENT {
        DRAG_BOARD, SELECT_POS
    }
    
    public void init(final OnyxBoard board) {
        
        this.board = board;
        this.ops.put(MouseInput.EVENT.DRAG_BOARD, BoardDragger.getInstance());
        this.ops.put(MouseInput.EVENT.SELECT_POS, ClickPosition.getInstance());
    }
    
    @Override
    public void mouseMoved(MouseEvent e) { }
    
    @Override
    public void mousePressed(MouseEvent e) {
        
        mouseDown = true;
        mouseDownOnBorder = board.collidesWithBorders(new Point(e.getX(), e.getY()));
        if (mouseDownOnBorder) board.setCursor(GRAB_CURSOR);
        BoardDragger.getInstance().update(e, board);
    }

    @Override
    public void mouseReleased(MouseEvent e) { 
        mouseDown = false;
        mouseDownOnBorder = false;
        board.setCursor(HAND_CURSOR);
    }
    
    @Override
    public void mouseDragged(MouseEvent e) { 
        
        if (mouseDownOnBorder) {
            try {
                ops.get(MouseInput.EVENT.DRAG_BOARD).exec(e, board);
            } catch (final InvalidOnyxPositionException Iopex) {
                Logger.getLogger(MouseInput.class.getName()).log(Level.SEVERE, null, Iopex);
            }
        }
    }
    
    @Override
    public void mouseClicked(MouseEvent e) { 
        
        if (OnyxGame.getInstance().isGameEnd()) return;
        
        final OnyxConst.COLOR c = board.getPosCollection().getVirtualPiece().color;
        OnyxGame.getInstance().initMove(OnyxConst.COLOR.getOposite(c.bool));
        
        try {
            if (ops.get(MouseInput.EVENT.SELECT_POS).exec(e, board)) {
                OnyxGame.getInstance().performMove(board.getPosCollection(), board);
            }
        } catch (final OnyxGameSyncException | NoValidOnyxPositionsFoundException | 
                InvalidOnyxPositionException ex) {
            Logger.getLogger(KeyInput.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        OnyxGame.getInstance().closeMove();
    }

    @Override
    public void mouseEntered(MouseEvent e) { }

    @Override
    public void mouseExited(MouseEvent e) { }
    
}
