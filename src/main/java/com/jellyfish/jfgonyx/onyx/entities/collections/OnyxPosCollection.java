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
package com.jellyfish.jfgonyx.onyx.entities.collections;

import com.jellyfish.jfgonyx.onyx.constants.OnyxConst;
import com.jellyfish.jfgonyx.onyx.entities.OnyxDiamond;
import com.jellyfish.jfgonyx.onyx.entities.OnyxPos;
import com.jellyfish.jfgonyx.onyx.exceptions.InvalidOnyxPositionException;
import com.jellyfish.jfgonyx.onyx.utils.OnyxConnectionHelper;
import com.jellyfish.jfgonyx.onyx.entities.OnyxVirtualPiece;
import com.jellyfish.jfgonyx.onyx.utils.OnyxUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

/**
 * @author thw
 */
public class OnyxPosCollection {
    
    /**
     * intmap mtx width & height int value = 24.
     */
    public static final int MTX_WH = 24;
    public static final String KEY_FORMAT = "%.1f-%.1f";
    private final HashMap<String, OnyxPos> positions = new HashMap<>();
    
    public OnyxPosCollection(final OnyxDiamondCollection diamonds) {
        this.init(diamonds);
    }
    
    private final void init(final OnyxDiamondCollection c) {
        this.initPositionCollection(c);
        OnyxConnectionHelper.buildPosConnections(this);
    }
    
    public void clearPieces() {
        for (OnyxPos p : this.positions.values()) {
            p.setPiece(null);
            if (p.isVirtuallyOccupied()) p.getVirtualPiece().setTmpOnyxPosition(null);
            p.setVirtualPiece(null);
        }
    }
    
    private void initPositionCollection(final OnyxDiamondCollection c) {
        
        String k = null;
        for (OnyxDiamond d : c.getDiamonds().values()) {
            for (OnyxPos p : d.positions) {
                if (!this.positions.containsValue(p)) {
                    k = String.format(OnyxPosCollection.KEY_FORMAT, p.x, p.y);
                    this.positions.put(k, p);
                }
            }
        }
    }
    
    public void spawnVirtualPiece(final OnyxConst.COLOR c) {
        this.positions.get(String.format(OnyxPosCollection.KEY_FORMAT, 7f, 6f)).setVirtualPiece(
            new OnyxVirtualPiece(c)
        );
    }
    
    public OnyxPos getPosition(final String k) {
        return this.positions.get(k);
    }
    
    public boolean containsPosition(final String k) {
        return this.getPosition(k) != null;
    }
    
    public boolean isValidMove(final OnyxPos pos, final OnyxDiamondCollection dc) {
        final boolean isCenter = OnyxUtils.isDiamondCenter(pos.getKey(), dc);
        return ((!pos.isOccupied() && !isCenter)) || 
            (isCenter && OnyxUtils.isCenterPosPlayable(pos.getKey(), this, dc));        
    }
    
    public boolean isValidVirtualMove(final OnyxPos pos, final OnyxDiamondCollection dc, final int opColorBit) {
        final boolean isCenter = OnyxUtils.isDiamondCenter(pos.getKey(), dc);
        return ((!pos.isOccupied(opColorBit) && !isCenter)) || 
            (isCenter && OnyxUtils.isCenterPosPlayable(pos.getKey(), this, dc));        
    }
    
    public HashMap<String, OnyxPos> getPositions() {
        return positions;
    }

    public OnyxVirtualPiece getVirtualPiece() {
        
        OnyxVirtualPiece vP = null;
        for (OnyxPos p : this.positions.values()) {
            if (p.isVirtuallyOccupied()) {
                vP = (OnyxVirtualPiece) p.getVirtualPiece();
                vP.setTmpOnyxPosition(p);
                return vP;
            }
        }
        
        return null;
    }
    
    public boolean hasVirtualPiece() {
        return this.getVirtualPiece() != null;
    }
    
    public List<OnyxPos> performTake(final String key, final int bitColor, final OnyxDiamondCollection dc, 
            final OnyxPosCollection pc) throws InvalidOnyxPositionException {
        
        if (StringUtils.isBlank(key)) throw new InvalidOnyxPositionException();

        List<OnyxPos> captured = new ArrayList<>();
        String[] keys = null;
        int i, j, lI;
        for (OnyxDiamond d : dc.getDiamondsByPosKey(key)) {
            
            if (d.isFivePosDiamond() && 
                pc.getPosition(d.getCenterPos().getKey()).isOccupied()) {
                continue;
            }
            
            lI = 0; i = 0; j = 0;
            keys = d.getCornerKeys();
            for (int index = 0; index < keys.length; ++index) {
                if (!key.equals(keys[index]) && pc.positions.containsKey(keys[index])) {

                    if (pc.getPosition(keys[index]).isOccupied() &&
                        pc.getPosition(keys[index]).getPiece().color.bit != bitColor) {
                        if (i == 1) {
                            if (lI == 0 && index == 2) ++i;
                            if (lI == 1 && index == 3) ++i;
                        } else if (i == 0) {
                            ++i;
                            lI = index;
                        }
                    } else if (pc.getPosition(keys[index]).isOccupied() &&
                        pc.getPosition(keys[index]).getPiece().color.bit == bitColor) {
                        ++j;
                    }
                }
            }
            
            if (i == 2 && j == 1) {
                for (String k : keys) {
                    if (pc.positions.containsKey(k) &&
                            !key.equals(k) && 
                            pc.positions.get(k).isOccupied() &&
                            pc.positions.get(k).getPiece().color.bit != bitColor) {
                        captured.add(pc.getPosition(k));
                    }
                }
            }
        }
        
        final List<OnyxPos> posSet = new ArrayList<>();
        for (OnyxPos p : captured) {
            posSet.add(p);
            pc.getPosition(p.getKey()).setPiece(null);
        }
        
        return posSet;
    }

    public List<OnyxPos> getTakePositions(final String key, final int bitColor, final OnyxDiamondCollection dc,
            final OnyxPosCollection pc) throws InvalidOnyxPositionException {

        final List<OnyxPos> posSet = new ArrayList<>();
        String[] keys = null;
        int i, j, lI;
        for (OnyxDiamond d : dc.getDiamondsByPosKey(key)) {
            
            posSet.clear();
            if (d.isFivePosDiamond() && 
                pc.getPosition(d.getCenterPos().getKey()).isOccupied()) {
                continue;
            }
            
            lI = 0; i = 0; j = 0;
            keys = d.getCornerKeys();
            for (int index = 0; index < keys.length; ++index) {
                if (!key.equals(keys[index]) && pc.positions.containsKey(keys[index])) {

                    if (pc.getPosition(keys[index]).isOccupied() &&
                        pc.getPosition(keys[index]).getPiece().color.bit != bitColor) {
                        if (i == 1) {
                            if ((lI == 0 && index == 2) || (lI == 1 && index == 3)) {
                                ++i;
                                posSet.add(pc.getPosition(keys[index]));
                            }
                        } else if (i == 0) {
                            ++i;
                            lI = index;
                            posSet.add(pc.getPosition(keys[index]));
                        }
                    } else if (pc.getPosition(keys[index]).isOccupied() &&
                        pc.getPosition(keys[index]).getPiece().color.bit == bitColor) {
                        ++j;
                    }
                }
            }
            
            if (i == 2 && j == 1) return posSet;
        }
        
        return null;
    }
    
    public void clearOutlines() {
        for (OnyxPos p : positions.values()) {
            if (p.isOccupied() && p.getPiece().isEngineMove()) p.getPiece().setEngineMove(false);
        }
    }
    
    public int getBlackPieceCount() {
        
        int n = 0;
        for (OnyxPos p : this.positions.values()) {
            n = p.isOccupied() && p.getPiece().color.bit == OnyxConst.COLOR.BLACK.bit ?
                    ++n : n;
        }
        
        return n;
    }
    
    public int getWhitePieceCount() {
        
        int n = 0;
        for (OnyxPos p : this.positions.values()) {
            n = p.isOccupied() && p.getPiece().color.bit == OnyxConst.COLOR.WHITE.bit ?
                    ++n : n;
        }
        
        return n;
    }
    
}