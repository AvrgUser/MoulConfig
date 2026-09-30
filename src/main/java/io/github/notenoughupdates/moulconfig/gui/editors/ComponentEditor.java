package io.github.notenoughupdates.moulconfig.gui.editors;

import io.github.notenoughupdates.moulconfig.DescriptionRendereringBehaviour;
import io.github.notenoughupdates.moulconfig.TitleRenderingBehaviour;
import io.github.notenoughupdates.moulconfig.common.ComponentHelper;
import io.github.notenoughupdates.moulconfig.common.IMinecraft;
import io.github.notenoughupdates.moulconfig.common.RenderContext;
import io.github.notenoughupdates.moulconfig.gui.GuiComponent;
import io.github.notenoughupdates.moulconfig.gui.GuiContext;
import io.github.notenoughupdates.moulconfig.gui.GuiImmediateContext;
import io.github.notenoughupdates.moulconfig.gui.GuiOptionEditor;
import io.github.notenoughupdates.moulconfig.gui.KeyboardEvent;
import io.github.notenoughupdates.moulconfig.gui.MouseEvent;
import io.github.notenoughupdates.moulconfig.gui.component.CenterComponent;
import io.github.notenoughupdates.moulconfig.gui.component.PanelComponent;
import io.github.notenoughupdates.moulconfig.processor.ProcessedOption;
import lombok.Getter;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import java.util.List;

public abstract class ComponentEditor extends GuiOptionEditor {
    private static final int HEIGHT = 45;

    protected ComponentEditor(ProcessedOption option) {
        super(option);
    }

    public abstract @NonNull GuiComponent getDelegate();

    private @Nullable GuiComponent overlay;
    @Getter
    private int overlayX, overlayY;

    public void closeOverlay() {
        this.overlay = null;
    }

    public boolean isOverlayOpen() {
        return overlay != null;
    }

    // TODO: close overlay on scroll

    public void openOverlay(GuiComponent overlay, int overlayX, int overlayY) {
        this.overlay = overlay;
        this.overlayX = overlayX;
        this.overlayY = overlayY;
    }

    public @Nullable GuiComponent getOverlayDelegate() {
        return overlay;
    }

    public GuiImmediateContext getImmContext(
        int x, int y, int width, int height, RenderContext renderContext
    ) {
        IMinecraft instance = IMinecraft.INSTANCE;
        return new GuiImmediateContext(
            renderContext,
            x, y,
            width, height,
            instance.getMouseX() - x,
            instance.getMouseY() - y,
            instance.getMouseX(),
            instance.getMouseY(),
            (float) instance.getMouseXHF() - x,
            (float) instance.getMouseYHF() - y
        );
    }

    @NullMarked
    public class EditorComponentWrapper extends PanelComponent {
        public final @Nullable GuiComponent bottomComponent;

        public EditorComponentWrapper(GuiComponent component) {
            this(component, null);
        }

        public EditorComponentWrapper(GuiComponent component, @Nullable GuiComponent bottomComponent) {
            super(component);
            this.bottomComponent = bottomComponent;
        }

        @Override
        public int getWidth() {
            return super.getWidth() + 150;
        }

        protected int getDescriptionHeight() {
            if (option.getConfig().getDescriptionBehaviour(option) == DescriptionRendereringBehaviour.SCALE_TEXT)
                return super.getHeight();
            var fr = IMinecraft.INSTANCE.getDefaultFontRenderer();
            return Math.max(45, ComponentHelper.splitText(option.getDescription(), 250 * 2 / 3 - 10).size() * (fr.lineHeight + 1) + 10);
        }

        public int getTopHeight() {
            int height = getDescriptionHeight();
            if (option.getConfig().getTitleRenderingBehaviour(option) != TitleRenderingBehaviour.LEFT)
                height += IMinecraft.INSTANCE.getDefaultFontRenderer().lineHeight + 1;
            return Math.max(HEIGHT, height);
        }

        @Override
        public int getHeight() {
            return getTopHeight() + (bottomComponent != null ? bottomComponent.getHeight() + 10 : 0);
        }

        @Override
        protected GuiImmediateContext getChildContext(GuiImmediateContext context) {
            return context.translated(5, 15, context.width() / 3 - 10, context.height() - 15);
        }

        protected int getEffectiveTopHeight(GuiImmediateContext context) {
            return Math.min(context.height(), (getTopHeight()));
        }

        protected GuiImmediateContext getBottomContext(GuiImmediateContext context) {
            int effectiveTopHeight = getEffectiveTopHeight(context);
            return context.translated(5, effectiveTopHeight + bottomOffset, context.width() - 10, context.height() - effectiveTopHeight - bottomOffset - 8);
        }

        protected GuiImmediateContext getTopContext(GuiImmediateContext context) {
            return context.translated(0, 0, context.width(), getEffectiveTopHeight(context));
        }

        int bottomOffset = 0;

        @Override
        public void render(GuiImmediateContext context) {
            context.renderContext().drawDarkRect(0, 0, context.width(), context.height() - 2);

            var topContext = getTopContext(context);
            renderTitle(topContext);

            renderDescription(topContext);

            renderElement(topContext);

            context.renderContext().pushMatrix();
            context.renderContext().translate(5, getEffectiveTopHeight(context) + bottomOffset);
            renderBottomElement(getBottomContext(context));
            context.renderContext().popMatrix();
        }

        protected void renderBottomElement(GuiImmediateContext context) {
            if (bottomComponent != null)
                bottomComponent.render(context);
        }

        protected void renderElement(GuiImmediateContext context) {
            context.renderContext().pushMatrix();
            context.renderContext().translate(5, 15);
            this.getElement().render(getChildContext(context));
            context.renderContext().popMatrix();
        }

        protected void renderTitle(GuiImmediateContext context) {
            int width = context.width();
            var minecraft = context.renderContext().getMinecraft();
            var fr = minecraft.getDefaultFontRenderer();
            switch (option.getConfig().getTitleRenderingBehaviour(option)) {
                case WIDE_CENTERED_UNDERLINED:
                    context.renderContext().drawHorizontalLine(16, 10, width - 10, 0xFF404040);
                    // fallthrough;
                case WIDE_CENTERED:
                    context.renderContext().drawStringCenteredScaledMaxWidth(
                        option.getName(), fr, width / 2, 10, true, width - 10, 0xe0e0e0
                    );
                    break;
                case LEFT:
                    context.renderContext().drawStringCenteredScaledMaxWidth(
                        option.getName(), fr, width / 6, 10, true, width / 3 - 10, 0xe0e0e0
                    );
                    break;
            }
        }

        @Override
        public boolean mouseEvent(MouseEvent mouseEvent, GuiImmediateContext context) {
            if (super.mouseEvent(mouseEvent, getTopContext(context)))
                return true;
            return bottomComponent != null && bottomComponent.mouseEvent(mouseEvent, getBottomContext(context));
        }

        @Override
        public boolean keyboardEvent(KeyboardEvent event, GuiImmediateContext context) {
            if (super.keyboardEvent(event, getTopContext(context)))
                return true;
            return bottomComponent != null && bottomComponent.keyboardEvent(event, getBottomContext(context));
        }

        protected void renderDescription(GuiImmediateContext context) {
            int width = context.width();
            var minecraft = context.renderContext().getMinecraft();
            var fr = minecraft.getDefaultFontRenderer();
            int yOffset = option.getConfig().getTitleRenderingBehaviour(option) != TitleRenderingBehaviour.LEFT ? fr.lineHeight + 13 : 5;
            float scale = 1;
            List<Component> lines;
            int descriptionHeight = context.height() - yOffset;
            while (true) {
                lines = ComponentHelper.splitText(option.getDescription(), (int) (width * 2 / 3 / scale - 10));
                if (lines.size() * scale * (fr.lineHeight + 1) < descriptionHeight)
                    break;
                scale -= 1 / 8f;
                if (scale < 1 / 16f) break;
            }
            context.renderContext().pushMatrix();
            context.renderContext().translate(5 + width / 3, yOffset);
            context.renderContext().scale(scale, scale);
            for (var line : lines) {
                context.renderContext().drawString(fr, line, 0, 0, 0xc0c0c0, false);
                context.renderContext().translate(0, fr.lineHeight + 1);
            }
            context.renderContext().popMatrix();
        }
    }

    protected GuiComponent wrapComponent(GuiComponent component, @Nullable GuiComponent bottomComponent) {
        return new EditorComponentWrapper(
            new CenterComponent(component),
            bottomComponent
        );
    }

    protected GuiComponent wrapComponent(GuiComponent component) {
        return new EditorComponentWrapper(
            new CenterComponent(component)
        );
    }

    @Override
    public int getHeight() {
        return Math.max(getDelegate().getHeight(), super.getHeight());
    }

    private int lastRenderX, lastRenderY, lastRenderWidth, lastRenderHeight;

    @Override
    public final boolean mouseInput(int x, int y, int width, int mouseX, int mouseY, MouseEvent mouseEvent) {
        return getDelegate().mouseEvent(mouseEvent, getImmContext(x, y, width, getHeight(), IMinecraft.INSTANCE.provideTopLevelRenderContext()));
    }

    @Override
    public final boolean keyboardInput(KeyboardEvent keyboardEvent) {
        final var ctx = getImmContext(lastRenderX, lastRenderY, lastRenderWidth, lastRenderHeight, IMinecraft.INSTANCE.provideTopLevelRenderContext());
        final var overlay = getOverlayDelegate();
        if (overlay != null) {
            overlay.foldRecursive((Void) null, (comp, _void) -> {
                comp.setContext(getDelegate().getContext());
                return _void;
            });
            if (overlay.keyboardEvent(keyboardEvent, ctx))
                return true;
        }
        return getDelegate().keyboardEvent(keyboardEvent, ctx);
    }

    @Override
    public void setGuiContext(GuiContext guiContext) {
        getDelegate().foldRecursive((Void) null, (comp, _void) -> {
            comp.setContext(guiContext);
            return _void;
        });
    }

    @Override
    public final void render(RenderContext renderContext, int x, int y, int width) {
        // TODO: remove this
        lastRenderX = x;
        lastRenderY = y;
        lastRenderWidth = width;
        lastRenderHeight = getHeight();

        var context = getImmContext(x, y, width, getHeight(), renderContext);
        context.renderContext().pushMatrix();
        context.renderContext().translate(context.renderOffsetX(), context.renderOffsetY());
        getDelegate().render(context);
        context.renderContext().popMatrix();
    }

    @Override
    public final boolean mouseInputOverlay(int x, int y, int width, int mouseX, int mouseY, MouseEvent event) {
        if (overlay == null) return false;
        overlay.foldRecursive((Void) null, (comp, _void) -> {
            comp.setContext(getDelegate().getContext());
            return _void;
        });
        return overlay.mouseEvent(event, getImmContext(overlayX, overlayY, overlay.getWidth(), overlay.getHeight(), IMinecraft.INSTANCE.provideTopLevelRenderContext()));
    }

    @Override
    public final void renderOverlay(RenderContext context, int x, int y, int width) {
        if (overlay == null) return;
        overlay.foldRecursive((Void) null, (comp, _void) -> {
            comp.setContext(getDelegate().getContext());
            return _void;
        });
        final var ctx = getImmContext(overlayX, overlayY, overlay.getWidth(), overlay.getHeight(), context);
        ctx.renderContext().translate(overlayX, overlayY);
        overlay.render(ctx);
    }
}
