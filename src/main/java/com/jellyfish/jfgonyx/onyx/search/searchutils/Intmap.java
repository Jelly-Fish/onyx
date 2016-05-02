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
package com.jellyfish.jfgonyx.onyx.search.searchutils;

import com.jellyfish.jfgonyx.constants.OnyxConst;
import com.jellyfish.jfgonyx.helpers.HTMLDisplayHelper;
import com.jellyfish.jfgonyx.onyx.entities.OnyxPos;
import com.jellyfish.jfgonyx.onyx.entities.collections.OnyxPosCollection;
import com.jellyfish.jfgonyx.ui.MainFrame;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.StringUtils;

/**
 * @author thw
 * static linear and matricial integer arrays represent empty boards for compare
 * usages. linear_intmap & mtx_intmap contain board's integer state value(s).
 * text_representation String array is only for print purposes.
 */
public class Intmap {
    
    private final int[] linear_intmap;
    private final int[][] mtx_intmap;
    private final int width;
    private static final String FILE_FORMAT = "src/main/resources/imap/%s/%s.onyx";
    private static final String DIRECTORY_FORMAT = "src/main/resources/imap/%s";
    private static final String LINE_START_FORMAT = "[%.1f]";
    private static final String DT_STAMP = "[DATE STAMP] >> %s";
    private static final String MOVE_N = "[STRING DISPLAY] >> MOVE N°%d";
    private static final String MTX_MOVE_N = "[INTEGER MATRIX] >> MOVE N°%d";
    private static final String CHARSET = "UTF-8";
    private static final String NEW_LINE = "\n";
    private final String[] text_representation;
    private final int[] linear_static;
    private final int[][] mtx_static;
    
    public Intmap(final OnyxPosCollection c) {
        final int w = (OnyxConst.BOARD_SIDE_SQUARE_COUNT * 2) + 1;
        this.width = w;
        this.mtx_intmap = new int[w][w];
        this.linear_static = new int[(int) Math.pow(w, 2.0)];
        this.mtx_static = new int[w][w];
        this.text_representation = new String[w];
        this.linear_intmap = this.build(c);
    }
    
    private int[] build(final OnyxPosCollection c) {

        String l = StringUtils.EMPTY;
        OnyxPos tmp = null;
        final int[] bmap = new int[(int) Math.pow(this.width, 2.0)];
        int i = 0, j = 0, k = 0;
        
        for (float y = 1.0f; y <= OnyxConst.BOARD_SIDE_SQUARE_COUNT + 1; y += .5f) {
            l = String.format(Intmap.LINE_START_FORMAT, y); 
            l += y < 10 ? StringUtils.SPACE : StringUtils.EMPTY;
            for (float x = 1.0f; x <= OnyxConst.BOARD_SIDE_SQUARE_COUNT + 1; x += .5f) {
                tmp = c.getPosition(String.format(OnyxPosCollection.KEY_FORMAT, x, y));
                bmap[i] = tmp == null ? 3 : (tmp.isOccupied() ? tmp.getPiece().color.bit : 2);
                l += bmap[i] > 1 ? StringUtils.SPACE + StringUtils.SPACE :
                        StringUtils.SPACE + bmap[i];
                this.mtx_intmap[j][k] = bmap[i];
                this.mtx_static[j][k] = tmp == null ? 3 : 2;
                this.linear_static[i] = tmp == null ? 3 : 2;
                ++i;
                ++k;
            }
            this.text_representation[j] = l;
            ++j;
            k = 0;
        }
        
        return bmap;
    }
    
    public final void print(final int m, final String dtStamp) {
        
        if (this.linear_intmap == null || this.text_representation == null) return;
        
        try {
            final ArrayList<String> lines = new ArrayList<>();
            lines.add(String.format(DT_STAMP, dtStamp));
            lines.add(String.format(MOVE_N, m));
            lines.addAll(Arrays.asList(this.text_representation));
            lines.add(Intmap.NEW_LINE + String.format(MTX_MOVE_N, m) + Intmap.NEW_LINE + this.printmtx());
            final File dir = new File(String.format(Intmap.DIRECTORY_FORMAT, dtStamp));
            if (!dir.exists()) dir.mkdir();
            
            final File f = new File(Paths.get(String.format(Intmap.FILE_FORMAT, dtStamp, dtStamp)).toUri());
            if (f.exists()) {
                Files.write(Paths.get(String.format(Intmap.FILE_FORMAT, dtStamp, dtStamp)),
                        lines, Charset.forName(CHARSET), StandardOpenOption.APPEND);
            } else {
                Files.write(Paths.get(String.format(Intmap.FILE_FORMAT, dtStamp, dtStamp)),
                    lines, Charset.forName(CHARSET));
            }
            
        } catch (final IOException ioex) {
            Logger.getLogger(Intmap.class.getName()).log(Level.SEVERE, null, ioex);
        }
    }
    
    public String printmtx() {
        
        if (this.mtx_intmap == null) return null;
        final StringBuilder s = new StringBuilder();
        
        for (int[] m : this.mtx_intmap) {
            for (int i = 0; i < m.length; i++) {
                s.append(StringUtils.SPACE).append(m[i]);
            }
            s.append(Intmap.NEW_LINE);
        }
        
        return s.toString();
    }
    
    public int[] getLinear_intmap() {
        return linear_intmap;
    }

    public int[][] getMtx_intmap() {
        return mtx_intmap;
    }
    
    public String[] getText_representation() {
        return text_representation;
    }
    
}
