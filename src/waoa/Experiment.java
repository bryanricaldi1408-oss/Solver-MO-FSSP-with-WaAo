package waoa;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import model.ProblemInstance;
import java.io.PrintStream;
import java.io.FileNotFoundException;

public class Experiment {
    public static void main(String[] args) {
        try {
            PrintStream fileStream = new PrintStream("hasilExperimen.txt");
            System.setOut(fileStream);
        } catch (Exception e) {
        }
        int machines[] = {5,10,20};
        int jobs[] = {50,100};
        int[] populationSizes = {25,50,100};
        int[] maxIterations = {50, 100, 150};
        int topPop=0,topIter=0;
        int[][] bestKaisuu = new int[populationSizes.length][maxIterations.length];
        for(int machine:machines){
            for(int jobNum:jobs){

                String filePath = "problem/tai"+jobNum+"_"+machine+".txt";
                List<ProblemInstance> instances = ProblemInstance.loadInstanceFromFile(filePath);
                
                if (instances.isEmpty()) {
                    System.out.println("Tidak ada testcase yang berhasil di-load.");
                    return;
                }
                for (ProblemInstance testCase:instances){
                    
                    System.out.println("Menyelesaikan Taillard Benchmark (" + testCase.getNumJobs() + " Jobs, " + testCase.getNumMachines() + " Machines)... known makespan lower bound: " +testCase.getLowerBound()+" known upper bound: "+ testCase.getUpperBound() );
                    
                    Walrus[][] bestWalrusBYParam = new Walrus[populationSizes.length][maxIterations.length];
                    for (int i=0;i<populationSizes.length;i++){
                        for(int j=0;j<maxIterations.length;j++){
                            
                            WaOA solver = new WaOA(testCase, populationSizes[i], maxIterations[j]);
                            List<Walrus> paretoFront = solver.solve();
                            Collections.sort(paretoFront);
                            bestWalrusBYParam[i][j] = paretoFront.get(0);
                        }
                    }
                    Walrus best = bestWalrusBYParam[0][0];
                    int bestPop=0,bestIter=0;
                    System.out.println("\n=== HASIL Optimal ===");
                    System.out.printf("%-15s | %-15s | %-12s | %-15s | %s\n", "populationSize", "MaxIterations", "Makespan", "Total Flow Time", "Urutan Kerja (Job Sequence)");
                    System.out.println("-------------------------------------------------------------------------");
                    for (int i=0;i<populationSizes.length;i++){
                        for(int j=0;j<maxIterations.length;j++){
                            Walrus walrus = bestWalrusBYParam[i][j];
                            System.out.printf("%-15d | %-15d | %-12.1f | %-15.1f | ", populationSizes[i], maxIterations[j], walrus.objectives[0], walrus.objectives[1]);
                            for (int job : walrus.jobSequence) {
                                System.out.print(job + " ");
                            }
                            if (walrus.compareTo(best)<0){
                                best = walrus;
                                bestPop = i;
                                bestIter= j;
                            }
                            System.out.println();
                        }
                    }
                    System.out.printf("best found on poopulation size %d  and max iteration %d\n", populationSizes[bestPop], maxIterations[bestIter]);
                    
                    if (++bestKaisuu[bestPop][bestIter]>bestKaisuu[topPop][topIter]){
                        topPop = bestPop;
                        topIter = bestIter;
                    }
                }
            }
        }
        System.out.printf("over all test cases best seems to be at population size %d and max iteration %d\n", populationSizes[topPop], maxIterations[topIter]);
    }
}