
class Solution {

    fun maxActivated(points: Array<IntArray>): Int {
        val unionFind = UnionFind(points.size)
        joinAllConnectedPoints(unionFind, points)
        return findMaxActivatedPointsWithOneAddition(unionFind, points)
    }

    private fun joinAllConnectedPoints(unionFind: UnionFind, points: Array<IntArray>) {
        val rowToIndex = mutableMapOf<Int, Int>()
        val columnToIndex = mutableMapOf<Int, Int>()

        for (i in points.indices) {
            val row = points[i][0]
            val column = points[i][1]

            if (rowToIndex.containsKey(row)) {
                unionFind.joinByRank(rowToIndex[row]!!, i);
            }
            if (columnToIndex.containsKey(column)) {
                unionFind.joinByRank(columnToIndex[column]!!, i);
            }

            rowToIndex[row] = i
            columnToIndex[column] = i
        }
    }

    private fun findMaxActivatedPointsWithOneAddition(unionFind: UnionFind, points: Array<IntArray>): Int {
        var largestGroup = 0
        var secondLargestGroup = 0

        for (groupSize in unionFind.rank) {
            if (groupSize > largestGroup) {
                secondLargestGroup = largestGroup;
                largestGroup = groupSize
            } else if (groupSize > secondLargestGroup) {
                secondLargestGroup = groupSize
            }
        }

        if (largestGroup == points.size) {
            return largestGroup + 1
        }

        return largestGroup + secondLargestGroup + 1
    }
}

class UnionFind(val numberOfPoints: Int) {

    private companion object {
        const val INITIAL_RANK = 1
    }

    val rank = IntArray(numberOfPoints) { INITIAL_RANK }
    val parent = IntArray(numberOfPoints) { i -> i }

    fun findParent(index: Int): Int {
        if (parent[index] != index) {
            parent[index] = findParent(parent[index])
        }
        return parent[index]
    }

    fun joinByRank(indexOne: Int, indexTwo: Int) {
        val first = findParent(indexOne)
        val second = findParent(indexTwo)

        if (first == second) {
            return
        }

        if (rank[first] >= rank[second]) {
            rank[first] += rank[second]
            parent[second] = first
        } else {
            rank[second] += rank[first]
            parent[first] = second
        }
    }
}
