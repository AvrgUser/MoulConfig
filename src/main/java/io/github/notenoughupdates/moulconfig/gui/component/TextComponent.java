/*
 * Copyright (C) 2023 NotEnoughUpdates contributors
 *
 * This file is part of MoulConfig.
 *
 * MoulConfig is free software: you can redistribute it
 * and/or modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version.
 *
 * MoulConfig is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with MoulConfig. If not, see <https://www.gnu.org/licenses/>.
 *
 */

package io.github.notenoughupdates.moulconfig.gui.component;

import io.github.notenoughupdates.moulconfig.common.IMinecraft;
import io.github.notenoughupdates.moulconfig.gui.GuiComponent;
import io.github.notenoughupdates.moulconfig.gui.GuiImmediateContext;
import io.github.notenoughupdates.moulconfig.internal.ComponentHelper;
import lombok.RequiredArgsConstructor;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.regex.Pattern;

/**
 * A gui element which renders a string in a single line
 */
@RequiredArgsConstructor
public class TextComponent extends GuiComponent {
    final Font fontRenderer;
    final Supplier<Component> string;
    final int suggestedWidth;
    final TextAlignment alignment;
    final boolean shadow;
    final boolean split;
    private Component lastString;
    private int lastWidth = -1;
    private List<Component> lastSplit;
    private static final Pattern colorPattern = Pattern.compile("§[a-f0-9r]");

    public TextComponent(Component string, int width, TextAlignment alignment) {
        this(IMinecraft.INSTANCE.getDefaultFontRenderer(), () -> string, width, alignment, false, false);
    }

    public TextComponent(Component string, int width) {
        this(IMinecraft.INSTANCE.getDefaultFontRenderer(), () -> string, width, TextAlignment.LEFT, false, false);
    }

    public TextComponent(Component string) {
        this(string, IMinecraft.INSTANCE.getDefaultFontRenderer().width(string));
    }

    public TextComponent(String string) {
        this(Component.literal(string));
    }

    @Override
    public int getWidth() {
        return suggestedWidth + 4;
    }

    @Override
    public int getHeight() {
        return 2 + (fontRenderer.lineHeight + 2) * split(string.get(), getWidth()).size();
    }

    public List<Component> split(Component text, int width) {
        if (!split) return Collections.singletonList(text);
        if (Objects.equals(text, lastString) && width == lastWidth)
            return lastSplit;
        lastString = text;
        lastWidth = width;
        lastSplit = ComponentHelper.splitText(text, width);
        return lastSplit;
    }

    @Override
    public void render(GuiImmediateContext context) {
        context.renderContext().pushMatrix();
        List<Component> lines = split(string.get(), context.width());
        for (Component line : lines) {
            int length = fontRenderer.width(line);
            if (length > context.width()) {
                context.renderContext().drawStringScaledMaxWidth(line, fontRenderer, 2, 2, shadow, context.width(), -1);
            } else switch (alignment) {
                case LEFT:
                    context.renderContext().drawString(fontRenderer, line, 2, 2, -1, shadow);
                    break;
                case CENTER:
                    context.renderContext().drawString(fontRenderer, line, context.width() / 2 - length / 2 + 2, 2, -1, shadow);
                    break;
                case RIGHT:
                    context.renderContext().drawString(fontRenderer, line, context.width() - length + 2, 2, -1, shadow);
                    break;
            }
            context.renderContext().translate(0, fontRenderer.lineHeight + 2);
        }
        context.renderContext().popMatrix();
    }

    public enum TextAlignment {
        LEFT, CENTER, RIGHT
    }
}
