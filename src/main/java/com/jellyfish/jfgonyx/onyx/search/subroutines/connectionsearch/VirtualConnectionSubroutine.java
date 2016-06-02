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
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;

/**
 * @author thw
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
    private OnyxTail tail;

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
    public void buildTails(final List<OnyxPos> sPoss) {
        
        for (OnyxPos p : sPoss) {
            this.startLowBorder = p.getPiece().color.bool ? p.isLowXBorder() : p.isLowYBorder();
            this.linked = false;
            this.tail = new OnyxTail();
            this.buildTail(p, p.getKey());
        }
    
        /** FIXME : bebug purpose, print all tails...
        this.printAllTails();
        ** END DEBUG */
        
        this.tail = this.trimTails();
        
        if (this.tail != null) print(AbstractSubroutine.VTAIL_CANDIDATE_RES, 
            this.type, this.color, this.tail.lenght(), this.tail.toString());
    }
    
    private void buildTail(final OnyxPos p, final String kEx) {       
        
        this.checkedKeys.add(p.getKey());
        final String[] cnxs = p.isDiamondCenter() ? p.connections : sortCnxPositions1(p, p.connections);
        OnyxPos tmp = null;

        for (String cnx : cnxs) {
            
            if (this.linked) return;
            tmp = c.getPosition(cnx);
            if (tmp == null) continue;
            
            if (this.isTailEnd(tmp)) { 
                this.tail.append(tmp);
                this.tails.add(new VirtualConnectionTailTrimSubroutine(this.tail).trim());
                this.linked = true;
                return;
            }            
            
            if (!cnx.equals(kEx) && !tmp.isOccupied(this.opColorBit) &&
                !this.tail.contains(tmp.getKey()) &&
                c.isValidVirtualMove(tmp, this.board, this.opColorBit) && 
                !this.checkedKeys.contains(cnx)) {
                this.tail.append(tmp);
                this.buildTail(tmp, tmp.getKey());
            }
        }        
    }
    
    private String[] sortCnxPositions1(final OnyxPos p, final String[] cnxs) {
        
        final OnyxPos poss[] = new OnyxPos[3]; 
        final String[] r = new String[3];
        int i = -1;        
        OnyxPos tmp = null;
        
        if (this.linked) return r;
        
        for (String cnx : cnxs) {
            
            tmp = c.getPosition(cnx);
            if (tmp == null) continue;
            
            if (((this.startLowBorder && (this.color.bool && (tmp.y == p.y && tmp.x > p.x))) ||
                (this.startLowBorder && (!this.color.bool && (tmp.x == p.x && tmp.y > p.y)))) ||
                ((!this.startLowBorder && (this.color.bool && (tmp.y == p.y && tmp.x < p.x))) ||
                (!this.startLowBorder && (!this.color.bool && (tmp.x == p.x && tmp.y < p.y))))) {
                poss[++i] = tmp;
            }
        }
        
        for (String cnx : cnxs) {
            tmp = c.getPosition(cnx);
            if (tmp == null) continue;
            if ((this.color.bool && tmp.x == p.x) || (!this.color.bool && tmp.y == p.y)) poss[++i] = tmp;
        }        
        
        r[0] = poss[0] != null ? poss[0].getKey() : StringUtils.EMPTY;
        r[1] = this.color.bool && poss[2] != null ? poss[2].getKey() : 
            poss[1] != null ? poss[1].getKey() : StringUtils.EMPTY; 
        r[2] = this.color.bool && poss[1] != null ? poss[1].getKey() : 
            poss[2] != null ? poss[2].getKey() : StringUtils.EMPTY; 

        return r;
    }
    
    @Deprecated
    private String[] sortCnxPositions2(final OnyxPos p) {
        
        final String[] cnxs = p.connections;
        final OnyxPos poss[] = new OnyxPos[7]; 
        final LinkedList<String> r = new LinkedList<>();
        int i = -1;
        OnyxPos tmp = null;
        
        /**
         * If the tail is linked (reached oposite border depending on low
         * border start TRUE/FALSE) then return all recusive calls.
         */
        if (this.linked) return r.toArray(new String[r.size()]);
        
        /**
         * Forward position - only one forward position must be found depending
         * on color and tail start side (start @ low border TRUE/FALSE) :
         * poss[0] = that position.
         */
        for (String cnx : cnxs) {
            
            tmp = c.getPosition(cnx);
            if (tmp == null) continue;
                        
            if (((this.startLowBorder && (this.color.bool && (tmp.y == p.y && tmp.x > p.x))) ||
                (this.startLowBorder && (!this.color.bool && (tmp.x == p.x && tmp.y > p.y)))) ||
                ((!this.startLowBorder && (this.color.bool && (tmp.y == p.y && tmp.x < p.x))) ||
                (!this.startLowBorder && (!this.color.bool && (tmp.x == p.x && tmp.y < p.y))))) {
                poss[++i] = tmp;
            }
        }
        
        if (poss[0] != null) r.addLast(poss[0].getKey());
        
        /**
         * Right & left moves - only two moves can found as this method is only
         * used for non-diamond-center position - after iteration, found 
         * positions 1 & 2 go to poss[1 OR 2] indexes depending on color.
         */
        for (String cnx : cnxs) {            
            tmp = c.getPosition(cnx);
            if (tmp == null) continue;            
            if ((this.color.bool && tmp.x == p.x) || (!this.color.bool && tmp.y == p.y)) poss[++i] = tmp;
        }              
        
        if (this.color.bool && poss[2] != null) r.addLast(poss[2].getKey());
        else if (poss[1] != null) r.addLast(poss[1].getKey());
        
        if (this.color.bool && poss[1] != null) r.addLast(poss[1].getKey());
        else if (poss[2] != null) r.addLast(poss[2].getKey());
                
        /**
         * Forward up & down moves - max of two moves are possible - if target
         * position is a diamond center with occupied neighbour positions then
         * discard - after iteration, found positions 1 & 2 go to poss[3 & 4] 
         * indexes depending on color.
         */
        for (String cnx : cnxs) {
            
            tmp = c.getPosition(cnx);
            if (tmp == null || (tmp.isDiamondCenter() && tmp.hasNeighbour(this.c, this.color))) continue;  
            
            if (((this.startLowBorder && (this.color.bool && (tmp.y != p.y && tmp.x > p.x))) ||
                (this.startLowBorder && (!this.color.bool && (tmp.x != p.x && tmp.y > p.y)))) ||
                ((!this.startLowBorder && (this.color.bool && (tmp.y != p.y && tmp.x < p.x))) ||
                (!this.startLowBorder && (!this.color.bool && (tmp.x != p.x && tmp.y < p.y))))) {
                poss[++i] = tmp;
            }
        }
        
        if (this.color.bool && poss[4] != null) r.addLast(poss[4].getKey());
        else if (poss[3] != null) r.addLast(poss[3].getKey());
        
        if (this.color.bool && poss[3] != null) r.addLast(poss[3].getKey());
        else if (poss[4] != null) r.addLast(poss[4].getKey());

        /**
         * Return/backwards up & down moves - max of two moves are possible - if target
         * position is a diamond center with occupied neighbour positions then
         * discard - after iteration, found positions 1 & 2 go to poss[5 & 6] 
         * indexes depending on color.
         */
        for (String cnx : cnxs) {
            
            tmp = c.getPosition(cnx);
            if (tmp == null || (tmp.isDiamondCenter() && tmp.hasNeighbour(this.c, this.color))) continue;  
            
            if (((this.startLowBorder && (this.color.bool && (tmp.y != p.y && tmp.x < p.x))) ||
                (this.startLowBorder && (!this.color.bool && (tmp.x != p.x && tmp.y < p.y)))) ||
                ((!this.startLowBorder && (this.color.bool && (tmp.y != p.y && tmp.x > p.x))) ||
                (!this.startLowBorder && (!this.color.bool && (tmp.x != p.x && tmp.y > p.y))))) {
                poss[++i] = tmp;
            }
        }
        
        if (this.color.bool && poss[6] != null) r.addLast(poss[6].getKey());
        else if (poss[5] != null) r.addLast(poss[5].getKey());
        
        if (this.color.bool && poss[3] != null) r.addLast(poss[3].getKey());
        else if (poss[4] != null) r.addLast(poss[4].getKey());

        /**
         * Finally return linked list to array.
         */
        return r.toArray(new String[r.size()]);
    }
    
    private OnyxTail trimTails() {
        
        OnyxTail tmp = null;
        for (OnyxTail t : this.tails) {
            if (t.lenght() < GraphicsVars.getInstance().BOARD_SIDE_SQUARE_COUNT) continue;
            if (tmp == null || t.lenght() < tmp.lenght()) tmp = t;
        }
        
        return tmp;
    }
    
    private boolean isTailEnd(final OnyxPos p) {
        return (this.startLowBorder && this.color.bool && p.x > this.max - .1f) ||
            (this.startLowBorder && !this.color.bool && p.y > this.max - .1f) ||
            (!this.startLowBorder && this.color.bool && p.x < 1.1f) ||
            (!this.startLowBorder && !this.color.bool && p.y < 1.1f);
    }
    
    public OnyxTail getTail() {
        return tail;
    }

    private void printAllTails() {
        
        for (OnyxTail t : this.tails) {
            if (t != null && t.lenght() > 0) print(AbstractSubroutine.VTAIL_CANDIDATE_FORMAT, 
                this.type, this.color, this.tail.lenght(), this.tail.toString());
        }
    }
    
    
}
