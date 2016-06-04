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
package com.jellyfish.jfgonyx.onyx.entities;

import com.jellyfish.jfgonyx.onyx.constants.OnyxConst;
import java.util.LinkedList;

/**
 * @author thw
 */
public class OnyxTail {
    
    private boolean connected = false;
    private final LinkedList<OnyxPos> positions = new LinkedList<>();
    
    public void append(final OnyxPos p) {
        positions.addLast(p);
    }
    
    public void appendTailEnd(final OnyxPos p) {
        positions.addLast(p);
    }
    
    public void remove(final String posK) {
        
        int i = -1;
        for (OnyxPos p : positions) {
            if (p.getKey().equals(posK)) {
                i = positions.indexOf(p);
            }
        }
        
        if (i > -1) positions.remove(i);
    }
    
    public void remove(final int i) {
        if (i > -1 && positions.size() < i) positions.remove(i);
    }
    
    public boolean isTailStart(final String k) {
        return positions.getFirst().getKey().equals(k);
    }
    
    public boolean isTailEnd(final String k) {
        return positions.getLast().getKey().equals(k);
    }
        
    public LinkedList<OnyxPos> getPositions() {
        return positions;
    }
    
    public boolean contains(final String k) {
        for (OnyxPos p : positions) {
            if (k.equals(p.getKey())) return true;
        }
        return false;
    }
    
    public int lenght() {
        return positions.size();
    }
    
    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("tail start to end: ");     
        for (OnyxPos p : positions) sb.append(OnyxConst.POS_MAP.get(p.getKey())).append(",");
        return sb.toString().substring(0, sb.length() - 1);
    }
    
}
