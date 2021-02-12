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

package dev.dreta.itemeditor.guis;

import dev.dreta.itemeditor.ItemEditor;
import dev.dreta.itemeditor.utils.ItemStackBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

import java.util.*;
import java.util.function.Function;

/**
 * ItemCreator provides a nice GUI allowing the user
 * to edit an item's metadata at ease.
 */
public class ItemCreator implements Listener {
    // This map should be self-explanatory.
    public static Map<ChatColor, Color> chatToColor = new HashMap<>();
    // Maps commonly used enchantment names to the real enchantments
    public static Map<String, Enchantment> enchantments = new HashMap<>();
    // Maps commonly used status effect names to the real status effects
    public static Map<String, PotionType> potions = new HashMap<>();

    // The following are the static items used in this inventory.
    //<editor-fold desc="Static Inventory Items">
    public static ItemStack NO_ITEM = new ItemStackBuilder(Material.BARRIER)
            .name(ChatColor.RED + "No item to edit!")
            .lore(Arrays.asList(
                    ChatColor.GRAY + "You can put an item from your",
                    ChatColor.GRAY + "inventory here to start editing",
                    ChatColor.GRAY + "it.",
                    ChatColor.GREEN + "Left click to set item"
            ))
            .build();
    public static ItemStack SET_NAME = new ItemStackBuilder(Material.NAME_TAG)
            .name(ChatColor.YELLOW + "Set Name")
            .lore(Arrays.asList(
                    ChatColor.GRAY + "Change the name of the item.",
                    ChatColor.GREEN + "Left click to set name"
            ))
            .build();
    public static ItemStack SET_LORE = new ItemStackBuilder(Material.OAK_SIGN)
            .name(ChatColor.YELLOW + "Set Lore")
            .lore(Arrays.asList(
                    ChatColor.GRAY + "Change the lore (description) of",
                    ChatColor.GRAY + "the item.",
                    ChatColor.GREEN + "Left click to set lore"
            ))
            .build();
    public static ItemStack ENCHANT = new ItemStackBuilder(Material.ENCHANTED_BOOK)
            .name(ChatColor.YELLOW + "Enchant")
            .lore(Arrays.asList(
                    ChatColor.GRAY + "Add enchantments to this item.",
                    ChatColor.GREEN + "Left click to enchant"
            ))
            .build();
    public static ItemStack LEATHER_COLOR = new ItemStackBuilder(Material.LEATHER_HELMET)
            .name(ChatColor.YELLOW + "Set Leather Color")
            .leatherColor(Color.GREEN)
            .lore(Arrays.asList(
                    ChatColor.GRAY + "Dye a piece of leather armor.",
                    ChatColor.GREEN + "Left click to dye"
            ))
            .build();
    public static ItemStack LEATHER_COLOR_NOT_APPLICABLE = new ItemStackBuilder(Material.LEATHER_HELMET)
            .name(ChatColor.RED + "Set Leather Color")
            .leatherColor(Color.GREEN)
            .lore(Arrays.asList(
                    ChatColor.GRAY + "Dye a piece of leather armor.",
                    ChatColor.RED + "Not applicable"
            ))
            .build();
    public static ItemStack POTION_COLOR = new ItemStackBuilder(Material.POTION)
            .name(ChatColor.YELLOW + "Set Potion Data")
            .potion(new PotionData(PotionType.INSTANT_HEAL, false, false), Color.PURPLE)
            .hideFlags(ItemFlag.HIDE_POTION_EFFECTS)
            .lore(Arrays.asList(
                    ChatColor.GRAY + "Set the potion effects and color of",
                    ChatColor.GRAY + "a potion.",
                    ChatColor.GREEN + "Left click to set potion data"
            ))
            .build();
    public static ItemStack POTION_COLOR_NOT_APPLICABLE = new ItemStackBuilder(Material.POTION)
            .name(ChatColor.RED + "Set Potion Data")
            .potion(new PotionData(PotionType.INSTANT_HEAL, false, false), Color.PURPLE)
            .hideFlags(ItemFlag.HIDE_POTION_EFFECTS)
            .lore(Arrays.asList(
                    ChatColor.GRAY + "Set the potion effects and color of",
                    ChatColor.GRAY + "a potion.",
                    ChatColor.RED + "Not applicable"
            ))
            .build();
    public static ItemStack UNBREAKABLE = new ItemStackBuilder(Material.BEDROCK)
            .name(ChatColor.YELLOW + "Set Unbreakable")
            .lore(Arrays.asList(
                    ChatColor.GRAY + "Toggle whether this item will be",
                    ChatColor.GRAY + "affected by durability.",
                    ChatColor.GRAY + "Unbreakable: " + ChatColor.RED + "Off",
                    ChatColor.GREEN + "Left click to toggle unbreakability"
            ))
            .build();
    public static ItemStack UNBREAKABLE_ON = new ItemStackBuilder(Material.BEDROCK)
            .name(ChatColor.YELLOW + "Set Unbreakable")
            .unbreakable(true)
            .enchant(Enchantment.DURABILITY, 5)
            .hideFlags(ItemFlag.HIDE_ENCHANTS)
            .lore(Arrays.asList(
                    ChatColor.GRAY + "Toggle whether this item will be",
                    ChatColor.GRAY + "affected by durability.",
                    ChatColor.GRAY + "Unbreakable: " + ChatColor.GREEN + "On",
                    ChatColor.GREEN + "Left click to toggle unbreakability"
            ))
            .build();
    public static ItemStack CUSTOM_MODEL_DATA = new ItemStackBuilder(Material.POTATO)
            .name(ChatColor.YELLOW + "Set Custom Model Data")
            .lore(Arrays.asList(
                    ChatColor.GRAY + "Set the custom model data of this",
                    ChatColor.GRAY + "item. Useful with a resource pack.",
                    ChatColor.GREEN + "Left click to set custom model data"
            ))
            .build();
    //</editor-fold>

    // The following are mappings from what some random person will
    // describe an enchantment/potion effect to the actual representation.
    // The following also contains mappings from ChatColor to Bukkit Color.
    //<editor-fold desc="Mappings">
    static {
        // Initialize the mappings
        chatToColor.put(ChatColor.BLACK, Color.BLACK);
        chatToColor.put(ChatColor.AQUA, Color.AQUA);
        chatToColor.put(ChatColor.BLUE, Color.fromRGB(0x5555ff));
        chatToColor.put(ChatColor.DARK_AQUA, Color.fromRGB(0x00aaaa));
        chatToColor.put(ChatColor.DARK_BLUE, Color.fromRGB(0x0000aa));
        chatToColor.put(ChatColor.DARK_GRAY, Color.fromRGB(0x555555));
        chatToColor.put(ChatColor.DARK_GREEN, Color.fromRGB(0x00aa00));
        chatToColor.put(ChatColor.DARK_PURPLE, Color.fromRGB(0xaa00aa));
        chatToColor.put(ChatColor.DARK_RED, Color.fromRGB(0xaa0000));
        chatToColor.put(ChatColor.GOLD, Color.fromRGB(0xffaa00));
        chatToColor.put(ChatColor.GRAY, Color.fromRGB(0xaaaaaa));
        chatToColor.put(ChatColor.GREEN, Color.fromRGB(0x55ff55));
        chatToColor.put(ChatColor.LIGHT_PURPLE, Color.fromRGB(0xff55ff));
        chatToColor.put(ChatColor.RED, Color.fromRGB(0xff5555));
        chatToColor.put(ChatColor.WHITE, Color.WHITE);
        chatToColor.put(ChatColor.YELLOW, Color.fromRGB(0xffff55));

        // I assume people will call it in the following ways
        // None of these have spaces as we will remove the space from
        // the message before checking.
        enchantments.put("aquaaffinity", Enchantment.WATER_WORKER);
        enchantments.put("waterworker", Enchantment.WATER_WORKER);
        enchantments.put("waterdig", Enchantment.WATER_WORKER);
        enchantments.put("watermine", Enchantment.WATER_WORKER);
        enchantments.put("watermining", Enchantment.DIG_SPEED);
        enchantments.put("underwatermine", Enchantment.WATER_WORKER);
        enchantments.put("underwatermining", Enchantment.WATER_WORKER);

        enchantments.put("baneofarthropods", Enchantment.DAMAGE_ARTHROPODS);
        enchantments.put("damagearthropods", Enchantment.DAMAGE_ARTHROPODS);
        enchantments.put("arthropods", Enchantment.DAMAGE_ARTHROPODS);

        enchantments.put("blastprotection", Enchantment.PROTECTION_EXPLOSIONS);
        enchantments.put("explosionprotection", Enchantment.PROTECTION_EXPLOSIONS);
        enchantments.put("explosionsprotection", Enchantment.PROTECTION_EXPLOSIONS);
        enchantments.put("protectionblast", Enchantment.PROTECTION_EXPLOSIONS);
        enchantments.put("protectionexplosion", Enchantment.PROTECTION_EXPLOSIONS);
        enchantments.put("protectionexplosions", Enchantment.PROTECTION_EXPLOSIONS);
        enchantments.put("protectiontnt", Enchantment.PROTECTION_EXPLOSIONS);
        enchantments.put("tntprotection", Enchantment.PROTECTION_EXPLOSIONS);

        enchantments.put("channeling", Enchantment.CHANNELING);

        enchantments.put("curseofbinding", Enchantment.BINDING_CURSE);
        enchantments.put("bindingcurse", Enchantment.BINDING_CURSE);
        enchantments.put("cursebinding", Enchantment.BINDING_CURSE);
        enchantments.put("bindcurse", Enchantment.BINDING_CURSE);
        enchantments.put("binding", Enchantment.BINDING_CURSE);
        enchantments.put("bind", Enchantment.BINDING_CURSE);

        enchantments.put("curseofvanishing", Enchantment.VANISHING_CURSE);
        enchantments.put("vanishingcurse", Enchantment.VANISHING_CURSE);
        enchantments.put("cursevanish", Enchantment.VANISHING_CURSE);
        enchantments.put("vanishcurse", Enchantment.VANISHING_CURSE);
        enchantments.put("vanishing", Enchantment.VANISHING_CURSE);
        enchantments.put("vanish", Enchantment.VANISHING_CURSE);

        enchantments.put("depthstrider", Enchantment.DEPTH_STRIDER);
        enchantments.put("underwaterspeed", Enchantment.DEPTH_STRIDER);
        enchantments.put("waterspeed", Enchantment.DEPTH_STRIDER);

        enchantments.put("efficiency", Enchantment.DIG_SPEED);
        enchantments.put("digspeed", Enchantment.DIG_SPEED);
        enchantments.put("minespeed", Enchantment.DIG_SPEED);
        enchantments.put("miningspeed", Enchantment.DIG_SPEED);
        enchantments.put("speedmining", Enchantment.DIG_SPEED);
        enchantments.put("speedmine", Enchantment.DIG_SPEED);
        enchantments.put("speeddig", Enchantment.DIG_SPEED);

        enchantments.put("featherfalling", Enchantment.PROTECTION_FALL);
        enchantments.put("protectionfall", Enchantment.PROTECTION_FALL);
        enchantments.put("fallprotection", Enchantment.PROTECTION_FALL);

        enchantments.put("fireaspect", Enchantment.FIRE_ASPECT);
        enchantments.put("swordfire", Enchantment.FIRE_ASPECT);

        enchantments.put("fireprotection", Enchantment.PROTECTION_FIRE);
        enchantments.put("protectionfire", Enchantment.PROTECTION_FIRE);
        enchantments.put("burnprotection", Enchantment.PROTECTION_FIRE);
        enchantments.put("protectionburn", Enchantment.PROTECTION_FIRE);

        enchantments.put("flame", Enchantment.ARROW_FIRE);
        enchantments.put("arrowfire", Enchantment.ARROW_FIRE);
        enchantments.put("firearrow", Enchantment.ARROW_FIRE);

        enchantments.put("fortune", Enchantment.LOOT_BONUS_BLOCKS);
        enchantments.put("lootblocks", Enchantment.LOOT_BONUS_BLOCKS);
        enchantments.put("lootbonus blocks", Enchantment.LOOT_BONUS_BLOCKS);

        enchantments.put("frostwalker", Enchantment.FROST_WALKER);

        enchantments.put("impaling", Enchantment.IMPALING);

        enchantments.put("infinity", Enchantment.ARROW_INFINITE);
        enchantments.put("infinite", Enchantment.ARROW_INFINITE);
        enchantments.put("arrowinfinity", Enchantment.ARROW_INFINITE);
        enchantments.put("arrowinfinite", Enchantment.ARROW_INFINITE);

        enchantments.put("knockback", Enchantment.KNOCKBACK);
        enchantments.put("kb", Enchantment.KNOCKBACK);

        enchantments.put("looting", Enchantment.LOOT_BONUS_MOBS);
        enchantments.put("lootmobs", Enchantment.LOOT_BONUS_MOBS);
        enchantments.put("lootbonus mobs", Enchantment.LOOT_BONUS_MOBS);

        enchantments.put("loyalty", Enchantment.LOYALTY);

        enchantments.put("luckofthesea", Enchantment.LUCK);
        enchantments.put("luckofsea", Enchantment.LUCK);
        enchantments.put("sealuck", Enchantment.LUCK);
        enchantments.put("lucksea", Enchantment.LUCK);

        enchantments.put("lure", Enchantment.LURE);

        enchantments.put("mending", Enchantment.MENDING);

        enchantments.put("multishot", Enchantment.MULTISHOT);

        enchantments.put("piercing", Enchantment.PIERCING);

        enchantments.put("power", Enchantment.ARROW_DAMAGE);
        enchantments.put("arrowdamage", Enchantment.ARROW_DAMAGE);

        enchantments.put("projectileprotection", Enchantment.PROTECTION_PROJECTILE);
        enchantments.put("protectionprojectile", Enchantment.PROTECTION_PROJECTILE);
        enchantments.put("arrowprotection", Enchantment.PROTECTION_PROJECTILE);
        enchantments.put("protectionarrow", Enchantment.PROTECTION_PROJECTILE);

        enchantments.put("protection", Enchantment.PROTECTION_ENVIRONMENTAL);
        enchantments.put("protectionenvironmental", Enchantment.PROTECTION_ENVIRONMENTAL);

        enchantments.put("punch", Enchantment.ARROW_KNOCKBACK);
        enchantments.put("arrowknockback", Enchantment.ARROW_KNOCKBACK);
        enchantments.put("knockbackarrow", Enchantment.ARROW_KNOCKBACK);
        enchantments.put("arrowkb", Enchantment.ARROW_KNOCKBACK);
        enchantments.put("kbarrow", Enchantment.ARROW_KNOCKBACK);
        enchantments.put("bowkb", Enchantment.ARROW_KNOCKBACK);
        enchantments.put("kbbow", Enchantment.ARROW_KNOCKBACK);

        enchantments.put("quickcharge", Enchantment.QUICK_CHARGE);

        enchantments.put("respiration", Enchantment.OXYGEN);
        enchantments.put("oxygen", Enchantment.OXYGEN);

        enchantments.put("riptide", Enchantment.RIPTIDE);

        enchantments.put("sharpness", Enchantment.DAMAGE_ALL);
        enchantments.put("damageall", Enchantment.DAMAGE_ALL);
        enchantments.put("damage", Enchantment.DAMAGE_ALL);

        enchantments.put("silktouch", Enchantment.SILK_TOUCH);

        enchantments.put("smite", Enchantment.DAMAGE_UNDEAD);
        enchantments.put("damageundead", Enchantment.DAMAGE_UNDEAD);
        enchantments.put("undeaddamage", Enchantment.DAMAGE_UNDEAD);

        enchantments.put("soulspeed", Enchantment.SOUL_SPEED);

        enchantments.put("sweepingedge", Enchantment.SWEEPING_EDGE);
        enchantments.put("sweep", Enchantment.SWEEPING_EDGE);
        enchantments.put("sweepedge", Enchantment.SWEEPING_EDGE);

        enchantments.put("thorns", Enchantment.THORNS);
        enchantments.put("thorn", Enchantment.THORNS);
        enchantments.put("damageback", Enchantment.THORNS);

        enchantments.put("unbreaking", Enchantment.DURABILITY);
        enchantments.put("durability", Enchantment.DURABILITY);

        // I assume people will call it in the following ways
        potions.put("speed", PotionType.SPEED);
        potions.put("fast", PotionType.SPEED);
        potions.put("speedfast", PotionType.SPEED);

        potions.put("slowness", PotionType.SLOWNESS);
        potions.put("slow", PotionType.SLOWNESS);
        potions.put("speedslow", PotionType.SLOWNESS);

        potions.put("strength", PotionType.STRENGTH);
        potions.put("damageall", PotionType.STRENGTH);
        potions.put("damage", PotionType.STRENGTH);
        potions.put("strong", PotionType.STRENGTH);

        potions.put("weakness", PotionType.WEAKNESS);
        potions.put("weak", PotionType.WEAKNESS);

        potions.put("instanthealth", PotionType.INSTANT_HEAL);
        potions.put("instantheal", PotionType.INSTANT_HEAL);
        potions.put("quickhealth", PotionType.INSTANT_HEAL);
        potions.put("quickheal", PotionType.INSTANT_HEAL);

        potions.put("instantdamage", PotionType.INSTANT_DAMAGE);
        potions.put("quickdamage", PotionType.INSTANT_DAMAGE);

        potions.put("jumpboost", PotionType.JUMP);
        potions.put("jump", PotionType.JUMP);
        potions.put("jumphigh", PotionType.JUMP);
        potions.put("jumphigher", PotionType.JUMP);

        potions.put("regeneration", PotionType.REGEN);
        potions.put("regen", PotionType.REGEN);

        potions.put("fireresistance", PotionType.FIRE_RESISTANCE);
        potions.put("resistancefire", PotionType.FIRE_RESISTANCE);

        potions.put("waterbreathing", PotionType.WATER_BREATHING);
        potions.put("waterbreath", PotionType.WATER_BREATHING);

        potions.put("invisibility", PotionType.INVISIBILITY);
        potions.put("invisible", PotionType.INVISIBILITY);
        potions.put("vanish", PotionType.INVISIBILITY);
        potions.put("vanishing", PotionType.INVISIBILITY);

        potions.put("nightvision", PotionType.NIGHT_VISION);

        potions.put("poison", PotionType.POISON);

        potions.put("slowfalling", PotionType.SLOW_FALLING);
        potions.put("slowfall", PotionType.SLOW_FALLING);
        potions.put("fallprotection", PotionType.SLOW_FALLING);
        potions.put("protectionfall", PotionType.SLOW_FALLING);
    }
    //</editor-fold>

    private Player player;
    /**
     * This callback will be called when we finished editing an item,
     * either when the item is taken out from the inventory, or the
     * player closed the inventory and the item is not taken out.
     * <p>
     * In the case of closing the inventory, the boolean return
     * value determines whether the item should be dropped or not,
     * otherwise, the return value determines whether the user will
     * get the item from clicking or not.
     */
    private final Function<ItemStack, Boolean> itemCallback;
    /**
     * This callback will be called when the inventory is closed without
     * an item inside it.
     */
    private final Runnable cancelCallback;
    private final Inventory inv;
    private ItemStack item;

    // The following are used by listeners to determine what the user
    // is doing.
    private boolean naming = false;
    private int loring = 0;  // Represents the line we're at
    private int enchanting = 0;  // Represents the stage (1 for enchantment, 2 for level) we're at
    private boolean coloring = false;
    private int potioning = 0;  // Represents the stage (1 for type, 2 for extended, 3 for upgraded, 4 for color) we're at
    private boolean cmding = false;  // Custom Model Data'ing

    private Enchantment enchantment;  // This is the enchantment that the user specified.
    // We have two stages in the enchantment part so we will have to save that state.

    // This is the potion type that the user specified.
    // We have three stages so we will have to save that state.
    private PotionType potionType;
    private boolean extended;  // This is whether the potion should be extended.
    private boolean upgraded;  // This is whether the potion should be upgraded.

    public ItemCreator(Player player, Function<ItemStack, Boolean> itemCallback, Runnable cancelCallback) {
        this.player = player;
        this.item = null;
        this.itemCallback = itemCallback;
        this.cancelCallback = cancelCallback;

        inv = Bukkit.createInventory(null, 45, "Item Creator");
        inv.setItem(13, NO_ITEM);

        Bukkit.getPluginManager().registerEvents(this, ItemEditor.inst);
    }

    /**
     * Open the item editor for the player.
     */
    public void open() {
        this.player.openInventory(inv);
    }

    /**
     * This is an utility method that trys to parse color from a
     * string.
     * @param player The player that initiated this action. We will send error messages to this player.
     * @param text The text to parse from
     * @return The parsed color
     */
    public Color getColorFromText(Player player, String text) {
        Color c;
        try {
            ChatColor color = ChatColor.valueOf(text.replace(" ", "_").toUpperCase());
            if (color.isColor()) {
                // Use the chat color for the color
                c = chatToColor.get(color);
            } else {
                throw new IllegalArgumentException();  // Force to the catch block
            }
        } catch (IllegalArgumentException ex) {
            try {
                c = Color.fromRGB(Integer.parseInt(text.replace("#", ""), 16));
            } catch (NumberFormatException exc) {
                player.sendMessage(ItemEditor.getMsg("item-creator.invalid-color"));
                return null;
            }
        }
        return c;
    }

    @EventHandler
    public void onSetItem(InventoryClickEvent e) {
        if (this.player == null) {
            return;
        }

        Player player = (Player) e.getWhoClicked();
        if (player.getUniqueId().equals(this.player.getUniqueId())) {
            if (e.getSlot() == 13) {  // If we are clicking on the item
                ItemStack item = e.getCursor();
                if (item == null || item.getType() == Material.AIR) {
                    // If we have an item in the inventory, then we will attempt
                    // to remove that from the inventory and finish editing.
                    if (this.item != null) {
                        inv.setItem(13, NO_ITEM);  // We remove the item
                        for (int i = 28; i < 35; i++) {
                            inv.setItem(i, null);  // and all of the operations.
                        }

                        if (itemCallback.apply(this.item)) {
                            // Return item if applicable
                            ItemStack i = this.item.clone();
                            Bukkit.getScheduler().runTask(ItemEditor.inst, () -> player.setItemOnCursor(i));
                        }
                        this.item = null;
                    }
                    player.updateInventory();
                    return;
                }

                inv.setItem(13, item);  // Set the item
                inv.setItem(28, SET_NAME);  // and all of the operations.
                inv.setItem(29, SET_LORE);
                inv.setItem(30, ENCHANT);
                if (item.getType().toString().contains("LEATHER")) {  // Check for applicability on leather armor.
                    inv.setItem(31, LEATHER_COLOR);
                } else {
                    inv.setItem(31, LEATHER_COLOR_NOT_APPLICABLE);
                }
                if (item.getType().toString().contains("POTION")) {  // Check for applicability on potions.
                    inv.setItem(32, POTION_COLOR);
                } else {
                    inv.setItem(32, POTION_COLOR_NOT_APPLICABLE);
                }
                inv.setItem(33, item.getItemMeta().isUnbreakable() ? UNBREAKABLE_ON : UNBREAKABLE);
                inv.setItem(34, CUSTOM_MODEL_DATA);

                player.updateInventory();  // Must do this when updating the inventory in an inventory click event.
                this.item = item.clone();
                // Remove the item on the cursor
                player.setItemOnCursor(null);
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onSetName(AsyncPlayerChatEvent e) {
        if (this.player == null) {
            return;
        }
        Player player = e.getPlayer();
        if (naming && player.getUniqueId().equals(this.player.getUniqueId())) {
            String name = ChatColor.translateAlternateColorCodes('&', e.getMessage());
            ItemMeta meta = this.item.getItemMeta();
            meta.setDisplayName(ChatColor.RESET /* So we won't get that italic effect */ + name);
            this.item.setItemMeta(meta);
            inv.setItem(13, this.item);
            Bukkit.getScheduler().runTask(ItemEditor.inst, () -> player.openInventory(inv));
            naming = false;
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onSetLore(AsyncPlayerChatEvent e) {
        if (this.player == null) {
            return;
        }
        Player player = e.getPlayer();
        if (loring != 0 && player.getUniqueId().equals(this.player.getUniqueId())) {
            ItemMeta meta = this.item.getItemMeta();
            e.setCancelled(true);
            if (e.getMessage().equals("DELETE")) {
                if (!meta.hasLore() || meta.getLore().isEmpty()) {
                    player.sendMessage(ItemEditor.getMsg("item-creator.set-lore.no-last-line"));
                    return;
                }
                List<String> lore = new ArrayList<>(meta.getLore());
                lore.remove(lore.size() - 1);
                meta.setLore(lore);
                this.item.setItemMeta(meta);
                player.sendMessage(ItemEditor.getMsg("item-creator.set-lore.deleted"));
                return;
            } else if (e.getMessage().equals("END")) {
                inv.setItem(13, this.item);
                Bukkit.getScheduler().runTask(ItemEditor.inst, () -> player.openInventory(inv));
                loring = 0;
                return;
            }
            // Concatenate the original lore and the new lore
            List<String> lore = new ArrayList<>((meta.hasLore() ? meta.getLore().size() : 0) + 1);
            lore.addAll(meta.hasLore() ? meta.getLore() : Collections.emptyList());
            lore.add(ChatColor.RESET + ChatColor.translateAlternateColorCodes('&', e.getMessage()));
            meta.setLore(lore);
            this.item.setItemMeta(meta);

            // Proceed to next line
            loring++;
            player.sendMessage(ItemEditor.getMsg("item-creator.set-lore.set-line").replace("{LINE}",
                    ChatColor.RESET + ChatColor.translateAlternateColorCodes('&', e.getMessage())));
            player.sendMessage(ItemEditor.getMsg("item-creator.set-lore.msg").replace("{LINE}", String.valueOf(loring)));
        }
    }

    @EventHandler
    public void enchant(AsyncPlayerChatEvent e) {
        if (this.player == null) {
            return;
        }
        Player player = e.getPlayer();
        if (enchanting != 0 && player.getUniqueId().equals(this.player.getUniqueId())) {
            e.setCancelled(true);

            // Check if the user wants to cancel
            if (e.getMessage().equals("CANCEL")) {
                enchantment = null;
                enchanting = 0;
                Bukkit.getScheduler().runTask(ItemEditor.inst, () -> player.openInventory(inv));
                return;
            }

            if (enchanting == 1) {  // For the first stage, get the specific enchantment
                Enchantment ench = enchantments.get(e.getMessage());
                if (ench == null) {  // If the entered enchantment is invalid
                    player.sendMessage(ItemEditor.getMsg("item-creator.set-enchant.enchantment-invalid"));
                    enchanting = 0;  // Reset state
                    Bukkit.getScheduler().runTask(ItemEditor.inst, () -> player.openInventory(inv));
                    return;
                }
                this.enchantment = ench;  // Save the selected enchantment
                player.sendMessage(ItemEditor.getMsg("item-creator.set-enchant.level"));  // Send the message
            } else if (enchanting == 2) {  // For the second stage, check for the level
                try {
                    int level = Integer.parseInt(e.getMessage());
                    // Apply the enchantment
                    ItemMeta meta = this.item.getItemMeta();
                    meta.addEnchant(this.enchantment, level, true);
                    this.item.setItemMeta(meta);
                    // Update inventory and reopen it
                    inv.setItem(13, this.item);
                    this.enchanting = 0;  // Reset state
                    this.enchantment = null;
                    Bukkit.getScheduler().runTask(ItemEditor.inst, () -> player.openInventory(inv));
                } catch (NumberFormatException ex) {  // If the input isn't a number
                    player.sendMessage(ItemEditor.getMsg("item-creator.set-enchant.level-invalid"));
                    this.enchanting = 0;  // Reset state
                    this.enchantment = null;
                    Bukkit.getScheduler().runTask(ItemEditor.inst, () -> player.openInventory(inv));
                }
                return;
            }
            enchanting++;  // Move to the next stage
        }
    }

    @EventHandler
    public void setPotionData(AsyncPlayerChatEvent e) {
        if (this.player == null) {
            return;
        }
        Player player = e.getPlayer();
        if (potioning != 0 && player.getUniqueId().equals(this.player.getUniqueId())) {
            e.setCancelled(true);

            // Check if the user wants to cancel
            if (e.getMessage().equals("CANCEL")) {
                potionType = null;
                extended = false;  // Reset state
                upgraded = false;
                potioning = 0;
                Bukkit.getScheduler().runTask(ItemEditor.inst, () -> player.openInventory(inv));
                return;
            }

            if (potioning == 1) {  // For the first stage, get the potion type
                PotionType potionType = potions.get(e.getMessage());
                if (potionType == null) {  // If the entered potion type is invalid
                    player.sendMessage(ItemEditor.getMsg("item-creator.set-potion-data.potion-invalid"));
                    potioning = 0;  // Reset state
                    Bukkit.getScheduler().runTask(ItemEditor.inst, () -> player.openInventory(inv));
                    return;
                }
                this.potionType = potionType;  // Save the selected potion type
                player.sendMessage(ItemEditor.getMsg("item-creator.set-potion-data.extended"));
            } else if (potioning == 2) {  // For the second stage, get the extended status
                extended = e.getMessage().equalsIgnoreCase("yes");
                if (extended) {
                    potioning += 2;  // You can't both extend and upgrade a potion
                    // So we skip the upgrading step
                    player.sendMessage(ItemEditor.getMsg("item-creator.set-potion-data.color"));
                    return;
                }
                player.sendMessage(ItemEditor.getMsg("item-creator.set-potion-data.upgraded"));
            } else if (potioning == 3) {  // For the third stage, get the upgraded status
                upgraded = e.getMessage().equalsIgnoreCase("yes");
                player.sendMessage(ItemEditor.getMsg("item-creator.set-potion-data.color"));
            } else if (potioning == 4) {  // For the last stage, get the color
                PotionMeta meta = (PotionMeta) this.item.getItemMeta();
                meta.setBasePotionData(new PotionData(this.potionType, this.extended, this.upgraded));

                if (!e.getMessage().equals("DEFAULT")) {  // If the color isn't default
                    Color c = getColorFromText(player, e.getMessage());
                    if (c == null) {
                        return;
                    }
                    meta.setColor(c);
                }

                this.item.setItemMeta(meta);
                inv.setItem(13, this.item);  // Update inventory
                Bukkit.getScheduler().runTask(ItemEditor.inst, () -> player.openInventory(inv));

                this.potionType = null;
                this.extended = false;  // Reset state
                this.upgraded = false;
                this.potioning = 0;
                return;
            }
            potioning++;
        }
    }

    @EventHandler
    public void setLeatherColor(AsyncPlayerChatEvent e) {
        if (this.player == null) {
            return;
        }
        Player player = e.getPlayer();
        if (coloring && player.getUniqueId().equals(this.player.getUniqueId())) {
            LeatherArmorMeta meta = (LeatherArmorMeta) this.item.getItemMeta();
            Color c = getColorFromText(player, e.getMessage());
            if (c == null) {
                return;
            }
            meta.setColor(c);
            this.item.setItemMeta(meta);
            inv.setItem(13, this.item);
            Bukkit.getScheduler().runTask(ItemEditor.inst, () -> player.openInventory(inv));
            coloring = false;
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void setCustomModelData(AsyncPlayerChatEvent e) {
        if (this.player == null) {
            return;
        }
        Player player = e.getPlayer();
        if (cmding && player.getUniqueId().equals(this.player.getUniqueId())) {
            try {
                int data = Integer.parseInt(e.getMessage());ItemMeta meta = this.item.getItemMeta();
                meta.setCustomModelData(data);
                this.item.setItemMeta(meta);
                inv.setItem(13, this.item);
            } catch (NumberFormatException ex) {
                player.sendMessage(ItemEditor.getMsg("item-creator.invalid-integer"));
            }

            Bukkit.getScheduler().runTask(ItemEditor.inst, () -> player.openInventory(inv));
            cmding = false;
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onOperationClick(InventoryClickEvent e) {
        if (this.player == null) {
            return;
        }
        Player player = (Player) e.getWhoClicked();
        if (player.getUniqueId().equals(this.player.getUniqueId())) {
            if (this.item == null) {
                return;
            }

            // Set op state and send message accordingly. We also check for applicability here.
            if (e.getSlot() == 28) {
                naming = true;
                player.sendMessage(ItemEditor.getMsg("item-creator.set-name"));
                player.closeInventory();
            } else if (e.getSlot() == 29) {
                loring = 1;
                player.sendMessage(ItemEditor.getMsg("item-creator.set-lore.msg").replace("{LINE}", "1"));
                player.closeInventory();
            } else if (e.getSlot() == 30) {
                enchanting = 1;
                player.sendMessage(ItemEditor.getMsg("item-creator.set-enchant.name"));
                player.closeInventory();
            } else if (e.getSlot() == 31 && this.item.getType().toString().contains("LEATHER")) {
                coloring = true;
                player.sendMessage(ItemEditor.getMsg("item-creator.set-leather-color"));
                player.closeInventory();
            } else if (e.getSlot() == 32 && this.item.getType().toString().contains("POTION")) {
                potioning = 1;
                player.sendMessage(ItemEditor.getMsg("item-creator.set-potion-data.potion"));
                player.closeInventory();
            } else if (e.getSlot() == 33) {
                // Except for unbreakable, which is simple enough that we handle it directly in
                // this method.
                ItemMeta meta = this.item.getItemMeta();
                meta.setUnbreakable(!meta.isUnbreakable());
                if (meta.isUnbreakable()) {
                    inv.setItem(33, UNBREAKABLE_ON);
                } else {
                    inv.setItem(33, UNBREAKABLE);
                }
                this.item.setItemMeta(meta);
                inv.setItem(13, this.item);
                player.updateInventory();
            } else if (e.getSlot() == 34) {
                cmding = true;
                player.sendMessage(ItemEditor.getMsg("item-creator.set-custom-model-data"));
                player.closeInventory();
            }
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        if (this.player == null) {
            return;
        }
        if (naming || loring != 0 || enchanting != 0 || coloring || potioning != 0 || cmding) {
            return;  // Do not cancel when we aren't intending it.
        }
        if (e.getPlayer().getUniqueId().equals(this.player.getUniqueId())) {
            // Call cancel callback and unregister listener.
            if (this.item == null) {
                cancelCallback.run();
            } else {
                // Drop the item if applicable
                if (itemCallback.apply(this.item)) {
                    this.player.getWorld().dropItem(this.player.getLocation(), this.item);
                }
            }
            this.player = null;
        }
    }
}
