package io.github.notenoughupdates.moulconfig.gui;

public interface MouseEvent {
    record Click(int mouseButton, boolean mouseState) implements MouseEvent {

        public int component1() {
                return mouseButton;
            }

            public boolean component2() {
                return mouseState;
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (!(o instanceof Click click)) return false;
                return mouseButton == click.mouseButton && mouseState == click.mouseState;
            }

        @Override
            public String toString() {
                return "Click(mouseButton=" + mouseButton + ", mouseState=" + mouseState + ")";
            }
        }

    record Move(float dx, float dy) implements MouseEvent {

        public float component1() {
                return dx;
            }

            public float component2() {
                return dy;
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (!(o instanceof Move move)) return false;
                return Float.compare(move.dx, dx) == 0 && Float.compare(move.dy, dy) == 0;
            }

        @Override
            public String toString() {
                return "Move(dx=" + dx + ", dy=" + dy + ")";
            }
        }

    final class Scroll implements MouseEvent {
        private final float dWheel;

        public Scroll(float dWheel) {
            this.dWheel = dWheel;
        }

        public float getDWheel() {
            return dWheel;
        }

        public float component1() {
            return dWheel;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Scroll scroll)) return false;
            return Float.compare(scroll.dWheel, dWheel) == 0;
        }

        @Override
        public int hashCode() {
            return Float.hashCode(dWheel);
        }

        @Override
        public String toString() {
            return "Scroll(dWheel=" + dWheel + ")";
        }
    }
}
