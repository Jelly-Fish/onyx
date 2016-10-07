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
import com.jellyfish.jfgonyx.onyx.exceptions.OnyxEndGameException;

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
    String moveVirtual(final String k) throws InvalidOnyxPositionException;
    
    /**
     * Validte virtual piece's position for move.
     * @return Move if successful.
     * @throws InvalidOnyxPositionException if no such position found.
     */
    String playMove() throws InvalidOnyxPositionException;
    
    /**
     * Request engine to search for new move.
     * @param color the color to search for which can be a UI side search.
     * @return the move to play. 
     * @throws com.jellyfish.jfgonyx.onyx.exceptions.OnyxEndGameException 
     * @see OnyxMove
     * @throws NoValidOnyxPositionsFoundException if search result does not map to
     * a valid onyx position on the board.
     * @throws InvalidOnyxPositionException if no such position found.
     */
    OnyxMove requestMove(final OnyxConst.COLOR color) 
            throws NoValidOnyxPositionsFoundException, InvalidOnyxPositionException, OnyxEndGameException;
    
    /**
     * Request engine to search for and play new move.
     * @param color the color to search for which can be a UI side search. 
     * @return move.
     * @throws com.jellyfish.jfgonyx.onyx.exceptions.OnyxEndGameException 
     * @see OnyxMove
     * @throws NoValidOnyxPositionsFoundException if search result does not map to
     * a valid onyx position on the board.
     * @throws InvalidOnyxPositionException if no such position found.
     */
    String requestNewMove(final OnyxConst.COLOR color) 
            throws NoValidOnyxPositionsFoundException, InvalidOnyxPositionException, OnyxEndGameException;
    
    /**
     * Append a new move to the game - the move is therefor a valid Onyx move.
     * @param move move to append to game.
     * @return move position in alphanum.
     */
    String appendMove(final OnyxMove move);
    
    /**
     * Add new virtual for UI management.
     * @return new virtual position.
     * @throws NoValidOnyxPositionsFoundException if no valid position is available.
     * @throws InvalidOnyxPositionException if no such position found.
     */
    String appendNewVirtual() throws NoValidOnyxPositionsFoundException, InvalidOnyxPositionException;    
    
}
