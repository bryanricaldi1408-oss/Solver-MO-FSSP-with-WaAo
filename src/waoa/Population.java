package waoa;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Population {
    private final List<Walrus> members;
    private final int populationSize;
    private final int numJobs;

    public Population(int populationSize, int numJobs) {
        this.populationSize = populationSize;
        this.numJobs = numJobs;
        this.members = new ArrayList<>(populationSize);
    }

    public void initializeRandomly(Random rand, double lowerBound, double upperBound) {
        this.members.clear();
        for (int i = 0; i < populationSize; i++) {
            Walrus walrus = new Walrus(numJobs);
            for (int j = 0; j < numJobs; j++) {
                walrus.position[j] = lowerBound + (upperBound - lowerBound) * rand.nextDouble();
            }
            walrus.jobSequence = convertPositionToSequence(walrus.position);
            this.members.add(walrus);
        }
    }

    public int[] convertPositionToSequence(double[] position) {
        int[] sequence = new int[numJobs];
        Integer[] indices = new Integer[numJobs];
        for (int i = 0; i < numJobs; i++) indices[i] = i;

        // Mengurutkan indeks berdasarkan nilai posisi terbesar (LPV)
        Arrays.sort(indices, (a, b) -> Double.compare(position[b], position[a]));

        for (int i = 0; i < numJobs; i++) {
            sequence[i] = indices[i];
        }
        return sequence;
    }

    public List<Walrus> getMembers() { return members; }
}