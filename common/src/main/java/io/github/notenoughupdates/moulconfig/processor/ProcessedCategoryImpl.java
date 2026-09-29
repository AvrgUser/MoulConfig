package io.github.notenoughupdates.moulconfig.processor;

import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProcessedCategoryImpl implements ProcessedCategory {
    public final Component name;
    public final Component desc;
    public final Field reflectField;
    public final List<ProcessedOption> options = new ArrayList<>();
    public final Map<Integer, ProcessedOption> accordionAnchors = new HashMap<>();

    public @Nullable String parent;

    @Override
    public Component getDisplayName() {
        return name;
    }

    @Override
    public Component getDescription() {
        return desc;
    }

    @Override
    public String getDebugDeclarationLocation() {
        return reflectField.toString();
    }

    @Override
    public String getIdentifier() {
        return reflectField.toString();
    }

    @Override
    public @Nullable String getParentCategoryId() {
        return parent;
    }

    @Override
    public @Unmodifiable List<ProcessedOption> getOptions() {
        return options;
    }

    @Override
    public @Unmodifiable Map<Integer, ProcessedOption> getAccordionAnchors() {
        return accordionAnchors;
    }

    public ProcessedCategoryImpl(Field field, Component name, Component desc) {
        this(field, name, desc, null);
    }

    public ProcessedCategoryImpl(Field field, Component name, Component desc, @Nullable String parent) {
        this.reflectField = field;
        this.name = name;
        this.parent = parent;
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "ProcessedCategory {" + getIdentifier() + "}";
    }
}
