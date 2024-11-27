package com.sovdee.snapshots.textures.specialcases;

import com.sovdee.snapshots.Side;
import com.sovdee.snapshots.Snapshots;
import com.sovdee.snapshots.textures.BlockTexture;
import com.sovdee.snapshots.textures.Textures;
import net.minecraft.world.level.GrassColor;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.block.CraftBiome;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.IOException;
import java.io.InputStream;

public class GrassTexture extends BlockTexture {

    private final BufferedImage sideOverlay;
    private final BufferedImage snowySide;

    public GrassTexture(NamespacedKey key) {
        super(key);
        try {
            InputStream imageStream = Snapshots.instance.getResource("textures/grass_block_side.png");
            assert imageStream != null;
            addTexture(Side.SIDE, ImageIO.read(imageStream));

            imageStream = Snapshots.instance.getResource("textures/grass_block_top.png");
            assert imageStream != null;
            addTexture(Side.TOP, ImageIO.read(imageStream));

            imageStream = Snapshots.instance.getResource("textures/dirt.png");
            assert imageStream != null;
            addTexture(Side.BOTTOM, ImageIO.read(imageStream));

            imageStream = Snapshots.instance.getResource("textures/grass_block_side_overlay.png");
            assert imageStream != null;
            sideOverlay = ImageIO.read(imageStream);

            imageStream = Snapshots.instance.getResource("textures/grass_block_snow.png");
            assert imageStream != null;
            snowySide = ImageIO.read(imageStream);

            imageStream = Snapshots.instance.getResource("textures/grass_color_map.png");
            assert imageStream != null;
            BufferedImage grass_map = ImageIO.read(imageStream);
            if (grass_map.getType() != BufferedImage.TYPE_INT_ARGB) {
                BufferedImage tmp = new BufferedImage(grass_map.getWidth(), grass_map.getHeight(), BufferedImage.TYPE_INT_ARGB);
                tmp.getGraphics().drawImage(grass_map, 0, 0, null);
                grass_map = tmp;
            }
            GrassColor.init(((DataBufferInt)grass_map.getRaster().getDataBuffer()).getData());
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        Textures.textureCache.put(key, this);
    }

    @Override
    public BufferedImage getTexture(Side side, Block block) {
        if (side == Side.SIDE) {
            // tint overlay
            int tint_rgb = CraftBiome.bukkitToMinecraft(block.getBiome()).getGrassColor(0,0) & 0x00FFFFFF;
            BufferedImage tinted_overlay = new BufferedImage(sideOverlay.getWidth(), sideOverlay.getHeight(),2);
            for (int x = 0; x < tinted_overlay.getWidth(); x++) {
                for (int y = 0; y < tinted_overlay.getHeight(); y++) {
                    if ( sideOverlay.getRGB(x,y) >> 24 > 0)
                        tinted_overlay.setRGB(x, y, 0xFF000000);
                    tinted_overlay.setRGB(x, y, (sideOverlay.getRGB(x,y) + tint_rgb) & 0x00FFFFFF);
                    Particle
                }
            }
            // merge with side
            BufferedImage tinted_side = new BufferedImage(sideOverlay.getWidth(), sideOverlay.getHeight(),1);
            Graphics graphics = tinted_side.getGraphics();
            graphics.drawImage(super.getTexture(side, block), 0, 0, null);
            graphics.drawImage(tinted_overlay, 0, 0, null);
            return tinted_side;
        } else if (side == Side.TOP) {
            // tint texture
            BufferedImage top = super.getTexture(side, block);
            BufferedImage tinted_top = new BufferedImage(top.getWidth(), top.getHeight(),1);
            int tint_rgb = CraftBiome.bukkitToMinecraft(block.getBiome()).getGrassColor(0, 0);
            for (int x = 0; x < tinted_top.getWidth(); x++) {
                for (int y = 0; y < tinted_top.getHeight(); y++) {
                    tinted_top.setRGB(x, y, (top.getRGB(x,y) + tint_rgb) & 0x00FFFFFF);
                }
            }
            return tinted_top;
        } else {
            return super.getTexture(side, block);
        }
    }
}
