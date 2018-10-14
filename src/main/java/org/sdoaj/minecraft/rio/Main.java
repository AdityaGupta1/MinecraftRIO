package org.sdoaj.minecraft.rio;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.sdoaj.minecraft.rio.proxy.CommonProxy;

@Mod(modid = Main.MODID, name = Main.NAME, version = Main.VERSION)
public class Main {
    public static final String MODID = "minecraftrio";
    public static final String NAME = "MinecraftRIO";
    public static final String VERSION = "1.0";

    @SidedProxy(clientSide = "org.sdoaj.minecraft.rio.proxy.ClientProxy", serverSide = "org.sdoaj.minecraft.rio.proxy.ServerProxy")
    public static CommonProxy proxy;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit(event);
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
        // NetworkTablesClient.getInstance().run();
   }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }
}
