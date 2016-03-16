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
package com.jellyfish.jfgonyx.onyx.search;

import com.jellyfish.jfgonyx.onyx.search.searchutils.Intmap;
import com.jellyfish.jfgonyx.constants.GraphicsConst;
import com.jellyfish.jfgonyx.onyx.entities.OnyxMove;
import com.jellyfish.jfgonyx.onyx.abstractions.AbstractOnyxSearch;
import com.jellyfish.jfgonyx.onyx.entities.collections.OnyxPosCollection;
import com.jellyfish.jfgonyx.onyx.exceptions.InvalidOnyxPositionException;
import com.jellyfish.jfgonyx.onyx.exceptions.NoValidOnyxPositionsFoundException;
import com.jellyfish.jfgonyx.onyx.interfaces.search.OnyxRandomSeachable;
import com.jellyfish.jfgonyx.ui.OnyxBoard;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author thw
 */
public class IntmapSearch extends AbstractOnyxSearch implements OnyxRandomSeachable {

    @Override
    public OnyxMove search(final OnyxPosCollection c, final OnyxBoard board, 
            final GraphicsConst.COLOR color) throws NoValidOnyxPositionsFoundException, InvalidOnyxPositionException {
        
        final boolean win = this.isWin(new Intmap(c), color);
        return null;
    }
    
    /*
     * Depending on color
     *   IF pos found on both sides THEN
     *     seak connection
     *     IF connection found return true
     *     ELSE return false
     *   ELSE return false
     */
    @Deprecated
    public boolean isWin(final Intmap imap, final GraphicsConst.COLOR c) {
        
        final int[][] mtx = imap.getMtx_intmap();
        final List<Point> bounds = this.getStartIndexes(mtx, c.bit);
        return false;
    }
    
    @Override
    public boolean isWin(final OnyxPosCollection c, final GraphicsConst.COLOR color) 
            throws NoValidOnyxPositionsFoundException {
        throw new UnsupportedOperationException();
    }
    
    private List<Point> getStartIndexes(final int[][] mtx, final int bitColor) {
        
        final List<Point> p = new ArrayList<>();
        
        for (int i = 0; i < mtx.length; i++) {
            if (mtx[i][0] == bitColor) p.add(new Point(i, 0));
        }
        
        for (int i = 0; i < mtx.length; i++) {
            if (mtx[i][mtx[i].length - 1] == bitColor) p.add(new Point(i, mtx[i].length - 1));
        }
        
        for (int i = 0; i < mtx.length; i++) {
            if (mtx[0][i] == bitColor) p.add(new Point(0, i));
        }
        
        for (int i = 0; i < mtx.length; i++) {
            if (mtx[mtx[i].length - 1][i] == bitColor) p.add(new Point(mtx[i].length - 1, i));
        }
        
        return p;
    }
    
}
