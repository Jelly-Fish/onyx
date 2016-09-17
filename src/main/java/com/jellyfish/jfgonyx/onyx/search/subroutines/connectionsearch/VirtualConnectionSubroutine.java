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

import com.jellyfish.jfgonyx.onyx.OnyxGame;
import com.jellyfish.jfgonyx.onyx.abstractions.AbstractSubroutine;
import com.jellyfish.jfgonyx.onyx.constants.OnyxConst;
import com.jellyfish.jfgonyx.onyx.entities.OnyxPos;
import com.jellyfish.jfgonyx.onyx.entities.OnyxTail;
import com.jellyfish.jfgonyx.onyx.entities.collections.OnyxPosCollection;
import com.jellyfish.jfgonyx.onyx.vars.GraphicsVars;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;

/**
 * @author thw
 */
public class VirtualConnectionSubroutine extends AbstractSubroutine {
    
    protected final AbstractSubroutine.SUBROUTINE_TYPE type;
    protected final OnyxPosCollection c;
    protected final OnyxGame game;
    protected final OnyxConst.COLOR color;
    protected final int opColorBit;
    protected final float max = GraphicsVars.getInstance().BOARD_SIDE_SQUARE_COUNT + 1f;
    private final Set<String> checkedKeys = new HashSet();
    private boolean startLowBorder = false;
    private boolean linked = false;
    private final List<OnyxTail> tails = new ArrayList<>();
    private OnyxTail tail;

    public VirtualConnectionSubroutine(final OnyxConst.COLOR color, 
            final OnyxGame game) {
        this.c = game.getPosCollection();
        this.color = color;
        this.opColorBit = OnyxConst.COLOR.getOposite(color.bool).bit;
        this.game = game;
        this.type = AbstractSubroutine.SUBROUTINE_TYPE.VCNX;
    }
    
    /**
     * @param sPoss start positions - low & high borders of color to search for.
     */
    public void buildTails(final List<OnyxPos> sPoss) {

        for (OnyxPos p : sPoss) {
            initTailSearch(p);
            buildTail(p, p.getKey(), true);
            buildTail(p, p.getKey(), false);
        }
        
        tail = trimTails();
    }
    
    private void initTailSearch(final OnyxPos p) {
        startLowBorder = p.getPiece().color.bool ? p.isLowXBorder() : p.isLowYBorder();
        linked = false;
        checkedKeys.clear();
        tail = new OnyxTail(p);
    }
    
    private void buildTail(final OnyxPos p, final String kEx, final boolean rFirst) {       
                
        if (linked) return;
        
        checkedKeys.add(p.getKey());
        
        final String[] cnxs = p.isDiamondCenter() ? p.connections : sortCnxPositions(p, rFirst);
        OnyxPos tmp = null;

        for (String cnx : cnxs) {

            if (linked) return;
            
            tmp = c.getPosition(cnx);
            if (tmp == null) continue;
            
            if (isTailEnd(tmp)) { 
                tail.append(tmp);
                tails.add(new VirtualConnectionTailTrimSubroutine(tail).trim());
                linked = true;
                return;
            }            
            
            if (!cnx.equals(kEx) && !tmp.isOccupied(opColorBit) && !tail.contains(tmp.getKey()) &&
                c.isValidVirtualMove(tmp, game.getDiamondCollection(), opColorBit) && !checkedKeys.contains(cnx)) {
                tail.append(tmp);
                buildTail(tmp, tmp.getKey(), rFirst);
            }
        }        
    }
    
    /**
     * @param p position to sort connections for.
     * @param rFirst if RIGHT/EAST first in connection sorting.
     * @return sorted connections of p for vcnx search.
     */
    private String[] sortCnxPositions(final OnyxPos p, final boolean rFirst) {
        
        final OnyxPos poss[] = new OnyxPos[3];
        final String res[] = new String[3];
        int index = -1;        
        OnyxPos tmp = null;

        for (String cnx : p.connections) {
            
            tmp = c.getPosition(cnx);
            if (tmp == null) continue;
            
            /**
             * Forward position. Only one forward position must be found depending
             * on color and tail start side (start @ low border TRUE/FALSE) :
             * poss[0] = that position.
             */
            if (((startLowBorder && (color.bool && (tmp.y == p.y && tmp.x > p.x))) ||
                (startLowBorder && (!color.bool && (tmp.x == p.x && tmp.y > p.y)))) ||
                ((!startLowBorder && (color.bool && (tmp.y == p.y && tmp.x < p.x))) ||
                (!startLowBorder && (!color.bool && (tmp.x == p.x && tmp.y < p.y))))) {
                poss[++index] = tmp;
            }
        }
        
        /**
         * Right & left moves. Only two moves can found as this method is only
         * used for non-diamond-center position - after iteration, found 
         * positions 1 & 2 go to poss[1 OR 2] indexes depending on color.
         */
        for (String cnx : p.connections) {
            tmp = c.getPosition(cnx);
            if (tmp == null) continue;
            if ((color.bool && tmp.x == p.x) || (!color.bool && tmp.y == p.y)) {
                poss[++index] = tmp;
            }
        }                
        
        if (color.bool && rFirst && poss[2] != null && poss[1] != null && poss[2].y > poss[1].y) {
            tmp = poss[2]; 
            poss[2] = poss[1];
            poss[1] = tmp;
        } else if (color.bool && !rFirst && poss[2] != null && poss[1] != null && poss[2].y < poss[1].y) {
            tmp = poss[1]; 
            poss[1] = poss[2];
            poss[2] = tmp;  
        }
           
        if (!color.bool && rFirst && poss[2] != null && poss[1] != null && poss[2].x > poss[1].x) {
            tmp = poss[2]; 
            poss[2] = poss[1];
            poss[1] = tmp;
        } else if (!color.bool && !rFirst && poss[2] != null && poss[1] != null && poss[2].x < poss[1].x) {
            tmp = poss[1]; 
            poss[1] = poss[2];
            poss[2] = tmp;  
        }
        
        for (int i = 0; i < poss.length; ++i) res[i] = poss[i] != null ? poss[i].getKey() : StringUtils.EMPTY;

        return res;
    }
    
    private OnyxTail trimTails() {
        
        OnyxTail tmp = null;
        for (OnyxTail t : tails) {
            if (t.lenght() < GraphicsVars.getInstance().BOARD_SIDE_SQUARE_COUNT) continue;
            if (tmp == null || t.lenght() < tmp.lenght()) tmp = t;
        }
        
        return tmp;
    }
    
    private boolean isTailEnd(final OnyxPos p) {
        return (startLowBorder && color.bool && p.x > max - .1f) ||
            (startLowBorder && !color.bool && p.y > max - .1f) ||
            (!startLowBorder && color.bool && p.x < 1.1f) ||
            (!startLowBorder && !color.bool && p.y < 1.1f);
    }
    
    public OnyxTail getTail() {
        return tail;
    }    
    
    public List<OnyxTail> getTails() {
        return tails;
    }
    
}
