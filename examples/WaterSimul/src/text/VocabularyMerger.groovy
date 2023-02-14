package text

import java.nio.file.Path

static Map<String, String> mapOfInput(table) {
    def map = [:]

    for (int i = 0; i < table.getDimension().numberOfLines; ++i) {
        map.put(table.cellAsString(2, i), table.cellAsString(1, i))
    }
    return map
}

def filterMap = mapOfInput(input)
def mapList = new ArrayList<Map<String, String>>()
for (int i = 1; i < inputs.size(); ++i) {
    mapList.add(mapOfInput(inputs.get(i)))
}

int i = 0;
filterMap.forEach((key, value) ->
        println(Integer.toString(i++) + ";" + mapList.collect {it.get(key)}.
                join(";") +
                ";" + key))
