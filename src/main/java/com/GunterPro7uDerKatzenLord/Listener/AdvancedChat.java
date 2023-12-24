package com.GunterPro7uDerKatzenLord.Listener;

import com.GunterPro7uDerKatzenLord.Party;
import com.GunterPro7uDerKatzenLord.Setting;
import com.GunterPro7uDerKatzenLord.Utils.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.ChatComponentText;
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

        String text = clearChatComponent(unformattedText);

        // Do Chat Actions if specific message

        System.out.println(actionMap);

        for (Map.Entry<ChatCondition, Function> entry : actionMap.entrySet()) {
            ChatCondition key = entry.getKey();

            if (key.getCondition().check(key.getText(), text)) {
                entry.getValue().action(event.message);
                break;
            }
        }

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
            Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessageWithOptionalDeletion(iChatComponent, messageInformation.getId());
        } else {
            event.message = iChatComponent;
        }

        Listeners.searchChat.setChatLine(iChatComponent, messageInformation.getId(), 0);
    }

    @SubscribeEvent
    public void messageCheck(ClientChatEvent event) throws IOException {
        String text = event.getText();
        if (Party.isAPartyToggled()) {
            Party.getToggledParty().sendMessage(text);
        }
        if (!text.trim().isEmpty() && Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            event.setText(text + "|GunterEss");
        }
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
                            if ((Keyboard.isKeyDown(29) || Keyboard.isKeyDown(157)) && Mouse.isButtonDown(1)) {
                                messageToCopy = messageInformation.getColorMessage();
                            } else {
                                messageToCopy = messageInformation.getMessage();
                            }
                            if (Setting.COPY_WITH_STACK.isEnabled() && messageInformation.getCount() != 1) {
                                messageToCopy += " (" + ((Keyboard.isKeyDown(29) || Keyboard.isKeyDown(157)) && Mouse.isButtonDown(1) ? "&7" : "") + messageInformation.getCount() + ")";
                            }
                        } else {
                            messageToCopy = AdvancedChat.clearChatComponent(chatComponent.getUnformattedText());
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
                            GuiTextField guiTextField = (GuiTextField) privateField.get(Minecraft.getMinecraft().currentScreen);

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
            if (Minecraft.getMinecraft().currentScreen instanceof GuiChat) {
                MinecraftForge.EVENT_BUS.post(new ClientChatEvent(getTextField()));
            }
        } else if (Keyboard.isKeyDown(Keyboard.KEY_SECTION)) {
            sendPrivateMessage("now!");
        }
    }

    @SubscribeEvent
    public void beforeMessageSent(ClientChatEvent event) {
        String text = event.getText();
        if (!text.endsWith("|GunterEss")) {
            return;
        }
        // Originale Nachricht an backend
        Map<String, String> args = new HashMap<>();
        args.put("playerName", Minecraft.getMinecraft().thePlayer.getGameProfile().getName());
        args.put("text", text);
        args.put("time", String.valueOf(System.currentTimeMillis()));
        JsonHelper.fetchToBackend("configuredMessages/addModifiedMessage.php", args);

        // Change message for minecraft chat
        text = text.replaceAll("\\$[0-9a-zA-Z]", "").replaceAll("\\$y.*\\$y", "");
        event.setText(text);

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
        if (Minecraft.getMinecraft().currentScreen instanceof GuiChat) {
            Class<?> clazz = GuiChat.class;
            Field privateField = clazz.getDeclaredField("field_146415_a");
            privateField.setAccessible(true);
            return (GuiTextField) privateField.get(Minecraft.getMinecraft().currentScreen);
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
        return Minecraft.getMinecraft().ingameGUI.getChatGUI().getChatComponent(Mouse.getX(), Mouse.getY());
    }

    public static IChatComponent formatChatComponentForCopy(IChatComponent message, String insertion, boolean updateColors) {
        final IChatComponent iChatComponent = new ChatComponentText("");
        for (final IChatComponent chatComponent : message) {
            String text = chatComponent.getUnformattedTextForChat();
            if (updateColors) {
                if (clearChatComponent(text).endsWith("|GunterEss")) {
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
    private boolean alej;

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) throws IOException {
        BackendService backendService = BackendService.getInstance();
        if (backendService != null) {
            backendService.run();
        }
        if (Minecraft.getMinecraft().isIntegratedServerRunning()) {
            if (backendService == null) {
                // TODO change this #1
                if (!alej) {
                    alej = true;
                    Utils.execute(() -> {
                        try {
                            //new BackendService(new Socket("49.12.101.156", 5000));
                            new BackendService(new Socket("localhost", 5000));
                            BackendService.getInstance().send("init;" + Minecraft.getMinecraft().thePlayer.getGameProfile().getName());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }, 2000);
                }
            } // TODO erkennen falls verbindung zum server verloren geht, dann entsprechend reagieren und schließen und in x sekunden neue verbindung aufbauen
        }
    }

    public static void sendChatMessageAsPlayer(String text) {
        Minecraft.getMinecraft().thePlayer.sendChatMessage(text);
    }

    public static String clearChatComponent(String text) {
        return text.replaceAll("§[0-9a-zA-Z]", "");
    }

    public static void sendPrivateMessage(String text) {
        sendPrivateMessage(new ChatComponentText(text));
    }

    public static void sendPrivateMessage(IChatComponent iChatComponent) {
        String string = "§a§lGunterEss > §r" + iChatComponent.getFormattedText(); // TODO this shit here will not work lmfao
        Minecraft.getMinecraft().thePlayer.addChatMessage(AdvancedChat.formatChatComponentForCopy(new ChatComponentText(string), AdvancedChat.clearChatComponent(string), false));
    }

    public void doBackendLoop() {
        AdvancedChat.sendPrivateMessage(JsonHelper.fetchToBackend("configuredMessages/loopForMessages.php", new HashMap<>(), () -> {

        }));

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
