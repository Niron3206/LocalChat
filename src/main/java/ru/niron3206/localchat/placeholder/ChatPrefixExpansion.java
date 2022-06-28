package ru.niron3206.localchat.placeholder;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import ru.niron3206.localchat.ChatEvent;

public class ChatPrefixExpansion extends PlaceholderExpansion {

    @Override
    public String getIdentifier() {
        return "localchat";
    }

    @Override
    public String getAuthor() {
        return "Niron3206";
    }

    @Override
    public String getVersion() {
        return "1.0";
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onPlaceholderRequest(Player player, String params) {
        if (player == null) {
            return "";
        }
        if (params.equals("chatprefix")) {
            return ChatEvent.getChatPrefix();
        }
        return null;
    }
}
