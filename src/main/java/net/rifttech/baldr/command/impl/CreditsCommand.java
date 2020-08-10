package net.rifttech.baldr.command.impl;

import me.lucko.helper.command.context.CommandContext;
import net.rifttech.baldr.command.Command;
import net.rifttech.baldr.manager.AlertManager;
import org.bukkit.entity.Player;

public class CreditsCommand extends Command {
    public CreditsCommand() {
        super("credits", "shows credits");
    }

    @Override
    public void handle(Player sender, CommandContext<Player> ctx) {
        AlertManager alertManager = plugin.getAlertManager();

        alertManager.toggleAlerts(sender);

        boolean hasAlerts = alertManager.getAlerts().contains(sender.getUniqueId());
        sender.sendMessage("§4BaldrX by Simon & Johannes");
    }
}
