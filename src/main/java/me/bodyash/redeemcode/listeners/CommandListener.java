package me.bodyash.redeemcode.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

import me.bodyash.redeemcode.dao.DAO;
import me.bodyash.redeemcode.utils.Codegenerator;
import me.bodyash.redeemcode.utils.ConfigUtil;

public class CommandListener {

	private ConfigUtil config;
	private DAO dao;

	public CommandListener(ConfigUtil config, DAO dao) {
		this.config = config;
		this.dao = dao;
	}

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (label.equalsIgnoreCase("redeemcode")) {
			return redeemcode(sender, command, label, args);
		}
		if (label.equalsIgnoreCase("generatecode")) {
			return generatecode(sender, command, label, args);
		}
		if (label.equalsIgnoreCase("deletecode")) {
			return deletecode(sender, command, label, args);
		}
		return false;
	}



	private boolean redeemcode(CommandSender sender, Command command, String label, String[] args) {
		if (args.length == 0){
			sender.sendMessage(config.getChatLogo() + ChatColor.DARK_RED + " Wromg command promt, try this: ");
			sender.sendMessage(ChatColor.GREEN + "/redeemcode <code>");
			return false;
		}
		if (sender instanceof ConsoleCommandSender){
			sender.sendMessage(ChatColor.DARK_RED + "This command cannot be executed from console, sorry");
			return false;
		}
		if (sender.hasPermission("redeemcode.redeem")) {
			if (dao.checkCode(args[0])){
				String type = dao.getCodeType(args[0]);
				if (type != null){
					for (String commandToExecute : config.getCodetypes().get(type)) {
						Bukkit.dispatchCommand(Bukkit.getConsoleSender(), this.replace(commandToExecute, sender.getName()));
					}
				}
				dao.removeCode(args[0]);
			}else{
				sender.sendMessage(config.getChatLogo() + ChatColor.DARK_RED + " Invalid code");
			}
		} else {
			sender.sendMessage(
					this.config.getChatLogo() + ChatColor.DARK_RED + " You don`t have permission to do that.");
		}
		return false;
	}

	private boolean generatecode(CommandSender sender, Command command, String label, String[] args) {
		if (args.length == 0){
			sender.sendMessage(config.getChatLogo() + ChatColor.DARK_RED + " Wrong command promt, try this: ");
			sender.sendMessage(ChatColor.GREEN + "/generatecode <type>");
			return false;
		}
		if (sender.hasPermission("redeemcode.generatecode")) {
			if (config.getCodetypes().containsKey(args[0])){
				String code;
				do{
					code = Codegenerator.generatecode(config.getCodelength());
				}while(dao.checkCode(code));
				dao.addCode(args[0], code);
				sender.sendMessage(config.getChatLogo() + ChatColor.GREEN + " Your code: " + ChatColor.WHITE + code);
				return true;
			}else{
				sender.sendMessage(config.getChatLogo() + ChatColor.DARK_RED + " There is no type " + args[0] + " in config.yml");
			}
		} else {
			sender.sendMessage(
					this.config.getChatLogo() + ChatColor.DARK_RED + " You don`t have permission to do that.");
		}
		return false;
	}
	
	private boolean deletecode(CommandSender sender, Command command, String label, String[] args) {
		if (args.length == 0){
			sender.sendMessage(config.getChatLogo() + ChatColor.DARK_RED + " Wromg command promt, try this: ");
			sender.sendMessage(ChatColor.GREEN + "/deletecode <code>");
			return false;
		}
		if (sender.hasPermission("redeemcode.deletecode")){
			if(dao.checkCode(args[0])){
				dao.removeCode(args[0]);
				return true;
			}else{
				sender.sendMessage(config.getChatLogo() + ChatColor.DARK_RED + " There is no code " + args[0] + " in codes.yml");
			}
		}else{
			sender.sendMessage(
					this.config.getChatLogo() + ChatColor.DARK_RED + " You don`t have permission to do that.");
		}
		return false;
	}
	
	private String replace(String s, String name){
		s = s.replace("%USERNAME%", name);
		return s;
	}

}
