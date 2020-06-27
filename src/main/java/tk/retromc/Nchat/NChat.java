package tk.retromc.NChat;

import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;
import java.io.File;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class NChat extends JavaPlugin {
	public HashMap<UUID, UUID> replies; /* would love for this to be private */
	public HashSet<UUID> spies;
	public HashSet<UUID> mutes;

	public YamlConfiguration messages;
	public YamlConfiguration config;

	private File configFile = new File(getDataFolder(), "config.yml");
	private File messageFile = new File(getDataFolder(), "messages.yml");

	@Override
	public void onEnable() {
		config = getDefaultFile(configFile);
		messages = getDefaultFile(messageFile);

		replies = new HashMap<UUID, UUID>();
		spies = new HashSet<UUID>();
		mutes = new HashSet<UUID>();

		new SpyExecutor(this);
		new MsgExecutor(this);
		new NickExecutor(this);
		new ReplyExecutor(this);
		new MuteExecutor(this);

		ChatListener bigBrother = new ChatListener(this, config.getString("chatFormat"));
		Bukkit.getPluginManager().registerEvents(bigBrother, this);

		JoinListener joinListener = new JoinListener(this);
		Bukkit.getPluginManager().registerEvents(joinListener, this);
	}

	private YamlConfiguration getDefaultFile(File f) {
		if (!f.exists())
			saveResource(f.getName(), false);
		return YamlConfiguration.loadConfiguration(f);
	}

	/* Utilities  */ 
	public String getMessage(String s) {
		return color(messages.getString(s));
	}

	public static String color(String s) {
		return ChatColor.translateAlternateColorCodes('&', s);
	}
}
