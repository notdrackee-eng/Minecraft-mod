
package com.minecrart.modmanager;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.text.Text;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class KeybindManagerScreen extends Screen {

    private final Screen parent;
    private int page = 0;

    private static final int PAGE_SIZE = 8;

    public KeybindManagerScreen(Screen parent) {
        super(Text.literal("Keybind Manager"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        List<KeyBinding> keys = Arrays.asList(
            MinecraftClient.getInstance().options.allKeys
        );

        keys.sort(Comparator.comparing(KeyBinding::getId));

        int totalPages = Math.max(
            1, (keys.size() + PAGE_SIZE - 1) / PAGE_SIZE
        );

        page = Math.min(page, totalPages - 1);

        int start = page * PAGE_SIZE;
        int end = Math.min(start + PAGE_SIZE, keys.size());

        for (int i = start; i < end; i++) {
            KeyBinding key = keys.get(i);
            String id = key.getId();

            boolean hidden = HiddenKeybinds.isHidden(id);

            String label = (hidden ? "[HIDDEN] " : "[VISIBLE] ")
                + id;

            addDrawableChild(
                ButtonWidget.builder(
                    Text.literal(label),
                    button -> {
                        HiddenKeybinds.toggle(id);
                        MinecraftClient.getInstance().setScreen(
                            new KeybindManagerScreen(parent, page)
                        );
                    }
                )
                .dimensions(
                    width / 2 - 160,
                    45 + (i - start) * 25,
                    320,
                    22
                )
                .build()
            );
        }

        ButtonWidget previous = ButtonWidget.builder(
            Text.literal("Previous"),
            button -> {
                page--;
                refresh();
            }
        ).dimensions(width / 2 - 160, height - 30, 90, 20).build();

        previous.active = page > 0;
        addDrawableChild(previous);

        ButtonWidget next = ButtonWidget.builder(
            Text.literal("Next"),
            button -> {
                page++;
                refresh();
            }
        ).dimensions(width / 2 + 70, height - 30, 90, 20).build();

        next.active = page < totalPages - 1;
        addDrawableChild(next);

        addDrawableChild(
            ButtonWidget.builder(
                Text.literal("Back"),
                button -> MinecraftClient.getInstance().setScreen(parent)
            )
            .dimensions(width / 2 - 45, height - 55, 90, 20)
            .build()
        );
    }

    private void refresh() {
        MinecraftClient.getInstance().setScreen(
            new KeybindManagerScreen(parent, page)
        );
    }

    private KeybindManagerScreen(Screen parent, int page) {
        this(parent);
        this.page = page;
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
            Text.literal("KEYBIND MANAGER"),
            width / 2,
            15,
            0xFFFFFF
        );

        context.drawCenteredTextWithShadow(
            textRenderer,
            Text.literal("Select keybinds to hide from Controls"),
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
