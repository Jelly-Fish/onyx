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

import com.jellyfish.jfgonyx.constants.GraphicsConst;
import com.jellyfish.jfgonyx.entities.OnyxPos;
import com.jellyfish.jfgonyx.helpers.MainPanelGHelper;
import com.jellyfish.jfgonyx.onyx.interfaces.OnyxObserver;
import java.awt.Font;
import java.awt.Graphics;
import java.util.LinkedList;
import javax.swing.JPanel;

/**
 * @author thw
 */
public class MainPanel extends JPanel implements OnyxObserver {

    private final OnyxBoard board;
    private final Font FONT = new Font("consolas", Font.BOLD, 14);
    private final LinkedList<String> moveLabels = new LinkedList<>();
    private final String labelFormat = "%d - %s";
    
    public MainPanel(final OnyxBoard board) {
        
        super();
        this.setDoubleBuffered(true);
        this.board = board;
        this.setBackground(GraphicsConst.MAIN_PANEL_BACKGROUND_COLOR);
    }
    
    @Override
    public void paintComponent(Graphics g) {
        
        super.paintComponent(g);
        MainPanelGHelper.drawMove(g, this.FONT, this.labelFormat, this.moveLabels);
    }
    
    @Override
    public void notifyMove(final String m) {
        this.moveLabels.add(m);
        this.repaint();
    }
    
    public void init() {
        this.board.setObserver(this);
        for (OnyxPos p : this.board.getPosCollection().positions.values()) {
            if (p.isOccupied()) {
                this.moveLabels.add(p.toString());
            }
        }
    }
    
}