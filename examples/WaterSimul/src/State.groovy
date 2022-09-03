import java.util.Map.Entry
import java.util.stream.Collectors

class State {

    Map<Tank, Integer> level = new HashMap<>()

    void add(Tank t, int level) {
        this.level.put(t, level);
        null
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

