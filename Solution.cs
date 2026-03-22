
using System;
using System.Linq;
using System.Collections.Generic;

public class Solution
{
    public int MaxActivated(int[][] points)
    {
        UnionFind unionFind = new UnionFind(points.Length);
        joinAllConnectedPoints(unionFind, points);
        return FindMaxActivatedPointsWithOneAddition(unionFind, points);
    }

    private static void joinAllConnectedPoints(UnionFind unionFind, int[][] points)
    {
        Dictionary<int, int> rowToIndex = [];
        Dictionary<int, int> columnToIndex = [];

        for (int i = 0; i < points.Length; ++i)
        {
            int row = points[i][0];
            int column = points[i][1];

            if (rowToIndex.ContainsKey(row))
            {
                unionFind.JoinByRank(rowToIndex[row], i);
            }
            if (columnToIndex.ContainsKey(column))
            {
                unionFind.JoinByRank(columnToIndex[column], i);
            }

            rowToIndex[row] = i;
            columnToIndex[column] = i;
        }
    }

    private static int FindMaxActivatedPointsWithOneAddition(UnionFind unionFind, int[][] points)
    {
        int largestGroup = 0;
        int secondLargestGroup = 0;

        foreach (int groupSize in unionFind.Rank)
        {
            if (groupSize > largestGroup)
            {
                secondLargestGroup = largestGroup;
                largestGroup = groupSize;
            }
            else if (groupSize > secondLargestGroup)
            {
                secondLargestGroup = groupSize;
            }
        }

        if (largestGroup == points.Length)
        {
            return largestGroup + 1;
        }

        return largestGroup + secondLargestGroup + 1;
    }
}

class UnionFind
{
    private static readonly int INITIAL_RANK = 1;

    public readonly int[] Rank;
    public readonly int[] Parent;

    public UnionFind(int numberOfPoints)
    {
        Rank = Enumerable.Repeat(INITIAL_RANK, numberOfPoints).ToArray();
        Parent = Enumerable.Range(0, numberOfPoints).ToArray();
    }

    public int FindParent(int index)
    {
        if (Parent[index] != index)
        {
            Parent[index] = FindParent(Parent[index]);
        }
        return Parent[index];
    }

    public void JoinByRank(int indexOne, int indexTwo)
    {
        int first = FindParent(indexOne);
        int second = FindParent(indexTwo);

        if (first == second)
        {
            return;
        }

        if (Rank[first] >= Rank[second])
        {
            Rank[first] += Rank[second];
            Parent[second] = first;
        }
        else
        {
            Rank[second] += Rank[first];
            Parent[first] = second;
        }
    }
}
