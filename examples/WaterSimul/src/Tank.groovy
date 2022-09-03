class Tank {
    Length altitude = new Length()
    Length width = Length.ofMeter(1.0)
    int id

    Surface surface() {
        Surface.square(width)
    }

    @Override
    String toString() {
        "alt:" + altitude.toString() + ";\nwidth:" + width.toString()
    }
}