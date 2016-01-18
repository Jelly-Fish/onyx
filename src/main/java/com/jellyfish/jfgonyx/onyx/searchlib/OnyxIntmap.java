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
package com.jellyfish.jfgonyx.onyx.searchlib;

import com.jellyfish.jfgonyx.constants.OnyxConst;
import com.jellyfish.jfgonyx.onyx.entities.OnyxPos;
import com.jellyfish.jfgonyx.onyx.entities.OnyxPosCollection;
import static com.jellyfish.jfgonyx.onyx.entities.OnyxPosCollection.KEY_FORMAT;

/**
 * @author thw
 */
public class OnyxIntmap {
    
    private final int[] intmap;

    public OnyxIntmap(final OnyxPosCollection c) {
        this.intmap = this.build(c);
    }
    
    private int[] build(final OnyxPosCollection c) {
        
        /**
         * FIXME : Exception in thread "AWT-EventQueue-0" java.lang.ArrayIndexOutOfBoundsException: 484
         * line 63
         */
        
        OnyxPos tmp = null;
        final int[] bmap = new int[22 * 22];
        int i = 0;
        for (float y = 1.0f; y <= (float) (OnyxConst.BOARD_SIDE_SQUARE_COUNT * 2); y += .5f) {
            for (float x = 1.0f; x <= (float) (OnyxConst.BOARD_SIDE_SQUARE_COUNT * 2); x += .5f) {
                tmp = c.getPosition(String.format(KEY_FORMAT, x, y));
                bmap[i] = tmp == null ? 3 : (tmp.isOccupied() ? tmp.getPiece().color.bitColor : 2);
                ++i;
            }
        }
        
        return bmap;
    }
    
    public void print() {
        for (int i = 0; i < this.intmap.length; ++i) {
            if (i % (OnyxConst.BOARD_SIDE_SQUARE_COUNT * 2) != 0) {
                System.out.print(this.intmap[i]);
            } else {
                System.out.print('\n');
            }
        }
    }
    
}
