package tk.retromc.NChat;

import tk.retromc.NChat.NChat;

import java.util.UUID;
import java.util.HashSet;
import java.io.File;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

public class SpyExecutor implements CommandExecutor {
	private NChat instance;

	public SpyExecutor(NChat i) {
		instance = i;

		instance.getCommand("msgspy").setExecutor(this);
	}

	public boolean onCommand(CommandSender sender, Command cms, String label, String args[]) {
		if (!sender.hasPermission("nchat.spy")) {
			sender.sendMessage(instance.getMessage("noPermission"));
			return false;
		}

		UUID uuid;
		uuid = ((Player) sender).getUniqueId();

		File f = PlayerFile.get(instance, uuid, true);
		YamlConfiguration playerdata = YamlConfiguration.loadConfiguration(f);
		boolean isSpy = playerdata.getBoolean("isMsgspy");

		playerdata.set("isMsgspy", !isSpy);

		try {
			playerdata.save(f);
			instance.spies.add(uuid);
			sender.sendMessage(instance.getMessage(!isSpy ? "msg.spy.enable" : "msg.spy.disable"));
		} catch (Exception x) {
			x.printStackTrace();
		}

		return true;
	}
}
