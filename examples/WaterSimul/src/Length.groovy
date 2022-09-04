class Length {
    public double inMeter = 0.0;

    public static Length ofMm(int mm) {
        def length = new Length()
        length.inMeter = ((double) mm) / 1000.0;
        length
    }

    public static Length ofMeter(double m) {
        def length = new Length()
        length.inMeter = m
        length
    }

    static Length meter() {
        return ofMeter(1.0d);
    }

    Length plus(Length other) {
        ofMeter(inMeter + other.inMeter)
    }

    int compare(Length other) {
        def diff = inMeter - other.inMeter
        if (diff > 0.0) return 1;
        if (diff < 0.0) return -1;
        return 0;
    }

    @Override
    String toString() {
        return inMeter + "m";
    }
}
