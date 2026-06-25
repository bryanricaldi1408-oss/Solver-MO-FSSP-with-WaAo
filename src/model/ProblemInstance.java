package model;

import java.io.*;
import java.util.*;

public class ProblemInstance{
    private final int numJobs;
    private final int numMachines;
    private final int[][] processingTimes;
    private int lowerBound;
    private int upperBound;

    public ProblemInstance(int numJobs, int numMachines, int[][] processingTimes){
        this.numJobs = numJobs;
        this.numMachines = numMachines;
        this.processingTimes = processingTimes;
    }

    public static List<ProblemInstance> loadInstanceFromFile(String filePath){
        System.out.println("Memuat file benchmark Taillard: " + filePath);
        List<ProblemInstance> instances = new ArrayList<>();

        try (Scanner sc = new Scanner(new File(filePath))) {
            while (sc.hasNext()) {
                String token = sc.next();
                if (token.equals("number")) {
                    sc.nextLine(); // consume the rest of the text "of jobs, number of machines..."
                    
                    int numJobs = sc.nextInt();
                    int numMachines = sc.nextInt();
                    int seed = sc.nextInt();
                    int upperBound = sc.nextInt();
                    int lowerBound = sc.nextInt();
                    
                    sc.next(); // "processing"
                    sc.next(); // "times"
                    sc.next(); // ":"
                    
                    // Taillard format: numMachines rows, numJobs columns
                    int[][] processingTimes = new int[numJobs][numMachines];
                    for (int m = 0; m < numMachines; m++) {
                        for (int j = 0; j < numJobs; j++) {
                            processingTimes[j][m] = sc.nextInt();
                        }
                    }
                     
                    ProblemInstance instance = new ProblemInstance(numJobs, numMachines, processingTimes);
                    instance.lowerBound = lowerBound;
                    instance.upperBound = upperBound;
                    instances.add(instance);
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("Gagal membaca file. Pastikan lokasi file benar: " + filePath);
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Terjadi kesalahan saat parsing file format Taillard: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println(instances.size());
        return instances;
    }
         
    public int getNumJobs() { return numJobs; }
    public int getNumMachines() { return numMachines; }
    public int[][] getProcessingTimes() { return processingTimes; }
    public int getUpperBound() {return upperBound;}
    public int getLowerBound() {return lowerBound;}

}
