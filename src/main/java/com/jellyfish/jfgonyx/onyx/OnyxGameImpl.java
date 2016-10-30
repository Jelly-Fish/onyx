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
import com.jellyfish.jfgonyx.onyx.exceptions.OnyxEndGameException;
import com.jellyfish.jfgonyx.onyx.exceptions.OnyxGameSyncException;
import com.jellyfish.jfgonyx.onyx.interfaces.OnyxGame;
import com.jellyfish.jfgonyx.onyx.interfaces.OnyxObserver;
import com.jellyfish.jfgonyx.onyx.utils.GameDisplayUtils;
import com.jellyfish.jfgonyx.onyx.vars.OnyxCommonVars;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Onyx game interface implementation.
 * @author thw
 */
public class OnyxGameImpl implements OnyxGame {
    
    // <editor-fold defaultstate="collapsed" desc="private vars"> 
    private final OnyxDiamondCollection diamonds;
    private final OnyxPosCollection positions;
    private boolean initialized = false;
    private Map<Integer, OnyxMove> moves = new HashMap<>();
    private OnyxConst.COLOR engineColor = null;
    private int moveCount = 0;
    private OnyxObserver observer = null;
    // </editor-fold> 

    // <editor-fold defaultstate="collapsed" desc="Construct methods"> 
    /**
     * Construct.
     * @param engineColor engine color.
     */
    OnyxGameImpl(final OnyxConst.COLOR engineColor) { 
        
            Onyx.gameEnd = false;
            Onyx.whitePlayingLowBorder = false;
            Onyx.blackPlayingLowBorder = false;
            this.diamonds = new OnyxDiamondCollection().build();
            this.positions = new OnyxPosCollection(this.diamonds);
            this.engineColor = engineColor;
            
        try {
            this.initStartLayout();        
            this.init(engineColor);
        } catch (final OnyxGameSyncException OGSEx) {
            Logger.getLogger(OnyxGameImpl.class.getName()).log(Level.SEVERE, null, OGSEx);
            System.exit(0);
        } catch (final NoValidOnyxPositionsFoundException | InvalidOnyxPositionException | OnyxEndGameException ex) {
            Logger.getLogger(OnyxGameImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    // </editor-fold> 
    
    // <editor-fold defaultstate="collapsed" desc="Init private methods"> 
    private void init(final OnyxConst.COLOR engineColor) throws OnyxGameSyncException, NoValidOnyxPositionsFoundException, 
            InvalidOnyxPositionException, OnyxEndGameException {
        
        if (initialized) throw new OnyxGameSyncException(OnyxGameSyncException.DEFAULT_MSG);
        initialized = true;  
        
        if (engineColor.bool) {            
            performMove(positions, engineColor);
        } else {
            positions.spawnVirtualPiece(OnyxConst.COLOR.VIRTUAL_BLACK);
        }
    }
    
    private void initStartLayout() throws OnyxGameSyncException {
        
        if (initialized) throw new OnyxGameSyncException(OnyxGameSyncException.DEFAULT_MSG);
        
        final String separator = "-";
        final String one = "1,0";
        final DecimalFormat df = new DecimalFormat("#.0");
        
        for (OnyxPos p : positions.getPositions().values()) p.setPiece(null);

        final String min = df.format(OnyxCommonVars.getInstance().BOARD_SIDE_POS_COUNT / 2f);
        final String max = df.format(OnyxCommonVars.getInstance().BOARD_SIDE_POS_COUNT);
        final String minPlus = df.format((OnyxCommonVars.getInstance().BOARD_SIDE_POS_COUNT / 2f) + 1f);
        
        positions.getPosition(one + separator + min).addPiece(new OnyxPiece(OnyxConst.COLOR.BLACK));      
        this.appendMove(new OnyxMove(positions.getPosition(one + separator + min), 
            positions.getPosition(one + separator + min).getPiece()));
        
        positions.getPosition(min + separator + one).addPiece(new OnyxPiece(OnyxConst.COLOR.WHITE));
        this.appendMove(new OnyxMove(positions.getPosition(min + separator + one), 
                positions.getPosition(min + separator + one).getPiece()));
        
        positions.getPosition(max + separator + minPlus).addPiece(new OnyxPiece(OnyxConst.COLOR.BLACK));
        this.appendMove(new OnyxMove(positions.getPosition(max + separator + minPlus), 
                positions.getPosition(max + separator + minPlus).getPiece()));
        
        positions.getPosition(minPlus + separator + max).addPiece(new OnyxPiece(OnyxConst.COLOR.WHITE));
        this.appendMove(new OnyxMove(positions.getPosition(minPlus + separator + max), 
                positions.getPosition(minPlus + separator + max).getPiece()));
        
        positions.getPosition(one + separator + minPlus).addPiece(new OnyxPiece(OnyxConst.COLOR.BLACK));
        this.appendMove(new OnyxMove(positions.getPosition(one + separator + minPlus), 
                positions.getPosition(one + separator + minPlus).getPiece()));
        
        positions.getPosition(minPlus + separator + one).addPiece(new OnyxPiece(OnyxConst.COLOR.WHITE));
        this.appendMove(new OnyxMove(positions.getPosition(minPlus + separator + one), 
                positions.getPosition(minPlus + separator + one).getPiece()));
        
        positions.getPosition(max + separator + min).addPiece(new OnyxPiece(OnyxConst.COLOR.BLACK));
        this.appendMove(new OnyxMove(positions.getPosition(max + separator + min), 
                positions.getPosition(max + separator + min).getPiece()));
        
        positions.getPosition(min + separator + max).addPiece(new OnyxPiece(OnyxConst.COLOR.WHITE));
        this.appendMove(new OnyxMove(positions.getPosition(min + separator + max), 
                positions.getPosition(min + separator + max).getPiece()));
    }
    // </editor-fold> 
    
    // <editor-fold defaultstate="collapsed" desc="Impl overrides"> 
    @Override
    public String playMove() throws InvalidOnyxPositionException {
        
        List<OnyxPos> posSet = null;
        final OnyxVirtualPiece vp = positions.getVirtualPiece();
        final String k = vp.getTmpOnyxPosition().getKey();
        final OnyxPos tmpPos = positions.getPosition(k);
        
        if (diamonds.isDiamondCenter(k) && !isCenterPosPlayable(k)) throw new InvalidOnyxPositionException(   
            String.format(InvalidOnyxPositionException.INVALID_CENTER, k));
        if (tmpPos.isOccupied()) throw new InvalidOnyxPositionException(
            String.format(InvalidOnyxPositionException.OCCUPIED, k));
        
        positions.getPosition(k).setPiece(
            new OnyxPiece(vp.color.bool ? OnyxConst.COLOR.BLACK : OnyxConst.COLOR.WHITE)
        );
        
        posSet = positions.getTakePositions(k, vp.color.bit, diamonds, positions);
                
        positions.getPosition(k).setVirtualPiece(null);
        OnyxMove m = null;
        if (posSet != null && !Onyx.gameEnd) {
            posSet = positions.performTake(k, vp.color.bit, diamonds, positions);
            m = new OnyxMove(positions.getPosition(k), positions.getPosition(k).getPiece(), posSet, 
                posSet.size() * OnyxConst.SCORE.TAKE.getValue());
        } else {
            m = new OnyxMove(positions.getPosition(k), positions.getPosition(k).getPiece());
        }
        
        return appendMove(m);
    }
    
    @Override
    public String moveVirtual(final String k) throws InvalidOnyxPositionException, OnyxEndGameException {

        if (Onyx.gameEnd) throw new OnyxEndGameException(Onyx.winColor);        
        final String virtualK = positions.getVirtualPiece().getTmpOnyxPosition().getKey();
        if (virtualK.equals(k) && positions.getPositions().containsKey(k)) return k;

        if (positions.getPositions().containsKey(k)) {
            positions.getVirtualPiece().setTmpOnyxPosition(positions.getPositions().get(k));
            positions.getPosition(k).setVirtualPiece(positions.getVirtualPiece());
            positions.getPosition(virtualK).setVirtualPiece(null);
            
            return k;
        }
        
        throw new InvalidOnyxPositionException(
            String.format(InvalidOnyxPositionException.MSG_STRING_ARG, k));
    }
    
    @Override
    public OnyxMove requestMove(final OnyxConst.COLOR color) 
            throws NoValidOnyxPositionsFoundException, InvalidOnyxPositionException, OnyxEndGameException {
        
        final OnyxMove m = Onyx.search(this, color);
        if (Onyx.gameEnd) throw new OnyxEndGameException(Onyx.winColor);
        if (m == null) throw new NoValidOnyxPositionsFoundException();
        else positions.clearOutlines();
        positions.getPosition(m.getPos().getKey()).setPiece(new OnyxPiece(color, true));
        
        Onyx.assertEndGame(this, color);
        
        return m;
    }
    
    @Override
    public String requestNewMove(final OnyxConst.COLOR color) 
            throws NoValidOnyxPositionsFoundException, InvalidOnyxPositionException, OnyxEndGameException {   
        return appendMove(requestMove(color));
    }
    
    @Override
    public String appendMove(final OnyxMove move) {
       
        moves.put(moves.size() + 1, move);
        ++moveCount;
        if (observer != null) observer.notifyMove(move.toString());
        
        return move.toString() + " color: " + move.getPos().getPiece().color.str;
    }
    
    @Override
    public String appendNewVirtual(final OnyxConst.COLOR color) throws NoValidOnyxPositionsFoundException, 
            InvalidOnyxPositionException {
        
        final OnyxMove m = Onyx.getNewVirtual(positions, this, color);
        positions.getPosition(m.getPos().getKey()).setVirtualPiece(
            new OnyxVirtualPiece(color)
        );
        
        return m.toString();
    }
    
    @Override
    public void notifyStartLayout(final OnyxObserver observer) {     
        if (observer == null) return;
        for (OnyxMove m : moves.values()) observer.notifyMove(m.toString());
        displayGameStatus(observer);
    }
        
    @Override
    public void displayGameStatus(final OnyxObserver observer) {
        observer.notifyGameStatus(GameDisplayUtils.buildGameToTextOutput(moves, positions,
            OnyxCommonVars.getInstance().BOARD_SIDE_POS_COUNT));
    }
    
    @Override
    public OnyxObserver getObserver() {
        return observer;
    }
    // </editor-fold> 
    
    // <editor-fold defaultstate="collapsed" desc="private methods"> 
    /**
     * Perform move - move request must be initialized first.
     * @throws com.jellyfish.jfgonyx.onyx.exceptions.InvalidOnyxPositionException
     * @see OnyxGameImpl openRequest.
     * @param c OnyxPos collection.
     * @throws OnyxGameSyncException
     * @throws NoValidOnyxPositionsFoundException 
     * @throws OnyxEndGameException
     */
    private void performMove(final OnyxPosCollection c, final OnyxConst.COLOR color) 
            throws OnyxGameSyncException, NoValidOnyxPositionsFoundException, 
            InvalidOnyxPositionException, OnyxEndGameException {
        
        final OnyxMove m = requestMove(color);
        if (m != null && !Onyx.gameEnd) {
            appendMove(m);
        }
    }
    
    private boolean isCenterPosPlayable(final String k) {
        
        int counter = 0;
        if (k == null || !positions.containsPosition(k) || 
                !diamonds.isDiamondCenter(k)) {
            return false;
        }
        
        for (String cK : positions.getPosition(k).connections) {
            counter = positions.getPosition(cK).isOccupied() ? ++counter : counter;
        }
        
        return counter == 0;
    }
    // </editor-fold> 
    
    // <editor-fold defaultstate="collapsed" desc="public accessors & methods"> 
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
    
    public Map<Integer, OnyxMove> getMoves() {
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

    @Override
    public void setObserver(final OnyxObserver observer) {
        this.observer = observer;
    }
    // </editor-fold> 
    
}
