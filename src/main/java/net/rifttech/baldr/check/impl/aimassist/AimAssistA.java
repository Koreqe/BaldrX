package net.rifttech.baldr.check.impl.aimassist;

import net.rifttech.baldr.check.type.movement.RotationCheck;
import net.rifttech.baldr.player.PlayerData;
import net.rifttech.baldr.player.tracker.impl.ActionTracker;
import net.rifttech.baldr.util.update.MovementUpdate;
import org.bukkit.entity.Player;

public class AimAssistA extends RotationCheck {

    private final ActionTracker actionTracker = playerData.getActionTracker();

    public AimAssistA(PlayerData playerData) {
        super(playerData, "AimAssist A");
    }

    /*
    @Author Johannes 10/8/2020
     */

    @Override
    public void handle(Player player, MovementUpdate update) {

        float diffYaw = Math.abs(update.getTo().getYaw() - update.getFrom().getYaw());

        if(actionTracker.getLastAttackTicks() > 3) return;

        if(diffYaw % 1.0 == 0.0 && diffYaw != 0) {
            if(++violations > 5) {
                alert(player, String.format("YAW %.1f", diffYaw));
            }
        } else decreaseVl(1);
    }
}
