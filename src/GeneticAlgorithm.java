import java.lang.reflect.Array;
import java.util.*;



public class GeneticAlgorithm  {

    private  ArrayList<Chromosome> chromosomes;
    private int populationSize;
    private int chromosomeLength;
    private int generations;
    private float crossoverRate;
    private float mutationRate;

    private Chromosome solutionChromosome = null;
    private int solutionGeneration = 0;
    private int generation = 01;
    private float[] averageFitnessPerGeneration;
    private GeneticAlgorithm(int populationSize, int chromosomeLength, int generations, float crossoverRate, float mutationRate) {
        this.populationSize = populationSize;
        this.chromosomeLength = chromosomeLength;
        this.generations = generations;
        this.crossoverRate = crossoverRate;
        this.mutationRate = mutationRate;

        averageFitnessPerGeneration = new float[generations-1];
        chromosomes = new ArrayList<Chromosome>(populationSize);

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

        // generate initial population
        for(int i = 0; i < populationSize-1; i++){
            Chromosome temp = new Chromosome(chromosomeLength);
            temp.generateRandomChromosome();
            temp.calculateFitness();
            chromosomes.add(temp);
        }

        chromosomes.set(0, new Chromosome( new StringBuilder("000000000000000000000000000000")));
        chromosomes.get(0).calculateFitness();


        while ((generation < generations)) {

            ArrayList<Chromosome> newPopulation = new ArrayList<Chromosome>();

            // sort the collection based on fitness in descending order
            Collections.sort(chromosomes, new Comparator<Chromosome>() {
                @Override
                public int compare(Chromosome o1, Chromosome o2) {
                    return o2.getFitness() - o1.getFitness();
                }
            });

            // select top 5 % of the population and paste it into the list of chromosomes used for reproduction
            int index = (int) (0.05 * populationSize);
            ArrayList<Chromosome> reproduction = new ArrayList<Chromosome>(chromosomes.subList(0, index));

            for(int chromosomeIndex = 0; chromosomeIndex < populationSize; chromosomeIndex++) {
                // get random chromosomes from the reproduction list
                int randIndex1 = (int) (Math.random() * reproduction.size());
                int randIndex2 = (int) (Math.random() * reproduction.size());

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
                ch.calculateFitness();
                sumFitness += ch.getFitness();
                if (ch.getChromosomeStringBuilder().toString().equals(Chromosome.solution) && solutionChromosome == null) {
                    solutionChromosome = ch;
                    solutionGeneration = generation;
                    break;
                }
            }

            // calculate the average fitness of this generation
            averageFitnessPerGeneration[generation-1] = sumFitness / chromosomes.size();

            chromosomes = new ArrayList<Chromosome>(newPopulation);
            newPopulation.clear();
            reproduction.clear();
            sumFitness = 0;

           // System.out.println("Average fitness for generation " + generation + " = " + averageFitnessPerGeneration[generation-1]);
            generation++;
        }

        if(solutionChromosome == null) {
            System.out.println("No solution found in this run");
        } else {
            System.out.println("Solution found at generation: " + solutionGeneration + " Solution: " + solutionChromosome.getChromosomeStringBuilder().toString());
        }

        printPopulation(chromosomes);

/*
        for(int i = 0; i < averageFitnessPerGeneration.length; i ++) {
            System.out.print(averageFitnessPerGeneration[i] + ",");
        } */

    }

    public static GeneticAlgorithm createGA(int populationSize, int chromosomeLength, int generations, float crossoverRate, float mutationRate) {
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

        return (flag) ? new GeneticAlgorithm(populationSize, chromosomeLength, generations, crossoverRate, mutationRate) : null;
    }

}