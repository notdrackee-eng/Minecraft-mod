
package com.minecrart.modmanager.mixin;

import com.minecrart.modmanager.HiddenKeybinds;

import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.gui.screen.option.ControlsListWidget;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.objectweb.asm.Opcodes;

import java.util.Arrays;

@Mixin(ControlsListWidget.class)
public class ControlsListWidgetMixin {

    @Redirect(
        method = "update",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/client/option/GameOptions;allKeys:[Lnet/minecraft/client/option/KeyBinding;",
            opcode = Opcodes.GETFIELD
        )
    )
    private KeyBinding[] minecrart$filterHiddenKeys(GameOptions options) {

        KeyBinding[] allKeys =
            ((GameOptionsAccessor) (Object) options)
                .minecrart$getAllKeys();

        return Arrays.stream(allKeys)
            .filter(key -> !HiddenKeybinds.isHidden(key.getId()))
            .toArray(KeyBinding[]::new);
    }
}
