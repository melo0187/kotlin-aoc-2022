fun main() {
    fun String.charsToProcessUntilFirstMarkerDetected(markerSize: Int): Int =
        toCharArray()
            .toList()
            .windowed(markerSize)
            .indexOfFirst { markerSizedCharSequence ->
                markerSizedCharSequence.distinct().size == markerSize
            } + markerSize

    fun part1(input: List<String>): List<Int> =
        input.map { datastreamBuffer -> datastreamBuffer.charsToProcessUntilFirstMarkerDetected(4) }

    fun part2(input: List<String>): List<Int> =
        input.map { datastreamBuffer -> datastreamBuffer.charsToProcessUntilFirstMarkerDetected(14) }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day06_test")
    check(part1(testInput) == listOf(7, 5, 6, 10, 11))
    check(part2(testInput) == listOf(19, 23, 23, 29, 26))

    val input = readInput("Day06")
    part1(input).println()
    part2(input).println()
}
