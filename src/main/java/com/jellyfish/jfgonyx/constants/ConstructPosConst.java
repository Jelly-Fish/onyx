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
package com.jellyfish.jfgonyx.constants;

/**
 * Position constants for build and then drawing purposes only.
 * @author thw
 */
public class ConstructPosConst {
    
    public static enum INTPOS {
        
        ONE(1f, 0), TWO(2f, 1), THREE(3f, 2), FOUR(4f, 3), 
        FIVE(5f, 4), SIX(6f, 5), SEVEN(7f, 6), EIGHT(8f, 7), 
        NINE(9f, 8), TEN(10f, 9), ELEVEN(11f, 10), TWELVE(12f, 11); // ...
        
        private final Float fValue;
        private final int index;
        
        INTPOS(final float fValue, final int index) {
            this.fValue = fValue;
            this.index = index;
        }
        
        public static INTPOS getPos(final int index) {
            for (INTPOS i : INTPOS.values()) if (i.getIndex() == index) return i;
            throw new IndexOutOfBoundsException();
        }
        
        public Float getFloatValue() {
            return this.fValue;
        }
        
        public int getIndex() {
            return this.index;
        }
        
        public Character getCharValue() {
            return String.valueOf(this.index).toCharArray()[0];
        }
            
    }
    
    public static enum CHARPOS {
        
        a('a', 1f, 0), b('b', 2f, 1), c('c', 3f, 2), d('d', 4f,  3), 
        e('e', 5f, 4), f('e', 6f, 5), g('g', 7f, 6),
        h('h', 8f, 7), i('i', 9f, 8), j('j', 10f, 9), 
        k('k', 11f, 10), l('l', 12f, 11); // ...
        
        private final char charValue;
        private final Float fValue;
        private final Integer index;
        
        CHARPOS(final char charValue, final float fValue, final int index) {
            this.charValue = charValue;
            this.index = index;
            this.fValue = fValue;
        }
        
        public static CHARPOS getPos(final int index) {
            for (CHARPOS c : CHARPOS.values()) if (c.getIndex() == index) return c;
            throw new IndexOutOfBoundsException();
        }
        
        public Float getFloatValue() {
            return this.fValue;
        }
        
        public Character getCharValue() {
            return this.charValue;
        }
        
        public int getIndex() {
            return this.index;
        }
        
    }
    
}
