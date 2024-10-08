package xyz.iamthedefender.cosmetics.category.woodskin.items;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.inventory.ItemStack;
import xyz.iamthedefender.cosmetics.api.cosmetics.RarityType;
import xyz.iamthedefender.cosmetics.api.cosmetics.category.WoodSkin;

import java.util.List;

public class SprucePlank extends WoodSkin {
    @Override
    public ItemStack getItem() {
        return XMaterial.SPRUCE_PLANKS.parseItem();
    }

    @Override
    public String base64() {
        return null;
    }

    @Override
    public String getIdentifier() {
        return "spruce-plank";
    }

    @Override
    public String getDisplayName() {
        return "Spruce Plank";
    }

    @Override
    public List<String> getLore() {
        return List.of("&7Select the Spruce Plank Wood Skin", "&7to be used when placing wood", "&7blocks.");
    }

    @Override
    public int getPrice() {
        return 20000;
    }

    @Override
    public RarityType getRarity() {
        return RarityType.RARE;
    }

    @Override
    public ItemStack woodSkin() {
        return XMaterial.SPRUCE_PLANKS.parseItem();
    }
}
