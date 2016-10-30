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

import com.jellyfish.jfgonyx.onyx.entities.collections.OnyxDiamondCollection;
import com.jellyfish.jfgonyx.onyx.entities.collections.OnyxPosCollection;
import com.jellyfish.jfgonyx.onyx.interfaces.OnyxObserver;
import com.jellyfish.jfgonyx.onyx.vars.OnyxCommonVars;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

/**
 * @author thw
 */
public class OnyxBoard extends javax.swing.JPanel {
    
    private final OnyxDiamondCollection diamonds;
    private final OnyxPosCollection positions;
    private final List<OnyxObserver> observers = new ArrayList<>();
    private final Rectangle[] borderRectangles;
    
    public OnyxBoard(final OnyxDiamondCollection diamonds, final OnyxPosCollection positions) {
        
        super();
        this.diamonds = diamonds;
        this.positions = positions;
        this.setSize(OnyxCommonVars.getInstance().BOARD_WIDTH, OnyxCommonVars.getInstance().BOARD_WIDTH);
        this.borderRectangles = initBorderRectangles();
        this.setOpaque(false);
        this.setDoubleBuffered(true);
        this.setCursor(new Cursor(Cursor.HAND_CURSOR));
        this.setPreferredSize(new Dimension(OnyxCommonVars.getInstance().BOARD_WIDTH, 
                OnyxCommonVars.getInstance().BOARD_WIDTH));
        this.setFocusable(true);
        this.requestFocus();
    }
    
    public void dispose() {
        observers.clear();
    }
    
    private Rectangle[] initBorderRectangles() {
        
        return new Rectangle[] {
            new Rectangle(0, 0, (OnyxCommonVars.getInstance().SQUARE_WIDTH - 10), 
                    OnyxCommonVars.getInstance().BOARD_WIDTH),
            new Rectangle(0, 
                OnyxCommonVars.getInstance().BOARD_WIDTH - (OnyxCommonVars.getInstance().SQUARE_WIDTH - 10), 
                OnyxCommonVars.getInstance().BOARD_WIDTH, (OnyxCommonVars.getInstance().SQUARE_WIDTH - 10)),
            new Rectangle(OnyxCommonVars.getInstance().BOARD_WIDTH - 
                (OnyxCommonVars.getInstance().SQUARE_WIDTH - 10), 0,
                (OnyxCommonVars.getInstance().SQUARE_WIDTH - 10), OnyxCommonVars.getInstance().BOARD_WIDTH),
            new Rectangle(0, 0, OnyxCommonVars.getInstance().BOARD_WIDTH, 
                (OnyxCommonVars.getInstance().SQUARE_WIDTH - 10))
        };
    }
    
    public boolean collidesWithBorders(final Point point) {
        for (Rectangle r : borderRectangles) {
            if (r.contains(point)) return true;
        }
        return false;
    }
    
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        OnyxBoardGHelper.drawBoard((Graphics2D) g, diamonds, positions, this);
    }

}
