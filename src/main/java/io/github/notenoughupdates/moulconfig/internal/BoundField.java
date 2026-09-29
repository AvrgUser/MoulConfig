package io.github.notenoughupdates.moulconfig.internal;

import java.lang.reflect.Field;
import java.util.Objects;

public record BoundField(Field field, Object boundTo) {

    public Field component1() {
        return field;
    }

    public Object component2() {
        return boundTo;
    }

    public BoundField copy(Field field, Object boundTo) {
        return new BoundField(field, boundTo);
    }

    @Override
    public String toString() {
        return field + " bound to " + boundTo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BoundField that)) return false;
        return Objects.equals(field, that.field) && Objects.equals(boundTo, that.boundTo);
    }

}
