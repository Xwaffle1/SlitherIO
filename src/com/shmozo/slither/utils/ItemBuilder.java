package com.shmozo.slither.utils;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemBuilder {

	private ItemStack itemStack;

	public ItemBuilder setItem(ItemStack item, String name, String[] lore) {
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		meta.setLore(Arrays.asList(lore));
		item.setItemMeta(meta);
		this.itemStack = item;
		return this;
	}

	public ItemBuilder setItem(Material material, short shortID, String name, String[] lore) {
		ItemStack tempItem = new ItemStack(material, 1, shortID);
		ItemMeta meta = tempItem.getItemMeta();
		meta.setDisplayName(name);
		meta.setLore(Arrays.asList(lore));
		tempItem.setItemMeta(meta);
		this.itemStack = tempItem;
		return this;
	}

	public ItemBuilder setItem(ItemStack item) {
		this.itemStack = item;
		return this;
	}

	public ItemBuilder addLore(String lore) {
		ItemStack item = itemStack;
		ItemMeta meta = item.getItemMeta();
		List<String> itemLore = meta.getLore();
		itemLore.add(lore);
		meta.setLore(itemLore);
		item.setItemMeta(meta);
		this.itemStack = item;
		return this;
	}

	public ItemStack build() {
		return itemStack;
	}

}
