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
package com.jellyfish.jfgonyx.helpers;

import com.jellyfish.jfgonyx.constants.GraphicsConst;
import com.jellyfish.jfgonyx.entities.*;
import com.jellyfish.jfgonyx.ui.OnyxBoard;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;

/**
 * @author thw
 */
public class GraphicsHelper {

    public static void buildPolygons(final OnyxDiamondCollection c) {

        for (OnyxDiamond d : c.diamonds.values()) {

            if (d.onPairLine) {
                if (d.isFivePosDiamond()) {
                    d.setPolygon(new Polygon(
                            new int[]{
                                d.positions[0].gX, d.positions[1].gX + GraphicsConst.ZIGZAG,
                                d.positions[2].gX + GraphicsConst.ZIGZAG, d.positions[3].gX
                            },
                            new int[]{
                                d.positions[0].gY + GraphicsConst.ZIGZAG,
                                d.positions[1].gY + GraphicsConst.ZIGZAG,
                                d.positions[2].gY, d.positions[3].gY
                            }, 4));
                } else {
                    d.setPolygon(new Polygon(
                            new int[]{
                                d.positions[0].gX, d.positions[1].gX + GraphicsConst.ZIGZAG,
                                d.positions[2].gX + GraphicsConst.ZIGZAG, d.positions[3].gX
                            },
                            new int[]{
                                d.positions[0].gY, d.positions[1].gY,
                                d.positions[2].gY + GraphicsConst.ZIGZAG,
                                d.positions[3].gY + GraphicsConst.ZIGZAG
                            }, 4));
                }
            } else {
                if (d.isFivePosDiamond()) {
                    d.setPolygon(new Polygon(
                            new int[]{
                                d.positions[0].gX + GraphicsConst.ZIGZAG, d.positions[1].gX,
                                d.positions[2].gX, d.positions[3].gX + GraphicsConst.ZIGZAG
                            },
                            new int[]{
                                d.positions[0].gY, d.positions[1].gY,
                                d.positions[2].gY + GraphicsConst.ZIGZAG,
                                d.positions[3].gY + GraphicsConst.ZIGZAG
                            }, 4));
                } else {
                    d.setPolygon(new Polygon(
                            new int[]{
                                d.positions[0].gX + GraphicsConst.ZIGZAG, d.positions[1].gX,
                                d.positions[2].gX, d.positions[3].gX + GraphicsConst.ZIGZAG
                            },
                            new int[]{
                                d.positions[0].gY + GraphicsConst.ZIGZAG,
                                d.positions[1].gY + GraphicsConst.ZIGZAG,
                                d.positions[2].gY, d.positions[3].gY
                            }, 4));
                }
            }
        }
    }

    public static void drawBoard(Graphics2D g, final OnyxDiamondCollection c,
            final OnyxPosCollection p, final OnyxBoard board) {

        g.clearRect(board.getX(), board.getY(), board.getWidth(), board.getHeight());
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);

        /**
         * Translate board before rotation.
         */
        g.translate(board.getWidth() - GraphicsConst.SQUARE_WIDTH,
                board.getHeight() - GraphicsConst.SQUARE_WIDTH);
        final AffineTransform previous = g.getTransform();
        g.rotate(Math.toRadians(180.0));

        g.setColor(Color.BLACK);

        for (OnyxDiamond d : c.diamonds.values()) {

            if (d.onPairLine) {
                if (d.isFivePosDiamond()) {
                    g.setColor(GraphicsConst.FULL_DIAMOND);
                    g.fillPolygon(d.getPolygon());
                    g.setColor(GraphicsConst.LINE);
                    g.drawPolygon(d.getPolygon());
                    g.drawLine(d.positions[0].gX, d.positions[0].gY + GraphicsConst.ZIGZAG,
                            d.positions[2].gX + GraphicsConst.ZIGZAG, d.positions[2].gY);
                    g.drawLine(d.positions[1].gX + GraphicsConst.ZIGZAG,
                            d.positions[1].gY + GraphicsConst.ZIGZAG,
                            d.positions[3].gX, d.positions[3].gY);
                } else {
                    g.setColor(GraphicsConst.DIAMOND);
                    g.fillPolygon(d.getPolygon());
                    g.setColor(GraphicsConst.LINE);
                    g.drawPolygon(d.getPolygon());
                    g.drawLine(d.positions[1].gX + GraphicsConst.ZIGZAG, d.positions[1].gY,
                            d.positions[3].gX, d.positions[3].gY + GraphicsConst.ZIGZAG);
                }
            } else {
                if (d.isFivePosDiamond()) {
                    g.setColor(GraphicsConst.FULL_DIAMOND);
                    g.fillPolygon(d.getPolygon());
                    g.setColor(GraphicsConst.LINE);
                    g.drawPolygon(d.getPolygon());
                    g.drawLine(d.positions[0].gX + GraphicsConst.ZIGZAG, d.positions[0].gY,
                            d.positions[2].gX, d.positions[2].gY + GraphicsConst.ZIGZAG);
                    g.drawLine(d.positions[1].gX, d.positions[1].gY,
                            d.positions[3].gX + GraphicsConst.ZIGZAG,
                            d.positions[3].gY + GraphicsConst.ZIGZAG);
                } else {
                    g.setColor(GraphicsConst.DIAMOND);
                    g.setColor(GraphicsConst.DIAMOND);
                    g.fillPolygon(d.getPolygon());
                    g.setColor(GraphicsConst.LINE);
                    g.drawPolygon(d.getPolygon());
                    g.drawLine(d.positions[0].gX + GraphicsConst.ZIGZAG,
                            d.positions[0].gY + GraphicsConst.ZIGZAG,
                            d.positions[2].gX, d.positions[2].gY);
                }
            }
        }

        GraphicsHelper.drawPieces(g, p);
        if (p.hasVirtualPiece()) {
            GraphicsHelper.drawPiece(g, p.getVirtualPiece().getTmpOnyxPosition(), p.getVirtualPiece());
        }
        g.setTransform(previous);
    }

    private static void drawPieces(Graphics2D g, final OnyxPosCollection c) {

        for (OnyxPos p : c.positions.values()) {
            if (p.isOccupied()) {
                GraphicsHelper.drawPiece(g, p, p.getPiece());
            }
        }
    }

    private static void drawPiece(Graphics2D g, final OnyxPos p, final OnyxPiece piece) {

        GraphicsHelper.translate(g, p);

        g.setColor(piece.color.boolColor
                ? GraphicsConst.BLACK_OUTLINE : GraphicsConst.WHITE_OUTLINE);
        g.drawOval(p.gX - 15, p.gY - 15, 30, 30);
        g.setColor(piece.color.color);
        g.fillOval(p.gX - 15, p.gY - 15, 30, 30);
        
        if (piece.isVirtual()) {
            final Stroke s = g.getStroke();
            g.setStroke(new BasicStroke(2));
            g.setColor(GraphicsConst.VIRTUAL_OUTLINE);
            g.drawOval(p.gX - 15, p.gY - 15, 30, 30);
            g.setStroke(s);
        }

        GraphicsHelper.unTranslate(g, p);
    }
    
    private static void translate(Graphics2D g, final OnyxPos p) {
        
        int c = 0;
        if (p.x % 2 == 0) {
            g.translate(0, GraphicsConst.TRANSLATION);
        } else if (p.isDiamondCenter()) {
            ++c;
        }
        
        if (p.y % 2 == 0) {
            g.translate(GraphicsConst.TRANSLATION, 0);
        } else if (p.isDiamondCenter()) {
            ++c;
        }
        
        if (c == 2) {
            g.translate(GraphicsConst.CENTER_TRANSLATION, GraphicsConst.CENTER_TRANSLATION);
        }
    }
    
    private static void unTranslate(Graphics2D g, final OnyxPos p) {
        
        int c = 0;
        if (p.x % 2 == 0) {
            g.translate(0, -GraphicsConst.TRANSLATION);
        } else if (p.isDiamondCenter()) {
            ++c;
        }
        
        if (p.y % 2 == 0) {
            g.translate(-GraphicsConst.TRANSLATION, 0);
        } else if (p.isDiamondCenter()) {
            ++c;
        }
        
        if (c == 2) {
            g.translate(-GraphicsConst.CENTER_TRANSLATION, -GraphicsConst.CENTER_TRANSLATION);
        }
    }

}
