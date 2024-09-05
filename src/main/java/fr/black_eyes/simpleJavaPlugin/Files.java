package fr.black_eyes.simpleJavaPlugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import lombok.Setter;

public class Files {
	private File dataFile;
	private FileConfiguration data;
	private File configFile;
	private FileConfiguration config;
	private File langFile;
	private FileConfiguration lang;
	public Boolean PER_WORLD_MESSAGE;
	private @Setter JavaPlugin plugin;
	private static Files instance;
	
	public static Files getInstance() {
		if(instance == null) {
			instance = new Files();
			return instance;
		}
		else return new Files();
	}

	private Files() {}
	
	public void saveData() {
		try {
			data.save(getDataF());
		} catch (IOException | IllegalArgumentException e) {
			e.printStackTrace();
		
		}	
	}
	
	
	public void reloadConfig() {
		try {
			configFile = new File(plugin.getDataFolder(), "config.yml");
			langFile = new File(plugin.getDataFolder(), "lang.yml");
			config.load(getConfigF());
			lang.load(getLangF());
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
	}
	
	public void reloadData() {
		try {
			dataFile = new File(plugin.getDataFolder(), "data.yml");
			data.load(getDataF());
		} catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
	}
	
	public void saveConfig() {
		try {
			config.save(getConfigF());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void saveLang() {
		try {
			lang.save(getLangF());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//function to update config on new version
	public void setConfig(String path, Object value) {
		if(!this.getConfig().isSet(path)){
			getConfig().set(path, value);
			Utils.logInfo("Added config option '"+path+"' in config.yml");
			try {
				getConfig().save(getConfigF());
				getConfig().load(getConfigF());
			} catch (IOException | InvalidConfigurationException e) {
				e.printStackTrace();
			}
		}
	}
	
	//function to edit lang file on new version
	public void setLang(String path, Object value) {
		if(!this.getLang().isSet(path)) {
			getLang().set(path, value);
			Utils.logInfo("Added message '"+path+"' in lang.yml");
			try {
				getLang().save(getLangF());
				getLang().load(getLangF());
			} catch (IOException | InvalidConfigurationException e) {
				e.printStackTrace();
			}
		}
	}
	
	//file initializations
	private File getDataF() {
		return dataFile;
	}
	private File getConfigF() {
		return configFile;
	}
	public File getLangF() {
		return langFile;
	}
	public FileConfiguration getData() {
		return data;
	}
	public FileConfiguration getConfig() {
		return config;
	}
	public FileConfiguration getLang() {
		return lang;
	}
	
	

	
	public boolean initFiles() {
		//config
	    configFile = new File(plugin.getDataFolder(), "config.yml");
	    langFile = new File(plugin.getDataFolder(), "lang.yml");
	    dataFile = new File(plugin.getDataFolder(), "data.yml");
	    if (!configFile.exists()) {
	        configFile.getParentFile().mkdirs();
	        plugin.saveResource("config.yml", false);
	    }
	    config= new YamlConfiguration();
	    try {
	        config.load(configFile);
	    } catch (IOException | InvalidConfigurationException e) {
	        e.printStackTrace();
	    }
	    
	    //lang
	    if (!langFile.exists()) {
	        langFile.getParentFile().mkdirs();
	        plugin.saveResource("lang.yml", false);
	    }
	    lang= new YamlConfiguration();
	    try {
	        lang.load(langFile);
	    } catch (IOException | InvalidConfigurationException e) {
	        e.printStackTrace();
	    }
	   
	    //data
	    if (!dataFile.exists()) {
	        dataFile.getParentFile().mkdirs();
	        plugin.saveResource("data.yml", false);
	    }
	    if(dataFile.length() <10) {
	    	Utils.logInfo("&cInvalid data file detected! Finding backup right now...");
			File directoryPath = new File(plugin.getDataFolder() + "/backups/");
			List<String> contents = Arrays.asList(directoryPath.list());
			int i;
			//finding valid backup name
			for(i=0; contents.contains(i+"data.yml");i++);
			
			if((new File(plugin.getDataFolder() + "/backups/", (i-1)+"data.yml")).length() ==0) {
				Utils.logInfo("&cDidn't find old enough backups, creating new data file!");
			    plugin.saveResource("data.yml", true);
			}
			else {
				Utils.logInfo("&aFound an old enough backup. If it doesn't works, contact developper (all infos in spigot plugin page)");
				Path source = Paths.get(plugin.getDataFolder() + "/data.yml");
			    Path target = Paths.get(plugin.getDataFolder() + "/backups/"+(i-2)+"data.yml");
			    try {
			    	java.nio.file.Files.copy(target, source,  StandardCopyOption.REPLACE_EXISTING);
			    } catch (IOException e1) {
			        e1.printStackTrace();
			    }
				dataFile = new File(plugin.getDataFolder(), "/data.yml");
			}
	    }
	    data= new YamlConfiguration();
	    try {
	        data.load(dataFile);
	    } catch ( Exception e) {
	    	e.printStackTrace();
	        return false;
	    }
		return true;
	}
}
