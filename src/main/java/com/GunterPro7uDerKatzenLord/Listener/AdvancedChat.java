package com.GunterPro7uDerKatzenLord.Listener;

import com.GunterPro7uDerKatzenLord.Party;
import com.GunterPro7uDerKatzenLord.Setting;
import com.GunterPro7uDerKatzenLord.Utils.*;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.client.event.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.GunterPro7uDerKatzenLord.Main.mc;

public class AdvancedChat {
    public static int current_id = 1;
    public static String lastJson;
    public static final Map<String, Long> jsonList = new HashMap<>();
    private static boolean pasteEnabled;
    private static final AdvancedChat instance = new AdvancedChat();

    public static final Map<ChatCondition, Function> actionMap = Utils.createMap(ChatCondition.class, Function.class,
            new ChatCondition("QUICK MATHS! Solve: ", Condition.STARTSWITH), (Function) message -> AdvancedChat.sendChatMessageAsPlayer("/ac " + String.valueOf(MathUtils.eval(clearChatMessage(message.getUnformattedText()).substring("QUICK MATHS! Solve: ".length()).replaceAll("x", "*"))).replace(".0", "")),
            new ChatCondition("Click HERE to sign the ", Condition.STARTSWITH), (Function) message -> AdvancedChat.sendChatMessageAsPlayer(message.getChatStyle().getChatClickEvent().getValue()),
            new ChatCondition("[FEAR] Public Speaking Demon: Speak ", Condition.STARTSWITH), (Function) message -> AdvancedChat.sendChatMessageAsPlayer("/ac q weqwe qwe qwe qeqwe qweqwe qwe qwe ")
    );

    private AdvancedChat() {
    }

    public static AdvancedChat getInstance() {
        return instance;
    }

    @SubscribeEvent
    public void onChatMessage(final ClientChatReceivedEvent event) {
        String unformattedText = event.message.getUnformattedText();
        String formattedText = event.message.getFormattedText();

        if (unformattedText.matches("\\{.*}")) { // Example: {"server":"mini95DK","gametype":"SKYBLOCK","mode":"dynamic","map":"Private Island"}
            if (!unformattedText.equals(lastJson)) {
                if (jsonList.containsKey(unformattedText)) {
                    long time = System.currentTimeMillis() - jsonList.get(unformattedText);
                    sendPrivateMessage("You already joined this server " + Utils.formatTime(time, "H'h' m'm' s's'") + " ago!");
                }
                if (jsonList.size() == 0) {
                    MinecraftForge.EVENT_BUS.post(new EnteredSkyblockEvent());
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

        String text = clearChatMessage(unformattedText);

        // Do Chat Actions if specific message

        //for (Map.Entry<ChatCondition, Function> entry : actionMap.entrySet()) {
        //    ChatCondition key = entry.getKey();
//
        //    if (key.getCondition().check(key.getText(), text)) {
        //        entry.getValue().action(event.message);
        //        break;
        //    }
        //}

        // Change message to color if GunterEss configed
        boolean checkColors = text.endsWith("|GunterEss");

        // Change message so they can be copied afterwards

        MessageInformation messageInformation = MessageInformation.getInstances().computeIfAbsent(text, key -> new MessageInformation(key, formattedText.replaceAll("\\$", "\\$").replaceAll("§", "\\$"), current_id++));
        messageInformation.count();

        final IChatComponent iChatComponent = formatChatComponentForCopy(event.message, text, checkColors);

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
                    iChatComponent.appendSibling(formatChatComponentForCopy(new ChatComponentText(" §7(" + messageInformation.getCount() + ")"), text, checkColors));
                }
            }
            mc.ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(iChatComponent, messageInformation.getId());
        } else {
            event.message = iChatComponent;
        }

        Listeners.searchChat.setChatLine(iChatComponent, messageInformation.getId(), 0);
    }

    @SubscribeEvent
    public void messageCheck(ClientChatEvent event) throws IOException {
        String text = event.getText();
        if (Party.isAPartyToggled() && !text.startsWith("/")) {
            Party.getToggledParty().sendMessage(text);
        }
        if (!text.trim().isEmpty() && false) {
            event.setText(text + "|GunterEss");

            //String playerName = Minecraft.getMinecraft().thePlayer.getGameProfile().getName();
            //BackendService.getInstance().send(String.format("gmsg;%s;%s;%s" , playerName, System.currentTimeMillis(), text));
            // Syntax: gmsg | playername | timestamp | message
        }
        if (Setting.SEND_CHECK_FOR_7MESSAGE.isEnabled()) {
            if (text.startsWith("7") && text.length() > 1) {
                char secondChar = text.charAt(1);
                String command = "/" + text.substring(1);

                if (secondChar != ' ' && !Character.isDigit(secondChar)) {
                    IChatComponent iChatComponent = new ChatComponentText("§7Wrong Command Detected! ").appendSibling(
                            new ChatComponentText("§e[Send Anyway] ").setChatStyle(
                                    new ChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/gunterEss mcchat " + text)).setChatHoverEvent(
                                            new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ChatComponentText("§7Send the Message §r" + text + "§7 into the chat.")))

                            )).appendSibling(
                            new ChatComponentText("§6[Send As Command]").setChatStyle(
                                    new ChatStyle().setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/gunterEss mcchat " + command)).setChatHoverEvent(
                                            new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                                                    new ChatComponentText("§7Use the Command §r" + command + "§7.")))));

                    sendPrivateMessage(iChatComponent, true);
                    event.setCanceled(true);
                }
            }
        }
    }

    @SubscribeEvent
    public void onChatTooltipRenderer(final RenderGameOverlayEvent.Post event) {
        if (event.type == RenderGameOverlayEvent.ElementType.CHAT) {
            if (mc.currentScreen instanceof GuiChat) {
                IChatComponent chatComponent = getHoveredChatComponent();
                if (chatComponent != null) {
                    MessageInformation messageInformation = getMessageInformation(chatComponent);
                    if (messageInformation == null) {
                        return;
                    }
                    int scaling = mc.gameSettings.guiScale == 0 ? 4 : mc.gameSettings.guiScale;
                    int height = mc.fontRendererObj.FONT_HEIGHT;

                    int x = mc.ingameGUI.getChatGUI().getChatWidth() + 5;
                    int y = (mc.displayHeight - Mouse.getY()) / scaling;
                    y -= y % height + height;

                    messageInformation.drawTimeInfoBox(x, y, false);
                }
            }
        }
    }

    @SubscribeEvent
    public void onMouseInput(final GuiScreenEvent.MouseInputEvent event) throws IllegalAccessException, NoSuchFieldException {
        if (Setting.COPY_CHAT.isEnabled()) {
            IChatComponent chatComponent = getHoveredChatComponent();
            if (chatComponent != null) {
                if (Mouse.isButtonDown(1) || Mouse.isButtonDown(0)) {
                    MessageInformation messageInformation = getMessageInformation(chatComponent);
                    if ((Keyboard.isKeyDown(29) || Keyboard.isKeyDown(157)) || (Keyboard.isKeyDown(42) || Keyboard.isKeyDown(52))) {
                        String messageToCopy;
                        if (messageInformation != null) {
                            if ((Keyboard.isKeyDown(29) || Keyboard.isKeyDown(157)) && Mouse.isButtonDown(1)) {
                                messageToCopy = messageInformation.getColorMessage();
                            } else {
                                messageToCopy = messageInformation.getMessage();
                            }
                            if (Setting.COPY_WITH_STACK.isEnabled() && messageInformation.getCount() != 1) {
                                messageToCopy += " (" + ((Keyboard.isKeyDown(29) || Keyboard.isKeyDown(157)) && Mouse.isButtonDown(1) ? "$7" : "") + messageInformation.getCount() + ")";
                            }
                        } else {
                            messageToCopy = AdvancedChat.clearChatMessage(chatComponent.getUnformattedText());
                        }
                        if ((Keyboard.isKeyDown(42) || Keyboard.isKeyDown(52)) && Mouse.isButtonDown(1)) {
                            String message = messageToCopy.replaceAll("\\$[0-9a-zA-Z]", "");
                            Utils.copyToClipBoard(message.endsWith("|GunterEss") ? message.substring(0, message.length() - 10) : message);
                        } else if ((Keyboard.isKeyDown(29) || Keyboard.isKeyDown(157)) && Mouse.isButtonDown(1)) {
                            Utils.copyToClipBoard(messageToCopy);
                        }
                        if (Mouse.isButtonDown(0) && (Keyboard.isKeyDown(42) || Keyboard.isKeyDown(52))) {
                            Class<?> clazz = GuiChat.class;
                            Field privateField = clazz.getDeclaredField("field_146415_a");
                            privateField.setAccessible(true);
                            GuiTextField guiTextField = (GuiTextField) privateField.get(mc.currentScreen);

                            if (messageInformation != null && Setting.COPY_WITH_STACK.isEnabled()) {
                                // Check if it got copied already
                                if (pasteEnabled) {
                                    int count = messageInformation.getCount();
                                    if (count > 1)
                                        TimeUtils.addToQueue(1, () -> guiTextField.writeText(" (" + count + ")"));
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

    @SubscribeEvent
    public void onMessageSending(GuiScreenEvent.KeyboardInputEvent event) throws NoSuchFieldException, IllegalAccessException {
        if (Keyboard.isKeyDown(Keyboard.KEY_RETURN) || Keyboard.isKeyDown(Keyboard.KEY_NUMPADENTER)) {
            if (mc.currentScreen instanceof GuiChat) {
                MinecraftForge.EVENT_BUS.post(new ClientChatEvent(getTextField()));
            }
        } else if (Keyboard.isKeyDown(Keyboard.KEY_SECTION)) {
            sendPrivateMessage("now!");
        }

    }

    @SubscribeEvent
    public void beforeMessageSent(ClientChatEvent event) {
        //String text = event.getText();
        //if (!text.endsWith("|GunterEss")) {
        //    return;
        //}
        //// Originale Nachricht an backend
        //Map<String, String> args = new HashMap<>();
        //args.put("playerName", mc.thePlayer.getGameProfile().getName());
        //args.put("text", text);
        //args.put("time", String.valueOf(System.currentTimeMillis()));
        //JsonHelper.fetchToBackend("configuredMessages/addModifiedMessage.php", args);
//
        //// Change message for minecraft chat
        //text = text.replaceAll("\\$[0-9a-zA-Z]", "").replaceAll("\\$y.*\\$y", "");
        //event.setText(text);

        // Farbe mit normalen minecraft $.. (muss hier nicht umgeändert werden) // TODO während dem rendern des chat text-field's zu farben ändern
        // Hinterlegt mit z.b $z // TODO während man das schriebt schon hinterlegen
        // Link so: Hier der Link: $yhttps://google$xcom/cooltest$xetc$y. :D // TODO

        // TODO chatnachrichten sollen normal geschickt werden klnnen, chatnachricht wird zu meinen server geschickt, wenn es die selbe ist wie am anderen client, (also mein name und .contains) dann den part der .contains() wurde mit meiner geschickten nachricht erstezen
        // TODO nur das machen, wenn es unter 10 sekunden an den client gesendet wird
    }

    public void setTextFieldContent(String text) throws NoSuchFieldException, IllegalAccessException {
        getTextField().setText(text);
    }

    public GuiTextField getTextField() throws NoSuchFieldException, IllegalAccessException {
        if (mc.currentScreen instanceof GuiChat) {
            Class<?> clazz = GuiChat.class;
            Field privateField = clazz.getDeclaredField("field_146415_a");
            privateField.setAccessible(true);
            return (GuiTextField) privateField.get(mc.currentScreen);
        }
        return null;
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
        if (Listeners.searchChat.getChatOpen())
            return Listeners.searchChat.getChatComponent(Mouse.getX(), Mouse.getY());
        return mc.ingameGUI.getChatGUI().getChatComponent(Mouse.getX(), Mouse.getY());
    }

    public static IChatComponent formatChatComponentForCopy(IChatComponent message, String insertion, boolean updateColors) {
        final IChatComponent iChatComponent = new ChatComponentText("");
        for (final IChatComponent chatComponent : message) {
            String text = chatComponent.getUnformattedTextForChat();
            if (updateColors) {
                if (clearChatMessage(text).endsWith("|GunterEss")) {
                    text = text.replaceAll("\\|GunterEss", "");
                }
                text = text.replaceAll("(?!\\\\$)\\$", "§");
            }
            final IChatComponent newIChatComponent = new ChatComponentText(text);
            newIChatComponent.setChatStyle(chatComponent.getChatStyle().setInsertion(insertion).createShallowCopy());
            iChatComponent.appendSibling(newIChatComponent);
        }

        return iChatComponent;
    }

    @SubscribeEvent
    public void backendRecieved(BackendRecievedEvent event) {
        String text = event.getText();
        System.out.println(text);

        if (text.startsWith("msg")) {
            String[] parts = text.split(";");
            String playerFrom = parts[1];
            String message = String.join(" ", Arrays.copyOfRange(parts, 2, parts.length));
            sendPrivateMessage(playerFrom + " > " + message);
        } else if (text.startsWith("party")) {
            Party.processServerMessage(text.substring("party;".length()));
        }
    }

    // TODO change this #1
    private boolean backendOffline;

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) throws IOException {
        BackendService backendService = BackendService.getInstance();
        if (backendService != null) {
            backendService.run();
        }
        if (backendService == null && !backendOffline) {
            if (mc.thePlayer != null) {
                try {
                    new BackendService(new Socket("49.12.101.156", 5000));
                    //new BackendService(new Socket("localhost", 5000));    // Testing
                    BackendService.getInstance().send("init;" + mc.thePlayer.getGameProfile().getName());
                } catch (IOException e) {
                    backendOffline = true;
                    System.out.println("Backend Offline! It hopefully will be back soon! (It will be checked again next restart)");
                }
            }
        } // TODO erkennen falls verbindung zum server verloren geht, dann entsprechend reagieren und schließen und in x sekunden neue verbindung aufbauen
    }

    public static void sendChatMessageAsPlayer(String text) {
        mc.thePlayer.sendChatMessage(text);
    }

    public static String clearChatMessage(String text) {
        return text.replaceAll("§[0-9a-zA-Z]", "");
    }

    public static void sendPrivateMessage(String text) {
        sendPrivateMessage(new ChatComponentText(text), true);
    }

    public static void sendPrivateMessage(IChatComponent iChatComponent, boolean geMark) {
        if (geMark) {
            iChatComponent = new ChatComponentText("§a§lGunterEss > §r").appendSibling(iChatComponent);
        }
        mc.thePlayer.addChatMessage(AdvancedChat.formatChatComponentForCopy(iChatComponent, AdvancedChat.clearChatMessage(iChatComponent.getUnformattedText()), false));
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

    @FunctionalInterface
    public interface Callback {
        void run();
    }
}
