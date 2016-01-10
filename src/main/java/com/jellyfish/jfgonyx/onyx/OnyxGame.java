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

import com.jellyfish.jfgonyx.constants.GraphicsConst;
import com.jellyfish.jfgonyx.entities.OnyxPiece;
import com.jellyfish.jfgonyx.entities.OnyxPosCollection;
import com.jellyfish.jfgonyx.entities.OnyxVirtualPiece;
import com.jellyfish.jfgonyx.onyx.exceptions.InvalidOnyxPositionException;
import com.jellyfish.jfgonyx.onyx.exceptions.NoValidOnyxPositionsFoundException;
import com.jellyfish.jfgonyx.onyx.exceptions.OnyxGameSyncException;
import com.jellyfish.jfgonyx.ui.OnyxBoard;
import org.apache.commons.lang3.StringUtils;

/**
 * @author thw
 */
public class OnyxGame {
    
    private static GraphicsConst.COLOR colorToPlay = null;
    private static boolean requestInitialized = false;
    
    /**
     * Perform move - move request must be initialized first.
     * @throws com.jellyfish.jfgonyx.onyx.exceptions.InvalidOnyxPositionException
     * @see OnyxGame openRequest.
     * @param c OnyxPos collection.
     * @param board OnyxBoard instance.
     * @throws OnyxGameSyncException
     * @throws NoValidOnyxPositionsFoundException 
     */
    public static void performMove(final OnyxPosCollection c, final OnyxBoard board) 
            throws OnyxGameSyncException, NoValidOnyxPositionsFoundException, InvalidOnyxPositionException {
        
        OnyxGame.checkInit();
        final String k = OnyxGame.requestMove(c, board);
        OnyxGame.appendNewVirtual(c, board);
        OnyxGame.closeMove();
        board.getObserver().notifyMove(k);
    }
    
    private static String requestMove(final OnyxPosCollection c, final OnyxBoard board) 
            throws NoValidOnyxPositionsFoundException, InvalidOnyxPositionException {
        
        final String k = Onyx.getSEARCH().get(Onyx.SEARCH_TYPE.ONYXPOSCOL).search(c, board, OnyxGame.colorToPlay);
        System.out.println("searc k = " + k);
        if (StringUtils.isBlank(k)) throw new NoValidOnyxPositionsFoundException();
        c.getPosition(k).setPiece(new OnyxPiece(OnyxGame.colorToPlay));
        return k;
    }
    
    private static void appendNewVirtual(final OnyxPosCollection c, final OnyxBoard board) 
            throws NoValidOnyxPositionsFoundException, InvalidOnyxPositionException {
        
        final String virtualKey = Onyx.getSEARCH().get(Onyx.SEARCH_TYPE.RANDOM).search(c, board, OnyxGame.colorToPlay);
        c.getPosition(virtualKey).setVirtualPiece(
             new OnyxVirtualPiece(GraphicsConst.COLOR.getVirtualOposite(OnyxGame.colorToPlay.boolColor))
        );
    }
    
    /**
     * @param color the color to play next or to search move for and append to board.
     */
    public static void initMove(final GraphicsConst.COLOR color) {
        OnyxGame.colorToPlay = color;
        OnyxGame.initMoveRequest();
    }
    
    public static void closeMove() {
        OnyxGame.colorToPlay = null;
        OnyxGame.initMoveRequest();
    }
    
    private static void initMoveRequest() {
        OnyxGame.requestInitialized = OnyxGame.colorToPlay != null;
    }
    
    /**
     * Check that move request color value has been set/initialized.
     * @throws OnyxGameSyncException 
     */
    private static void checkInit() throws OnyxGameSyncException {
        if (OnyxGame.colorToPlay == null) throw new OnyxGameSyncException();
        if (!OnyxGame.requestInitialized) throw new OnyxGameSyncException(
                String.format(OnyxGameSyncException.WRONG_TURN_MSG, OnyxGame.colorToPlay.strColor));
    }
    
}
