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
package com.jellyfish.jfgonyx.entities;

import com.jellyfish.jfgonyx.constants.ConstructPosConst;
import com.jellyfish.jfgonyx.onyx.exceptions.InvalidOnyxPositionException;
import com.jellyfish.jfgonyx.ui.OnyxBoard;
import java.awt.Polygon;
import java.util.Arrays;

/**
 * Onyx square entity for engine & graphcal purposes, infact each OnyxPos
 * has a reference to it's OnyxDiamond instance.
 * @see OnyxPos Onyx position definition.
 * @author thw
 */
public class OnyxDiamond {

    public final OnyxPos[] positions;
    public final boolean onPairLine;
    private Polygon polygon;
    
    public OnyxDiamond(final int x, final int y, final boolean full) {
        
        this.onPairLine = y % 2 == 0;
        
        if (full) {
            this.positions = new OnyxPos[5];
            positions[4] = new OnyxPos(ConstructPosConst.CHARPOS.getPos(x).getFloatValue() + 
                    (ConstructPosConst.CHARPOS.getPos(0).getFloatValue() / 2),
                    ConstructPosConst.INTPOS.getPos(y).getFloatValue() + 
                    (ConstructPosConst.INTPOS.getPos(0).getFloatValue() / 2), this);
        } else {
            this.positions = new OnyxPos[4];
        }
        
        positions[0] = new OnyxPos(ConstructPosConst.CHARPOS.getPos(x).getFloatValue(),
                    ConstructPosConst.INTPOS.getPos(y).getFloatValue(), this);
            positions[1] = new OnyxPos(ConstructPosConst.CHARPOS.getPos(x).getFloatValue(),
                    ConstructPosConst.INTPOS.getPos(y + 1).getFloatValue(), this);
            positions[2] = new OnyxPos(ConstructPosConst.CHARPOS.getPos(x + 1).getFloatValue(),
                    ConstructPosConst.INTPOS.getPos(y + 1).getFloatValue(), this);
            positions[3] = new OnyxPos(ConstructPosConst.CHARPOS.getPos(x + 1).getFloatValue(),
                    ConstructPosConst.INTPOS.getPos(y).getFloatValue(), this);
        
    }
    
    public boolean isFivePosDiamond() {
        return this.positions.length == 5;
    }
    
    public boolean isCenterPosUsable(final OnyxPos p, final OnyxVirtualPiece piece, final OnyxBoard b) {
         
        int count = 0;
        for (OnyxDiamond d : b.getDiamondCollection().diamonds.values()) {
            count = 0;
            if (d.contains(p)) {
                for (String k : d.getAllKeys()) {
                    if (b.getPosCollection().getPosition(k).isOccupied() && 
                            b.getPosCollection().getPosition(k).getPiece().color.bitColor == 
                            b.getPosCollection().getPosition(piece.getTmpOnyxPosition().getKey()
                            ).getPiece().color.bitColor)
                    ++count;
                }
                if (count == 4) return true;
            }
        }
        
        return false;
    }
    
    public OnyxPos getCenterPos() throws InvalidOnyxPositionException {
        
        if (this.isFivePosDiamond()) {
            return this.positions[4];
        }
        throw new InvalidOnyxPositionException();
    }
    
    /**
     * @param p OnyxPosition instance.
     * @param c bit color (white=0, black=1).
     * @return true if Onyx position p in parameter 1 is a take position.
     */
    public boolean isTakePosition(final OnyxPos p, final int c) {              
        return this.checkTakePosition(p, c, 0, 2, 1, 3) || this.checkTakePosition(p, c, 1, 3, 0, 2);
    }
    
    /**
     * The rule for capturing allows a player to capture two enemy pieces in a single turn. 
     * All of the following conditions must be met:
     * 1 - the two enemy pieces occupy opposite corners of a square;
     * 2 - a third corner of the square is already occupied by a piece belonging to the capturing player; 
     * 3 - the midpoint of the square is unoccupied.
     * 
     * @param p OnyxPos instance.
     * @param c this color to check the take possibility.
     * @param i opponent side index position.
     * @param j the opposite to i index position.
     * @param k the possible index position for a take.
     * @param l the opposite to k index position for a take.
     * @return true if OnyxPos p in parameter 1 is a take position.
     */
    private boolean checkTakePosition(final OnyxPos p, final int c, 
            final int i, final int j, final int k, final int l) {
        
        if (this.isFivePosDiamond() && this.positions[4].isOccupied()) {
            return false;
        }
        
        return this.positions[i].isOccupied() && this.positions[i].getPiece().color.bitColor != c &&
                !this.positions[i].equals(p) && this.positions[j].isOccupied() && 
                this.positions[j].getPiece().color.bitColor != c && !this.positions[j].equals(p) &&
                ((!this.positions[k].isOccupied() && this.positions[l].isOccupied() && 
                this.positions[l].getPiece().color.bitColor == c) || 
                (!this.positions[l].isOccupied() && this.positions[k].isOccupied() && 
                this.positions[k].getPiece().color.bitColor == c));
    }

    public boolean contains(final OnyxPos p) {
        
        for (OnyxPos pos : this.positions) {
            if (pos.equals(p)) {
                return true;
            }
        }
        
        return false;
    }
    
    public String[] getCornerKeys() {
        return new String[] { 
            this.positions[0].getKey(), this.positions[1].getKey(),
            this.positions[2].getKey(), this.positions[3].getKey()
        };
    }
    
    public String[] getAllKeys() {
        if (!this.isFivePosDiamond()) return this.getCornerKeys();
        else return new String[] { 
            this.positions[0].getKey(), this.positions[1].getKey(),
            this.positions[2].getKey(), this.positions[3].getKey(),
            this.positions[4].getKey()
        };
    }
    
    @Override
    public String toString() {
        return String.format("1=%s 2=%s 3=%s 4=%s c=%s", positions[0].toString(),
                positions[1].toString(), positions[2].toString(),
                positions[3].toString(), (positions.length == 5 ? positions[4].toString() : "null"));
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 11 * hash + Arrays.deepHashCode(this.positions);
        hash = 11 * hash + (this.onPairLine ? 1 : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final OnyxDiamond other = (OnyxDiamond) obj;
        if (!Arrays.deepEquals(this.positions, other.positions)) {
            return false;
        }
        return this.onPairLine == other.onPairLine;
    }
     
    public Polygon getPolygon() {
        return polygon;
    }

    public void setPolygon(Polygon polygon) {
        this.polygon = polygon;
    }

}
