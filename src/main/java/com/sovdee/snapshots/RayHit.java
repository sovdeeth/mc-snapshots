package com.sovdee.snapshots;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.image.BufferedImage;

public class RayHit {

    private final Vector uv;
    private final Block hitBlock;
    private final BlockFace hitFace;

    public RayHit(@NotNull RayTraceResult rayTraceResult) {

        Vector hitPos = rayTraceResult.getHitPosition();
        hitFace = rayTraceResult.getHitBlockFace();
        hitBlock = rayTraceResult.getHitBlock();
        assert hitBlock != null;
        Vector hitBlockPos = hitBlock.getLocation().toVector();

        // assume full side
        Vector bfNormal = hitFace.getDirection();
        hitPos.subtract(hitBlockPos);
        Vector offset = rejection(hitPos, bfNormal);
        uv = switch (hitFace) {
            case WEST -> new Vector(offset.getZ(), 1-offset.getY(), 0);
            case EAST -> new Vector(1-offset.getZ(), 1-offset.getY(), 0);
            case NORTH -> new Vector(1-offset.getX(), 1-offset.getY(), 0);
            case SOUTH -> new Vector(offset.getX(), 1-offset.getY(), 0);
            case UP, DOWN -> new Vector(1-offset.getX(), 1-offset.getZ(), 0);
            default -> throw new IllegalArgumentException("blockface " + hitFace + " invalid");
        };

        // scale to 16x16
        uv.multiply(16);
        uv.setX(Math.floor(uv.getX()));
        uv.setY(Math.floor(uv.getY()));
    }

    public Vector getUv() {
        return uv;
    }

    Color getPixel() {
        BufferedImage texture = Textures.getTexture(hitBlock.getType()).getTexture(Side.fromBlockFace(hitFace));
        return new Color(texture.getRGB((int) uv.getX(), (int) uv.getY()));
    }

    @Contract(value = "_, _ -> new", pure = true)
    Vector rejection(@NotNull Vector a, @NotNull Vector b) {
        // a - (a*b/b*b)b
        return a.clone().subtract(b.clone().multiply(a.dot(b) / b.dot(b)));
    }

}
