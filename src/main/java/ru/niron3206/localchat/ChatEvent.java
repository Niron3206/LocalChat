package ru.niron3206.localchat;

import io.papermc.paper.event.player.AsyncChatEvent;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import static org.bukkit.Bukkit.getOnlinePlayers;

public class ChatEvent implements Listener {

    private final LocalChat plugin;
    private static String prefix;

    public ChatEvent(LocalChat plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onChatEvent(AsyncChatEvent event) {
        Player sender = event.getPlayer();
        String message = PlainTextComponentSerializer.plainText().serialize(event.message());

        if(isEnabled()) {
            if(!hasPrefix(message)) {
                for (Player player : getOnlinePlayers()) {
                    if (isInTheSameWorld(sender, player) && inRange(getDistance(sender.getLocation(), player.getLocation()))) {
                        continue;
                    } else {
                        event.viewers().remove(player);
                    }
                }
                setPrefix(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("LocalChatPrefix")));
            } else {
                setPrefix(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("GlobalChatPrefix")));
            }

            if(!isPlaceHolderAPIEnabled()) {
                event.renderer((source, sourceDisplayName, messageComponent, viewer) ->
                        Component.text()
                                .append(Component.text(ChatEvent.getPrefix() + " "))
                                .append(event.getPlayer().displayName())
                                .append(Component.text(" > "))
                                .append(messageComponent)
                                .build());

            }
        }
    }

    private static double getDistance(Location sender, Location player) {
        return sender.distance(player);
    }

    private boolean inRange(double distance) {
        return distance < plugin.getConfig().getInt("LocalDistance");
    }

    public static String getPrefix() {
        return prefix;
    }
    public static void setPrefix(String prefix) {
        ChatEvent.prefix = prefix;
    }

    private boolean isEnabled() {
        return plugin.getConfig().getBoolean("Enabled");
    }
    private boolean isPlaceHolderAPIEnabled() {
        return plugin.getConfig().getBoolean("PlaceHolderAPIEnabled");
    }
    private boolean hasPrefix(String message) {
        return message.startsWith(plugin.getConfig().getString("Prefix"));
    }
    private boolean isInTheSameWorld(Player sender, Player player) {
        return sender.getWorld() == player.getWorld();
    }
}
