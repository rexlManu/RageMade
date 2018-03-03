package de.rexlmanu.ragemade.nicksystem.commands;
/*
* Class created by rexlManu
* Twitter: @rexlManu | Website: rexlManu.de
* Coded with IntelliJ
*/

import de.rexlmanu.ragemade.nicksystem.NickSystem;
import de.rexlmanu.ragemade.nicksystem.events.NickSystemUpdateNameTagEvent;
import de.rexlmanu.ragemade.nicksystem.object.NickManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class NickCommand extends Command {
    public NickCommand(String name) {
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
        if (NickManager.nicked_players.contains(player)) {
            player.sendMessage(NickSystem.PREFIX + "§7Du bist bereits genickt.");
        } else {
            NickManager nickManager = NickSystem.getInstance().getNickManager();
            String randomName = nickManager.getRandomName();
            CraftPlayer craftPlayer = (CraftPlayer) player;
            nickManager.setName(craftPlayer, randomName);
            nickManager.refreshPlayer(craftPlayer);
            nickManager.changeSkin(craftPlayer, randomName);
            player.setDisplayName(randomName);
            NickManager.nicked_players.add(player);
            NickSystemUpdateNameTagEvent nickSystemUpdateNameTagEvent = new NickSystemUpdateNameTagEvent(player, randomName);
            Bukkit.getPluginManager().callEvent(nickSystemUpdateNameTagEvent);
            player.sendMessage(NickSystem.PREFIX + "§7Du bist nun genickt als §5" + randomName + "§7.");
        }
        return false;
    }
}
