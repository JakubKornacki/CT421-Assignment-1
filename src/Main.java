import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        if(args.length < 3 || args[0].isEmpty() || args[1].isEmpty() || args[2].isEmpty() || args[3].isEmpty()) {
            System.out.println("Need to specify arguments: Generations (int), Population size (int) Crossover rate (float), Mutation Rate (float)");
            System.exit(1);
        }

        // parse population size and throw NumberFormatException if contains chars other than numbers
        int generations = Integer.parseInt(args[0]);
        int populationSize = Integer.parseInt(args[1]);
        float crossoverRate = Float.parseFloat(args[2]);
        float mutationRate = Float.parseFloat(args[3]);

        ArrayList<Chromosome> supervisors = new ArrayList<Chromosome>();
        ArrayList<Student> students = new ArrayList<Student>();

        String line = "";
        String delimiter = ";";
        String[] data;
        String studentName;
        String[] studentPreferences = new String[0];
        int[] capacitiesOfSupervisors = new int[0];

        try
        {

            // read student data from csv file
            BufferedReader br = new BufferedReader(new FileReader("Student-choices.csv"));
            while ((line = br.readLine()) != null)
            {
                data = line.split(delimiter);
                studentName = data[0];
                studentPreferences = new String[data.length-1];
                for(int i = 0; i < data.length-1; i ++) {
                    studentPreferences[i] = data[i+1];
                }
                students.add(Student.createStudent(studentName, studentPreferences));
            }
            capacitiesOfSupervisors = new int[studentPreferences.length];
            // read lecturer data from csv file
            BufferedReader br2 = new BufferedReader(new FileReader("Supervisors.csv"));
            int supervisorIndex = 0;
            while ((line = br2.readLine()) != null)
            {
                data = line.split(delimiter);
                capacitiesOfSupervisors[supervisorIndex] = Integer.parseInt(data[1]);
                supervisors.add(new Chromosome(students.size(), studentPreferences.length));
                supervisors.get(supervisorIndex).generateRandomChromosome();
                supervisorIndex++;
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        for(int i = 0; i < supervisors.size(); i++) {
            supervisors.get(i).setCapacity(capacitiesOfSupervisors);
        }
        int chromosomeLength = students.size();

        GeneticAlgorithm ga = GeneticAlgorithm.createGA(populationSize, chromosomeLength, generations, crossoverRate, mutationRate, supervisors, students);
        // something went wrong when creating the genetic algorithm
        if(ga == null) {
            System.out.println("Generations, crossover rate, mutation rate need to be greater than 0.");
            System.exit(1);
        }


    }


}