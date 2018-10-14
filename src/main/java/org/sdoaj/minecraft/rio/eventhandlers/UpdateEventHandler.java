package org.sdoaj.minecraft.rio.eventhandlers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.sdoaj.minecraft.rio.NetworkTablesClient;
import org.sdoaj.minecraft.rio.blocks.ModBlocks;

@Mod.EventBusSubscriber
public class UpdateEventHandler {
    private static NetworkTablesClient table = NetworkTablesClient.getInstance();

    private static ItemStack[] inventory = new ItemStack[36];

    static {
        inventory[0] = new ItemStack(Item.getItemFromBlock(ModBlocks.power_cube));
    }

    @SubscribeEvent
    public static void update(LivingEvent.LivingUpdateEvent event) {
        if (!(event.getEntity() instanceof EntityPlayer)) {
            return;
        }

        EntityPlayer player = (EntityPlayer) event.getEntity();

        for (int i = 0; i < 36; i++) {
            player.inventory.setInventorySlotContents(i, inventory[i] == null ? ItemStack.EMPTY : inventory[i].copy());
        }

        ItemStack intakeItemStack = new ItemStack(Blocks.WOOL, 1, 15);
        // String intakeState = table.getValue("intake state").getString();
        String intakeState = "intaking";
        switch(intakeState) {
            case "intaking":
                intakeItemStack = new ItemStack(Items.REDSTONE);
                break;
            case "has cube":
                intakeItemStack = new ItemStack(ModBlocks.power_cube);
                break;
        }
        intakeItemStack.setStackDisplayName(intakeState);
        inventory[0] = intakeItemStack;

        // double batteryVoltage = table.getValue("battery voltage").getDouble();
        double batteryVoltage = 12.7;
        double voltageAbove12 = batteryVoltage - Math.floor(batteryVoltage);
        for (int i = 0; i < 9; i++) {
            inventory[9 + i] = new ItemStack(Blocks.WOOL, 1, voltageAbove12 >= i * 0.1 ? 5 : 14)
                    .setStackDisplayName("battery voltage = " + batteryVoltage);
        }
    }
}
