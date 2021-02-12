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

package dev.dreta.itemeditor.utils;

import com.google.common.collect.Multimap;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionData;

import java.util.*;
import java.util.stream.Collectors;

/**
 * ItemStackBuilder provides an easier method to create an {@link ItemStack}.
 * @author Dreta
 */
public class ItemStackBuilder {
    private final ItemStack stack;
    private final Set<Attribute> attributed = new HashSet<>();

    public ItemStackBuilder(Material material) {
        stack = new ItemStack(material);
    }

    public ItemStackBuilder amount(int amount) {
        stack.setAmount(amount);
        return this;
    }

    public ItemStackBuilder name(String name) {
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(name);
        stack.setItemMeta(meta);
        return this;
    }

    public ItemStackBuilder lore(List<String> lore) {
        ItemMeta meta = stack.getItemMeta();
        meta.setLore(lore);
        stack.setItemMeta(meta);
        return this;
    }

    public ItemStackBuilder enchant(Enchantment enchantment, int level) {
        stack.addUnsafeEnchantment(enchantment, level);
        return this;
    }

    public ItemStackBuilder enchant(Map<Enchantment, Integer> enchantments) {
        stack.addUnsafeEnchantments(enchantments);
        return this;
    }

    public ItemStackBuilder leatherColor(Color color) {
        LeatherArmorMeta meta = (LeatherArmorMeta) stack.getItemMeta();
        meta.setColor(color);
        stack.setItemMeta(meta);
        return this;
    }

    public ItemStackBuilder canPlaceOn(Material... materials) {
        ItemMeta meta = stack.getItemMeta();
        meta.setPlaceableKeys(Arrays.stream(materials).map(Material::getKey).collect(Collectors.toList()));
        stack.setItemMeta(meta);
        return this;
    }

    public ItemStackBuilder canDestroy(Material... materials) {
        ItemMeta meta = stack.getItemMeta();
        meta.setDestroyableKeys(Arrays.stream(materials).map(Material::getKey).collect(Collectors.toList()));
        stack.setItemMeta(meta);
        return this;
    }

    public ItemStackBuilder skullOwner(OfflinePlayer player) {
        SkullMeta meta = (SkullMeta) stack.getItemMeta();
        meta.setOwningPlayer(player);
        stack.setItemMeta(meta);
        return this;
    }

    public ItemStackBuilder potion(PotionData data, Color color) {
        PotionMeta meta = (PotionMeta) stack.getItemMeta();
        meta.setBasePotionData(data);
        meta.setColor(color);
        this.stack.setItemMeta(meta);
        return this;
    }

    public ItemStackBuilder attribute(Multimap<Attribute, AttributeModifier> attributes) {
        ItemMeta meta = stack.getItemMeta();
        meta.setAttributeModifiers(attributes);
        stack.setItemMeta(meta);
        return this;
    }

    public ItemStackBuilder attribute(Attribute attribute, AttributeModifier modifier) {
        ItemMeta meta = stack.getItemMeta();
        if (!attributed.contains(attribute)) {
            meta.removeAttributeModifier(attribute);
            attributed.add(attribute);
        }
        meta.addAttributeModifier(attribute, modifier);
        stack.setItemMeta(meta);
        return this;
    }

    public ItemStackBuilder hideFlags(ItemFlag... flags) {
        ItemMeta meta = stack.getItemMeta();
        meta.addItemFlags(flags);
        stack.setItemMeta(meta);
        return this;
    }

    public ItemStackBuilder unbreakable(boolean unbreakable) {
        ItemMeta meta = stack.getItemMeta();
        meta.setUnbreakable(unbreakable);
        stack.setItemMeta(meta);
        return this;
    }

    public ItemStackBuilder customModelData(int data) {
        ItemMeta meta = stack.getItemMeta();
        meta.setCustomModelData(data);
        stack.setItemMeta(meta);
        return this;
    }

    public ItemStack build() {
        return stack;
    }
}
