import java.time.Duration
import java.util.stream.Collectors

def system = new TankSystem()
def tank1 = system.addTank()
tank1.altitude = Length.ofMeter(2)
def tank2 = system.addTank()
tank2.altitude = Length.ofMeter(1)

def tube1 = new Tube()
tube1.setTankSourceId(tank1.id)
tube1.setTankDestId(tank2.id)
tube1.section = Surface.square(Length.ofMeter(0.01))
system.addTube(tube1)

def state = new State();
state.add(tank1, Surface.square(Length.meter()).times(Length.ofMeter(0.5)))
state.add(tank2, Surface.square(Length.meter()).times(Length.ofMeter(0.0)))

def simulation = new ArrayList<State>()
simulation.add(state)

for (int i = 0; i < 10; ++i) {
    simulation.add(system.update(simulation.get(i), Duration.ofMillis(1000l)))
}

def levels = simulation.stream().map(s -> s.level).collect(Collectors.toList())
