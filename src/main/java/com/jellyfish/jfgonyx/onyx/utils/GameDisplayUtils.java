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
 * *****************************************************************************
 */
package com.jellyfish.jfgonyx.onyx.utils;

import com.jellyfish.jfgonyx.onyx.entities.OnyxMove;
import com.jellyfish.jfgonyx.onyx.entities.OnyxPos;
import com.jellyfish.jfgonyx.onyx.entities.collections.OnyxPosCollection;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

/**
 * @author thw
 */
public class GameDisplayUtils {
    
    private static final String BLACK = "X";
    private static final String WHITE = "O";
    private static final String VIRTUAL = "V";
    private static final String BACKSLASH_N = "\n";
    private static final String FILLER = "-";
    private static final String LINK_FILLER = "~";
    private static final String SPACE = " ";
    
    /**
     * Build game status/layout as text output.
     * @param moves
     * @param posCol
     * @param boardWidth borad width (starts at ZERO, therfor +1).
     * @return data for output to file or ui.
     */
    public static String buildGameToTextOutput(final Map<Integer, OnyxMove> moves, final OnyxPosCollection posCol,
        final float boardWidth) {
        
        int x = 0, y = 0, virtualBoardWidth = ((int) boardWidth) * 2;
        final StringBuilder sb = new StringBuilder();
        final String[][] mtx = new String[virtualBoardWidth + 2][virtualBoardWidth + 2]; 
        
        for (OnyxPos pos : posCol.getPositions().values()) {
            x = pos.x > 1f ? (int) (((pos.x) - 1f) * 2f) : 0;
            y = pos.y > 1f ? (int) (((pos.y) - 1f) * 2f) : 0;
            mtx[x][y] = FILLER;
        }
                
        for (OnyxMove move : moves.values()) {    
            if (!move.getPos().isOccupied()) continue;
            x = move.getPos().x > 1f ? (int) (((move.getPos().x) - 1f) * 2f) : 0;
            y = move.getPos().y > 1f ? (int) (((move.getPos().y) - 1f) * 2f) : 0;            
            mtx[x][y] = move.getPos().getPiece().isVirtual() ? VIRTUAL : 
                move.getPos().getPiece().color.bool ? BLACK : WHITE;    
        }

        for (int i = 0; i <= virtualBoardWidth; ++i) {
            for (int j = 0; j <= virtualBoardWidth; ++j) {
                sb.append(StringUtils.isNumeric(mtx[j][i]) ? mtx[j][i] + SPACE :
                mtx[j][i] == null ? SPACE + SPACE : SPACE + mtx[j][i]);
            }
            sb.append(BACKSLASH_N);
        }
        
        return sb.toString();
    }
    
}
