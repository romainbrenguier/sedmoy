class Volume {

    double inCubeMeter;

    Volume(Length l1, Length l2, Length l3) {
        inCubeMeter = l1.inMeter * l2.inMeter * l3.inMeter
    }

    Volume(Surface surface, Length l) {
        inCubeMeter = surface.inSquareMeter * l.inMeter
    }

    public static Volume cube(Length l) {
        new Volume(l, l, l)
    }

    static Volume zero() {
        cube(Length.ofMeter(0.0))
    }

    Length divide(Surface s) {
        Length.ofMeter(inCubeMeter / s.inSquareMeter)
    }

    Volume minus(Volume v) {
        def volume = zero()
        volume.inCubeMeter = inCubeMeter - v.inCubeMeter
        volume
    }
}
