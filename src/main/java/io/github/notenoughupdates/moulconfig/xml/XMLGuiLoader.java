package io.github.notenoughupdates.moulconfig.xml;

import io.github.notenoughupdates.moulconfig.gui.GuiComponent;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.NonNull;
import org.w3c.dom.Element;
import java.util.Map;
import javax.xml.namespace.QName;

public interface XMLGuiLoader<T extends GuiComponent> {
    @NonNull
    T createInstance(@NonNull XMLContext<?> context, @NonNull Element element);

    @NonNull
    QName getName();

    @NonNull
    Element emitXSDType(@NonNull XSDGenerator generator, @NonNull Element root);

    interface Basic<T extends GuiComponent> extends XMLGuiLoader<T> {
        @NonNull
        ChildCount getChildCount();

        @NonNull
        @Unmodifiable
        Map<String, Boolean> getAttributeNames();

        @Override
        default @NonNull Element emitXSDType(@NonNull XSDGenerator generator, @NonNull Element root) {
            return generator.emitBasicType(this);
        }
    }
}
