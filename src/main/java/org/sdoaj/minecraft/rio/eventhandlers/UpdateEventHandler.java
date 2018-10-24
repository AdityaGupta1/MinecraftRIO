package org.sdoaj.minecraft.rio.eventhandlers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.sdoaj.minecraft.rio.NetworkTablesClient;
import org.sdoaj.minecraft.rio.blocks.ModBlocks;
import org.sdoaj.minecraft.rio.constants.Constants;
import org.sdoaj.minecraft.rio.constants.InventoryConstants;

import java.util.Date;

@Mod.EventBusSubscriber
public class UpdateEventHandler {
    private static NetworkTablesClient table = NetworkTablesClient.getInstance();

    private static ItemStack[] inventory = new ItemStack[36];

    private static double minPitch = 0;
    private static double maxPitch = 50;

    @SubscribeEvent
    public static void update(LivingEvent.LivingUpdateEvent event) {
        if (!(event.getEntity() instanceof EntityPlayer)) {
            return;
        }

        EntityPlayer player = (EntityPlayer) event.getEntity();

        if (player.isSprinting()) {
            player.setSprinting(false);
        }

        // set inventory slots
        for (int i = 0; i < 36; i++) {
            player.inventory.setInventorySlotContents(i, inventory[i] == null ? ItemStack.EMPTY : inventory[i].copy());
        }

        // intake state (not intaking / intaking / has cube)

        String intakeState = table.get("intake state").getString();
        ItemStack intakeItemStack;
        switch(intakeState) {
            case "intaking":
                intakeItemStack = new ItemStack(Items.REDSTONE);
                break;
            case "has cube":
                intakeItemStack = new ItemStack(ModBlocks.POWER_CUBE);
                break;
            default:
                intakeItemStack = new ItemStack(Blocks.WOOL, 1, 15);
                break;
        }
        intakeItemStack.setStackDisplayName(intakeState);
        inventory[InventoryConstants.intakeSlot] = intakeItemStack;

        // battery voltage --> wool meter

        double batteryVoltage = table.get("battery voltage").getDouble();
        double voltageAbove12 = batteryVoltage - Math.floor(batteryVoltage);
        for (int i = 0; i < 9; i++) {
            inventory[9 + i] = new ItemStack(Blocks.WOOL, 1, voltageAbove12 >= i * 0.1 ? 5 : 14)
                    .setStackDisplayName("battery voltage = " + batteryVoltage);
        }

        // pitch --> intake angle

        if (player.rotationPitch < minPitch) {
            player.rotationPitch = (float) minPitch;
        }

        if (player.rotationPitch > maxPitch) {
            player.rotationPitch = (float) maxPitch;
        }

        table.set("wrist angle", player.rotationPitch);

        // forward, strafe, rotation

        double[] motion = getMotion(player);
        table.set("forward velocity", motion[0]);
        table.set("strafe velocity", motion[1]);
        table.set("azimuth setpoint", motion[2]);
    }

    private static BlockPos previousPosition;
    private static double previousTime;

    /**
     * Transforms movement to player-oriented (e.g. moving forward at 90 degrees yaw means forward movement, not strafe movement)
     * @return {forward velocity, strafe velocity, azimuth setpoint}
     */
    private static double[] getMotion(EntityPlayer player) {
        if (previousPosition == null) {
            previousPosition = player.getPosition();
            previousTime = getSeconds();
            return new double[]{0, 0, 0};
        }

        BlockPos currentPosition = player.getPosition();
        double currentAzimuth = player.rotationYaw;
        double currentTime = getSeconds();

        double[] deltaXZ = {currentPosition.getX() - previousPosition.getX(),
                currentPosition.getZ() - previousPosition.getZ()};
        double hypot = Math.sqrt(Math.pow(deltaXZ[0], 2) + Math.pow(deltaXZ[1], 2));
        double angle = Math.atan2(deltaXZ[1], deltaXZ[0]);
        double transformedAngle = angle - currentAzimuth;
        double[] transformedDeltaXZ = {hypot * Math.cos(transformedAngle), hypot * Math.sin(transformedAngle)};

        double deltaTime = currentTime - previousTime;

        previousPosition = currentPosition;
        previousTime = currentTime;

        double[] motion = {transformedDeltaXZ[0], transformedDeltaXZ[1], currentAzimuth};
        for (int i = 0; i < 2; i++) {
            motion[i] /= deltaTime;
            motion[i] /= Constants.maxWalkingSpeed;
        }
        return motion;
    }

    private static double getSeconds() {
        return new Date().getTime() / 1000.0;
    }

    @SubscribeEvent
    public static void shoot(PlayerInteractEvent event) {
        if (table.get("shoot").getBoolean()) {
            return; // return if robot already knows it has to shoot
        }

        if (event.getItemStack().getItem() != Item.getItemFromBlock(ModBlocks.POWER_CUBE)) {
            return;
        }

        EntityPlayer player = event.getEntityPlayer();

        if (player.inventory.currentItem != InventoryConstants.intakeSlot) {
            return;
        }

        table.set("shoot", true);
    }
}
