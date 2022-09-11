import java.time.Duration

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

    State update(State state, Duration duration) {
        def newState = new State()
        def dV = Volume.cube(Length.ofMeter(0.01));
        for (Tube t : tubes) {
            Energy energyLost = energyLostThroughTube(state, t, dV)

            if (energyLost.inKgMeterSquarePerSecondSquare > 0) {
                def sourceLevel = state.getLevel(t.tankSourceId)
                def destLevel = state.getLevel(t.tankDestId)
                def flowSpeed = energyLost.speedOfCinetic(Mass.ofWater(dV))
                def volumeLost = new Volume(t.section, flowSpeed.times(duration))

                def sourceTank = tanks.get(t.tankSourceId)
                newState.addToExisting(
                        sourceTank,
                        Volume.zero() - volumeLost,
                        sourceTank.surface().times(sourceLevel))
                def destTank = tanks.get(t.tankDestId)
                newState.addToExisting(
                        destTank,
                        volumeLost,
                        destTank.surface().times(destLevel))
            }
        }
        return newState
    }

    Energy energyLostThroughTube(State state, Tube t, Volume dV) {
        def energyBefore = energyInTankAtHeight(state, t.tankSourceId, t.sourceHeight, dV)
        def energyAfter = energyInTankAtHeight(state, t.tankDestId, t.destHeight, dV)
        energyBefore - energyAfter
    }

    // Assuming no cinetic energy
    private Energy energyInTankAtHeight(State state, int tankId, Length atHeight, Volume volume) {
        def depth = state.getLevel(tankId) - atHeight
        if (depth.inMeter < 0) depth = Length.ofMeter(0)
        def altitude = tanks.get(tankId).altitude + atHeight
        Energy.ofPressure(depth, volume) + Energy.potential(altitude, Mass.ofWater(volume))
    }
}
