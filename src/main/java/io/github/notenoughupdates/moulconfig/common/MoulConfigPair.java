package io.github.notenoughupdates.moulconfig.common;

import java.util.Objects;

public record MoulConfigPair<A, B>(A first, B second) {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MoulConfigPair<?, ?>(Object first1, Object second1))) return false;
        return Objects.equals(first, first1) && Objects.equals(second, second1);
    }

    @Override
    public String toString() {
        return "(" + first + ", " + second + ")";
    }
}
