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

package io.github.notenoughupdates.moulconfig.gui.editors;

import io.github.notenoughupdates.moulconfig.common.IMinecraft;
import io.github.notenoughupdates.moulconfig.gui.GuiComponent;
import io.github.notenoughupdates.moulconfig.gui.GuiImmediateContext;
import io.github.notenoughupdates.moulconfig.gui.MouseEvent;
import io.github.notenoughupdates.moulconfig.processor.ProcessedOption;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class GuiOptionEditorDropdown extends ComponentEditor {
    private final List<Component> values;
    private final boolean useOrdinal;
    private Enum<?>[] constants;
    private String valuesForSearch;

    public GuiOptionEditorDropdown(ProcessedOption option, String[] values) {
        this(option, values, false);
    }

    // TODO: rework this entire thing to accept Components and/or classes implementing a custom interfaces and/or a custom text mapper
    public GuiOptionEditorDropdown(
        ProcessedOption option,
        String[] values,
        boolean forceGivenValues
    ) {
        super(option);
        Class<?> clazz = (Class<?>) option.getType();
        if (Enum.class.isAssignableFrom(clazz) && !forceGivenValues) {
            constants = (Enum<?>[]) (clazz).getEnumConstants();
            this.values = new ArrayList<>();
            for (Enum<?> constant : constants) {
                this.values.add(Component.literal(constant.toString()));
            }
        } else {
            this.values = Arrays.stream(values).map(Component::literal).collect(Collectors.toList());
            assert values.length > 0;
        }
        this.useOrdinal = clazz == int.class || clazz == Integer.class;
    }

    int componentWidth = 0;
    private final GuiComponent dropdownOverlay = new GuiComponent() {
        @Override
        public int getWidth() {
            return componentWidth;
        }

        @Override
        public int getHeight() {
            return 13 + 12 * values.size();
        }

        @Override
        public boolean mouseEvent(@NotNull MouseEvent mouseEvent, @NotNull GuiImmediateContext context) {
            if (mouseEvent instanceof MouseEvent.Click(int mouseButton, boolean mouseState)) {
                if (mouseState) {
                    closeOverlay();
                }
                if (mouseState && mouseButton == 0 && context.isHovered()) {
                    int top = 0;
                    int mouseY = context.mouseY();
                    int dropdownY = 13;
                    for (int ordinal = 0; ordinal < values.size(); ordinal++) {
                        if (mouseY >= top + 3 + dropdownY && mouseY <= top + 3 + dropdownY + 12) {
                            if (constants != null) {
                                option.set(constants[ordinal]);
                            } else if (useOrdinal) {
                                option.set(ordinal);
                            } else {
                                option.set(values.get(ordinal).getString());
                            }
                        }
                        dropdownY += 12;
                    }
                }
                return true;
            }
            return super.mouseEvent(mouseEvent, context);
        }

        @Override
        public void render(@NotNull GuiImmediateContext context) {
            int selected = getSelectedIndex();
            Component selectedString = Component.literal(" - Select - ");
            if (selected >= 0 && selected < values.size()) {
                selectedString = values.get(selected);
            }

            int dropdownHeight = context.height();
            int dropdownWidth = context.width();

            int main = 0xff202026;
            int outlineColour = 0xffffffff;

            context.renderContext().pushMatrix();
            // TODO: do we even need that? (given the render order) context.getRenderContext().translate(0, 0, 100);
            int left = 0;
            int top = 0;
            context.renderContext().drawColoredRect(left, top, left + 1, top + dropdownHeight, outlineColour); //Left
            context.renderContext().drawColoredRect(left + 1, top, left + dropdownWidth, top + 1, outlineColour); //Top
            context.renderContext().drawColoredRect(left + dropdownWidth - 1, top + 1, left + dropdownWidth, top + dropdownHeight, outlineColour); //Right
            context.renderContext().drawColoredRect(left + 1, top + dropdownHeight - 1, left + dropdownWidth - 1, top + dropdownHeight, outlineColour); //Bottom
            context.renderContext().drawColoredRect(left + 1, top + 1, left + dropdownWidth - 1, top + dropdownHeight - 1, main); //Middle

            context.renderContext().drawColoredRect(left + 1, top + 14 - 1, left + dropdownWidth - 1, top + 14, outlineColour); //Bar
            int dropdownY = 13;
            Font fr = IMinecraft.INSTANCE.getDefaultFontRenderer();
            for (Component option : values) {
                if (option.getString().isEmpty()) {
                    option = Component.literal("<NONE>");
                }
                context.renderContext().drawStringScaledMaxWidth(
                    option,
                    fr,
                    left + 3,
                    top + 3 + dropdownY,
                    false,
                    dropdownWidth - 6,
                    0xffa0a0a0
                );
                dropdownY += 12;
            }
            context.renderContext().drawStringScaledMaxWidth(
                selectedString, fr, left + 3, top + 3, false,
                dropdownWidth - 16, 0xffa0a0a0
            );
            context.renderContext().drawOpenCloseTriangle(
                false, context.width() - 10, 4, 6, 6, -1
            );
            context.renderContext().popMatrix();
        }
    };
    private final GuiComponent component = wrapComponent(new GuiComponent() {
        @Override
        public int getWidth() {
            return 80;
        }

        @Override
        public int getHeight() {
            return 14;
        }

        @Override
        public boolean mouseEvent(@NotNull MouseEvent mouseEvent, @NotNull GuiImmediateContext context) {
            if (mouseEvent instanceof MouseEvent.Click && ((MouseEvent.Click) mouseEvent).mouseState() && context.isHovered()) {
                if (!isOverlayOpen()) {
                    componentWidth = context.width();
                    //Clamp the Y so that the dropdown can't go off the screen
                    int scaledHeight = context.renderContext().getMinecraft().getScaledHeight();
                    int clampedY;

                    if (context.renderOffsetY() + dropdownOverlay.getHeight() > scaledHeight) {
                        clampedY = scaledHeight - dropdownOverlay.getHeight();
                    } else {
                        clampedY = context.renderOffsetY();
                    }

                    openOverlay(dropdownOverlay, context.renderOffsetX(), clampedY);
                }
                return true;
            }
            return super.mouseEvent(mouseEvent, context);
        }

        @Override
        public void render(@NotNull GuiImmediateContext context) {
            int dropdownWidth = context.width();
            int selected = getSelectedIndex();
            if (selected >= values.size()) selected = values.size();
            Component selectedString = Component.literal(" - Select - ");
            if (selected >= 0 && selected < values.size()) {
                selectedString = values.get(selected);
            }

            context.renderContext().drawDarkRect(
                0, 0, dropdownWidth, context.height(), false
            );
            context.renderContext().drawOpenCloseTriangle(
                true, context.width() - 10, 4, 6, 6, -1
            );
            context.renderContext().drawStringScaledMaxWidth(
                selectedString, IMinecraft.INSTANCE.getDefaultFontRenderer(),
                3, 3, false, context.width() - 16, 0xffa0a0a0
            );
        }
    });

    @Override
    public @NotNull GuiComponent getDelegate() {
        return component;
    }

    private int getSelectedIndex() {
        Object selectedObject = option.get();
        if (selectedObject == null) return -1;
        if (useOrdinal) {
            return (int) selectedObject;
        } else if (constants != null) {
            return ((Enum<?>) selectedObject).ordinal();
        } else {
            return (values).stream().map(Component::getString).toList().indexOf(selectedObject);
        }
    }

    @Override
    public boolean fulfillsSearch(String word) {
        if (valuesForSearch == null) {
            valuesForSearch = values.stream().map(Component::getString).collect(Collectors.joining(" ")).toLowerCase(Locale.ROOT);
        }
        return super.fulfillsSearch(word) || valuesForSearch.contains(word);
    }

}
