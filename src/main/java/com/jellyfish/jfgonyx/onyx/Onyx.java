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
import com.jellyfish.jfgonyx.helpers.HTMLDisplayHelper;
import com.jellyfish.jfgonyx.onyx.entities.OnyxMove;
import com.jellyfish.jfgonyx.onyx.entities.collections.OnyxPosCollection;
import com.jellyfish.jfgonyx.onyx.exceptions.InvalidOnyxPositionException;
import com.jellyfish.jfgonyx.onyx.exceptions.NoValidOnyxPositionsFoundException;
import com.jellyfish.jfgonyx.onyx.interfaces.search.OnyxAbstractSearchable;
import com.jellyfish.jfgonyx.onyx.search.*;
import com.jellyfish.jfgonyx.onyx.search.searchutils.SearchUtils;
import com.jellyfish.jfgonyx.ui.MainFrame;
import com.jellyfish.jfgonyx.ui.OnyxBoard;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.StringUtils;

/**
 * @author thw
 */
class Onyx {
    
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
       
    static OnyxMove search(final OnyxPosCollection c, final OnyxBoard board, final OnyxConst.COLOR color) {
        
        try {
            
            final OnyxMove posSearchRes = SEARCH.get(STYPE.POSCOL).search(c, board, color);
            final boolean win = ((ConnectionSearch) SEARCH.get(STYPE.CNX)).isWin(
                    c, OnyxConst.COLOR.getOposite(color.bool));
            final boolean lose = ((ConnectionSearch) SEARCH.get(STYPE.CNX)).isWin(
                    c, color);
            final OnyxMove cnxSearchRes = SEARCH.get(STYPE.CNX).search(c, board, color);       
            final OnyxMove virtualCnxRes = SEARCH.get(STYPE.VIRTUALCNX).search(c, board, color);
            
            // Assert game ended :
            Onyx.gameEnd = win || lose;
                        
            /**
             * FIXME refactor : get this print stuff out of search method.
             * [START] Do printing debug stuff... 
             */
            if (posSearchRes != null) print(String.format(POSCOL_SEARCH_FORMAT,
                    OnyxConst.POS_MAP.get(posSearchRes.getPos().getKey()), posSearchRes.getScore()));
            if (cnxSearchRes != null) print(String.format(CNX_SEARCH_FORMAT,
                OnyxConst.POS_MAP.get(cnxSearchRes.getPos().getKey()), cnxSearchRes.getScore()));
            if (virtualCnxRes != null) print(String.format(VCNX_SEARCH_FORMAT,
                OnyxConst.POS_MAP.get(virtualCnxRes.getPos().getKey()), virtualCnxRes.getScore()));
            print(win ? 
                String.format(WIN, OnyxConst.COLOR.getOposite(color.bool).str) : 
                StringUtils.EMPTY, HTMLDisplayHelper.GOLD);
            /** [END] Do printing debug stuff... */
            
            if (Onyx.gameEnd) return null;            
            final OnyxMove m = SearchUtils.assertByScore(posSearchRes, cnxSearchRes, virtualCnxRes);
            if (m.isCapture()) c.performTake(m.getPos().getKey(), color.bit, board);
            
            return m;
            
        } catch (final NoValidOnyxPositionsFoundException nVOPFEx) {
            Logger.getLogger(Onyx.class.getName()).log(Level.SEVERE, null, nVOPFEx);
            print(String.format(ERR, nVOPFEx.getMessage()));
        } catch (final InvalidOnyxPositionException iOPEx) {
            Logger.getLogger(Onyx.class.getName()).log(Level.SEVERE, null, iOPEx + 
                    String.format(InvalidOnyxPositionException.DEFAULT_MSG, color.str));
            print(String.format(ERR, iOPEx.getMessage()));
        }
        
        return null;
    }
    
    static OnyxMove getNewVirtual(final OnyxPosCollection c, final OnyxBoard board, final OnyxConst.COLOR color) 
            throws NoValidOnyxPositionsFoundException, InvalidOnyxPositionException {
        return SEARCH.get(STYPE.RANDOM).search(c, board, color);
    }
    
    static boolean isLose(final OnyxPosCollection c, final OnyxConst.COLOR color) 
            throws NoValidOnyxPositionsFoundException {
        Onyx.gameEnd = ((ConnectionSearch) SEARCH.get(STYPE.CNX)).isWin(c, color);
        return Onyx.gameEnd;
    }
    
    private static void print(final String s) {
        if (!StringUtils.isBlank(s)) MainFrame.print(s, HTMLDisplayHelper.GAINSBORO);
    }
    
    private static void print(final String s, final String style) {
        if (!StringUtils.isBlank(s)) MainFrame.print(s, style);
    }
        
}
