package com.infinum.dbinspector.data.sources.memory.distance

import com.infinum.dbinspector.data.Sources
import kotlin.math.min

/**
 * Algorithm for measuring the difference between two Strings, also called a distance.
 *
 * It is the number of changes needed to change one String into another,
 * where each change is a single character modification.
 *
 */
internal class LevenshteinDistance : Sources.Memory.Distance {

    /**
     * Iterates over options and calculates unlimited distance between each option and query String.
     * Maps a tuple of index, option, and distance, then finds minimum distance tuple and returns index.
     *
     * @return result index of option with minimum distance.
     */
    override suspend fun calculate(query: String, options: List<String>): Int? =
        options.mapIndexed { index, option ->
            Triple(index, option, calculateUnlimited(query, option))
        }
            .minByOrNull { it.third }
            ?.first

    /**
     * Calculates Levenshtein distance between two Strings without a threshold to pass.
     * A higher score indicates a greater distance.
     *
     * calculateUnlimited("scar", "car")       = 1
     * calculateUnlimited("car", "mug")        = 3
     *
     * @return result distance.
     */
    @Suppress("NestedBlockDepth", "ReturnCount")
    private fun calculateUnlimited(
        query: String,
        other: String
    ): Int {
        var left: String = query
        var right: String = other

        var leftLength = left.length
        var rightLength = right.length
        when {
            leftLength == 0 -> return rightLength
            rightLength == 0 -> return leftLength
            else -> {
                if (leftLength > rightLength) {
                    val tmp: String = left
                    left = right
                    right = tmp
                    leftLength = rightLength
                    rightLength = right.length
                }
                val previous = IntArray(leftLength + 1)

                var i: Int
                var j = 1
                var upperLeft: Int
                var upper: Int
                var rightJ: Char
                var cost: Int
                i = 0
                while (i <= leftLength) {
                    previous[i] = i
                    i++
                }
                while (j <= rightLength) {
                    upperLeft = previous[0]
                    rightJ = right[j - 1]
                    previous[0] = j
                    i = 1
                    while (i <= leftLength) {
                        upper = previous[i]
                        cost = if (left[i - 1] == rightJ) 0 else 1
                        previous[i] = min(min(previous[i - 1] + 1, previous[i] + 1), upperLeft + cost)
                        upperLeft = upper
                        i++
                    }
                    j++
                }
                return previous[leftLength]
            }
        }
    }
}
