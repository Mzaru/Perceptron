import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public class Classifier {
    private Perceptron p;
    private String negative = null, positive = null;
    private int epoch;

    public Classifier() {
        Scanner in = new Scanner(System.in);
        double learningRate;
        System.out.println("Enter number of epochs ");
        epoch = in.nextInt();
        System.out.println("Enter learning rate");
        learningRate = in.nextDouble();
        try {
            String line;
            BufferedReader read = new BufferedReader(new FileReader("train.txt"));
            while ((line = read.readLine()) != null) {
                String[] lines = line.split(",");
                line = lines[lines.length - 1];
                if (negative == null) {
                    negative = line;
                    continue;
                } else if (!line.equals(negative)){
                    positive = line;
                    p = new Perceptron(lines.length - 1, learningRate);
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.train();
    }

    public void train() {
        for (int i = 0; i < epoch; i++) {
            try {
                String line;
                BufferedReader read = new BufferedReader(new FileReader("train.txt"));
                while ((line = read.readLine()) != null) {
                    String[] lines = line.split(",");
                    double[] input = parseString(lines);
                    boolean answer = positive.equals(lines[lines.length - 1]);
                    p.train(input, answer);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void test() {
        try {
            String line;
            BufferedReader read = new BufferedReader(new FileReader("test.txt"));
            double tp = 0, fn = 0, tn = 0, fp = 0;
            while ((line = read.readLine()) != null) {
                String[] lines = line.split(",");
                double[] input = parseString(lines);
                boolean answer = positive.equals(lines[lines.length - 1]);
                boolean decision = p.compute(input);
                if (answer == decision && answer) {
                    ++tp;
                } else if (answer == decision && !answer) {
                    ++tn;
                } else if (answer != decision && answer) {
                    ++fn;
                } else if (answer != decision && !answer) {
                    ++fp;
                }
                System.out.printf("For %s answer is: %s prediction is %s%n", Arrays.toString(input), answer? positive : negative, decision? positive : negative);
            }
            double accuracy = (tp + tn) / (tp + fp + fn + tn);
            double recall = tp / (tp + fn);
            System.out.printf("Accuracy is: %f  Recall is %f%n", accuracy * 100, recall * 100);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static double[] parseString(String[] s) {
        double[] arr = new double[s.length - 1];
        for (int i = 0; i < s.length - 1; i++) {
            arr[i] = Double.parseDouble(s[i]);
        }
        return arr;
    }

    public static void main(String[] args) {
//        6.9,3.1,4.9,1.5
        Classifier c = new Classifier();
        Scanner in = new Scanner(System.in);
        while (true) {
            System.out.println("Would you like to enter input manually? Y/N ");
            String line = in.nextLine();
            if (line.equalsIgnoreCase("y")) {
                double[] input = new double[c.p.getNumberOfInputs()];
                for (int i = 0; i < input.length; i++) {
                    input[i] = in.nextDouble();
                }
                boolean decision = c.p.compute(input);
                System.out.printf("The answer is %s%n", decision? c.positive: c.negative);
                break;
            } else if (line.equalsIgnoreCase("n")) {
                c.test();
                break;
            }
        }

    }
}
