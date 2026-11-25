package com.minecrart.modmanager.mixin;

import com.minecrart.modmanager.HiddenKeybinds;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.option.ControlsListWidget;
import net.minecraft.client.gui.screen.option.KeybindsScreen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.gui.widget.EntryListWidget;

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
    }
}
