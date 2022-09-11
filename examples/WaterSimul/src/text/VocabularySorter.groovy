package text

import java.nio.file.Path

def table = cachedFileReader.readCsv(((Path) currentDirectory).resolve("voc-example1.csv").normalize().toString())

def indexedTable = [:]
def scores = [:]

static String keyOfString(String s) {
    (s.size() > 1 ? s.substring(0, 1) : s).toLowerCase()
}

static int scoreOfRank(int rank) { 10000 / rank }

int offset = 1
for (int i = offset; i < 51; ++i) {
    def key = table.cellAsString(1, i)
    def value = table.cellAsString(2, i)
    indexedTable[key] = value
    def shortKey = keyOfString(key)
    scores[shortKey] = scores.getOrDefault(shortKey, 0) +
            scoreOfRank(Integer.parseInt(table.cellAsString(0, i).trim()))
}

static String formatSection(Map<String, String> map) {
    map.collect { "$it.key : $it.value" }.join("\n  ")
}

def grouped = indexedTable.groupBy { keyOfString it.key as String }

//grouped.entrySet()
grouped
   .sort { entry -> -scores[entry.key] }
   .collect {
            entry ->
                "# $entry.key [${scores[entry.key]}]\n\n" +
                        "  ${formatSection(entry.value as Map<String, String>)}"
        }.join("\n\n")
