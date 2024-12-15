package zeb.bws56.pvp.util;

import meteordevelopment.meteorclient.MeteorClient;
import meteordevelopment.meteorclient.mixininterface.IClientPlayerInteractionManager;
import meteordevelopment.meteorclient.utils.player.FindItemResult;
import meteordevelopment.meteorclient.utils.player.InvUtils;
import meteordevelopment.meteorclient.utils.player.SlotUtils;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.SlotActionType;

import static meteordevelopment.meteorclient.MeteorClient.mc;

public class InventoryUtil {


    public static int findBlock(Block block) {

        for (int i = 0; i < 9; i++) {
            ItemStack stack = mc.player.getInventory().getStack(i);
            if (stack.getItem() instanceof BlockItem) {
                BlockItem blockItem = (BlockItem) stack.getItem();
                if (blockItem.getBlock() == block) {
                    return i;
                }
            }
        }
        return -1;
    }
}
