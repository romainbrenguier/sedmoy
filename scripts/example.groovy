import com.github.romainbrenguier.sedmoy.model.Table

def table = input as Table

//table = table.upFrom(20)

static String normalize(String s) {
    s.toLowerCase()
}

static String keyOfString(String s) {
    normalize(s.size() > 2 ? s.substring(0, 2) : s)
}

def indexedTable = [:]
def scores = [:]

static int scoreOfRank(int rank) { 10000 / rank }

for (int i = 0; i < table.height(); ++i) {
    def key = table.cell(1, i)
    def value = table.cell(2, i)
    indexedTable[key] = value
    scores[keyOfString(key)] = scores.getOrDefault(keyOfString(key), 0) +
            scoreOfRank(Integer.parseInt(table.cell(0, i)))
}

def grouped = indexedTable.groupBy { keyOfString it.key as String }

static String formatSection(Map<String, String> map) {
    map.collect { "$it.key : $it.value" }.join("\n  * ")
}

grouped.sort { entry -> -scores[entry.key] }
        .collect {
            entry ->
                "# $entry.key [${scores[entry.key]}]\n\n" +
                        "  * ${formatSection(entry.value as Map<String, String>)}"
        }.join("\n\n")