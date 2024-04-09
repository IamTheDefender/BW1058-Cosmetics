package xyz.iamthedefender.cosmetics.category.woodskin.items;

import com.cryptomorin.xseries.XMaterial;
import xyz.iamthedefender.cosmetics.api.cosmetics.RarityType;
import xyz.iamthedefender.cosmetics.api.cosmetics.category.WoodSkin;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class OakPlank extends WoodSkin {
    @Override
    public ItemStack getItem() {
        return XMaterial.OAK_PLANKS.parseItem();
    }

    @Override
    public String base64() {
        return null;
    }

    @Override
    public String getIdentifier() {
        return "oak-plank";
    }

    @Override
    public String getDisplayName() {
        return "Oak Plank";
    }

    @Override
    public List<String> getLore() {
        return List.of("&7Select the Oak Plank Wood Skin", "&7to be used when placing wood", "&7blocks.");
    }

    @Override
    public int getPrice() {
        return 0;
    }

    @Override
    public RarityType getRarity() {
        return RarityType.COMMON;
    }

    @Override
    public ItemStack woodSkin() {
        return XMaterial.OAK_PLANKS.parseItem();
    }
}
