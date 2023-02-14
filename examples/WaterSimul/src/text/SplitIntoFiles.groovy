package text


def chapterNumber = 0
def file = chapitreWriter(chapterNumber)

private static FileWriter chapitreWriter(i) {
    new FileWriter(new File("Chapitre_" + i + ".txt"))
}

def table = input

for (int i = 0; i < table.getDimension().numberOfLines; ++i) {
    def line = table.cellAsString(0, i)
    if (line.startsWith("[]{#anchor") && line.contains("}Chapitre")) {
        file.close()
        file = chapitreWriter(++chapterNumber)
    }
    file.write(line + "\n")
}

file.close()
