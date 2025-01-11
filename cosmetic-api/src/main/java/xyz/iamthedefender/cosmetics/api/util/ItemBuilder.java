package xyz.iamthedefender.cosmetics.api.util;

import com.hakan.core.utils.ColorUtil;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ItemBuilder {

    private ItemStack itemStack;

    public ItemBuilder(){
        itemStack = new ItemStack(Material.PAPER);
    }

    public ItemBuilder(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public ItemBuilder material(Material material){
        itemStack.setType(material);
        return this;
    }

    public ItemBuilder itemStack(ItemStack itemStack){
        this.itemStack = itemStack;
        return this;
    }

    public ItemBuilder amount(int amount){
        itemStack.setAmount(amount);
        return this;
    }

    public ItemBuilder durability(short durability){
        itemStack.setDurability(durability);
        return this;
    }

    public ItemBuilder name(String name){
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ColorUtil.colored(name));
        itemStack.setItemMeta(itemMeta);

        return this;
    }


    public ItemBuilder lore(String... lore){
        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.setLore(new ArrayList<>(List.of(lore)).stream().map(ColorUtil::colored).collect(Collectors.toList()));
        itemStack.setItemMeta(itemMeta);

        return this;
    }

    public ItemBuilder lore(List<String> lore){
        return lore(lore.toArray(String[]::new));
    }

    public ItemBuilder customModelData(int data){
        //TODO
        ItemMeta itemMeta = itemStack.getItemMeta();
       // itemMeta.setCustomModelData(data);
        itemStack.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder enchantment(Enchantment enchantment, int level){
        itemStack.addUnsafeEnchantment(enchantment, level);
        return this;
    }

    public ItemBuilder itemFlag(ItemFlag... itemFlags){
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.addItemFlags(itemFlags);
        itemStack.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder addAllItemFlags(){
        return itemFlag(ItemFlag.values());
    }

    public ItemStack build(){
        return itemStack.clone();
    }



}
