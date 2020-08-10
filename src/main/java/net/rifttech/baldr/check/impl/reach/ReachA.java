package net.rifttech.baldr.check.impl.reach;

import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketListenerPlayIn;
import net.minecraft.server.v1_8_R3.PacketPlayInFlying;
import net.minecraft.server.v1_8_R3.PacketPlayInUseEntity;
import net.rifttech.baldr.check.type.PacketCheck;
import net.rifttech.baldr.player.PlayerData;
import net.rifttech.baldr.player.tracker.impl.ActionTracker;
import net.rifttech.baldr.player.tracker.impl.ConnectionTracker;
import net.rifttech.baldr.player.tracker.impl.MovementTracker;
import net.rifttech.baldr.util.location.CustomLocation;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.stream.Collectors;

public class ReachA extends PacketCheck {

    private final ActionTracker actionTracker = playerData.getActionTracker();
    private final MovementTracker movementTracker = playerData.getMovementTracker();
    private final ConnectionTracker connectionTracker = playerData.getConnectionTracker();

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
                    && !movementTracker.isTeleporting()
                    && playerData.getEntityPastLocations().getPreviousLocations().size() >= 10) {

                Vector origin = player.getLocation().toVector();

                List<Vector> pastLocation = playerData.entityPastLocations.getEstimatedLocation(connectionTracker.getTransactionPing(), System.currentTimeMillis(), 125 + Math.abs(connectionTracker.getTransactionPing() - connectionTracker.getLastTransactionPing())).stream().map(CustomLocation::toVector).collect(Collectors.toList());

                double reach = (float) pastLocation.stream().mapToDouble(vec -> vec.clone().setY(0).distance(origin.clone().setY(0)) - 0.56569f).min().orElse(0); //Expecting hit was hit to diagonal point of hitbox

                reach -= 0.065; // just to make the check a bit more lenient

                if(reach > 3.0) {
                    if (++violations >= 5) {
                        alert(player, String.format("R %.3f", reach));
                    }
                } else {
                    decreaseVl(1);
                }
            }
        }
    }
}
