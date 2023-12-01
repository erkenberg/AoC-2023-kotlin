fun main() {

    fun part1(input: List<String>): Int {
        return (input.map { line ->
            ((line.find { it.isDigit() })!!.toString() + (line.last { it.isDigit() })).toInt()
        }).sum()
    }

    fun part2(input: List<String>): Int {
        val replacementMap = mapOf(
            "one" to "1",
            "two" to "2",
            "three" to "3",
            "four" to "4",
            "five" to "5",
            "six" to "6",
            "seven" to "7",
            "eight" to "8",
            "nine" to "9"
        )
        val matcher = Regex(
            replacementMap.keys.joinToString("|")
                    + "|"
                    + (1..9).joinToString("|")
        )

        fun toResult(line: String): Int {
            val firstMatch = matcher.find(line)!!.value
            val first = if (firstMatch.length == 1) firstMatch else replacementMap[firstMatch]!!

            var index = line.length - 1
            var lastMatch: MatchResult? = null
            while (index >= 0) {
                lastMatch = matcher.find(line.substring(index))
                if (lastMatch != null) break
                index--
            }
            val last = if (lastMatch!!.value.length == 1) lastMatch.value else replacementMap[lastMatch.value]!!
            return (first + last).toInt()
        }

        return (input.map { line -> toResult(line) }).sum()
    }

    // test if implementation meets criteria from the description, like:
    check(part1(readInput("Day01_test1")) == 142)
    check(part2(readInput("Day01_test2")) == 281)

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
