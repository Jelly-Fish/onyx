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
import com.jellyfish.jfgonyx.onyx.entities.OnyxDiamond;
import com.jellyfish.jfgonyx.onyx.entities.collections.OnyxDiamondCollection;
import com.jellyfish.jfgonyx.onyx.entities.OnyxPiece;
import com.jellyfish.jfgonyx.onyx.entities.OnyxPos;
import com.jellyfish.jfgonyx.onyx.entities.collections.OnyxPosCollection;
import com.jellyfish.jfgonyx.helpers.OnyxBoardGHelper;
import com.jellyfish.jfgonyx.io.KeyInput;
import com.jellyfish.jfgonyx.io.MouseInput;
import com.jellyfish.jfgonyx.onyx.OnyxGame;
import com.jellyfish.jfgonyx.onyx.entities.OnyxMove;
import com.jellyfish.jfgonyx.onyx.exceptions.InvalidOnyxPositionException;
import com.jellyfish.jfgonyx.onyx.interfaces.OnyxBoardI;
import com.jellyfish.jfgonyx.onyx.interfaces.OnyxObserver;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author thw
 */
public class OnyxBoard extends javax.swing.JPanel implements OnyxBoardI {
    
    private final OnyxDiamondCollection diamonds;
    private final OnyxPosCollection positions;
    private final KeyInput keyInput;
    private final MouseInput mouseInput;
    private final List<OnyxObserver> observers = new ArrayList<>();
    
    public OnyxBoard(final OnyxDiamondCollection diamonds, final OnyxPosCollection positions) {
        
        super();
        this.diamonds = diamonds;
        this.positions = positions;
        this.setSize(GraphicsConst.BOARD_WIDTH, GraphicsConst.BOARD_WIDTH);
        this.setOpaque(false);
        this.setDoubleBuffered(true);
        this.setCursor(new Cursor(Cursor.HAND_CURSOR));
        this.setPreferredSize(new Dimension(GraphicsConst.BOARD_WIDTH, GraphicsConst.BOARD_WIDTH));
        this.keyInput = new KeyInput();
        this.mouseInput = new MouseInput();
        this.addKeyListener(keyInput);
        this.addMouseListener(mouseInput);
        this.addMouseMotionListener(mouseInput);
        this.setFocusable(true);
        this.focus();
    }
    
    @Override
    public void initInput() {
        this.keyInput.init(this);
        this.mouseInput.init(this);
    }
    
    @Override
    public void initStartLayout() {
        
        for (OnyxPos p : this.positions.getPositions().values()) p.setPiece(null);
        
        this.positions.getPosition("1,0-6,0").addPiece(new OnyxPiece(GraphicsConst.COLOR.BLACK));
        OnyxGame.getInstance().appendMove(new OnyxMove(this.positions.getPosition("1,0-6,0"), 
                this.positions.getPosition("1,0-6,0").getPiece(), null, false));
        this.positions.getPosition("6,0-1,0").addPiece(new OnyxPiece(GraphicsConst.COLOR.WHITE));
        OnyxGame.getInstance().appendMove(new OnyxMove(this.positions.getPosition("6,0-1,0"), 
                this.positions.getPosition("6,0-1,0").getPiece(), null, false));
        this.positions.getPosition("12,0-7,0").addPiece(new OnyxPiece(GraphicsConst.COLOR.BLACK));
        OnyxGame.getInstance().appendMove(new OnyxMove(this.positions.getPosition("12,0-7,0"), 
                this.positions.getPosition("12,0-7,0").getPiece(), null, false));
        this.positions.getPosition("7,0-12,0").addPiece(new OnyxPiece(GraphicsConst.COLOR.WHITE));
        OnyxGame.getInstance().appendMove(new OnyxMove(this.positions.getPosition("7,0-12,0"), 
                this.positions.getPosition("7,0-12,0").getPiece(), null, false));
        this.positions.getPosition("1,0-7,0").addPiece(new OnyxPiece(GraphicsConst.COLOR.BLACK));
        OnyxGame.getInstance().appendMove(new OnyxMove(this.positions.getPosition("1,0-7,0"), 
                this.positions.getPosition("1,0-7,0").getPiece(), null, false));
        this.positions.getPosition("7,0-1,0").addPiece(new OnyxPiece(GraphicsConst.COLOR.WHITE));
        OnyxGame.getInstance().appendMove(new OnyxMove(this.positions.getPosition("7,0-1,0"), 
                this.positions.getPosition("7,0-1,0").getPiece(), null, false));
        this.positions.getPosition("12,0-6,0").addPiece(new OnyxPiece(GraphicsConst.COLOR.BLACK));
        OnyxGame.getInstance().appendMove(new OnyxMove(this.positions.getPosition("12,0-6,0"), 
                this.positions.getPosition("12,0-6,0").getPiece(), null, false));
        this.positions.getPosition("6,0-12,0").addPiece(new OnyxPiece(GraphicsConst.COLOR.WHITE));
        OnyxGame.getInstance().appendMove(new OnyxMove(this.positions.getPosition("6,0-12,0"), 
                this.positions.getPosition("6,0-12,0").getPiece(), null, false));
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        OnyxBoardGHelper.drawBoard((Graphics2D) g, this.diamonds, this.positions, this);
    }
    
    @Override
    public boolean isCenterPosPlayable(final String k) {
        
        OnyxDiamond tmpDiamond = null;
        if (k == null || !this.positions.getPositions().containsKey(k)) return false;
        
        for (OnyxDiamond d : this.diamonds.getDiamonds().values()) {
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
            c = this.positions.getPosition(dK).isOccupied() ? c + 1 : c;
        }
        
        return c == 0;
    }

    @Override
    public boolean isDiamondCenter(final String k) {
        
        for (OnyxDiamond d : this.diamonds.getDiamonds().values()) {
            try {
                if (d.getCenterPos().getKey().equals(k)) {
                    return true;
                }
            } catch (final InvalidOnyxPositionException Iopex) { }
        }
        return false;
    }
    
    @Override
    public OnyxPosCollection getPosCollection() {
        return this.positions;
    }
    
    @Override
    public OnyxDiamondCollection getDiamondCollection() {
        return diamonds;
    }
    
    @Override
    public void notifyMove(final OnyxMove m) {
        for (OnyxObserver obs : this.observers) {
            obs.notifyMove(m);
        }
    }

    @Override
    public void setObserver(final OnyxObserver observer) {
        this.observers.add(observer);
    }
    
    @Override
    public final void focus() {
        this.requestFocus();
    }
       
    @Override
    public List<OnyxObserver> getObservers() {
        return this.observers;
    }
    
    @Override
    public void notifyMoves(final HashMap<Integer, OnyxMove> moves) {
        for (OnyxObserver obs : this.observers) {
            for (OnyxMove m : moves.values()) obs.notifyMove(m);
        }
    }

}
