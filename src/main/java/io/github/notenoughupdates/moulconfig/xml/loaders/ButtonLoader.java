package io.github.notenoughupdates.moulconfig.xml.loaders;

import io.github.notenoughupdates.moulconfig.gui.component.ButtonComponent;
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

public class ButtonLoader implements XMLGuiLoader.Basic<ButtonComponent> {
    @Override
    public @NonNull ButtonComponent createInstance(@NonNull XMLContext<?> context, @NonNull Element element) {
        return new ButtonComponent(
            context.getChildFragment(element),
            context.getPropertyFromAttribute(element, new QName("margin"), int.class, 2),
            context.getMethodFromAttribute(element, new QName("onClick"))
        );
    }

    @Override
    public @NonNull QName getName() {
        return XMLUniverse.qName("Button");
    }

    @Override
    public @NonNull ChildCount getChildCount() {
        return ChildCount.ONE;
    }

    @Override
    public @NonNull @Unmodifiable Map<String, Boolean> getAttributeNames() {
        return MapOfs.of(
            "margin", false,
            "onClick", true
        );
    }
}
