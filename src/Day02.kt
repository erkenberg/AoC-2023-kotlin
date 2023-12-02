fun main() {

    /**
     * Returns a map of color to maximum draws of this color for one game
     */
    fun parseGame(game: String): Map<String, Int> {
        val res = mutableMapOf("red" to 0, "blue" to 0, "green" to 0)
        val draws = game.substring(game.indexOf(":") + 1).split(";")

        for (draw in draws) {
            val cubes = draw.split(",")
            for (cube in cubes) {
                val split = cube.trim().split(" ")
                val count = split[0].toInt()
                val color = split[1].trim()
                if (res.containsKey(color) && res[color]!! < count) res[color] = count
            }
        }
        return res
    }


    fun part1(input: List<String>): Int {
        val games = input.map { parseGame(it) }
        var sum = 0
        games.forEachIndexed { index, entry ->
            if (entry["red"]!! <= 12 && entry["blue"]!! <= 14 && entry["green"]!! <= 13) sum += index + 1
        }
        return sum
    }

    fun part2(input: List<String>): Int {
        val games = input.map { parseGame(it) }
        var sum = 0
        games.forEach { entry -> sum += entry["red"]!! * entry["blue"]!! * entry["green"]!! }
        return sum
    }

    // test if implementation meets criteria from the description
    check(part1(readInput("Day02_test")) == 8)
    check(part2(readInput("Day02_test")) == 2286)

    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}
