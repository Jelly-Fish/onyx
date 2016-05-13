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

package com.jellyfish.jfgonyx.main;

import com.jellyfish.jfgonyx.onyx.constants.OnyxConst;
import com.jellyfish.jfgonyx.helpers.HTMLDisplayHelper;
import com.jellyfish.jfgonyx.helpers.LogHelper;
import com.jellyfish.jfgonyx.onyx.entities.collections.OnyxDiamondCollection;
import com.jellyfish.jfgonyx.onyx.entities.collections.OnyxPosCollection;
import com.jellyfish.jfgonyx.helpers.OnyxBoardGHelper;
import com.jellyfish.jfgonyx.onyx.OnyxGame;
import com.jellyfish.jfgonyx.onyx.exceptions.InvalidOnyxPositionException;
import com.jellyfish.jfgonyx.onyx.exceptions.NoValidOnyxPositionsFoundException;
import com.jellyfish.jfgonyx.onyx.exceptions.OnyxGameSyncException;
import com.jellyfish.jfgonyx.onyx.interfaces.OnyxBoardI;
import com.jellyfish.jfgonyx.onyx.search.searchutils.Intmap;
import com.jellyfish.jfgonyx.ui.MainFrame;
import com.jellyfish.jfgonyx.ui.OnyxPanel;
import com.jellyfish.jfgonyx.ui.OnyxBoard;
import com.jellyfish.jfgonyx.ui.utils.DataUtils;
import com.jellyfish.jfgonyx.vars.GraphicsVars;
import com.jellyfish.jfgonyx.vars.MainFrameVars;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author thw
 */
public class Main {
    
    private static MainFrame mainFrame = null;
    private static final String NEW_GAME = ">> New Onyx Game started @ %s<br />>> You are playing %s...";
    private static final String RESTART_GAME = ">> Restarted Onyx Game @ %s<br />>> You are playing %s...";
    
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
        
        deserialize();
        start(OnyxConst.COLOR.BLACK);
    }
    
    public static void start(final OnyxConst.COLOR color) {
        
        if (color.bool) {
            startBlack();
        } else {
            startWhite();
        }
    }
    
    public static void startWhite() {
        
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
        MainFrame.print(String.format(NEW_GAME, LogHelper.getDTFullStamp(), 
                OnyxConst.COLOR.WHITE.str), HTMLDisplayHelper.GOLD);
        board.setObserver(mainFrame);
        OnyxGame.getInstance().init((OnyxBoardI) board, OnyxConst.COLOR.BLACK);
        OnyxGame.getInstance().initMove(OnyxConst.COLOR.BLACK);
        try {
            OnyxGame.getInstance().performMove(positions, board);
        } catch (OnyxGameSyncException | NoValidOnyxPositionsFoundException | InvalidOnyxPositionException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        new Intmap(positions).print(0, OnyxGame.getInstance().dtStamp);
        board.notifyMoves(OnyxGame.getInstance().moves, HTMLDisplayHelper.GAINSBORO);
        OnyxGame.getInstance().initialized = true;
    }
    
    public static void startBlack() {
        
        final OnyxPanel panel = new OnyxPanel();
        final OnyxDiamondCollection diamonds = new OnyxDiamondCollection().build();
        OnyxBoardGHelper.buildPolygons(diamonds);
        final OnyxPosCollection positions = new OnyxPosCollection();
        positions.init(diamonds);
        positions.spawnVirtualPiece(OnyxConst.COLOR.VIRTUAL_BLACK);
        final OnyxBoard board = new OnyxBoard(diamonds, positions);
        board.initStartLayout();
        board.initInput();
        board.setObserver(panel);
        panel.init();
        mainFrame = new MainFrame(panel, board);
        MainFrame.print(String.format(NEW_GAME, LogHelper.getDTFullStamp(), 
                OnyxConst.COLOR.BLACK.str), HTMLDisplayHelper.GOLD);
        board.setObserver(mainFrame);
        OnyxGame.getInstance().init((OnyxBoardI) board, OnyxConst.COLOR.WHITE);
        new Intmap(positions).print(0, OnyxGame.getInstance().dtStamp);
        board.notifyMoves(OnyxGame.getInstance().moves, HTMLDisplayHelper.GAINSBORO);
        OnyxGame.getInstance().initialized = true;
    }
    
    public static void restartWhite() { 
        
        final OnyxBoardI board = OnyxGame.getInstance().boardInterface;
        board.restart();
        OnyxGame.newInstance().moves.clear();        
        OnyxGame.getInstance().init(board, OnyxConst.COLOR.BLACK);
        OnyxGame.getInstance().boardInterface.initStartLayout();
        new Intmap(OnyxGame.getInstance().boardInterface.getPosCollection()
            ).print(0, OnyxGame.getInstance().dtStamp);
        MainFrame.print(String.format(RESTART_GAME, LogHelper.getDTFullStamp(), 
                OnyxConst.COLOR.WHITE.str), HTMLDisplayHelper.GOLD);
        OnyxGame.getInstance().boardInterface.notifyMoves(OnyxGame.getInstance().moves, 
                HTMLDisplayHelper.GAINSBORO);
        OnyxGame.getInstance().initialized = true;
                
        try {
            OnyxGame.getInstance().initMove(OnyxConst.COLOR.BLACK);
            OnyxGame.getInstance().performMove(OnyxGame.getInstance().boardInterface.getPosCollection(), 
                    (OnyxBoard) OnyxGame.getInstance().boardInterface);
        } catch (OnyxGameSyncException | NoValidOnyxPositionsFoundException | InvalidOnyxPositionException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        OnyxGame.getInstance().setGameEnd(false);
    }
    
    public static void restartBlack() { 
               
        final OnyxBoardI board = OnyxGame.getInstance().boardInterface;
        board.restart();
        OnyxGame.newInstance().moves.clear();
        OnyxGame.getInstance().init(board, OnyxConst.COLOR.WHITE);
        OnyxGame.getInstance().boardInterface.initStartLayout();
        new Intmap(OnyxGame.getInstance().boardInterface.getPosCollection()
            ).print(0, OnyxGame.getInstance().dtStamp);
        MainFrame.print(String.format(RESTART_GAME, LogHelper.getDTFullStamp(), 
                OnyxConst.COLOR.BLACK.str), HTMLDisplayHelper.GOLD);
        OnyxGame.getInstance().boardInterface.notifyMoves(OnyxGame.getInstance().moves, 
                HTMLDisplayHelper.GAINSBORO);
        OnyxGame.getInstance().initialized = true;
        
        OnyxGame.getInstance().boardInterface.getPosCollection().spawnVirtualPiece(
            OnyxConst.COLOR.VIRTUAL_BLACK);
        OnyxGame.getInstance().setGameEnd(false);
    }

    private static void deserialize() {
        GraphicsVars.setInstance(
            (GraphicsVars) DataUtils.xmlDeserialize(
            DataUtils.DATA_ROOT + GraphicsVars.class.getSimpleName() +
            DataUtils.XML_FILE_EXTENTION, GraphicsVars.class.getClass()
        ));
        MainFrameVars.setInstance(
            (MainFrameVars) DataUtils.xmlDeserialize(
            DataUtils.DATA_ROOT + MainFrameVars.class.getSimpleName() +
            DataUtils.XML_FILE_EXTENTION, MainFrameVars.class.getClass()
        ));
    }
    
}
