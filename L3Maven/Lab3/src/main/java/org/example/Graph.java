// REMOVE THIS BEFORE TURNING IN
package org.example;

import java.io.*;
import java.util.*;

public class Graph
{
    public enum Representation
    {
        AdjacencyList,
        EdgeList,
        EdgeListSimplified,
        Matrix
    }

    // ---------------------

    public int noOfVertices = - 1;

    // Adjacency list representing the graph.
    public int[][] edges = null;


    // Creates a new empty graph.
    public Graph() { }

    public Graph(BufferedReader in, Representation rep)
    {
        switch (rep)
        {
            case AdjacencyList:
                readAdjacencyList(in);
                break;

            case EdgeList:
                readEdgeList(in, false);
                break;

            case EdgeListSimplified:
                readEdgeList(in, true);
                break;

            case Matrix:
                readMatrix(in);
                break;
        }
    }

    private int[] readNextLine(BufferedReader in)
    {
        String line = null;

        try
        {
            line = in.readLine();
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }

        String[] strNumbers = line.split("\\s");

        int[] numbers = new int[strNumbers.length];

        for (int i = 0; i < numbers.length; i++)
        {
            numbers[i] = Integer.parseInt(strNumbers[i]);
        }

        return numbers;
    }

    private void readAdjacencyList(BufferedReader in)
    {
        noOfVertices = readNextLine(in)[0];
        edges = new int[noOfVertices][];

        for (int i = 0; i < noOfVertices; i++)
        {
            edges[i] = readNextLine(in);
        }
    }

    private void readEdgeList(BufferedReader in, boolean simplified)
    {
        int[] graphSize = readNextLine(in);

        noOfVertices = graphSize[0];
        int noOfEdges = graphSize[1];

        // WTF Java, why?
        // ArrayList<Integer>[] buffer = new ArrayList<Integer>[noOfVertices];

        ArrayList<ArrayList<Integer>> buffer = new ArrayList<ArrayList<Integer>>(noOfVertices);

        for (int i = 0; i < noOfVertices; i++)
        {
            buffer.add(new ArrayList<Integer>());
        }

        for (int i = 0; i < noOfEdges; i++)
        {
            int[] edge = readNextLine(in);

            int from = edge[0];
            int to = edge[1];

            buffer.get(from).add(to);

            if (simplified)
            {
                buffer.get(to).add(from);
            }
        }


        edges = new int[noOfVertices][];

        for (int i = 0; i < noOfVertices; i++)
        {
            edges[i] = new int[buffer.get(i).size()];

            for (int j = 0; j < edges[i].length; j++)
            {
                edges[i][j] = buffer.get(i).get(j);
            }
        }

    }

    private void readMatrix(BufferedReader in)
    {
        noOfVertices = readNextLine(in)[0];
        edges = new int[noOfVertices][];

        ArrayList<Integer> buffer = new ArrayList<Integer>();

        for (int i = 0; i < noOfVertices; i++)
        {
            int[] row = readNextLine(in);
            buffer.clear();

            for (int j = 0; j < noOfVertices; j++)
            {
                if (row[j] == 1)
                {
                    buffer.add(j);
                }
            }

            edges[i] = new int[buffer.size()];

            for (int j = 0; j < edges[i].length; j++)
            {
                edges[i][j] = buffer.get(j);
            }
        }
    }

    // ---------------------

    public int[][] dfs(int startId)
    {
        // Output data
        int[] parIds = new int[noOfVertices];

        int[] preOrder = new int[noOfVertices];
        int[] postOrder = new int[noOfVertices];

        int preCount = 0;
        int postCount = 0;


        // Helpers to compute DFS
        int[] neighIndex = new int[noOfVertices];
        int[] stack = new int[noOfVertices];
        int stackSize = 0;

        boolean[] visited = new boolean[noOfVertices];

        for (int vId = 0; vId < noOfVertices; vId++)
        {
            parIds[vId] = -1;

            preOrder[vId] = -1;
            postOrder[vId] = -1;

            visited[vId] = false;
            neighIndex[vId] = 0;
        }

        // Push
        stack[stackSize] = startId;
        stackSize++;

        while (stackSize > 0)
        {
            int vId = stack[stackSize - 1];
            int nInd = neighIndex[vId];

            if (nInd == 0)
            {
                visited[vId] = true;

                // *** Pre-order for vId ***
                preOrder[preCount] = vId;
                preCount++;
            }

            if (nInd < edges[vId].length)
            {
                int neighId = edges[vId][nInd];

                if (!visited[neighId])
                {
                    // Push
                    stack[stackSize] = neighId;
                    stackSize++;

                    parIds[neighId] = vId;
                }

                neighIndex[vId]++;
            }
            else
            {
                // All neighbours checked, backtrack.
                stackSize--; // Pop;

                // *** Post-order for vId ***
                postOrder[postCount] =  vId;
                postCount++;
            }
        }

        return new int[][]
        {
            parIds,
            preOrder,
            postOrder
        };
    }

    // ---------------------

    public int[][] bfs(int startId)
    {
        // Output data
        // 0: distances from start vertex
        // 1: BFS-order
        // 2: parent-IDs
        int[][] bfsResult = new int[3][noOfVertices];

        int[] distances = bfsResult[0];
        int[] q = bfsResult[1];
        int[] parents = bfsResult[2];

        for (int i = 0; i < noOfVertices; i++)
        {
            distances[i] = Integer.MAX_VALUE;
            q[i] = -1;
            parents[i] = -1;
        }


        // Set start vertex
        distances[startId] = 0;
        q[0] = startId;
        int qSize = 1;

        for (int qInd = 0; qInd < qSize; qInd++)
        {
            int vInd = q[qInd];
            int nDis = distances[vInd] + 1;

            for (int nInd = 0; nInd < edges[vInd].length; nInd++)
            {
                int uInd = edges[vInd][nInd];

                if (nDis < distances[uInd])
                {
                    distances[uInd] = nDis;
                    parents[uInd] = vInd;

                    q[qSize] = uInd;
                    qSize++;
                }
            }
        }

        return bfsResult;
    }
}