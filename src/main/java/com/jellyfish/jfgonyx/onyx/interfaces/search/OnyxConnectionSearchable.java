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
package com.jellyfish.jfgonyx.onyx.interfaces.search;

import com.jellyfish.jfgonyx.onyx.OnyxGameImpl;
import com.jellyfish.jfgonyx.onyx.constants.OnyxConst;
import com.jellyfish.jfgonyx.onyx.entities.OnyxDiamond;
import com.jellyfish.jfgonyx.onyx.entities.OnyxMove;
import com.jellyfish.jfgonyx.onyx.entities.OnyxPos;
import com.jellyfish.jfgonyx.onyx.entities.collections.OnyxPosCollection;
import com.jellyfish.jfgonyx.onyx.exceptions.InvalidOnyxPositionException;
import com.jellyfish.jfgonyx.onyx.exceptions.NoValidOnyxPositionsFoundException;

/**
 *
 * @author thw
 */
public interface OnyxConnectionSearchable extends OnyxAbstractSearchable {
    
    /**
     * @param color the color for move, all equal colors will be discarded.
     * @param game
     * @throws com.jellyfish.jfgonyx.onyx.exceptions.NoValidOnyxPositionsFoundException
     * @throws com.jellyfish.jfgonyx.onyx.exceptions.InvalidOnyxPositionException
     * @see OnyxPos position definition.
     * @see OnyxDiamond Onyx diamond definition.
     * @return Best OnyxMove instance.
     * @see OnyxPosCollection OnyxPos instaces mapped to string key coordinates.
     */
    @Override
    OnyxMove search(final OnyxGameImpl game, final OnyxConst.COLOR color) 
            throws NoValidOnyxPositionsFoundException, InvalidOnyxPositionException;
    
    /**
     * Iterate through connections depending on color, if a border to border
     * connection is found return true, else return false.
     * <p>
     * Black borders are on x1.0 & x12.0 (Y from 1 to 12)
     * White borders are on y1.0 & y12.0 (x from 1 to 10)
     * <p>
     * Get all occupied positions by color (x 1.0 > 12.00 for black y for whites)
     * recursively go through all position P for opposite border position
     * IF no break THEN win true
     * ELSE false.
     * @see ConnectionWinSearch
     * @param c position collection containing position's connections key positions.
     * @param color the color to search win for.
     * @return true if win.
     * @throws NoValidOnyxPositionsFoundException is it get's messy...
     */
    @Override
    boolean isWin(final OnyxPosCollection c, final OnyxConst.COLOR color) 
            throws NoValidOnyxPositionsFoundException;
    
}
