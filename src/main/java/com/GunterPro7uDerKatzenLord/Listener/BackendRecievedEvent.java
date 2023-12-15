package com.GunterPro7uDerKatzenLord.Listener;

import net.minecraftforge.fml.common.eventhandler.Event;

public class BackendRecievedEvent extends Event {
    private final String text;

    public BackendRecievedEvent(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
