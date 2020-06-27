/* a set of utility functions for manipulating and retrieving player yml data */

package tk.retromc.NChat;

import java.util.UUID;
import java.io.File;

import org.bukkit.configuration.file.YamlConfiguration;

public class PlayerFile {
	public static File get(NChat instance, UUID target, boolean overwrite) {
		File f = new File(instance.getDataFolder(), instance.config.getString("userDataPath") + target + ".yml");

		try {
			if (overwrite && !f.exists())
				f.createNewFile();
		} catch (Exception x) {
			x.printStackTrace();
		}

		return f;
	}
}
