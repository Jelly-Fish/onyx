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

import com.jellyfish.jfgonyx.vars.GraphicsVars;
import com.jellyfish.jfgonyx.onyx.constants.OnyxConst;
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
import java.awt.Point;
import java.awt.Rectangle;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author thw
 */
public class OnyxBoard extends javax.swing.JPanel implements OnyxBoardI {
    
    private final OnyxDiamondCollection diamonds;
    private final OnyxPosCollection positions;
    private KeyInput keyInput;
    private MouseInput mouseInput;
    private final List<OnyxObserver> observers = new ArrayList<>();
    private final Rectangle[] borderRectangles;
    
    public OnyxBoard(final OnyxDiamondCollection diamonds, final OnyxPosCollection positions) {
        
        super();
        this.diamonds = diamonds;
        this.positions = positions;
        this.setSize(GraphicsVars.getInstance().BOARD_WIDTH, GraphicsVars.getInstance().BOARD_WIDTH);
        this.borderRectangles = initBorderRectangles();
        this.setOpaque(false);
        this.setDoubleBuffered(true);
        this.setCursor(new Cursor(Cursor.HAND_CURSOR));
        this.setPreferredSize(new Dimension(GraphicsVars.getInstance().BOARD_WIDTH, 
                GraphicsVars.getInstance().BOARD_WIDTH));
        this.keyInput = new KeyInput();
        this.mouseInput = new MouseInput();
        this.addKeyListener(keyInput);
        this.addMouseListener(mouseInput);
        this.addMouseMotionListener(mouseInput);
        this.setFocusable(true);
        this.focus();
    }
    
    public void dispose() {
        keyInput = null;
        mouseInput = null;
        observers.clear();
    }
    
    private Rectangle[] initBorderRectangles() {
        
        return new Rectangle[] {
            new Rectangle(0, 0, (GraphicsVars.getInstance().SQUARE_WIDTH - 10), 
                    GraphicsVars.getInstance().BOARD_WIDTH),
            new Rectangle(0, 
                GraphicsVars.getInstance().BOARD_WIDTH - (GraphicsVars.getInstance().SQUARE_WIDTH - 10), 
                GraphicsVars.getInstance().BOARD_WIDTH, (GraphicsVars.getInstance().SQUARE_WIDTH - 10)),
            new Rectangle(GraphicsVars.getInstance().BOARD_WIDTH - 
                (GraphicsVars.getInstance().SQUARE_WIDTH - 10), 0,
                (GraphicsVars.getInstance().SQUARE_WIDTH - 10), GraphicsVars.getInstance().BOARD_WIDTH),
            new Rectangle(0, 0, GraphicsVars.getInstance().BOARD_WIDTH, 
                (GraphicsVars.getInstance().SQUARE_WIDTH - 10))
        };
    }
    
    @Override
    public boolean collidesWithBorders(final Point point) {
        for (Rectangle r : borderRectangles) {
            if (r.contains(point)) return true;
        }
        return false;
    }
    
    @Override
    public void initInput() {
        keyInput.init(this);
        mouseInput.init(this);
    }
    
    @Override
    public void restart() {
        positions.clearPieces();
    }
    
    @Override
    public void initStartLayout() {
        
        final String separator = "-";
        final String one = "1,0";
        final DecimalFormat df = new DecimalFormat("#.0");
        
        for (OnyxPos p : positions.getPositions().values()) p.setPiece(null);

        final String min = df.format(GraphicsVars.getInstance().BOARD_SIDE_POS_COUNT / 2f);
        final String max = df.format(GraphicsVars.getInstance().BOARD_SIDE_POS_COUNT);
        final String minPlus = df.format((GraphicsVars.getInstance().BOARD_SIDE_POS_COUNT / 2f) + 1f);
        
        positions.getPosition(one + separator + min).addPiece(new OnyxPiece(OnyxConst.COLOR.BLACK));      
        OnyxGame.getInstance().appendMove(new OnyxMove(positions.getPosition(one + separator + min), 
            positions.getPosition(one + separator + min).getPiece()));
        
        positions.getPosition(min + separator + one).addPiece(new OnyxPiece(OnyxConst.COLOR.WHITE));
        OnyxGame.getInstance().appendMove(new OnyxMove(positions.getPosition(min + separator + one), 
                positions.getPosition(min + separator + one).getPiece()));
        
        positions.getPosition(max + separator + minPlus).addPiece(new OnyxPiece(OnyxConst.COLOR.BLACK));
        OnyxGame.getInstance().appendMove(new OnyxMove(positions.getPosition(max + separator + minPlus), 
                positions.getPosition(max + separator + minPlus).getPiece()));
        
        positions.getPosition(minPlus + separator + max).addPiece(new OnyxPiece(OnyxConst.COLOR.WHITE));
        OnyxGame.getInstance().appendMove(new OnyxMove(positions.getPosition(minPlus + separator + max), 
                positions.getPosition(minPlus + separator + max).getPiece()));
        
        positions.getPosition(one + separator + minPlus).addPiece(new OnyxPiece(OnyxConst.COLOR.BLACK));
        OnyxGame.getInstance().appendMove(new OnyxMove(positions.getPosition(one + separator + minPlus), 
                positions.getPosition(one + separator + minPlus).getPiece()));
        
        positions.getPosition(minPlus + separator + one).addPiece(new OnyxPiece(OnyxConst.COLOR.WHITE));
        OnyxGame.getInstance().appendMove(new OnyxMove(positions.getPosition(minPlus + separator + one), 
                positions.getPosition(minPlus + separator + one).getPiece()));
        
        positions.getPosition(max + separator + min).addPiece(new OnyxPiece(OnyxConst.COLOR.BLACK));
        OnyxGame.getInstance().appendMove(new OnyxMove(positions.getPosition(max + separator + min), 
                positions.getPosition(max + separator + min).getPiece()));
        
        positions.getPosition(min + separator + max).addPiece(new OnyxPiece(OnyxConst.COLOR.WHITE));
        OnyxGame.getInstance().appendMove(new OnyxMove(positions.getPosition(min + separator + max), 
                positions.getPosition(min + separator + max).getPiece()));
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        OnyxBoardGHelper.drawBoard((Graphics2D) g, diamonds, positions, this);
    }
    
    @Override
    public boolean isCenterPosPlayable(final String k) {
        
        int counter = 0;
        if (k == null || !positions.containsPosition(k) || 
                !isDiamondCenter(k)) {
            return false;
        }
        
        for (String cK : positions.getPosition(k).connections) {
            counter = positions.getPosition(cK).isOccupied() ? ++counter : counter;
        }
        
        return counter == 0;
    }

    @Override
    public boolean isDiamondCenter(final String k) {
        
        for (OnyxDiamond d : diamonds.getDiamonds().values()) {
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
        return positions;
    }
    
    @Override
    public OnyxDiamondCollection getDiamondCollection() {
        return diamonds;
    }
    
    @Override
    public void notifyMove(final OnyxMove m, final String color) {
        for (OnyxObserver obs : observers) {
            obs.notifyMove(m, color);
        }
    }

    @Override
    public void setObserver(final OnyxObserver observer) {
        observers.add(observer);
    }
    
    @Override
    public final void focus() {
        requestFocus();
    }
       
    @Override
    public List<OnyxObserver> getObservers() {
        return observers;
    }
    
    @Override
    public void notifyMoves(final HashMap<Integer, OnyxMove> moves, final String color) {
        for (OnyxObserver obs : observers) {
            for (OnyxMove m : moves.values()) obs.notifyMove(m, color);
        }
    }

}
