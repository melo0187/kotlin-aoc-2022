import Command.Companion.toCommandOrNull
import FileSystemNode.Companion.toDirNameOrNull
import FileSystemNode.Companion.toFileSystemNodeOrNull
import kotlin.reflect.KClass

sealed interface FileSystemNode {
    fun size(): Int

    data class Dir(val name: String, val contents: MutableList<FileSystemNode> = mutableListOf()) : FileSystemNode {
        override fun size(): Int =
            contents.fold(0) { acc, fileSystemNode ->
                val size = when (fileSystemNode) {
                    is Dir -> fileSystemNode.size()
                    is File -> fileSystemNode.size
                }
                acc + size
            }
    }

    data class File(val size: Int) : FileSystemNode {
        override fun size(): Int = size
    }

    companion object {
        fun String.toFileSystemNodeOrNull(): FileSystemNode? =
            when {
                startsWith("dir") -> Dir(takeLastWhile { it != ' ' })
                startsWith('$') -> null
                else -> File(takeWhile { it != ' ' }.toInt())
            }

        fun String.toDirNameOrNull(): String? =
            when {
                startsWith("dir") -> takeLastWhile { it != ' ' }
                else -> null
            }

    }
}

sealed interface Command {
    data class CDin(val dirName: String) : Command
    object CDout : Command
    object LS : Command

    companion object {
        fun String.toCommandOrNull(): Command? =
            when {
                endsWith("..") -> CDout
                startsWith("$ cd") -> CDin(takeLastWhile { it != ' ' })
                startsWith("$ ls") -> LS
                else -> null
            }
    }
}

fun main() {
    fun part1(input: List<String>): Int {
        var currentDirName = ""
        return input
            .fold(mutableMapOf("/" to FileSystemNode.Dir("/"))) { dirs, line ->
                when (val command = line.toCommandOrNull()) {
                    Command.CDout -> {
                        dirs
                    }

                    is Command.CDin -> {
                        currentDirName = command.dirName
                        dirs
                    }

                    Command.LS -> {
                        dirs
                    }

                    null -> {
                        when (val node = line.toFileSystemNodeOrNull()) {
                            is FileSystemNode.Dir ->
                                dirs.getValue(currentDirName).contents.add(node)
                                    .also { dirs[node.name] = node }

                            is FileSystemNode.File ->
                                dirs.getValue(currentDirName).contents.add(node)

                            null ->
                                error("Every line should either be a command or ls output, but was $line")
                        }
                        dirs
                    }
                }
            }
            .filterValues {
                it
                    .also { "Node $it has size ${it.size()}".println() }
                    .size() <= 100000
            }
            .values
            .sumOf { it.size() }
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day07_test")
    check(part1(testInput) == 95437)

    val input = readInput("Day07")
    part1(input).println()
    part2(input).println()
}
