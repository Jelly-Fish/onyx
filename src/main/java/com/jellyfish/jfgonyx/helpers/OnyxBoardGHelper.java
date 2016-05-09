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

import com.jellyfish.jfgonyx.onyx.entities.OnyxPos;
import com.jellyfish.jfgonyx.onyx.entities.collections.OnyxDiamondCollection;
import com.jellyfish.jfgonyx.onyx.entities.OnyxPiece;
import com.jellyfish.jfgonyx.onyx.entities.collections.OnyxPosCollection;
import com.jellyfish.jfgonyx.onyx.entities.OnyxDiamond;
import com.jellyfish.jfgonyx.vars.GraphicsVars;
import com.jellyfish.jfgonyx.constants.OnyxBoardPositionOutlineConst;
import com.jellyfish.jfgonyx.onyx.constants.OnyxConst;
import com.jellyfish.jfgonyx.ui.OnyxBoard;
import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.Stroke;
import org.apache.commons.lang3.StringUtils;

/**
 * Onyx board graphics helper.
 * @author thw
 */
public class OnyxBoardGHelper {

    public static void buildPolygons(final OnyxDiamondCollection c) {

        for (OnyxDiamond d : c.getDiamonds().values()) {

            if (d.onPairLine) {
                if (d.isFivePosDiamond()) {
                    d.setPolygon(new Polygon(
                        new int[]{
                            d.positions[0].gX, d.positions[1].gX + GraphicsVars.getInstance().ZIGZAG,
                            d.positions[2].gX + GraphicsVars.getInstance().ZIGZAG, d.positions[3].gX
                        },
                        new int[]{
                            d.positions[0].gY + GraphicsVars.getInstance().ZIGZAG,
                            d.positions[1].gY + GraphicsVars.getInstance().ZIGZAG,
                            d.positions[2].gY, d.positions[3].gY
                        }, 4));
                } else {
                    d.setPolygon(new Polygon(
                        new int[]{
                            d.positions[0].gX, d.positions[1].gX + GraphicsVars.getInstance().ZIGZAG,
                            d.positions[2].gX + GraphicsVars.getInstance().ZIGZAG, d.positions[3].gX
                        },
                        new int[]{
                            d.positions[0].gY, d.positions[1].gY,
                            d.positions[2].gY + GraphicsVars.getInstance().ZIGZAG,
                            d.positions[3].gY + GraphicsVars.getInstance().ZIGZAG
                        }, 4));
                }
            } else {
                if (d.isFivePosDiamond()) {
                    d.setPolygon(new Polygon(
                        new int[]{
                            d.positions[0].gX + GraphicsVars.getInstance().ZIGZAG, d.positions[1].gX,
                            d.positions[2].gX, d.positions[3].gX + GraphicsVars.getInstance().ZIGZAG
                        },
                        new int[]{
                            d.positions[0].gY, d.positions[1].gY,
                            d.positions[2].gY + GraphicsVars.getInstance().ZIGZAG,
                            d.positions[3].gY + GraphicsVars.getInstance().ZIGZAG
                        }, 4));
                } else {
                    d.setPolygon(new Polygon(
                        new int[]{
                            d.positions[0].gX + GraphicsVars.getInstance().ZIGZAG, d.positions[1].gX,
                            d.positions[2].gX, d.positions[3].gX + GraphicsVars.getInstance().ZIGZAG
                        },
                        new int[]{
                            d.positions[0].gY + GraphicsVars.getInstance().ZIGZAG,
                            d.positions[1].gY + GraphicsVars.getInstance().ZIGZAG,
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

        OnyxBoardGHelper.drawBackground(g, board);
        OnyxBoardGHelper.drawPositionOutline(g, board);
        
        for (OnyxDiamond d : c.getDiamonds().values()) {

            if (d.onPairLine) {
                if (d.isFivePosDiamond()) {
                    g.setColor(GraphicsVars.getInstance().FULL_DIAMOND);
                    g.fillPolygon(d.getPolygon());
                    g.setColor(GraphicsVars.getInstance().LINE);
                    g.drawPolygon(d.getPolygon());
                    g.drawLine(d.positions[0].gX, d.positions[0].gY + GraphicsVars.getInstance().ZIGZAG,
                            d.positions[2].gX + GraphicsVars.getInstance().ZIGZAG, d.positions[2].gY);
                    g.drawLine(d.positions[1].gX + GraphicsVars.getInstance().ZIGZAG,
                            d.positions[1].gY + GraphicsVars.getInstance().ZIGZAG,
                            d.positions[3].gX, d.positions[3].gY);
                } else {
                    g.setColor(GraphicsVars.getInstance().DIAMOND);
                    g.fillPolygon(d.getPolygon());
                    g.setColor(GraphicsVars.getInstance().LINE);
                    g.drawPolygon(d.getPolygon());
                    g.drawLine(d.positions[1].gX + GraphicsVars.getInstance().ZIGZAG, d.positions[1].gY,
                            d.positions[3].gX, d.positions[3].gY + GraphicsVars.getInstance().ZIGZAG);
                }
            } else {
                if (d.isFivePosDiamond()) {
                    g.setColor(GraphicsVars.getInstance().FULL_DIAMOND);
                    g.fillPolygon(d.getPolygon());
                    g.setColor(GraphicsVars.getInstance().LINE);
                    g.drawPolygon(d.getPolygon());
                    g.drawLine(d.positions[0].gX + GraphicsVars.getInstance().ZIGZAG, d.positions[0].gY,
                            d.positions[2].gX, d.positions[2].gY + GraphicsVars.getInstance().ZIGZAG);
                    g.drawLine(d.positions[1].gX, d.positions[1].gY,
                            d.positions[3].gX + GraphicsVars.getInstance().ZIGZAG,
                            d.positions[3].gY + GraphicsVars.getInstance().ZIGZAG);
                } else {
                    g.setColor(GraphicsVars.getInstance().DIAMOND);
                    g.setColor(GraphicsVars.getInstance().DIAMOND);
                    g.fillPolygon(d.getPolygon());
                    g.setColor(GraphicsVars.getInstance().LINE);
                    g.drawPolygon(d.getPolygon());
                    g.drawLine(d.positions[0].gX + GraphicsVars.getInstance().ZIGZAG,
                            d.positions[0].gY + GraphicsVars.getInstance().ZIGZAG,
                            d.positions[2].gX, d.positions[2].gY);
                }
            }
        }

        OnyxBoardGHelper.drawPieces(g, p);
        if (p.hasVirtualPiece()) {
            OnyxBoardGHelper.drawPiece(g, p.getVirtualPiece().getTmpOnyxPosition(), p.getVirtualPiece());
        }
    }

    private static void drawPieces(Graphics2D g, final OnyxPosCollection c) {

        for (OnyxPos p : c.getPositions().values()) {
            
            /**
             * Debug grphically for mouse click position selection.
            g.setColor(Color.GREEN);
            g.drawOval((int) p.rectangle.getX(), (int) p.rectangle.getY(), 
                (int) p.rectangle.getW(), (int) p.rectangle.getH());
             */
                        
            if (p.isOccupied()) {
                OnyxBoardGHelper.drawPiece(g, p, p.getPiece());
            }
        }
    }

    private static void drawPiece(Graphics2D g, final OnyxPos p, final OnyxPiece piece) {

        OnyxBoardGHelper.translate(g, p);

        g.setColor(piece.color.bool ? 
            GraphicsVars.getInstance().BLACK_OUTLINE : GraphicsVars.getInstance().WHITE_OUTLINE);
        g.drawOval(p.gX - GraphicsVars.getInstance().TRANSLATION, p.gY - GraphicsVars.getInstance().TRANSLATION, 
            GraphicsVars.getInstance().TRANSLATION * 2, GraphicsVars.getInstance().TRANSLATION * 2);
        g.setColor(piece.color.bool ? 
                GraphicsVars.getInstance().BLACK_PIECE : GraphicsVars.getInstance().WHITE_PIECE);
        g.fillOval(p.gX - GraphicsVars.getInstance().TRANSLATION, p.gY - GraphicsVars.getInstance().TRANSLATION, 
            GraphicsVars.getInstance().TRANSLATION * 2, GraphicsVars.getInstance().TRANSLATION * 2);
        
        if (piece.isVirtual() || piece.isEngineMove()) {
            final Stroke s = g.getStroke();
            g.setStroke(new BasicStroke(2));
            g.setColor(piece.isEngineMove() ?
                GraphicsVars.getInstance().ONYX_ENGINE_MOVE_OUTLINE : GraphicsVars.getInstance().VIRTUAL_OUTLINE);
            g.drawOval(p.gX - GraphicsVars.getInstance().TRANSLATION, p.gY - GraphicsVars.getInstance().TRANSLATION,
                GraphicsVars.getInstance().TRANSLATION * 2, GraphicsVars.getInstance().TRANSLATION * 2);
            g.setStroke(s);
        }

        OnyxBoardGHelper.unTranslate(g, p);
    }
    
    private static void translate(Graphics2D g, final OnyxPos p) {
        
        int c = 0;
        if (p.x % 2 == 0) {
            g.translate(0, GraphicsVars.getInstance().TRANSLATION);
        } else if (p.isDiamondCenter()) {
            ++c;
        }
        
        if (p.y % 2 == 0) {
            g.translate(GraphicsVars.getInstance().TRANSLATION, 0);
        } else if (p.isDiamondCenter()) {
            ++c;
        }
        
        if (c == 2) {
            g.translate(GraphicsVars.getInstance().CENTER_TRANSLATION, GraphicsVars.getInstance().CENTER_TRANSLATION);
        }
    }
    
    private static void unTranslate(Graphics2D g, final OnyxPos p) {
        
        int c = 0;
        if (p.x % 2 == 0) {
            g.translate(0, -GraphicsVars.getInstance().TRANSLATION);
        } else if (p.isDiamondCenter()) {
            ++c;
        }
        
        if (p.y % 2 == 0) {
            g.translate(-GraphicsVars.getInstance().TRANSLATION, 0);
        } else if (p.isDiamondCenter()) {
            ++c;
        }
        
        if (c == 2) {
            g.translate(-GraphicsVars.getInstance().CENTER_TRANSLATION, -GraphicsVars.getInstance().CENTER_TRANSLATION);
        }
    }

    private static void drawBackground(Graphics2D g, final OnyxBoard board) {
        g.setColor(GraphicsVars.getInstance().BACKGROUND);
        g.fillRect(0, 0, board.getWidth(), board.getHeight());
    }
    
    private static void drawPositionOutline(Graphics2D g, final OnyxBoard board) {
        
        g.setColor(GraphicsVars.getInstance().BLACK_PIECE);
        for (Polygon p : OnyxBoardPositionOutlineConst.OUTLINE_POLYGONS) {
            g.fillPolygon(p);
            g.setColor(g.getColor().equals(GraphicsVars.getInstance().BLACK_PIECE) ? 
                GraphicsVars.getInstance().WHITE_PIECE : GraphicsVars.getInstance().BLACK_PIECE);
        }
        
        g.setColor(GraphicsVars.getInstance().BACKGROUND);
        g.setFont(OnyxBoardPositionOutlineConst.POS_FONT);
        String value = StringUtils.EMPTY;
        
        int x = 4;
        int y = GraphicsVars.getInstance().BOARD_WIDTH - GraphicsVars.getInstance().SQUARE_WIDTH - 12;
        for (int i = 1; i <= OnyxConst.BOARD_SIDE_POS_COUNT; ++i) {
            value = String.valueOf(i);
            g.drawString(value, value.length() > 1 ? x : x * 2, y);
            y -= GraphicsVars.getInstance().SQUARE_WIDTH;
        }
        
        x = GraphicsVars.getInstance().BOARD_WIDTH - 20;
        y = GraphicsVars.getInstance().BOARD_WIDTH - GraphicsVars.getInstance().SQUARE_WIDTH - 4;
        for (int i = 1; i <= OnyxConst.BOARD_SIDE_POS_COUNT; ++i) {
            value = String.valueOf(i);
            g.drawString(value, value.length() > 1 ? x : x + 4, y);
            y -= GraphicsVars.getInstance().SQUARE_WIDTH;
        }
        
        x = GraphicsVars.getInstance().SQUARE_WIDTH - 8;
        y = 17;
        for (int i = 0; i < OnyxBoardPositionOutlineConst.CHAR_VALUES.length; ++i) {
            value = OnyxBoardPositionOutlineConst.CHAR_VALUES[i];
            g.drawString(value, x, y);
            x += GraphicsVars.getInstance().SQUARE_WIDTH;
        }
        
        x = GraphicsVars.getInstance().SQUARE_WIDTH + 8;
        y = GraphicsVars.getInstance().BOARD_WIDTH - 7;
        for (int i = 0; i < OnyxBoardPositionOutlineConst.CHAR_VALUES.length; ++i) {
            value = OnyxBoardPositionOutlineConst.CHAR_VALUES[i];
            g.drawString(value, x, y);
            x += GraphicsVars.getInstance().SQUARE_WIDTH;
        }
    }
    
}
