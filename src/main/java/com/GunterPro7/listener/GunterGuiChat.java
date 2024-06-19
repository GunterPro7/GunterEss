package com.GunterPro7.listener;

import com.GunterPro7.Main;
import com.GunterPro7.Setting;
import com.GunterPro7.gui.GuiTransparentButton;
import com.GunterPro7.gui.SearchChatGui;
import com.GunterPro7.utils.Utils;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.io.IOException;

import static com.GunterPro7.Command.enableSearchChat;

public class GunterGuiChat extends GuiChat implements Listener {
    private final SearchChatGui searchChat;
    private GuiTransparentButton matchCaseButton;
    private GuiTransparentButton regexCaseButton;
    private int sentHistoryCursor = -1;
    private String historyBuffer = "";

    public GunterGuiChat(SearchChatGui searchChat) {
        this.searchChat = searchChat;
    }

    @Override
    public void initGui() {
        super.initGui();

        matchCaseButton = new GuiTransparentButton(11, width - 32, this.height - 30, 14, 14, "Cc", Setting.SEARCH_TYPE.getValue() % 2 == 1);
        regexCaseButton = new GuiTransparentButton(12, width - 16, this.height - 30, 14, 14, ".*", Utils.isSearchTypeRegex());

        sentHistoryCursor = searchChat.getSentMessages().size();

        buttonList.add(matchCaseButton);
        buttonList.add(regexCaseButton);
    }

    @Override
    protected void handleComponentHover(IChatComponent component, int x, int y) {
        IChatComponent chatComponent = !searchChat.getChatOpen() ? component : searchChat.getChatComponent(x, y);
        System.out.println(chatComponent == null ? "Null" : chatComponent.getFormattedText());
        if (chatComponent != null && chatComponent.getChatStyle() != null && chatComponent.getChatStyle().getChatHoverEvent() != null) {
            super.handleComponentHover(!searchChat.getChatOpen() ? component : searchChat.getChatComponent(x, y), x, y); // TODO check if works
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawString(fontRendererObj, "Search:", 1, (int) (this.mc.displayHeight / (this.mc.gameSettings.guiScale == 0 ? 4 : this.mc.gameSettings.guiScale) - Main.mc.fontRendererObj.FONT_HEIGHT * 2.75), 0xFFFFFF);
        drawRect(2, this.height - 14, this.width - 2, this.height - 2, Integer.MIN_VALUE);

        this.inputField.setTextColor(searchChat.isSortInvalid() ? 0xFF5555 : 0xFFFFFF);
        this.inputField.drawTextBox();

        IChatComponent ichatcomponent = searchChat.getChatComponent(Mouse.getX(), Mouse.getY());
        if (ichatcomponent != null && ichatcomponent.getChatStyle().getChatHoverEvent() != null) {
            this.handleComponentHover(ichatcomponent, mouseX, mouseY);
        }

        for (GuiButton guiButton : buttonList) {
            guiButton.drawButton(Main.mc, mouseX, mouseY);
        }
    }

    @Override
    protected void keyTyped(char eventChar, int keyCode) throws IOException {
        if (keyCode == 28 || keyCode == 156) {
            searchChat.update(inputField.getText());
        } else {
            super.keyTyped(eventChar, keyCode);
            if (Setting.LIVE_SEARCH.isEnabled()) {
                searchChat.update(inputField.getText());
            }
        }
    }

    @Override
    public void getSentHistory(int msgPos) {
        int i = this.sentHistoryCursor + msgPos;
        int j = searchChat.getSentMessages().size();
        i = MathHelper.clamp_int(i, 0, j);
        if (i != this.sentHistoryCursor) {
            if (i == j) {
                this.sentHistoryCursor = j;
                this.inputField.setText(this.historyBuffer);
            } else {
                if (this.sentHistoryCursor == j) {
                    this.historyBuffer = this.inputField.getText();
                }

                this.inputField.setText(searchChat.getSentMessages().get(i));
                this.sentHistoryCursor = i;
            }
        }
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        int i = Mouse.getEventDWheel();
        if (i != 0) {
            if (i > 1) {
                i = 1;
            }

            if (i < -1) {
                i = -1;
            }

            if (!isShiftKeyDown()) {
                i *= 7;
            }

            searchChat.scroll(i);
        }
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
        searchChat.resetScroll();
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (mouseButton == 0) {
            boolean switched = matchCaseButton.mousePressed(Main.mc, mouseX, mouseY) | regexCaseButton.mousePressed(Main.mc, mouseX, mouseY);
            if (switched) {
                boolean match = matchCaseButton.isClicked();
                boolean regex = regexCaseButton.isClicked();

                Setting.SEARCH_TYPE.setValue(!match && !regex ? 0 : match && !regex ? 1 : !match ? 2 : 3);

                if (Setting.LIVE_SEARCH.isEnabled()) {
                    searchChat.update(inputField.getText());
                }
            }

            IChatComponent ichatcomponent = searchChat.getChatComponent(Mouse.getX(), Mouse.getY());

            if (ichatcomponent != null) {
                if (isShiftKeyDown()) {
                    String insertion = ichatcomponent.getChatStyle().getInsertion();
                    if (insertion != null) inputField.writeText(insertion);
                } else if (this.handleComponentClick(ichatcomponent)) {
                    enableSearchChat = false;
                    mc.gameSettings.chatVisibility = EntityPlayer.EnumChatVisibility.FULL;
                    return;
                }
            }

            if (this.handleComponentClick(ichatcomponent)) { // TODO check if works
                return;
            }
        }

        this.inputField.mouseClicked(mouseX, mouseY, mouseButton); // TODO check if works so nothing has been forgotten to click or something
        //super.mouseClicked(mouseX, mouseY, mouseButton);
    }
}
