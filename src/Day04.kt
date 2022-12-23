fun main() {
    fun String.toSectionIdList(): List<Int> =
        let {
            split('-', limit = 2)
                .takeIf { it.size == 2 }
                ?: throw IllegalArgumentException("String does not describe a section assignment")
        }
            .map(String::toInt)
            .let { (rangeStart, rangeEnd) ->
                (rangeStart..rangeEnd).toList()
            }

    fun part1(input: List<String>): Int = input
        .map { sectionAssignmentPair ->
            sectionAssignmentPair.split(',', limit = 2)
                .takeIf { it.size == 2 }
                ?: throw IllegalArgumentException("String does not describe a section assignment pair")
        }
        .map { (firstElfSectionAssignment, secondElfSectionAssignment) ->
            firstElfSectionAssignment.toSectionIdList() to secondElfSectionAssignment.toSectionIdList()
        }
        .count { (firstElfSectionIdList, secondElfSectionIdList) ->
            firstElfSectionIdList.containsAll(secondElfSectionIdList) ||
                    secondElfSectionIdList.containsAll(firstElfSectionIdList)
        }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    check(part1(testInput) == 2)

    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()
}
