package io.github.notenoughupdates.moulconfig.common;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.NonNull;

public class KeyBindHelper {
    private KeyBindHelper() {
    }

    public static InputConstants.Key keyboard(int key) {
        //~ if >= 26.3 'KEYSYM' -> 'KEYBOARD'
        return InputConstants.Type.KEYBOARD.getOrCreate(key);
    }

    public static InputConstants.Key mouse(int button) {
        return InputConstants.Type.MOUSE.getOrCreate(button);
    }

    public static @NonNull String getKeyName(InputConstants.Key key) {
        if (key == InputConstants.UNKNOWN) {
            return "NONE";
        }

        Component componentName = key.getDisplayName();
        String collapsed = componentName.tryCollapseToString();
        if (collapsed != null) {
            return collapsed;
        }
        return componentName.getString();
    }

    public static String getKeyIdentifier(InputConstants.Key key) {
        if (key == null) return null;
        String rawName = key.getName();
        //? if >= 26.3 {
        return rawName;
         //? } else {
        /*return switch (rawName) {
            case "key.keyboard.keypad.decimal" -> "key.keyboard.keypad.period";
            case "key.keyboard.menu" -> "key.keyboard.application";
            default -> rawName;
        };
        *///?}
    }

    public static InputConstants.Key getKeyByIdentifier(String rawName) {
        if (rawName == null) return null;
        //? if >= 26.3 {
        String name = rawName;
        //? } else {
        /*String name = switch (rawName) {
            case "key.keyboard.keypad.period" -> "key.keyboard.keypad.decimal";
            case "key.keyboard.application" -> "key.keyboard.menu";
            default -> rawName;
        };
        *///? }
        return InputConstants.getKey(name);
    }
}
