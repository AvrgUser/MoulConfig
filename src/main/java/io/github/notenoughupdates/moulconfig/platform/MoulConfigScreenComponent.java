package io.github.notenoughupdates.moulconfig.platform;

import com.mojang.blaze3d.platform.InputConstants;
import io.github.notenoughupdates.moulconfig.common.IMinecraft;
import io.github.notenoughupdates.moulconfig.gui.CloseEventListener;
import io.github.notenoughupdates.moulconfig.gui.GuiContext;
import io.github.notenoughupdates.moulconfig.gui.GuiImmediateContext;
import io.github.notenoughupdates.moulconfig.gui.KeyboardEvent;
import io.github.notenoughupdates.moulconfig.gui.MouseEvent;
import lombok.Getter;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public class MoulConfigScreenComponent extends Screen {
    @Getter
    final GuiContext guiContext;
    @Getter
    final @Nullable Screen previousScreen;

    public MoulConfigScreenComponent(
        Component title,
        GuiContext guiContext,
        @Nullable Screen previousScreen) {
        super(title);
        this.guiContext = guiContext;
        this.previousScreen = previousScreen;
        guiContext.setCloseRequestHandler(this::onClose);
    }

    public GuiImmediateContext createContext() {
        return createContext(null);
    }

    public GuiImmediateContext createContext(GuiGraphicsExtractor drawContext) {
        assert minecraft != null;
        var im = IMinecraft.INSTANCE;
        var mousePos = im.getMousePositionHF();
        var x = mousePos.first().intValue();
        var y = mousePos.second().intValue();
        return new GuiImmediateContext(
            new MoulConfigRenderContext(drawContext != null ? drawContext : MoulConfigPlatform.makeDrawContext()),
            0, 0,
            im.getScaledWidth(),
            im.getScaledHeight(),
            x, y, x, y,
            mousePos.first().floatValue(),
            mousePos.second().floatValue()
        );
    }

    @Override
    public void onClose() {
        if (guiContext.onBeforeClose() == CloseEventListener.CloseAction.NO_OBJECTIONS_TO_CLOSE)
            super.onClose();
    }

    @Override
    public void removed() {
        super.removed();
        guiContext.onAfterClose();
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor context, int mouseX, int mouseY, float deltaTicks) {
        super.extractRenderState(context, mouseX, mouseY, deltaTicks);
        var ctx = createContext(context);
        guiContext.getRoot().render(ctx);
        ctx.renderContext().renderExtraLayers();
    }

    @Override
    public boolean charTyped(CharacterEvent input) {
        return guiContext.getRoot().keyboardEvent(new KeyboardEvent.CharTyped((char) input.codepoint()), createContext());
    }

    @Override
    public boolean keyPressed(KeyEvent input) {
        int keyCode = input.key();
        //~ if >=26.3 'scancode' -> 'keycode'
        int scanCode = input.keycode();
        if (guiContext.root.keyboardEvent(new KeyboardEvent.KeyPressed(keyCode, scanCode, true), createContext()))
            return true;
        if (keyCode == InputConstants.KEY_ESCAPE) {
            if (guiContext.getFocusedElement() != null) {
                guiContext.setFocusedElement(null);
            } else {
                onClose();
            }
            return true;
        }
        return false;
    }

    public boolean keyReleased(KeyEvent input) {
        int keyCode = input.key();
        //~ if >=26.3 'scancode' -> 'keycode'
        int scanCode = input.keycode();
        return guiContext.root.keyboardEvent(
            new KeyboardEvent.KeyPressed(keyCode, scanCode, false),
            createContext()
        );
    }

    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        var ctx = createContext();
        var dx = (int) mouseX;
        var dy = (int) mouseY;
        var event = new MouseEvent.Move(
            ((float) mouseX) - ctx.mouseXHF(),
            ((float) mouseY) - ctx.mouseYHF()
        );
        ctx = new GuiImmediateContext(
            ctx.renderContext(),
            ctx.renderOffsetX(),
            ctx.renderOffsetY(),
            ctx.width(),
            ctx.height(),
            dx, dy,
            dx, dy,
            (float) mouseX, (float) mouseY
        );

        guiContext.getRoot().mouseEvent(event, ctx);
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent click, boolean doubled) {
        return guiContext.root.mouseEvent(
            new MouseEvent.Click(click.button(), true), createContext()
        );
    }

    @Override
    public boolean mouseReleased(MouseButtonEvent click) {
        return guiContext.root.mouseEvent(
            new MouseEvent.Click(click.button(), false), createContext()
        );
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        return guiContext.root.mouseEvent(
            new MouseEvent.Scroll(
                ((float) verticalAmount)
            ),
            createContext()
        );
    }


    @Override
    public boolean mouseDragged(
        MouseButtonEvent click, double offsetX, double offsetY
    ) {
        return true;
    }
}
