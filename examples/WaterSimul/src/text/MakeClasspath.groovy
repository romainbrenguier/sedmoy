package text

//import jdk.internal.net.http.common.Pair

import java.nio.file.Path
import java.util.stream.Collectors

// TODO this should be provided by some sedmoy library

def inputFile = "/home/rbrenguier/git/Halo-dev-halo/dccs-460-classpath.txt"

def toRead = ((Path) currentDirectory).resolve(inputFile).normalize().toString()
def table = cachedFileReader.readCsv(toRead)

def lines = new ArrayList<String>()

for (int i = 0; i < table.getDimension().numberOfLines; ++i) {
    lines.add(table.cellAsString(0, i))
}

// Map artifact to group
// Map to version
def versionMap = new HashMap<String,String>()
def groupMap = new HashMap<String,String>()
lines.stream().map{ it.split("\\\\")}
        .forEach{groupMap.put(it[6], it[5] )}

lines.stream().map{ it.split("\\\\")}
        .forEach{versionMap.put(it[6], it[7] )}

groupMap.entrySet().stream()
        .map{"    implementation \"" + it.value + ":" + it.key +":" + versionMap.get(it.key) + "\""}
        .collect(Collectors.joining("\n"))


