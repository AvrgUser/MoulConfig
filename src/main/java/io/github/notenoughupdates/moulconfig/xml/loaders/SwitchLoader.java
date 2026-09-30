package io.github.notenoughupdates.moulconfig.xml.loaders;

import io.github.notenoughupdates.moulconfig.gui.component.SwitchComponent;
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

public class SwitchLoader implements XMLGuiLoader.Basic<SwitchComponent> {
    @Override
    public @NonNull SwitchComponent createInstance(@NonNull XMLContext<?> context, @NonNull Element element) {
        var value = context.getPropertyFromAttribute(element, new QName("value"), Boolean.class);
        var time = context.getPropertyFromAttribute(element, new QName("animationSpeed"), Integer.class);
        return new SwitchComponent(value, time == null ? 100 : time.get());
    }

    @Override
    public @NonNull QName getName() {
        return XMLUniverse.qName("Switch");
    }

    @Override
    public @NonNull ChildCount getChildCount() {
        return ChildCount.NONE;
    }

    @Override
    public @NonNull @Unmodifiable Map<String, Boolean> getAttributeNames() {
        return MapOfs.of("value", true, "animationSpeed", false);
    }

}
