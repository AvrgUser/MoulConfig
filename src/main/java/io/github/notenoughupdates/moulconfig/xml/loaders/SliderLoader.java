package io.github.notenoughupdates.moulconfig.xml.loaders;

import io.github.notenoughupdates.moulconfig.gui.component.SliderComponent;
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

public class SliderLoader implements XMLGuiLoader.Basic<SliderComponent> {
    @Override
    public @NonNull SliderComponent createInstance(@NonNull XMLContext<?> context, @NonNull Element element) {
        return new SliderComponent(
            context.getPropertyFromAttribute(element, new QName("value"), Float.class),
            context.getPropertyFromAttribute(element, new QName("minValue"), Float.class).get(),
            context.getPropertyFromAttribute(element, new QName("maxValue"), Float.class).get(),
            context.getPropertyFromAttribute(element, new QName("minStep"), Float.class, 1F),
            context.getPropertyFromAttribute(element, new QName("width"), Integer.class, 80)
        );
    }

    @Override
    public @NonNull QName getName() {
        return XMLUniverse.qName("Slider");
    }

    @Override
    public @NonNull ChildCount getChildCount() {
        return ChildCount.NONE;
    }

    @Override
    public @NonNull @Unmodifiable Map<String, Boolean> getAttributeNames() {
        return MapOfs.of("value", true, "minValue", true, "maxValue", true, "minStep", false, "width", false);
    }
}
