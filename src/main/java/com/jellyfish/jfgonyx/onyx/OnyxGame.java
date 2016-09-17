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

import com.jellyfish.jfgonyx.onyx.constants.OnyxConst;
import com.jellyfish.jfgonyx.onyx.entities.OnyxMove;
import com.jellyfish.jfgonyx.onyx.entities.OnyxPiece;
import com.jellyfish.jfgonyx.onyx.entities.OnyxPos;
import com.jellyfish.jfgonyx.onyx.entities.OnyxVirtualPiece;
import com.jellyfish.jfgonyx.onyx.entities.collections.OnyxDiamondCollection;
import com.jellyfish.jfgonyx.onyx.entities.collections.OnyxPosCollection;
import com.jellyfish.jfgonyx.onyx.exceptions.InvalidOnyxPositionException;
import com.jellyfish.jfgonyx.onyx.exceptions.NoValidOnyxPositionsFoundException;
import com.jellyfish.jfgonyx.onyx.exceptions.OnyxGameSyncException;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author thw
 */
public class OnyxGame {
    
    private final OnyxDiamondCollection diamonds;
    private final OnyxPosCollection positions;
    public boolean initialized = false;
    public HashMap<Integer, OnyxMove> moves = new HashMap<>(); 
    public boolean wait = false;
    public OnyxConst.COLOR engineColor = null;    
    private OnyxConst.COLOR colorToPlay = null;
    private boolean requestInitialized = false;
    private int moveCount = 0;

    /**
     * Construct.
     * @param engineColor engine color.
     */
    public OnyxGame(final OnyxConst.COLOR engineColor) { 
        
        Onyx.gameEnd = false;
        Onyx.whitePlayingLowBorder = false;
        Onyx.blackPlayingLowBorder = false;
        this.diamonds = new OnyxDiamondCollection().build();
        this.positions = new OnyxPosCollection(this.diamonds);
        this.wait = false;
        this.requestInitialized = false;
        this.colorToPlay = null;
        this.engineColor = engineColor;
        this.init(engineColor);
    }
    
    final void init(final OnyxConst.COLOR engineColor) {        
       
        initialized = true;  
        
        if (!engineColor.bool) {            
            initMove(OnyxConst.COLOR.getOposite(engineColor.bool));            
            try {
                performMove(positions);
            } catch (OnyxGameSyncException | NoValidOnyxPositionsFoundException | InvalidOnyxPositionException ex) {
                initialized = false;
                Logger.getLogger(OnyxGame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    /**
     * Perform move - move request must be initialized first.
     * @throws com.jellyfish.jfgonyx.onyx.exceptions.InvalidOnyxPositionException
     * @see OnyxGame openRequest.
     * @param c OnyxPos collection.
     * @throws OnyxGameSyncException
     * @throws NoValidOnyxPositionsFoundException 
     */
    public void performMove(final OnyxPosCollection c) 
            throws OnyxGameSyncException, NoValidOnyxPositionsFoundException, InvalidOnyxPositionException {
        
        checkInit();
        final OnyxMove m = requestMove();
        if (m != null && !Onyx.gameEnd) {
            appendMove(m);
            appendNewVirtual(c);
        }
        closeMove();
    }
    
    public void closeMove() {
        colorToPlay = null;
        initMoveRequest();
    }
      
    /**
     * @param color the color to play next or to search move for and append to this.
     */
    public void initMove(final OnyxConst.COLOR color) {
        colorToPlay = color;
        initMoveRequest();
    }
    
    public void appendMove(final OnyxPos pos, final OnyxPiece piece, final List<OnyxPos> captured) {
        moves.put(moves.size() + 1, 
            new OnyxMove(pos, piece,  captured.size() * OnyxConst.SCORE.TAKE.getValue())
        );
    }
    
    public void appendMove(final OnyxMove move) {
        moves.put(moves.size() + 1, move);
        ++moveCount;
    }
    
    private OnyxMove requestMove() 
            throws NoValidOnyxPositionsFoundException, InvalidOnyxPositionException {
        
        final OnyxMove m = Onyx.search(this, colorToPlay);
        if (Onyx.gameEnd) return null;
        if (m == null) throw new NoValidOnyxPositionsFoundException();
        else positions.clearOutlines();
        positions.getPosition(m.getPos().getKey()).setPiece(new OnyxPiece(colorToPlay, true));
        
        return m;
    }
    
    private void appendNewVirtual(final OnyxPosCollection c) 
            throws NoValidOnyxPositionsFoundException, InvalidOnyxPositionException {
        
        if (isGameEnd() || Onyx.isLose(c, colorToPlay)) return;
        final OnyxMove m = Onyx.getNewVirtual(c, this, colorToPlay);
        c.getPosition(m.getPos().getKey()).setVirtualPiece(
            new OnyxVirtualPiece(OnyxConst.COLOR.getVirtualOposite(colorToPlay.bool))
        );
    }
            
    private void initMoveRequest() {
        requestInitialized = colorToPlay != null;
    }
    
    /**
     * Check that move request color value has been set/initialized.
     * @throws OnyxGameSyncException 
     */
    private void checkInit() throws OnyxGameSyncException {
        
        if (colorToPlay == null) throw new OnyxGameSyncException();
        if (!requestInitialized) throw new OnyxGameSyncException(
                String.format(OnyxGameSyncException.WRONG_TURN_MSG, colorToPlay.str));
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
    
    public OnyxDiamondCollection getDiamondCollection() {
        return diamonds;
    }

    public OnyxPosCollection getPosCollection() {
        return positions;
    }
    
    public int getMoveCount() {
        return moveCount;
    }
    
}
