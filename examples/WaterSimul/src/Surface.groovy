class Surface {

    double inSquareMeter;

    Surface(Length l1, Length l2) {
        inSquareMeter = l1.inMeter * l2.inMeter
    }

    Volume times(Length l) {
        new Volume(this, l)
    }

    public static Surface square(Length l) {
        new Surface(l, l)
    }

    @Override
    public String toString() {
        return inSquareMeter + " m2";
    }
}
