package org.sdoaj.minecraft.rio.proxy;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import org.sdoaj.minecraft.rio.blocks.BlockWithModel;
import org.sdoaj.minecraft.rio.blocks.ModBlocks;

@Mod.EventBusSubscriber
public class CommonProxy {
    public void preInit(FMLPreInitializationEvent event) {}

    public void init(FMLInitializationEvent event) {}

    public void postInit(FMLPostInitializationEvent event) {}

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        IForgeRegistry<Block> registry = event.getRegistry();

        registry.registerAll(ModBlocks.blocks.toArray(new Block[0]));
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        IForgeRegistry<Item> registry = event.getRegistry();
        registerItemBlocks(registry);
    }

    private static void registerItemBlocks(IForgeRegistry<Item> registry) {
        for (BlockWithModel blockWithModel : ModBlocks.blocks) {
            Block block = (Block) blockWithModel;
            registry.register(new ItemBlock(block).setRegistryName(block.getRegistryName()));
        }
    }
}
