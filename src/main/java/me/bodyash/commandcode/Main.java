package me.bodyash.commandcode;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.hibernate.SessionFactory;

import me.bodyash.commandcode.dao.DAOHib;
import me.bodyash.commandcode.dao.DAOYaml;
import me.bodyash.commandcode.listeners.CommandListener;
import me.bodyash.commandcode.listeners.OpListener;
import me.bodyash.commandcode.utils.ConfigUtil;
import me.bodyash.commandcode.utils.HibernateUtil;
import me.bodyash.commandcode.utils.updater.SpigotUpdater;

public class Main extends JavaPlugin{
	
	private ConfigUtil config;
	private CommandListener commandListener;
	private SpigotUpdater updater;
	
	@Override
	public void onEnable() {
		config = new ConfigUtil(getDataFolder());
		if (!config.getDbType().equalsIgnoreCase("MYSQL")){
			commandListener = new CommandListener(config, new DAOYaml(getDataFolder()));
		}else{
			try{
				commandListener = new CommandListener(config, new DAOHib(new HibernateUtil(config).createSessionFactory()));
			} catch (Exception e) {
				System.err.println("[RedeemCode] Mysql connection failed. (Hibbernate session factory creation failed)");
				commandListener = new CommandListener(config, new DAOYaml(getDataFolder()));
			}
		}
		if (config.isCheckUpdates()){
			updater = new SpigotUpdater(this);
			OpListener listener = new OpListener(updater);
			getServer().getPluginManager().registerEvents(listener, this);
		}
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		return commandListener.onCommand(sender, command, label, args);
	}

}
