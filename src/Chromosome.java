import java.util.ArrayList;

public class Chromosome {
    private StringBuilder genes;
    private StringBuilder chromosome;
    private int fitness;

    private int supervisorId;

    private int capacity;

    private String supervisorName;

    private int chromosomeLength;

    private int noOfSupervisors;

    public Chromosome(String supervisorName, int capacity, int chromosomeLength, int noOfSupervisors) {
        this.capacity = capacity;
        this.chromosomeLength = chromosomeLength;
        this.supervisorId = extractId(supervisorName);
        this.chromosome = new StringBuilder(chromosomeLength);
        this.genes = generateGenes(noOfSupervisors);
        this.noOfSupervisors = noOfSupervisors;
    }


    public Chromosome(String supervisorName, StringBuilder chromosome, int capacity, int noOfSupervisors) {
        this.supervisorName = supervisorName;
        this.capacity = capacity;
        this.chromosomeLength = chromosome.length();
        this.supervisorId = extractId(supervisorName);
        this.chromosome = chromosome;
        this.genes = generateGenes(noOfSupervisors);
        this.noOfSupervisors = noOfSupervisors;
    }

    public StringBuilder generateGenes(int noOfSupervisors) {
        StringBuilder temp = new StringBuilder();
        for(int i = 1; i < noOfSupervisors; i++) {
            temp.append(i-1);
        }
        return temp;
    }


    private int extractId(String supervisorName) {
        String[] parts = supervisorName.split("_");
        return Integer.parseInt(parts[1]);
    }

    public void calculateFitness(ArrayList<Student> students) {


    }


    public StringBuilder getChromosome() {
        return chromosome;
    }


    // generate the chromosome by randomly appending genes to it
    public void generateRandomChromosome() {
        for(int i = 0; i < chromosomeLength; i++) {
            int randIndex = (int) (Math.random() * genes.length());
            chromosome.append(genes.charAt(randIndex));
        }
    }


    // create a new chromosome from combining parts of two different chromosomes, split position of the chromosomes is the crossover point
    public static Chromosome crossover(Chromosome ch1, Chromosome ch2, int crossoverPoint) {
        String temp = ch1.getChromosome().substring(0, crossoverPoint);
        String temp2 = ch2.getChromosome().substring(crossoverPoint, ch2.getChromosome().length());
        return new Chromosome(new String(ch1.supervisorName), new StringBuilder(temp + temp2), ch1.capacity, ch1.noOfSupervisors);
    }



    public static Chromosome mutate(Chromosome ch1, StringBuilder genes) {
        // select a random mutation point
        int mutationPoint = (int) (Math.random() * ch1.getChromosome().length());
        // remove the gene at mutation point from the list of possible genes (to avoid mutating the gene into the same gene)
        String toRemove = ch1.getChromosome().substring(mutationPoint, mutationPoint+1);
        StringBuilder localGenes = new StringBuilder(genes.toString());
        localGenes.deleteCharAt(localGenes.indexOf(toRemove));

        // select a random gene from the rest of possible genes
        int randIndex = (int) (Math.random() * genes.length()-1);

        // replace the gene a mutation point with a random gene and return the newly created chromosome
        StringBuilder newChromosome = new StringBuilder(ch1.toString());
        newChromosome.setCharAt(mutationPoint, localGenes.charAt(randIndex));
        return new Chromosome(new String(ch1.supervisorName), newChromosome, ch1.capacity, ch1.noOfSupervisors);
    }

    public int getFitness() {
        return fitness;
    }

    public StringBuilder getGenes(){
        return genes;
    }

}
