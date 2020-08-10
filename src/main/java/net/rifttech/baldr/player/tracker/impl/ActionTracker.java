package net.rifttech.baldr.player.tracker.impl;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.server.v1_8_R3.PacketPlayInBlockDig;
import net.minecraft.server.v1_8_R3.PacketPlayInEntityAction;
import net.minecraft.server.v1_8_R3.PacketPlayInUseEntity;
import net.rifttech.baldr.Baldr;
import net.rifttech.baldr.player.PlayerData;
import net.rifttech.baldr.player.tracker.PlayerTracker;
import net.rifttech.baldr.wrapper.impl.serverbound.UseEntityWrapper;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

@Getter
public class ActionTracker extends PlayerTracker {
    private boolean digging;

    private boolean sprinting;
    private boolean sneaking;

    private PlayerData lastTargetData;

    private Player target;
    @Setter
    private int lastAttackTicks;

    public ActionTracker(Player player, PlayerData playerData) {
        super(player, playerData);
    }

    public void handleUseEntity(PacketPlayInUseEntity packet) {
        if (packet.a() != PacketPlayInUseEntity.EnumEntityUseAction.ATTACK)
            return;

        UseEntityWrapper useEntityWrapper = new UseEntityWrapper(packet);

        net.minecraft.server.v1_8_R3.Entity nmsEntity = ((CraftWorld) player.getWorld()).getHandle().a(useEntityWrapper.getId());

        Entity entity = nmsEntity == null ? null : nmsEntity.getBukkitEntity();

        if (entity instanceof Player) {
            Player entityPlayer = (Player) entity;

            target = entityPlayer;

            lastAttackTicks = 0;

            lastTargetData = Baldr.getInstance().getPlayerDataManager().getData(entityPlayer);
        } else {
            // We don't care about non player entities
            lastTargetData = null;
            target = null;
        }

    }

    // This way of handling block digging should be remade, only used here as an example
    public void handleBlockDig(PacketPlayInBlockDig blockDig) {
        switch (blockDig.c()) {
            case START_DESTROY_BLOCK:
                digging = true;
                break;

            case STOP_DESTROY_BLOCK:
            case ABORT_DESTROY_BLOCK:
                digging = false;
                break;
        }
    }
    public void handleEntityAction(PacketPlayInEntityAction entityAction) {
        switch (entityAction.b()) {
            case START_SNEAKING:
                sneaking = true;
                break;
            case STOP_SNEAKING:
                sneaking = false;
                break;
            case START_SPRINTING:
                sprinting = true;
                break;
            case STOP_SPRINTING:
                sprinting = false;
                break;
        }
    }
}
