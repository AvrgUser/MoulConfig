package io.github.notenoughupdates.moulconfig.xml.loaders;

import io.github.notenoughupdates.moulconfig.common.IMinecraft;
import io.github.notenoughupdates.moulconfig.gui.component.CollapsibleComponent;
import io.github.notenoughupdates.moulconfig.gui.component.TextComponent;
import io.github.notenoughupdates.moulconfig.observer.GetSetter;
import io.github.notenoughupdates.moulconfig.xml.ChildCount;
import io.github.notenoughupdates.moulconfig.xml.XMLContext;
import io.github.notenoughupdates.moulconfig.xml.XMLGuiLoader;
import io.github.notenoughupdates.moulconfig.xml.XMLUniverse;
import net.minecraft.network.chat.Component;
import org.w3c.dom.Element;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.xml.namespace.QName;

public class BasicCollapsibleLoader implements XMLGuiLoader.Basic<CollapsibleComponent> {
    @Override
    public CollapsibleComponent createInstance(XMLContext<?> context, Element element) {
        GetSetter<Boolean> state = context.getPropertyFromAttribute(element, new QName("value"), Boolean.class);
        if (state == null) state = GetSetter.floating(true);
        io.github.notenoughupdates.moulconfig.gui.GuiComponent body = context.getChildFragment(element);
        GetSetter<String> title = context.getPropertyFromAttribute(element, new QName("title"), String.class);
        TextComponent textComponent = new TextComponent(
            IMinecraft.INSTANCE.getDefaultFontRenderer(),
            () -> Component.literal(title.get()),
            IMinecraft.INSTANCE.getDefaultFontRenderer().width(title.get()),
            TextComponent.TextAlignment.LEFT,
            false,
            false
        );
        return new CollapsibleComponent(() -> textComponent, () -> body, state);
    }

    @Override
    public QName getName() {
        return XMLUniverse.qName("Collapsible");
    }

    @Override
    public ChildCount getChildCount() {
        return ChildCount.ONE;
    }

    @Override
    public Map<String, Boolean> getAttributeNames() {
        Map<String, Boolean> map = new LinkedHashMap<>();
        map.put("title", true);
        map.put("value", false);
        return map;
    }
}
