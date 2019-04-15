import java.io.*;
import java.util.*;

public class Lab3
{

    /**
     *  Problem: Finds all the vertices u such that all shortest paths from s to t pass through u.
     */
    private static int[] problem(Graph g, int sId, int tId) {
        // store bfsId results
        int[][] bfsid = g.bfs(sId);
        int[] sidistance = bfsid[0]; // stores distance of each node from root
        
        int[][] bftid = g.bfs(tId); 
        int[] tidistance = bftid[0]; // stores distance of each node from destination
        
        // data for while loop
        int V = tId;
        
        // mark required vertices
        boolean[] includeNode = new boolean[g.noOfVertices];

        // stores info on vertices in the interval
        HashMap<Integer, Integer> interval = new HashMap<Integer, Integer>();

        int sPathLen = sidistance[tId];
        int solutionLen = 0;
        // this is the layering part
        for (int i = 0; i < g.noOfVertices; i++) {
            int sdist = sidistance[i];
            int dist = sdist + tidistance[i];
            
            if (dist == sPathLen) {
                // if there is another node in the same layer, we don't want to include it
                if (interval.containsKey(sdist)) {
                    // make sure we don't decrement if we already have for this vertex
                    if (includeNode[interval.get(sdist)]) 
                        solutionLen--;

                    includeNode[interval.get(sdist)] = false;
                } else {
                    interval.put(sdist, i);
                    includeNode[i] = true;
                    solutionLen++;
                }
            }
        }

        int outIndex = 0;
        int[] solution = new int[solutionLen];

        // add must-pass nodes to the solution
        for (int i = 0; i < g.noOfVertices; i++) {
            if (includeNode[i]) {
                solution[outIndex] = i;
                outIndex++;
            }
        }
        return solution;
    }

    // ---------------------------------------------------------------------
    // Do not change any of the code below!

    private static final int LabNo = 3;

    private static boolean testProblem(BufferedReader in)
    {
        Graph g = new Graph(in, Graph.Representation.EdgeListSimplified);

        int[] uvIds = readNextLine(in);

        int[] answer = problem(g, uvIds[0], uvIds[1]);
        int[] solution = readNextLine(in);

        Arrays.sort(answer);
        Arrays.sort(solution);

        if (answer.length != solution.length) return false;

        for (int i = 0; i < answer.length; i++)
        {
            if (answer[i] != solution[i]) return false;
        }

        return true;
    }

    public static void main(String args[])
    {
        System.out.println("CS 428 -- Spring 2018 -- Lab " + LabNo);

        BufferedReader in;

        try
        {
            // Path for windows: L3Maven\\Lab3\\src\\testsLab3.txt
            // Path for Mac: L3Maven/Lab3/src/testsLab3.txt
            // CHANGE FILEREADER BACK TO: new FileReader("testsLab"+ LabNo + ".txt")
            // AFTER FINISHING
            in = new BufferedReader(new FileReader("testsLab"+ LabNo + ".txt"));
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return;
        }

        int noOfTests = 0;

        try
        {
            noOfTests = Integer.parseInt(in.readLine());
        }
        catch (IOException e)
        {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return;
        }

        System.out.println("-- -- -- -- --");
        System.out.println(noOfTests + " test cases.");

        boolean passedAll = true;

        for (int i = 1; i <= noOfTests; i++)
        {
            readNextLine(in);

            boolean passed = false;
            boolean exce = false;

            try
            {
                passed = testProblem(in);
            }
            catch (Exception ex)
            {
                passed = false;
                exce = true;
            }

            if (!passed)
            {
                System.out.println("Test " + i + " failed!" + (exce ? " (Exception)" : ""));
                passedAll = false;
            }
        }

        if (passedAll)
        {
            System.out.println("All test passed.");
        }

    }

    private static int[] readNextLine(BufferedReader in)
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

        if(line == null || line.isEmpty()) return null;

        String[] strNumbers = line.split("\\s");

        int[] numbers = new int[strNumbers.length];

        for (int i = 0; i < numbers.length; i++)
        {
            numbers[i] = Integer.parseInt(strNumbers[i]);
        }

        return numbers;
    }

}
