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
package com.jellyfish.jfgonyx.constants;

import java.awt.Color;

/**
 *
 * @author thw
 */
public class GraphicsConst {
    
    public static final int SQUARE_WIDTH = 50;
    public static final int BOARD_WIDTH = 700;
    public static final int ZIGZAG = 12;
    public static final Color FULL_DIAMOND = new Color(192,192,192);
    public static final Color DIAMOND = new Color(232,232,232);
    public static final Color LINE = new Color(12,12,12);
    public static final Color WHITE_OUTLINE = new Color(16,16,16);
    public static final Color BLACK_OUTLINE = new Color(196,196,196);
    public static final Color VIRTUAL_OUTLINE = Color.CYAN;
    
    public static enum COLOR {
        
        WHITE("white", Color.WHITE, false, 0), BLACK("black", Color.BLACK, true, 1),
        VIRTUAL_WHITE("virtual white", new Color(1f,1f,1f,.8f), false, 0), 
        VIRTUAL_BLACK("virtual black", new Color(0f,0f,0f,.8f), true, 1);
        
        public final String strColor;
        public final Boolean boolColor;
        public final int bitColor;
        public final Color color;
        
        COLOR(final String strColor, final Color color, final Boolean bColor, final int bitColor) {
            this.strColor = strColor;
            this.boolColor = bColor;
            this.bitColor = bitColor;
            this.color = color;
        }
        
        public static COLOR getOposite(final boolean bool) {
            return bool ? COLOR.BLACK : COLOR.WHITE;
        }
        
    }
    
}
