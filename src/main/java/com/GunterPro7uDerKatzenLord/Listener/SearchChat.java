//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.GunterPro7uDerKatzenLord.Listener;

import com.google.common.collect.Lists;

import java.util.Iterator;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class SearchChat extends Gui {
    private final Minecraft mc;
    public final List<ChatLine> chatLines = Lists.newArrayList();
    public final List<ChatLine> drawnChatLines = Lists.newArrayList();
    public final List<ChatLine> filteredChatLines = Lists.newArrayList();
    public int scrollPos;
    public boolean isScrolled;

    public SearchChat(Minecraft minecraft) {
        this.mc = minecraft;
    }

    public void drawChat(int updateCounter) {
        int offsetY = (int) (this.mc.displayHeight / (this.mc.gameSettings.guiScale == 0 ? 4 : this.mc.gameSettings.guiScale) - (this.mc.fontRendererObj.FONT_HEIGHT * 5.5));
        int i = this.getLineCount();
        boolean bl = false;
        int j = 0;
        int k = this.drawnChatLines.size();
        float f = this.mc.gameSettings.chatOpacity * 0.9F + 0.1F;
        if (k > 0) {
            if (this.getChatOpen()) {
                bl = true;
            }

            float g = this.getChatScale();
            int l = MathHelper.ceiling_float_int((float) this.getChatWidth() / g);
            GlStateManager.pushMatrix();
            GlStateManager.translate(2.0F, 20.0F, 0.0F);
            GlStateManager.scale(g, g, 1.0F);

            int m;
            int n;
            int o;
            int p;
            for (m = 0; m + this.scrollPos < this.drawnChatLines.size() && m < i; ++m) {
                ChatLine chatLine = (ChatLine) this.drawnChatLines.get(m + this.scrollPos);
                if (chatLine != null) {
                    n = updateCounter - chatLine.getUpdatedCounter();
                    if (n < 200 || bl || true) {
                        double d = (double) n / 200.0;
                        d = 1.0 - d;
                        d *= 10.0;
                        d = MathHelper.clamp_double(d, 0.0, 1.0);
                        d *= d;
                        o = (int) (255.0 * d);
                        if (bl) {
                            o = 255;
                        }

                        o = (int) ((float) o * f);
                        ++j;
                        if (o > 3) {
                            p = 0;
                            int q = -m * 9;
                            GlStateManager.enableBlend();
                            drawRect(p, offsetY + q - 9, p + l + 4, offsetY + q, 0x7F000000);
                            String string = chatLine.getChatComponent().getFormattedText();
                            this.mc.fontRendererObj.drawStringWithShadow(string, (float) p, offsetY + (float) (q - 8), 16777215 + (o << 24));
                            GlStateManager.disableAlpha();
                            GlStateManager.disableBlend();
                        }
                    }
                }
            }

            if (bl) {
                m = this.mc.fontRendererObj.FONT_HEIGHT;
                GlStateManager.translate(-3.0F, 0.0F, 0.0F);
                int r = k * m + k;
                n = j * m + j;
                int s = this.scrollPos * n / k;
                int t = n * n / r;
                if (r != n) {
                    o = s > 0 ? 170 : 96;
                    p = this.isScrolled ? 13382451 : 3355562;
                    drawRect(0, -s, 2, -s - t, p + (o << 24));
                    drawRect(2, -s, 1, -s - t, 13421772 + (o << 24));
                }
            }

            GlStateManager.popMatrix();
        }
    }

    public void sortChatLines(String s) {
        this.drawnChatLines.clear();
        int i = MathHelper.floor_float((float) this.getChatWidth() / this.getChatScale());

        for (ChatLine chatLine : this.chatLines) {
            IChatComponent chatComponent = chatLine.getChatComponent();
            String text = chatLine.getChatComponent().getUnformattedText();

            if (text.contains(s)) {
                List<IChatComponent> list = GuiUtilRenderComponents.splitText(chatComponent, i, this.mc.fontRendererObj, false, false);
                IChatComponent iChatComponent;

                for (Iterator<IChatComponent> iterator = list.iterator(); iterator.hasNext(); this.drawnChatLines.add(0, new ChatLine(chatLine.getUpdatedCounter(), iChatComponent, chatLine.getChatLineID()))) {
                    iChatComponent = iterator.next();
                }
            }
        }
    }

    public void setChatLine(IChatComponent chatComponent, int chatLineId, int updateCounter) {
        if (chatLineId != 0) {
            this.deleteChatLine(chatLineId);
        }

        //int i = MathHelper.floor_float((float) this.getChatWidth() / this.getChatScale());
        //List<IChatComponent> list = GuiUtilRenderComponents.splitText(chatComponent, i, this.mc.fontRendererObj, false, false);
        //boolean bl = this.getChatOpen();
//
        //IChatComponent iChatComponent;
        //for (Iterator iterator = list.iterator(); iterator.hasNext(); this.drawnChatLines.add(0, new ChatLine(updateCounter, iChatComponent, chatLineId))) {
        //    iChatComponent = (IChatComponent) iterator.next();
        //    if (bl && this.scrollPos > 0) {
        //        this.isScrolled = true;
        //        this.scroll(1);
        //    }
        //}
//
        //while (this.drawnChatLines.size() > 100) { // TODO DELETE THIS SHIT HERE
        //    this.drawnChatLines.remove(this.drawnChatLines.size() - 1);
        //}

        this.chatLines.add(0, new ChatLine(updateCounter, chatComponent, chatLineId));

        //while (this.chatLines.size() > 100) { // TODO DELETE THIS SHIT HERE
        //    this.chatLines.remove(this.chatLines.size() - 1);
        //}
    }

    public void resetScroll() {
        this.scrollPos = 0;
        this.isScrolled = false;
    }

    public void scroll(int amount) {
        this.scrollPos += amount;
        int i = this.drawnChatLines.size();
        if (this.scrollPos > i - this.getLineCount()) {
            this.scrollPos = i - this.getLineCount();
        }

        if (this.scrollPos <= 0) {
            this.scrollPos = 0;
            this.isScrolled = false;
        }

    }

    public IChatComponent getChatComponent(int mouseX, int mouseY) {
        if (!this.getChatOpen()) {
            return null;
        } else {
            ScaledResolution scaledResolution = new ScaledResolution(this.mc);
            int i = scaledResolution.getScaleFactor();
            float f = this.getChatScale();
            int j = mouseX / i - 3;
            int k = mouseY / i - 27;
            j = MathHelper.floor_float((float) j / f);
            k = MathHelper.floor_float((float) k / f);
            if (j >= 0 && k >= 0) {
                int l = Math.min(this.getLineCount(), this.drawnChatLines.size());
                if (j <= MathHelper.floor_float((float) this.getChatWidth() / this.getChatScale()) && k < this.mc.fontRendererObj.FONT_HEIGHT * l + l) {
                    int m = k / this.mc.fontRendererObj.FONT_HEIGHT + this.scrollPos;
                    if (m >= 0 && m < this.drawnChatLines.size()) {
                        ChatLine chatLine = (ChatLine) this.drawnChatLines.get(m);
                        int n = 0;
                        Iterator iterator = chatLine.getChatComponent().iterator();

                        while (iterator.hasNext()) {
                            IChatComponent iChatComponent = (IChatComponent) iterator.next();
                            if (iChatComponent instanceof ChatComponentText) {
                                n += this.mc.fontRendererObj.getStringWidth(GuiUtilRenderComponents.func_178909_a(((ChatComponentText) iChatComponent).getChatComponentText_TextValue(), false));
                                if (n > j) {
                                    return iChatComponent;
                                }
                            }
                        }
                    }

                    return null;
                } else {
                    return null;
                }
            } else {
                return null;
            }
        }
    }

    public boolean getChatOpen() {
        return this.mc.currentScreen instanceof GunterGuiChat;
    }

    public void deleteChatLine(int id) {
        Iterator<ChatLine> iterator = this.drawnChatLines.iterator();

        ChatLine chatLine;
        while (iterator.hasNext()) {
            chatLine = (ChatLine) iterator.next();
            if (chatLine.getChatLineID() == id) {
                iterator.remove();
            }
        }

        iterator = this.chatLines.iterator();

        while (iterator.hasNext()) {
            chatLine = (ChatLine) iterator.next();
            if (chatLine.getChatLineID() == id) {
                iterator.remove();
                break;
            }
        }

    }

    public int getChatWidth() {
        return calculateChatboxWidth(this.mc.gameSettings.chatWidth);
    }

    public int getChatHeight() {
        return calculateChatboxHeight(this.getChatOpen() ? this.mc.gameSettings.chatHeightFocused : this.mc.gameSettings.chatHeightUnfocused);
    }

    public float getChatScale() {
        return this.mc.gameSettings.chatScale;
    }

    public static int calculateChatboxWidth(float scale) {
        int i = 320;
        int j = 40;
        return MathHelper.floor_float(scale * (float) (i - j) + (float) j);
    }

    public static int calculateChatboxHeight(float scale) {
        int i = 180;
        int j = 20;
        return MathHelper.floor_float(scale * (float) (i - j) + (float) j);
    }

    public int getLineCount() {
        return this.getChatHeight() / 9;
    }
}
