package io.github.notenoughupdates.moulconfig.common;

import net.minecraft.network.chat.Component;
import org.jspecify.annotations.NullMarked;

import java.util.List;

@NullMarked
public interface IFontRenderer {
    int getHeight();

    default int getStringWidth(String string) {
        return getStringWidth(Component.literal(string));
    }

    int getStringWidth(Component Component);

    default int getCharWidth(char c) {
        return getStringWidth(Character.toString(c));
    }

    List<Component> splitText(Component Component, int width);

    default List<Component> splitLines(Component Component) {
        return splitText(Component, Integer.MAX_VALUE);
    }

    default String trimStringToWidth(String string, int width) {
        return trimStringToWidth(string, width, false);
    }

    String trimStringToWidth(String string, int width, boolean reversed);
}
