
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

public class Solution {

    public int maxActivated(int[][] points) {
        UnionFind unionFind = new UnionFind(points.length);
        joinAllConnectedPoints(unionFind, points);
        return findMaxActivatedPointsWithOneAddition(unionFind, points);
    }

    private static void joinAllConnectedPoints(UnionFind unionFind, int[][] points) {
        Map<Integer, Integer> rowToIndex = new HashMap<>();
        Map<Integer, Integer> columnToIndex = new HashMap<>();

        for (int i = 0; i < points.length; ++i) {
            int row = points[i][0];
            int column = points[i][1];

            if (rowToIndex.containsKey(row)) {
                unionFind.joinByRank(rowToIndex.get(row), i);
            }
            if (columnToIndex.containsKey(column)) {
                unionFind.joinByRank(columnToIndex.get(column), i);
            }

            rowToIndex.put(row, i);
            columnToIndex.put(column, i);
        }
    }

    private static int findMaxActivatedPointsWithOneAddition(UnionFind unionFind, int[][] points) {
        int largestGroup = 0;
        int secondLargestGroup = 0;

        for (int groupSize : unionFind.rank) {
            if (groupSize > largestGroup) {
                secondLargestGroup = largestGroup;
                largestGroup = groupSize;
            } else if (groupSize > secondLargestGroup) {
                secondLargestGroup = groupSize;
            }
        }

        if (largestGroup == points.length) {
            return largestGroup + 1;
        }

        return largestGroup + secondLargestGroup + 1;
    }
}

class UnionFind {

    private static final int INITIAL_RANK = 1;

    int[] rank;
    int[] parent;

    UnionFind(int numberOfPoints) {
        rank = new int[numberOfPoints];
        Arrays.fill(rank, INITIAL_RANK);
        parent = IntStream.range(0, numberOfPoints).toArray();
    }

    int findParent(int index) {
        if (parent[index] != index) {
            parent[index] = findParent(parent[index]);
        }
        return parent[index];
    }

    void joinByRank(int indexOne, int indexTwo) {
        int first = findParent(indexOne);
        int second = findParent(indexTwo);

        if (first == second) {
            return;
        }

        if (rank[first] >= rank[second]) {
            rank[first] += rank[second];
            parent[second] = first;
        } else {
            rank[second] += rank[first];
            parent[first] = second;
        }
    }
}
