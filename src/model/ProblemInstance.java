package model;

import java.util.*;
import java.io.*;

public class ProblemInstance{
    private final int numJobs;
    private final int numMachines;
    private final int[][] processingTimes;
    private final int[] dueDates;

    public ProblemInstance(int numJobs, int numMachines, int[][] processingTimes, int[] dueDates){
        this.numJobs = numJobs;
        this.numMachines = numMachines;
        this.processingTimes = processingTimes;
        this.dueDates = dueDates;
    }

    public static List<ProblemInstance> loadInstanceFromFile(String filePath){
        List<ProblemInstance> instances = new ArrayList<>();

        try (Scanner sc = new Scanner(new File(filePath))) {
            if (!sc.hasNextInt()) {
                return instances; // Mengembalikan list kosong jika file kosong
            }
            
            // Baris pertama: Total seluruh test case dalam file
            int totalTestCases = sc.nextInt();
            
            for (int t = 0; t < totalTestCases; t++) {
                // 1. Baca jumlah job dan mesin
                int numJobs = sc.nextInt();
                int numMachines = sc.nextInt();
                
                // 2. Baca matriks 2D untuk Processing Time (m x n)
                int[][] processingTimes = new int[numJobs][numMachines];
                for (int i = 0; i < numJobs; i++) {
                    for (int j = 0; j < numMachines; j++) {
                        processingTimes[i][j] = sc.nextInt();
                    }
                }
                
                // 3. Baca array 1D untuk Due Dates (m elemen)
                int[] dueDates = new int[numJobs];
                for (int i = 0; i < numJobs; i++) {
                    dueDates[i] = sc.nextInt();
                }
                
                // 4. Bungkus ke dalam objek ProblemInstance dan masukkan ke list
                ProblemInstance instance = new ProblemInstance(numJobs, numMachines, processingTimes, dueDates);
                instances.add(instance);
            }
            
        } catch (FileNotFoundException e) {
            System.err.println("Gagal membaca file. Pastikan lokasi file benar: " + filePath);
            e.printStackTrace();
        }
        
        return instances;
    }
         
}
