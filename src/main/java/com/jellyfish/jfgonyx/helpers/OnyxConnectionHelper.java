/**
 * *****************************************************************************
 * Copyright (c) 2015, 2016, 2017, Thomas.H Warner. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 * c list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * c list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its contributors
 * may be used to endorse or promote products derived from c software without
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

import com.jellyfish.jfgonyx.constants.OnyxConst;
import com.jellyfish.jfgonyx.onyx.entities.OnyxPos;
import com.jellyfish.jfgonyx.onyx.entities.collections.OnyxPosCollection;
import static com.jellyfish.jfgonyx.onyx.entities.collections.OnyxPosCollection.KEY_FORMAT;
import com.jellyfish.jfgonyx.ui.MainFrame;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author thw
 */
public class OnyxConnectionHelper {
    
    private final static String FILLER = ">> ######################################################################";
    private final static String PRINT_FORMAT_TITLE = ">> START ONYX CONNECTIONS.\n>> FOR ALL POSITION, ALL CONNECTED POSITIONS ARE PRINTED.";
    private final static String PRINT_FORMAT_HEAD = ">>\t %s [connections=%d] :";
    private final static String PRINT_FORMAT_TAIL = ">> END CONNECTIONS.\n>> CHECKED ONYX POSITIONS = [%d]";
    private final static String PRINT_FORMAT_CNX = ">>\t\t CNX-%d --> %s";
    private final static String BACKSLASH_N = "\n";
    private static final String FILE_PATH = "src/main/resources/onyxcnx";
    
    public static final void buildPosConnections(final OnyxPosCollection c) {

        float fX = 0f, fY = 0f;
        String kF = StringUtils.EMPTY;
        OnyxPos[] pos = null;
        
        for (int x = 2; x <= (OnyxConst.BOARD_SIDE_SQUARE_COUNT * 2) + 2; ++x) {
            for (int y = 2; y <= (OnyxConst.BOARD_SIDE_SQUARE_COUNT * 2) + 2; ++y) {
                
                fX = x / 2f;
                fY = y / 2f;
                kF = String.format(KEY_FORMAT, fX, fY);
                
                if (c.containsPosition(kF)) { 
                    
                    if (c.getPosition(kF).isDiamondCenter()) {
                        pos = new OnyxPos[4];
                        pos[0] = c.getPosition(String.format(KEY_FORMAT, fX - .5f, fY - .5f));
                        pos[1] = c.getPosition(String.format(KEY_FORMAT, fX + .5f, fY - .5f));
                        pos[2] = c.getPosition(String.format(KEY_FORMAT, fX + .5f, fY + .5f));
                        pos[3] = c.getPosition(String.format(KEY_FORMAT, fX - .5f, fY + .5f));
                    } else {
                        
                        if ((x / 2) % 2 == 0) { 
                            // x is even position.
                            if ((y / 2) % 2 == 0) { 
                                // y is even.
                                pos = new OnyxPos[7];
                                // NE spec :
                                pos[0] = c.getPosition(String.format(KEY_FORMAT, fX + 1f, fY + 1f));
                                pos[1] = c.getPosition(String.format(KEY_FORMAT, fX, fY - 1f));
                                pos[2] = c.getPosition(String.format(KEY_FORMAT, fX + .5f, fY - .5f));
                                pos[3] = c.getPosition(String.format(KEY_FORMAT, fX + 1f, fY));
                                pos[4] = c.getPosition(String.format(KEY_FORMAT, fX, fY + 1f));
                                pos[5] = c.getPosition(String.format(KEY_FORMAT, fX - .5f, fY + .5f));
                                pos[6] = c.getPosition(String.format(KEY_FORMAT, fX - 1f, fY));
                            } else { 
                                // y is odd.
                                pos = new OnyxPos[7];
                                // NW spec :
                                pos[0] = c.getPosition(String.format(KEY_FORMAT, fX - 1f, fY + 1f));
                                pos[1] = c.getPosition(String.format(KEY_FORMAT, fX, fY - 1f));
                                pos[2] = c.getPosition(String.format(KEY_FORMAT, fX + 1f, fY));
                                pos[3] = c.getPosition(String.format(KEY_FORMAT, fX + .5f, fY + .5f));
                                pos[4] = c.getPosition(String.format(KEY_FORMAT, fX, fY + 1f));
                                pos[5] = c.getPosition(String.format(KEY_FORMAT, fX - 1f, fY));
                                pos[6] = c.getPosition(String.format(KEY_FORMAT, fX - .5f, fY - .5f));
                            }
                        } else { 
                            // x is odd.
                            if ((y / 2) % 2 == 0) { 
                                // y is even.
                                pos = new OnyxPos[7];
                                // SE spec :
                                pos[0] = c.getPosition(String.format(KEY_FORMAT, fX + 1f, fY - 1f));
                                pos[1] = c.getPosition(String.format(KEY_FORMAT, fX, fY - 1f));
                                pos[2] = c.getPosition(String.format(KEY_FORMAT, fX + 1f, fY));
                                pos[3] = c.getPosition(String.format(KEY_FORMAT, fX + .5f, fY + .5f));
                                pos[4] = c.getPosition(String.format(KEY_FORMAT, fX, fY + 1f));
                                pos[5] = c.getPosition(String.format(KEY_FORMAT, fX - 1f, fY));
                                pos[6] = c.getPosition(String.format(KEY_FORMAT, fX - .5f, fY - .5f));                                
                            } else { 
                                // y is odd.
                                pos = new OnyxPos[7];                              
                                // SW spec :
                                pos[0] = c.getPosition(String.format(KEY_FORMAT, fX - 1f, fY - 1f));
                                pos[1] = c.getPosition(String.format(KEY_FORMAT, fX, fY - 1f));
                                pos[2] = c.getPosition(String.format(KEY_FORMAT, fX + .5f, fY - .5f));
                                pos[3] = c.getPosition(String.format(KEY_FORMAT, fX + 1f, fY));
                                pos[4] = c.getPosition(String.format(KEY_FORMAT, fX, fY + 1f));
                                pos[5] = c.getPosition(String.format(KEY_FORMAT, fX - .5f, fY + .5f));
                                pos[6] = c.getPosition(String.format(KEY_FORMAT, fX - 1f, fY));                                 
                            }
                        }
                    }
                    
                    c.getPosition(kF).connections = OnyxConnectionHelper.trimConnections(pos, c);
                }
            }
        }
    }
    
    private static String[] trimConnections(final OnyxPos[] candidates, 
            final OnyxPosCollection c) {
        
        final List<String> found = new ArrayList<>();
        for (OnyxPos pos : candidates) {
            if (pos != null) found.add(pos.getKey());
        }
        
        return found.toArray(new String[found.size()]);
    }
    
    public static final void systemPrint(final OnyxPosCollection c) {
        
        for (OnyxPos p : c.getPositions().values()) {
            final String k = p.getKey();
            final int count = p.connections.length;
            System.out.println(String.format(PRINT_FORMAT_HEAD, OnyxConst.POS_MAP.get(p.getKey()), count));
            for (int i = 0; i < count; ++i) {
                System.out.println(String.format(PRINT_FORMAT_CNX, i, OnyxConst.POS_MAP.get(p.connections[i])));
            }
        }
    }
    
    public static final void print(final OnyxPosCollection c) {
        
        int posCount = 0;
        final StringBuilder sb = new StringBuilder(FILLER);
        sb.append(BACKSLASH_N);
        sb.append(PRINT_FORMAT_TITLE);
        sb.append(BACKSLASH_N);
        
        for (OnyxPos p : c.getPositions().values()) {
            ++posCount;
            final String k = p.getKey();
            final int count = p.connections.length;
            sb.append(String.format(PRINT_FORMAT_HEAD, 
                OnyxConst.POS_MAP.get(p.getKey()), count)).append(BACKSLASH_N);
            for (int i = 0; i < count; ++i) {
                sb.append(String.format(PRINT_FORMAT_CNX, i,
                    OnyxConst.POS_MAP.get(p.connections[i]))).append(BACKSLASH_N);
            }
        }
        
        sb.append(String.format(PRINT_FORMAT_TAIL, posCount));
        sb.append(BACKSLASH_N);
        sb.append(FILLER);
        
        try {
            FileWriter f = new FileWriter(FILE_PATH, false);
            f.write(sb.toString());
            f.close();
            MainFrame.print(sb.toString());
        } catch (final IOException iOex) {
            Logger.getLogger(OnyxConnectionHelper.class.getName()).log(Level.SEVERE, null, iOex);
        } catch (final Exception ex) {
            Logger.getLogger(OnyxConnectionHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
    
}