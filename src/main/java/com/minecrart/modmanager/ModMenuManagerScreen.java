
package com.minecrart.modmanager;

import com.terraformersmc.modmenu.config.ModMenuConfig;
import com.terraformersmc.modmenu.config.ModMenuConfigManager;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ModMenuManagerScreen extends Screen {

    private final Screen parent;
    private final int page;

    private static final int PAGE_SIZE = 8;

    public ModMenuManagerScreen(Screen parent) {
        this(parent, 0);
    }

    public ModMenuManagerScreen(Screen parent, int page) {
        super(Text.literal("Mod Menu Manager"));
        this.parent = parent;
        this.page = page;
    }

    @Override
    protected void init() {
        List<ModContainer> mods =
            new ArrayList<>(FabricLoader.getInstance().getAllMods());

        mods.removeIf(mod ->
            mod.getMetadata().getId().equals("modmenu") ||
            mod.getMetadata().getId().equals("minecrart_mod")
        );

        mods.sort(Comparator.comparing(
            mod -> mod.getMetadata().getName(),
            String.CASE_INSENSITIVE_ORDER
        ));

        int totalPages = Math.max(1,
            (mods.size() + PAGE_SIZE - 1) / PAGE_SIZE);

        int currentPage = Math.min(page, totalPages - 1);
        int start = currentPage * PAGE_SIZE;
        int end = Math.min(start + PAGE_SIZE, mods.size());

        for (int i = start; i < end; i++) {
            ModContainer mod = mods.get(i);

            String id = mod.getMetadata().getId();
            String name = mod.getMetadata().getName();

            boolean hidden =
                ModMenuConfig.HIDDEN_MODS.getValue().contains(id);

            String label = (hidden ? "[HIDDEN] " : "[VISIBLE] ")
                + name + " (" + id + ")";

            addDrawableChild(
                ButtonWidget.builder(
                    Text.literal(label),
                    button -> toggleMod(id, currentPage)
                )
                .dimensions(width / 2 - 160,
                    45 + (i - start) * 25, 320, 22)
                .build()
            );
        }

        ButtonWidget previous = ButtonWidget.builder(
            Text.literal("Previous"),
            button -> MinecraftClient.getInstance().setScreen(
                new ModMenuManagerScreen(parent,
                    Math.max(0, currentPage - 1))
            )
        ).dimensions(width / 2 - 160, height - 30, 90, 20).build();

        previous.active = currentPage > 0;
        addDrawableChild(previous);

        ButtonWidget next = ButtonWidget.builder(
            Text.literal("Next"),
            button -> MinecraftClient.getInstance().setScreen(
                new ModMenuManagerScreen(parent,
                    Math.min(totalPages - 1, currentPage + 1))
            )
        ).dimensions(width / 2 + 70, height - 30, 90, 20).build();

        next.active = currentPage < totalPages - 1;
        addDrawableChild(next);

        addDrawableChild(
            ButtonWidget.builder(
                Text.literal("Keybinds"),
                button -> MinecraftClient.getInstance().setScreen(
                    new KeybindManagerScreen(this)
                )
            )
            .dimensions(width / 2 - 160, height - 55, 90, 20)
            .build()
        );

        addDrawableChild(
            ButtonWidget.builder(
                Text.literal("Done"),
                button -> MinecraftClient.getInstance().setScreen(parent)
            )
            .dimensions(width / 2 - 45, height - 55, 90, 20)
            .build()
        );
    }

    private void toggleMod(String id, int currentPage) {
        Set<String> hidden = new HashSet<>(
            ModMenuConfig.HIDDEN_MODS.getValue()
        );

        if (!hidden.add(id)) {
            hidden.remove(id);
        }

        ModMenuConfig.HIDDEN_MODS.setValue(hidden);
        ModMenuConfigManager.save();

        MinecraftClient.getInstance().setScreen(
            new ModMenuManagerScreen(parent, currentPage)
        );
    }

    @Override
    public void render(
        DrawContext context,
        int mouseX,
        int mouseY,
        float delta
    ) {
        renderBackground(context, mouseX, mouseY, delta);
        super.render(context, mouseX, mouseY, delta);

        context.drawCenteredTextWithShadow(
            textRenderer,
            Text.literal("MOD MENU MANAGER"),
            width / 2,
            15,
            0xFFFFFF
        );

        context.drawCenteredTextWithShadow(
            textRenderer,
            Text.literal("Click a mod to toggle its visibility"),
            width / 2,
            29,
            0xAAAAAA
        );
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
