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
    /*
    @Author Johannes 10/8/2020
     */

    @Override
    public void handle(Player player, Packet<PacketListenerPlayIn> packet) {
        if (packet instanceof PacketPlayInFlying) {
            if(actionTracker.getLastAttackTicks() <= 1
                    && actionTracker.getTarget() != null
                    && !player.getGameMode().equals(GameMode.CREATIVE)
                    && playerData.getPreviousLocations().size() >= 10) {

                Vector origin = player.getEyeLocation().toVector();

                double reach = playerData.getPreviousLocations().stream().mapToDouble(loc -> {
                    double x = loc.getX();
                    double z = loc.getY();

                    float distanceX = (float) (origin.getX() - z);
                    float distanceZ = (float) (origin.getZ() - x);

                    return (distanceX - distanceZ) - 0.56569f;

                }).min().orElse(-1);

                if(reach > 3.3) {
                    if (++violations > 10) {
                        decreaseVl(5);
                        alert(player, String.format("R %.3f", reach));
                    }
                } else {
                    decreaseVl(1);
                }
            }
        }
    }
}
