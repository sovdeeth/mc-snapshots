package com.sovdee.snapshots.textures;

import com.sovdee.snapshots.Side;
import com.sovdee.snapshots.Snapshots;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.EnumMap;

public class BlockTexture {

    private final EnumMap<Side, BufferedImage> textures;
    private final NamespacedKey key;

    public static @Nullable BlockTexture getTextureOf(@NotNull Material material) {
        NamespacedKey key = material.getKey();

        BlockTexture texture = new BlockTexture(key);
        boolean hasTexture = false;
        for (Side side : Side.values()) {
            InputStream imageStream = Snapshots.instance.getResource("textures/" + key.getKey() + side.getSuffix() + ".png");
            if (imageStream == null) continue;
            try {
                texture.addTexture(side, ImageIO.read(imageStream));
                hasTexture = true;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return hasTexture ? texture : null;
    }

    public BlockTexture(NamespacedKey key) {
        this.key = key;
        this.textures = new EnumMap<>(Side.class);
    }

    public void addTexture(Side side, BufferedImage texture) {
        textures.put(side, texture);
    }

    public BufferedImage getTexture(Side side, Block block) {
        BufferedImage image;
        do {
            image = textures.get(side);
            if (image != null) return image;
        } while ((side = side.lessPrecise()) != null);
        throw new IllegalStateException(key + " has no textures!");
    }

}
