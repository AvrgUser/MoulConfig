package io.github.notenoughupdates.moulconfig.platform;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.network.chat.Component;
#if MC > 12107
import net.minecraft.client.input.KeyEvent;
#endif

public class ModernKeybindHelper {
    public static Component getKeyName(int keyCode) { // TODO: translations
        if (keyCode == -1) {
            return Component.literal("NONE");
        } else if (keyCode >= 0 && keyCode <= 9) {
            return Component.literal("Button " + (keyCode + 1));
        } else {
            #if MC < 12109
            Component keyName = MoulConfigText.wrap(InputConstants.getKey(keyCode, 0).getDisplayName());
            #else
            Component keyName = MoulConfigText.wrap(InputConstants.getKey(new KeyEvent(keyCode, 0, 0)).getDisplayName());
            #endif
            if (keyName == null) {
                keyName = Component.literal("???");
            }
            return keyName;
        }
    }
}
