package com.GunterPro7uDerKatzenLord.Gui;

import com.GunterPro7uDerKatzenLord.Listener.Listeners;
import com.GunterPro7uDerKatzenLord.Main;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.item.ItemStack;

import java.util.List;

import static com.GunterPro7uDerKatzenLord.Main.mc;

public class CustomIngameUI {

    public int backgroundColor;
    public int borderColor;
    public String[] lines;
    public int boxWidth;
    public int boxHeight;

    private CustomIngameUI(int backgroundColor, int borderColor) {
        this.backgroundColor = backgroundColor;
        this.borderColor = borderColor;


    }

    private void initLines() {
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

    public CustomIngameUI(int backgroundColor, int borderColor, String... lines) {
        this(backgroundColor, borderColor);
        this.lines = lines;
        initLines();
    }



    public CustomIngameUI(int backgroundColor, int borderColor, List<String> lines) {
        this(backgroundColor, borderColor);
        this.lines = lines.toArray(new String[0]);
        initLines();
    }

    public void drawInfoBox(int offsetX, int offsetY) {
        FontRenderer fontRenderer = mc.fontRendererObj;

        String text = "Crops: " + Listeners.cropTimeList.size();
        int maxWidth = fontRenderer.getStringWidth(text);

        int boxX = offsetX + 12; // X-Position des GUI, hier 12 Pixel rechts von der Maus
        int boxY = offsetY + 12; // Y-Position des GUI, hier 12 Pixel unter der Maus

        int textHeight = fontRenderer.FONT_HEIGHT;
        int padding = 2; // Abstand zwischen Text und Rand
        int currentHeight = textHeight + padding * 2;

        int counter = 0;
        for (String line : lines) {
            int lineWidth = fontRenderer.getStringWidth(line);
            if (lineWidth > maxWidth) {
                maxWidth = lineWidth;
            }
            fontRenderer.drawString(line, boxX + padding, boxY + padding + currentHeight * counter++, -1); // Text zeichnen
        }

        int boxWidth = maxWidth + padding * 2;
        int boxHeight = currentHeight * lines.length;

        Gui.drawRect(boxX, boxY, boxX + boxWidth, boxY + boxHeight, backgroundColor); // Hintergrund zeichnen
        Gui.drawRect(boxX, boxY, boxX + boxWidth, boxY + 1, borderColor); // Oberer Rand zeichnen
        Gui.drawRect(boxX, boxY + boxHeight - 1, boxX + boxWidth, boxY + boxHeight, borderColor); // Unterer Rand zeichnen
        Gui.drawRect(boxX, boxY, boxX + 1, boxY + boxHeight, borderColor); // Linker Rand zeichnen
        Gui.drawRect(boxX + boxWidth - 1, boxY, boxX + boxWidth, boxY + boxHeight, borderColor); // Rechter Rand zeichnen
    }
}