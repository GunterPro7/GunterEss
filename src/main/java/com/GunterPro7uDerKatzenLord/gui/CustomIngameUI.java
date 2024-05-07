package com.GunterPro7uDerKatzenLord.gui;

import com.GunterPro7uDerKatzenLord.listener.MiscListener;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;

import java.util.List;

import static com.GunterPro7uDerKatzenLord.Main.mc;

public class CustomIngameUI {

    public int backgroundColor;
    public int borderColor;
    public String[] lines;
    public int boxWidth;
    public int boxHeight;

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

    public void drawInfoBox(int offsetX, int offsetY, boolean background) {
        FontRenderer fontRenderer = mc.fontRendererObj;

        String text = "Crops: " + MiscListener.cropTimeList.size();
        int maxWidth = fontRenderer.getStringWidth(text);

        int margin = 8;
        int padding = 2;

        int boxX = offsetX + margin; // X-Position des GUI, hier 12 Pixel rechts von der Maus
        int boxY = offsetY + margin; // Y-Position des GUI, hier 12 Pixel unter der Maus

        int textHeight = fontRenderer.FONT_HEIGHT;
        int currentHeight = textHeight + padding * 2;

        for (String line : lines) {
            int lineWidth = fontRenderer.getStringWidth(line);
            if (lineWidth > maxWidth) {
                maxWidth = lineWidth;
            }
        }

        int boxWidth = maxWidth + padding * 2;
        int boxHeight = currentHeight * lines.length;

        if (background) {
            Gui.drawRect(boxX, boxY, boxX + boxWidth, boxY + boxHeight, backgroundColor); // Hintergrund zeichnen
            Gui.drawRect(boxX, boxY, boxX + boxWidth, boxY + 1, borderColor); // Oberer Rand zeichnen
            Gui.drawRect(boxX, boxY + boxHeight - 1, boxX + boxWidth, boxY + boxHeight, borderColor); // Unterer Rand zeichnen
            Gui.drawRect(boxX, boxY, boxX + 1, boxY + boxHeight, borderColor); // Linker Rand zeichnen
            Gui.drawRect(boxX + boxWidth - 1, boxY, boxX + boxWidth, boxY + boxHeight, borderColor); // Rechter Rand zeichnen
        }

        int counter = 0;
        for (String line : lines) {
            fontRenderer.drawStringWithShadow(line, boxX + padding, boxY + padding + currentHeight * counter++, -1); // Text zeichnen
        }
    }
}