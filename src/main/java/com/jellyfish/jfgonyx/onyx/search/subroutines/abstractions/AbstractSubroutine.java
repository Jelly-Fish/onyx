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
package com.jellyfish.jfgonyx.onyx.search.subroutines.abstractions;

import com.jellyfish.jfgonyx.constants.OnyxConst;
import com.jellyfish.jfgonyx.helpers.HTMLDisplayHelper;
import com.jellyfish.jfgonyx.onyx.entities.OnyxMove;
import com.jellyfish.jfgonyx.ui.MainFrame;
import java.util.List;
import java.util.Set;

/**
 *
 * @author thw
 */
public abstract class AbstractSubroutine {
    
    /**
     * Search resulat as Onyx move istance.
     * @see OnyxMove
     */
    protected OnyxMove move = null;
    
    public final void print(final String sK, final List<OnyxMove> candidates, final String f) {
        
        for (OnyxMove m : candidates) {
            if (!m.hasPosition() || m.getPos().getKey() == null) continue;
            MainFrame.print(String.format(f, OnyxConst.POS_MAP.get(sK), 
                    OnyxConst.POS_MAP.get(m.getPos().getKey()), m.getScore()), 
                    HTMLDisplayHelper.WHITE);
        }
    }
    
    public final void print(final String sK, final Set<OnyxMove> candidates, final String f) {
        
        for (OnyxMove m : candidates) {
            if (!m.hasPosition() || m.getPos().getKey() == null) continue;
            MainFrame.print(String.format(f, OnyxConst.POS_MAP.get(sK), 
                    OnyxConst.POS_MAP.get(m.getPos().getKey()), m.getScore()),
                    HTMLDisplayHelper.WHITE);
        }
    }
    
    public final void print(final String sK, final OnyxMove candidate, final String f) {
        
        if (!candidate.hasPosition() || candidate.getPos().getKey() == null) return;
        
        MainFrame.print(String.format(f, 
                OnyxConst.POS_MAP.get(sK), 
                OnyxConst.POS_MAP.get(candidate.getPos().getKey()), candidate.getScore()),
                HTMLDisplayHelper.GRAY);
    }
    
    public final void print(final String k, final String n, final String f) {
        
        if (k == null || n == null) return;
        
        MainFrame.print(String.format(f, OnyxConst.POS_MAP.get(k), n),
                HTMLDisplayHelper.WHITE);
    }
    
    public final void print(final String color, final String k, final String n, final String f) {
        
        if (color == null || k == null || n == null) return;
        
        MainFrame.print(String.format(f, color, OnyxConst.POS_MAP.get(k), n),
                HTMLDisplayHelper.GRAY);
    }
    
    public final void print(final String k, final String f) {
        
        if (k == null) return;
        
        MainFrame.print(String.format(f, k),
                HTMLDisplayHelper.GRAY);
    }
    
}
