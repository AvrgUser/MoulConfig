package io.github.notenoughupdates.moulconfig.internal;

import io.github.notenoughupdates.moulconfig.common.IMinecraft;
import net.minecraft.network.chat.Component;


public class ComponentHelper {
    public static Component mapStringOrComponent(Object object) {
        if (object instanceof String) {
            return Component.literal((String) object);
        }
        if (object instanceof Component) {
            return (Component) object;
        }
        var structured = IMinecraft.INSTANCE.createComponentInternal(object);
        if (structured != null) {
            return structured;
        }
        throw new IllegalArgumentException("Expected string or structured text, found " + object);
    }
}
