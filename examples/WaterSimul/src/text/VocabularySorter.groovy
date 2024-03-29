package text

import java.nio.file.Path

def inputFile = "/home/rbrenguier/Documents/1000_most_common_Danish.csv"
def linesToSelect = 100

def table = input
if (input == null) {
    toRead = ((Path) currentDirectory).resolve(inputFile).normalize().toString()
    table = cachedFileReader.readCsv(toRead)
}

cachedFileReader.setSeparator("\t")

def indexedTable = [:]
def scores = [:]

static String keyOfString(String s) {
    (s.size() > 1 ? s.substring(0, 1) : s).toLowerCase()
}

static int scoreOfRank(int rank) { 10000 / rank }

static String formatSection(Map<String, String> map) {
    map.collect { "  * $it.key : $it.value" }.join("\n  ")
}

static String formatSectionKeyOnly(Map<String, String> map) {
    map.collect { "  * $it.key" }.join("\n")
}

static void makeIndexAndScore(table, indexedTable, scores, limitLines) {
    int offset = 1
    for (int i = offset; i < Math.min(table.getDimension().numberOfLines, limitLines); ++i) {
        def key = table.cellAsString(1, i)
        def value = table.cellAsString(2, i)
        indexedTable[key] = value
        def shortKey = keyOfString(key)
        scores[shortKey] = scores.getOrDefault(shortKey, 0) +
                scoreOfRank(Integer.parseInt(table.cellAsString(0, i).trim()))
    }
}

makeIndexAndScore(table, indexedTable, scores, linesToSelect)

def grouped = indexedTable.groupBy { keyOfString it.key as String }
def toLearn = grouped
   .sort { entry -> -scores[entry.key] }
   .collect {
            entry ->
                "# $entry.key [${scores[entry.key]}]\n\n" +
                        "  ${formatSection(entry.value as Map<String, String>)}"
        }.join("\n\n")

println(toLearn)

println("\n\n# Test")
def toTest = grouped
    .sort { entry -> -scores[entry.key] }
       .collect {
            entry ->
                "${formatSectionKeyOnly(entry.value as Map<String, String>)}"
        }.join("\n")

println(toTest)

