import java.util.ArrayList;

public class Chromosome {
    private StringBuilder genes = new StringBuilder("01");
    private StringBuilder chromosomeStringBuilder;
    private int fitness;

    private int supervisorId;

    private int capacity;

    private String supervisorName;

    private int chromosomeLength;
    public Chromosome (int chromosomeLength) {
        chromosomeStringBuilder = new StringBuilder(chromosomeLength);
        this.chromosomeLength = chromosomeLength;
    }

    public Chromosome(String supervisorName, int capacity, int chromosomeLength) {
        this.supervisorName = supervisorName;
        this.capacity = capacity;
        this.chromosomeLength = chromosomeLength;
        this.supervisorId = extractId(supervisorName);
        chromosomeStringBuilder = new StringBuilder(chromosomeLength);

    }
    public Chromosome (StringBuilder chromosome) {
        this.chromosomeStringBuilder = chromosome;
        this.chromosomeLength = chromosome.length();
    }

    private int extractId(String supervisorName) {
        String[] parts = supervisorName.split("_");
        return Integer.parseInt(parts[1]);
    }

    // fitness is the sum of all characters that match the solution when compared character by character
    public void calculateFitness(ArrayList<Student> students) {
        fitness = 0;
        int studentsConsidered = 0;
        for(int i = 0; i < chromosomeLength; i++) {
            // a gene is a 1 which means that the student under the index of that gene is considered in this mapping



            // and if this is the only chromosome with a 1 at that position then add upp fitness
            // student can only be assigned to one lecturer

            if(chromosomeStringBuilder.charAt(i) == '1') {
                // get the student and his preference list
                Student student = students.get(i);
                int[] preferenceList = student.getPreferenceList();
                // sum up the preferences in this list to get the fitness and update the students considered counter for each student
                for(int j = 0; i < preferenceList.length; i++) {
                    fitness += preferenceList[j];
                }
                studentsConsidered++;
            }
        }
        // if this solution involves more students than the capacity of this lecturer kill this chromosome and replace it with a new random one
        if(studentsConsidered > capacity) {
            chromosomeStringBuilder = replaceChromosome();
        }

    }

    private StringBuilder replaceChromosome() {
        StringBuilder temp = new StringBuilder();
        for(int i = 0; i < chromosomeLength; i++) {
            int randIndex = (int) (Math.random() * genes.length());
            temp.append(genes.charAt(randIndex));
        }
        return temp;
    }

    public StringBuilder getChromosomeStringBuilder() {
        return chromosomeStringBuilder;
    }


    // generate the chromosome by randomly appending genes to it
    public void generateRandomChromosome() {
        for(int i = 0; i < chromosomeLength; i++) {
            int randIndex = (int) (Math.random() * genes.length());
            chromosomeStringBuilder.append(genes.charAt(randIndex));
        }
    }


    // create a new chromosome from combining parts of two different chromosomes, split position of the chromosomes is the crossover point
    public static Chromosome crossover(StringBuilder ch1, StringBuilder ch2, int crossoverPoint) {
        String temp1 = ch1.substring(0, crossoverPoint);
        String temp2 = ch2.substring(crossoverPoint, ch2.length());
        return new Chromosome( new StringBuilder(temp1 + temp2));
    }



    public static Chromosome mutate(StringBuilder ch1, StringBuilder genes) {
        // select a random mutation point
        int mutationPoint = (int) (Math.random() * ch1.length());
        // remove the gene at mutation point from the list of possible genes (to avoid mutating the gene into the same gene)
        String toRemove = ch1.substring(mutationPoint, mutationPoint+1);
        StringBuilder localGenes = new StringBuilder(genes.toString());
        localGenes.deleteCharAt(localGenes.indexOf(toRemove));

        // select a random gene from the rest of possible genes
        int randIndex = (int) (Math.random() * genes.length()-1);

        // replace the gene a mutation point with a random gene and return the newly created chromosome
        StringBuilder newChromosome = new StringBuilder(ch1.toString());
        newChromosome.setCharAt(mutationPoint, localGenes.charAt(randIndex));
        return new Chromosome(newChromosome);
    }

    public int getFitness() {
        return fitness;
    }

    public StringBuilder getGenes(){
        return genes;
    }

}
