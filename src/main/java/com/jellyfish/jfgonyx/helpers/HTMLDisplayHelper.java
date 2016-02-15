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

import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author thw
 */
public class HTMLDisplayHelper {
    
    private static final String SPAN = "<span style=\"%s\">%s</span>";
    private static final String TR = "<tr>%s</tr>";
    private static final String TD = 
            "<td style=\"align: center; background-color: rgb(172,172,162);\">%s</td>";
    private static final String TABLE = 
        "<table style=\"border-collapse: collapse;\">%s</table>";
    private static final String FONT_STYLE = "font-family: consolas; font-size: 14px;"; 
    public static final String GRAY_BOLD_TEXT = "<b style=\"color: rgb(124,124,124);" + 
            HTMLDisplayHelper.FONT_STYLE + "\">%s</b>"; 
    public static final String LIGHTGRAY_BOLD_TEXT = "<b style=\"color: rgb(216,216,216);" + 
            HTMLDisplayHelper.FONT_STYLE + "\">%s</b>"; 
    public static final String DARKGRAY_BOLD_TEXT = "<b style=\"color: rgb(60,60,60);" + 
            HTMLDisplayHelper.FONT_STYLE + "\">%s</b>"; 
    private static final String BLACK_BOLD_TEXT = "<b style=\"color: black;" + 
            HTMLDisplayHelper.FONT_STYLE + "\">%s</b>";
    private static final String WHITE_BOLD_TEXT = "<b style=\"color: white;" + 
            HTMLDisplayHelper.FONT_STYLE + "\">%s</b>";
    
    static final String buildHTML(final String[] m, final int b, final int w) {
        final StringBuilder html = new StringBuilder();
        html.append(buildPieceCountTable(b, w));
        html.append(buildMoveDataTable(m));
        return html.toString();
    }
    
    static String buildHTML(String data) {
        return String.format(SPAN, FONT_STYLE, data);
    }
    
    public static final String getMoveText(final int n) {
        return ((n & 1) == 0) & n > 0 ? HTMLDisplayHelper.WHITE_BOLD_TEXT :
            HTMLDisplayHelper.BLACK_BOLD_TEXT;
    }
    
    private static String buildMoveDataTable(final String[] m) {
        
        final StringBuilder innerTableHTML = new StringBuilder();
        String row = StringUtils.EMPTY;
        
        for (int i = 1; i <= m.length; ++i) {
            if ((i & 1) == 0) {
                row += String.format(HTMLDisplayHelper.TD, String.format(DARKGRAY_BOLD_TEXT, i + "."));
                row += String.format(HTMLDisplayHelper.TD, String.format(WHITE_BOLD_TEXT, m[i - 1]));
                innerTableHTML.append(String.format(HTMLDisplayHelper.TR, row));
                row = StringUtils.EMPTY;
            } else {
                row += String.format(HTMLDisplayHelper.TD, String.format(DARKGRAY_BOLD_TEXT, i + "."));
                row += String.format(HTMLDisplayHelper.TD, String.format(BLACK_BOLD_TEXT, m[i - 1]));
            }
        }
        
        return String.format(HTMLDisplayHelper.TABLE, innerTableHTML.toString());
    }

    private static String buildPieceCountTable(final int blacks, final int whites) {
        
        final StringBuilder data = new StringBuilder();
        data.append("<p>");
        data.append(String.format(String.format(LIGHTGRAY_BOLD_TEXT, "WHITE PIECE COUNT: "))); 
        data.append(String.format(WHITE_BOLD_TEXT, String.valueOf(whites)));
        data.append("<br>");
        data.append(String.format(String.format(LIGHTGRAY_BOLD_TEXT, "BLACK PIECE COUNT: "))); 
        data.append(String.format(BLACK_BOLD_TEXT, String.valueOf(blacks)));
        data.append("</p><br>");
        
        return data.toString();
    }
    
}
