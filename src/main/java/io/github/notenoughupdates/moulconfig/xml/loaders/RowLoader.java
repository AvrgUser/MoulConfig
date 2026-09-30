package io.github.notenoughupdates.moulconfig.xml.loaders;

import io.github.notenoughupdates.moulconfig.gui.component.RowComponent;
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

public class RowLoader implements XMLGuiLoader.Basic<RowComponent> {
    @Override
    public @NonNull RowComponent createInstance(@NonNull XMLContext<?> context, @NonNull Element element) {
        return new RowComponent(context.getChildFragments(element));
    }

    @Override
    public @NonNull QName getName() {
        return XMLUniverse.qName("Row");
    }

    @Override
    public @NonNull ChildCount getChildCount() {
        return ChildCount.ANY;
    }

    @Override
    public @NonNull @Unmodifiable Map<String, Boolean> getAttributeNames() {
        return MapOfs.of();
    }
}
