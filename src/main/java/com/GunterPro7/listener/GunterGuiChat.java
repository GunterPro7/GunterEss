package com.GunterPro7.listener;

import com.GunterPro7.Main;
import com.GunterPro7.gui.GuiTransparentButton;
import com.GunterPro7.gui.GuiTransparentImageButton;
import com.GunterPro7.gui.SearchChatGui;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.io.IOException;

import static com.GunterPro7.Command.enableSearchChat;

public class GunterGuiChat extends GuiChat implements Listener {
    private final SearchChatGui searchChat;
    private GuiTransparentButton wordCaseButton;
    private GuiTransparentButton matchCaseButton;
    private GuiTransparentButton regexCaseButton;

    public GunterGuiChat(SearchChatGui searchChat) {
        this.searchChat = searchChat;
    }

    @Override
    public void initGui() {
        super.initGui();

        wordCaseButton = new GuiTransparentButton(10, width - 50, this.height - 30, 14, 14, "Wo");
        matchCaseButton = new GuiTransparentButton(11, width - 34, this.height - 30, 14, 14, "Cc");
        regexCaseButton = new GuiTransparentButton(12, width - 18, this.height - 30, 14, 14, ".*");
        buttonList.add(wordCaseButton);
        buttonList.add(matchCaseButton);
        buttonList.add(regexCaseButton);
    }

    @Override
    protected void handleComponentHover(IChatComponent component, int x, int y) {
        IChatComponent chatComponent = !searchChat.getChatOpen() ? component : searchChat.getChatComponent(x, y);
        if (chatComponent != null && chatComponent.getChatStyle() != null && chatComponent.getChatStyle().getChatHoverEvent() != null) {
            super.handleComponentHover(!searchChat.getChatOpen() ? component : searchChat.getChatComponent(x, y), x, y); // TODO check if works
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawString(fontRendererObj, "Search:", 1, (int) (this.mc.displayHeight / (this.mc.gameSettings.guiScale == 0 ? 4 : this.mc.gameSettings.guiScale) - Main.mc.fontRendererObj.FONT_HEIGHT * 2.75), 0xFFFFFF);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void keyTyped(char eventChar, int keyCode) throws IOException {
        if (keyCode == 28 || keyCode == 156) {
            searchChat.resetScroll();
            searchChat.sortChatLines(inputField.getText());
        } else {
            super.keyTyped(eventChar, keyCode);
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
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (mouseButton == 0) {
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
