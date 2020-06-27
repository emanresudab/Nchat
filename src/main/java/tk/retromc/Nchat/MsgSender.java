package tk.retromc.NChat;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.Bukkit;

public class MsgSender {
	public static void sendMessage(Player dispatcher, Player recipient, String message, NChat instance) {
		String dispatcherName, recipientName;

		dispatcherName = dispatcher.getDisplayName();
		recipientName = recipient.getDisplayName();

		if (dispatcher.hasPermission("nchat.color"))
			message = NChat.color(message);

		dispatcher.sendMessage(decorateString(instance.getMessage("msg.dispatchFormat"),
		                                      dispatcherName, recipientName,
		                                      message));

		recipient.sendMessage(decorateString(instance.getMessage("msg.receiveFormat"),
		                                     dispatcherName, recipientName,
		                                     message));

		/* notify spies */

		Player p;
		for (UUID spy : instance.spies) {
			p = Bukkit.getPlayer(spy);

			if (dispatcher.getUniqueId() != spy && recipient.getUniqueId() != spy)
				p.sendMessage(decorateString(instance.getMessage("spy.format"),
				                             dispatcherName, recipientName, message));
		}

	}

	private static String decorateString(String s, String dispatcher, String recipient, String message) {
		s = s.replaceAll("\\$dispatcher", dispatcher);
		s = s.replaceAll("\\$recipient", recipient);
		s = s.replaceAll("\\$message", message);

		return s;
	}
}
