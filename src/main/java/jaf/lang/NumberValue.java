package jaf.lang;

public final class NumberValue implements Value {

    private final double value;

    public NumberValue(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    public int intValue() {
        return (int) value;
    }

    @Override
    public String getType() {
        return "number";
    }

    @Override
    public String toString() {
        if (value == (long) value) {
            return Long.toString((long) value);
        }
        return Double.toString(value);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof NumberValue)) return false;
        NumberValue other = (NumberValue) obj;
        return Double.compare(value, other.value) == 0;
    }

    @Override
    public int hashCode() {
        return Double.hashCode(value);
    }
}