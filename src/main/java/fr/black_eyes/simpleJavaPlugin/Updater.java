package fr.black_eyes.simpleJavaPlugin;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Scanner;
import java.util.function.Consumer;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import lombok.Setter;





public class Updater {

    private JavaPlugin plugin;
    
    //example value
    @Setter private String spigot_complete_id /* = "lootchest.61564"*/;

    @SuppressWarnings("deprecation")
    public Updater(JavaPlugin plugin) {
        if(spigot_complete_id == null) {
        	Utils.logInfo("&cUpdater not initialized, please set the spigot_complete_id value.");
        	return;
        }
        this.plugin = plugin;
        String installedVersion = plugin.getDescription().getVersion();
        this.getVersion(version -> {
            if (!version.equals(installedVersion) && Integer.parseInt(version.replace(".", "")) >  Integer.parseInt(installedVersion.replace(".", ""))) {
            	String lastverDownloadUrl = "https://www.spigotmc.org/resources/"+spigot_complete_id+"/history";
			    Utils.logInfo( "&aA new version " + version + " was found on Spigot (your version: " + installedVersion + "). Please update me! <3 - Link: " + lastverDownloadUrl);

            } else {
            	Utils.logInfo("&aThe plugin seems up to date.");
            }
        });
        
    }

    private void getVersion(final Consumer<String> consumer) {
        Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {
            int resourceId = spigot_complete_id.split("\\.")[1].isEmpty() ? Integer.parseInt(spigot_complete_id.split("\\.")[0]) : Integer.parseInt(spigot_complete_id.split("\\.")[1]);
            try (InputStream inputStream = new URI("https://api.spigotmc.org/legacy/update.php?resource=" + resourceId).toURL().openStream(); Scanner scanner = new Scanner(inputStream)) {
                if (scanner.hasNext()) {
                    consumer.accept(scanner.next());
                }
            } catch (IOException | URISyntaxException exception) {
            	Utils.logInfo("&cFailed to check for a update on spigot.");
            }
        });
    }
}





