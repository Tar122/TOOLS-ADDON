package zeb.bws56.pvp.modules;

import meteordevelopment.meteorclient.mixininterface.IPlayerInteractEntityC2SPacket;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import org.jetbrains.annotations.NotNull;
import zeb.bws56.pvp.Addon;
import net.minecraft.client.MinecraftClient;
import zeb.bws56.pvp.event.PacketEvent;

import static net.minecraft.entity.effect.StatusEffects.WEAKNESS;

public class CrystalOptimizer extends Module {

    // Constructor for the module, initializes the module with a name, category and description
    public CrystalOptimizer() {
        super(Addon.CATEGORY, "CrystalOptimizer", "Does not wait for server-side confirmation when breaking crystals");
    }

    private final MinecraftClient mc = MinecraftClient.getInstance();

    @EventHandler
    public void onUpdate() {
        Entity entity;
        HitResult hitResult = mc.crosshairTarget;
        if (hitResult == null) {
            return;
        }
        if (hitResult.getType() == HitResult.Type.ENTITY && (entity = ((EntityHitResult) hitResult).getEntity()) instanceof EndCrystalEntity) {
            if (entity instanceof EndCrystalEntity ) {
                entity.kill();
                entity.setRemoved(Entity.RemovalReason.KILLED);
                entity.onRemoved();
            }

        }
    }
}
