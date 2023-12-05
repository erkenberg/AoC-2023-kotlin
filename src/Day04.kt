import kotlin.math.pow

data class ScratchInfo(
    val card: Int,
    val winners: List<Int>,
    val own: List<Int>,
    val duplicates: Int,
    var instances: Int = 1
)

fun main() {


    fun findDuplicateCount(first: List<Int>, second: List<Int>): Int {
        var counter = 0
        for (number in first) {
            if (second.contains(number)) counter++
        }
        return counter
    }

    fun parseScratchInfo(input: List<String>): Map<Int, ScratchInfo> = input.associate { line: String ->
        val parts = line.split(*arrayOf(":", "|"))
        val card = parts[0].split(whiteSpaceRegex)[1].toInt()
        val winners = parts[1].trim().split(whiteSpaceRegex).map { it.toInt() }
        val own = parts[2].trim().split(whiteSpaceRegex).map { it.toInt() }
        val duplicates = findDuplicateCount(winners, own)
        card to ScratchInfo(card, winners, own, duplicates)
    }

    fun part1(input: List<String>): Int {
        val scratchInfo = parseScratchInfo(input)
        return (scratchInfo.values.map { if (it.duplicates > 0) (2.0.pow((it.duplicates - 1).toDouble())).toInt() else 0 }).sum()
    }

    fun part2(input: List<String>): Int {
        val scratchInfo = parseScratchInfo(input)
        scratchInfo.forEach { (card, info) ->
            for (next in (card + 1)..(card + info.duplicates)) {
                scratchInfo[next]!!.instances += info.instances
            }
        }
        return (scratchInfo.values.map { it.instances }).sum()
    }

    // test if implementation meets criteria from the description
    check(part1(readInput("Day04_test")) == 13)
    check(part2(readInput("Day04_test")) == 30)

    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()
}
