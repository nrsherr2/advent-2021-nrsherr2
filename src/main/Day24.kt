import kotlin.math.floor
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.declaredMemberProperties
import kotlin.system.measureTimeMillis

fun main() {


    val day24Input = readInput("Day24_Input")

    val timeToExecuteDay24 = measureTimeMillis {
        val part1Output = Day24.part1(day24Input)
        val part2Output = Day24.part2(day24Input)
        println(
            """
                *** PART 1 ***
                $part1Output
                *** PART 2 ***
                $part2Output
                ***  END  ***
            """.trimIndent()
        )
    }
    println("Processing time: ${timeToExecuteDay24}ms")
}

object Day24 {
    fun part1(input: List<String>): Long {
        (99999999999999 downTo 10000000000000).asSequence().map { it.toString().toCharArray().toMutableList() }
            .filter { !it.contains('0') }.forEach { num ->
                val originalNum = num.joinToString("").toLong()
                if(num.joinToString("").toLong() % 10013 == 0L) println(num)
//                print("${num.joinToString("")} -> ")
                val alu = ALU()
                input.forEach { ln ->
                    val spl = ln.split(" ")
                    if (spl[0] == "inp") {
                        val value = num.removeAt(0).digitToInt()
                        alu.inp(spl[1], value)
                    } else {
                        val (instr, left, right) = spl
                        val func = alu::class.declaredMemberFunctions.first { it.name == instr }
                        func.call(alu, left, right)
                    }
                }
//                println(alu.report())
                if (alu.z == 0) return originalNum
            }
        return 0
    }

    fun part2(input: List<String>): Long {
        return 0
    }

    class ALU {
        var w: Int = 0
        var x: Int = 0
        var y: Int = 0
        var z: Int = 0

        fun inp(r: String, value: Int) {
            reg(r).setter.call(this, value)
        }

        fun add(r: String, roi: String) {
            val reg = reg(r)
            val v = reg.getter.call(this) + regOrInt(roi)
            reg.setter.call(this, v)
        }

        fun mul(r: String, roi: String) {
            val reg = reg(r)
            val v = reg.getter.call(this) * regOrInt(roi)
            reg.setter.call(this, v)
        }

        fun div(r: String, roi: String) {
            val reg = reg(r)
            val v = floor(reg.getter.call(this).toDouble() / regOrInt(roi).toDouble()).toInt()
            reg.setter.call(this, v)
        }

        fun mod(r: String, roi: String) {
            val reg = reg(r)
            val v = reg.getter.call(this) % regOrInt(roi)
            reg.setter.call(this, v)
        }

        fun eql(r: String, roi: String) {
            val reg = reg(r)
            val v = if (reg.getter.call(this) == regOrInt(roi)) 1 else 0
            reg.setter.call(this, v)
        }

        private fun reg(roi: String): KMutableProperty<Int> =
            ALU::class.declaredMemberProperties.first { it.name == roi } as KMutableProperty<Int>

        private fun regOrInt(roi: String): Int {
            return try {
                roi.toInt()
            } catch (e: NumberFormatException) {
                reg(roi).getter.call(this)
            }
        }

        fun report() = "[w=$w, x=$x, y=$y, z=$z]"
    }
}



