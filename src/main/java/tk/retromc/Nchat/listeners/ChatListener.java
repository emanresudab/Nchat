package tk.retromc.NChat;

import java.util.UUID;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.Listener;
import org.bukkit.entity.Player;
import org.bukkit.OfflinePlayer;
import org.bukkit.Bukkit;

public class ChatListener implements Listener {
	private NChat instance;
	private String format;

	public ChatListener(NChat i, String f) {
		instance = i;
		format = f;
	}

	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		if (instance.mutes.contains(e.getPlayer().getUniqueId())) {
			e.setCancelled(true);
			e.getPlayer().sendMessage("mute.attempt");
			notifyOps(e);
			return;
		}

		if (e.getPlayer().hasPermission("nchat.color"))
			e.setMessage(instance.color(e.getMessage()));

		e.setFormat(instance.color(instance.config.getString("chatFormat")));
	}

	private void notifyOps(AsyncPlayerChatEvent e) {
		for (OfflinePlayer op : Bukkit.getOperators()) {
			if (!op.isOnline())
				continue;

			op.getPlayer().sendMessage(instance.getMessage("mute.blockedChat").replaceAll("\\$message", e.getMessage()));
		}
	}
}
