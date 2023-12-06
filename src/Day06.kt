fun main() {

    fun findRecordBeatingCount(time: Long, recordDistance: Long): Long {
        // brute force
        var res = 0L
        for (speed in 0..time) {
            val distance = speed * (time - speed)
            if (distance > recordDistance) res++
        }
        return res
    }


    fun part1(input: List<String>): Long {
        val times = splitIntString(input[0].split(":")[1]).map { it.toLong() }
        val recordDistances = splitIntString(input[1].split(":")[1]).map { it.toLong() }
        var res = 1L
        times.forEachIndexed { index, time ->
            res *= findRecordBeatingCount(time, recordDistances[index])
        }
        return res
    }

    fun part2(input: List<String>): Long {
        val time = whiteSpaceRegex.replace(input[0].split(":")[1], "").toLong()
        val recordDistance = whiteSpaceRegex.replace(input[1].split(":")[1], "").toLong()
        return findRecordBeatingCount(time, recordDistance)
    }

    // test if implementation meets criteria from the description
    check(part1(readInput("Day06_test")) == 288L)
    check(part2(readInput("Day06_test")) == 71503L)

    val input = readInput("Day06")
    part1(input).println()
    part2(input).println()
}
