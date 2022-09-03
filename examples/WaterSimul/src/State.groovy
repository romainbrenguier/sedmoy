import java.util.Map.Entry
import java.util.stream.Collectors

class State {

    // Keys are tank ids
    private Map<Integer, Length> level = new HashMap<>()

    void add(Tank t, Volume level) {
        this.level.put(t.id, level.divide(t.surface()));
        null
    }

    void addToExisting(Tank t, Volume volume, Volume onCreate) {
        def existing = level.getOrDefault(t.id, onCreate.divide(t.surface()))
        level.put(t.id, existing + volume.divide(t.surface()))
    }

    Length getLevel(Integer tankId) {
        level.getOrDefault(tankId, Length.ofMeter(0.0))
    }

    @Override
    String toString() {
        def builder = new StringBuilder()
        builder.append("state:").append(level.entrySet().size()).append(" tanks\n")
        for (Entry<Tank, Integer> entry: level.entrySet()) {
            builder.append("Tank:\n")
            builder.append(entry.key).append('\n')
            builder.append("Level:").append(entry.value).append("\n")
        }
        builder.toString()
    }
}

