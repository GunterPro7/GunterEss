package com.GunterPro7;

import com.GunterPro7.listener.Listener;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.io.File;

public class PreventLabymodUpdater implements Listener {
    private static final PreventLabymodUpdater INSTANCE = new PreventLabymodUpdater();
    private static final File file = new File("LabyMod\\updater.jar");
    private boolean fileDeleted;
    private PreventLabymodUpdater() {
    }

    public static PreventLabymodUpdater getInstance() {
        return INSTANCE;
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
