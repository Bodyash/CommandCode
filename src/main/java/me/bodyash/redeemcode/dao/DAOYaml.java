package me.bodyash.redeemcode.dao;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.file.YamlConfiguration;

public class DAOYaml implements DAO {
	
	private File file;
	private YamlConfiguration codes;
	
	public DAOYaml(File dir) {
		this.file = new File(dir, "codes.yml");
		this.codes = YamlConfiguration.loadConfiguration(file);
		if (!this.file.exists()){
			this.codes.options().header("If u want to change codes manualy - do it for your own risk");
			save();
		}
		
	}

	@Override
	public void addCode(String type, String code) {
		if (codes.getList(type) != null){
			List<String> list = (List<String>) codes.getList(type);
			list.add(code);
			codes.set(type, list);
			save();
		}else{
			List<String> list = new ArrayList<>();
			list.add(code);
			codes.set(type, list);
			save();
		}
	}

	@Override
	public boolean removeCode(String code) {
		for (String type : codes.getKeys(false)) {
			List<String> list = (List<String>) codes.getList(type);
			if (list.contains(code)){
				list.remove(code);
				codes.set(type, list);
				save();
				return true;
			}
		}
		return false;

	}

	@Override
	public boolean checkCode(String code) {
		for (String type : codes.getKeys(false)) {
			List<String> list = (List<String>) codes.getList(type);
			if (list.contains(code)){
				return true;
			}
		}
		return false;
	}

	@Override
	public void removeAllCodes() {
		file.delete();
		this.codes.options().header("Tittle");
		save();
	}
	
	private void save(){
		try {
			this.codes.save(this.file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getCodeType(String code) {
		for (String type : codes.getKeys(false)) {
			List<String> list = (List<String>) codes.getList(type);
			if (list.contains(code)){
				return type;
			}
		}
		return null;
	}

}
