/*******************************************************************************
 * Copyright (c) 2015, 2016, 2017, Thomas.H Warner.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, 
 * are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this 
 * list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, 
 * this list of conditions and the following disclaimer in the documentation and/or 
 * other materials provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its contributors 
 * may be used to endorse or promote products derived from this software without 
 * specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND 
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED 
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY 
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES 
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON 
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS 
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
 *******************************************************************************/

package com.jellyfish.jfgonyx.ui;

import com.jellyfish.jfgonyx.constants.GraphicsConst;
import com.jellyfish.jfgonyx.entities.OnyxDiamond;
import com.jellyfish.jfgonyx.entities.OnyxDiamondCollection;
import com.jellyfish.jfgonyx.entities.OnyxPiece;
import com.jellyfish.jfgonyx.entities.OnyxPosCollection;
import com.jellyfish.jfgonyx.helpers.GraphicsHelper;
import com.jellyfish.jfgonyx.io.KeyInput;
import com.jellyfish.jfgonyx.onyx.exceptions.InvalidOnyxPositionException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyListener;

/**
 *
 * @author thw
 */
public class OnyxBoard extends javax.swing.JPanel {
    
    private final OnyxDiamondCollection diamonds;
    private final OnyxPosCollection positions;
    private final KeyListener keyInput;

    public OnyxBoard(final OnyxDiamondCollection diamonds, final OnyxPosCollection positions) {
        
        super(true);
        this.diamonds = diamonds;
        this.positions = positions;
        this.setSize(GraphicsConst.BOARD_WIDTH, GraphicsConst.BOARD_WIDTH);
        this.setOpaque(true);
        this.setPreferredSize(new Dimension(GraphicsConst.BOARD_WIDTH, GraphicsConst.BOARD_WIDTH));
        this.setBackground(Color.WHITE);
        this.keyInput = new KeyInput();
        this.addKeyListener(keyInput);
        this.setFocusable(true);
        this.requestFocusInWindow();
    }
    
    public void initStartLayout() {
        
        this.positions.getPosition("6,0-1,0").addPiece(new OnyxPiece(GraphicsConst.COLOR.WHITE));
        this.positions.getPosition("7,0-1,0").addPiece(new OnyxPiece(GraphicsConst.COLOR.WHITE));
        this.positions.getPosition("6,0-12,0").addPiece(new OnyxPiece(GraphicsConst.COLOR.WHITE));
        this.positions.getPosition("7,0-12,0").addPiece(new OnyxPiece(GraphicsConst.COLOR.WHITE));
        this.positions.getPosition("1,0-6,0").addPiece(new OnyxPiece(GraphicsConst.COLOR.BLACK));
        this.positions.getPosition("1,0-7,0").addPiece(new OnyxPiece(GraphicsConst.COLOR.BLACK));
        this.positions.getPosition("12,0-6,0").addPiece(new OnyxPiece(GraphicsConst.COLOR.BLACK));
        this.positions.getPosition("12,0-7,0").addPiece(new OnyxPiece(GraphicsConst.COLOR.BLACK));
        
    }
    
    @Override
    public void paintComponent(Graphics g) {
        
        super.paintComponent(g);
        g.clearRect(0, 0, this.getWidth(), this.getHeight());
        GraphicsHelper.drawBoard((Graphics2D) g, this.diamonds, this.positions, this);
    }
    
    public boolean isCenterPosPlayable(final String k, final int bitColor) {
        
        OnyxDiamond tmpDiamond = null;
        if (k == null || !this.positions.positions.containsKey(k)) return false;
        
        for (OnyxDiamond d : this.diamonds.diamonds.values()) {
            try {
                if (d.getCenterPos().getKey().equals(k)) {
                    tmpDiamond = d;
                    break;
                }
            } catch (final InvalidOnyxPositionException Iopex) { }
        }
        
        int c = 0;
        if (tmpDiamond == null || !tmpDiamond.isFivePosDiamond()) return false;
        for (String dK : tmpDiamond.getAllKeys()) {
            c = this.positions.getPosition(dK).isOccupied() && 
                this.positions.getPosition(dK).getPiece().color.bitColor == bitColor ?
                c + 1 : c;
        }
        
        return c == 4;
    }

    public boolean isDiamondCenter(final String k) {
        
        for (OnyxDiamond d : this.diamonds.diamonds.values()) {
            try {
                if (d.getCenterPos().getKey().equals(k)) {
                    return true;
                }
            } catch (final InvalidOnyxPositionException Iopex) { }
        }
        return false;
    }
    
    public KeyInput getOnyxKeyListener() {
        return (KeyInput) this.keyInput;
    }
    
    public OnyxPosCollection getPosCollection() {
        return this.positions;
    }
    
    public OnyxDiamondCollection getDiamondCollection() {
        return diamonds;
    }
    
}
