package de.rexlmanu.ragemade.nicksystem;
/*
* Class created by rexlManu
* Twitter: @rexlManu | Website: rexlManu.de
* Coded with IntelliJ
*/

import de.rexlmanu.ragemade.nicksystem.commands.NickCommand;
import de.rexlmanu.ragemade.nicksystem.commands.UnnickCommand;
import de.rexlmanu.ragemade.nicksystem.listeners.player.PlayerJoinListener;
import de.rexlmanu.ragemade.nicksystem.object.NickManager;
import org.bukkit.Bukkit;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.plugin.java.JavaPlugin;

public class NickSystem extends JavaPlugin {

    private static NickSystem instance;

    public static final String PREFIX = "§8» §5NickSystem §8× §7";

    private NickManager nickManager;

    @Override
    public void onEnable() {
        instance = this;

        this.nickManager = new NickManager();
        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(), this);

        SimpleCommandMap commandMap = ((CraftServer) Bukkit.getServer()).getCommandMap();
        commandMap.register("nickSystem", new NickCommand("nick"));
        commandMap.register("nickSystem", new UnnickCommand("unnick"));
    }

    public NickManager getNickManager() {
        return nickManager;
    }

    public static NickSystem getInstance() {
        return instance;
    }
}
