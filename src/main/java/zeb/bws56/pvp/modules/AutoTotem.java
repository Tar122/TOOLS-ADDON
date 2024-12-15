package zeb.bws56.pvp.modules;

import zeb.bws56.pvp.Addon;
import meteordevelopment.meteorclient.events.packets.PacketEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.player.FindItemResult;
import meteordevelopment.orbit.EventHandler;
import meteordevelopment.orbit.EventPriority;
import net.minecraft.client.MinecraftClient;
import meteordevelopment.meteorclient.utils.player.InvUtils;
import meteordevelopment.meteorclient.settings.SettingGroup;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.entity.Entity;
import net.minecraft.item.Items;
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket;
import zeb.bws56.pvp.util.TimerUtil;

public class AutoTotem extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();
    private final Setting<Boolean> OpenInventory = sgGeneral.add(new BoolSetting.Builder()
        .name("OpenInventory")
        .description("Automatically open inventory to equip totem in offhand.")
        .defaultValue(false)
        .build()
    );

    private final Setting<Boolean> CloseInventory = sgGeneral.add(new BoolSetting.Builder()
        .name("CloseInventory")
        .description("Automatically close inventory after equipping totem.")
        .defaultValue(false)
        .build()
    );

    private final Setting<Integer> delay = sgGeneral.add(new IntSetting.Builder()
        .name("delay")
        .description("The delay in ticks between slot movements.")
        .defaultValue(0)
        .min(0)
        .build()
    );

    private boolean locked;
    private final TimerUtil timer = new TimerUtil();

    public AutoTotem() {
        super(Addon.CATEGORY, "auto-totem", "Automatically equips a totem in your offhand.");
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onTick(TickEvent.Pre event) {
        if (!mc.isWindowFocused() || !locked || !timer.hasPassed(delay.get())) {
        }
        FindItemResult result = InvUtils.find(Items.TOTEM_OF_UNDYING);
        if (result.count() <= 0) {
            return;
        }

        // ทำงานตามการตั้งค่า OpenInventory และ CloseInventory
        if (OpenInventory.get()) {
            if (locked && mc.player.getOffHandStack().getItem() != Items.TOTEM_OF_UNDYING) {
                mc.getTutorialManager().onInventoryOpened();// แจ้งว่าเปิดหน้าจอกระเป๋า\
                if (mc.player != null) {
                    mc.setScreen(new InventoryScreen(mc.player));
                }
                InvUtils.move().from(result.slot()).toOffhand();  // ย้าย Totem ไปที่ออฟแฮนด์
            }
        }


        if (CloseInventory.get()) {

            if (mc.currentScreen instanceof InventoryScreen) {
                if (mc.player != null) {
                    mc.player.closeScreen();  // ปิดหน้าจอหลังจากย้าย Totem
                }
            }
        }
        if (!timer.hasPassed(150)) {
            locked = false;
        }

    }


    @EventHandler(priority = EventPriority.HIGH)
    private void onReceivePacket(PacketEvent.Receive event) {
        if (!(event.packet instanceof EntityStatusS2CPacket p)) return;
        if (p.getStatus() != 35) return;

        Entity entity = p.getEntity(mc.world);
        if (entity == null || !(entity.equals(mc.player))) return;

        // รีเซ็ต locked เมื่อได้รับสถานะจากแพ็กเก็ตที่บอกว่าผู้เล่นได้รับความเสียหาย
        locked = true;
    }
}
