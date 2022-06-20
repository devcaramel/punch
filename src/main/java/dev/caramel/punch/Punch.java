// LICENSE - The MIT License (MIT)

package dev.caramel.punch;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.security.SecureRandom;
import java.util.Random;

public final class Punch extends JavaPlugin implements Listener, CommandExecutor {

    public final Random RANDOM = new SecureRandom();

    @Override
    public void onEnable() {

        saveDefaultConfig();

        Bukkit.getPluginManager().registerEvents(this, this);
        getCommand("punch").setExecutor(this);

    }

    @EventHandler
    public void onPunch(EntityDamageByEntityEvent event) {

        if (event.getDamager() instanceof Player attacker) {

            ItemStack weapon = attacker.getInventory().getItemInMainHand();

            boolean isStick = weapon.getType() == Material.STICK;
            boolean matchingName = weapon.getItemMeta().getDisplayName().equals(ChatColor.RED + "Punching Stick");

            if (isStick && matchingName) {
                punch(event.getEntity(), attacker);
            }

        }

    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player player) {

            ItemStack stick = new ItemStack(Material.STICK);

            ItemMeta meta = stick.getItemMeta();
            meta.setDisplayName(ChatColor.RED + "Punching Stick");

            stick.setItemMeta(meta);

            player.getInventory().addItem(stick);

        } else {

            sender.sendMessage("Only players can use this command.");

        }

        return true;

    }

    public void punch(Entity defender, Player attacker) {

        int punchStrength = RANDOM.nextInt(getMaximumVelocity()-getMinimumVelocity()+1)+getMinimumVelocity();

        Vector upward = new Vector(0, getVerticalVelocity(), 0);

        defender.setVelocity(attacker.getEyeLocation().getDirection().multiply(punchStrength).add(upward));

    }

    private int getMinimumVelocity() {

        return getConfig().getInt("minimum-punch-strength", 5);

    }

    private int getMaximumVelocity() {

        return getConfig().getInt("maximum-punch-strength", 20);

    }

    private int getVerticalVelocity() {

        return getConfig().getInt("vertical-strength", 10);

    }

}
