package fr.black_eyes.simpleJavaPlugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;
import lombok.Getter;


public abstract class SimpleJavaPlugin extends JavaPlugin {

	@Getter protected Files configFiles;
	protected PluginCommand command;

	@SuppressWarnings("deprecation")
	public void setCommandExecutor(CommandExecutor commandExecutor) {
		// get the first command from plugin.yml
		command = getDescription().getCommands().keySet().stream().map(this::getCommand).findFirst().orElse(null);
		command.setExecutor(commandExecutor);
	}
	

	@Override
	public void onDisable() {
		backUp();
		Utils.logInfo("&aBacked up data file in case of crash");
	}
    
	@Override
	public void onEnable() {
		
		configFiles = Files.getInstance();

		Utils.setPlugin(this);
		Utils.logInfo("Loading config files...");
		if(!configFiles.initFiles()) {
        	Utils.logInfo("&cConfig or data files couldn't be initialized, the plugin will stop.");
        	return;
        }

        super.onEnable();
        configFiles.saveConfig();
        configFiles.saveLang();
		//load config
		//setConfigs(Config.getInstance(configFiles.getConfig()));
        new Updater(this);
	}
	

	/**
	 * Creates a backup of data.yml, which is sometimes lost by plugin users in some rare cases.
	 */
	public void backUp() {
		File directoryPath = new File(getDataFolder() + "/backups/");
		if(!directoryPath.exists()) {
			directoryPath.mkdir();
		}
		List<String> contents = Arrays.asList(directoryPath.list());
		int i=0;
		//finding valid backup name
		if(!contents.isEmpty()) {
			while( !contents.contains(i+"data.yml")) i++;
		}
		while( contents.contains(i+"data.yml")) {
			if (contents.contains((i+10)+"data.yml")) {
				Path oldBackup = Paths.get(getDataFolder() +"/backups/"+ (i)+"data.yml");
				try {
					java.nio.file.Files.deleteIfExists(oldBackup);
				} catch (IOException e) {
					e.printStackTrace();
				}
				i+=9;
			}
			i++;
		}
		
		//auto-deletion of backup to keep only the 10 last ones
		Path oldBackup = Paths.get(getDataFolder() +"/backups/"+ (i-10)+"data.yml");
		try {
			java.nio.file.Files.deleteIfExists(oldBackup);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//backing up
		Path source = Paths.get(getDataFolder() + "/data.yml");
	    Path target = Paths.get(getDataFolder() + "/backups/"+i+"data.yml");
	    try {
	    	java.nio.file.Files.copy(source, target);
	    } catch (IOException e1) {
	        e1.printStackTrace();
	    }
	}


}
