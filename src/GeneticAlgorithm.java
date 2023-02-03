import java.lang.reflect.Array;
import java.util.*;

public class GeneticAlgorithm  {

    private  ArrayList<Chromosome> supervisors;
    private ArrayList<Student> students;
    private int populationSize;
    private int chromosomeLength;
    private int generations;
    private float crossoverRate;
    private float mutationRate;

    private int generation = 1;
    private ArrayList<Chromosome> bestMapping;
    private float bestSumFitnessScore = Float.MAX_VALUE;

    private float[] averageFitnessPerGeneration;
    private GeneticAlgorithm(int populationSize, int chromosomeLength, int generations, float crossoverRate, float mutationRate, ArrayList<Chromosome> supervisors, ArrayList<Student> students) {
        this.populationSize = populationSize;
        this.chromosomeLength = chromosomeLength;
        this.generations = generations;
        this.crossoverRate = crossoverRate;
        this.mutationRate = mutationRate;

        averageFitnessPerGeneration = new float[generations-1];
        this.supervisors = supervisors;
        this.students = students;
        startSearch();
    }

    // helper function to print chromosomes and their fitness of a population
    public void printPopulation(ArrayList<Chromosome> array) {
        StringBuilder output = new StringBuilder();
        for(Chromosome ch : array) {
            output.append("Chromosome: " +ch.getChromosomeStringBuilder().toString() + " Fitness: " + ch.getFitness() + "\n");
        }
        System.out.println(output.toString());
    }

    private void startSearch() {
        float sumFitness = 0;
        boolean crossover = false;

        while ((generation < generations)) {

            ArrayList<Chromosome> newPopulation = new ArrayList<Chromosome>();

            // sort the collection based on fitness in ascending order
            Collections.sort(supervisors, new Comparator<Chromosome>() {
                @Override
                public int compare(Chromosome o1, Chromosome o2) {
                    return o1.getFitness() - o2.getFitness();
                }
            });

            // select top 5 % of the population and paste it into the list of chromosomes used for reproduction
            int index = (int) (0.05 * populationSize);
            ArrayList<Chromosome> reproduction = new ArrayList<Chromosome>(supervisors.subList(0, index));

            for(int chromosomeIndex = 0; chromosomeIndex < populationSize; chromosomeIndex++) {
                // get random chromosomes from the reproduction list
                int randIndex1 = 0, randIndex2 = 0;
                do {
                    randIndex1 = new Random().nextInt(reproduction.size());
                    randIndex2 = new Random().nextInt(reproduction.size());
                } while (randIndex1 != randIndex2);

                Chromosome ch1 = reproduction.get(randIndex1);
                Chromosome ch2 = reproduction.get(randIndex2);

                // StringBuilder representations of chromosomes for reproduction
                StringBuilder strBuildCh1 = ch1.getChromosomeStringBuilder();
                StringBuilder strBuildCh2 = ch2.getChromosomeStringBuilder();

                // select a random crossover point and create a new chromosome by crossover of 2 chromosomes from the reproduction list
                if (Math.random() <= crossoverRate) {
                    int crossoverPoint = (int) ((Math.random() * (chromosomeLength-1 - 1)) + 1);
                    newPopulation.add(Chromosome.crossover(strBuildCh1, strBuildCh2, crossoverPoint));
                    crossover = true;
                }

                // mutate the latest chromosome in the new population (Skip if the first chromosome in this population has not been created by crossover)
                if (Math.random() <= mutationRate) {
                    if(newPopulation.size() != 0) {
                        int mutationIndex = newPopulation.size() - 1;
                        Chromosome latestChromosome = newPopulation.get(mutationIndex);
                        newPopulation.set(mutationIndex, Chromosome.mutate(latestChromosome.getChromosomeStringBuilder(), latestChromosome.getGenes()));
                    }
                }

                // create a random chromosome if crossover did not happen at this iteration
                if(!(crossover)) {
                    Chromosome newChromosome = new Chromosome(chromosomeLength);
                    newChromosome.generateRandomChromosome();
                    newPopulation.add(newChromosome);
                }

                crossover = false;
            }

            // update the fitness of the newly generated population and check whether the solution has been found
            for (Chromosome ch : newPopulation) {
                ch.calculateFitness(students);
                sumFitness += ch.getFitness();
            }

            // if the sum fitness of this population is the lowest so far then this is the best mapping generated so far
            if(bestSumFitnessScore > sumFitness) {
                bestSumFitnessScore = sumFitness;
                bestMapping = (ArrayList<Chromosome>) newPopulation.clone();
            }


            // calculate the average fitness of this generation
            averageFitnessPerGeneration[generation-1] = sumFitness / supervisors.size();

            supervisors = new ArrayList<Chromosome>(newPopulation);
            newPopulation.clear();
            reproduction.clear();
            sumFitness = 0;

           // System.out.println("Average fitness for generation " + generation + " = " + averageFitnessPerGeneration[generation-1]);
            generation++;
        }

       // printPopulation(supervisors);

        //
        System.out.println("The best mapping evolved: ");
        for(Chromosome ch : bestMapping) {
            System.out.println(ch.getChromosomeStringBuilder().toString() + " Fitness: " +  ch.getFitness());
        }

        /*
        for(int i = 0; i < averageFitnessPerGeneration.length; i ++) {
            System.out.print(averageFitnessPerGeneration[i] + ",");
        }*/

    }

    public static GeneticAlgorithm createGA(int populationSize, int chromosomeLength, int generations, float crossoverRate, float mutationRate, ArrayList<Chromosome> supervisors, ArrayList<Student> students) {
        boolean flag = true;

        if(populationSize <= 0) {
            flag = false;
        }

        if (chromosomeLength <= 0) {
            flag = false;
        }

        if (generations <= 0) {
            flag = false;
        }

        if (crossoverRate < 0.0 || crossoverRate > 1.0) {
            flag = false;
        }

        if (mutationRate < 0.0 || mutationRate > 1.0) {
            flag = false;
        }

        if(supervisors.isEmpty()) {
            flag = false;
        }

        if(students.isEmpty()) {
            flag = false;
        }

        return (flag) ? new GeneticAlgorithm(populationSize, chromosomeLength, generations, crossoverRate, mutationRate, supervisors, students) : null;
    }

}