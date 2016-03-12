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

package com.jellyfish.jfgonyx.starter;

import com.jellyfish.jfgonyx.constants.GraphicsConst;
import com.jellyfish.jfgonyx.onyx.entities.collections.OnyxDiamondCollection;
import com.jellyfish.jfgonyx.onyx.entities.collections.OnyxPosCollection;
import com.jellyfish.jfgonyx.helpers.OnyxBoardGHelper;
import com.jellyfish.jfgonyx.onyx.OnyxGame;
import com.jellyfish.jfgonyx.onyx.entities.OnyxPos;
import com.jellyfish.jfgonyx.onyx.exceptions.InvalidOnyxPositionException;
import com.jellyfish.jfgonyx.onyx.exceptions.NoValidOnyxPositionsFoundException;
import com.jellyfish.jfgonyx.onyx.exceptions.OnyxGameSyncException;
import com.jellyfish.jfgonyx.onyx.interfaces.OnyxBoardI;
import com.jellyfish.jfgonyx.onyx.search.searchutils.Intmap;
import com.jellyfish.jfgonyx.ui.MainFrame;
import com.jellyfish.jfgonyx.ui.OnyxPanel;
import com.jellyfish.jfgonyx.ui.OnyxBoard;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author thw
 */
public class Starter {
    
    private static MainFrame mainFrame = null;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        // <editor-fold defaultstate="collapsed" desc="UI Manager">    
        try {
            // Set System L&F
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            System.err.println("Look & feel setup failed.");
        }
        //</editor-fold>
        
        start(GraphicsConst.COLOR.BLACK);
    }
    
    private static void start(final GraphicsConst.COLOR color) {
        
        if (color.boolColor) {
            startBlack();
        } else {
            startWhite();
        }
    }
    
    private static void startWhite() {
        
        final OnyxPanel panel = new OnyxPanel();
        final OnyxDiamondCollection diamonds = new OnyxDiamondCollection().build();
        OnyxBoardGHelper.buildPolygons(diamonds);
        final OnyxPosCollection positions = new OnyxPosCollection();
        positions.init(diamonds);
        final OnyxBoard board = new OnyxBoard(diamonds, positions);
        board.initStartLayout();
        board.initInput();
        board.setObserver(panel);
        panel.init();
        mainFrame = new MainFrame(panel, board);
        board.setObserver(mainFrame);
        OnyxGame.getInstance().init((OnyxBoardI) board, GraphicsConst.COLOR.BLACK);
        OnyxGame.getInstance().initMove(GraphicsConst.COLOR.BLACK);
        try {
            OnyxGame.getInstance().performMove(positions, board);
        } catch (OnyxGameSyncException | NoValidOnyxPositionsFoundException | InvalidOnyxPositionException ex) {
            Logger.getLogger(Starter.class.getName()).log(Level.SEVERE, null, ex);
        }
        new Intmap(positions).print(0, OnyxGame.getInstance().dtStamp);
        board.notifyMoves(OnyxGame.getInstance().moves);
        OnyxGame.getInstance().initialized = true;
    }
    
    private static void startBlack() {
        
        final OnyxPanel panel = new OnyxPanel();
        final OnyxDiamondCollection diamonds = new OnyxDiamondCollection().build();
        OnyxBoardGHelper.buildPolygons(diamonds);
        final OnyxPosCollection positions = new OnyxPosCollection();
        positions.init(diamonds);
        positions.spawnVirtualPiece(GraphicsConst.COLOR.VIRTUAL_BLACK);
        final OnyxBoard board = new OnyxBoard(diamonds, positions);
        board.initStartLayout();
        board.initInput();
        board.setObserver(panel);
        panel.init();
        mainFrame = new MainFrame(panel, board);
        board.setObserver(mainFrame);
        OnyxGame.getInstance().init((OnyxBoardI) board, GraphicsConst.COLOR.WHITE);
        new Intmap(positions).print(0, OnyxGame.getInstance().dtStamp);
        board.notifyMoves(OnyxGame.getInstance().moves);
        OnyxGame.getInstance().initialized = true;
    }
    
    /**
     * UI playing white - blacks move first then spawn a new virtual.
     */
    public static void restartWhite() { 
        
        for (OnyxPos p : OnyxGame.getInstance().boardInterface.getPosCollection().getPositions().values()) {
            p.setPiece(null);
            p.setVirtualPiece(null);
        }
        
        OnyxGame.newInstance().moves.clear();
        OnyxGame.getInstance().init(OnyxGame.getInstance().boardInterface, GraphicsConst.COLOR.BLACK);
        OnyxGame.getInstance().initMove(GraphicsConst.COLOR.BLACK);
        OnyxGame.getInstance().boardInterface.initStartLayout();
        new Intmap(OnyxGame.getInstance().boardInterface.getPosCollection()
            ).print(0, OnyxGame.getInstance().dtStamp);
        OnyxGame.getInstance().boardInterface.notifyMoves(OnyxGame.getInstance().moves);
        OnyxGame.getInstance().initialized = true;
        
        try {
            OnyxGame.getInstance().performMove(OnyxGame.getInstance().boardInterface.getPosCollection(), 
                    (OnyxBoard) OnyxGame.getInstance().boardInterface);
        } catch (OnyxGameSyncException | NoValidOnyxPositionsFoundException | InvalidOnyxPositionException ex) {
            Logger.getLogger(Starter.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        OnyxGame.getInstance().boardInterface.getPosCollection().spawnVirtualPiece(
            GraphicsConst.COLOR.VIRTUAL_WHITE);
        OnyxGame.getInstance().setGameEnd(false);
    }
    
    public static void restartBlack() { 
        
        for (OnyxPos p : OnyxGame.getInstance().boardInterface.getPosCollection().getPositions().values()) {
            p.setPiece(null);
            p.setVirtualPiece(null);
        }
        
        OnyxGame.newInstance().moves.clear();
        OnyxGame.getInstance().init(OnyxGame.getInstance().boardInterface, GraphicsConst.COLOR.WHITE);
        OnyxGame.getInstance().boardInterface.initStartLayout();
        new Intmap(OnyxGame.getInstance().boardInterface.getPosCollection()
            ).print(0, OnyxGame.getInstance().dtStamp);
        OnyxGame.getInstance().boardInterface.notifyMoves(OnyxGame.getInstance().moves);
        OnyxGame.getInstance().initialized = true;
        
        OnyxGame.getInstance().boardInterface.getPosCollection().spawnVirtualPiece(
            GraphicsConst.COLOR.VIRTUAL_BLACK);
        OnyxGame.getInstance().setGameEnd(false);
    }
    
}
