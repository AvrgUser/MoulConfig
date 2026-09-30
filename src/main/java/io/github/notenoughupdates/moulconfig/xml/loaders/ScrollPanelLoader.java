package io.github.notenoughupdates.moulconfig.xml.loaders;

import io.github.notenoughupdates.moulconfig.gui.component.ScrollPanelComponent;
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

public class ScrollPanelLoader implements XMLGuiLoader.Basic<ScrollPanelComponent> {
    @Override
    public @NonNull ScrollPanelComponent createInstance(@NonNull XMLContext<?> context, @NonNull Element element) {
        return new ScrollPanelComponent(
            context.getPropertyFromAttribute(element, new QName("width"), Integer.class).get(),
            context.getPropertyFromAttribute(element, new QName("height"), Integer.class).get(),
            context.getChildFragment(element)
        );
    }

    @Override
    public @NonNull QName getName() {
        return XMLUniverse.qName("ScrollPanel");
    }

    @Override
    public @NonNull ChildCount getChildCount() {
        return ChildCount.ONE;
    }

    @Override
    public @NonNull @Unmodifiable Map<String, Boolean> getAttributeNames() {
        return MapOfs.of("width", true, "height", true);
    }
}
