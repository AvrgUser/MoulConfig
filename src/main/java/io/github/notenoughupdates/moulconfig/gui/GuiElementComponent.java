package io.github.notenoughupdates.moulconfig.gui;

import io.github.notenoughupdates.moulconfig.internal.Warnings;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.jspecify.annotations.NonNull;

/**
 * Adapts a {@link GuiElement} as a {@link GuiComponent} that renders on the entire screen. Not applicable to be used in transformed situations.
 */
@EqualsAndHashCode(callSuper = true)
@Value
public class GuiElementComponent extends GuiComponent {
    GuiElement element;

    @Override
    public void setContext(GuiContext context) {
        super.setContext(context);
        if (context.getRoot() != this)
            Warnings.warn("Mounting GuiElementComponent at location other than root. This can cause issues.");
    }

    @Override
    public int getWidth() {
        return mc.getScaledWidth();
    }

    @Override
    public int getHeight() {
        return mc.getScaledHeight();
    }

    @Override
    public void render(@NonNull GuiImmediateContext context) {
        if (context.renderOffsetX() != 0 || context.renderOffsetY() != 0) {
            Warnings.warn("Cannot render GuiElement with a pretransformed matrix stack");
        }
        element.render();
    }

    @Override
    public boolean mouseEvent(@NonNull MouseEvent mouseEvent, @NonNull GuiImmediateContext context) {
        return element.mouseInput(context.mouseX(), context.mouseY(), mouseEvent);
    }

    @Override
    public boolean keyboardEvent(@NonNull KeyboardEvent event, @NonNull GuiImmediateContext context) {
        return element.keyboardInput(event);
    }
}
