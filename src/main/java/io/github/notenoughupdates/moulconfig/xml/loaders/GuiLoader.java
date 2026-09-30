package io.github.notenoughupdates.moulconfig.xml.loaders;

import io.github.notenoughupdates.moulconfig.gui.component.CenterComponent;
import io.github.notenoughupdates.moulconfig.gui.component.PanelComponent;
import io.github.notenoughupdates.moulconfig.internal.MapOfs;
import io.github.notenoughupdates.moulconfig.xml.ChildCount;
import io.github.notenoughupdates.moulconfig.xml.XMLContext;
import io.github.notenoughupdates.moulconfig.xml.XMLGuiLoader;
import io.github.notenoughupdates.moulconfig.xml.XMLUniverse;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.NonNull;
import org.w3c.dom.Element;
import java.util.Map;
import javax.xml.namespace.QName;

public class GuiLoader implements XMLGuiLoader.Basic<CenterComponent> {
    @Override
    public @NonNull CenterComponent createInstance(@NonNull XMLContext<?> context, @NonNull Element element) {
        return new CenterComponent(new PanelComponent(context.getChildFragment(element)));
    }

    @Override
    public @NonNull QName getName() {
        return XMLUniverse.qName("Gui");
    }

    @Override
    public @NonNull ChildCount getChildCount() {
        return ChildCount.ONE;
    }

    @Override
    public @NonNull @Unmodifiable Map<String, Boolean> getAttributeNames() {
        return MapOfs.of();
    }
}
