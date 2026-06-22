package waoa;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ParetoArchive {
    private final List<Walrus> archive;
    private final int maxSize;


    public ParetoArchive(int maxSize) {
        this.archive = new ArrayList<>();
        this.maxSize = maxSize;
    }

    public synchronized void update(Walrus candidate) {
        Walrus copy = candidate.cloneWalrus();
        List<Walrus> toRemove = new ArrayList<>();

        for (Walrus archived : archive) {
            boolean isSequenceDuplicate = Arrays.equals(archived.jobSequence, copy.jobSequence);
            if (isSequenceDuplicate) {
                return; 
            }
            if (archived.dominates(copy)) {
                return; // Jika kandidat didominasi oleh isi arsip, tolak
            }
            if (copy.dominates(archived)) {
                toRemove.add(archived); // Jika kandidat mendominasi isi arsip, tandai hapus
            }
        }

        archive.removeAll(toRemove);
        archive.add(copy);

        if (archive.size() > maxSize) {
            archive.remove(archive.size() - 1);
        }
    }

    public synchronized List<Walrus> getArchive() { 
        return new ArrayList<>(archive); 
    }
}