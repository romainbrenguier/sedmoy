package text

import java.nio.file.Path

def inputFile = "/home/rbrenguier/Documents/1000_most_common_Danish.csv"
def linesToSelect = 200

def table = input
if (input == null) {
    toRead = ((Path) currentDirectory).resolve(inputFile).normalize().toString()
    table = cachedFileReader.readCsv(toRead)
}

cachedFileReader.setSeparator("\t")

def indexedTable = [:]
def scores = [:]

static def vowels() {
    return "[aeiouyаеиоуяъ]"
}

static String firstVowel(String s) {
    for (int i = 0; i < s.length(); ++i) {
        if (java.util.regex.Pattern.matches(vowels(), s.charAt(i).
                toString()))
            return s.charAt(i).toString()
    }
    return "_"
}

static String firstConsonnent(String s) {
    for (int i = 0; i < s.length(); ++i) {
        if (!(java.util.regex.Pattern.matches(vowels(), s.charAt(i).
                toString())))
            return s.charAt(i).toString()
    }
    return "_"
}

static String keyOfString(String s) {
//    (s.size() > 1 ? s.substring(0, 1) : s).toLowerCase()
    firstVowel(s)+firstConsonnent(s)
}

static int scoreOfRank(int rank) { 10000 / rank }

static String formatSection(Map<String, String> map) {
    map.sort {it.key} .collect { "  * $it.key : $it.value" }.join("\n  ")
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
   .sort { entry -> entry.key }
//-scores[entry.key] }
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


println("\n\n# Solution")
def solution = grouped
    .sort { entry -> -scores[entry.key] }
       .collect {
            entry ->
                "  ${formatSection(entry.value as Map<String, String>)}"
        }.join("\n")

println(solution)
