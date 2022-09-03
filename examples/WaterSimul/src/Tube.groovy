class Tube {
    int tankSourceId
    // Height is relative to corresponding tank
    double sourceHeight
    int tankDestId
    double destHeight

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
