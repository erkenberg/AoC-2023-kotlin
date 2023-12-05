import kotlin.math.min

data class SeedRangeMapEntry(val destinationStart: Long, val sourceStart: Long, val range: Long)

class SeedRangeMap(private val entries: List<SeedRangeMapEntry>) {
    fun get(source: Long): Long {
        for (entry in entries) {
            if (source >= entry.sourceStart && source <= (entry.sourceStart + entry.range)) return entry.destinationStart + (source - entry.sourceStart)
        }
        return source
    }
}

fun main() {

    fun getSeeds(input: String): List<Long> {
        val seedRegex = Regex("seeds:([\\s\\d+]*)")
        val (seeds) = seedRegex.find(input)!!.destructured
        return seeds.trim().split(whiteSpaceRegex).map { it.toLong() }
    }

    fun getSeedRangeMap(input: String, mapName: String): SeedRangeMap {
        val sectionRegex = Regex("$mapName map:([\\s\\d+]*)")
        val rangeRegex = Regex("(\\d+)\\s(\\d+)\\s(\\d+)")
        val (section) = sectionRegex.find(input)!!.destructured
        val entries = section.trim().split("\n").map {
            val (destinationStart, sourceStart, range) = rangeRegex.find(it)!!.destructured
            SeedRangeMapEntry(destinationStart.toLong(), sourceStart.toLong(), range.toLong())
        }
        return SeedRangeMap(entries)
    }

    fun part1(input: String): Long {
        val seeds = getSeeds(input)
        val locations = seeds.map { seed ->
            val soil = getSeedRangeMap(input, "seed-to-soil").get(seed)
            val fertilizer = getSeedRangeMap(input, "soil-to-fertilizer").get(soil)
            val water = getSeedRangeMap(input, "fertilizer-to-water").get(fertilizer)
            val light = getSeedRangeMap(input, "water-to-light").get(water)
            val temperature = getSeedRangeMap(input, "light-to-temperature").get(light)
            val humidity = getSeedRangeMap(input, "temperature-to-humidity").get(temperature)
            val location = getSeedRangeMap(input, "humidity-to-location").get(humidity)
            location
        }

        return locations.min()
    }

    fun part2(input: String): Long {
        val seedRanges = getSeeds(input)
        val soilMap = getSeedRangeMap(input, "seed-to-soil")
        val fertilizerMap = getSeedRangeMap(input, "soil-to-fertilizer")
        val waterMap = getSeedRangeMap(input, "fertilizer-to-water")
        val lightMap = getSeedRangeMap(input, "water-to-light")
        val temperatureMap = getSeedRangeMap(input, "light-to-temperature")
        val humidityMap = getSeedRangeMap(input, "temperature-to-humidity")
        val locationMap = getSeedRangeMap(input, "humidity-to-location")

        var minLocation = Long.MAX_VALUE

        // slow brood force, there might be a faster solution, but its good enough, took roughly 5 minutes
        for (entry in seedRanges.indices step 2) {
            "Entry Index $entry => ${(entry * 100) / seedRanges.size}%".println()
            for (range in 0..<seedRanges[entry + 1]) {
                if (range % 25000000 == 0L) "Range $range from ${seedRanges[entry + 1]} => ${(range * 100) / seedRanges[entry + 1]}%".println()
                val soil = soilMap.get(seedRanges[entry] + range)
                val fertilizer = fertilizerMap.get(soil)
                val water = waterMap.get(fertilizer)
                val light = lightMap.get(water)
                val temperature = temperatureMap.get(light)
                val humidity = humidityMap.get(temperature)
                val location = locationMap.get(humidity)
                if (location < minLocation) "Smaller result: $location for seed ${seedRanges[0] + range}".println()
                minLocation = min(minLocation, location)
            }
        }
        return minLocation
    }

    // test if implementation meets criteria from the description
    check(part1(readInputAsString("Day05_test")) == 35L)
    check(part2(readInputAsString("Day05_test")) == 46L)

    val input = readInputAsString("Day05")
    part1(input).println()
    part2(input).println()
}
