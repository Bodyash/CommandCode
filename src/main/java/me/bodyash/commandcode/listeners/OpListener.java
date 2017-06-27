package me.bodyash.commandcode.listeners;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import me.bodyash.commandcode.utils.updater.SpigotUpdater;

public class OpListener implements Listener {
	
	private SpigotUpdater updater;
	
	public OpListener(SpigotUpdater updater) {
		this.updater = updater;
	}

	@EventHandler(ignoreCancelled=true, priority=EventPriority.MONITOR)
	private void onPlayerJoin(PlayerJoinEvent e){
		if (e.getPlayer().isOp() && updater.isUpdateFound()){
			e.getPlayer().sendMessage(ChatColor.YELLOW + "[CommandCode] " + ChatColor.GREEN + "an update found, download link:");
			e.getPlayer().sendMessage(ChatColor.WHITE + "" + ChatColor.BOLD + "https://www.spigotmc.org/resources/commandcode.36179/");
		}
	}

}
