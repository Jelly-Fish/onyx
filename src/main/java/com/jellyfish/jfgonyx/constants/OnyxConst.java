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

import java.util.HashMap;

/**
 *
 * @author thw
 */
public class OnyxConst {
    
    public static final String KEY_SEPARATOR = "/";
    public static final int BOARD_SIDE_SQUARE_COUNT = 11;
    
    public static final HashMap<String, String> POS_MAP = new HashMap<>();
    static {
        
        OnyxConst.POS_MAP.put("1,0-1,0", "A-12");
        OnyxConst.POS_MAP.put("2,0-1,0", "B-12");
        OnyxConst.POS_MAP.put("3,0-1,0", "C-12");
        OnyxConst.POS_MAP.put("4,0-1,0", "D-12");
        OnyxConst.POS_MAP.put("5,0-1,0", "E-12");
        OnyxConst.POS_MAP.put("6,0-1,0", "F-12");
        OnyxConst.POS_MAP.put("7,0-1,0", "G-12");
        OnyxConst.POS_MAP.put("8,0-1,0", "H-12");
        OnyxConst.POS_MAP.put("9,0-1,0", "I-12");
        OnyxConst.POS_MAP.put("10,0-1,0", "J-12");
        OnyxConst.POS_MAP.put("11,0-1,0", "K-12");
        OnyxConst.POS_MAP.put("12,0-1,0", "L-12");

        OnyxConst.POS_MAP.put("1,0-2,0", "A-11");
        OnyxConst.POS_MAP.put("2,0-2,0", "B-11");
        OnyxConst.POS_MAP.put("3,0-2,0", "C-11");
        OnyxConst.POS_MAP.put("4,0-2,0", "D-11");
        OnyxConst.POS_MAP.put("5,0-2,0", "E-11");
        OnyxConst.POS_MAP.put("6,0-2,0", "F-11");
        OnyxConst.POS_MAP.put("7,0-2,0", "G-11");
        OnyxConst.POS_MAP.put("8,0-2,0", "H-11");
        OnyxConst.POS_MAP.put("9,0-2,0", "I-11");
        OnyxConst.POS_MAP.put("10,0-2,0", "J-11");
        OnyxConst.POS_MAP.put("11,0-2,0", "K-11");
        OnyxConst.POS_MAP.put("12,0-2,0", "L-11");

        OnyxConst.POS_MAP.put("1,0-3,0", "A-10");
        OnyxConst.POS_MAP.put("2,0-3,0", "B-10");
        OnyxConst.POS_MAP.put("3,0-3,0", "C-10");
        OnyxConst.POS_MAP.put("4,0-3,0", "D-10");
        OnyxConst.POS_MAP.put("5,0-3,0", "E-10");
        OnyxConst.POS_MAP.put("6,0-3,0", "F-10");
        OnyxConst.POS_MAP.put("7,0-3,0", "G-10");
        OnyxConst.POS_MAP.put("8,0-3,0", "H-10");
        OnyxConst.POS_MAP.put("9,0-3,0", "I-10");
        OnyxConst.POS_MAP.put("10,0-3,0", "J-10");
        OnyxConst.POS_MAP.put("11,0-3,0", "K-10");
        OnyxConst.POS_MAP.put("12,0-3,0", "L-10");

        OnyxConst.POS_MAP.put("1,0-4,0", "A-9");
        OnyxConst.POS_MAP.put("2,0-4,0", "B-9");
        OnyxConst.POS_MAP.put("3,0-4,0", "C-9");
        OnyxConst.POS_MAP.put("4,0-4,0", "D-9");
        OnyxConst.POS_MAP.put("5,0-4,0", "E-9");
        OnyxConst.POS_MAP.put("6,0-4,0", "F-9");
        OnyxConst.POS_MAP.put("7,0-4,0", "G-9");
        OnyxConst.POS_MAP.put("8,0-4,0", "H-9");
        OnyxConst.POS_MAP.put("9,0-4,0", "I-9");
        OnyxConst.POS_MAP.put("10,0-4,0", "J-9");
        OnyxConst.POS_MAP.put("11,0-4,0", "K-9");
        OnyxConst.POS_MAP.put("12,0-4,0", "L-9");

        OnyxConst.POS_MAP.put("1,0-5,0", "A-8");
        OnyxConst.POS_MAP.put("2,0-5,0", "B-8");
        OnyxConst.POS_MAP.put("3,0-5,0", "C-8");
        OnyxConst.POS_MAP.put("4,0-5,0", "D-8");
        OnyxConst.POS_MAP.put("5,0-5,0", "E-8");
        OnyxConst.POS_MAP.put("6,0-5,0", "F-8");
        OnyxConst.POS_MAP.put("7,0-5,0", "G-8");
        OnyxConst.POS_MAP.put("8,0-5,0", "H-8");
        OnyxConst.POS_MAP.put("9,0-5,0", "I-8");
        OnyxConst.POS_MAP.put("10,0-5,0", "J-8");
        OnyxConst.POS_MAP.put("11,0-5,0", "K-8");
        OnyxConst.POS_MAP.put("12,0-5,0", "L-8");

        OnyxConst.POS_MAP.put("1,0-6,0", "A-7");
        OnyxConst.POS_MAP.put("2,0-6,0", "B-7");
        OnyxConst.POS_MAP.put("3,0-6,0", "C-7");
        OnyxConst.POS_MAP.put("4,0-6,0", "D-7");
        OnyxConst.POS_MAP.put("5,0-6,0", "E-7");
        OnyxConst.POS_MAP.put("6,0-6,0", "F-7");
        OnyxConst.POS_MAP.put("7,0-6,0", "G-7");
        OnyxConst.POS_MAP.put("8,0-6,0", "H-7");
        OnyxConst.POS_MAP.put("9,0-6,0", "I-7");
        OnyxConst.POS_MAP.put("10,0-6,0", "J-7");
        OnyxConst.POS_MAP.put("11,0-6,0", "K-7");
        OnyxConst.POS_MAP.put("12,0-6,0", "L-7");

        OnyxConst.POS_MAP.put("1,0-7,0", "A-6");
        OnyxConst.POS_MAP.put("2,0-7,0", "B-6");
        OnyxConst.POS_MAP.put("3,0-7,0", "C-6");
        OnyxConst.POS_MAP.put("4,0-7,0", "D-6");
        OnyxConst.POS_MAP.put("5,0-7,0", "E-6");
        OnyxConst.POS_MAP.put("6,0-7,0", "F-6");
        OnyxConst.POS_MAP.put("7,0-7,0", "G-6");
        OnyxConst.POS_MAP.put("8,0-7,0", "H-6");
        OnyxConst.POS_MAP.put("9,0-7,0", "I-6");
        OnyxConst.POS_MAP.put("10,0-7,0", "J-6");
        OnyxConst.POS_MAP.put("11,0-7,0", "K-6");
        OnyxConst.POS_MAP.put("12,0-7,0", "L-6");

        OnyxConst.POS_MAP.put("1,0-8,0", "A-5");
        OnyxConst.POS_MAP.put("2,0-8,0", "B-5");
        OnyxConst.POS_MAP.put("3,0-8,0", "C-5");
        OnyxConst.POS_MAP.put("4,0-8,0", "D-5");
        OnyxConst.POS_MAP.put("5,0-8,0", "E-5");
        OnyxConst.POS_MAP.put("6,0-8,0", "F-5");
        OnyxConst.POS_MAP.put("7,0-8,0", "G-5");
        OnyxConst.POS_MAP.put("8,0-8,0", "H-5");
        OnyxConst.POS_MAP.put("9,0-8,0", "I-5");
        OnyxConst.POS_MAP.put("10,0-8,0", "J-5");
        OnyxConst.POS_MAP.put("11,0-8,0", "K-5");
        OnyxConst.POS_MAP.put("12,0-8,0", "L-5");

        OnyxConst.POS_MAP.put("1,0-9,0", "A-4");
        OnyxConst.POS_MAP.put("2,0-9,0", "B-4");
        OnyxConst.POS_MAP.put("3,0-9,0", "C-4");
        OnyxConst.POS_MAP.put("4,0-9,0", "D-4");
        OnyxConst.POS_MAP.put("5,0-9,0", "E-4");
        OnyxConst.POS_MAP.put("6,0-9,0", "F-4");
        OnyxConst.POS_MAP.put("7,0-9,0", "G-4");
        OnyxConst.POS_MAP.put("8,0-9,0", "H-4");
        OnyxConst.POS_MAP.put("9,0-9,0", "I-4");
        OnyxConst.POS_MAP.put("10,0-9,0", "J-4");
        OnyxConst.POS_MAP.put("11,0-9,0", "K-4");
        OnyxConst.POS_MAP.put("12,0-9,0", "L-4");

        OnyxConst.POS_MAP.put("1,0-10,0", "A-3");
        OnyxConst.POS_MAP.put("2,0-10,0", "B-3");
        OnyxConst.POS_MAP.put("3,0-10,0", "C-3");
        OnyxConst.POS_MAP.put("4,0-10,0", "D-3");
        OnyxConst.POS_MAP.put("5,0-10,0", "E-3");
        OnyxConst.POS_MAP.put("6,0-10,0", "F-3");
        OnyxConst.POS_MAP.put("7,0-10,0", "G-3");
        OnyxConst.POS_MAP.put("8,0-10,0", "H-3");
        OnyxConst.POS_MAP.put("9,0-10,0", "I-3");
        OnyxConst.POS_MAP.put("10,0-10,0", "J-3");
        OnyxConst.POS_MAP.put("11,0-10,0", "K-3");
        OnyxConst.POS_MAP.put("12,0-10,0", "L-3");

        OnyxConst.POS_MAP.put("1,0-11,0", "A-2");
        OnyxConst.POS_MAP.put("2,0-11,0", "B-2");
        OnyxConst.POS_MAP.put("3,0-11,0", "C-2");
        OnyxConst.POS_MAP.put("4,0-11,0", "D-2");
        OnyxConst.POS_MAP.put("5,0-11,0", "E-2");
        OnyxConst.POS_MAP.put("6,0-11,0", "F-2");
        OnyxConst.POS_MAP.put("7,0-11,0", "G-2");
        OnyxConst.POS_MAP.put("8,0-11,0", "H-2");
        OnyxConst.POS_MAP.put("9,0-11,0", "I-2");
        OnyxConst.POS_MAP.put("10,0-11,0", "J-2");
        OnyxConst.POS_MAP.put("11,0-11,0", "K-2");
        OnyxConst.POS_MAP.put("12,0-11,0", "L-2");

        OnyxConst.POS_MAP.put("1,0-12,0", "A-1");
        OnyxConst.POS_MAP.put("2,0-12,0", "B-1");
        OnyxConst.POS_MAP.put("3,0-12,0", "C-1");
        OnyxConst.POS_MAP.put("4,0-12,0", "D-1");
        OnyxConst.POS_MAP.put("5,0-12,0", "E-1");
        OnyxConst.POS_MAP.put("6,0-12,0", "F-1");
        OnyxConst.POS_MAP.put("7,0-12,0", "G-1");
        OnyxConst.POS_MAP.put("8,0-12,0", "H-1");
        OnyxConst.POS_MAP.put("9,0-12,0", "I-1");
        OnyxConst.POS_MAP.put("10,0-12,0", "J-1");
        OnyxConst.POS_MAP.put("11,0-12,0", "K-1");
        OnyxConst.POS_MAP.put("12,0-12,0", "L-1");

        OnyxConst.POS_MAP.put("2,5-11,5", "B-C-1-2");
        OnyxConst.POS_MAP.put("4,5-11,5", "D-E-1-2");
        OnyxConst.POS_MAP.put("6,5-11,5", "F-G-1-2");
        OnyxConst.POS_MAP.put("8,5-11,5", "H-I-1-2");
        OnyxConst.POS_MAP.put("10,5-11,5", "J-K-1-2");

        OnyxConst.POS_MAP.put("2,5-9,5", "B-C-3-4");
        OnyxConst.POS_MAP.put("4,5-9,5", "D-E-3-4");
        OnyxConst.POS_MAP.put("6,5-9,5", "F-G-3-4");
        OnyxConst.POS_MAP.put("8,5-9,5", "H-I-3-4");
        OnyxConst.POS_MAP.put("10,5-9,5", "J-K-3-4");

        OnyxConst.POS_MAP.put("2,5-7,5", "B-C-5-6");
        OnyxConst.POS_MAP.put("4,5-7,5", "D-E-5-6");
        OnyxConst.POS_MAP.put("6,5-7,5", "F-G-5-6");
        OnyxConst.POS_MAP.put("8,5-7,5", "H-I-5-6");
        OnyxConst.POS_MAP.put("10,5-7,5", "J-K-5-6");

        OnyxConst.POS_MAP.put("2,5-5,5", "B-C-7-8");
        OnyxConst.POS_MAP.put("4,5-5,5", "D-E-7-8");
        OnyxConst.POS_MAP.put("6,5-5,5", "F-G-7-8");
        OnyxConst.POS_MAP.put("8,5-5,5", "H-I-7-8");
        OnyxConst.POS_MAP.put("10,5-5,5", "J-K-7-8");

        OnyxConst.POS_MAP.put("2,5-3,5", "B-C-9-10");
        OnyxConst.POS_MAP.put("4,5-3,5", "D-E-9-10");
        OnyxConst.POS_MAP.put("6,5-3,5", "F-G-9-10");
        OnyxConst.POS_MAP.put("8,5-3,5", "H-I-9-10");
        OnyxConst.POS_MAP.put("10,5-3,5", "J-K-9-10");

        OnyxConst.POS_MAP.put("2,5-1,5", "B-C-11-12");
        OnyxConst.POS_MAP.put("4,5-1,5", "D-E-11-12");
        OnyxConst.POS_MAP.put("6,5-1,5", "F-G-11-12");
        OnyxConst.POS_MAP.put("8,5-1,5", "H-I-11-12");
        OnyxConst.POS_MAP.put("10,5-1,5", "J-K-11-12");

        OnyxConst.POS_MAP.put("1,5-10,5", "A-B-2-3");
        OnyxConst.POS_MAP.put("3,5-10,5", "C-D-2-3");
        OnyxConst.POS_MAP.put("5,5-10,5", "E-F-2-3");
        OnyxConst.POS_MAP.put("7,5-10,5", "G-H-2-3");
        OnyxConst.POS_MAP.put("9,5-10,5", "I-J-2-3");
        OnyxConst.POS_MAP.put("11,5-10,5", "K-L-2-3");

        OnyxConst.POS_MAP.put("1,5-8,5", "A-B-4-5");
        OnyxConst.POS_MAP.put("3,5-8,5", "C-D-4-5");
        OnyxConst.POS_MAP.put("5,5-8,5", "E-F-4-5");
        OnyxConst.POS_MAP.put("7,5-8,5", "G-H-4-5");
        OnyxConst.POS_MAP.put("9,5-8,5", "I-J-4-5");
        OnyxConst.POS_MAP.put("11,5-8,5", "K-L-4-5");

        OnyxConst.POS_MAP.put("1,5-6,5", "A-B-6-7");
        OnyxConst.POS_MAP.put("3,5-6,5", "C-D-6-7");
        OnyxConst.POS_MAP.put("5,5-6,5", "E-F-6-7");
        OnyxConst.POS_MAP.put("7,5-6,5", "G-H-6-7");
        OnyxConst.POS_MAP.put("9,5-6,5", "I-J-6-7");
        OnyxConst.POS_MAP.put("11,5-6,5", "K-L-6-7");

        OnyxConst.POS_MAP.put("1,5-4,5", "A-B-8-9");
        OnyxConst.POS_MAP.put("3,5-4,5", "C-D-8-9");
        OnyxConst.POS_MAP.put("5,5-4,5", "E-F-8-9");
        OnyxConst.POS_MAP.put("7,5-4,5", "G-H-8-9");
        OnyxConst.POS_MAP.put("9,5-4,5", "I-J-8-9");
        OnyxConst.POS_MAP.put("11,5-4,5", "K-L-8-9");

        OnyxConst.POS_MAP.put("1,5-2,5", "A-B-10-11");
        OnyxConst.POS_MAP.put("3,5-2,5", "C-D-10-11");
        OnyxConst.POS_MAP.put("5,5-2,5", "E-F-10-11");
        OnyxConst.POS_MAP.put("7,5-2,5", "G-H-10-11");
        OnyxConst.POS_MAP.put("9,5-2,5", "I-J-10-11");
        OnyxConst.POS_MAP.put("11,5-2,5", "K-L-10-11");
        
    }
    
    public static enum SCORE {
    
        WIN(100f), TAKE(23.5f), NEIGHBOUR(1f), TAIL(1.2f),
        RANDOM(0f), COUNTERPOS(33.6f), ATTACK(23.3f), CENTER(23.4f);
        
        private final float score;
        
        SCORE(final float score) {
            this.score = score;
        }
        
        public float getValue() {
            return this.score;
        }
        
    }
    
}
