package zeb.bws56.pvp.modules;

import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.IntSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.player.InvUtils;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import zeb.bws56.pvp.Addon;

import static meteordevelopment.meteorclient.MeteorClient.mc;

public class AnchorExploder extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    public AnchorExploder() {
        super(Addon.CATEGORY, "AnchorExploder", "Explodes anchors with glowstone in them when looking at them");
    }

    private final Setting<Integer> delay = sgGeneral.add(new IntSetting.Builder()
        .name("delay")
        .description("Delay between each action")
        .defaultValue(0)
        .min(0)
        .max(200)
        .build()
    );

    private final Setting<Integer> switchTo = sgGeneral.add(new IntSetting.Builder()
        .name("switchto")
        .description("Slot that will be switched to to explode the anchor")
        .defaultValue(0)
        .min(0)
        .max(9)
        .build()
    );



    @EventHandler
    public void onTick(TickEvent.Post event) {
        if (mc.currentScreen != null || mc.isWindowFocused()) {
            if (mc.player.getMainHandStack().getItem() == Items.SHIELD && mc.player.isUsingItem()) {
                return;
            }


            int slot = switchTo.get();


            HitResult hitResult = mc.crosshairTarget;
            if (hitResult != null && hitResult.getType() == HitResult.Type.BLOCK && hitResult instanceof BlockHitResult blockHitResult) {
                BlockState blockState = mc.world.getBlockState(blockHitResult.getBlockPos());
                if (blockState.getBlock() == Blocks.RESPAWN_ANCHOR && blockState.get(Properties.CHARGES) != 0) {
                    if (mc.player.getMainHandStack().getItem() == Items.GLOWSTONE) {
                        InvUtils.swap(slot, false);
                    }


                    mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND, blockHitResult);
                    mc.player.swingHand(Hand.MAIN_HAND);
                }
            }
        }
    }
}
