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

import java.awt.Font;
import java.awt.Polygon;

/**
 * @author thw
 */
public class OnyxBoardPositionOutlineConst {
    
    public final static Polygon[] OUTLINE_POLYGONS = new Polygon[4];
    static {
        
        OnyxBoardPositionOutlineConst.OUTLINE_POLYGONS[0] =
        new Polygon(
            new int[] { 0, GraphicsConst.BOARD_WIDTH, 
                GraphicsConst.BOARD_WIDTH - 24, 24 },
            new int[] { 0, 0, 24, 24 }, 4
        );
        OnyxBoardPositionOutlineConst.OUTLINE_POLYGONS[1] =
        new Polygon(
            new int[] { GraphicsConst.BOARD_WIDTH - 24, 
                GraphicsConst.BOARD_WIDTH, GraphicsConst.BOARD_WIDTH,
                GraphicsConst.BOARD_WIDTH - 24 },
            new int[] { 24, 0, GraphicsConst.BOARD_WIDTH,
                GraphicsConst.BOARD_WIDTH - 24 }, 4
        );
        OnyxBoardPositionOutlineConst.OUTLINE_POLYGONS[2] =
        new Polygon(
            new int[] { 24, GraphicsConst.BOARD_WIDTH - 24, 
                GraphicsConst.BOARD_WIDTH, 0 },
            new int[] { GraphicsConst.BOARD_WIDTH - 24, 
                GraphicsConst.BOARD_WIDTH - 24, GraphicsConst.BOARD_WIDTH,
                GraphicsConst.BOARD_WIDTH }, 4
        );
        OnyxBoardPositionOutlineConst.OUTLINE_POLYGONS[3] =
        new Polygon(
            new int[] { 0, 24, 24, 0 },
            new int[] { 0, 24, GraphicsConst.BOARD_WIDTH - 24,
                GraphicsConst.BOARD_WIDTH }, 4
        );
        
    }
    public final static Font POS_FONT = new Font("arial", Font.BOLD, 14);
    public final static String[] CHAR_VALUES = new String[] 
        { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L" };

}
