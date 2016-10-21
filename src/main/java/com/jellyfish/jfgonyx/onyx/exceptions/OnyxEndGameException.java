/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jellyfish.jfgonyx.onyx.exceptions;

import com.jellyfish.jfgonyx.onyx.constants.OnyxConst;

/**
 *
 * @author thw
 */
public class OnyxEndGameException extends OnyxException {

    public static final String DEFAULT_MSG = "Game is finished, %s wins the game !";

    public OnyxEndGameException(final OnyxConst.COLOR color) {
        super(String.format(OnyxEndGameException.DEFAULT_MSG, color.str));
    }
    
}
