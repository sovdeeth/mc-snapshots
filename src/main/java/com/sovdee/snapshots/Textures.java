package com.sovdee.snapshots;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ConcurrentHashMap;

public class Textures {

    public static final ConcurrentHashMap<NamespacedKey, BlockTexture> textureCache = new ConcurrentHashMap<>();

    public static @NotNull BlockTexture getTexture(@NotNull Material material) {
        BlockTexture texture = textureCache.get(material.getKey());
        if (texture != null) return texture;

        synchronized (textureCache) {
            // check if populated while waiting
            texture = textureCache.get(material.getKey());
            if (texture != null) return texture;

            texture = BlockTexture.getTextureOf(material);
            if (texture == null) throw new IllegalArgumentException("Material " + material + " has no textures.");
            textureCache.put(material.getKey(), texture);
            return texture;
        }
    }

}
