package net.rifttech.baldr.check.impl.reach;

import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketListenerPlayIn;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import net.minecraft.server.v1_8_R3.PacketPlayInUseEntity;
import net.rifttech.baldr.check.type.PacketCheck;
import net.rifttech.baldr.player.PlayerData;
import net.rifttech.baldr.player.tracker.impl.ActionTracker;
import net.rifttech.baldr.player.tracker.impl.MovementTracker;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class ReachA extends PacketCheck {

    private final ActionTracker actionTracker = playerData.getActionTracker();

    public ReachA(PlayerData playerData) {
        super(playerData, "Reach A");
    }

    @Override
    public void handle(Player player, Packet<PacketListenerPlayIn> packet) {
        if (packet instanceof PacketPlayInFlying) {
            if(actionTracker.getLastAttackTicks() <= 1 && !player.getGameMode().equals(GameMode.CREATIVE)) {

                Player target = actionTracker.getTarget();

                float distanceX = (float) (player.getEyeLocation().getX() - target.getEyeLocation().getX());
                float distanceZ = (float) (player.getEyeLocation().getZ() - target.getEyeLocation().getZ());

                float reach = (distanceX - distanceZ) - 0.56569f;

                if(reach > 3) {
                    if ((violations += 10) > 45) {
                        alert(player, String.format("R %.3f", reach));
                    }
                } else {
                    decreaseVl(8);
                }
            }
        }
    }
}
