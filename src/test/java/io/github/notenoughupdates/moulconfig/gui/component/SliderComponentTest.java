package io.github.notenoughupdates.moulconfig.gui.component;

import io.github.notenoughupdates.moulconfig.common.Layer;
import io.github.notenoughupdates.moulconfig.common.RenderContext;
import io.github.notenoughupdates.moulconfig.common.TextureFilter;
import io.github.notenoughupdates.moulconfig.gui.GuiImmediateContext;
import io.github.notenoughupdates.moulconfig.observer.GetSetter;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.function.Consumer;

public class SliderComponentTest {
    @Test
    void renderAcceptsIntegerValue() {
        renderSliderWithValue(3);
    }

    @Test
    void renderAcceptsFloatValue() {
        renderSliderWithValue(3F);
    }

    @Test
    void renderAcceptsDoubleValue() {
        renderSliderWithValue(3D);
    }

    private void renderSliderWithValue(Number value) {
        SliderComponent slider = new SliderComponent(GetSetter.floating(value), 0F, 10F, 1F, 80);
        GuiImmediateContext context = new GuiImmediateContext(new NoOpRenderContext(), 0, 0, 80, 16, 0, 0, 0, 0, 0F, 0F);

        Assertions.assertDoesNotThrow(() -> slider.render(context));
    }

    private static class NoOpRenderContext implements RenderContext {
        @Override
        public void pushMatrix() {
        }

        @Override
        public void popMatrix() {
        }

        @Override
        public void translate(float x, float y) {
        }

        @Override
        public void scale(float x, float y) {
        }

        @Override
        public void drawOnTop(@NonNull Layer layer, @NonNull ScissorBehaviour escapeScissors, @NonNull Consumer<RenderContext> later) {
        }

        @Override
        public void drawColouredQuads(int colour, float... coordinates) {
        }

        @Override
        public void drawString(Font fontRenderer, Component text, int x, int y, int color, boolean shadow) {
        }

        @Override
        public void drawColoredRect(float left, float top, float right, float bottom, int color) {
        }

        @Override
        public void invertedRect(float left, float top, float right, float bottom, int additiveColor) {
        }

        @Override
        public void drawTexturedTintedRect(
            Identifier texture,
            float x,
            float y,
            float width,
            float height,
            float u1,
            float v1,
            float u2,
            float v2,
            int color,
            TextureFilter filter
        ) {
        }

        @Override
        public void drawDarkRect(int x, int y, int width, int height, boolean shadow) {
        }

        @Override
        public void drawGradientRect(int left, int top, int right, int bottom, int startColor, int endColor) {
        }

        @Override
        public void pushScissor(int left, int top, int right, int bottom) {
        }

        @Override
        public void pushRawScissor(int left, int top, int right, int bottom) {
        }

        @Override
        public void popScissor() {
        }

        @Override
        public void assertNoScissors() {
        }

        @Override
        public void clearScissor() {
        }

        @Override
        public void drawTooltipNow(int x, int y, List<Component> tooltipLines) {
        }

        @Override
        public void renderExtraLayers() {
        }
    }
}
