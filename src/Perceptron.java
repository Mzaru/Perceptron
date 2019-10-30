import java.util.concurrent.ThreadLocalRandom;

public class Perceptron {

    private double[] weights;
    private double threshold, learningRate;

    public Perceptron(int numberOfInputs, double learningRate) {
        this.learningRate = learningRate;
        ThreadLocalRandom random = ThreadLocalRandom.current();
        weights = new double[numberOfInputs];
        for (int i = 0; i < weights.length; i++) {
            weights[i] = random.nextDouble(0.2);
        }
        threshold = random.nextDouble(0.2);
    }

    public boolean compute(double[] input) {
        if (input.length != weights.length) {
            throw new IllegalArgumentException("The number of inputs should be equal to number of weights");
        }
        double net = 0;
        for (int i = 0; i < weights.length; i++) {
            net += weights[i] * input[i];
        }
        if (net >= threshold) {
            return true;
        } else {
            return false;
        }
    }

    public void train(double[] input, boolean answer) {
        boolean decision = this.compute(input);
        if (answer == decision) {
            return;
        }

        for (int i = 0; i < weights.length; i++) {
            weights[i] = weights[i] + learningRate * ((answer) ? 1 : -1) * input[i];
        }
        threshold = threshold + learningRate * ((answer) ? 1 : -1) * -1;
    }

    public int getNumberOfInputs() {
        return weights.length;
    }
}
