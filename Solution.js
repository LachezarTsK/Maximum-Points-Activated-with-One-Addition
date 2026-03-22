
/**
 * @param {number[][]} points
 * @return {number}
 */
var maxActivated = function (points) {
    const unionFind = new UnionFind(points.length);
    joinAllConnectedPoints(unionFind, points);
    return findMaxActivatedPointsWithOneAddition(unionFind, points);
};

/**
 * @param {UnionFind} unionFind 
 * @param {number[][]} points
 * @return {void}
 */
function joinAllConnectedPoints(unionFind, points) {
    const rowToIndex = new Map();
    const columnToIndex = new Map();

    for (let i = 0; i < points.length; ++i) {
        const row = points[i][0];
        const column = points[i][1];

        if (rowToIndex.has(row)) {
            unionFind.joinByRank(rowToIndex.get(row), i);
        }
        if (columnToIndex.has(column)) {
            unionFind.joinByRank(columnToIndex.get(column), i);
        }

        rowToIndex.set(row, i);
        columnToIndex.set(column, i);
    }
}

/**
 * @param {UnionFind} unionFind 
 * @param {number[][]} points
 * @return {number}
 */
function  findMaxActivatedPointsWithOneAddition(unionFind, points) {
    let largestGroup = 0;
    let secondLargestGroup = 0;

    for (let groupSize of unionFind.rank) {
        if (groupSize > largestGroup) {
            secondLargestGroup = largestGroup;
            largestGroup = groupSize;
        } else if (groupSize > secondLargestGroup) {
            secondLargestGroup = groupSize;
        }
    }

    if (largestGroup === points.length) {
        return largestGroup + 1;
    }

    return largestGroup + secondLargestGroup + 1;
}

class UnionFind {

    static INITIAL_RANK = 1;

    /**
     * @param {number} numberOfPoints
     */
    constructor(numberOfPoints) {
        this.rank = new Array(numberOfPoints).fill(UnionFind.INITIAL_RANK);
        this.parent = Array.from(new Array(numberOfPoints).keys());
    }

    /**
     * @param {number} index
     * @return {number}
     */
    findParent(index) {
        if (this.parent[index] !== index) {
            this.parent[index] = this.findParent(this.parent[index]);
        }
        return this.parent[index];
    }

    /**
     * @param {number} indexOne
     * @param {number} indexTwo 
     * @return {void}
     */
    joinByRank(indexOne, indexTwo) {
        const first = this.findParent(indexOne);
        const second = this.findParent(indexTwo);

        if (first === second) {
            return;
        }

        if (this.rank[first] >= this.rank[second]) {
            this.rank[first] += this.rank[second];
            this.parent[second] = first;
        } else {
            this.rank[second] += this.rank[first];
            this.parent[first] = second;
        }
    }
}
