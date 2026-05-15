package normallynormal.Input;

public record ControllerInput(String button, float value) implements Comparable<ControllerInput> {
    @Override
    public int compareTo(ControllerInput o) {
        int comp = button.compareTo(o.button);
        if (comp == 0) {
            if (this.value < o.value) {
                comp = -1;
            }
            else if (this.value > o.value) {
                comp = 1;
            }
        }
        return comp;
    }

    public boolean matchesButton(ControllerInput other) {
        return other.button.equals(this.button);
    }

    public String getDisplayText() {
        if (value < 0) return button + "-";
        return button;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ControllerInput)) {
            return false;
        }
        return ((ControllerInput) obj).button.equals(this.button) && ((ControllerInput) obj).value == this.value;
    }
}