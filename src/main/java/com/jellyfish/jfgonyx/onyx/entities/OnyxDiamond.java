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
package com.jellyfish.jfgonyx.onyx.entities;

import com.jellyfish.jfgonyx.onyx.constants.ConstructPosConst;
import com.jellyfish.jfgonyx.onyx.entities.OnyxPos;
import com.jellyfish.jfgonyx.onyx.exceptions.InvalidOnyxPositionException;
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
    
    public OnyxDiamond(final int x, final int y, final boolean isFivePosDiamond) {
        
        this.onPairLine = y % 2 == 0;
        
        if (isFivePosDiamond) {
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
        return positions.length == 5;
    }
    
    public OnyxPos getCenterPos() throws InvalidOnyxPositionException {
        
        if (isFivePosDiamond()) {
            return positions[4];
        }
        throw new InvalidOnyxPositionException();
    }

    public boolean contains(final OnyxPos p) {
        
        for (OnyxPos pos : positions) {
            if (pos.equals(p)) {
                return true;
            }
        }
        
        return false;
    }
    
    public String[] getCornerKeys() {
        return new String[] { 
            positions[0].getKey(), positions[1].getKey(),
            positions[2].getKey(), positions[3].getKey()
        };
    }
    
    public String[] getAllKeys() {
        if (!isFivePosDiamond()) return getCornerKeys();
        else return new String[] { 
            positions[0].getKey(), positions[1].getKey(),
            positions[2].getKey(), positions[3].getKey(),
            positions[4].getKey()
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
        hash = 11 * hash + Arrays.deepHashCode(positions);
        hash = 11 * hash + (onPairLine ? 1 : 0);
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
        if (!Arrays.deepEquals(positions, other.positions)) {
            return false;
        }
        return onPairLine == other.onPairLine;
    }
     
    public Polygon getPolygon() {
        return polygon;
    }

    public void setPolygon(final Polygon polygon) {
        this.polygon = polygon;
    }

}
