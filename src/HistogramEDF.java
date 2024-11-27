import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.statistics.HistogramDataset;

import javax.swing.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class HistogramEDF extends JFrame {

    public HistogramEDF(double[] inputData) {
        // Step 1: Sort the data
        Arrays.sort(inputData);

        // Step 2: Calculate frequency of each unique value
        Map<Double, Integer> frequencyMap = new HashMap<>();
        for (double value : inputData) {
            frequencyMap.put(value, frequencyMap.getOrDefault(value, 0) + 1);
        }

        // Step 3: Calculate relative frequency (proportions)
        int n = inputData.length;
        Map<Double, Double> relativeFrequencyMap = new HashMap<>();
        for (Map.Entry<Double, Integer> entry : frequencyMap.entrySet()) {
            double proportion = (double) entry.getValue() / n;
            relativeFrequencyMap.put(entry.getKey(), proportion);
        }

        // Step 4: Create intervals for the histogram (using Sturges' formula)
        int bins = (int) (1 + Math.log(n) / Math.log(2)); // Sturges' formula
        double min = inputData[0];
        double max = inputData[n - 1];
        double intervalWidth = (max - min) / bins;

        // Step 5: Calculate the cumulative relative frequency for the histogram
        double[] cumulativeFrequencies = new double[bins];
        double start = min;
        double cumulativeFrequency = 0.0;

        // Count the number of data points in each bin
        for (int i = 0; i < bins; i++) {
            double end = start + intervalWidth;
            int countInBin = 0;

            // Count the number of data points that fall in the bin
            for (double value : inputData) {
                if (value >= start && value < end) {
                    countInBin++;
                }
            }

            // Calculate the cumulative frequency for this bin
            cumulativeFrequency += (double) countInBin / n;
            cumulativeFrequencies[i] = cumulativeFrequency;

            // Move to the next interval
            start = end;
        }

        // Step 6: Plot the histogram using JFreeChart
        HistogramDataset histogramDataset = new HistogramDataset();
        histogramDataset.addSeries("Cumulative Histogram for EDF", cumulativeFrequencies, bins, min, max + intervalWidth);

        JFreeChart histogramChart = ChartFactory.createHistogram(
                "Cumulative Histogram for EDF",
                "Value",
                "Cumulative Proportion",
                histogramDataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        // Step 7: Display the chart
        ChartPanel chartPanel = new ChartPanel(histogramChart);
        chartPanel.setPreferredSize(new java.awt.Dimension(800, 600));
        setContentPane(chartPanel);

        setTitle("Cumulative Histogram for Empirical Distribution Function");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        // Example dataset (replace this with your actual dataset)
        double[] inputData = {-0.76, -1.14, -0.55, 1.07, -0.62, -0.14, 0.21, -1.45, -1.31, 1.45,
                0.64, 0.24, -0.21, 1.46, -1.07, 1.04, 0.21, -0.31, 1.16, -1.12};

        SwingUtilities.invokeLater(() -> {
            HistogramEDF frame = new HistogramEDF(inputData);
            frame.setVisible(true);
        });
    }
}
