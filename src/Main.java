import java.util.Collections;
import java.util.List;
import model.ProblemInstance;
import waoa.WaOA;
import waoa.Walrus;

public class Main {
    public static void main(String[] args) {
        String filePath = "../problem/tai20_20.txt"; 
        if (args.length>0) filePath = "../problem/"+args[0];
        List<ProblemInstance> instances = ProblemInstance.loadInstanceFromFile(filePath);
        
        if (instances.isEmpty()) {
            System.out.println("Tidak ada testcase yang berhasil di-load.");
            return;
        }
        for (ProblemInstance testCase:instances){
            
            System.out.println("Menyelesaikan Taillard Benchmark (" + testCase.getNumJobs() + " Jobs, " + testCase.getNumMachines() + " Machines)... known makespan lower bound: " +testCase.getLowerBound()+" known upper bound: "+ testCase.getUpperBound() );
            
            int populationSize = 50;
            int maxIterations = 100;
            
            WaOA solver = new WaOA(testCase, populationSize, maxIterations);
            List<Walrus> paretoFront = solver.solve();
            Collections.sort(paretoFront);
            
            System.out.println("\n=== HASIL PARETO OPTIMAL FRONT ===");
            
            System.out.printf("%-12s | %-15s | %s\n", "Makespan", "Total Flow Time", "Urutan Kerja (Job Sequence)");
            System.out.println("-------------------------------------------------------------------------");
            for (Walrus walrus : paretoFront) {
                System.out.printf("%-12.1f | %-15.1f | ", walrus.objectives[0], walrus.objectives[1]);
                for (int job : walrus.jobSequence) {
                    System.out.print(job + " ");
                }
                System.out.println();
            }
            System.out.println();
        }
        System.out.println("order given by Rajendran comparison");
    }
}