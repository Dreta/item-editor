/*
 * Item Editor: Modify items with ease in Minecraft servers.
 * Copyright (C) 2021 Dreta
 *
 * Item Editor is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Item Editor is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Item Editor. If not, see <https://www.gnu.org/licenses/>.
 */

package dev.dreta.itemeditor;

import co.aikar.commands.PaperCommandManager;
import dev.dreta.itemeditor.commands.ItemEditorCommand;
import dev.dreta.itemeditor.utils.Configuration;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class ItemEditor extends JavaPlugin {
    public static ItemEditor inst;
    public static Logger logger;
    private static Configuration messages;

    public static String getMsg(String key) {
        return ChatColor.translateAlternateColorCodes('&', messages.getConfig().getString(key));
    }

    @Override
    public void onEnable() {
        inst = this;
        logger = Logger.getLogger("Minecraft");

        messages = Configuration.loadConfiguration("messages.yml");

        PaperCommandManager manager = new PaperCommandManager(this);
        manager.enableUnstableAPI("brigadier");
        manager.registerCommand(new ItemEditorCommand());

        logger.info("Successfully enabled ItemEditor!");
    }

    @Override
    public void onDisable() {
        logger.info("Successfully disabled ItemEditor!");
    }
}
