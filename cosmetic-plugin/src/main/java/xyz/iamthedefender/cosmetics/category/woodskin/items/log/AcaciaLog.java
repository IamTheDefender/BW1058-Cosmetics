package xyz.iamthedefender.cosmetics.category.woodskin.items.log;

import com.cryptomorin.xseries.XMaterial;
import org.bukkit.inventory.ItemStack;
import xyz.iamthedefender.cosmetics.api.cosmetics.RarityType;
import xyz.iamthedefender.cosmetics.api.cosmetics.category.WoodSkin;

import java.util.List;

public class AcaciaLog extends WoodSkin {
    @Override
    public ItemStack getItem() {
        return XMaterial.ACACIA_LOG.parseItem();
    }

    @Override
    public String base64() {
        return null;
    }

    @Override
    public String getIdentifier() {
        return "acacia-log";
    }

    @Override
    public String getDisplayName() {
        return "Acacia Log";
    }

    @Override
    public List<String> getLore() {
        return List.of("&7Select the Acacia Log Wood Skin", "&7to be used when placing wood", "&7blocks.");
    }

    @Override
    public int getPrice() {
        return 60000;
    }

    @Override
    public RarityType getRarity() {
        return RarityType.LEGENDARY;
    }

    @Override
    public ItemStack woodSkin() {
        return XMaterial.ACACIA_LOG.parseItem();
    }
}
