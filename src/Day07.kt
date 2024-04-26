import Command.Companion.toCommandOrNull
import FileSystemNode.Companion.toFileSystemNodeOrNull

sealed interface FileSystemNode {
    fun size(): Long

    data class Dir(val name: String, val contents: MutableList<FileSystemNode> = mutableListOf()) : FileSystemNode {
        override fun size(): Long =
            contents.fold(0) { acc, fileSystemNode ->
                val size = when (fileSystemNode) {
                    is Dir -> fileSystemNode.size()
                    is File -> fileSystemNode.size
                }
                acc + size
            }
    }

    data class File(val name: String, val size: Long) : FileSystemNode {
        override fun size(): Long = size
    }

    companion object {
        fun String.toFileSystemNodeOrNull(): FileSystemNode? =
            when {
                startsWith("dir") -> Dir(takeLastWhile { it != ' ' })
                startsWith('$') -> null
                else -> {
                    val (size, name) = split(' ')
                    File(name, size.toLong())
                }
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
    fun part1(input: List<String>): Long {
        var currentDir = FileSystemNode.Dir("/")
        return input
            .fold(mutableListOf(currentDir)) { dirs, line ->
                when (val command = line.toCommandOrNull()) {
                    Command.CDout -> {
                        // do nothing
                    }

                    is Command.CDin -> {
                        currentDir = dirs.find { it.name == command.dirName }
                            ?: error("Dir we cd into should have been added already when it was read from ls output")
                    }

                    Command.LS -> {
                        // do nothing
                    }

                    null -> {
                        when (val node = line.toFileSystemNodeOrNull()) {
                            is FileSystemNode.Dir ->
                                currentDir.contents.add(node)
                                    .also { dirs.add(node) }

                            is FileSystemNode.File ->
                                currentDir.contents.add(node)

                            null ->
                                error("Every line should either be a command or ls output, but was $line")
                        }
                    }
                }
                dirs
            }.onEach {
                it.println()
                it.size().println()
            }
            .filter { dir ->
                dir.size() <= 100000
            }
            .sumOf(FileSystemNode.Dir::size)
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day07_test")
    check(part1(testInput) == 95437.toLong())

    val input = readInput("Day07")
    part1(input).println()
    part2(input).println()
}
