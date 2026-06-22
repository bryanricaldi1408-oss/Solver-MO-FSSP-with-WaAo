package waoa;

public class Walrus {

    public double[] position;
    public double[] objectives;
    public int[] jobSequence;

    public Walrus(int numJobs){
        this.position = new double[numJobs];
        this.objectives = new double[2];
        this.jobSequence = new int[numJobs];
    }
    
    // Cek objective mana yang lebih dominan antara makespan dan total flow time
    public boolean dominates(Walrus other){
        boolean atLeastOneBetter = false;
        for(int i=0; i<this.objectives.length; i++){
            if(this.objectives[i] > other.objectives[i]) return false;
            if(this.objectives[i] < other.objectives[i]) atLeastOneBetter = true;
        }
        return atLeastOneBetter;
    }

    public Walrus cloneWalrus() {
        Walrus copy = new Walrus(this.position.length);
        System.arraycopy(this.position, 0, copy.position, 0, this.position.length);
        System.arraycopy(this.jobSequence, 0, copy.jobSequence, 0, this.jobSequence.length);
        System.arraycopy(this.objectives, 0, copy.objectives, 0, copy.objectives.length);
        return copy;
    }
    
}