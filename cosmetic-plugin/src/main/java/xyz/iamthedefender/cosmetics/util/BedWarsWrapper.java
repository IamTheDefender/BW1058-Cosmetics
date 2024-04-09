package xyz.iamthedefender.cosmetics.util;

import com.andrei1058.bedwars.api.arena.team.ITeam;
import xyz.iamthedefender.cosmetics.api.handler.ITeamHandler;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;

public class BedWarsWrapper {

    public static ITeamHandler wrap(com.tomkeuper.bedwars.api.arena.team.ITeam team){
        return  new ITeamHandler() {
            @Override
            public Location getBed() {
                return team.getBed();
            }

            @Override
            public List<Player> getPlayers() {
                return team.getMembers();
            }

            @Override
            public String getName() {
                return team.getName();
            }

            @Override
            public Location getTeamUpgrades() {
                return team.getTeamUpgrades();
            }

            @Override
            public Location getShop() {
                return team.getShop();
            }

            @Override
            public Location getSpawn() {
                return team.getSpawn();
            }

            @Override
            public int getSize() {
                return team.getSize();
            }
        };
    }
    public static ITeamHandler wrap(ITeam team){
        return  new ITeamHandler() {
            @Override
            public Location getBed() {
                return team.getBed();
            }

            @Override
            public List<Player> getPlayers() {
                return team.getMembers();
            }

            @Override
            public String getName() {
                return team.getName();
            }

            @Override
            public Location getTeamUpgrades() {
                return team.getTeamUpgrades();
            }

            @Override
            public Location getShop() {
                return team.getShop();
            }

            @Override
            public Location getSpawn() {
                return team.getSpawn();
            }

            @Override
            public int getSize() {
                return team.getSize();
            }
        };
    }
}
