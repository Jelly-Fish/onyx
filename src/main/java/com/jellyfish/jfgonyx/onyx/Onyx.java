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
import com.jellyfish.jfgonyx.onyx.entities.collections.OnyxPosCollection;
import com.jellyfish.jfgonyx.onyx.exceptions.InvalidOnyxPositionException;
import com.jellyfish.jfgonyx.onyx.exceptions.NoValidOnyxPositionsFoundException;
import com.jellyfish.jfgonyx.onyx.interfaces.search.OnyxAbstractSearchable;
import com.jellyfish.jfgonyx.onyx.search.ConnectionSearch;
import com.jellyfish.jfgonyx.onyx.search.PositionSearch;
import com.jellyfish.jfgonyx.onyx.search.RandomSearch;
import com.jellyfish.jfgonyx.onyx.search.VirtualConnetionSearch;
import com.jellyfish.jfgonyx.onyx.search.searchutils.SearchUtils;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author thw
 */
class Onyx {
    
    public static OnyxConst.COLOR winColor = null;
    public static boolean gameEnd = false;
    public static boolean whitePlayingLowBorder = false;
    public static boolean blackPlayingLowBorder = false;
    
    // String formats.
    private static final String ERR = "Something got messy :X >> %s";
    private static final String POSCOL_SEARCH_FORMAT = "SEARCH.ONYXPOSCOL -> [%s] score -> [%.1f]";
    private static final String CNX_SEARCH_FORMAT = "SEARCH.CNX -> [%s] score -> [%.3f]";
    private static final String VCNX_SEARCH_FORMAT = "SEARCH.VCNX -> [%s] score -> [%.3f]";
    private static final String WIN = "%s's WIN ! WuHu !!!";
            
    private final static HashMap<STYPE, OnyxAbstractSearchable> SEARCH = new HashMap<>();
    static {
        SEARCH.put(STYPE.POSCOL, new PositionSearch());
        SEARCH.put(STYPE.RANDOM, new RandomSearch());
        SEARCH.put(STYPE.CNX, new ConnectionSearch());
        SEARCH.put(STYPE.VIRTUALCNX, new VirtualConnetionSearch());
    }
    
    /**
     * Search types.
     */
    static enum STYPE {
        
        RANDOM("Random dumb search :X"), 
        POSCOL("Use onyx position collection for take or counter position searches."),
        CNX("Connection search style building & taking advantage of position trees."),
        VIRTUALCNX("Virtual best links simulation.");
        
        private final String desc;
        
        STYPE(final String desc) {
            this.desc = desc;
        }   
        
        public String getDesc() {
            return this.desc;
        }
    }
       
    static OnyxMove search(final OnyxGameImpl game, final OnyxConst.COLOR color) {
        
        try {
            
            final OnyxMove posSearchRes = SEARCH.get(STYPE.POSCOL).search(game, color);
            final OnyxMove cnxSearchRes = SEARCH.get(STYPE.CNX).search(game, color);       
            final OnyxMove virtualCnxRes = SEARCH.get(STYPE.VIRTUALCNX).search(game, color);            
            final OnyxMove m = SearchUtils.assertByScore(posSearchRes, cnxSearchRes, virtualCnxRes);
            if (m.isCapture()) game.getPosCollection().performTake(
                m.getPos().getKey(), color.bit, game.getDiamondCollection(), game.getPosCollection());
            
            return m;
            
        } catch (final NoValidOnyxPositionsFoundException nVOPFEx) {
            Logger.getLogger(Onyx.class.getName()).log(Level.SEVERE, null, nVOPFEx);
        } catch (final InvalidOnyxPositionException iOPEx) {
            Logger.getLogger(Onyx.class.getName()).log(Level.SEVERE, null, iOPEx + 
                    String.format(InvalidOnyxPositionException.DEFAULT_MSG, color.str));
        }
        
        return null;
    }
    
    static OnyxMove getNewVirtual(final OnyxPosCollection c, final OnyxGameImpl game, final OnyxConst.COLOR color) 
            throws NoValidOnyxPositionsFoundException, InvalidOnyxPositionException {
        return SEARCH.get(STYPE.RANDOM).search(game, color);
    }
        
    static void assertEndGame(final OnyxGameImpl game, final OnyxConst.COLOR color) throws NoValidOnyxPositionsFoundException {
        
        final boolean lose = ((ConnectionSearch) SEARCH.get(STYPE.CNX)).isWin(
            game.getPosCollection(), OnyxConst.COLOR.getOposite(color.bool));
        final boolean win = ((ConnectionSearch) SEARCH.get(STYPE.CNX)).isWin(
            game.getPosCollection(), color);

        Onyx.gameEnd = win || lose;

        if (Onyx.gameEnd) {
            Onyx.winColor = win ? color : OnyxConst.COLOR.getOposite(color.bool);
            game.displayGameStatus(game.getObserver());
            game.getObserver().notify(String.format(WIN, Onyx.winColor.str));
        }      
    }
    
}