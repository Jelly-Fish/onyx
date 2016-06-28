/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jellyfish.jfgonyx.onyx.search.subroutines.connectionsearch;

import com.jellyfish.jfgonyx.onyx.abstractions.AbstractSubroutine;
import com.jellyfish.jfgonyx.onyx.constants.OnyxConst;
import com.jellyfish.jfgonyx.onyx.entities.OnyxPos;
import com.jellyfish.jfgonyx.onyx.entities.OnyxTail;
import com.jellyfish.jfgonyx.onyx.entities.collections.OnyxPosCollection;
import com.jellyfish.jfgonyx.onyx.exceptions.InvalidOnyxPositionException;
import com.jellyfish.jfgonyx.onyx.search.subroutines.positionsearch.OnyxPosStateSubroutine;
import com.jellyfish.jfgonyx.ui.OnyxBoard;
import java.util.List;

/**
 * @author thw
 */
public class VirtualTailCrossSearchResultsSubroutine extends AbstractSubroutine {
    
    /**
     * @param sT tail to search position for.
     * @param sTails result of virtual tail search : contains all tail candidates
     * from onyx best tail search.
     * @param oT oponent tail.
     * @param opTails result of virtual tail search : contains all tail
     * candidates from oponent tail search.
     * @param b onyx board.
     * @param c position collection.
     * @param color color to serach for.
     * @param opColor oponent color for oponent neighbour positions.
     * @return first OnyxPos found that belongs to both sT & oT tails.
     * @throws com.jellyfish.jfgonyx.onyx.exceptions.InvalidOnyxPositionException
     */
    public OnyxPos crossTailSearch(final OnyxTail sT, final List<OnyxTail> sTails, 
        final OnyxTail oT, final List<OnyxTail> opTails, final OnyxBoard b, 
        final OnyxPosCollection c, final OnyxConst.COLOR color, final OnyxConst.COLOR opColor) throws InvalidOnyxPositionException {

        if (sT == null || oT == null) return null;
        
        OnyxPos pos = this.crossMainTails(sT, oT, b, c, color, opColor);   
        if (pos == null) pos = this.crossWithAllOnyxTails(opTails, oT, b, c, color, opColor);
        if (pos == null) pos = this.crossAllTails(sTails, opTails, b, c, color, opColor);
                
        return pos;
    }
    
    /**
     * Search for all onyx tails search result and compare with oponent best tail candidate.
     * @param sTails contains all Onyx tail candidates search results.
     * @param oT oponent tail.
     * @param b onyx board.
     * @param c position collection.
     * @param color color to serach for.
     * @param opColor oponent color for oponent neighbour positions.
     * @return best position.
     * @throws InvalidOnyxPositionException 
     */
    private OnyxPos crossWithAllOnyxTails(final List<OnyxTail> sTails, final OnyxTail oT, 
        final OnyxBoard b, final OnyxPosCollection c, final OnyxConst.COLOR color, 
        final OnyxConst.COLOR opColor) throws InvalidOnyxPositionException {
        
        OnyxPos pos = null;
        
        for (OnyxTail t : sTails) {
            pos = this.crossMainTails(t, oT, b, c, color, opColor);  
            if (pos != null) break;
        }
        
        return pos;
    }
    
    /**
     * @param sTails contains all Onyx tail candidates search results.
     * @param opTails contains all Onyx oponent tail candidates search results.
     * @param b onyx board.
     * @param c position collection.
     * @param color color to serach for.
     * @param opColor oponent color for oponent neighbour positions.
     * @return best position.
     * @throws InvalidOnyxPositionException
     */
    private OnyxPos crossAllTails(final List<OnyxTail> sTails, final List<OnyxTail> opTails, 
        final OnyxBoard b, final OnyxPosCollection c, final OnyxConst.COLOR color, 
        final OnyxConst.COLOR opColor) throws InvalidOnyxPositionException {
        
        OnyxPos pos = null;
        
        for (OnyxTail sT : sTails) {
            for (OnyxTail oT : opTails) {
                pos = this.crossMainTails(sT, oT, b, c, color, opColor);
                if (pos != null) break;
            }            
        }
        
        return pos;
    }
    
    /**
     * Search best move crossing oponent's and onyx's main tails (lowest move 
     * count direct tails). This method may be called with any tail search
     * result. Does not 
     * @param sT Onyx tail to search position for.
     * @param oT oponent tail.
     * @param b onyx board.
     * @param c position collection.
     * @param color color to serach for.
     * @param opColor oponent color for oponent neighbour positions.
     * @return best position.
     * @throws InvalidOnyxPositionException 
     */
    private OnyxPos crossMainTails(final OnyxTail sT, final OnyxTail oT, 
        final OnyxBoard b, final OnyxPosCollection c, final OnyxConst.COLOR color, 
        final OnyxConst.COLOR opColor) throws InvalidOnyxPositionException {
        
        if (sT == null || oT == null) return null;
        OnyxPos pos = null;
        
        for (OnyxPos pOT : oT.getPositions()) {
            
            for (OnyxPos sOT : sT.getPositions()) {
                
                if (new OnyxPosStateSubroutine(sOT).willEnableTake(b, c, color) 
                    || sOT.isOccupied()) {
                    continue;
                }
                
                if (pos == null && sOT.getKey().equals(pOT.getKey())) pos = sOT;
                
                if (sOT.getKey().equals(pOT.getKey()) && (sOT.hasNeighbour(c, opColor) ||
                    sOT.hasNeighbour(c, color))) {
                    pos = sOT;
                }
                
                if (sOT.getKey().equals(pOT.getKey()) && sOT.hasNeighbour(c, opColor) &&
                    sOT.hasNeighbour(c, color)) {
                    pos = sOT;
                }
            }
        }
        
        return pos;
    }
    
}
