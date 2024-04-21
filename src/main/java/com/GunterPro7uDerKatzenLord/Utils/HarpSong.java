package com.GunterPro7uDerKatzenLord.Utils;

public enum HarpSong {
    HYMN_TO_THE_JOY("Hymn to the Joy"),
    FRERE_Jacques("Frère Jacques"),
    AMAZING_GRACE("Amazing Grace"),
    BRAHMS_LULLABY("Brahm's Lullaby"),
    HAPPY_BIRTHDAY_TO_YOU("Happy Birthday to You"),
    Greensleeves("Greensleeves"),
    Geothermy("Geothermy?"),
    Minuet("Minuet"),
    JOY_TO_THE_WORLD("Joy to the World"),
    GODLY_IMAGINATION("Godly Imagination"),
    LA_VIE_EN_ROSE("La Vie en Rose"),
    THROUGH_THE_CAMPFIRE("Through the Campfire"),
    PACHELBEL("Pachelbel"),
    ;

    private final String name;
    /** How long we should wait when we get a block above it (subtracted by ping) */
    private final int interval;

    HarpSong(String name) {
        this(name, 1000);
    }

    HarpSong(String name, int interval) {
        this.name = name;
        this.interval = interval;
    }

    public String getName() {
        return name;
    }

    public int getInterval() {
        return interval;
    }

    public static HarpSong valueOfName(String name) {
        for (HarpSong song : values()) {
            if (song.name.equals(name)) {
                return song;
            }
        }

        return null;
    }
}
