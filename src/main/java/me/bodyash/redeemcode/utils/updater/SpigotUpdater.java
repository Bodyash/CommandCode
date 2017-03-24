package me.bodyash.redeemcode.utils.updater;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;


public class SpigotUpdater {
	private String WRITE_STRING;
	private String version;
	private String oldVersion;
	private SpigotUpdater.UpdateResult result = SpigotUpdater.UpdateResult.DISABLED;
	private HttpURLConnection connection;
	private boolean updateFound;

	public enum UpdateResult {
		NO_UPDATE,
		DISABLED,
		FAIL_SPIGOT,
		SPIGOT_UPDATE_AVAILABLE
	}

	public SpigotUpdater(JavaPlugin plugin) {
		String RESOURCE_ID = "36179"; // change resource id
		oldVersion = plugin.getDescription().getVersion().replaceAll("-SNAPSHOT-", ".");
		try {
			String QUERY = "/api/general.php";
			String HOST = "http://www.spigotmc.org";
			connection = (HttpURLConnection) new URL(HOST + QUERY).openConnection();
		} catch (IOException e) {
			result = UpdateResult.FAIL_SPIGOT;
			return;
		}

		String API_KEY = "98BE0FE67F88AB82B4C197FAF1DC3B69206EFDCC4D3B80FC83A00037510B99B4";
		WRITE_STRING = "key=" + API_KEY + "&resource=" + RESOURCE_ID;
		runSpigot();
	}

	private void runSpigot() {
		connection.setDoOutput(true);
		try {
			String REQUEST_METHOD = "POST";
			connection.setRequestMethod(REQUEST_METHOD);
			connection.getOutputStream().write(WRITE_STRING.getBytes("UTF-8"));
		} catch (IOException e) {
			result = UpdateResult.FAIL_SPIGOT;
		}
		String currversion;
		try {
			currversion = new BufferedReader(new InputStreamReader(connection.getInputStream())).readLine();
		} catch (Exception e) {
			result = UpdateResult.FAIL_SPIGOT;
			return;
		}
		if (currversion.length() <= 7) {
			this.version = currversion.replace("[^A-Za-z]", "").replace("|", "").replace("-", "");
			spigotCheckUpdate();
			return;
		}
		result = UpdateResult.FAIL_SPIGOT;
	}

	private void spigotCheckUpdate() {
		Integer oldVersion = Integer.parseInt(this.oldVersion.replace(".", ""));
		Integer currentVersion = Integer.parseInt(this.version.replace(".", ""));
		if (oldVersion < currentVersion) {
				result = UpdateResult.SPIGOT_UPDATE_AVAILABLE;
				this.updateFound = true;
		} else {
			result = UpdateResult.NO_UPDATE;
			this.updateFound = false;
		}
	}
	

	public UpdateResult getResult() {
		return result;
	}

	public String getVersion() {
		return version;
	}
	
    
    public boolean isUpdateFound(){
    	return updateFound;
    }

	public void printResultToConsole() {
		if(result == UpdateResult.FAIL_SPIGOT){
			Bukkit.getLogger().log(Level.WARNING, "[CommandCode] Update check fail");
		}
		if(result == UpdateResult.NO_UPDATE){
			Bukkit.getLogger().log(Level.INFO, "[CommandCode] Update not found");
		}
		if (result == UpdateResult.SPIGOT_UPDATE_AVAILABLE){
			Bukkit.getLogger().log(Level.INFO, "[CommandCode] UPDATE FOUND!");
		}
	}
	

}