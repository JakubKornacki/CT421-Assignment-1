import java.lang.reflect.Array;
import java.util.*;

public class GeneticAlgorithm  {

    private ArrayList<Student> students;
    private ArrayList<Chromosome> chromosomes;
    private ArrayList<Chromosome> supervisors;
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
        this.averageFitnessPerGeneration = new float[generations-1];
        this.students = students;
        this.supervisors = supervisors;
        this.chromosomes = new ArrayList<Chromosome>(populationSize);
        startSearch();
    }


    private void startSearch() {
        float sumFitness = 0;
        boolean crossover = false;

        // generate initial population
        for(int i = 0; i < populationSize; i++) {
            Chromosome temp = new Chromosome(students.size(), supervisors.size());
            temp.setCapacity(supervisors.get(0).getCapacities());
            temp.generateRandomChromosome();
            temp.calculateFitness(students);
            chromosomes.add(temp);
        }

        while (generation < generations) {

            ArrayList<Chromosome> newPopulation = new ArrayList<Chromosome>();

            // sort the collection based on fitness in ascending order
            Collections.sort(chromosomes, new Comparator<Chromosome>() {
                @Override
                public int compare(Chromosome o1, Chromosome o2) {
                    return o1.getFitness() - o2.getFitness();
                }
            });

            Chromosome ch1 = chromosomes.get(0);
            Chromosome ch2 = chromosomes.get(1);

            for(int chromosomeIndex = 0; chromosomeIndex < populationSize; chromosomeIndex++) {

                // select a random crossover point and create a new chromosome by crossover of 2 chromosomes from the reproduction list
                if (Math.random() <= crossoverRate) {
                    int crossoverPoint = (int) ((Math.random() * (chromosomeLength-1 - 1)) + 1);
                    newPopulation.add(Chromosome.crossover(ch1, ch2, crossoverPoint));
                    crossover = true;
                }

                // mutate the latest chromosome in the new population (Skip if the first chromosome in this population has not been created by crossover)

                if(newPopulation.size() != 0) {
                    int mutationIndex = newPopulation.size() - 1;
                    Chromosome latestChromosome = newPopulation.get(mutationIndex);
                    newPopulation.set(mutationIndex, Chromosome.mutate(latestChromosome, mutationRate));
                }


                // create a random chromosome if crossover did not happen at this iteration
                if(!(crossover)) {
                    newPopulation.add(chromosomeIndex,new Chromosome(students.size(), supervisors.size()));
                    newPopulation.get(chromosomeIndex).generateRandomChromosome();
                    newPopulation.get(chromosomeIndex).setCapacity(ch1.getCapacities());
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
            averageFitnessPerGeneration[generation-1] = sumFitness / chromosomes.size();

            chromosomes = new ArrayList<Chromosome>(newPopulation);
            newPopulation.clear();
            sumFitness = 0;

           // System.out.println("Average fitness for generation " + generation + " = " + averageFitnessPerGeneration[generation-1]);
            generation++;
        }

        System.out.println("The best mapping evolved: ");
        for(Chromosome ch : bestMapping) {
            System.out.println(ch.chromosomeToString() + "\t\t\tFitness: " +  ch.getFitness());
        }
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