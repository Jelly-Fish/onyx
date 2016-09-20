/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jellyfish.jfgonyx.onyx.interfaces;

import com.jellyfish.jfgonyx.onyx.constants.OnyxConst;
import com.jellyfish.jfgonyx.onyx.entities.OnyxMove;
import com.jellyfish.jfgonyx.onyx.exceptions.InvalidOnyxPositionException;
import com.jellyfish.jfgonyx.onyx.exceptions.NoValidOnyxPositionsFoundException;

/**
 * Main UI Interface.
 * @author thw
 */
public interface OnyxGame {
    
    /**
     * Move around virtual piece without playing a new move.
     * @param k the key to move towrds.
     * @return true if successful.
     * @throws InvalidOnyxPositionException if no such position found.
     */
    boolean moveVirtual(final String k) throws InvalidOnyxPositionException;
    
    /**
     * Validte virtual piece's position for move.
     * @return true if successful.
     * @throws InvalidOnyxPositionException if no such position found.
     */
    boolean playMove() throws InvalidOnyxPositionException;
    
    /**
     * Request engine to search for new move.
     * @param color the color to search for which can be a UI side search.
     * @return the move to play. 
     * @see OnyxMove
     * @throws NoValidOnyxPositionsFoundException if search result does not map to
     * a valid onyx position on the board.
     * @throws InvalidOnyxPositionException if no such position found.
     */
    OnyxMove requestMove(final OnyxConst.COLOR color) 
            throws NoValidOnyxPositionsFoundException, InvalidOnyxPositionException;
    
    /**
     * Append a new move to the game - the move is therefor a valid Onyx move.
     * @param move move to append to game.
     */
    void appendMove(final OnyxMove move);
    
}
