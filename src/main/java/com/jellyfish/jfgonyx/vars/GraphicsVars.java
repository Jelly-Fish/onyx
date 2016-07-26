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
package com.jellyfish.jfgonyx.vars;

import java.awt.Color;

/**
 * @author thw
 */
public class GraphicsVars implements java.io.Serializable {
    
    private static GraphicsVars instance;
    private static final int DEFAULT_BOARD_WIDTH = 670;
    
    public int EXTRA_SQUARES = 2;
    public int BOARD_SIDE_SQUARE_COUNT = 13;
    public float BOARD_SIDE_POS_COUNT = 11.0f;
    public int SQUARE_WIDTH = 50;
    public int BOARD_WIDTH = 670;
    public int ZIGZAG = 12;
    public Color WHITE_PIECE = Color.WHITE;
    public Color BLACK_PIECE = Color.BLACK;
    public Color COMPONENTS_BACKGROUND_COLOR1 = new Color(172,172,162);
    public Color COMPONENTS_BACKGROUND_COLOR2 = new Color(124,124,124);
    public Color BACKGROUND = new Color(210,160,48);
    public Color FULL_DIAMOND = new Color(169,125,16);
    public Color DIAMOND = new Color(212,170,54);
    public Color LINE = new Color(12,12,12);
    public Color WHITE_OUTLINE = new Color(16,16,16);
    public Color BLACK_OUTLINE = new Color(64,64,64);
    public Color VIRTUAL_OUTLINE = Color.CYAN;
    public Color ONYX_ENGINE_MOVE_OUTLINE = Color.RED;
    public int TRANSLATION = 16;
    public int CENTER_TRANSLATION = 32;
    
    private GraphicsVars() { 
        BOARD_WIDTH += EXTRA_SQUARES * SQUARE_WIDTH;
        BOARD_SIDE_POS_COUNT = ((float) BOARD_SIDE_SQUARE_COUNT) + 1f;
    }

    public static GraphicsVars getInstance() {       
        if (instance == null) instance = new GraphicsVars();
        return instance;
    }
    
    public static void setInstance(final GraphicsVars gv) {
        instance = gv;
        instance.resetInstance();
    }
    
    public void resetInstance() {
        BOARD_WIDTH = DEFAULT_BOARD_WIDTH;
        BOARD_WIDTH += EXTRA_SQUARES * SQUARE_WIDTH;
        BOARD_SIDE_POS_COUNT = ((float) BOARD_SIDE_SQUARE_COUNT) + 1f;
    }
    
}
