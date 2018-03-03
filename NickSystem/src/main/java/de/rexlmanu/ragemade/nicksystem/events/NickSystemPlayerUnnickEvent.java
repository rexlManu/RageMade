package de.rexlmanu.ragemade.nicksystem.events;
/*
* Class created by rexlManu
* Twitter: @rexlManu | Website: rexlManu.de
* Coded with IntelliJ
*/

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class NickSystemPlayerUnnickEvent extends Event{

    private static final HandlerList handerList = new HandlerList();

    public static HandlerList getHanderList() {
        return handerList;
    }

    private Player player;
    private String name;

    public NickSystemPlayerUnnickEvent(Player player, String name) {
        this.player = player;
        this.name = name;
    }

    @Override
    public HandlerList getHandlers() {
        return handerList;
    }
}
