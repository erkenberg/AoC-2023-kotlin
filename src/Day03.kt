import kotlin.math.max
import kotlin.math.min

class Schematics(input: List<String>) {
    private val schematics: Array<Array<Char>> = Array(input.size) { y -> Array(input[y].length) { x -> input[y][x] } }

    fun getPartNumbers(): List<Int> {
        val partNumbers: MutableList<Int> = mutableListOf()

        fun hasSurroundingSymbol(y: Int, x: Int): Boolean {
            for (yy in max((y - 1), 0)..min((y + 1), this.schematics.size - 1)) {
                for (xx in max((x - 1), 0)..min((x + 1), this.schematics[yy].size - 1)) {
                    if (xx == x && yy == y) continue
                    if (!(this.schematics[yy][xx].isDigit() || this.schematics[yy][xx] == '.')) return true
                }
            }
            return false
        }

        for (y in 0..<this.schematics.size) {
            var x = 0
            while (x < this.schematics[y].size) {
                if (!this.schematics[y][x].isDigit()) {
                    x++
                    continue
                }
                var isPart = false
                var number = ""
                while (true) {
                    number += this.schematics[y][x]
                    isPart = isPart || hasSurroundingSymbol(y, x)
                    x += 1
                    if (x >= this.schematics[y].size || !this.schematics[y][x].isDigit()) break
                }
                if (isPart) partNumbers.add(number.toInt())
            }
        }
        return partNumbers
    }

    fun getGearRatios(): List<Int> {
        val gearRatios: MutableList<Int> = mutableListOf()

        // NOTE: horrendously inefficient, don't do this at home. Could e.g. save tons of double checks with some more effort.
        fun calculateGearRatio(y: Int, x: Int): Int? {
            val numbers = mutableSetOf<Int>()
            for (yy in max((y - 1), 0)..min((y + 1), this.schematics.size - 1)) {
                for (xx in max((x - 1), 0)..min((x + 1), this.schematics[yy].size - 1)) {
                    var xNumber = xx
                    var number = ""
                    if (this.schematics[yy][xx].isDigit()) {
                        while (xNumber > 0 && this.schematics[yy][xNumber - 1].isDigit()) xNumber--
                        while (true) {
                            number += this.schematics[yy][xNumber]
                            xNumber += 1
                            if (xNumber >= this.schematics[yy].size || !this.schematics[yy][xNumber].isDigit()) break
                        }
                        // NOTE: assumes each number only occurs once
                        numbers.add(number.toInt())
                    }
                }
            }
            return if (numbers.size == 2) numbers.first() * numbers.last() else null
        }

        for (y in 0..<this.schematics.size) {
            for (x in 0..<this.schematics[y].size) {
                if (this.schematics[y][x] != '*') continue
                val gearRatio = calculateGearRatio(y, x)
                if (gearRatio != null) gearRatios.add(gearRatio)
            }
        }
        return gearRatios
    }

}


fun main() {
    fun part1(input: List<String>): Int {
        val schematics = Schematics(input)
        val sum = schematics.getPartNumbers().sum()
        return sum
    }

    fun part2(input: List<String>): Int {
        val schematics = Schematics(input)
        val sum = schematics.getGearRatios().sum()
        return sum
    }

    // test if implementation meets criteria from the description
    check(part1(readInput("Day03_test")) == 4361)
    check(part2(readInput("Day03_test")) == 467835)

    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
}
