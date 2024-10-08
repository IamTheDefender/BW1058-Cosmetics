package xyz.iamthedefender.cosmetics.category.victorydance.items;

import com.cryptomorin.xseries.XMaterial;
import com.hakan.core.HCore;
import com.hakan.core.skin.Skin;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;
import net.citizensnpcs.trait.LookClose;
import net.citizensnpcs.trait.SkinTrait;
import net.citizensnpcs.util.NMS;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import xyz.iamthedefender.cosmetics.api.cosmetics.RarityType;
import xyz.iamthedefender.cosmetics.api.cosmetics.category.VictoryDance;
import xyz.iamthedefender.cosmetics.api.util.Utility;
import xyz.iamthedefender.cosmetics.category.victorydance.util.UsefulUtilsVD;
import xyz.iamthedefender.cosmetics.util.MathUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.bukkit.Bukkit.getLogger;

public class TwerkApocalypseDance extends VictoryDance {
    @Override
    public ItemStack getItem() {
        return XMaterial.LEATHER_BOOTS.parseItem();
    }

    @Override
    public String base64() {
        return null;
    }

    @Override
    public String getIdentifier() {
        return "twerk-apocalypse";
    }

    @Override
    public String getDisplayName() {
        return "Twerk Apocalypse";
    }

    @Override
    public List<String> getLore() {
        return List.of("&7Spawns tons of more yourself and", "&7have them twerk.");
    }

    @Override
    public int getPrice() {
        return 100000;
    }

    @Override
    public RarityType getRarity() {
        return RarityType.LEGENDARY;
    }

    @Override
    public void execute(Player winner) {
        Skin skin = null;
        getLogger().info("Trying to find the skin of " + winner.getName());
        try{
            List<String> values = Arrays.asList(Utility.getFromName(winner.getName()));
            skin = new Skin(values.get(0), values.get(1));
        }catch (Exception e){}


        NPCRegistry registry = CitizensAPI.getNPCRegistry();
        List<NPC> npcs = new ArrayList<>();

        Skin finalSkin = skin;
        HCore.syncScheduler().every(1).limit(15).run(() -> {
            List<Block> freeBlocks = UsefulUtilsVD.getFreeBlocks(winner.getLocation());
            Location loc = freeBlocks.get(MathUtil.getRandom(0, freeBlocks.size() -1)).getLocation();
            loc.setYaw((float) MathUtil.getRandom(0.0D, 360.0D));

            if (loc.getBlock().getType() == Material.AIR && loc.subtract(0,1,0).getBlock().getType() != Material.AIR) {
                NPC npc = registry.createNPC(EntityType.PLAYER, winner.getDisplayName());
                if(finalSkin != null){
                    npc.getOrAddTrait(SkinTrait.class).setTexture(finalSkin.getTexture(), finalSkin.getSignature());
                }

                npc.getOrAddTrait(LookClose.class).lookClose(false);
                npc.spawn(loc.add(0,1,0));
                npcs.add(npc);

                HCore.syncScheduler().every(1, TimeUnit.SECONDS).run(() -> {
                    if (npc.isSpawned()) {
                        Player npcP  = (Player) npc.getEntity();
                        npcP.setSneaking(!npcP.isSneaking());
                        NMS.setSneaking(npcP, npcP.isSneaking());
                    }
                });
            }
        });

        Bukkit.getScheduler().runTaskLater(HCore.getInstance(), () -> {
            for (NPC npc : npcs) {
                npc.destroy();
            }
        }, 20L * 9 + 10L);

    }
}
