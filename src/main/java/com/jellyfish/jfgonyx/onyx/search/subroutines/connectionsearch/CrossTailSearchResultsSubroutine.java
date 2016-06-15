/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jellyfish.jfgonyx.onyx.search.subroutines.connectionsearch;

import com.jellyfish.jfgonyx.onyx.abstractions.AbstractSubroutine;
import com.jellyfish.jfgonyx.onyx.constants.OnyxConst;
import com.jellyfish.jfgonyx.onyx.entities.OnyxMove;
import com.jellyfish.jfgonyx.onyx.entities.OnyxPos;
import com.jellyfish.jfgonyx.onyx.entities.OnyxTail;
import com.jellyfish.jfgonyx.onyx.entities.collections.OnyxPosCollection;
import com.jellyfish.jfgonyx.onyx.exceptions.InvalidOnyxPositionException;
import com.jellyfish.jfgonyx.onyx.search.subroutines.positionsearch.OnyxPosStateSubroutine;
import com.jellyfish.jfgonyx.ui.OnyxBoard;
import java.util.List;

/**
 *
 * @author thw
 */
public class CrossTailSearchResultsSubroutine extends AbstractSubroutine {
    
    /**
     * @param sT tail to search position for.
     * @param sTails result of virtual tail search : contains all candidates for
     * best tail (best tail = param 0).
     * @param oT oponent tail.
     * @param b onyx board.
     * @param c position collection.
     * @param color color to serach for.
     * @param opColor oponent color for oponent neighbour positions.
     * @param opTailMove oponent tail best move (depending on quality of
     * super class's search...).
     * @return first OnyxPos found that belongs to both sT & oT tails.
     * @throws com.jellyfish.jfgonyx.onyx.exceptions.InvalidOnyxPositionException
     */
    public OnyxPos crossTailSearch(final OnyxTail sT, final List<OnyxTail> sTails, final OnyxTail oT, 
        final OnyxBoard b, final OnyxPosCollection c, final OnyxConst.COLOR color, final OnyxConst.COLOR opColor,
        final OnyxMove opTailMove) throws InvalidOnyxPositionException {

        if (sT == null || oT == null) return null;
        
        OnyxPos pos = this.crossTails(sT, oT, b, c, color, opColor, true, opTailMove);   
        
        if (pos == null) {
            for (OnyxTail t : sTails) {
                pos = this.crossTails(sT, oT, b, c, color, opColor, false, opTailMove);  
                if (pos != null) break;
            }
        }
        
        if (pos == null) {
            for (OnyxPos sOT : sT.getPositions()) {
                if (!sOT.isOccupied() && sOT.hasNeighbour(c, color)) {
                    pos = sOT;
                    break;                  
                }
            }
        }
                
        return pos;
    }
    
    private OnyxPos crossTails(final OnyxTail sT, final OnyxTail oT, 
        final OnyxBoard b, final OnyxPosCollection c, final OnyxConst.COLOR color, final OnyxConst.COLOR opColor,
        final boolean checkOpTailMove, final OnyxMove opTailMove) throws InvalidOnyxPositionException {
        
        if (sT == null || oT == null) return null;
        OnyxPos pos = null;
        
        for (OnyxPos pOT : oT.getPositions()) {
            
            for (OnyxPos sOT : sT.getPositions()) {
                
                if (new OnyxPosStateSubroutine(sOT).willEnableTake(b, c, color) 
                    || sOT.isOccupied()) {
                    continue;
                }
                
                if (checkOpTailMove && sOT.getKey().equals(opTailMove.getPos().getKey())) {
                    pos = sOT;
                }
                
                if (sOT.getKey().equals(pOT.getKey())) pos = sOT;
                
                if (sOT.getKey().equals(pOT.getKey()) && sOT.hasNeighbour(c, opColor) &&
                    sOT.hasNeighbour(c, color)) {
                    pos = sOT;
                }
            }
        }
        
        return pos;
    }
    
}
