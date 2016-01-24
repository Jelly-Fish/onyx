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
package com.jellyfish.jfgonyx.ui;

import com.jellyfish.jfgonyx.onyx.OnyxGame;
import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author thw
 */
public class OnyxMoveHistory extends javax.swing.JTextArea {

    private final OnyxPanel panel;
    private int screenX, screenY, xPos, yPos;
    private final Cursor GRAB_CURSOR = new Cursor(Cursor.MOVE_CURSOR);
    private final Cursor CURSOR = new Cursor(Cursor.DEFAULT_CURSOR);
    
    public OnyxMoveHistory(final OnyxPanel parent) {
        super();
        this.panel = parent;
        this.setFocusable(true);
        this.setEditable(false);
        this.setDoubleBuffered(true);
        this.initListeners();
    }
        
    public void appendMove(final String m) {
        this.append(m);
    }

    void clear() {
        this.setText(StringUtils.EMPTY);
    }

    private void initListeners() {
        
        this.addMouseListener(new MouseListener() {
            
            @Override
            public void mouseClicked(MouseEvent e) { }
                
            @Override
            public void mousePressed(MouseEvent e) {
                screenX = e.getXOnScreen();
                screenY = e.getYOnScreen();
                xPos = getX();
                yPos = getY();
                setCursor(GRAB_CURSOR);
            }
            
            @Override
            public void mouseExited(MouseEvent e) { 
                OnyxGame.boardInterface.focus();
                setCursor(CURSOR);
            }

            @Override
            public void mouseReleased(MouseEvent e) { setCursor(CURSOR); }

            @Override
            public void mouseEntered(MouseEvent e) { }
        });
        
        addMouseMotionListener(new MouseMotionListener() {

            @Override
            public void mouseDragged(MouseEvent e) {
                setCursor(GRAB_CURSOR);
                final int deltaX = e.getXOnScreen() - screenX;
                final int deltaY = e.getYOnScreen() - screenY;
                setLocation(xPos + deltaX, yPos + deltaY);
            }

            @Override
            public void mouseMoved(MouseEvent e) { setCursor(GRAB_CURSOR); }
        });
    }
    
}
