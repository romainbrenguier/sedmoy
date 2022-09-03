class Tank {
    Length altitude = new Length()
    Length width = new Length()
    int id

    @Override
    String toString() {
        "alt:" + altitude.toString() + ";\nwidth:" + width.toString()
    }
}