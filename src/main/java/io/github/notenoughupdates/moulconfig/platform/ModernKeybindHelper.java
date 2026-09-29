package io.github.notenoughupdates.moulconfig.platform;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.network.chat.Component;
import net.minecraft.client.input.KeyEvent;

public class ModernKeybindHelper {
    public static Component getKeyName(int keyCode) { // TODO: translations
        if (keyCode == -1) {
            return Component.literal("NONE");
        } else if (keyCode >= 0 && keyCode <= 9) {
            return Component.literal("Button " + (keyCode + 1));
        } else {
            Component keyName = MoulConfigText.wrap(InputConstants.getKey(new KeyEvent(keyCode, 0, 0)).getDisplayName());
            if (keyName == null) {
                keyName = Component.literal("???");
            }
            return keyName;
        }
    }
}
