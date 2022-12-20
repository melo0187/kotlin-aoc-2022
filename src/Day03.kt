fun main() {
    val priorityByItemType = (('a'..'z') + ('A'..'Z'))
        .mapIndexed { index, itemType ->
            val priority = index + 1
            itemType to priority
        }
        .toMap()

    fun part1(input: List<String>): Int = input
        .map { rucksack ->
            val (compartment1, compartment2) = rucksack.chunked(rucksack.length / 2)
            compartment1.toCharArray() to compartment2.toCharArray()
        }
        .map { (compartment1, compartment2) ->
            (compartment1 intersect compartment2.toSet()).single()
        }
        .sumOf { commonItemType ->
            priorityByItemType.getValue(commonItemType)
        }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    check(part1(testInput) == 157)

    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
}
