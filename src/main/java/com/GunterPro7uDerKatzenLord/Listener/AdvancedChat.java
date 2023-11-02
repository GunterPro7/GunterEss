package com.GunterPro7uDerKatzenLord.Listener;

import com.GunterPro7uDerKatzenLord.Setting;
import com.GunterPro7uDerKatzenLord.Utils.MathUtils;
import com.GunterPro7uDerKatzenLord.Utils.MessageInformation;
import com.GunterPro7uDerKatzenLord.Utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.util.HashMap;
import java.util.Map;

public class AdvancedChat {
    public static int current_id = 1;
    public static final Map<Integer, MessageInformation> MAP = new HashMap<>();
    public static final String brea = "§F";
    public static final Map<ChatCondition, Function> actionMap = Utils.createMap(ChatCondition.class, Function.class,
            new ChatCondition("QUICK MATHS! Solve: ", Condition.STARTSWITH), (Function) message -> Utils.sendChatMessageAsPlayer("/ac " + String.valueOf(MathUtils.eval(message.getUnformattedText().replaceAll("§[0-9a-zA-Z]", "").substring("QUICK MATHS! Solve: ".length()).replaceAll("x", "*"))).replace(".0", "")),
            new ChatCondition("Click HERE to sign the ", Condition.STARTSWITH), (Function) message -> Utils.sendChatMessageAsPlayer(message.getChatStyle().getChatClickEvent().getValue()),
            new ChatCondition("[FEAR] Public Speaking Demon: Speak ", Condition.STARTSWITH), (Function) message -> Utils.sendChatMessageAsPlayer("/ac q weqwe qwe qwe qeqwe qweqwe qwe qwe ")
    );

    @SubscribeEvent
    public void onChatMessage(final ClientChatReceivedEvent event) {

        if (event.type == 2 || event.message.getUnformattedText().matches("\\{.*}")) { // TODO we can probably read the json sent by the server: {"server":"mini95DK","gametype":"SKYBLOCK","mode":"dynamic","map":"Private Island"}
            return;
        }
        if (Setting.REMOVE_BLANK_LINES.isEnabled() && event.message.getUnformattedText().trim().equals("")) {
            event.message = new ChatComponentText("");
            return;
        }

        String text = event.message.getUnformattedText().replaceAll("§[0-9a-zA-Z]", "");

        // Do Chat Actions if specific message

        System.out.println(actionMap);

        for (Map.Entry<ChatCondition, Function> entry : actionMap.entrySet()) {
            ChatCondition key = entry.getKey();

            if (key.getCondition().check(key.getText(), text)) {
                entry.getValue().action(event.message);
                break;
            }
        }




        // Change message so they can be copied afterwards

        MessageInformation messageInformation;
        if (MessageInformation.instances.containsKey(text)) {
            messageInformation = MessageInformation.instances.get(text);
        } else {
            messageInformation = new MessageInformation(text, current_id++);
            MessageInformation.instances.put(text, messageInformation);
        }

        messageInformation.count();
        final int id = messageInformation.getId();
        final String idString = Utils.convertToMinecraftId(id);
        MAP.put(id, messageInformation);

        final IChatComponent iChatComponent = new ChatComponentText("");
        for (final IChatComponent chatComponent : event.message) {
            final IChatComponent newIChatComponent = new ChatComponentText(chatComponent.getUnformattedTextForChat() + brea + idString);
            newIChatComponent.setChatStyle(chatComponent.getChatStyle().createShallowCopy());
            iChatComponent.appendSibling(newIChatComponent);
        }

        if (Setting.STACK_CHAT_MESSAGES.isEnabled()) {
            if (messageInformation.getCount() != 1) {
                iChatComponent.appendText(" §7(" + messageInformation.getCount() + ")" + brea + idString); // TODO check if the color looks nice
            }

            event.message = new ChatComponentText("");
            event.setCanceled(true);
            if (Setting.DONT_CHECK_USELESS_CHAT_MESSAGES.isEnabled()) {
                if (Utils.isIgnoredMessage(text)) {
                    // TODO do action ether dont do the (count) or just remove the whole message
                }
            }
            Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(iChatComponent, messageInformation.getId());
        } else {
            event.message = iChatComponent;
        }
    }

    @SubscribeEvent
    public void onChatTooltipRenderer(final RenderGameOverlayEvent.Post event) {
        if (event.type == RenderGameOverlayEvent.ElementType.CHAT) {
            if (Minecraft.getMinecraft().currentScreen instanceof GuiChat) {
                IChatComponent chatComponent = Minecraft.getMinecraft().ingameGUI.getChatGUI().getChatComponent(Mouse.getX(), Mouse.getY());
                if (chatComponent != null) {
                    MessageInformation messageInformation = getMessageInformation(chatComponent);
                    if (messageInformation == null) {
                        return;
                    }
                    int scaling = Minecraft.getMinecraft().gameSettings.guiScale;
                    int height = Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT;

                    int x = Minecraft.getMinecraft().ingameGUI.getChatGUI().getChatWidth() + 5;
                    int y = (Minecraft.getMinecraft().displayHeight - Mouse.getY()) / scaling;
                    y -= y % height + height;

                    messageInformation.drawTimeInfoBox(x, y, false);
                }
            }
        }
    }

    @SubscribeEvent
    public void onMouseInput(final GuiScreenEvent.MouseInputEvent event) {
        if (Setting.COPY_CHAT_ENABLED.isEnabled()) {
            IChatComponent chatComponent = getHoveredChatComponent();
            if (Mouse.isButtonDown(1)) {
                MessageInformation messageInformation = getMessageInformation(chatComponent);
                if (Keyboard.isKeyDown(29) || Keyboard.isKeyDown(157)) {
                    String messageToCopy;
                    if (messageInformation != null) {
                        messageToCopy = messageInformation.getMessage();
                        if (Setting.COPY_WITH_STACK.isEnabled() && messageInformation.getCount() != 1) {
                            messageToCopy += " (" + messageInformation.getCount() + ")";
                        }
                    } else {
                        messageToCopy = chatComponent.getUnformattedText().replaceAll("§[0-9a-zA-Z]", "");
                    }
                    Utils.copyToClipBoard(messageToCopy);
                } else if (Keyboard.isKeyDown(42) || Keyboard.isKeyDown(52)) {
                    Utils.sendPrivateMessage("Open the GUI here from textChatComponent -> " + (messageInformation != null ? messageInformation.getMessage() : chatComponent.getUnformattedTextForChat().replaceAll("§[0-9a-zA-Z]", "")));
                }
            }
        }
    }

    public static MessageInformation getMessageInformation(final IChatComponent chatComponent) {
        if (chatComponent != null) {
            final String[] strings = chatComponent.getUnformattedText().split(brea);
            if (strings.length > 1) {
                final String id = strings[strings.length - 1].replaceAll("§", "").replaceAll("[a-zA-Z]", "");
                int intId;
                try {
                    intId = Integer.parseInt(id);
                } catch (NumberFormatException e) {
                    return null;
                }
                return MAP.get(intId);
            }
        }

        return null;
    }

    public static IChatComponent getHoveredChatComponent() {
        return Minecraft.getMinecraft().ingameGUI.getChatGUI().getChatComponent(Mouse.getX(), Mouse.getY());
    }

    @Deprecated
    private void copyMessage() {
        if (Mouse.isButtonDown(1) && (Keyboard.isKeyDown(29) || Keyboard.isKeyDown(157) || Keyboard.isKeyDown(42) || Keyboard.isKeyDown(52))) {
            final Minecraft mc = Minecraft.getMinecraft();
            final int mouseX = Mouse.getEventX();
            final int mouseY = Mouse.getEventY();
            final IChatComponent chatComponent = mc.ingameGUI.getChatGUI().getChatComponent(mouseX, mouseY);
            if (chatComponent != null) {
                String messageToCopy;
                MessageInformation messageInformation = null;
                try {
                    final String[] strings = chatComponent.getUnformattedText().split(brea);
                    final String id = strings[strings.length - 1].replaceAll("§", "").replaceAll("[a-zA-Z]", "");
                    final int intId = Integer.parseInt(id);
                    messageInformation = MAP.get(intId);
                    messageToCopy = messageInformation.getMessage();
                    if (Setting.COPY_WITH_STACK.isEnabled() && messageInformation.getCount() != 1) {
                        messageToCopy += " (" + messageInformation.getCount() + ")";
                    }
                    //System.out.println(MessageInformation.instances.get(messageToCopy).getCount());
                    //System.out.println(MessageInformation.instances);
                    //System.out.println(MAP);
                } catch (Exception e) {
                    messageToCopy = chatComponent.getUnformattedText().replaceAll("§[0-9a-zA-Z]", "");
                }
                Utils.copyToClipBoard(messageToCopy);
                try {
                    System.out.println(messageInformation.getCount() + " " + messageInformation.getTime());
                } catch (Exception e) {

                }
            }
        }
    }

    public static class ChatCondition {
        private final String text;
        private final Condition condition;

        public ChatCondition(String text, Condition condition) {
            this.text = text;
            this.condition = condition;
        }

        public String getText() {
            return text;
        }

        public Condition getCondition() {
            return condition;
        }
    }

    public enum Condition {
        STARTSWITH {
            @Override
            public boolean check(String part, String text) {
                return text.startsWith(part);
            }
        },
        ENDSWITH {
            @Override
            public boolean check(String part, String text) {
                return text.endsWith(part);
            }
        },
        CONTAINS {
            @Override
            public boolean check(String part, String text) {
                return text.contains(part);
            }
        },
        EQUALS {
            @Override
            public boolean check(String part, String text) {
                return text.equals(part);
            }
        };

        public abstract boolean check(String part, String target);
    }

    @FunctionalInterface
    public interface Function {
        void action(IChatComponent message);
    }
}
