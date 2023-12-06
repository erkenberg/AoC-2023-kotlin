import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readLines
import kotlin.io.path.readText

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = Path("src/$name.txt").readLines()
fun readInputAsString(name: String) = Path("src/$name.txt").readText()


/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)

val whiteSpaceRegex = Regex("\\s+")

/**
 * Takes a string of numbers and splits it in a List of Integers.
 */
fun splitIntString(input: String, separator: Regex = whiteSpaceRegex): List<Int> {
    return input.trim().split(separator).map { it.trim().toInt() }
}