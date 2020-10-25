import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.Stopwatch;

public class PercolationStats {
    private int n;
    private int trials;
    private double mean;
    private double SD;
    private double confLow;
    private double confHigh;
    private double[] result;
    private Percolation percolation;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        //Corner case
        if (n <= 0 || trials <= 0) throw new IllegalArgumentException();

        result = new double[trials];

        for (int i = 0; i < trials; i++) {
            percolation = new Percolation(n);
            int emptySites = 0;
            while (!percolation.percolates()) {
                int row = StdRandom.uniform(0, n);
                int col = StdRandom.uniform(0, n);
                percolation.open(row, col);
            }
            double res = percolation.numberOfOpenSites() / ((double) (n * n));
            result[i] = res;
        }
        double sum = 0;
        for (int i = 0; i < trials; i++) {
            sum += result[i];
        }
        mean = sum / trials;

        double sum2 = 0;
        for (int i = 0; i < trials; i++) {
            sum2 += Math.pow((result[i] - mean), 2);
        }
        SD = Math.sqrt(sum2 / (trials - 1));

        confLow = mean - ((1.96 * SD) / Math.sqrt(trials));
        confHigh = mean + ((1.96 * SD) / Math.sqrt(trials));
    }

    // sample mean of percolation threshold
    public double mean() {
        // return StdStats.mean(result);
        return mean;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(result);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLow() {
        // return mean() - ((1.96 * stddev()) / Math.sqrt(trials));
        return confLow;
    }

    // high endpoint of 95% confidence interval
    public double confidenceHigh() {
        // return mean() + ((1.96 * stddev()) / Math.sqrt(trials));
        return confHigh;
    }

    // test client (see below)
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);

        Stopwatch timer = new Stopwatch();

        PercolationStats percolationStats = new PercolationStats(n, trials);

        System.out.println("mean()             = " + percolationStats.mean());
        System.out.println("stddev()           = " + percolationStats.stddev());
        System.out.println(
                "confidenceLow()    = " + percolationStats.confidenceLow());
        System.out.println(
                "confidenceHigh()   = " + percolationStats.confidenceHigh());

        double time = timer.elapsedTime();

        System.out.println("elapsed time       = " + time);
    }
}

