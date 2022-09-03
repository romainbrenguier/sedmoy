import java.util.stream.Collectors

def system = new TankSystem()
def tank1 = system.addTank()
tank1.altitude = Length.ofMeter(1)
def tank2 = system.addTank()
tank2.altitude = Length.ofMeter(1)

def tube1 = new Tube()
tube1.setTankSourceId(tank1.id)
tube1.setTankDestId(tank2.id)
system.addTube(tube1)
system

def state = new State();
state.add(tank1, Volume.cube(Length.ofMeter(0.9)))
state.add(tank2, Volume.cube(Length.ofMeter(0.9)))

def simulation = new ArrayList()
simulation.add(state)
for (int i = 0; i < 10; ++i) {
    simulation.add(system.update(simulation.get(i)))
}
def levels = simulation.stream().map(s -> s.level).collect(Collectors.toList())
levels
