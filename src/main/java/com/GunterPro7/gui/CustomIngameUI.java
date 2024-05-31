package com.GunterPro7.gui;

import com.GunterPro7.Main;
import com.GunterPro7.Setting;
import com.GunterPro7.listener.AdvancedChat;
import com.GunterPro7.utils.McUtils;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;

import java.util.List;

import static com.GunterPro7.Main.mc;

public class CustomIngameUI {
    public static final int PADDING = 2;

    public int backgroundColor;
    public int borderColor;
    public String[] lines;
    public int boxWidth;
    public int boxHeight;
    private Align align = Align.LEFT;

    public CustomIngameUI(int backgroundColor, int borderColor, String... lines) {
        this.backgroundColor = backgroundColor;
        this.borderColor = borderColor;
        this.lines = lines;

        FontRenderer fontRenderer = mc.fontRendererObj;
        int maxWidth = 0;
        for (String line : lines) {
            int lineWidth = fontRenderer.getStringWidth(line);
            if (lineWidth > maxWidth) {
                maxWidth = lineWidth;
            }
        }

        boxWidth = maxWidth;
        boxHeight = (fontRenderer.FONT_HEIGHT + 4) * lines.length;
    }



    public CustomIngameUI(int backgroundColor, int borderColor, List<String> lines) {
        this(backgroundColor, borderColor, lines.toArray(new String[0]));
    }

    public void align(Align align) {
        this.align = align;
    }

    public void drawInfoBox(Setting.Position position, boolean background) {
        drawInfoBox(position.getOffsetX(), position.getOffsetY(), background);
    }

    public void drawInfoBox(double offsetX, double offsetY, boolean background) {
        drawInfoBox((int) (McUtils.getScaleWidth() * offsetX), (int) (McUtils.getScaleHeight() * offsetY), background);
    }

    public void drawInfoBox(int offsetX, int offsetY, boolean background) {
        FontRenderer fontRenderer = mc.fontRendererObj;

        int textHeight = fontRenderer.FONT_HEIGHT;
        int currentHeight = textHeight + PADDING * 2;

        int maxWidth = 0;
        for (String line : lines) {
            int lineWidth = fontRenderer.getStringWidth(line);
            if (lineWidth > maxWidth) {
                maxWidth = lineWidth;
            }
        }

        int boxWidth = maxWidth + PADDING * 2;
        int boxHeight = currentHeight * lines.length;

        int scaleWidth = McUtils.getScaleWidth();
        int scaleHeight = McUtils.getScaleHeight();

        int boxX = offsetX;
        int boxY = offsetY + fontRenderer.FONT_HEIGHT > scaleHeight ? scaleHeight - fontRenderer.FONT_HEIGHT : offsetY;

        if (align == Align.MIDDLE) {
            boxX -= boxWidth / 2;
        } else if (align == Align.RIGHT) {
            boxX -= boxWidth;
        }

        if (background) {
            Gui.drawRect(boxX, boxY, boxX + boxWidth, boxY + boxHeight, backgroundColor); // Hintergrund zeichnen
            Gui.drawRect(boxX, boxY, boxX + boxWidth, boxY + 1, borderColor); // Oberer Rand zeichnen
            Gui.drawRect(boxX, boxY + boxHeight - 1, boxX + boxWidth, boxY + boxHeight, borderColor); // Unterer Rand zeichnen
            Gui.drawRect(boxX, boxY + 1, boxX + 1, boxY + boxHeight - 1, borderColor); // Linker Rand zeichnen
            Gui.drawRect(boxX + boxWidth - 1, boxY + 1, boxX + boxWidth, boxY + boxHeight - 1, borderColor); // Rechter Rand zeichnen
        }

        int counter = 0;
        for (String line : lines) {
            fontRenderer.drawStringWithShadow(line, boxX + PADDING, boxY + PADDING + currentHeight * counter++, -1); // Text zeichnen
        }
    }
}