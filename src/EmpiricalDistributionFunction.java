import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.util.Arrays;

public class EmpiricalDistributionFunction extends JFrame {

    public EmpiricalDistributionFunction(double[] data) {
        // Sort the data for EDF calculation
        Arrays.sort(data);
        int n = data.length;

        // Empirical Distribution Function (EDF)
        XYSeries edfSeries = new XYSeries("Empirical Distribution Function");
        double cumulativeProbability = 0.0;
        for (int i = 0; i < n; i++) {
            cumulativeProbability = (i + 1.0) / n;
            edfSeries.add(data[i], cumulativeProbability);
            // Add step transition
            if (i < n - 1) {
                edfSeries.add(data[i + 1], cumulativeProbability);
            }
        }

        // Frequency Polygon (Group Data)
        int bins = (int) (1 + Math.log(data.length) / Math.log(2)); // Sturges' formula
        double min = data[0];
        double max = data[data.length - 1];
        double intervalWidth = (max - min) / bins;

        XYSeries frequencyPolygonSeries = new XYSeries("Frequency Polygon");
        double start = min;
        for (int i = 0; i < bins; i++) {
            double midpoint = start + intervalWidth / 2;
            double frequency = 0;

            for (double value : data) {
                if (value >= start && value < start + intervalWidth) {
                    frequency++;
                }
            }

            frequencyPolygonSeries.add(midpoint, frequency / n); // Proportional frequency
            start += intervalWidth;
        }

        // Create datasets for plotting
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(edfSeries);
        dataset.addSeries(frequencyPolygonSeries);

        // Create the chart
        JFreeChart chart = ChartFactory.createXYLineChart(
                "Empirical Distribution Function and Frequency Polygon",
                "Value",
                "Cumulative Probability",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        // Add the chart to a panel
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(800, 600));
        setContentPane(chartPanel);
    }

    public static void main(String[] args) {
        // Example dataset
        double[] data = {-0.76, -1.14, -0.55, 1.07, -0.62, -0.14, 0.21, -1.45, -1.31, 1.45,
                0.64, 0.24, -0.21, 1.46, -1.07, 1.04, 0.21, -0.31, 1.16, -1.12};

        SwingUtilities.invokeLater(() -> {
            EmpiricalDistributionFunction frame = new EmpiricalDistributionFunction(data);
            frame.setSize(800, 600);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });
    }
}
