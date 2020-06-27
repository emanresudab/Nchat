package tk.retromc.NChat;

import java.io.File;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.Listener;
import org.bukkit.entity.Player;
import org.bukkit.configuration.file.YamlConfiguration;

public class JoinListener implements Listener {
	private NChat instance;

	public JoinListener(NChat i) {
		instance = i;
	}

	@EventHandler
	public void onLogin(PlayerJoinEvent e) {
		Player p = e.getPlayer();

		File f = PlayerFile.get(instance, p.getUniqueId(), true);
		YamlConfiguration playerdata = YamlConfiguration.loadConfiguration(f);

		if (playerdata.isSet("nick"))
			p.setDisplayName(playerdata.getString("nick"));

		if (playerdata.getBoolean("muted"))
			instance.mutes.add(p.getUniqueId());

		if (playerdata.getBoolean("IsMsgspy"))
			instance.spies.add(p.getUniqueId());
	}
}
