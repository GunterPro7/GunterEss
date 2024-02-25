package com.GunterPro7uDerKatzenLord;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class PreventLabymodUpdater {
    public static final PreventLabymodUpdater INSTANCE = new PreventLabymodUpdater();
    private static final File file = new File("LabyMod\\updater.jar");
    private boolean fileDeleted;
    private PreventLabymodUpdater() {
    }

    @SubscribeEvent
    public void checkForLabymodUpdater(TickEvent.ClientTickEvent event) {
        if (!fileDeleted && file.exists()) {
            boolean deleted = file.delete();
            if (deleted) {
                fileDeleted = true;
                System.out.println("Successfully deleted LabyMod Auto Updater! :)");
            }
        }
    }
}
