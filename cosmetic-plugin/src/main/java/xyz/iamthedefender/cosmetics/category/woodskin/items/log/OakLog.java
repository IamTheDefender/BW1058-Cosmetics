package xyz.iamthedefender.cosmetics.category.woodskin.items.log;

import com.cryptomorin.xseries.XMaterial;
import xyz.iamthedefender.cosmetics.api.cosmetics.RarityType;
import xyz.iamthedefender.cosmetics.api.cosmetics.category.WoodSkin;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class OakLog extends WoodSkin {
    @Override
    public ItemStack getItem() {
        return XMaterial.OAK_LOG.parseItem();
    }

    @Override
    public String base64() {
        return null;
    }

    @Override
    public String getIdentifier() {
        return "oak-log";
    }

    @Override
    public String getDisplayName() {
        return "Oak Log";
    }

    @Override
    public List<String> getLore() {
        return List.of("&7Select the Oak Log Wood Skin", "&7to be used when placing wood", "&7blocks.");
    }

    @Override
    public int getPrice() {
        return 40000;
    }

    @Override
    public RarityType getRarity() {
        return RarityType.LEGENDARY;
    }

    @Override
    public ItemStack woodSkin() {
        return XMaterial.OAK_LOG.parseItem();
    }
}
