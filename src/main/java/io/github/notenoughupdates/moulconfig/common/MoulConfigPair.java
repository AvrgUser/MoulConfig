package io.github.notenoughupdates.moulconfig.common;

import java.util.Objects;

public record MoulConfigPair<A, B>(A first, B second) {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MoulConfigPair<?, ?> that)) return false;
        return Objects.equals(first, that.first) && Objects.equals(second, that.second);
    }

    @Override
    public String toString() {
        return "(" + first + ", " + second + ")";
    }
}
