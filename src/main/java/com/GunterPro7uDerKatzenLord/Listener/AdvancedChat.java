package com.GunterPro7uDerKatzenLord.Listener;

import com.GunterPro7uDerKatzenLord.Setting;
import com.GunterPro7uDerKatzenLord.Utils.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.client.event.*;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class AdvancedChat {
    public static int current_id = 1;
    public static String lastJson;
    public static final Map<String, Long> jsonList = new HashMap<>();
    private static boolean pasteEnabled;
    private static final AdvancedChat instance = new AdvancedChat();

    public static final Map<ChatCondition, Function> actionMap = Utils.createMap(ChatCondition.class, Function.class,
            new ChatCondition("QUICK MATHS! Solve: ", Condition.STARTSWITH), (Function) message -> AdvancedChat.sendChatMessageAsPlayer("/ac " + String.valueOf(MathUtils.eval(clearChatComponent(message.getUnformattedText()).substring("QUICK MATHS! Solve: ".length()).replaceAll("x", "*"))).replace(".0", "")),
            new ChatCondition("Click HERE to sign the ", Condition.STARTSWITH), (Function) message -> AdvancedChat.sendChatMessageAsPlayer(message.getChatStyle().getChatClickEvent().getValue()),
            new ChatCondition("[FEAR] Public Speaking Demon: Speak ", Condition.STARTSWITH), (Function) message -> AdvancedChat.sendChatMessageAsPlayer("/ac q weqwe qwe qwe qeqwe qweqwe qwe qwe ")
    );

    private AdvancedChat() {}

    public static AdvancedChat getInstance() {
        return instance;
    }

    @SubscribeEvent
    public void onChatMessage(final ClientChatReceivedEvent event) {
        String unformattedText = event.message.getUnformattedText();

        if (unformattedText.matches("\\{.*}")) { // Example: {"server":"mini95DK","gametype":"SKYBLOCK","mode":"dynamic","map":"Private Island"}
            if (!unformattedText.equals(lastJson)) {
                if (jsonList.containsKey(unformattedText)) {
                    long time = System.currentTimeMillis() - jsonList.get(unformattedText);
                    sendPrivateMessage("You already joined this server " + Utils.formatTime(time, "H'h' m'm' s's'") + " ago!");
                }
                jsonList.put(lastJson, System.currentTimeMillis());
            }
            lastJson = unformattedText;
            return;
        }
        if (event.type == 2) {
            return;
        }
        if (Setting.REMOVE_BLANK_LINES.isEnabled() && event.message.getUnformattedText().trim().equals("")) {
            event.message = new ChatComponentText("");
            return;
        }

        String text = AdvancedChat.clearChatComponent(unformattedText);

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

        MessageInformation messageInformation = MessageInformation.getInstances().computeIfAbsent(text, key -> new MessageInformation(key, current_id++));
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

        Listeners.searchChat.setChatLine(iChatComponent, messageInformation.getId(), 0);
    }

    @SubscribeEvent
    public void onChatTooltipRenderer(final RenderGameOverlayEvent.Post event) {
        if (event.type == RenderGameOverlayEvent.ElementType.CHAT) {
            if (Minecraft.getMinecraft().currentScreen instanceof GuiChat) {
                IChatComponent chatComponent = getHoveredChatComponent();
                if (chatComponent != null) {
                    MessageInformation messageInformation = getMessageInformation(chatComponent);
                    if (messageInformation == null) {
                        return;
                    }
                    int scaling = Minecraft.getMinecraft().gameSettings.guiScale == 0 ? 4 : Minecraft.getMinecraft().gameSettings.guiScale;
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
    public void onMouseInput(final GuiScreenEvent.MouseInputEvent event) throws IllegalAccessException, NoSuchFieldException {
        if (Setting.COPY_CHAT_ENABLED.isEnabled()) {
            IChatComponent chatComponent = getHoveredChatComponent();
            if (chatComponent != null) {
                if (Mouse.isButtonDown(1) || Mouse.isButtonDown(0)) {
                    MessageInformation messageInformation = getMessageInformation(chatComponent);
                    if ((Keyboard.isKeyDown(29) || Keyboard.isKeyDown(157)) || (Keyboard.isKeyDown(42) || Keyboard.isKeyDown(52))) {
                        String messageToCopy;
                        if (messageInformation != null) {
                            messageToCopy = messageInformation.getMessage();
                            if (Setting.COPY_WITH_STACK.isEnabled() && messageInformation.getCount() != 1) {
                                messageToCopy += " (" + messageInformation.getCount() + ")";
                            }
                        } else {
                            messageToCopy = AdvancedChat.clearChatComponent(chatComponent.getUnformattedText());
                        }
                        if ((Keyboard.isKeyDown(42) || Keyboard.isKeyDown(52)) && Mouse.isButtonDown(1)) {
                            //sendPrivateMessage("Open the GUI here from textChatComponent -> " + (messageInformation != null ? messageInformation.getMessage() : AdvancedChat.clearChatComponent(chatComponent.getUnformattedTextForChat())));
                            //sendPrivateMessage(chatComponent.getChatStyle().getInsertion());
                        } else if ((Keyboard.isKeyDown(29) || Keyboard.isKeyDown(157)) && Mouse.isButtonDown(1)) {
                            Utils.copyToClipBoard(messageToCopy);
                        } if (Mouse.isButtonDown(0) && (Keyboard.isKeyDown(42) || Keyboard.isKeyDown(52))) {
                            Class<?> clazz = GuiChat.class;
                            Field privateField = clazz.getDeclaredField("field_146415_a");
                            privateField.setAccessible(true);
                            GuiTextField guiTextField = (GuiTextField) privateField.get(Minecraft.getMinecraft().currentScreen);

                            if (messageInformation != null && Setting.COPY_WITH_STACK.isEnabled()) {
                                // Check if it got copied already
                                if (pasteEnabled) {
                                    int count = messageInformation.getCount();
                                    if (count > 1) TimeUtils.addToQueue(1, () -> guiTextField.writeText(" (" + count + ")"));
                                    pasteEnabled = false;
                                }
                            } else {
                                guiTextField.writeText(messageToCopy);
                            }
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void checkForMouseClick(TickEvent.ClientTickEvent event) {
        if (!Mouse.isButtonDown(0)) {
            pasteEnabled = true;
        }
    }

    public static MessageInformation getMessageInformation(final IChatComponent chatComponent) {
        if (chatComponent != null) {
            String insertion = chatComponent.getChatStyle().getInsertion();
            if (insertion != null) {
                return MessageInformation.getInstance(insertion);
            }
        }
        return null;
    }

    public static IChatComponent getHoveredChatComponent() {
        if (Listeners.searchChat.getChatOpen()) return Listeners.searchChat.getChatComponent(Mouse.getX(), Mouse.getY());
        return Minecraft.getMinecraft().ingameGUI.getChatGUI().getChatComponent(Mouse.getX(), Mouse.getY());
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

    public static void sendChatMessageAsPlayer(String text) {
        Minecraft.getMinecraft().thePlayer.sendChatMessage(text);
    }

    public static String clearChatComponent(String text) {
        return text.replaceAll("§[0-9a-zA-Z]", "");
    }

    public static void sendPrivateMessage(String text) {
        String string = "§a§lGunterEss > §r" + text;
        Minecraft.getMinecraft().thePlayer.addChatMessage(AdvancedChat.formatChatComponentForCopy(new ChatComponentText(string), AdvancedChat.clearChatComponent(string)));
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
