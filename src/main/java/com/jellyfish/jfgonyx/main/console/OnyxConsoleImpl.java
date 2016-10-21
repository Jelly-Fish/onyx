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
 * *****************************************************************************
 */
package com.jellyfish.jfgonyx.main.console;

import com.jellyfish.jfgonyx.onyx.constants.OnyxConst;
import com.jellyfish.jfgonyx.onyx.exceptions.InvalidOnyxPositionException;
import com.jellyfish.jfgonyx.onyx.exceptions.NoValidOnyxPositionsFoundException;
import com.jellyfish.jfgonyx.onyx.exceptions.OnyxEndGameException;
import com.jellyfish.jfgonyx.onyx.interfaces.OnyxGame;
import com.jellyfish.jfgonyx.onyx.interfaces.OnyxObserver;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import javax.swing.text.BadLocationException;
import javax.swing.text.Utilities;

/**
 *
 * @author thw
 */
public class OnyxConsoleImpl extends javax.swing.JFrame implements OnyxConsole, OnyxObserver {
   
    private javax.swing.JScrollPane scrollPane;
    private javax.swing.JTextArea textArea;
    private final OnyxGame og;
    private final OnyxConst.COLOR engnColor;
    
    /**
     * Creates new form OnyxConsole
     * @param display if false then console is not visible.
     * @param og onyx game instance.
     * @param engnColor engine color.
     */
    public OnyxConsoleImpl(final boolean display, final OnyxGame og, final OnyxConst.COLOR engnColor) {
        this.og = og;
        this.engnColor = engnColor;
        initComponents();
        init();
        this.setVisible(display);
    }
    
    private void init() {
        
        this.setTitle("Onyx console");
        final ImageIcon icn = new ImageIcon(getClass().getClassLoader().getResource("icons/icn.png"));
        this.setIconImage(icn.getImage());
        this.setLocation(0, 0);
        this.textArea.setLineWrap(true);
        this.textArea.append(this.getClass().getName() + " v2.0");
        this.og.setObserver(this);
        this.og.notifyStartLayout(this);
        this.textArea.append(BACKSLH_N);
        this.textArea.setCaretPosition(this.textArea.getDocument().getLength());
    }
    
    private void OnyxConsoleClosing(java.awt.event.WindowEvent evt) {                                    
        
        /**
         * FIXME : cleanup.
         */
        
        System.exit(0);
    }
    
    @Override
    public void notifyMove(final String m) {
        textArea.append(String.format("%s>> onyx move: %s", OnyxConsole.BACKSLH_N, m));
    }
    
    @Override
    public void notifyGameStatus(final String data) {
        this.textArea.append(BACKSLH_N + data);
    }
    
    @Override
    public void notify(final String data) {
        this.textArea.append(BACKSLH_N + data);
    }
    
    private void textAreaKeyPressed(java.awt.event.KeyEvent evt) {                                    
        
        if(evt.getKeyCode() == KeyEvent.VK_ENTER) {

            try {
                
                int end = textArea.getDocument().getLength();
                int start = Utilities.getRowStart(textArea, end);
                
                while (start == end) {
                    end--;
                    start = Utilities.getRowStart(textArea, end);
                }
                                          
                final String move = og.moveVirtual(
                    OnyxConst.POS_MAP.formatInput(textArea.getText(start, end - start).toUpperCase()));
                textArea.append(String.format("%smoving virtual: %s", OnyxConsole.BACKSLH_N, move));
                textArea.append(String.format("%s%s", OnyxConsole.BACKSLH_N, og.playMove()));
                textArea.append(String.format("%sengine response: %s", 
                    OnyxConsole.BACKSLH_N, og.requestNewMove(engnColor)));
                textArea.append(String.format("%snew virtual: %s", 
                    OnyxConsole.BACKSLH_N, og.appendNewVirtual()));
                og.displayGameStatus(this);
                
            } catch (final BadLocationException | InvalidOnyxPositionException | 
                    NoValidOnyxPositionsFoundException | OnyxEndGameException e) {              
                textArea.append(String.format("%s%s", BACKSLH_N, e.getMessage()));
            }
        }    
    } 
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="init swing components">                          
    private void initComponents() {

        scrollPane = new javax.swing.JScrollPane();
        textArea = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setName("onyxconsole"); // NOI18N
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                OnyxConsoleClosing(evt);
            }
        });

        scrollPane.setBorder(null);
        scrollPane.setForeground(new java.awt.Color(212, 208, 200));

        textArea.setBackground(new java.awt.Color(0, 0, 0));
        textArea.setColumns(20);
        textArea.setFont(new java.awt.Font("Consolas", 1, 14)); // NOI18N
        textArea.setForeground(new java.awt.Color(255, 255, 255));
        textArea.setRows(5);
        textArea.setBorder(null);
        textArea.setCaretColor(new java.awt.Color(255, 255, 255));
        textArea.setSelectedTextColor(new java.awt.Color(0, 0, 0));
        textArea.setSelectionColor(new java.awt.Color(255, 255, 255));
        textArea.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                textAreaKeyPressed(evt);
            }
        });
        scrollPane.setViewportView(textArea);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 473, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(scrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 586, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>  
    
}
