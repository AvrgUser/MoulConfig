package io.github.notenoughupdates.moulconfig.xml.loaders;

import io.github.notenoughupdates.moulconfig.gui.component.ColumnComponent;
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

public class ColumnLoader implements XMLGuiLoader.Basic<ColumnComponent> {
    @Override
    public @NonNull ColumnComponent createInstance(@NonNull XMLContext<?> context, @NonNull Element element) {
        return new ColumnComponent(context.getChildFragments(element));
    }

    @Override
    public @NonNull QName getName() {
        return XMLUniverse.qName("Column");
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
