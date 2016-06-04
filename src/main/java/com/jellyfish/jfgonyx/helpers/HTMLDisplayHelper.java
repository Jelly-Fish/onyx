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
package com.jellyfish.jfgonyx.helpers;

/**
 * @author thw
 */
public class HTMLDisplayHelper {
    
    private static final String FONT_SIZE = "12";
    private static final String FONT_FAMILY = "consolas";
    private static final String SPAN = "<span style=\"%s\">%s</span>";
    private static final String FONT_STYLE = "font-family: %s; font-size: %spx; color: %s"; 
    
    public static final String WHITE = "rgb(245,245,245)";
    public static final String GRAY = "rgb(162,162,168)";
    public static final String LIME = "rgb(0,255,0)";
    public static final String MEDIUM_TURQUOISE = "rgb(72,209,204)";
    public static final String DARK_TURQUOISE = "rgb(0,206,209)";
    public static final String AQUA_TURQUOISE = "rgb(0,255,255)";
    public static final String HOT_PINK = "rgb(255,105,180)";
    public static final String GAINSBORO = "rgb(220,220,220)";
    public static final String LIME_GREEN = "rgb(50,205,50)";
    public static final String GOLD = "rgb(255,215,0)";
    public static final String COPPER_YELLOW_DARK = "rgb(186,186,39)";
    
    static String buildHTML(final String data, final String color) {
        return String.format(SPAN, String.format(FONT_STYLE, FONT_FAMILY, FONT_SIZE, color), data);
    }
    
}
