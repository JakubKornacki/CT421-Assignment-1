public class Main {
    public static void main(String[] args) {
        if(args.length < 5 || args[0].isEmpty() || args[1].isEmpty() || args[2].isEmpty() || args[3].isEmpty() || args[4].isEmpty()) {
            System.out.println("Need to specify arguments: Population size: (int), Chromosome length (int), Generations (int), Crossover rate (float), Mutation Rate (float)");
            System.exit(1);
        }

        // parse population size and throw NumberFormatException if contains chars other than numbers
        int populationSize = Integer.parseInt(args[0]);
        int chromosomeLength = Integer.parseInt(args[1]);
        int generations = Integer.parseInt(args[2]);
        float crossoverRate = Float.parseFloat(args[3]);
        float mutationRate = Float.parseFloat(args[4]);
        GeneticAlgorithm ga = GeneticAlgorithm.createGA(populationSize, chromosomeLength, generations, crossoverRate, mutationRate);
        // something went wrong when creating the genetic algorithm
        if(ga == null) {
            System.out.println("Population size, chromosome length, number of generations, crossover rate and mutation rate need to be greater than 0.");
            System.exit(1);
        }
    }
}