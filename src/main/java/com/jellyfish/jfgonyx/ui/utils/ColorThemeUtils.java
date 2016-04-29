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
package com.jellyfish.jfgonyx.ui.utils;

import com.jellyfish.jfgonyx.ui.OnyxPanel;
import com.jellyfish.jfgonyx.vars.GraphicsVars;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

/**
 * @author thw
 */
public class ColorThemeUtils {
    
    /**
     * data directory for serializations.
     */
    public static final String DATA_ROOT = "colorstyles/";
    private static final String SPLITER = "_";
    private static final Map<String, GraphicsVars> THEMES = new HashMap<>();
    
    public static void list() {

        for (File f : new File(DataUtils.DATA_ROOT + ColorThemeUtils.DATA_ROOT).listFiles()) {
            ColorThemeUtils.THEMES.put(ColorThemeUtils.trimThemeName(f),
                (GraphicsVars) DataUtils.xmlDeserialize(f.getPath(), GraphicsVars.class.getClass()));
        }
    }
    
    public static javax.swing.JMenu appendThemes(final javax.swing.JMenu menu, 
        final OnyxPanel mainPanel, final Component ... components) {
                
        javax.swing.JMenuItem item = null;
        GraphicsVars gv= null;
        
        for (Map.Entry<String, GraphicsVars> entry : THEMES.entrySet()) {
            item = new javax.swing.JMenuItem();
            item.setText(entry.getKey());
            item.addActionListener(
                new ActionListener() { 
                    
                    @Override
                    public void actionPerformed(final ActionEvent e) {
                        GraphicsVars.setInstance(ColorThemeUtils.THEMES.get(
                            ((javax.swing.JMenuItem)e.getSource()).getText()));
                        mainPanel.repaint();
                        for (Component c : components) c.repaint();
                    }
                }
            );
            
            menu.add(item);
        }
        
        return menu;
    }
    
    private static String trimThemeName(final File f) {
        
        final String[] split = f.getName().split(SPLITER);
        String name = StringUtils.EMPTY;
        for (String s : split) name += 
            s.replace(DataUtils.XML_FILE_EXTENTION, StringUtils.EMPTY) + StringUtils.SPACE;
        
        return StringUtils.capitalize(name).trim();
    }
    
}
