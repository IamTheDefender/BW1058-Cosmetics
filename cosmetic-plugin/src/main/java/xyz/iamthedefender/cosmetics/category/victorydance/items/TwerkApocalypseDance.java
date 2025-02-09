package xyz.iamthedefender.cosmetics.category.victorydance.items;

import com.cryptomorin.xseries.XMaterial;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;
import net.citizensnpcs.npc.skin.Skin;
import net.citizensnpcs.trait.LookClose;
import net.citizensnpcs.trait.SkinTrait;
import net.citizensnpcs.util.NMS;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import xyz.iamthedefender.cosmetics.api.cosmetics.RarityType;
import xyz.iamthedefender.cosmetics.api.cosmetics.category.VictoryDance;
import xyz.iamthedefender.cosmetics.api.util.Run;
import xyz.iamthedefender.cosmetics.api.util.Utility;
import xyz.iamthedefender.cosmetics.category.victorydance.util.UsefulUtilsVD;
import xyz.iamthedefender.cosmetics.util.MathUtil;

import java.util.*;

import static org.bukkit.Bukkit.getLogger;

public class TwerkApocalypseDance extends VictoryDance {

    private final Map<Player, List<NPC>> npcsStorage = new HashMap<>();

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
        NPCRegistry registry = CitizensAPI.getNPCRegistry();

        addTask(winner, Run.every(() -> {
            List<Block> freeBlocks = UsefulUtilsVD.getFreeBlocks(winner.getLocation());
            Location loc = freeBlocks.get(MathUtil.getRandom(0, freeBlocks.size() -1)).getLocation();
            loc.setYaw((float) MathUtil.getRandom(0.0D, 360.0D));

            if (loc.getBlock().getType() == Material.AIR && loc.subtract(0,1,0).getBlock().getType() != Material.AIR) {
                NPC npc = registry.createNPC(EntityType.PLAYER, winner.getDisplayName());

                npc.getOrAddTrait(SkinTrait.class).setSkinName(winner.getName(), true);

                npc.getOrAddTrait(LookClose.class).lookClose(false);
                npc.spawn(loc.add(0,1,0));
                npcsStorage.computeIfAbsent(winner, k -> new ArrayList<>()).add(npc);

                Run.every((r) -> {
                    if(!npc.isSpawned()) {
                        r.cancel();
                        return;
                    }

                    Player npcP = (Player) npc.getEntity();
                    npcP.setSneaking(!npcP.isSneaking());
                    NMS.setSneaking(npcP, npcP.isSneaking());
                }, 20L);
            }
        }, 1L, 15));

        Run.delayed(() ->
                Optional.ofNullable(npcsStorage.get(winner)).ifPresent(list -> list.forEach(NPC::destroy))
                , 20L * 9 + 10L);
    }

    @Override
    public void stopExecution(Player winner) {
        super.stopExecution(winner);

        Optional.ofNullable(npcsStorage.get(winner)).ifPresent(list -> list.forEach(NPC::destroy));
    }
}
