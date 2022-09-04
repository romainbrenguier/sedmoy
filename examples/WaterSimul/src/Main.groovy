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
state.add(tank1, Surface.square(Length.meter()).times(Length.ofMeter(1.5)))
state.add(tank2, Volume.cube(Length.meter()))

def simulation = new ArrayList<State>()
simulation.add(state)
for (int i = 0; i < 10; ++i) {
    simulation.add(system.update(simulation.get(i)))
}
def levels = simulation.stream().map(s -> s.level).collect(Collectors.toList())
levels
