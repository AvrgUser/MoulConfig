package io.github.notenoughupdates.moulconfig.common;

import io.github.notenoughupdates.moulconfig.gui.GuiComponent;
import io.github.notenoughupdates.moulconfig.gui.GuiContext;
import io.github.notenoughupdates.moulconfig.gui.GuiElement;
import io.github.notenoughupdates.moulconfig.gui.GuiElementComponent;
import io.github.notenoughupdates.moulconfig.internal.InitUtil;
import io.github.notenoughupdates.moulconfig.internal.MCLogger;
import io.github.notenoughupdates.moulconfig.internal.Warnings;
import io.github.notenoughupdates.moulconfig.processor.MoulConfigProcessor;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.net.URI;
import java.util.ServiceLoader;

@NullMarked
public interface IMinecraft {
    InputStream loadResourceLocation(Identifier resourceLocation);

    MCLogger getLogger(String label);

    /**
     * @return if the {@code resourceLocation} is one that is only temporarily used as a generated target by {@link #generateDynamicTexture}.
     */
    boolean isGeneratedSentinel(Identifier resourceLocation);

    /**
     * Dynamically load a buffered image into a minecraft bindable texture. The returned resource location must be destroyed.
     */
    DynamicTextureReference generateDynamicTexture(BufferedImage image);

    MoulConfigPair<Double, Double> getMousePositionHF();

    default MoulConfigPair<Integer, Integer> getMousePosition() {
        var mousePositionHF = getMousePositionHF();
        return new MoulConfigPair<>(
            mousePositionHF.first().intValue(),
            mousePositionHF.second().intValue());
    }

    default int getMouseX() {
        return (int) getMouseXHF();
    }

    default int getMouseY() {
        return (int) getMouseYHF();
    }

    default double getMouseXHF() {
        return getMousePositionHF().first();
    }

    default double getMouseYHF() {
        return getMousePositionHF().second();
    }

    boolean isDevelopmentEnvironment();

    Font getDefaultFontRenderer();

    int getScaledWidth();

    int getScaledHeight();

    int getScaleFactor();

    boolean isOnMacOs();

    boolean isMouseButtonDown(int mouseButton);

    boolean isKeyboardKeyDown(int keyCode);

    void addExtraBuiltinConfigProcessors(MoulConfigProcessor<?> processor);

    void sendClickableChatMessage(Component message, String action, @Nullable ClickType clickType);

    default void sendChatMessage(Component message) {
        sendClickableChatMessage(message, "", null);
    }

    /**
     * This is a method to provide a render context. Note that constructing this context directly will potentially give
     * you an incorrect render state, leading to visual glitches. Depending on your platform, this might also require
     * additional platform specific cleanup / post rendering work to be done. Use only if you know exactly that none
     * of your rendering requires this extra functionality.
     *
     * @deprecated This context will be at the top level, not providing any of the useful translations and scalings that might be needed to render properly. Use with care.
     */
    @Deprecated
    RenderContext provideTopLevelRenderContext();

    default void openWrappedScreen(GuiElement guiElement) {
        openWrappedScreen(new GuiElementComponent(guiElement));
    }

    void openWrappedScreen(GuiContext guiContext);

    default void openWrappedScreen(GuiComponent component) {
        openWrappedScreen(new GuiContext(component));
    }

    void copyToClipboard(String string);

    String copyFromClipboard();

    default void startTextInput(Object owner, boolean focused) {
    }

    void openUri(URI uri);

    IMinecraft INSTANCE = InitUtil.makeUnchecked(() -> {
        var serviceLoader = ServiceLoader.load(IMinecraft.class);
        serviceLoader.reload();
        var iterator = serviceLoader.iterator();
        var instance = iterator.next();
        if (iterator.hasNext()) {
            var duplicate = iterator.next();
            Warnings.warn("Duplicate IMinecraft class ${duplicate}, ${instance}");
        }
        return instance;
    });

    static IMinecraft getInstance() {
        return INSTANCE;
    }
}
