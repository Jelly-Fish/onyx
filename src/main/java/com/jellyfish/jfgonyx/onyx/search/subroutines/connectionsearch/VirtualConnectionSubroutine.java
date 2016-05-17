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

import com.jellyfish.jfgonyx.onyx.abstractions.AbstractSubroutine;
import com.jellyfish.jfgonyx.onyx.constants.OnyxConst;
import com.jellyfish.jfgonyx.onyx.entities.OnyxPos;
import com.jellyfish.jfgonyx.onyx.entities.OnyxTail;
import com.jellyfish.jfgonyx.onyx.entities.collections.OnyxPosCollection;
import com.jellyfish.jfgonyx.ui.OnyxBoard;
import com.jellyfish.jfgonyx.vars.GraphicsVars;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;

/**
 * @author thw
 * @deprecated 
 */
public class VirtualConnectionSubroutine extends AbstractSubroutine {
    
    protected final AbstractSubroutine.SUBROUTINE_TYPE type;
    protected final OnyxPosCollection c;
    protected final OnyxBoard board;
    protected final OnyxConst.COLOR color;
    protected final int opColorBit;
    protected final float max = GraphicsVars.getInstance().BOARD_SIDE_SQUARE_COUNT + 1f;
    private final Set<String> checkedKeys = new HashSet();
    private boolean startLowBorder = false;
    private boolean linked = false;
    private final List<OnyxTail> tails = new ArrayList<>();
    private OnyxTail tmpTail;
    
    public VirtualConnectionSubroutine(final OnyxPosCollection c, final OnyxConst.COLOR color, 
            final OnyxBoard board) {
        this.c = c;
        this.color = color;
        this.opColorBit = OnyxConst.COLOR.getOposite(color.bool).bit;
        this.board = board;
        this.type = AbstractSubroutine.SUBROUTINE_TYPE.VCNX;
    }
    
    /**
     * @param sPoss start positions - low & high borders of color to search for.
     */
    public void seekTail(final List<OnyxPos> sPoss) {
        
        for (OnyxPos p : sPoss) {
            this.startLowBorder = p.getPiece().color.bool ? p.isLowXBorder() : p.isLowYBorder();
            this.linked = false;
            this.tmpTail = new OnyxTail();
            this.buildTail(p, p.getKey());
        }
        
        /**
         * FIXME : finish coding & testing this subroutine.
         * Generates as many OnyxTails as there is position in sPoss. Must be 
         * trimed : shortest tail is selected.
         */        
        final OnyxTail t = this.trimTails();
        System.out.println("////////////////////////////////////////////////////"); 
        for (OnyxPos p : t.getPositions()) System.out.println(OnyxConst.POS_MAP.get(p.getKey()));
    }
    
    private void buildTail(final OnyxPos p, final String kEx) {       
        
        this.checkedKeys.add(p.getKey());
        OnyxPos tmp = null;
        
        /**
         * If p is diamond center ??? F***ing good question ! FIXME in such case.
         */
        
        for (String k : trimConnections(p, p.connections)) {
            
            if (this.linked) return;
            tmp = c.getPosition(k);
            if (tmp == null) continue;
            
            if (this.isTailEnd(tmp)) {                
                this.tails.add(this.tmpTail);
                this.linked = true;
                return;
            }            
            
            if (!k.equals(kEx) && !tmp.isOccupied(this.opColorBit) &&
                c.isValidMove(tmp, this.board) && !this.checkedKeys.contains(k)) {                 
                this.tmpTail.append(tmp);
                this.buildTail(tmp, tmp.getKey());
            }
        }
        
    }
    
    private String[] trimConnections(final OnyxPos p, final String[] cnxs) {
        
        int i = -1;
        final String[] r = new String[] { 
            StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY
        };
        OnyxPos tmp = null;
        
        if (this.linked) return r;
        
        for (String cnx : cnxs) {
            
            tmp = c.getPosition(cnx);
            if (tmp == null) continue;
            
            if ((this.startLowBorder && (this.color.bool && (tmp.y == p.y && tmp.x > p.x))) ||
                (this.startLowBorder && (!this.color.bool && (tmp.x == p.x && tmp.y > p.y)))) {
                r[++i] = cnx;
            }
            
            if ((!this.startLowBorder && (this.color.bool && (tmp.y == p.y && tmp.x < p.x))) ||
                (!this.startLowBorder && (!this.color.bool && (tmp.x == p.x && tmp.y < p.y)))) {
                r[++i] = cnx;
            }
        }
        
        for (String cnx : cnxs) {
            
            tmp = c.getPosition(cnx);
            if (tmp == null) continue;
            
            if ((this.color.bool && tmp.x == p.x) || (!this.color.bool && tmp.y == p.y)) {
                r[++i] = cnx;
            }
        }
        
        return r;
    }
    
    private OnyxTail trimTails() {
        
        OnyxTail tmp = null;
        for (OnyxTail t : this.tails) {
            if (tmp == null || t.getTailCount() < tmp.getTailCount()) tmp = t;
        }
        
        return tmp;
    }
    
    private boolean isTailEnd(final OnyxPos p) {
        return (this.startLowBorder && this.color.bool && p.x > this.max - .1f) ||
            (this.startLowBorder && !this.color.bool && p.y > this.max - .1f) ||
            (!this.startLowBorder && this.color.bool && p.x < 1.1f) ||
            (!this.startLowBorder && !this.color.bool && p.y < 1.1f);
    }
    
}
