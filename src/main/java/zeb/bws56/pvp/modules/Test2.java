package zeb.bws56.pvp.modules;

import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.player.InvUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.block.Blocks;
import net.minecraft.item.Items;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import zeb.bws56.pvp.Addon;
import zeb.bws56.pvp.util.InventoryUtil;

public class Test2 extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    public Test2() {
        super(Addon.CATEGORY, "Test2", "Module for testing different modes");
    }

    private final Setting<Mode> mode = sgGeneral.add(new EnumSetting.Builder<Mode>()
        .name("mode")
        .description("Select the mode")
        .defaultValue(Mode.Normal)
        .build()
    );

    private final Setting<Integer> delay = sgGeneral.add(new IntSetting.Builder()
        .name("delay")
        .description("Delay between each action")
        .defaultValue(0)
        .min(0)
        .max(200)
        .build()
    );

    private final Setting<Boolean> switchBack = sgGeneral.add(new BoolSetting.Builder()
        .name("Switch Back")
        .description("Switch back to the original slot after use")
        .visible(() -> mode.get() == Mode.Glowstone)
        .defaultValue(true)
        .build()
    );

    private final Setting<Integer> switchSlot = sgGeneral.add(new IntSetting.Builder()
        .name("Switch Slot")
        .description("Slot to switch to when using glowstone")
        .defaultValue(0)
        .visible(() -> mode.get() == Mode.Glowstone)
        .min(0)
        .max(9)
        .build()
    );

    public int progress;

    public enum Mode {
        Normal,
        Glowstone
    }

    @EventHandler
    public void onTick(TickEvent.Post event) {

        if (mc.currentScreen != null || mc.isWindowFocused()) {
            return;
        }

        int SlotRESPAWN_ANCHOR = InventoryUtil.findBlock(Blocks.RESPAWN_ANCHOR);
        int SlotGLOWSTONE = InventoryUtil.findBlock(Blocks.GLOWSTONE);

        if (mode.get() == Mode.Normal) {
            if (progress == 0) {
                if (InvUtils.find(Items.RESPAWN_ANCHOR) != null) {
                    mc.player.getInventory().selectedSlot = SlotRESPAWN_ANCHOR;
                    increaseProgress();
                } else {
                    toggle();
                }
            } else if (progress == 1) {
                if (mc.crosshairTarget instanceof BlockHitResult blockHitResult &&
                    !mc.world.getBlockState(blockHitResult.getBlockPos()).isAir()) {
                    mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND, blockHitResult);
                    mc.player.swingHand(Hand.MAIN_HAND);
                    increaseProgress();
                }
            } else if (progress == 2) {
                if (InvUtils.find(Items.GLOWSTONE) != null) {
                    mc.player.getInventory().selectedSlot = SlotGLOWSTONE;
                    increaseProgress();
                } else {
                    toggle();
                }
            } else if (progress == 3) {
                if (mc.crosshairTarget instanceof BlockHitResult blockHitResult &&
                    mc.world.getBlockState(blockHitResult.getBlockPos()).getBlock() == Blocks.RESPAWN_ANCHOR) {
                    mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND, blockHitResult);
                    mc.player.swingHand(Hand.MAIN_HAND);
                    increaseProgress();
                }
            } else if (progress == 4) {
                toggle();
            }

        } else if (mode.get() == Mode.Glowstone) {
            if (progress == 0) {
                if (mc.crosshairTarget instanceof BlockHitResult blockHitResult &&
                    mc.world.getBlockState(blockHitResult.getBlockPos()).getBlock() == Blocks.RESPAWN_ANCHOR &&
                    mc.world.getBlockState(blockHitResult.getBlockPos()).get(Properties.CHARGES) == 0) {
                    if (InvUtils.find(Items.GLOWSTONE) != null) {
                        mc.player.getInventory().selectedSlot = SlotGLOWSTONE;
                        increaseProgress();
                    } else {
                        toggle();
                    }
                }
            } else if (progress == 1) {

                if (mc.crosshairTarget instanceof BlockHitResult blockHitResult &&
                    mc.world.getBlockState(blockHitResult.getBlockPos()).getBlock() == Blocks.RESPAWN_ANCHOR) {
                    mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND, blockHitResult);
                    mc.player.swingHand(Hand.MAIN_HAND);
                    increaseProgress();
                }
                if (switchBack.get()) {
                    mc.player.getInventory().selectedSlot = switchSlot.get() - 1;
                    progress = 0;
                }
            } else if (progress == 2) {
                if (switchBack.get()) {
                    mc.player.getInventory().selectedSlot = switchSlot.get() - 1;
                    progress = 0;
                }
            }
        }
    }

    private void increaseProgress() {
        progress += 1;
    }
}
