package de.rexlmanu.ragemade.nicksystem.listeners.player;
/*
* Class created by rexlManu
* Twitter: @rexlManu | Website: rexlManu.de
* Coded with IntelliJ
*/

import de.rexlmanu.ragemade.nicksystem.NickSystem;
import de.rexlmanu.ragemade.nicksystem.events.NickSystemAbfragePlayerEvent;
import de.rexlmanu.ragemade.nicksystem.events.NickSystemUpdateNameTagEvent;
import de.rexlmanu.ragemade.nicksystem.object.NickManager;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void on(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        NickManager nickManager = NickSystem.getInstance().getNickManager();
        if (player.hasPermission("nicksystem.use")) {
            NickSystemAbfragePlayerEvent nickSystemAbfragePlayerEvent = new NickSystemAbfragePlayerEvent(player);
            Bukkit.getPluginManager().callEvent(nickSystemAbfragePlayerEvent);
            if (!nickSystemAbfragePlayerEvent.isCancel()) {
                String randomName = nickManager.getRandomName();
                CraftPlayer craftPlayer = (CraftPlayer) player;
                nickManager.setName(craftPlayer, randomName);
                nickManager.refreshPlayer(craftPlayer);
                nickManager.changeSkin(craftPlayer, randomName);
                player.setDisplayName(randomName);
                NickManager.nicked_players.add(player);
                NickSystemUpdateNameTagEvent nickSystemUpdateNameTagEvent = new NickSystemUpdateNameTagEvent(player, randomName);
                Bukkit.getPluginManager().callEvent(nickSystemUpdateNameTagEvent);
            }
        }
    }
}
