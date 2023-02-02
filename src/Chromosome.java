public class Chromosome {
    private StringBuilder genes = new StringBuilder("01");
    private StringBuilder chromosomeStringBuilder;
    private int fitness;

    public static final String solution = "111111111111111111111111111111";

    private int chromosomeLength;
    public Chromosome (int chromosomeLength) {
        chromosomeStringBuilder = new StringBuilder(chromosomeLength);
        this.chromosomeLength = chromosomeLength;
    }

    public Chromosome (StringBuilder chromosome) {
        this.chromosomeStringBuilder = chromosome;
        this.chromosomeLength = chromosome.length();
    }


    // fitness is the sum of all 1's in the chromosome
    public void calculateFitness() {
        fitness = 0;
        for(int i = 0; i < chromosomeStringBuilder.length(); i++) {
            if(chromosomeStringBuilder.charAt(i) == '1') {
                fitness++;
            }
        }
        // if there are no 1's in the solution then set the fitness to be 2 * solution length
        if(fitness == 0) {
            fitness = 2 * chromosomeLength;
        }

        this.fitness = fitness;
    }

    public StringBuilder getChromosomeStringBuilder() {
        return chromosomeStringBuilder;
    }


    // generate the chromosome by randomly appending genes to it
    public void generateRandomChromosome() {
        for(int i = 0; i < chromosomeStringBuilder.capacity(); i++) {
            int randIndex = (int) (Math.random() * genes.length());
            chromosomeStringBuilder.append(genes.charAt(randIndex));
        }
    }


    // create a new chromosome from combining parts of two different chromosomes, split position of the chromosomes is the crossover point
    public static Chromosome crossover(StringBuilder ch1, StringBuilder ch2, int crossoverPoint) {
        String temp = ch1.substring(0, crossoverPoint);
        String temp2 = ch2.substring(crossoverPoint, ch2.length());
        return new Chromosome(new StringBuilder(temp+temp2));
    }


    /*public static Chromosome mutate() {
        int mutationPoint = (int) (Math.random() * chromosomeLength-1);
        String toRemove = chromosome.substring(mutationPoint, mutationPoint+1);
        StringBuilder localGenes = new StringBuilder(genes);
        localGenes.deleteCharAt(localGenes.indexOf(toRemove));
        int randIndex = (int) (Math.random() * localGenes.length());
        chromosome.setCharAt(mutationPoint, localGenes.charAt(randIndex));
        return this;
    } */

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
