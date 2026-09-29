package io.github.notenoughupdates.moulconfig.platform;

import com.mojang.blaze3d.platform.InputConstants;
import io.github.notenoughupdates.moulconfig.common.*;
import io.github.notenoughupdates.moulconfig.gui.GuiContext;
import io.github.notenoughupdates.moulconfig.internal.FilterAssertionCache;
import io.github.notenoughupdates.moulconfig.internal.MCLogger;
import io.github.notenoughupdates.moulconfig.internal.Warnings;
import io.github.notenoughupdates.moulconfig.processor.MoulConfigProcessor;
import io.github.notenoughupdates.moulconfig.xml.XMLUniverse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.net.URI;
import java.util.Arrays;
import java.util.Objects;

@Slf4j
@NullMarked
public class MoulConfigPlatform implements IMinecraft {
    public static @Nullable MoulConfigPlatform instance;
    Minecraft mc = Minecraft.getInstance();

    public MoulConfigPlatform() {
        if (instance != null) {
            Warnings.warn("Constructed duplicate MoulConfig instance");
        }
        instance = this;
    }

    @SneakyThrows
    @Override
    public InputStream loadResourceLocation(Identifier resourceLocation) {
        return mc.getResourceManager()
            .getResourceOrThrow(resourceLocation)
            .open();
    }

    @Override
    public MCLogger getLogger(String label) {
        Logger logger = LogManager.getLogger(label);
        return new MCLogger() {
            @Override
            public void warn(@NotNull String text) {
                logger.warn(text);
            }

            @Override
            public void info(@NotNull String text) {
                logger.info(text);
            }

            @Override
            public void error(@NotNull String text, @NotNull Throwable throwable) {
                logger.error(text, throwable);
            }
        };
    }

    @Override
    public boolean isGeneratedSentinel(Identifier resourceLocation) {
        return Objects.equals("moulconfig", resourceLocation.getNamespace())
            && resourceLocation.getPath().startsWith("dynamic/");
    }

    private static void setTextureData(DynamicTexture texture, BufferedImage image) {
        var destinationImage = texture.getPixels();
        assert destinationImage != null;
        for (int i = 0; i < image.getWidth(); i++) {
            for (int j = 0; j < image.getHeight(); j++) {
                var argb = image.getRGB(i, j);
                destinationImage.setPixel(i, j, argb);
            }
        }
    }

    @Override
    public DynamicTextureReference generateDynamicTexture(BufferedImage img) {
        var identifier = Identifier.fromNamespaceAndPath(
            "moulconfig",
            "dynamic/" + java.util.concurrent.ThreadLocalRandom.current().nextLong()
        );

        var texture = new DynamicTexture(
            identifier.getPath(),
            img.getWidth(),
            img.getHeight(),
            true
        );

        setTextureData(texture, img);
        texture.upload();
        mc.getTextureManager().register(identifier, texture);
        return new DynamicTextureReference() {
            @Override
            public Identifier getIdentifier() {
                return identifier;
            }

            @Override
            public void update(BufferedImage bufferedImage) {
                setTextureData(texture, bufferedImage);
                texture.upload();
            }

            @Override
            protected void doDestroy() {
                FilterAssertionCache.destroyGlobalFilter(identifier);
                mc.getTextureManager().release(identifier);
            }
        };
    }

    @Override
    public MoulConfigPair<Double, Double> getMousePositionHF() {
        var mouse = mc.mouseHandler;
        var window = mc.getWindow();
        double x = mouse.getScaledXPos(window);
        double y = mouse.getScaledYPos(window);
        return new MoulConfigPair<>(x, y);
    }

    @Override
    public boolean isDevelopmentEnvironment() {
        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }

    @Override
    public Font getDefaultFontRenderer() {
        return mc.font;
    }

    @Override
    public int getScaledWidth() {
        return mc.getWindow().getGuiScaledWidth();
    }

    @Override
    public int getScaledHeight() {
        return mc.getWindow().getGuiScaledHeight();
    }

    @Override
    public int getScaleFactor() {
        return mc.getWindow().getGuiScale();
    }

    @Override
    public boolean isOnMacOs() {
        return net.minecraft.util.Util.getPlatform() == net.minecraft.util.Util.OS.OSX;
    }

    @Override
    public boolean isMouseButtonDown(int mouseButton) {
        return GLFW.glfwGetMouseButton(
            mc.getWindow().handle(),
            mouseButton
        ) == GLFW.GLFW_PRESS;
    }

    @Override
    public boolean isKeyboardKeyDown(int keyboardKey) {
        return InputConstants.isKeyDown(
            mc.getWindow(),
            keyboardKey
        );
    }

    @Override
    public void addExtraBuiltinConfigProcessors(MoulConfigProcessor<?> processor) {

    }

    @Override
    public void sendClickableChatMessage(
        Component message,
        String action,
        @Nullable ClickType type
    ) {
        if (type != null) {
            message = message.copy().withStyle(it -> it.withClickEvent(switch (type) {
                case OPEN_LINK -> new ClickEvent.OpenUrl(URI.create(action));
                case RUN_COMMAND -> new ClickEvent.RunCommand(action);
            }));
        }

        //? if >=26.2 {
        mc.gui.hud.getChat().addClientSystemMessage(message);
        //?} else {
        /*mc.gui.getChat().addClientSystemMessage(message);
         *///?}
    }

    @Override
    public String getKeyName(InputConstants.Key key) {
        val componentName = key.getDisplayName();
        val collapsed = componentName.tryCollapseToString();
        if (collapsed != null) {
            return collapsed;
        }
        return componentName.getString();
    }

    @Override
    public MutableComponent createLiteral(String text) {
        return Component.literal(text);
    }

    @Override
    public MutableComponent createTranslatable(String key, Component... args) {
        return Component.translatable(key, Arrays.stream(args).toArray());
    }

    @Override
    public @Nullable Component createComponentInternal(Object obj) {
        if (obj instanceof Component text) {
            return text;
        }

        return null;
    }

    @ApiStatus.Internal
    public static GuiGraphicsExtractor makeDrawContext() {
        var mc = Minecraft.getInstance();

        return new GuiGraphicsExtractor(
            mc,
            //? if >=26.2 {
            mc.gameRenderer.gameRenderState().guiRenderState
            //?} else {
            /*mc.gameRenderer.getGameRenderState().guiRenderState
             *///?}
            ,
            (int) mc.mouseHandler.getScaledXPos(mc.getWindow()),
            (int) mc.mouseHandler.getScaledYPos(mc.getWindow())
        );
    }

    @Override
    public RenderContext provideTopLevelRenderContext() {
        return new MoulConfigRenderContext(makeDrawContext());
    }

    public void openWrappedScreen(Screen screen) {
        //? if >=26.2 {
        mc.gui.setScreen(screen);
        //?} else {
        /*mc.setScreen(screen);
         *///?}
    }

    @Override
    public void openWrappedScreen(GuiContext gui) {
        openWrappedScreen(new MoulConfigScreenComponent(Component.empty(), gui, null));
    }

    @Override
    public void registerPlatformTypeMorphisms(XMLUniverse universe) {
        universe.registerTypeMorphism(
            new BoxNativeMorphisms.ComponentMorphism()
        );
    }

    @Override
    public void copyToClipboard(String string) {
        mc.keyboardHandler.setClipboard(string);
    }

    @Override
    public String copyFromClipboard() {
        return mc.keyboardHandler.getClipboard();
    }
}
