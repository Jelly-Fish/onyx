/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jellyfish.jfgonyx.onyx.vars;

/**
 * @author thw
 */
public class OnyxGameVars implements java.io.Serializable {
    
    private static OnyxGameVars instance;
    public int ONYX_GAMESTART_MOVE_COUNT = 10;
            
    private OnyxGameVars() { }

    public static OnyxGameVars getInstance() {       
        if (instance == null) instance = new OnyxGameVars();
        return instance;
    }
    
    public static void setInstance(final OnyxGameVars gv) {
        instance = gv;
    }
}
