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

import com.jellyfish.jfgonyx.constants.OnyxConst;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

/**
 * @author thw
 */
public class OnyxMove {
    
    private final OnyxPos pos;
    private final OnyxPiece piece;
    private List<OnyxPos> captured;
    private final boolean win;

    private float score;

    public OnyxMove(final OnyxPos pos, final OnyxPiece piece, final List<OnyxPos> captured, 
            final boolean win, final float score) {
        this.pos = pos;
        this.piece = piece;
        this.win = win;
        this.captured = captured;
        this.score = score;
    }
    
    public OnyxMove(final boolean win) {
        this(null, null, null, win, win ? OnyxConst.SCORE.WIN.getValue() : 0f);
    }
    
    public OnyxMove(final OnyxPos pos) {
        this(pos, null, null, false, 0);
    }
    
    public OnyxMove(final OnyxPos pos, final OnyxPiece piece) {
        this(pos, piece, null, false, 0);
    }
    
    public OnyxMove(final OnyxPos pos, final float score) {
        this(pos, null, null, false, score);
    }
    
    public OnyxMove(final OnyxPos pos, final List<OnyxPos> captured, final float score) {
        this(pos, null, captured, false, score);
    }
    
    public OnyxMove(final OnyxPos pos, final OnyxPiece piece, final float score) {
        this(pos, piece, null, false, score);
    }
    
    public OnyxMove(final OnyxPos pos, final OnyxPiece piece, final List<OnyxPos> captured, final float score) {
        this(pos, piece, captured, false, score);
    }
    
    public boolean isCapture() {
        return this.captured != null && this.captured.size() > 0;
    }
    
    public boolean hasPosition() {
        return !(this.pos == null);
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(OnyxConst.POS_MAP.get(this.pos.getKey()));
        if (this.isCapture()) {
            sb.append(this.captured.size() == 2 ? "*" : this.captured.size() == 4 ? "**" : StringUtils.EMPTY);
        }
        return sb.toString();
    }
    
    public List<OnyxPos> getCaptured() {
        return captured;
    }
    
    public void setCaptured(List<OnyxPos> captured) {
        this.captured = captured;
    }

    public OnyxPos getPos() {
        return pos;
    }

    public OnyxPiece getPiece() {
        return piece;
    }
    
    public boolean isWin() {
        return win;
    }
    
    public float getScore() {
        return score;
    }
    
    public void setScore(final float s) {
        this.score = s;
    }
        
}
