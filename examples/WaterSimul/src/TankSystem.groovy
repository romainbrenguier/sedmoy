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

    State update(State state) {
        def newState = new State()
        for (Tube t : tubes) {
            def sourceLevel = state.getLevel(t.tankSourceId)
            def destLevel = state.getLevel(t.tankDestId)
            if (sourceLevel.compare(t.sourceHeight) > 0) {
                def sourceTank = tanks.get(t.tankSourceId)

                def volume = Volume.cube(Length.ofMeter(-0.1))
                newState.addToExisting(
                        sourceTank,
                        volume,
                        sourceTank.surface().times(sourceLevel))
                def destTank = tanks.get(t.tankDestId)
                newState.addToExisting(
                        destTank,
                        Volume.cube(Length.ofMeter(0.1)),
                        destTank.surface().times(destLevel))
            }
        }
        return newState
    }
}
