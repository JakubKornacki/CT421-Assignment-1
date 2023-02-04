import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

public class Chromosome {
    private int[] genes;
    private int[] chromosome;
    private int[] capacity;
    private int fitness;
    private int chromosomeLength;

    private int noOfSupervisors;

    public Chromosome(int[] chromosome, int[] capacity, int noOfSupervisors) {
        this.capacity = capacity;
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

    public void setCapacity(int[] capacity) {
        this.capacity = capacity;
    }

    public int[] generateGenes(int noOfSupervisors) {
        int[] temp = new int[noOfSupervisors];
        for(int i = 0; i < noOfSupervisors; i++) {
            temp[i] = i+1;
        }
        return temp;
    }


   public void calculateFitness(ArrayList<Student> students) {
        fitness = 0;
        // For each student s assigned to work with lecturer l, consider the preference p that the student had
        // for that lecturer. Sum these preferences together. The lower the sum, the better the solution.
        int studentsConsidered = 0;
        for(Student student : students) {
            int [] preferences = student.getPreferenceList();
            for (int studentIndex = 0; studentIndex < chromosome.length; studentIndex++) {
                // if the lecturer is equal
                if (chromosome[studentIndex] == student.getStudentId()) {
                    int lecturerIndex = chromosome[studentIndex]-1;
                    if(studentIndex == 0) {
                        fitness += preferences[chromosome[studentIndex] -1];
                    } else {
                        fitness += preferences[chromosome[studentIndex - 1] - 1];
                    }
                    studentsConsidered++;
                }
            }
            if(studentsConsidered > capa)

                studentsConsidered = 0;
        }
    }

    public int[] getChromosome() {
        return chromosome;
    }

    public int[] getCapacities() {
        return capacity;
    }


    // generate the chromosome by randomly appending genes to it
    public void generateRandomChromosome() {
        for(int i = 0; i < chromosomeLength; i++) {
            int randIndex = new Random().nextInt(genes.length);
            chromosome[i] = genes[randIndex];
        }
    }


    // create a new chromosome from combining parts of two different chromosomes, split position of the chromosomes is the crossover point
    public static Chromosome crossover(Chromosome ch1, Chromosome ch2, int crossoverPoint) {
        int[] newChromosome = new int[ch1.chromosomeLength];
        for(int i = 0; i < ch1.chromosomeLength; i++) {
            if(i  < crossoverPoint) {
                newChromosome[i] = ch1.getChromosome()[i];  // int array
            } else {
                newChromosome[i] = ch2.getChromosome()[i];
            }
        }
        return new Chromosome( newChromosome, ch1.capacity, ch1.noOfSupervisors);
    }



    public static Chromosome mutate(Chromosome ch1, float mutationRate) {
        int[] newChromosome = new int[ch1.chromosomeLength];
        for(int i = 0; i < ch1.chromosomeLength; i++) {
            newChromosome[i] = ch1.getChromosome()[i];
            if(Math.random() <= mutationRate) {
                int randIndex = new Random().nextInt(ch1.chromosomeLength);
                newChromosome[i] = ch1.getChromosome()[randIndex];
            }
        }
        return new Chromosome(newChromosome, ch1.capacity, ch1.noOfSupervisors);
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
