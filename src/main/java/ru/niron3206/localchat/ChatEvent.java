package ru.niron3206.localchat;

import io.papermc.paper.event.player.AsyncChatEvent;
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
    private static String chatPrefix;

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
                    if (!isInTheSameWorld(sender, player)) {
                        event.viewers().remove(player);
                    } else {
                        if (!inRange(getDistance(sender.getLocation(), player.getLocation()))) {
                            event.viewers().remove(player);
                        }
                    }
                }
                setChatPrefix(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("LocalChatPrefix")));
            } else {
                setChatPrefix(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("GlobalChatPrefix")));
                if(message.length() != 1) {
                    message = message.replaceFirst(plugin.getConfig().getString("Prefix"), "");
                }
            }

            if(!isPlaceHolderAPIEnabled()) {
                String finalMessage = message;
                event.renderer((source, sourceDisplayName, messageComponent, viewer) ->
                        Component.text()
                                .append(Component.text(ChatEvent.getChatPrefix() + " "))
                                .append(event.getPlayer().displayName())
                                .append(Component.text(" > "))
                                .append(Component.text(finalMessage))
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

    public static String getChatPrefix() {
        return chatPrefix;
    }
    public static void setChatPrefix(String chatPrefix) {
        ChatEvent.chatPrefix = chatPrefix;
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
