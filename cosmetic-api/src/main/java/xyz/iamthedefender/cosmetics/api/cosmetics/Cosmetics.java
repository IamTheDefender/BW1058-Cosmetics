package xyz.iamthedefender.cosmetics.api.cosmetics;

import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * Represents an abstract category of cosmetics.
 */
public abstract class Cosmetics {

    /**
     * Get the item stack associated with this cosmetic.
     * This item stack will be used to display the cosmetic in the shop.
     *
     * @return item stack associated with this cosmetic.
     */
    public abstract ItemStack getItem();

    /**
     * Get the base64 texture of the head.
     * This is only used if the item stack is a player head.
     *
     * @return base64 texture of the head.
     */
    public abstract String base64();

    /**
     * Get the identifier of this cosmetic.
     * This identifier will be used to save the cosmetic in the config.
     *
     * @return identifier of this cosmetic.
     */
    public abstract String getIdentifier();

    /**
     * Get the display name of this cosmetic.
     * This display name will be used when displaying the cosmetic in the shop.
     *
     * @return display name of this cosmetic.
     */
    public abstract String getDisplayName();

    /**
     * Get the lore of this cosmetic.
     * This lore will be used when displaying the cosmetic in the shop.
     *
     * @return lore of this cosmetic.
     */
    public abstract List<String> getLore();

    /**
     * Get the price of this cosmetic.
     * This price will be used when displaying the cosmetic in the shop.
     *
     * @return price of this cosmetic.
     */
    public abstract int getPrice();

    /**
     * Get the rarity of this cosmetic.
     * This rarity will be used when displaying the cosmetic in the shop.
     *
     * @return rarity of this cosmetic.
     */
    public abstract RarityType getRarity();

    /**
     * Register this cosmetic.
     */
    public abstract void register();
}
