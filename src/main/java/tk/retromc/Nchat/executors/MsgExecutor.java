package tk.retromc.NChat;

import java.util.UUID;

import org.bukkit.command.CommandSender;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

public class MsgExecutor extends Applicator {
	public MsgExecutor(NChat i) {
		instance = i;
		isSelf = false;
		messagePath = "msg";

		instance.getCommand("msg").setExecutor(this);
	}

	public boolean executeCommand(CommandSender sender, Command cmd, String label, Player target, String args[]) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("invalidSender");
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

		MsgSender.sendMessage((Player) sender, target, String.join(" ", args), instance);

		instance.replies.put(((Player) sender).getUniqueId(), target.getUniqueId());
		instance.replies.put(target.getUniqueId(), ((Player) sender).getUniqueId());

		return true;
	}
}
