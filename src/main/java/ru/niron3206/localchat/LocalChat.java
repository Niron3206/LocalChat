package ru.niron3206.localchat;

import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import ru.niron3206.localchat.placeholder.ChatPrefixExpansion;

import java.io.File;

public final class LocalChat extends JavaPlugin {

    @Override
    public void onEnable() {
        File config = new File(getDataFolder() + File.separator + "config.yml");
        if(!config.exists()) {
            getLogger().info(ChatColor.GREEN + "Creating a new config.....");
            getConfig().options().copyDefaults(true);
            saveDefaultConfig();
        }
        getCommand("localchat").setExecutor(this);
        Bukkit.getPluginManager().registerEvents(new ChatEvent(this), this);
        new ChatPrefixExpansion().register();
        getLogger().info(ChatColor.GREEN + "Plugin enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info(ChatColor.RED + "Plugin disabled!");
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(cmd.getName().equalsIgnoreCase("localchat")
                && args.length == 1
                && args[0].equalsIgnoreCase("reload")
                && sender.hasPermission("localchat.reload")) {
            reloadConfig();
            sender.sendMessage(ChatColor.GREEN + "Plugin reloaded!");
            getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "&f[LocalChat] ") + ChatColor.GREEN + "Config reloaded!");
            return true;
        }
        return onCommand(sender, cmd, label, args);
    }

}
