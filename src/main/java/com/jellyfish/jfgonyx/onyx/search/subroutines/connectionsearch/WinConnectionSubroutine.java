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
package com.jellyfish.jfgonyx.onyx.search.subroutines.connectionsearch;

import com.jellyfish.jfgonyx.constants.OnyxConst;
import com.jellyfish.jfgonyx.onyx.entities.OnyxPos;
import com.jellyfish.jfgonyx.onyx.entities.collections.OnyxPosCollection;
import com.jellyfish.jfgonyx.onyx.search.subroutines.abstractions.AbstractSubroutine;
import java.util.HashSet;
import java.util.Set;

/**
 * @author thw
 */
public class WinConnectionSubroutine extends AbstractSubroutine {
    
    private final static String WIN = "%s wins the game !";
    protected final OnyxPosCollection c;
    protected final OnyxConst.COLOR color;
    protected final boolean display;
    protected final float max = OnyxConst.BOARD_SIDE_SQUARE_COUNT + 1f;
    protected final Set<String> checked = new HashSet<>();
    protected boolean win = false;
    protected int iteration = -1;
    
    public WinConnectionSubroutine(final OnyxPosCollection c, final OnyxConst.COLOR color, 
            final boolean display) {
        this.c = c;
        this.color = color;
        this.display = display;
    }
    
    public WinConnectionSubroutine(final OnyxPosCollection c, final OnyxConst.COLOR color) {
        this.c = c;
        this.color = color;
        this.display = false;
    }
    
    public void connection(final OnyxPos p, final String kEx) {       
        
        if (this.win) return;
        
        OnyxPos tmp = null;
        for (String k : p.connections) {
            tmp = c.getPosition(k);
            if (this.persue(tmp, kEx)) {
                if ((this.color.bool && tmp.x > this.max - .1f) ||
                    (!this.color.bool && tmp.y > this.max - .1f)) {
                    this.win = true;
                }
                this.checked.add(kEx);
                this.connection(tmp, k); 
            }
        }
    }
    
    boolean persue(final OnyxPos p, final String kEx) {
        return p != null && !this.checked.contains(p.getKey()) && !p.getKey().equals(kEx) &&
                p.isOccupied() && p.getPiece().color.bit == this.color.bit;
    }
    
    @SuppressWarnings("empty-statement")
    private int keyArraySize(final String[] keys) {
        int i = -1;
        while (keys[++i] != null);
        return i;
    }
   
    public boolean isWin() {
        if (this.win && this.display) print(WIN, this.color.str.toUpperCase());
        return this.win;
    }
    
}
