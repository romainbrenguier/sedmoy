import com.github.romainbrenguier.sedmoy.csv.Table

def table = input as Table

println "Input first 10 lines:\n${table.upFrom(10)}"

def keySet = [] as HashSet<String>

for (int i = 0; i < table.width(); ++i) {
    println("$i : ${table.cell(i, 0)}")
}

for (int i = 0; i < table.height(); ++i) {
    def cell = table.cell(1, i)
    if (cell.size() >= 2)
        keySet.add(cell.substring(0, 2))
}

println keySet
