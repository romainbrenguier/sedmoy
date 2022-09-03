class Length {
    public int mm = 10;

    public static Length ofMm(int mm) {
        def length = new Length()
        length.mm = mm
        length
    }

    @Override
    String toString() {
        return mm + "mm";
    }
}
