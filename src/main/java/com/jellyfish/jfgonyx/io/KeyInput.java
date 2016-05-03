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
import com.jellyfish.jfgonyx.io.events.KeyMoveVirutalPiece;
import com.jellyfish.jfgonyx.onyx.OnyxGame;
import com.jellyfish.jfgonyx.onyx.exceptions.InvalidOnyxPositionException;
import com.jellyfish.jfgonyx.onyx.exceptions.NoValidOnyxPositionsFoundException;
import com.jellyfish.jfgonyx.onyx.exceptions.OnyxGameSyncException;
import com.jellyfish.jfgonyx.onyx.interfaces.OnyxExecutable;
import com.jellyfish.jfgonyx.ui.OnyxBoard;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author thw
 */
public class KeyInput implements KeyListener {

    private OnyxBoard board = null;
    private final HashMap<KeyInput.EVENT, OnyxExecutable> ops = new HashMap<>();
    
    public static enum EVENT {
        VIRTUAL_P_MOVE
    }
    
    public void init(final OnyxBoard board) {
        this.board = board;
        this.ops.put(KeyInput.EVENT.VIRTUAL_P_MOVE, new KeyMoveVirutalPiece());
    }
    
    @Override
    public void keyReleased(KeyEvent e) {
        
        if (OnyxGame.getInstance().isGameEnd()) return;
        
        final OnyxConst.COLOR c = this.board.getPosCollection().getVirtualPiece().color;
        OnyxGame.getInstance().initMove(OnyxConst.COLOR.getOposite(c.bool));

        try {
            if (this.ops.get(KeyInput.EVENT.VIRTUAL_P_MOVE).exec(e, this.board)) {
                OnyxGame.getInstance().performMove(this.board.getPosCollection(), this.board);
            }
        } catch (final OnyxGameSyncException | NoValidOnyxPositionsFoundException | 
                InvalidOnyxPositionException ex) {
            Logger.getLogger(KeyInput.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        OnyxGame.getInstance().closeMove();
    }
    
    @Override
    public void keyTyped(KeyEvent e) { }

    @Override
    public void keyPressed(KeyEvent e) { }
    
}
