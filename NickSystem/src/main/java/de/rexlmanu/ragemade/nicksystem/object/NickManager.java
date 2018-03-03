package de.rexlmanu.ragemade.nicksystem.object;
/*
* Class created by rexlManu
* Twitter: @rexlManu | Website: rexlManu.de
* Coded with IntelliJ
*/

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import de.rexlmanu.ragemade.nicksystem.NickSystem;
import de.rexlmanu.ragemade.nicksystem.utils.GameProfileBuilder;
import de.rexlmanu.ragemade.nicksystem.utils.UUIDFetcher;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_8_R3.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo.EnumPlayerInfoAction;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

public class NickManager {

    public final static ArrayList<Player> nicked_players = new ArrayList<>();

    private List<String> nicknames;

    private File file;
    private YamlConfiguration cfg;

    public NickManager() {
        this.file = new File(NickSystem.getInstance().getDataFolder(), "nicknames.yml");
        this.cfg = YamlConfiguration.loadConfiguration(file);

        if (!file.exists()) {
            NickSystem.getInstance().getDataFolder().mkdir();
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            ArrayList<String> value = new ArrayList<>();
            value.add("GommeHD");
            value.add("ungespielt");
            value.add("_qlow");
            this.cfg.set("Names", value);
            try {
                cfg.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        this.nicknames = cfg.getStringList("Names");
    }

    public String getRandomName() {
        int i = new Random().nextInt(nicknames.size());
        String s = nicknames.get(i);
        for (Player nicked_player : nicked_players) {
            if (nicked_player.getDisplayName().equalsIgnoreCase(s)) {
                return getRandomName();
            }
        }
        return s;
    }

    public void changeSkin(CraftPlayer cp, String skinName) {
        GameProfile gp = cp.getProfile();

        try {
            gp = GameProfileBuilder.fetch(UUIDFetcher.getUUID(skinName));
        } catch (Exception ex) {
        }

        Collection<Property> props = gp.getProperties().get("textures");
        cp.getProfile().getProperties().removeAll("textures");
        cp.getProfile().getProperties().putAll("textures", props);

        destroyPlayer(cp);
        removePlayerFromTab(cp);

        Bukkit.getScheduler().runTaskLater(NickSystem.getInstance(), () -> {
            addPlayerToTab(cp);
            spawnPlayer(cp);
        }, 5);
    }

    public void refreshPlayer(CraftPlayer cp) {
        destroyPlayer(cp);
        removePlayerFromTab(cp);

        Bukkit.getScheduler().runTaskLater(NickSystem.getInstance(), () -> {
            addPlayerToTab(cp);
            spawnPlayer(cp);
        }, 2);
    }

    private final static Field field = getField(GameProfile.class, "name");

    public void setName(CraftPlayer cp, String nickName) {
        try {
            field.set(cp.getProfile(), nickName);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void destroyPlayer(CraftPlayer cp) {
        sendPacket(new PacketPlayOutEntityDestroy(cp.getEntityId()));
    }

    public void removePlayerFromTab(CraftPlayer cp) {
        sendPacket(new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.REMOVE_PLAYER, cp.getHandle()));
    }

    public void spawnPlayer(CraftPlayer cp) {
        for (Player all : Bukkit.getOnlinePlayers()) {
            CraftPlayer cpAll = ((CraftPlayer) all);

            if (!(cpAll.equals(cp))) {
                cpAll.getHandle().playerConnection.sendPacket(new PacketPlayOutNamedEntitySpawn(cp.getHandle()));
            }
        }
    }

    public void addPlayerToTab(CraftPlayer cp) {
        sendPacket(new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.ADD_PLAYER, cp.getHandle()));
    }

    public void sendPacket(Packet<?> packet) {
        for (Player all : Bukkit.getOnlinePlayers()) {
            ((CraftPlayer) all).getHandle().playerConnection.sendPacket(packet);
        }
    }

    public static Field getField(Class<?> clazz, String name) {
        Field field;

        try {
            field = clazz.getDeclaredField(name);
            field.setAccessible(true);

            return field;
        } catch (NoSuchFieldException | SecurityException ex) {
            return null;
        }
    }

}
