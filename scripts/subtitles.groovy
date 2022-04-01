import com.github.romainbrenguier.sedmoy.csv.Table

// Take an srt file and remove non text

def table = input as Table

int to_skip = 2
for (int i = 0; i < table.height(); ++i) {
    if (to_skip > 0) {
        to_skip--
    } else if (table.cell(0, i).isEmpty()) {
        to_skip = 2
        println("<p>")
    } else {
        println(String.join("\n", table.row(i)))
    }
}