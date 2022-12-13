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
def map1 = mapOfInput(input1)
def map2 = mapOfInput(input2)
def map3 = mapOfInput(input3)
def map4 = mapOfInput(input4)

filterMap.forEach((key, value) ->
    println(map1.get(key) + " | " + map2.get(key) + " | " +
            map3.get(key) + " | " + map4.get(key) + " | " + key )
)
