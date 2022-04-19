// LICENSE - The MIT License (MIT)

package dev.wither.punch;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;
import org.checkerframework.common.reflection.qual.GetClass;

import java.security.SecureRandom;
import java.util.Random;

/**
 * Punch is a plugin that will launch a player in the direction they are facing.
 */
public final class Punch extends JavaPlugin implements Listener, CommandExecutor {

    @Override
    public void onEnable() {

        saveDefaultConfig();

        Bukkit.getPluginManager().registerEvents(this, this);
        getCommand("punch").setExecutor(this);

    }

    @Override
    public void onDisable() {

    }

    private int getMaximumVelocity() {

        try {

            return getConfig().getInt("punch-strength-maximum");

        } catch (NullPointerException npe) {

            return 10;

        }

    }

    @EventHandler
    public void onPunch(EntityDamageByEntityEvent event) {

        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {

            Player attacker = (Player) event.getDamager();
            Player defender = (Player) event.getEntity();

            int punchStrength = RANDOM.nextInt(getMaximumVelocity())+1;

            defender.setVelocity(defender.getVelocity().add(new Vector(0, punchStrength, 0)));
            attacker.sendMessage("You have yeeted " + defender.getName() + " into the sky.");

        }

    }
    public static final Random RANDOM = new SecureRandom();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        String name = args[0];

        Player player = Bukkit.getPlayer(name);

        if (player == null) {

            sender.sendMessage("That player is not online you dumb shit.");
            return true;

        }

        int punchStrength = RANDOM.nextInt(getMaximumVelocity())+1;

        player.setVelocity(player.getVelocity().add(new Vector(0, punchStrength, 0)));

        return true;
    }


}
