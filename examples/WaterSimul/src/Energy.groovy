class Energy {
    // in m s-2
    static final double GRAVITATION = 9.81;

    double inKgMeterSquarePerSecondSquare

    private Energy(double inKgMeterSquarePerSecondSquare) {
        this.inKgMeterSquarePerSecondSquare = inKgMeterSquarePerSecondSquare;
    }

    private Energy plus(Energy other) {
        new Energy(inKgMeterSquarePerSecondSquare + other.inKgMeterSquarePerSecondSquare)
    }

    private Energy minus(Energy other) {
        new Energy(inKgMeterSquarePerSecondSquare - other.inKgMeterSquarePerSecondSquare)
    }

    static Energy ofPressure(Length waterDepth, Volume volume) {
        new Energy(Mass.waterMassVolumic * waterDepth.inMeter * volume.inCubeMeter * GRAVITATION)
    }

    static Energy cinetic(Mass mass, Speed speed) {
        new Energy(0.5 * mass.inKg * speed.getInMeterPerSeconds() * speed.getInMeterPerSeconds())
    }

    static Energy potential(Length altitude, Mass mass) {
        new Energy(altitude.inMeter * mass.inKg * GRAVITATION)
    }

    Speed speedOfCinetic(Mass mass) {
        Speed.perSecond(Length.ofMeter(Math.sqrt(2 * inKgMeterSquarePerSecondSquare / mass.inKg)))
    }
}

