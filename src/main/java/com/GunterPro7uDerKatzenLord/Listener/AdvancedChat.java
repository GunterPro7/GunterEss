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
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.util.HashMap;
import java.util.Map;

public class AdvancedChat {
    public static int current_id = 1;
    public static String lastJson;
    public static final Map<String, Long> jsonList = new HashMap<>();

    public static final Map<ChatCondition, Function> actionMap = Utils.createMap(ChatCondition.class, Function.class,
            new ChatCondition("QUICK MATHS! Solve: ", Condition.STARTSWITH), (Function) message -> Utils.sendChatMessageAsPlayer("/ac " + String.valueOf(MathUtils.eval(Utils.clearChatComponent(message.getUnformattedText()).substring("QUICK MATHS! Solve: ".length()).replaceAll("x", "*"))).replace(".0", "")),
            new ChatCondition("Click HERE to sign the ", Condition.STARTSWITH), (Function) message -> Utils.sendChatMessageAsPlayer(message.getChatStyle().getChatClickEvent().getValue()),
            new ChatCondition("[FEAR] Public Speaking Demon: Speak ", Condition.STARTSWITH), (Function) message -> Utils.sendChatMessageAsPlayer("/ac q weqwe qwe qwe qeqwe qweqwe qwe qwe ")
    );

    @SubscribeEvent
    public void onChatMessage(final ClientChatReceivedEvent event) {
        String unformattedText = event.message.getUnformattedText();

        if (unformattedText.matches("\\{.*}")) {
            if (!unformattedText.equals(lastJson)) {
                if (jsonList.containsKey(unformattedText)) {
                    long time = System.currentTimeMillis() - jsonList.get(unformattedText);
                    Utils.sendPrivateMessage("You already joined this server " + Utils.formatTime(time, "H'h' m'm' s's'") + " ago!");
                }
                jsonList.put(lastJson, System.currentTimeMillis());
            }
            lastJson = unformattedText;
            return;
        }
        if (event.type == 2 || event.message.getUnformattedText().matches("\\{.*}")) { // TODO we can probably read the json sent by the server: {"server":"mini95DK","gametype":"SKYBLOCK","mode":"dynamic","map":"Private Island"}
            return;
        }
        if (Setting.REMOVE_BLANK_LINES.isEnabled() && event.message.getUnformattedText().trim().equals("")) {
            event.message = new ChatComponentText("");
            return;
        }

        String text = Utils.clearChatComponent(unformattedText);

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

        MessageInformation messageInformation = MessageInformation.instances.computeIfAbsent(text, key -> new MessageInformation(key, current_id++));
        messageInformation.count();

        final IChatComponent iChatComponent = formatChatComponentForCopy(event.message, text);

        if (Setting.STACK_CHAT_MESSAGES.isEnabled()) {
            event.message = new ChatComponentText("");
            event.setCanceled(true);

            boolean addCount = true;
            if (Setting.DONT_CHECK_USELESS_CHAT_MESSAGES.isEnabled()) {
                if (Utils.isIgnoredMessage(text)) {
                    addCount = false;
                }
            }

            if (addCount) {
                if (messageInformation.getCount() != 1) {
                    iChatComponent.appendSibling(formatChatComponentForCopy(new ChatComponentText(" §7(" + messageInformation.getCount() + ")"), text));
                }
            }
            Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(iChatComponent, messageInformation.getId());
        } else {
            event.message = iChatComponent;
        }
    }

    public static IChatComponent formatChatComponentForCopy(IChatComponent message, String insertion) {
        final IChatComponent iChatComponent = new ChatComponentText("");
        for (final IChatComponent chatComponent : message) {
            final IChatComponent newIChatComponent = new ChatComponentText(chatComponent.getUnformattedTextForChat());
            newIChatComponent.setChatStyle(chatComponent.getChatStyle().setInsertion(insertion).createShallowCopy());
            iChatComponent.appendSibling(newIChatComponent);
        }

        return iChatComponent;
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
            if (chatComponent != null) {
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
                            messageToCopy = Utils.clearChatComponent(chatComponent.getUnformattedText());
                        }
                        Utils.copyToClipBoard(messageToCopy);
                    } else if (Keyboard.isKeyDown(42) || Keyboard.isKeyDown(52)) {
                        Utils.sendPrivateMessage("Open the GUI here from textChatComponent -> " + (messageInformation != null ? messageInformation.getMessage() : Utils.clearChatComponent(chatComponent.getUnformattedTextForChat())));
                        Utils.sendPrivateMessage(chatComponent.getChatStyle().getInsertion());

                    }
                }
            }
        }
    }

    public static MessageInformation getMessageInformation(final IChatComponent chatComponent) {
        if (chatComponent != null) {
            String insertion = chatComponent.getChatStyle().getInsertion();
            if (insertion != null) {
                return MessageInformation.instances.get(insertion);
            }
        }
        return null;
    }

    public static IChatComponent getHoveredChatComponent() {
        return Minecraft.getMinecraft().ingameGUI.getChatGUI().getChatComponent(Mouse.getX(), Mouse.getY());
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
