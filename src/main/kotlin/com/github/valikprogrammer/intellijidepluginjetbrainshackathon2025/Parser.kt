import java.io.File


class Structure {
    val chunks: HashMap<String, Chunk> = hashMapOf()
    fun addChunk(name: String, chunk: Chunk) {
        chunks[name] = chunk
    }
    fun addDependencies() {
        val names = chunks.keys
        for (name in names) {
            for (line in chunks[name]!!.lines) {
                for (otherName in names) {
                    if (line.contains(otherName)) {
                        chunks[name]!!.addDependency(otherName)
                    }
                }
            }
        }
    }
    fun getChunkPrompt(name: String): String {
        var res = "chunk is ${chunks[name]!!.getText()}"
        for (dependency in chunks[name]!!.dependencies) {
            res += "Mentioned concept $dependency:\n" + chunks[dependency]!!.getText()
        }
        return res
    }
}

class Chunk(val start: Int) {
//    val firstStringIndex = 0
    var end = 0
    val lines: MutableList<String> = mutableListOf()
    val dependencies = mutableListOf<String>()
    fun addLine(value: String) {
        lines.add(value)
    }
    fun addDependency(value: String) {
        dependencies.add(value)
    }
    fun getText(): String {
        return lines.joinToString("\n")
    }
}

class Parser {
    fun extractName(header: String): String? {
        val regex = "(fun|class|data class)\\s+([\\w\\d_]+)".toRegex()
        val matchResult = regex.find(header)

        // Return the captured group (the name) or null if no match is found
        return matchResult?.groups?.get(2)?.value
    }

    fun coolStart(line: String): Boolean {
        val trimmed = line.trim()
        if (trimmed.startsWith("data") ||
            trimmed.startsWith("class") || trimmed.startsWith("fun")
        ) {
            return true
        }
        return false
    }

    fun parse(lines: List<String>): Structure {
        var res = Structure()
        val chunks = mutableListOf<Chunk>()
        var depth = 0
        var inside_object = false

        lines.forEachIndexed { index, line ->
            if (depth == 0 && inside_object) {
                chunks[chunks.size - 1].end = index

                inside_object = false
            }
            if (depth == 0 && coolStart(line)) { // starting a new chunk
                res.addChunk(extractName(line)!!, Chunk(index))
                inside_object = true
                chunks.add(Chunk(index))
            }
            if (line.contains("{")) {
                depth++
            }
            if (line.contains("}")) {
                depth--
            }
            if (inside_object) {
                chunks[chunks.size - 1].addLine(line)
            }
        }
        res.addDependencies()
        return res
    }

    fun main() {
        val lines = File("parse.kts").readLines()
        val structure = parse(lines)
        println(structure.getChunkPrompt("main"))
    }
}