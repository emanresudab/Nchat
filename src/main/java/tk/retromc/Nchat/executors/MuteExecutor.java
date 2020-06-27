package tk.retromc.NChat;

import tk.retromc.NChat.NChat;

import java.util.UUID;
import java.io.File;

import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.Bukkit;

public class MuteExecutor extends UuidApplicator {
	public MuteExecutor(NChat i) {
		instance = i;
		isSelf = true;
		messagePath = "mute";

		instance.getCommand("mute").setExecutor(this);
		instance.getCommand("unmute").setExecutor(this);
	}

	public boolean executeCommand(CommandSender sender, Command cmd, String label, UUID target, String name, String args[]) {
		if (!sender.hasPermission("nchat.mute")) {
			sender.sendMessage(instance.getMessage("permissionDenied"));
			return false;
		}

		File f = PlayerFile.get(instance, target, true);
		YamlConfiguration playerdata = YamlConfiguration.loadConfiguration(f);
		boolean muted = playerdata.getBoolean("muted");
		Player p = Bukkit.getPlayer(target);

		playerdata.set("muted", !muted);

		if (muted) {
			instance.mutes.add(target);
			sender.sendMessage(instance.getMessage("mute.muted").replaceAll("\\$player", name));

			if (p != null && p.isOnline())
				p.sendMessage(instance.getMessage("mute.enableNotify"));
		} else {
			instance.mutes.remove(target);
			sender.sendMessage(instance.getMessage("mute.unmuted").replaceAll("\\$player", name));

			if (p != null && p.isOnline())
				p.sendMessage(instance.getMessage("mute.disableNotify"));
		}

		try {
			playerdata.save(f);
		} catch (Exception x) {
			x.printStackTrace();
		}

		return true;
	}
}
