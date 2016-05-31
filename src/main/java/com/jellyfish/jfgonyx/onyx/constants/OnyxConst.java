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
package com.jellyfish.jfgonyx.onyx.constants;

import com.jellyfish.jfgonyx.constants.OnyxBoardPositionOutlineConst;
import com.jellyfish.jfgonyx.vars.GraphicsVars;
import java.awt.Color;
import org.apache.commons.lang3.StringUtils;

/**
 * @author thw
 */
public class OnyxConst {
    
    public static final String KEY_SEPARATOR = "/";
    private static final String SPLIT = "-";
    private static final String DOT = ".";
    private static final String COMMA = ",";
    private static final String POSITIVE_DECIMAL = "5";
    
    
    public static final class POS_MAP {
        
        private static String strX, strY;
        private static float fX, fY;
        
        public static String get(final String key) {
            
            String pos = StringUtils.EMPTY;
            
            try {
                
                strX = key.split(SPLIT)[0].replaceAll(COMMA, DOT);
                strY = key.split(SPLIT)[1].replaceAll(COMMA, DOT);
                fX = Float.valueOf(strX);
                fY = Float.valueOf(strY);

                if (strX.substring(strX.indexOf(DOT) + 1, strX.length()).equals(POSITIVE_DECIMAL)) {
                    pos += OnyxBoardPositionOutlineConst.CHAR_VALUES[(int) ((fX - .5f) - 1f)] + SPLIT;
                    pos += OnyxBoardPositionOutlineConst.CHAR_VALUES[(int) ((fX + .5f) - 1f)] + SPLIT;
                    pos += String.valueOf(Math.abs((int) ((fY - .5f) - 
                            (GraphicsVars.getInstance().BOARD_SIDE_SQUARE_COUNT + 1f))) + 1) + SPLIT;
                    pos += String.valueOf(Math.abs((int) ((fY + .5f) - 
                            (GraphicsVars.getInstance().BOARD_SIDE_SQUARE_COUNT + 1f))) + 1);                    
                } else {
                    pos += OnyxBoardPositionOutlineConst.CHAR_VALUES[(int) (fX - 1f)] + SPLIT;
                    pos += String.valueOf(Math.abs((int) (fY - 
                        ((GraphicsVars.getInstance().BOARD_SIDE_SQUARE_COUNT + 1f) + 1f))));
                }    
            } catch (final Exception ex) {
                pos = ">> [!] POS_MAP convertion failed for " + key + "\n>> " + ex.getMessage();
            }
            
            return pos;
        }
        
    }
    
    public static enum SCORE {
    
        WIN(1000f), 
        WIN_LINK(999.999f), 
        COUNTER_WIN_LINK(998.999f), 
        TAKE(600.0f), 
        NEIGHBOUR(1f), 
        TAIL(13.37f),
        VTAIL(202.101f),
        SUB_TAIL(39.77f),
        RANDOM(1f), 
        COUNTER_POS(101.1f), 
        ATTACK(34.4f), 
        SMALL_LOCK(67.4f),
        BIG_LOCK(301.2f), 
        CENTER(19.7f), 
        OVERRIDE(500.0f);
        
        private final float score;
        
        SCORE(final float score) {
            this.score = score;
        }
        
        public float getValue() {
            return this.score;
        }
        
    }
 
    public static enum COLOR {
        
        WHITE("white", GraphicsVars.getInstance().WHITE_PIECE, false, 0), 
        BLACK("black", GraphicsVars.getInstance().BLACK_PIECE, true, 1),
        VIRTUAL_WHITE("virtual white", new Color(1f,1f,1f,.8f), false, 0), 
        VIRTUAL_BLACK("virtual black", new Color(0f,0f,0f,.8f), true, 1);
        
        public final String str;
        public final Boolean bool;
        public final int bit;
        public final Color color;
        
        COLOR(final String str, final Color color, final Boolean bool, final int bit) {
            this.str = str;
            this.bool = bool;
            this.bit = bit;
            this.color = color;
        }
        
        public static COLOR getOposite(final boolean bool) {
            return bool ? COLOR.WHITE : COLOR.BLACK;
        }
        
        public static COLOR getVirtualOposite(final boolean bool) {
            return bool ? COLOR.VIRTUAL_WHITE : COLOR.VIRTUAL_BLACK;
        }
        
        public static COLOR getVirtual(final boolean bool) {
            return bool ? COLOR.VIRTUAL_BLACK : COLOR.VIRTUAL_WHITE;
        }
        
    }
    
}
