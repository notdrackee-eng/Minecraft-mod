
package com.minecrart.modmanager;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

public class HiddenKeybinds {

    private static final Gson GSON = new Gson();

    private static final Path FILE =
        FabricLoader.getInstance()
            .getConfigDir()
            .resolve("minecrart_mod_hidden_keybinds.json");

    private static final Set<String> hidden = load();

    public static boolean isHidden(String id) {
        return hidden.contains(id);
    }

    public static void toggle(String id) {
        if (!hidden.add(id)) {
            hidden.remove(id);
        }

        save();
    }

    private static Set<String> load() {
        Set<String> result = new HashSet<>();

        if (!Files.exists(FILE)) {
            return result;
        }

        try {
            JsonArray array =
                JsonParser.parseString(Files.readString(FILE))
                    .getAsJsonArray();

            array.forEach(element ->
                result.add(element.getAsString())
            );

        } catch (Exception e) {
            System.err.println(
                "Could not load hidden keybinds: " + e.getMessage()
            );
        }

        return result;
    }

    private static void save() {
        try {
            Files.createDirectories(FILE.getParent());
            Files.writeString(FILE, GSON.toJson(hidden));
        } catch (IOException e) {
            System.err.println(
                "Could not save hidden keybinds: " + e.getMessage()
            );
        }
    }
}
