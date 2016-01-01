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
package com.jellyfish.jfgonyx.engine;

import com.jellyfish.jfgonyx.constants.GraphicsConst;
import com.jellyfish.jfgonyx.engine.OnyxEngine.SEARCH_TYPE;
import com.jellyfish.jfgonyx.engine.interfaces.OnyxPosCollectionSearchable;
import com.jellyfish.jfgonyx.entities.OnyxDiamond;
import com.jellyfish.jfgonyx.entities.OnyxPos;
import com.jellyfish.jfgonyx.entities.OnyxPosCollection;
import com.jellyfish.jfgonyx.exceptions.NoValidOnysPositionsFound;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

/**
 * @author thw
 */
class OnyxPosCollectionSearch implements OnyxPosCollectionSearchable {
    
    /**
     * Look for all possible take moves
     * IF diamond has 2 corners set to != color
     *   Search for neighbour diamonds with similar configuration
     *   IF found then double take
     * ELSE Look for counter attacks
     *   Prevent full set diamonds where != color
     * ELSE play neighbour != color positions
     *   Find forward positions depending on color
     */
    @Override
    public String search(final OnyxPosCollection c, final GraphicsConst.COLOR color) throws NoValidOnysPositionsFound {
    
        String res = StringUtils.EMPTY;
        final String take = this.getTakePos(c, color.bitColor);
        final String counter = this.getCounterPos(c, color.boolColor);
        final String neighbour = this.getNeighbourPos(c, color.boolColor);
        return StringUtils.isBlank(take) ? 
                (StringUtils.isBlank(counter) ? 
                    (StringUtils.isBlank(neighbour) ? null : neighbour) : counter) : take;
    }
    
    /**
     * @param c Onyx position collection.
     * @param boolColor boolean color value, black OR white.
     * @return Strongest move found or NULL if no such position has been found.
     */
    private String getNeighbourPos(final OnyxPosCollection c, final boolean boolColor) throws NoValidOnysPositionsFound {
        return OnyxEngine.SEARCH.get(SEARCH_TYPE.RANDOM).search(c, GraphicsConst.COLOR.getOposite(boolColor));
    }
    
    /**
     * @param c Onyx position collection.
     * @param boolColor boolean color value, black OR white.
     * @return Strongest counter attack move found (to prevent sealing positions) 
     * or NULL if no such position has been found.
     */
    private String getCounterPos(final OnyxPosCollection c, final boolean boolColor) throws NoValidOnysPositionsFound {
        return OnyxEngine.SEARCH.get(SEARCH_TYPE.RANDOM).search(c, GraphicsConst.COLOR.getOposite(boolColor));
    }
    
    /**
     * @param c Onyx position collection.
     * @param bitColor bit color value, black OR white (white=0, black=1).
     * @return Strongest take move found or NULL if no such position has been found.
     */
    private String getTakePos(final OnyxPosCollection c, final int bitColor) {
        
        final List<OnyxPos> positions = new ArrayList<>();
        final List<OnyxDiamond> diamonds = new ArrayList<>();

        OnyxDiamond diamond = null;
        for (OnyxPos p : c.positions.values()) {
            if ((!p.diamond.equals(diamond) || diamond == null) && 
                    p.diamond.isTakePosition(p, bitColor)) {                
                positions.add(p);
                diamonds.add(p.diamond);
            } else {
                diamond = p.diamond;
            }
        }
        
        if (positions.size() == 1 && diamonds.size() <= 1) return positions.get(0).getKey();
        if (positions.size() < 1) return null;
        
        OnyxPos pos = null;
        int maxF = 1;
        for (OnyxPos p : positions) {
            
            int f = 0;
            for (OnyxDiamond d : diamonds) {
                if (d.contains(p)) ++f;
            }
            
            if (f >= maxF) {
                pos = p;
                maxF = f;
            }
        }
        
        if (pos != null) return pos.getKey();
        
        return null;
    }
    
}
