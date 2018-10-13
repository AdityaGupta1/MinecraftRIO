package org.sdoaj.minecraft.rio.blocks;

import net.minecraft.block.material.Material;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

public class ModBlocks {
    public static final List<BlockWithModel> blocks = new ArrayList<>();

    public static final BlockBasic power_cube = new BlockBasic("power_cube", Material.CLOTH,
            0.8f, 4.0f, "pickaxe", 1);

    @SideOnly(Side.CLIENT)
    public static void initModels() {
        for (BlockWithModel block : blocks) {
            block.initModel();
        }
    }
}