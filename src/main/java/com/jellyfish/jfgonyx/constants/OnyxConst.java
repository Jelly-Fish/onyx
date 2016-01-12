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
    
    public static final int BOARD_SIDE_SQUARE_COUNT = 11;
    public static final HashMap<String, String> POS_MAP = new HashMap<>();
    static {
        
        OnyxConst.POS_MAP.put("1,0-1,0", "12-A");
        OnyxConst.POS_MAP.put("2,0-1,0", "12-B");
        OnyxConst.POS_MAP.put("3,0-1,0", "12-C");
        OnyxConst.POS_MAP.put("4,0-1,0", "12-D");
        OnyxConst.POS_MAP.put("5,0-1,0", "12-E");
        OnyxConst.POS_MAP.put("6,0-1,0", "12-F");
        OnyxConst.POS_MAP.put("7,0-1,0", "12-G");
        OnyxConst.POS_MAP.put("8,0-1,0", "12-H");
        OnyxConst.POS_MAP.put("9,0-1,0", "12-I");
        OnyxConst.POS_MAP.put("10,0-1,0", "12-J");
        OnyxConst.POS_MAP.put("11,0-1,0", "12-K");
        OnyxConst.POS_MAP.put("12,0-1,0", "12-L");

        OnyxConst.POS_MAP.put("1,0-2,0", "11-A");
        OnyxConst.POS_MAP.put("2,0-2,0", "11-B");
        OnyxConst.POS_MAP.put("3,0-2,0", "11-C");
        OnyxConst.POS_MAP.put("4,0-2,0", "11-D");
        OnyxConst.POS_MAP.put("5,0-2,0", "11-E");
        OnyxConst.POS_MAP.put("6,0-2,0", "11-F");
        OnyxConst.POS_MAP.put("7,0-2,0", "11-G");
        OnyxConst.POS_MAP.put("8,0-2,0", "11-H");
        OnyxConst.POS_MAP.put("9,0-2,0", "11-I");
        OnyxConst.POS_MAP.put("10,0-2,0", "11-J");
        OnyxConst.POS_MAP.put("11,0-2,0", "11-K");
        OnyxConst.POS_MAP.put("12,0-2,0", "11-L");

        OnyxConst.POS_MAP.put("1,0-3,0", "10-A");
        OnyxConst.POS_MAP.put("2,0-3,0", "10-B");
        OnyxConst.POS_MAP.put("3,0-3,0", "10-C");
        OnyxConst.POS_MAP.put("4,0-3,0", "10-D");
        OnyxConst.POS_MAP.put("5,0-3,0", "10-E");
        OnyxConst.POS_MAP.put("6,0-3,0", "10-F");
        OnyxConst.POS_MAP.put("7,0-3,0", "10-G");
        OnyxConst.POS_MAP.put("8,0-3,0", "10-H");
        OnyxConst.POS_MAP.put("9,0-3,0", "10-I");
        OnyxConst.POS_MAP.put("10,0-3,0", "10-J");
        OnyxConst.POS_MAP.put("11,0-3,0", "10-K");
        OnyxConst.POS_MAP.put("12,0-3,0", "10-L");

        OnyxConst.POS_MAP.put("1,0-4,0", "9-A");
        OnyxConst.POS_MAP.put("2,0-4,0", "9-B");
        OnyxConst.POS_MAP.put("3,0-4,0", "9-C");
        OnyxConst.POS_MAP.put("4,0-4,0", "9-D");
        OnyxConst.POS_MAP.put("5,0-4,0", "9-E");
        OnyxConst.POS_MAP.put("6,0-4,0", "9-F");
        OnyxConst.POS_MAP.put("7,0-4,0", "9-G");
        OnyxConst.POS_MAP.put("8,0-4,0", "9-H");
        OnyxConst.POS_MAP.put("9,0-4,0", "9-I");
        OnyxConst.POS_MAP.put("10,0-4,0", "9-J");
        OnyxConst.POS_MAP.put("11,0-4,0", "9-K");
        OnyxConst.POS_MAP.put("12,0-4,0", "9-L");

        OnyxConst.POS_MAP.put("1,0-5,0", "8-A");
        OnyxConst.POS_MAP.put("2,0-5,0", "8-B");
        OnyxConst.POS_MAP.put("3,0-5,0", "8-C");
        OnyxConst.POS_MAP.put("4,0-5,0", "8-D");
        OnyxConst.POS_MAP.put("5,0-5,0", "8-E");
        OnyxConst.POS_MAP.put("6,0-5,0", "8-F");
        OnyxConst.POS_MAP.put("7,0-5,0", "8-G");
        OnyxConst.POS_MAP.put("8,0-5,0", "8-H");
        OnyxConst.POS_MAP.put("9,0-5,0", "8-I");
        OnyxConst.POS_MAP.put("10,0-5,0", "8-J");
        OnyxConst.POS_MAP.put("11,0-5,0", "8-K");
        OnyxConst.POS_MAP.put("12,0-5,0", "8-L");

        OnyxConst.POS_MAP.put("1,0-6,0", "7-A");
        OnyxConst.POS_MAP.put("2,0-6,0", "7-B");
        OnyxConst.POS_MAP.put("3,0-6,0", "7-C");
        OnyxConst.POS_MAP.put("4,0-6,0", "7-D");
        OnyxConst.POS_MAP.put("5,0-6,0", "7-E");
        OnyxConst.POS_MAP.put("6,0-6,0", "7-F");
        OnyxConst.POS_MAP.put("7,0-6,0", "7-G");
        OnyxConst.POS_MAP.put("8,0-6,0", "7-H");
        OnyxConst.POS_MAP.put("9,0-6,0", "7-I");
        OnyxConst.POS_MAP.put("10,0-6,0", "7-J");
        OnyxConst.POS_MAP.put("11,0-6,0", "7-K");
        OnyxConst.POS_MAP.put("12,0-6,0", "7-L");

        OnyxConst.POS_MAP.put("1,0-7,0", "6-A");
        OnyxConst.POS_MAP.put("2,0-7,0", "6-B");
        OnyxConst.POS_MAP.put("3,0-7,0", "6-C");
        OnyxConst.POS_MAP.put("4,0-7,0", "6-D");
        OnyxConst.POS_MAP.put("5,0-7,0", "6-E");
        OnyxConst.POS_MAP.put("6,0-7,0", "6-F");
        OnyxConst.POS_MAP.put("7,0-7,0", "6-G");
        OnyxConst.POS_MAP.put("8,0-7,0", "6-H");
        OnyxConst.POS_MAP.put("9,0-7,0", "6-I");
        OnyxConst.POS_MAP.put("10,0-7,0", "6-J");
        OnyxConst.POS_MAP.put("11,0-7,0", "6-K");
        OnyxConst.POS_MAP.put("12,0-7,0", "6-L");

        OnyxConst.POS_MAP.put("1,0-8,0", "5-A");
        OnyxConst.POS_MAP.put("2,0-8,0", "5-B");
        OnyxConst.POS_MAP.put("3,0-8,0", "5-C");
        OnyxConst.POS_MAP.put("4,0-8,0", "5-D");
        OnyxConst.POS_MAP.put("5,0-8,0", "5-E");
        OnyxConst.POS_MAP.put("6,0-8,0", "5-F");
        OnyxConst.POS_MAP.put("7,0-8,0", "5-G");
        OnyxConst.POS_MAP.put("8,0-8,0", "5-H");
        OnyxConst.POS_MAP.put("9,0-8,0", "5-I");
        OnyxConst.POS_MAP.put("10,0-8,0", "5-J");
        OnyxConst.POS_MAP.put("11,0-8,0", "5-K");
        OnyxConst.POS_MAP.put("12,0-8,0", "5-L");

        OnyxConst.POS_MAP.put("1,0-9,0", "4-A");
        OnyxConst.POS_MAP.put("2,0-9,0", "4-B");
        OnyxConst.POS_MAP.put("3,0-9,0", "4-C");
        OnyxConst.POS_MAP.put("4,0-9,0", "4-D");
        OnyxConst.POS_MAP.put("5,0-9,0", "4-E");
        OnyxConst.POS_MAP.put("6,0-9,0", "4-F");
        OnyxConst.POS_MAP.put("7,0-9,0", "4-G");
        OnyxConst.POS_MAP.put("8,0-9,0", "4-H");
        OnyxConst.POS_MAP.put("9,0-9,0", "4-I");
        OnyxConst.POS_MAP.put("10,0-9,0", "4-J");
        OnyxConst.POS_MAP.put("11,0-9,0", "4-K");
        OnyxConst.POS_MAP.put("12,0-9,0", "4-L");

        OnyxConst.POS_MAP.put("1,0-10,0", "3-A");
        OnyxConst.POS_MAP.put("2,0-10,0", "3-B");
        OnyxConst.POS_MAP.put("3,0-10,0", "3-C");
        OnyxConst.POS_MAP.put("4,0-10,0", "3-D");
        OnyxConst.POS_MAP.put("5,0-10,0", "3-E");
        OnyxConst.POS_MAP.put("6,0-10,0", "3-F");
        OnyxConst.POS_MAP.put("7,0-10,0", "3-G");
        OnyxConst.POS_MAP.put("8,0-10,0", "3-H");
        OnyxConst.POS_MAP.put("9,0-10,0", "3-I");
        OnyxConst.POS_MAP.put("10,0-10,0", "3-J");
        OnyxConst.POS_MAP.put("11,0-10,0", "3-K");
        OnyxConst.POS_MAP.put("12,0-10,0", "3-L");

        OnyxConst.POS_MAP.put("1,0-11,0", "2-A");
        OnyxConst.POS_MAP.put("2,0-11,0", "2-B");
        OnyxConst.POS_MAP.put("3,0-11,0", "2-C");
        OnyxConst.POS_MAP.put("4,0-11,0", "2-D");
        OnyxConst.POS_MAP.put("5,0-11,0", "2-E");
        OnyxConst.POS_MAP.put("6,0-11,0", "2-F");
        OnyxConst.POS_MAP.put("7,0-11,0", "2-G");
        OnyxConst.POS_MAP.put("8,0-11,0", "2-H");
        OnyxConst.POS_MAP.put("9,0-11,0", "2-I");
        OnyxConst.POS_MAP.put("10,0-11,0", "2-J");
        OnyxConst.POS_MAP.put("11,0-11,0", "2-K");
        OnyxConst.POS_MAP.put("12,0-11,0", "2-L");

        OnyxConst.POS_MAP.put("1,0-12,0", "1-A");
        OnyxConst.POS_MAP.put("2,0-12,0", "1-B");
        OnyxConst.POS_MAP.put("3,0-12,0", "1-C");
        OnyxConst.POS_MAP.put("4,0-12,0", "1-D");
        OnyxConst.POS_MAP.put("5,0-12,0", "1-E");
        OnyxConst.POS_MAP.put("6,0-12,0", "1-F");
        OnyxConst.POS_MAP.put("7,0-12,0", "1-G");
        OnyxConst.POS_MAP.put("8,0-12,0", "1-H");
        OnyxConst.POS_MAP.put("9,0-12,0", "1-I");
        OnyxConst.POS_MAP.put("10,0-12,0", "1-J");
        OnyxConst.POS_MAP.put("11,0-12,0", "1-K");
        OnyxConst.POS_MAP.put("12,0-12,0", "1-L");

        OnyxConst.POS_MAP.put("2,5-11,5", "B-C-1-2");
        OnyxConst.POS_MAP.put("4,5-11,5", "D-E-1-2");
        OnyxConst.POS_MAP.put("6,5-11,5", "G-G-1-2");
        OnyxConst.POS_MAP.put("8,5-11,5", "H-I-1-2");
        OnyxConst.POS_MAP.put("10,5-11,5", "J-J-1-2");

        OnyxConst.POS_MAP.put("2,5-9,5", "B-C-3-4");
        OnyxConst.POS_MAP.put("4,5-9,5", "D-E-3-4");
        OnyxConst.POS_MAP.put("6,5-9,5", "G-G-3-4");
        OnyxConst.POS_MAP.put("8,5-9,5", "H-I-3-4");
        OnyxConst.POS_MAP.put("10,5-9,5", "J-J-3-4");

        OnyxConst.POS_MAP.put("2,5-7,5", "B-C-5-6");
        OnyxConst.POS_MAP.put("4,5-7,5", "D-E-5-6");
        OnyxConst.POS_MAP.put("6,5-7,5", "G-G-5-6");
        OnyxConst.POS_MAP.put("8,5-7,5", "H-I-5-6");
        OnyxConst.POS_MAP.put("10,5-7,5", "J-J-5-6");

        OnyxConst.POS_MAP.put("2,5-5,5", "B-C-7-8");
        OnyxConst.POS_MAP.put("4,5-5,5", "D-E-7-8");
        OnyxConst.POS_MAP.put("6,5-5,5", "G-G-7-8");
        OnyxConst.POS_MAP.put("8,5-5,5", "H-I-7-8");
        OnyxConst.POS_MAP.put("10,5-5,5", "J-J-7-8");

        OnyxConst.POS_MAP.put("2,5-3,5", "B-C-9-10");
        OnyxConst.POS_MAP.put("4,5-3,5", "D-E-9-10");
        OnyxConst.POS_MAP.put("6,5-3,5", "G-G-9-10");
        OnyxConst.POS_MAP.put("8,5-3,5", "H-I-9-10");
        OnyxConst.POS_MAP.put("10,5-3,5", "J-J-9-10");

        OnyxConst.POS_MAP.put("2,5-1,5", "B-C-11-12");
        OnyxConst.POS_MAP.put("4,5-1,5", "D-E-11-12");
        OnyxConst.POS_MAP.put("6,5-1,5", "G-G-11-12");
        OnyxConst.POS_MAP.put("8,5-1,5", "H-I-11-12");
        OnyxConst.POS_MAP.put("10,5-1,5", "J-J-11-12");

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
    
}
