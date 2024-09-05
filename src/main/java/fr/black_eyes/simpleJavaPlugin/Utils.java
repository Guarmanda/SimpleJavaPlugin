package fr.black_eyes.simpleJavaPlugin;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import fr.black_eyes.simpleJavaPlugin.colors.Ansi;
import fr.black_eyes.simpleJavaPlugin.colors.Ansi.Attribute;
import lombok.Setter;

@SuppressWarnings("deprecation")
public class Utils  {

	@Setter private static JavaPlugin plugin;
	static Map<String, String> replace = new HashMap<String, String>(){{
		put("&0",Ansi.ansi().a(Attribute.RESET).fg(Ansi.Color.BLACK).boldOff().toString());
		put("&1",Ansi.ansi().a(Attribute.RESET).fg(Ansi.Color.BLUE).boldOff().toString());
		put("&2",Ansi.ansi().a(Attribute.RESET).fg(Ansi.Color.GREEN).boldOff().toString());
		put("&3",Ansi.ansi().a(Attribute.RESET).fg(Ansi.Color.CYAN).boldOff().toString());
		put("&4",Ansi.ansi().a(Attribute.RESET).fg(Ansi.Color.RED).boldOff().toString());
		put("&5",Ansi.ansi().a(Attribute.RESET).fg(Ansi.Color.MAGENTA).boldOff().toString());
		put("&6",Ansi.ansi().a(Attribute.RESET).fg(Ansi.Color.YELLOW).boldOff().toString());
		put("&7",Ansi.ansi().a(Attribute.RESET).fg(Ansi.Color.WHITE).boldOff().toString());
		put("&8",Ansi.ansi().a(Attribute.RESET).fg(Ansi.Color.BLACK).bold().toString());
		put("&9",Ansi.ansi().a(Attribute.RESET).fg(Ansi.Color.BLUE).bold().toString());
		put("&a",Ansi.ansi().a(Attribute.RESET).fg(Ansi.Color.GREEN).bold().toString());
		put("&b",Ansi.ansi().a(Attribute.RESET).fg(Ansi.Color.CYAN).bold().toString());
		put("&c",Ansi.ansi().a(Attribute.RESET).fg(Ansi.Color.RED).bold().toString());
		put("&d",Ansi.ansi().a(Attribute.RESET).fg(Ansi.Color.MAGENTA).bold().toString());
		put("&e",Ansi.ansi().a(Attribute.RESET).fg(Ansi.Color.YELLOW).bold().toString());
		put("&f",Ansi.ansi().a(Attribute.RESET).fg(Ansi.Color.WHITE).bold().toString());
		put("&l",Ansi.ansi().a(Attribute.BLINK_SLOW).toString());
		put("&m",Ansi.ansi().a(Attribute.STRIKETHROUGH_ON).toString());
		put("&n",Ansi.ansi().a(Attribute.UNDERLINE).toString());
	}};

	private Utils() {
	}

	public static String color(String s){
		return ChatColor.translateAlternateColorCodes('&', s);
	}
	
	//message functions that automatically get a message from config lang file
	public static void msg(CommandSender p, String path, String replacer, String replacement) {
		String message = path;
		if(Files.getInstance().getLang().isSet(path)) {
			message = getMsg(path, replacer, replacement);
		}
		sendMultilineMessage(message, p);
	}
	
	//message functions that automatically get a message from config lang file
	public static void msg(CommandSender p, String path, String replacer, String replacement, String replacer2, String replacement2) {
		String message = Files.getInstance().getLang().getString(path).replace(replacer, replacement).replace(replacer2, replacement2);
		sendMultilineMessage(message, p);
	}
	
	/*
	 * This function is only for messages of chest spawning.
	 * 
	 */
	public static void msg(CommandSender p, String path,  String r1, String r1b, String r2, String r2b, String r3, String r3b, String r4, String r4b,  String r5, String r5b) {
		String message = path;
		if(Files.getInstance().getLang().isSet(path)) {
			message = Files.getInstance().getLang().getString(path);
		}
		message = message.replace(r1, r1b).replace(r2, r2b).replace(r3, r3b).replace(r4, r4b).replace(r5, r5b);
		sendMultilineMessage(message, p);
	}
	
	public static void sendMultilineMessage(String message, CommandSender player) {
		List<String> msgs = Arrays.asList(message.split("\\\\n"));
		msgs.stream().forEach(msg -> player.sendMessage(color(msg)));
	}
	
	public static String getMsg(String path, String replacer, String replacement,  String replacer2, String replacement2) {
		return color(getMsg(path, replacer, replacement).replace( replacer2, replacement2));
	}
	public static String getMsg(String path, String replacer, String replacement) {
		return color(getMsg(path).replace(replacer, replacement));
	}
	public static String getMsg(String path) {
		return color(Files.getInstance().getLang().getString(path));
	}

	/**
	 * Send a message to logs with colors, only if logs are enabled in config.
	 * @param msg the message to send
	 */
	public static void logInfo(String msg) {
		Files configFiles = Files.getInstance();
    	if(configFiles.getConfig() ==null || !configFiles.getConfig().isSet("ConsoleMessages") || configFiles.getConfig().getBoolean("ConsoleMessages")) {
			// use replace to replace all the keys from the map with their values
			for (Map.Entry<String, String> entry : replace.entrySet()) {
				msg = msg.replace(entry.getKey(), entry.getValue().toString());
			}
			//add reset to the end of the message
			msg = msg + Ansi.ansi().a(Attribute.RESET).toString();
			Bukkit.getLogger().info("["+plugin.getDescription().getName()+"] "+msg);
		}
	}
	
	
}
