package com.GunterPro7.gui;

import com.GunterPro7.Main;
import com.GunterPro7.listener.AdvancedChat;
import com.GunterPro7.listener.GunterGuiChat;
import com.GunterPro7.utils.MessageInformation;
import com.google.common.collect.Lists;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.jetbrains.annotations.NotNull;

@SideOnly(Side.CLIENT)
public class SearchChatGui extends Gui {
    private final Minecraft mc;
    private final List<ChatLine> chatLines = Lists.newArrayList();
    private final List<ChatLine> drawnChatLines = Lists.newArrayList();
    private String sortValue = "";
    private int scrollPos;
    private boolean isScrolled;

    public SearchChatGui(Minecraft minecraft) {
        this.mc = minecraft;
    }

    public void drawChat(int updateCounter) {
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
                    if (n < 200 || bl) {
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
                            drawRect(p, q - 9, p + l + 4, q, 0x7F000000);
                            String string = chatLine.getChatComponent().getFormattedText();
                            GlStateManager.enableBlend();

                            String fullString = MessageInformation.getById(chatLine.getChatLineID()).getMcMessage();
                            System.out.println(fullString);


                            Map<String, Boolean> parts = getSearchingPartsForSpecificLine(string, fullString, sortValue, true); // TODo make a sort like switch, like intellij has
                            System.out.println(parts);

                            StringBuilder stringBuilder2 = new StringBuilder();
                            FontRenderer fontRenderer = Main.mc.fontRendererObj;
                            for (Map.Entry<String, Boolean> part : parts.entrySet()) {
                                if (part.getValue()) {
                                    int left = fontRenderer.getStringWidth(stringBuilder2.toString());
                                    int right = fontRenderer.getStringWidth(stringBuilder2.append(part.getKey()).toString());
                                    drawRect(left, q - 8, right, q - 8 + fontRenderer.FONT_HEIGHT, 0xA7f9da15);
                                } else {
                                    stringBuilder2.append(part.getKey());
                                }
                            }

                            this.mc.fontRendererObj.drawStringWithShadow(string, (float) p, (float) (q - 8), 16777215 + (o << 24));
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

    @NotNull
    private static Map<String, Boolean> getSearchingPartsForSpecificLine(String string, String fullString, String sortValue, boolean ignoreCase) {
        Map<String, Boolean> result = new LinkedHashMap<>();
        Map<String, Boolean> list = getSearchingParts(fullString, sortValue, ignoreCase);

        int index = fullString.indexOf(string); // multiple indexes in the list are allowed, because it'll have the same output

        int curLength = 0;

        for (Map.Entry<String, Boolean> entry : list.entrySet()) {
            int length = entry.getKey().length();

            if (curLength + length >= index) {
                String part = entry.getKey().substring(Math.max(0, index - curLength), Math.min(Math.max(index - curLength + string.length(), 0), length));
                //System.out.println(part);
                //System.out.println(entry.getValue().toString());

                result.put(part, entry.getValue());
            }

            curLength += length;
        }

        return result;
    }

    @NotNull
    private static Map<String, Boolean> getSearchingParts(String string, String sortValue, boolean ignoreCase) {
        if (ignoreCase) {
            sortValue = sortValue.toLowerCase();
        }

        String[] strings = (ignoreCase ? AdvancedChat.clearChatMessage(string).toLowerCase() : AdvancedChat.clearChatMessage(string)).split(Pattern.quote((ignoreCase ? sortValue.toLowerCase() : sortValue)));

        Map<String, Boolean> parts = new LinkedHashMap<>();

        boolean skippingNext = false;

        StringBuilder stringBuilder = new StringBuilder();
        StringBuilder clearStringBuilder = new StringBuilder();

        int stringIdx = 0;

        for (char c : string.toCharArray()) {
            if (!skippingNext) {
                if (c == '§') {
                    skippingNext = true;
                } else {

                    boolean stringEqual = stringIdx < strings.length && strings[stringIdx].contentEquals(clearStringBuilder);
                    if (stringEqual || sortValue.contentEquals(clearStringBuilder)) {
                        parts.put(stringBuilder.toString(), !stringEqual);

                        if (stringEqual) {
                            stringIdx++;
                        }
                        stringBuilder.setLength(0);
                        clearStringBuilder.setLength(0);
                    }
                    clearStringBuilder.append(ignoreCase ? Character.toLowerCase(c) : c);
                }

            } else {
                skippingNext = false;
            }

            stringBuilder.append(c);
        }

        // Reihenfolge nicht ändern
        boolean sortValueEqual = sortValue.contentEquals(clearStringBuilder);

        if (sortValueEqual || strings[stringIdx].contentEquals(clearStringBuilder)) {
            parts.put(stringBuilder.toString(), sortValueEqual);
        }

        return parts;
    }

    public void sortChatLines(String s) {
        this.sortValue = s;
        this.drawnChatLines.clear();
        int i = MathHelper.floor_float((float) this.getChatWidth() / this.getChatScale()) + 1;

        this.chatLines.forEach((chatLine) -> sortChatLine(chatLine, s, i));
    }

    private void sortChatLine(ChatLine chatLine, String s, int i) {
        IChatComponent chatComponent = chatLine.getChatComponent();
        String text = AdvancedChat.clearChatMessage(chatLine.getChatComponent().getUnformattedText());

        if (text.toLowerCase().contains(s.toLowerCase())) {
            List<IChatComponent> list = GuiUtilRenderComponents.splitText(chatComponent, i, this.mc.fontRendererObj, false, false);
            IChatComponent iChatComponent;

            for (Iterator<IChatComponent> iterator = list.iterator(); iterator.hasNext(); this.drawnChatLines.add(0, new ChatLine(chatLine.getUpdatedCounter(), iChatComponent, chatLine.getChatLineID()))) {
                iChatComponent = iterator.next();
            }
        }
    }

    public void setChatLine(IChatComponent chatComponent, int chatLineId, int updateCounter) {
        if (chatLineId != 0) {
            this.deleteChatLine(chatLineId);
        }

        ChatLine chatLine = new ChatLine(updateCounter, chatComponent, chatLineId);

        sortChatLine(chatLine, this.sortValue, MathHelper.floor_float((float) this.getChatWidth() / this.getChatScale()) + 1);
        this.chatLines.add(chatLine);
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
        Iterator<ChatLine> iterator = this.chatLines.iterator();

        ChatLine chatLine;
        while (iterator.hasNext()) {
            chatLine = iterator.next();
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
