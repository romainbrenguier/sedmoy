class Tube {
    int tankSourceId
    // Height is relative to corresponding tank
    Length sourceHeight = Length.ofMeter(0.1)
    int tankDestId
    Length destHeight = Length.ofMeter(0.1)

    @Override
    public String toString() {
        return "Tube{" +
                "src=" + tankSourceId +
                ", srcH=" + sourceHeight +
                ", dst=" + tankDestId +
                ", dstH=" + destHeight +
                '}';
    }
}
