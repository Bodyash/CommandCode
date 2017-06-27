package me.bodyash.commandcode.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

public class ConfigUtil {

	// files
	private File configFile;
	private YamlConfiguration config;

	private String consoleLogo = "[CommandCode] ";
	private Map<String, List<String>> codetypes;
	private int codelength = 10;
	private String chatLogo = "&a[&bCommandCode&a]";
	private boolean checkUpdates = true;
	
	private String dbType = "yml";
	private String dbUser = "root";
	private String dbPassword = "root";
	private int dbPort = 3306;
	private String dbAdress = "127.0.0.1";
	private String dbDatabase = "redeemcode";

	public ConfigUtil(File pluginFolder) {
		this.configFile = new File(pluginFolder, "config.yml");
		this.config = YamlConfiguration.loadConfiguration(configFile);
		this.startup();
	}

	private void startup() {
		if (!this.configFile.exists()) {
			Bukkit.getLogger().log(Level.WARNING, this.consoleLogo + "... Starting config creation ...");
			this.createConfig();
			loadconfig();
		} else {
			loadconfig();
		}
	}

	@SuppressWarnings("unchecked")
	private void loadconfig() {
		codetypes = new HashMap<>();
		for (String node : config.getConfigurationSection("codeconfig").getKeys(false)) {
			List<String> commandlist = (List<String>) config.getList("codeconfig." + node + ".commands");
			codetypes.put(node, commandlist);
		}
		codelength = (this.config.getInt("codelength"));
		chatLogo = this.config.getString("chatlogo");
		checkUpdates = this.config.getBoolean("checkforupdates");
		this.dbType =  config.getString("Database.type");
		if (dbType.equalsIgnoreCase("mysql")){
			dbUser = config.getString("Database.user");
			dbPassword = config.getString("Database.pass");
			dbPort = config.getInt("Database.port");
			dbAdress = config.getString("Database.adress");
			dbDatabase = config.getString("Database.database");
		}
	}

	private void createConfig() {
		this.config.options()
				.header("codelength - How many symbols code must be\n"
						+ "Chatlogo - Logo Used in Chat: (support for colorcodes)\n" + "Code Types Config:\n"
						+ "All code types must have uniqe name\n" + "all commands support color codes\n"
						+ "%USERNAME% tag will be replaced to sender nickname");
		this.config.set("codelength", 10);
		this.config.set("chatlogo", this.chatLogo);
		this.config.set("checkforupdates", checkUpdates);
		
		this.config.set("Database.type", dbType);
		this.config.set("Database.user", dbUser);
		this.config.set("Database.pass", dbPassword);
		this.config.set("Database.port", dbPort);
		this.config.set("Database.adress", dbAdress);
		this.config.set("Database.database", dbDatabase);

		List<String> list = new ArrayList<>();
		list.add("temprank %USERNAME% VIP 31d Member");
		list.add("broadcast &a%USERNAME% &egot a &dVIP &estatus for 31 days!");

		List<String> list2 = new ArrayList<>();
		list2.add("temprank %USERNAME% Premium 31d Member");
		list2.add("broadcast &a%USERNAME% &egot a &dPremium &estatus for 31 days!");

		this.config.set("codeconfig.vip.commands", list);
		this.config.set("codeconfig.premium.commands", list2);

		try {
			this.config.save(configFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public int getCodelength() {
		return this.codelength;
	}

	public String getChatLogo() {
		return this.colorize(this.chatLogo);
	}

	public String colorize(String s) {
		if (s == null)
			return "";
		return s.replaceAll("&([0-9a-f])", "\u00A7$1");
	}

	public Map<String, List<String>> getCodetypes() {
		return codetypes;
	}

	public boolean isCheckUpdates() {
		return checkUpdates;
	}
	
	public String getDbType() {
		return dbType;
	}

	public String getDbUser() {
		return dbUser;
	}

	public String getDbPassword() {
		return dbPassword;
	}

	public int getDbPort() {
		return dbPort;
	}

	public String getDbAdress() {
		return dbAdress;
	}

	public String getDbDatabase() {
		return dbDatabase;
	}

}
