import model.ProblemInstance;
import waoa.WaOA;
import waoa.Walrus;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        String filePath = "../problem/tai20_20.txt"; 
        List<ProblemInstance> instances = ProblemInstance.loadInstanceFromFile(filePath);
        
        if (instances.isEmpty()) {
            System.out.println("Tidak ada testcase yang berhasil di-load.");
            return;
        }

        ProblemInstance testCase = instances.get(0);
        System.out.println("Menyelesaikan Taillard Benchmark (" + testCase.getNumJobs() + " Jobs, " + testCase.getNumMachines() + " Machines)...");

        int populationSize = 50;
        int maxIterations = 100;

        WaOA solver = new WaOA(testCase, populationSize, maxIterations);
        List<Walrus> paretoFront = solver.solve();

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
    }
}