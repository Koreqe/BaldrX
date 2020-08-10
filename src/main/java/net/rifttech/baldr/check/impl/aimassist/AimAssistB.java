package net.rifttech.baldr.check.impl.aimassist;

import net.rifttech.baldr.check.type.movement.RotationCheck;
import net.rifttech.baldr.player.PlayerData;
import net.rifttech.baldr.player.tracker.impl.ActionTracker;
import net.rifttech.baldr.util.update.MovementUpdate;
import org.bukkit.entity.Player;

public class AimAssistB extends RotationCheck {

    private final ActionTracker actionTracker = playerData.getActionTracker();

    public AimAssistB(PlayerData playerData) {
        super(playerData, "AimAssist B");
    }

    /*
    @Author Johannes 10/8/2020
     */

    @Override
    public void handle(Player player, MovementUpdate update) {

        float diffYaw = Math.abs(update.getTo().getYaw() - update.getFrom().getYaw());
        float diffPitch = Math.abs(update.getTo().getPitch() - update.getFrom().getPitch());

        if(actionTracker.getLastAttackTicks() > 5) return;

        if(diffYaw > 4.0F && diffPitch > 0.01 && diffPitch < 0.2) {
            if(++violations > 5) {
                alert(player, String.format("YAW %.1f PITCH %.1f", diffYaw, diffPitch));
            }
        } else decreaseVl(2);
    }
}
