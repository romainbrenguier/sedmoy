class Mass {
    // in kg m-3
    public static double waterMassVolumic = 1000.0d;

    double inKg;

    Mass(double inKg) {
        this.inKg = inKg
    }

    static Mass ofLiquid(Volume volume, double volumicMass) {
        return new Mass(volume.getInCubeMeter() * volumicMass);
    }

    static Mass ofWater(Volume volume) {
        return ofLiquid(volume, waterMassVolumic);
    }
}

