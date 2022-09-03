def state = new State();

def system = new TankSystem()
def tank1 = system.addTank()
tank1.altitude = Length.ofMm(300)
def tank2 = system.addTank()
tank2.altitude = Length.ofMm(100)

def tube1 = new Tube()
tube1.setTankSourceId(tank1.id)
tube1.setTankDestId(tank2.id)
system.addTube(tube1)
system
