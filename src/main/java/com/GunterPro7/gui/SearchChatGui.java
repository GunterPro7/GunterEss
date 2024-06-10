package com.GunterPro7.gui;

import com.GunterPro7.listener.AdvancedChat;
import com.GunterPro7.listener.GunterGuiChat;
import com.GunterPro7.utils.MessageInformation;
import com.GunterPro7.utils.Utils;
import com.google.common.collect.Lists;

import java.util.*;
import java.util.regex.Pattern;

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
    private final List<String> sentMessages = Lists.newArrayList();
    private String sortValue = "";
    private boolean sortInvalid;
    private int scrollPos;
    private boolean isScrolled;

    private final Map<ChatLine, List<Map.Entry<String, Boolean>>> searchingParts = new HashMap<>();

    public SearchChatGui(Minecraft minecraft) { // TODO using the hover event thing, so we can hover over everything... not the frkn chat lol
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
                ChatLine chatLine = this.drawnChatLines.get(m + this.scrollPos);
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

                            if (!searchingParts.containsKey(chatLine)) {
                                String string2 = AdvancedChat.clearChatMessageToOnlyThickness(chatLine.getChatComponent().getFormattedText());
                                String fullString = MessageInformation.getById(chatLine.getChatLineID()).getMessageWithOnlyThickness();

                                searchingParts.put(chatLine, getSearchingPartsForSpecificLine(string2, fullString, sortValue,
                                        Utils.isSearchTypeIgnoringCase(), Utils.isSearchTypeRegex()));
                            }

                            StringBuilder stringBuilder2 = new StringBuilder();


                            for (Map.Entry<String, Boolean> part : searchingParts.get(chatLine)) {
                                if (part.getValue()) {
                                    int left = mc.fontRendererObj.getStringWidth(stringBuilder2.toString());
                                    int right = mc.fontRendererObj.getStringWidth(stringBuilder2.append(part.getKey()).toString());
                                    drawRect(left, q - 8, right, q - 8 + mc.fontRendererObj.FONT_HEIGHT, 0xA7f9da15);
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
    private static List<Map.Entry<String, Boolean>> getSearchingPartsForSpecificLine(String string, String fullString, String sortValue, boolean ignoreCase, boolean regex) {
        List<Map.Entry<String, Boolean>> result = new ArrayList<>();
        List<Map.Entry<String, Boolean>> list = getSearchingParts(fullString, sortValue, ignoreCase, regex);

        int index = fullString.indexOf(string); // multiple indexes in the list are allowed, because it'll have the same output

        int curLength = 0;

        for (Map.Entry<String, Boolean> entry : list) {
            int length = entry.getKey().length();

            if (curLength + length >= index) {
                String part = entry.getKey().substring(Math.max(0, index - curLength), Math.min(Math.max(index - curLength + string.length(), 0), length));
                if (!part.isEmpty()) {
                    result.add(new AbstractMap.SimpleEntry<>(AdvancedChat.convertChatMessageWithOnlyThickness(part), entry.getValue()));
                }
            }

            curLength += length;
        }

        return result;
    }

    @NotNull
    private static List<Map.Entry<String, Boolean>> getSearchingParts(String string, String sortValue, boolean ignoreCase, boolean regex) {
        if (ignoreCase) {
            sortValue = sortValue.toLowerCase();
        }

        String[] strings = (ignoreCase ? string.replaceAll("§", "").toLowerCase() : string.replaceAll("§", "")).split(regex ? sortValue : Pattern.quote((ignoreCase ? sortValue.toLowerCase() : sortValue)));

        List<Map.Entry<String, Boolean>> parts = new ArrayList<>();

        StringBuilder stringBuilder = new StringBuilder();
        StringBuilder clearStringBuilder = new StringBuilder();

        int stringIdx = 0;

        for (char c : string.toCharArray()) {
            if (c != '§') {
                boolean stringEqual = stringIdx < strings.length && strings[stringIdx].contentEquals(clearStringBuilder);
                if (stringEqual || (regex ? clearStringBuilder.toString().matches(sortValue) : sortValue.contentEquals(clearStringBuilder))) {
                    parts.add(new AbstractMap.SimpleEntry<>(stringBuilder.toString(), !stringEqual));

                    if (stringEqual) {
                        stringIdx++;
                    }
                    stringBuilder.setLength(0);
                    clearStringBuilder.setLength(0);
                }
                clearStringBuilder.append(ignoreCase ? Character.toLowerCase(c) : c);
            }

            stringBuilder.append(c);
        }

        // Reihenfolge nicht ändern
        boolean sortValueEqual = !regex ? sortValue.contentEquals(clearStringBuilder) : clearStringBuilder.toString().matches(sortValue);

        if (sortValueEqual || (strings.length > stringIdx && strings[stringIdx].contentEquals(clearStringBuilder))) {
            parts.add(new AbstractMap.SimpleEntry<>(stringBuilder.toString(), sortValueEqual));
        }

        return parts;
    }

    public void addToSentMessages(String message) {
        if (this.sentMessages.isEmpty() || !this.sentMessages.get(this.sentMessages.size() - 1).equals(message)) {
            this.sentMessages.add(message);
        }
    }

    public void sortChatLines(String s) {
        if (Utils.isSearchTypeRegex() && !Utils.isRegexValid(s)) {
            this.sortInvalid = true;
            s = "";
        } else {
            this.sortInvalid = false;
        }

        this.sortValue = s;
        this.drawnChatLines.clear();
        int i = MathHelper.floor_float((float) this.getChatWidth() / this.getChatScale()) + 1;

        this.chatLines.forEach((chatLine) -> sortChatLine(chatLine, this.sortValue, i, Utils.isSearchTypeIgnoringCase(), Utils.isSearchTypeRegex()));
        if (this.drawnChatLines.isEmpty()) {
            sortInvalid = true;
        }

        this.searchingParts.clear();
    }

    private void sortChatLine(ChatLine chatLine, String s, int i, boolean ignoreCase, boolean regex) {
        IChatComponent chatComponent = chatLine.getChatComponent();
        String text = AdvancedChat.clearChatMessage(chatLine.getChatComponent().getUnformattedText());

        if (ignoreCase) {
            text = text.toLowerCase();
            s = s.toLowerCase();
        }

        if (regex ? Utils.containsRegex(text, s) : text.contains(s)) {
            List<IChatComponent> list = GuiUtilRenderComponents.splitText(chatComponent, i, this.mc.fontRendererObj, false, false); // BSP: "test UWU" regex: "test.*UWU" -> liste: [] weil alles aufgebraucht wurde
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

        sortChatLine(chatLine, this.sortValue, MathHelper.floor_float((float) this.getChatWidth() / this.getChatScale()) + 1, Utils.isSearchTypeIgnoringCase(), Utils.isSearchTypeRegex());
        this.chatLines.add(chatLine);
        this.searchingParts.clear();
    }

    public void update(String text) {
        this.resetScroll();
        this.sortChatLines(text);
        this.addToSentMessages(text);
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
                        ChatLine chatLine = this.drawnChatLines.get(m);
                        int n = 0;

                        for (IChatComponent iChatComponent : chatLine.getChatComponent()) {
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

    public List<String> getSentMessages() {
        return sentMessages;
    }

    public boolean isSortInvalid() {
        return this.sortInvalid;
    }
}
