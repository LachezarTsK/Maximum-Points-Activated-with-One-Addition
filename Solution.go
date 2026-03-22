
package main

func maxActivated(points [][]int) int {
    unionFind := NewUnionFind(len(points))
    joinAllConnectedPoints(&unionFind, points)
    return findMaxActivatedPointsWithOneAddition(&unionFind, points)
}

func joinAllConnectedPoints(unionFind *UnionFind, points [][]int) {
    rowToIndex := map[int]int{}
    columnToIndex := map[int]int{}

    for i := range points {
        row := points[i][0]
        column := points[i][1]

        if containsKey(rowToIndex, row) {
            unionFind.joinByRank(rowToIndex[row], i)
        }
        if containsKey(columnToIndex, column) {
            unionFind.joinByRank(columnToIndex[column], i)
        }

        rowToIndex[row] = i
        columnToIndex[column] = i
    }
}

func findMaxActivatedPointsWithOneAddition(unionFind *UnionFind, points [][]int) int {
    largestGroup := 0
    secondLargestGroup := 0

    for _, groupSize := range unionFind.rank {
        if groupSize > largestGroup {
            secondLargestGroup = largestGroup
            largestGroup = groupSize
        } else if groupSize > secondLargestGroup {
            secondLargestGroup = groupSize
        }
    }

    if largestGroup == len(points) {
        return largestGroup + 1
    }

    return largestGroup + secondLargestGroup + 1
}

func containsKey[Key comparable, Value any](mapToCheck map[Key]Value, key Key) bool {
    var has bool
    _, has = mapToCheck[key]
    return has
}

type UnionFind struct {
    INITIAL_RANK int
    rank         []int
    parent       []int
}

func NewUnionFind(numberOfPoints int) UnionFind {
    unionFind := UnionFind{
        INITIAL_RANK: 1,
        rank:         make([]int, numberOfPoints),
        parent:       make([]int, numberOfPoints),
    }
    for i := range numberOfPoints {
        unionFind.rank[i] = unionFind.INITIAL_RANK
        unionFind.parent[i] = i
    }
    return unionFind
}

func (this UnionFind) findParent(index int) int {
    if this.parent[index] != index {
        this.parent[index] = this.findParent(this.parent[index])
    }
    return this.parent[index]
}

func (this UnionFind) joinByRank(indexOne int, indexTwo int) {
    first := this.findParent(indexOne)
    second := this.findParent(indexTwo)

    if first == second {
        return
    }

    if this.rank[first] >= this.rank[second] {
        this.rank[first] += this.rank[second]
        this.parent[second] = first
    } else {
        this.rank[second] += this.rank[first]
        this.parent[first] = second
    }
}
