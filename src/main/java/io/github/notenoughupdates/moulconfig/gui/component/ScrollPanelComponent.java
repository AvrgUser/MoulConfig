package io.github.notenoughupdates.moulconfig.gui.component;

import io.github.notenoughupdates.moulconfig.gui.GuiComponent;
import io.github.notenoughupdates.moulconfig.gui.GuiImmediateContext;
import io.github.notenoughupdates.moulconfig.gui.KeyboardEvent;
import io.github.notenoughupdates.moulconfig.gui.MouseEvent;
import lombok.RequiredArgsConstructor;


import java.util.function.BiFunction;

@RequiredArgsConstructor
public class ScrollPanelComponent extends GuiComponent {
    final int width;
    final int height;
    final GuiComponent child;
    int scrollOffset;

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public <T> T foldChildren(T initial, BiFunction<GuiComponent, T, T> visitor) {
        return visitor.apply(child, initial);
    }

    @Override
    public void render(GuiImmediateContext context) {
        context.renderContext().pushScissor(0, 0, context.width(), context.height());
        context.renderContext().pushMatrix();
        context.renderContext().translate(0, -scrollOffset);
        child.render(context.translatedNonRendering(0, -scrollOffset, context.width(), context.height()));
        context.renderContext().popMatrix();
        context.renderContext().popScissor();
    }

    @Override
    public boolean keyboardEvent(KeyboardEvent event, GuiImmediateContext context) {
        return child.keyboardEvent(event, context.translatedNonRendering(0, -scrollOffset, context.width(), context.height()));
    }

    @Override
    public boolean mouseEvent(MouseEvent mouseEvent, GuiImmediateContext context) {
        if (child.mouseEvent(mouseEvent, context.translatedNonRendering(0, -scrollOffset, context.width(), context.height()))) {
            return true;
        }
        if (context.isHovered() && mouseEvent instanceof MouseEvent.Scroll) {
            scrollOffset = (int) Math.max(0, Math.min(scrollOffset - (((MouseEvent.Scroll) mouseEvent).getDWheel() * 15), child.getHeight() - height));
            return true;
        }
        return false;
    }
}
