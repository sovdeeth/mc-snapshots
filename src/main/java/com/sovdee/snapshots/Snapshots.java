package com.sovdee.snapshots;

import com.sovdee.snapshots.textures.BlockTexture;
import com.sovdee.snapshots.textures.Textures;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

public final class Snapshots extends JavaPlugin {

    public static Snapshots instance;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;

        BlockTexture.getTextureOf(Material.BEEHIVE);

        Textures.getTexture(Material.BARREL);

        return;

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        instance = null;
    }
}
