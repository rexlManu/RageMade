package de.rexlmanu.ragemade.nicksystem.commands;
/*
* Class created by rexlManu
* Twitter: @rexlManu | Website: rexlManu.de
* Coded with IntelliJ
*/

import de.rexlmanu.ragemade.nicksystem.NickSystem;
import de.rexlmanu.ragemade.nicksystem.events.NickSystemPlayerUnnickEvent;
import de.rexlmanu.ragemade.nicksystem.object.NickManager;
import de.rexlmanu.ragemade.nicksystem.utils.UUIDFetcher;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class UnnickCommand extends Command {
    public UnnickCommand(String name) {
        super(name);
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {
        if (!(commandSender instanceof Player)) return true;
        Player player = (Player) commandSender;
        if (!player.hasPermission("nicksystem.use")) {
            player.sendMessage(NickSystem.PREFIX + "§cDazu hast du keine Berechtigung.");
            return true;
        }
        if (!NickManager.nicked_players.contains(player)) {
            player.sendMessage(NickSystem.PREFIX + "§7Du bist nicht genickt.");
        } else {
            CraftPlayer cp = (CraftPlayer) player;
            String nickname = player.getDisplayName();
            String name = UUIDFetcher.getName(player.getUniqueId());
            NickManager nickManager = NickSystem.getInstance().getNickManager();
            nickManager.setName(cp, name);
            nickManager.refreshPlayer(cp);
            nickManager.changeSkin(cp, name);
            player.setDisplayName(name);
            NickSystemPlayerUnnickEvent nickSystemPlayerUnnickEvent = new NickSystemPlayerUnnickEvent(player, nickname);
            Bukkit.getPluginManager().callEvent(nickSystemPlayerUnnickEvent);
            player.sendMessage(NickSystem.PREFIX + "§7Du bist nun nicht mehr genickt.");
        }
        return false;
    }
}
