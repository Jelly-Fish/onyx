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

import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author thw
 */
public class DataDisplayConst {
    
    private static final String TR = "<tr>%s</tr>";
    private static final String TD = "<td style=\"border: 1px solid black; align: center;\">%s</td>";
    private static final String TABLE = 
        "<table style=\"border-collapse: collapse;\">%s</table>";
    private static final String FONT_STYLE = "font-family: consolas; font-size: 14px;"; 
    public static final String DARKGREY_BOLD_TEXT = "<b style=\"color: orange;" + 
            DataDisplayConst.FONT_STYLE + "\">%s</b>"; 
    public static final String BLACK_BOLD_TEXT = "<b style=\"color: black;" + 
            DataDisplayConst.FONT_STYLE + "\">%s</b>";
    public static final String WHITE_BOLD_TEXT = "<b style=\"color: white;" + 
            DataDisplayConst.FONT_STYLE + "\">%s</b>";
    
    public static String getMoveText(final int n) {
        return ((n & 1) == 0) & n > 0 ? DataDisplayConst.WHITE_BOLD_TEXT :
            DataDisplayConst.BLACK_BOLD_TEXT;
    }
    
    public static String buildMoveDataTable(final String[] m) {
        
        final StringBuilder innerTableHTML = new StringBuilder();
        String row = StringUtils.EMPTY;
        
        for (int i = 1; i <= m.length; ++i) {
            if ((i & 1) == 0) {
                row += String.format(DataDisplayConst.TD, String.format(DARKGREY_BOLD_TEXT, i + "."));
                row += String.format(DataDisplayConst.TD, String.format(WHITE_BOLD_TEXT, m[i - 1]));
                innerTableHTML.append(String.format(DataDisplayConst.TR, row));
                row = StringUtils.EMPTY;
            } else {
                row += String.format(DataDisplayConst.TD, String.format(DARKGREY_BOLD_TEXT, i + "."));
                row += String.format(DataDisplayConst.TD, String.format(BLACK_BOLD_TEXT, m[i - 1]));
            }
        }
        
        return String.format(DataDisplayConst.TABLE, innerTableHTML.toString());
    }
    
}
