
package com.minecrart.modmanager.mixin;

import com.minecrart.modmanager.HiddenKeybinds;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.option.ControlsListWidget;
import net.minecraft.client.gui.screen.option.KeybindsScreen;
import net.minecraft.client.option.KeyBinding;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Iterator;
import java.util.List;

@Mixin(ControlsListWidget.class)
public class ControlsListWidgetMixin {

    @Inject(method = "<init>", at = @At("TAIL"))
    private void minecrart$filterHiddenKeybinds(
            KeybindsScreen parent,
            MinecraftClient client,
            CallbackInfo ci
    ) {
        List<?> entries =
                ((EntryListWidgetAccessor) (Object) this)
                        .minecrart$getChildren();

        // Remove hidden keybinds
        Iterator<?> iterator = entries.iterator();

        while (iterator.hasNext()) {
            Object entry = iterator.next();

            if (entry instanceof ControlsListWidget.KeyBindingEntry keyEntry) {
                KeyBinding binding =
                        ((KeyBindingEntryAccessor) (Object) keyEntry)
                                .minecrart$getBinding();

                if (HiddenKeybinds.isHidden(binding.getId())) {
                    iterator.remove();
                }
            }
        }

        // Remove category headings with no visible keybinds
        int i = 0;

        while (i < entries.size()) {
            Object entry = entries.get(i);

            if (entry instanceof ControlsListWidget.CategoryEntry) {
                boolean hasVisibleKeybind = false;

                for (int j = i + 1; j < entries.size(); j++) {
                    Object next = entries.get(j);

                    if (next instanceof ControlsListWidget.CategoryEntry) {
                        break;
                    }

                    if (next instanceof ControlsListWidget.KeyBindingEntry) {
                        hasVisibleKeybind = true;
                        break;
                    }
                }

                if (!hasVisibleKeybind) {
                    entries.remove(i);
                    continue;
                }
            }

            i++;
        }
    }
}
