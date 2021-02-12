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

package dev.dreta.itemeditor.commands;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.CommandAlias;
import co.aikar.commands.annotation.Default;
import co.aikar.commands.annotation.Description;
import dev.dreta.itemeditor.guis.ItemCreator;
import org.bukkit.entity.Player;

@CommandAlias("itemeditor")
@Description("Open item editor")
public class ItemEditorCommand extends BaseCommand {
    @Default
    public void itemEditor(Player player) {
        ItemCreator editor = new ItemCreator(player, a -> true, () -> {});
        editor.open();
    }
}
