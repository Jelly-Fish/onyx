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
package com.jellyfish.jfgonyx.onyx;

import com.jellyfish.jfgonyx.onyx.entities.OnyxMove;
import com.jellyfish.jfgonyx.onyx.search.searchutils.Intmap;
import com.jellyfish.jfgonyx.constants.DTStampConst;
import com.jellyfish.jfgonyx.onyx.constants.OnyxConst;
import com.jellyfish.jfgonyx.helpers.HTMLDisplayHelper;
import com.jellyfish.jfgonyx.onyx.entities.OnyxPiece;
import com.jellyfish.jfgonyx.onyx.entities.OnyxPos;
import com.jellyfish.jfgonyx.onyx.entities.collections.OnyxPosCollection;
import com.jellyfish.jfgonyx.onyx.entities.OnyxVirtualPiece;
import com.jellyfish.jfgonyx.onyx.exceptions.InvalidOnyxPositionException;
import com.jellyfish.jfgonyx.onyx.exceptions.NoValidOnyxPositionsFoundException;
import com.jellyfish.jfgonyx.onyx.exceptions.OnyxGameSyncException;
import com.jellyfish.jfgonyx.onyx.interfaces.OnyxBoardI;
import com.jellyfish.jfgonyx.ui.OnyxBoard;
import java.util.HashMap;
import java.util.List;

/**
 * @author thw
 */
public class OnyxGame {
    
    public boolean initialized = false;
    public HashMap<Integer, OnyxMove> moves = new HashMap<>(); 
    public boolean wait = false;
    public OnyxBoardI boardInterface = null;
    public String dtStamp;
    public OnyxConst.COLOR engineColor = null;
    
    private static OnyxGame instance = null;
    private OnyxConst.COLOR colorToPlay = null;
    private boolean requestInitialized = false;
    private int moveCount = 0;

    private OnyxGame() { 
        Onyx.gameEnd = false;
        Onyx.whitePlayingLowBorder = false;
        Onyx.blackPlayingLowBorder = false;
    }
    
    public void init(final OnyxBoardI boardInterface, final OnyxConst.COLOR engineColor) {
        this.wait = false;
        this.requestInitialized = false;
        this.colorToPlay = null;
        this.boardInterface = boardInterface;
        this.dtStamp = DTStampConst.sdf.format(new java.util.Date());
        this.engineColor = engineColor;
    }
    
    /**
     * Perform move - move request must be initialized first.
     * @throws com.jellyfish.jfgonyx.onyx.exceptions.InvalidOnyxPositionException
     * @see OnyxGame openRequest.
     * @param c OnyxPos collection.
     * @param board OnyxBoard instance.
     * @throws OnyxGameSyncException
     * @throws NoValidOnyxPositionsFoundException 
     */
    public void performMove(final OnyxPosCollection c, final OnyxBoard board) 
            throws OnyxGameSyncException, NoValidOnyxPositionsFoundException, InvalidOnyxPositionException {
        
        this.checkInit();
        final OnyxMove m = this.requestMove(c, board);
        if (m != null && !Onyx.gameEnd) {
            this.appendMove(m);
            this.appendNewVirtual(c, board);
            board.notifyMove(m, HTMLDisplayHelper.HOT_PINK);
        }
        this.closeMove();
    }
    
    public void closeMove() {
        this.colorToPlay = null;
        this.initMoveRequest();
    }
      
    /**
     * @param color the color to play next or to search move for and append to board.
     */
    public void initMove(final OnyxConst.COLOR color) {
        this.colorToPlay = color;
        this.initMoveRequest();
    }
    
    public void appendMove(final OnyxPos pos, final OnyxPiece piece, final List<OnyxPos> captured) {
        this.moves.put(this.moves.size() + 1, 
            new OnyxMove(pos, piece,  captured.size() * OnyxConst.SCORE.TAKE.getValue())
        );
    }
    
    public void appendMove(final OnyxMove move) {
                
        this.moves.put(this.moves.size() + 1, move);
        ++this.moveCount;
        if (this.initialized && this.boardInterface != null) {
            new Intmap(this.boardInterface.getPosCollection()).print(
                this.moveCount, this.dtStamp
            );            
        }
    }
    
    private OnyxMove requestMove(final OnyxPosCollection c, final OnyxBoard board) 
            throws NoValidOnyxPositionsFoundException, InvalidOnyxPositionException {
        
        final OnyxMove m = Onyx.search(c, board, this.colorToPlay);
        if (Onyx.gameEnd) return null;
        if (m == null) throw new NoValidOnyxPositionsFoundException();
        else board.getPosCollection().clearOutlines();
        c.getPosition(m.getPos().getKey()).setPiece(new OnyxPiece(this.colorToPlay, true));
        return m;
    }
    
    private void appendNewVirtual(final OnyxPosCollection c, final OnyxBoard board) 
            throws NoValidOnyxPositionsFoundException, InvalidOnyxPositionException {
        
        if (this.isGameEnd() || Onyx.isLose(c, this.colorToPlay)) return;
        final OnyxMove m = Onyx.getNewVirtual(c, board, this.colorToPlay);
        c.getPosition(m.getPos().getKey()).setVirtualPiece(
            new OnyxVirtualPiece(OnyxConst.COLOR.getVirtualOposite(this.colorToPlay.bool))
        );
    }
            
    private void initMoveRequest() {
        this.requestInitialized = this.colorToPlay != null;
    }
    
    /**
     * Check that move request color value has been set/initialized.
     * @throws OnyxGameSyncException 
     */
    private void checkInit() throws OnyxGameSyncException {
        
        if (this.colorToPlay == null) throw new OnyxGameSyncException();
        if (!this.requestInitialized) throw new OnyxGameSyncException(
                String.format(OnyxGameSyncException.WRONG_TURN_MSG, this.colorToPlay.str));
    }
    
    /**
     * Is White/Black playing tails starting at high or low border ?
     * @param m Onyx move instance.
     * @see OnyxMove
     */    
    public void updateTailTendency(final OnyxMove m) {
        
        if (!m.hasTailStart() || !m.getTailStartPos().isOccupied()) return;
        
        if (m.getTailStartPos().getPiece().color.bool) {
            Onyx.blackPlayingLowBorder = m.getTailStartPos().isLowXBorder();
        } else {
            Onyx.whitePlayingLowBorder = m.getTailStartPos().isLowYBorder();
        }
    }
     
    public boolean getLowBorderTendency(final OnyxConst.COLOR color) {
        return color.bool ? Onyx.blackPlayingLowBorder : Onyx.whitePlayingLowBorder;
    }
    
    public boolean isGameEnd() {
        return Onyx.gameEnd;
    }
    
    public void setGameEnd(final boolean b) {
        Onyx.gameEnd = b;
    }
    
    public HashMap<Integer, OnyxMove> getMoves() {
        return moves;
    }
    
    public int getMoveCount() {
        return this.moveCount;
    }
    
    public static OnyxGame getInstance() {
        
        if (OnyxGame.instance == null) {
            OnyxGame.instance = new OnyxGame();
        }
        
        return OnyxGame.instance;
    }
    
    public static OnyxGame newInstance() {
        OnyxGame.instance = new OnyxGame();
        return OnyxGame.instance;
    }
    
}
