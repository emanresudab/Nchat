package tk.retromc.NChat;

import tk.retromc.NChat.NChat;

import java.util.Arrays;
import java.util.UUID;
import java.util.HashMap;
import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;

public class NickExecutor extends UuidApplicator {
	public NickExecutor(NChat i) {
		instance = i;
		isSelf = false;
		messagePath = "nick";

		instance.getCommand("nick").setExecutor(this);
	}

	public boolean executeCommand(CommandSender sender, Command cmd, String label, UUID target, String name, String args[]) {
		if (!sender.hasPermission("nchat.nick")) {
			sender.sendMessage(instance.getMessage("permissionDenied"));
			return false;
		}

		File f = PlayerFile.get(instance, target, true);
		YamlConfiguration playerdata = YamlConfiguration.loadConfiguration(f);

		Player p = Bukkit.getPlayer(target);

		if (args.length == 0) { /* disable nick if no arguments */
			if (p != null) {
				p.setDisplayName(name);

				if (((Player) sender).getUniqueId() != target)
					p.sendMessage(instance.getMessage("nick.disabled"));
			}

			playerdata.set("nick", null);
			sender.sendMessage(instance.getMessage("nick.disabled"));
			return true;
		}

		/* set nick */
		String nick;
		nick = String.join(" ", args);

		if(sender.hasPermission("nchat.nick.color"))
			nick = instance.color(nick);

		if (p != null)
			p.setDisplayName(nick);

		playerdata.set("nick", nick);

		try {
			playerdata.save(f);
		} catch (Exception x) {
			x.printStackTrace();
		}

		/* notify parties */
		String dispatchmsg, targetmsg;
		dispatchmsg = instance.getMessage("nick.confirm");
		dispatchmsg = dispatchmsg.replaceAll("\\$target", name);
		dispatchmsg = dispatchmsg.replaceAll("\\$nick", nick);

		sender.sendMessage(dispatchmsg);
	
		if (!target.equals((Player) sender) && p != null) {
			targetmsg = instance.color(instance.getMessage("nick.changed"));
			targetmsg = targetmsg.replaceAll("\\$nick", nick);

			p.sendMessage(targetmsg);
		}

		return true;
	}
}
