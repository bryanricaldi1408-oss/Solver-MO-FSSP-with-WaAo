package waoa;

import model.ProblemInstance;
import java.util.List;
import java.util.Random;

public class WaOA {
    private final ProblemInstance problem;
    private final int popSize;
    private final int maxIterations;
    private final Random rand;
    
    private final double LB = -10.0;
    private final double UB = 10.0;

    public WaOA(ProblemInstance problem, int popSize, int maxIterations) {
        this.problem = problem;
        this.popSize = popSize;
        this.maxIterations = maxIterations;
        this.rand = new Random();
    }

    public List<Walrus> solve() {
        int numJobs = problem.getNumJobs();
        Population population = new Population(popSize, numJobs);
        population.initializeRandomly(rand, LB, UB);
        
        ParetoArchive archive = new ParetoArchive(100);

        // Evaluasi objektif awal untuk seluruh populasi
        for (Walrus walrus : population.getMembers()) {
            walrus.objectives = evaluateObjectives(walrus.jobSequence);
            archive.update(walrus);
        }

        // Loop Iterasi Utama WaOA
        for (int t = 1; t <= maxIterations; t++) {
            List<Walrus> members = population.getMembers();

            for (int i = 0; i < popSize; i++) {
                Walrus current = members.get(i);

                // -------------------------------------------------------------
                // PHASE 1: Feeding Strategy (Exploration)
                // -------------------------------------------------------------
                List<Walrus> leaders = archive.getArchive();
                Walrus sw = leaders.get(rand.nextInt(leaders.size())); // Strongest Walrus dari arsip

                Walrus p1 = new Walrus(numJobs);
                for (int j = 0; j < numJobs; j++) {
                    int I = rand.nextInt(2) + 1;
                    p1.position[j] = current.position[j] + rand.nextDouble() * (sw.position[j] - I * current.position[j]);
                }
                p1.jobSequence = population.convertPositionToSequence(p1.position);
                p1.objectives = evaluateObjectives(p1.jobSequence);

                if (!current.dominates(p1)) {
                    members.set(i, p1);
                    current = p1;
                    archive.update(p1);
                }

                // -------------------------------------------------------------
                // PHASE 2: Migration
                // -------------------------------------------------------------
                int k;
                do { k = rand.nextInt(popSize); } while (k == i);
                Walrus target = members.get(k);

                Walrus p2 = new Walrus(numJobs);
                boolean targetDominates = target.dominates(current);

                for (int j = 0; j < numJobs; j++) {
                    if (targetDominates) {
                        int I = rand.nextInt(2) + 1;
                        p2.position[j] = current.position[j] + rand.nextDouble() * (target.position[j] - I * current.position[j]);
                    } else {
                        p2.position[j] = current.position[j] + rand.nextDouble() * (current.position[j] - target.position[j]);
                    }
                }
                p2.jobSequence = population.convertPositionToSequence(p2.position);
                p2.objectives = evaluateObjectives(p2.jobSequence);

                if (!current.dominates(p2)) {
                    members.set(i, p2);
                    current = p2;
                    archive.update(p2);
                }

                // -------------------------------------------------------------
                // PHASE 3: Escaping and Fighting Against Predators (Exploitation)
                // -------------------------------------------------------------
                Walrus p3 = new Walrus(numJobs);
                double lbLocal = LB / t; 
                double ubLocal = UB / t;

                for (int j = 0; j < numJobs; j++) {
                    p3.position[j] = current.position[j] + (lbLocal + (ubLocal - rand.nextDouble() * lbLocal));
                }
                p3.jobSequence = population.convertPositionToSequence(p3.position);
                p3.objectives = evaluateObjectives(p3.jobSequence);

                if (!current.dominates(p3)) {
                    members.set(i, p3);
                    archive.update(p3);
                }
            }
        }
        return archive.getArchive();
    }

    private double[] evaluateObjectives(int[] sequence) {
        int[][] pTime = problem.getProcessingTimes();
        int n = problem.getNumJobs();
        int m = problem.getNumMachines();
        int[][] C = new int[n][m];

        C[0][0] = pTime[sequence[0]][0];
        for (int j = 1; j < m; j++) {
            C[0][j] = C[0][j-1] + pTime[sequence[0]][j];
        }

        for (int i = 1; i < n; i++) {
            C[i][0] = C[i-1][0] + pTime[sequence[i]][0];
            for (int j = 1; j < m; j++) {
                C[i][j] = Math.max(C[i-1][j], C[i][j-1]) + pTime[sequence[i]][j];
            }
        }

        double makespan = C[n-1][m-1];
        double totalFlowTime = 0;
        for (int i = 0; i < n; i++) {
            totalFlowTime += C[i][m-1];
        }

        return new double[]{makespan, totalFlowTime};
    }
}