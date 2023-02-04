import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

public class Chromosome {
    private int[] genes;
    private int[] chromosome;
    private int[] capacities;
    private int fitness;
    private int chromosomeLength;

    private int noOfSupervisors;

    public Chromosome(int[] chromosome, int[] capacities, int noOfSupervisors) {
        this.capacities = capacities;
        this.chromosomeLength = chromosome.length;
        this.chromosome = chromosome;
        this.genes = generateGenes(noOfSupervisors);
        this.noOfSupervisors = noOfSupervisors;
    }

    public Chromosome(int chromosomeLength, int noOfSupervisors) {
        this.chromosomeLength = chromosomeLength;
        this.chromosome = new int[chromosomeLength];
        this.genes = generateGenes(noOfSupervisors);
        this.noOfSupervisors = noOfSupervisors;
    }

    public void setCapacities(int[] capacities) {
        this.capacities = capacities;
    }

    // the genes are the all supervisor ID in range (1 to noOfSupervisors)
    public int[] generateGenes(int noOfSupervisors) {
        int[] temp = new int[noOfSupervisors];
        for(int i = 0; i < noOfSupervisors; i++) {
            temp[i] = i+1;
        }
        return temp;
    }


   public void calculateFitness(ArrayList<Student> students) {
        // reset the fitness at each method invocation
        fitness = 0;
        // create a reference array which holds the counts of students choosing to work with a particular supervisor
        int[] chromosomeCapacities = new int[capacities.length];

        for(Student student : students) {
            // get the preference list of the student currently considered
            int [] preferences = student.getPreferenceList();
            for (int studentIndex = 0; studentIndex < chromosome.length; studentIndex++) {
                // if the lecturer is equal
                if (chromosome[studentIndex] == student.getStudentId()) {
                    // check to ensure index out of bounds exception does not occur
                    // increase the fitness by the preference that the student has for the supervisor found in the chromosome array under index studentIndex
                    // make sure to adjust indexes properly (student and supervisors IDs are in range (1 to list.size()) and reference arrays are int range (0 to list.size()-1)
                    if(studentIndex == 0) {
                        fitness += preferences[chromosome[studentIndex] -1];
                    } else {
                        fitness += preferences[chromosome[studentIndex - 1] - 1];
                    }
                    // increase the capacity count by 1 for the supervisor found in the chromosome array under index studentIndex
                    chromosomeCapacities[chromosome[studentIndex]-1]++;
                }
            }

        }

       // check for exceeded capacities and penalise the chromosome by adding 30 to the fitness for each supervisor capacity exceeded
       for(int j = 0; j < capacities.length; j++) {
           if(chromosomeCapacities[j] > capacities[j]) {
               fitness += 30;
           }
       }
    }

    public int[] getChromosome() {
        return chromosome;
    }

    public int[] getCapacities() {
        return capacities;
    }


    // generate the chromosome by randomly appending genes to it
    public void generateRandomChromosome() {
        for(int i = 0; i < chromosomeLength; i++) {
            int randIndex = new Random().nextInt(genes.length);
            chromosome[i] = genes[randIndex];
        }
    }


    // create a new chromosome from combining parts of two different chromosomes first part being all values less
    // than crossover point and second part being all values greater or equal than to the crossover point
    public static Chromosome crossover(Chromosome ch1, Chromosome ch2, int crossoverPoint) {
        int[] newChromosome = new int[ch1.chromosomeLength];
        for(int i = 0; i < ch1.chromosomeLength; i++) {
            if(i  < crossoverPoint) {
                newChromosome[i] = ch1.getChromosome()[i];  // int array
            } else {
                newChromosome[i] = ch2.getChromosome()[i];
            }
        }
        return new Chromosome( newChromosome, ch1.capacities, ch1.noOfSupervisors);
    }


    // create a new chromosome by mutation by copying the chromosome gene by gene and changing the gene[i] with a random gene if mutation has occurred
    public static Chromosome mutate(Chromosome ch1, float mutationRate) {
        int[] newChromosome = new int[ch1.chromosomeLength];
        for(int i = 0; i < ch1.chromosomeLength; i++) {
            newChromosome[i] = ch1.getChromosome()[i];
            if(Math.random() <= mutationRate) {
                int randIndex = new Random().nextInt(ch1.chromosomeLength);
                newChromosome[i] = ch1.getChromosome()[randIndex];
            }
        }
        return new Chromosome(newChromosome, ch1.capacities, ch1.noOfSupervisors);
    }

    public int getFitness() {
        return fitness;
    }

    public int[] getGenes(){
        return genes;
    }

    public String chromosomeToString(){
        StringBuilder temp = new StringBuilder();
        for(int i = 0; i < chromosomeLength; i++) {
            temp.append("[" + chromosome[i] + "]");
        }
        return temp.toString();
    }

}
