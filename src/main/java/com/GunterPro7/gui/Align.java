package com.GunterPro7.gui;

public enum Align {
    LEFT,
    MIDDLE,
    RIGHT;

    public static Align nextAlign(Align curAlign) {
        System.out.println(curAlign.ordinal());
        System.out.println("ordianal: " + curAlign.ordinal() + 1);
        return curAlign == null ? LEFT : values()[(curAlign.ordinal() + 1) % values().length];
    }
}
