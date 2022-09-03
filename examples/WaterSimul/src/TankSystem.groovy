class TankSystem {
    List<Tank> tanks = new ArrayList<>()
    List<Tube> tubes = new ArrayList<>()

    Tank addTank() {
        def tank = new Tank()
        tank.id = tanks.size()
        tanks.add(tank)
        tank
    }

    void addTube(Tube tube) {
        tubes.add(tube)
    }
}
