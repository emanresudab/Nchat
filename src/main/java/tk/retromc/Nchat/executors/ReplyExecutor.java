package tk.retromc.NChat;

import tk.retromc.NChat.NChat;
import tk.retromc.NChat.MsgSender;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;

public class ReplyExecutor implements CommandExecutor {
	private NChat instance;

	public ReplyExecutor(NChat i) {
		instance = i;

		instance.getCommand("reply").setExecutor(this);
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String args[]) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(instance.getMessage("playerOnly"));
			return false;
		}

		if (!sender.hasPermission("minecraft.command.msg")) {
			sender.sendMessage(instance.getMessage("noPermission"));
			return false;
		}

		if (args.length < 1) {
			sender.sendMessage(instance.getMessage("msg.usageMessage"));
			return false;
		}

		if (!instance.replies.containsKey(((Player) sender).getUniqueId())) {
			sender.sendMessage(instance.getMessage("msg.noReply"));
			return false;
		}

		Player recipient;
		recipient = Bukkit.getPlayer(instance.replies.get(((Player) sender).getUniqueId()));

		MsgSender.sendMessage((Player) sender, recipient, String.join(" ", args), instance);
		return true;
	}
}
