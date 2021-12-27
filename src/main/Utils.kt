import java.io.File
import java.math.BigInteger
import java.security.MessageDigest

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = File("src/resources", "$name.txt").readLines()

/**
 * Converts string to md5 hash.
 */
fun String.md5(): String = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray())).toString(16)

fun assertEquals(expected: Any?, condition: Any?) {
    require(condition == expected) { "Test Failed! Expected $expected, Received $condition" }
}

fun String.toSortedString() = toCharArray().sorted().joinToString("")

data class Point(var rowNum: Int, var colNum: Int)
