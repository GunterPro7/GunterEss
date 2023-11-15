package com.GunterPro7uDerKatzenLord;

import com.GunterPro7uDerKatzenLord.Listener.AdvancedChat;
import com.GunterPro7uDerKatzenLord.Listener.ClientBlockListener;
import com.GunterPro7uDerKatzenLord.Listener.Listeners;
import com.GunterPro7uDerKatzenLord.Utils.JsonHelper;
import com.GunterPro7uDerKatzenLord.Utils.Utils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.Mixins;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static com.GunterPro7uDerKatzenLord.Listener.Listeners.collectionJson;

@Mod(modid = "GunterEss", useMetadata = true)
public class Main {
    public static final Minecraft mc = Minecraft.getMinecraft();
    public static boolean starting = true;
    public static final String VERSION = "1.2";
    public static final boolean DEV = false;
    public static File configDirectory;
    public static File gunterEssDelFile;

    @Mod.EventHandler
    public void preServerStarting(final FMLPreInitializationEvent event) throws IOException {
        MixinBootstrap.init();
        Mixins.addConfiguration("mixins.GunterEss.json");
        configDirectory = event.getModConfigurationDirectory();
        File gunterEssDirectory = new File(configDirectory.getAbsolutePath() + "/GunterEss/");
        if (!gunterEssDirectory.exists()) gunterEssDirectory.mkdir();

        gunterEssDelFile = new File(gunterEssDirectory.getAbsolutePath() + "/GunterEssDel.java");
        if (!gunterEssDelFile.exists()) gunterEssDelFile.createNewFile();

        File gunterEssJar = Loader.instance().activeModContainer().getSource();

        final String autoDeletionFileContent = "import javax.swing.*;\n" +
                "import java.io.File;\n" +
                "import java.io.IOException;\n" +
                "import java.io.InputStreamReader;\n" +
                "import java.io.BufferedReader;\n" +
                "public class GunterEssDel {\n" +
                "    public static void main(String[] args) {\n" +
                "        try {\n" +
                "            String command = System.getProperty(\"os.name\").startsWith(\"Windows\") ? \"tasklist\" : \"ps aux\";\n" +
                "            Process process = Runtime.getRuntime().exec(command);\n" +
                "            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));\n" +
                "            String line;\n" +
                "            while ((line = reader.readLine()) != null) {\n" +
                "                if (line.contains(\"javaw.exe\")) {\n" +
                "                    int pid = Integer.parseInt(line.replaceAll(\"\\\\s+\", \" \").split(\" \")[1]);\n" +
                "                    if (checkIfRealMinecraft(pid)) runLoopUntilDead(pid);\n" +
                "                }\n" +
                "            }\n" +
                "        } catch (IOException | InterruptedException ignore) {}\n" +
                "    }\n" +
                "    private static void runLoopUntilDead(int pid) throws InterruptedException, IOException {\n" +
                "        String command = \"tasklist /fi \\\"pid eq \" + pid + \"\\\"\";\n" +
                "        while (true) {\n" +
                "            Thread.sleep(1000);\n" +
                "            Process process = Runtime.getRuntime().exec(command);\n" +
                "            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));\n" +
                "            reader.readLine();reader.readLine();reader.readLine();\n" +
                "            String line = reader.readLine();\n" +
                "            if (line == null) {\n" +
                "                SwingUtilities.invokeLater(AutoDeletion::new);\n" +
                "                return;\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "    private static boolean checkIfRealMinecraft(int pid) throws IOException {\n" +
                "        String command = \"tasklist /fi \\\"pid eq \" + pid + \"\\\" /v\";\n" +
                "        Process process = Runtime.getRuntime().exec(command);\n" +
                "        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));\n" +
                "        reader.readLine();reader.readLine();reader.readLine();\n" +
                "        String line = reader.readLine();\n" +
                "        return line.contains(\"Minecraft\") || line.contains(\"1.8.9\") || line.contains(\"LabyMod\");\n" +
                "    }\n" +
                "    public static class AutoDeletion extends JFrame {\n" +
                "        public AutoDeletion() {\n" +
                "            boolean deleted = new File(\"" + gunterEssJar.getAbsolutePath().replaceAll("\\\\", "\\\\\\\\") + "\").delete();\n" +
                "            if (deleted) {\n" +
                "                setTitle(\"GunterEss deletion task!\");\n" +
                "                setLayout(new java.awt.FlowLayout());\n" +
                "                JLabel label = new JLabel(\"Deletion successful! \\nClosing in 3 sec...\");\n" +
                "                add(label);\n" +
                "                setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);\n" +
                "                setSize(350, 100);\n" +
                "                setLocationRelativeTo(null);\n" +
                "                setVisible(true);\n" +
                "                Timer timer = new Timer(3000, e -> dispose());\n" +
                "                timer.setRepeats(false);\n" +
                "                timer.start();\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "}";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(gunterEssDelFile))) {
            writer.write(autoDeletionFileContent);
        }
    }


    @Mod.EventHandler
    public void serverStarting(final FMLInitializationEvent event) throws IOException {
        System.out.println("INITIALIZING GunterEss :D");

        MinecraftForge.EVENT_BUS.register(new ClientBlockListener());
        MinecraftForge.EVENT_BUS.register(new Listeners());
        MinecraftForge.EVENT_BUS.register(AdvancedChat.getInstance());
        ClientCommandHandler.instance.registerCommand(new Command());
        if (Setting.COLLECTION_OVERLAY.isEnabled()) {
            try {
                JsonHelper.collectionApiFetch();
                collectionJson = JsonHelper.fetch("https://api.hypixel.net/resources/skyblock/collections");
            } catch (Exception e) {
                System.out.println("No network, retrying soon!");
            }

            if (collectionJson != null) {
                JsonObject jsonObject = new Gson().fromJson(collectionJson, JsonObject.class);
                JsonHelper.addEntriesToEnum(jsonObject);
            }
        }

        final String response;
        try {
            response = JsonHelper.fetch("http://49.12.101.156/GunterEss/VersionCheck.php?VERSION=" + VERSION + "&DEV=" + DEV);
        } catch (IOException e) {
            System.out.println("Backend Service not available");
            return;
        }

        boolean updateAvailable = Boolean.parseBoolean(response.split("\"update_available\":")[1].split(",")[0]);
        if (updateAvailable) {
            String newVersion = response.split("\"newest_version\":")[1].split("}")[0].replaceAll("\"", "");
            JsonHelper.downloadFile("http://49.12.101.156/GunterEss/latest.jar", "mods/GunterEss-" + newVersion + ".jar");
            Runtime.getRuntime().exec("cmd.exe /c java " + gunterEssDelFile.getAbsolutePath());
        }
    }
}

