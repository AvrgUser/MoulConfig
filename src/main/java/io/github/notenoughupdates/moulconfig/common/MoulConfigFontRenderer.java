package io.github.notenoughupdates.moulconfig.common;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MoulConfigFontRenderer {
    @NotNull
    public static List<@NotNull Component> splitText(@NotNull Component text, int width) {
        var font = IMinecraft.INSTANCE.getDefaultFontRenderer();
        var list = new ArrayList<Component>();
        font.getSplitter().splitLines(text, width, Style.EMPTY, (stringVisitable, isWrapped) -> {
            var appendable = Component.empty();
            list.add(appendable);
            stringVisitable.visit((style, string) -> {
                appendable.append(Component.literal(string).setStyle(style));
                return Optional.empty();
            }, Style.EMPTY);
        });
        return list;
    }

    @NotNull
    public String trimStringToWidth(@NotNull String string, int width, boolean reverse) {
        var font = IMinecraft.INSTANCE.getDefaultFontRenderer();
        return font.plainSubstrByWidth(string, width, reverse);
    }
}
