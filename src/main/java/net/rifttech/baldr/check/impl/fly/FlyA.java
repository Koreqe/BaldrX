package net.rifttech.baldr.check.impl.fly;

import net.rifttech.baldr.check.type.movement.PositionCheck;
import net.rifttech.baldr.player.PlayerData;
import net.rifttech.baldr.player.tracker.impl.MovementTracker;
import net.rifttech.baldr.util.update.MovementUpdate;
import org.bukkit.entity.Player;

/**
 * This is a gravity check, it enforces the game physics (on the vertical axis) and compares the player vertical move
 * to our game physics enforced estimation from the last vertical move
 */
public class FlyA extends PositionCheck {
    private final MovementTracker movementTracker = playerData.getMovementTracker();

    private double lastOffsetY;

    public FlyA(PlayerData playerData) {
        super(playerData, "Fly A");
    }

    @Override
    public void handle(Player player, MovementUpdate update) {
        double offsetY = update.getTo().getY() - update.getFrom().getY();

        if (update.getTo().getY() % ON_GROUND == 0 || movementTracker.isTeleporting() || Math.abs(offsetY) + 0.0980000019 < 0.05)
            return;

        double estimatedOffsetY = (lastOffsetY * VERTICAL_AIR_FRICTION) - 0.08;

        if (Math.abs(estimatedOffsetY - offsetY) > 0.002 && !movementTracker.isTeleporting()) {
            if ((violations += 10) > 45) {
                alert(player, String.format("O %.3f", offsetY));
            }
        } else {
            decreaseVl(8);
        }

        lastOffsetY = offsetY;
    }
}
