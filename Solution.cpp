
#include <span>
#include <ranges>
#include <vector>
#include <numeric>
#include <unordered_map>
using namespace std;

struct UnionFind {

    inline static const int INITIAL_RANK = 1;

    vector<int> rank;
    vector<int> parent;

    UnionFind(int numberOfPoints) {
        rank.assign(numberOfPoints, INITIAL_RANK);
        parent.resize(numberOfPoints);
        ranges::iota(parent, 0);
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
        }
        else {
            rank[second] += rank[first];
            parent[first] = second;
        }
    }
};

class Solution {

public:
    int maxActivated(vector<vector<int>>& points) const {
        UnionFind unionFind(points.size());
        joinAllConnectedPoints(unionFind, points);
        return findMaxActivatedPointsWithOneAddition(unionFind, points);
    }

private:
    static void joinAllConnectedPoints(UnionFind& unionFind, span<const vector<int>> points) {
        unordered_map<int, int> rowToIndex;
        unordered_map<int, int> columnToIndex;

        for (int i = 0; i < points.size(); ++i) {
            int row = points[i][0];
            int column = points[i][1];

            if (rowToIndex.contains(row)) {
                unionFind.joinByRank(rowToIndex[row], i);
            }
            if (columnToIndex.contains(column)) {
                unionFind.joinByRank(columnToIndex[column], i);
            }

            rowToIndex[row] = i;
            columnToIndex[column] = i;
        }
    }

    static int findMaxActivatedPointsWithOneAddition(const UnionFind& unionFind, span<const vector<int>> points) {
        int largestGroup = 0;
        int secondLargestGroup = 0;

        for (int groupSize : unionFind.rank) {
            if (groupSize > largestGroup) {
                secondLargestGroup = largestGroup;
                largestGroup = groupSize;
            }
            else if (groupSize > secondLargestGroup) {
                secondLargestGroup = groupSize;
            }
        }

        if (largestGroup == points.size()) {
            return largestGroup + 1;
        }

        return largestGroup + secondLargestGroup + 1;
    }
};
