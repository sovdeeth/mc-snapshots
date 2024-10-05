package com.sovdee.snapshots;

import org.bukkit.block.BlockFace;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public enum Side {
    DEFAULT(""),
    SIDE("_side"),
    TOP("_top"),
    BOTTOM("_bottom"),
    FRONT("_front"),
    BACK("_back");

    private final String suffix;

    Side(String suffix) {
        this.suffix = suffix;
    }

    public String getSuffix() {
        return suffix;
    }

    @Contract(pure = true)
    public @Nullable Side lessPrecise() {
        return switch (this) {
            case TOP, BOTTOM, SIDE -> DEFAULT;
            case BACK, FRONT -> SIDE;
            case DEFAULT -> null;
        };
    }

    @Contract(pure = true)
    public static Side fromBlockFace(@NotNull BlockFace face) {
        return switch (face) {
            case UP -> TOP;
            case DOWN -> BOTTOM;
            case SELF -> DEFAULT;
            default -> SIDE;
        };
    }
}
