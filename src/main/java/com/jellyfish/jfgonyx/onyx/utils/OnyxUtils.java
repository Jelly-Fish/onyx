/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jellyfish.jfgonyx.onyx.utils;

import com.jellyfish.jfgonyx.onyx.entities.OnyxDiamond;
import com.jellyfish.jfgonyx.onyx.entities.collections.OnyxDiamondCollection;
import com.jellyfish.jfgonyx.onyx.entities.collections.OnyxPosCollection;
import com.jellyfish.jfgonyx.onyx.exceptions.InvalidOnyxPositionException;

/**
 *
 * @author thw
 */
public class OnyxUtils {
    
    public static boolean isCenterPosPlayable(final String k, final OnyxPosCollection pc, final OnyxDiamondCollection dc) {
        
        int counter = 0;
        if (k == null || !pc.containsPosition(k) || 
            !isDiamondCenter(k, dc)) {
            return false;
        }
        
        for (String cK : pc.getPosition(k).connections) {
            counter = pc.getPosition(cK).isOccupied() ? ++counter : counter;
        }
        
        return counter == 0;
    }

    public static boolean isDiamondCenter(final String k, final OnyxDiamondCollection dc) {
        
        for (OnyxDiamond d : dc.getDiamonds().values()) {
            try {
                if (d.getCenterPos().getKey().equals(k)) {
                    return true;
                }
            } catch (final InvalidOnyxPositionException Iopex) { }
        }
        return false;
    }
    
}
