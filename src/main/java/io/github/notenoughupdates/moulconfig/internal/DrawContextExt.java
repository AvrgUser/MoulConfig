package io.github.notenoughupdates.moulconfig.internal;

import io.github.notenoughupdates.moulconfig.common.RenderContext;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;

public final class DrawContextExt {
    public static final DrawContextExt INSTANCE = new DrawContextExt();

    private DrawContextExt() {
    }

    public static void drawStringCenteredScalingDownWithMaxWidth(
        RenderContext context,
        Component text,
        int centerX,
        int centerY,
        int maxWidth,
        int color
    ) {
        drawStringCenteredScalingDownWithMaxWidth(context, text, centerX, centerY, maxWidth, color, false, context.getMinecraft().getDefaultFontRenderer());
    }

    public static void drawStringCenteredScalingDownWithMaxWidth(
        RenderContext context,
        Component text,
        int centerX,
        int centerY,
        int maxWidth,
        int color,
        boolean shadow
    ) {
        drawStringCenteredScalingDownWithMaxWidth(context, text, centerX, centerY, maxWidth, color, shadow, context.getMinecraft().getDefaultFontRenderer());
    }

    public static void drawStringCenteredScalingDownWithMaxWidth(
        RenderContext context,
        Component text,
        int centerX,
        int centerY,
        int maxWidth,
        int color,
        boolean shadow,
        Font fr
    ) {
        context.pushMatrix();
        int width = fr.width(text);
        float factor = Math.min(maxWidth / (float) width, 1F);
        context.translate((float) centerX, (float) centerY);
        context.scale(factor, factor);
        context.drawString(fr, text, -width / 2, -fr.lineHeight / 2, color, shadow);
        context.popMatrix();
    }
}
