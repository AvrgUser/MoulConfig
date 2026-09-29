package io.github.notenoughupdates.moulconfig.gui.editors;

import com.mojang.blaze3d.platform.InputConstants;
import io.github.notenoughupdates.moulconfig.GuiTextures;
import io.github.notenoughupdates.moulconfig.annotations.ConfigEditorKeybind;
import io.github.notenoughupdates.moulconfig.common.IMinecraft;
import io.github.notenoughupdates.moulconfig.common.KeyBindHelper;
import io.github.notenoughupdates.moulconfig.common.RenderContext;
import io.github.notenoughupdates.moulconfig.gui.GuiComponent;
import io.github.notenoughupdates.moulconfig.gui.GuiImmediateContext;
import io.github.notenoughupdates.moulconfig.gui.KeyboardEvent;
import io.github.notenoughupdates.moulconfig.gui.MouseEvent;
import io.github.notenoughupdates.moulconfig.internal.Warnings;
import io.github.notenoughupdates.moulconfig.processor.ProcessedOption;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;

public class GuiOptionEditorKeybind extends ComponentEditor {
    private boolean editingKeycode = false;
    GuiComponent component;

    public GuiOptionEditorKeybind(ProcessedOption option, InputConstants.Key defaultKey) {
        super(option);
        if (option.getType() != InputConstants.Key.class)
            Warnings.warn(ConfigEditorKeybind.class + " can only be applied to InputConstants.Key properties.");

        component = wrapComponent(new GuiComponent() {
            @Override
            public int getWidth() {
                return 0;
            }

            @Override
            public int getHeight() {
                return 30;
            }

            @Override
            public void render(@NotNull GuiImmediateContext context) {
                int height = getHeight();
                RenderContext renderContext = context.renderContext();
                int width = getWidth();

                renderContext.drawTexturedRect(GuiTextures.BUTTON, width / 6 - 24, height - 7 - 14, 48, 16);

                InputConstants.Key key = (InputConstants.Key) option.get();
                String keyName = KeyBindHelper.getKeyName(key);
                Component text = editingKeycode ?
                    Component.literal("> ").append(keyName).append(" <")
                    : Component.literal(keyName);
                renderContext.drawStringCenteredScaledMaxWidth(text,
                    IMinecraft.INSTANCE.getDefaultFontRenderer(),
                    width / 6, height - 7 - 6,
                    false, 38, 0xFF303030
                );

                int resetX = width / 6 - 24 + 48 + 3;
                int resetY = height - 7 - 14 + 3;

                renderContext.drawTexturedRect(GuiTextures.RESET, resetX, resetY, 10, 11);
                int mouseX = context.mouseX();
                int mouseY = context.mouseY();
                if (mouseX >= resetX && mouseX < resetX + 10 &&
                    mouseY >= resetY && mouseY < resetY + 11) {
                    renderContext.scheduleDrawTooltip(
                        context.mouseX(), context.mouseY(),
                        Collections.singletonList(Component.literal("Reset to Default")
                            .withColor(TextColor.RED)));
                }
            }

            @Override
            public boolean mouseEvent(@NotNull MouseEvent mouseEvent, @NotNull GuiImmediateContext context) {
                if (!(mouseEvent instanceof MouseEvent.Click(int mouseButton, boolean mouseState))) return false;
                if (mouseState && mouseButton != InputConstants.UNKNOWN.getValue() && editingKeycode) {
                    editingKeycode = false;
                    InputConstants.Key key = KeyBindHelper.mouse(mouseButton);
                    option.set(key);
                    return true;
                }

                if (mouseState && mouseButton == InputConstants.MOUSE_BUTTON_LEFT) {
                    int height = getHeight();
                    int width = getHeight();
                    int mouseX = context.mouseX();
                    int mouseY = context.mouseY();
                    if (mouseX > width / 6 - 24 && mouseX < width / 6 + 16 &&
                        mouseY > height - 7 - 14 && mouseY < height - 7 + 2) {
                        editingKeycode = true;
                        return true;
                    }
                    if (mouseX > width / 6 - 24 + 48 - 3 && mouseX < width / 6 - 24 + 48 + 13 - 5 &&
                        mouseY > height - 7 - 14 + 3 && mouseY < height - 7 - 14 + 3 + 11) {
                        option.set(defaultKey);
                        return true;
                    }
                }

                return false;
            }

            @Override
            public boolean keyboardEvent(@NotNull KeyboardEvent keyboardEvent, @NotNull GuiImmediateContext context) {
                if (keyboardEvent instanceof KeyboardEvent.KeyPressed keyPressed) {
                    if (editingKeycode) {
                        if (keyPressed.getPressed()) return true;
                        editingKeycode = false;
                        int keycode = keyPressed.getKeycode();
                        if (keycode == InputConstants.KEY_ESCAPE || keycode == 0) {
                            keycode = InputConstants.UNKNOWN.getValue();
                        }
                        InputConstants.Key key = KeyBindHelper.keyboard(keycode);
                        option.set(key);
                        return true;
                    } else {
                        return false;
                    }
                }

                return editingKeycode;
            }
        });
    }

    @Override
    public @NotNull GuiComponent getDelegate() {
        return component;
    }
}
