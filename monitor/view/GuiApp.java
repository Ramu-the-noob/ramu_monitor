package monitor;

import monitor.model.HardwareMonitor;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;

public class GuiApp extends JFrame {

    private final HardwareMonitor monitor;
    private final JLabel cpuLabel;
    private final JLabel memoryLabel;
    private final JLabel tempLabel;
    private final JLabel networkRxLabel;
    private final JLabel networkTxLabel;
    private final JLabel totalMemoryLabel;
    private final JLabel uptimeLabel;

    public GuiApp() {
        monitor = new HardwareMonitor();

        setTitle("Ramu System Monitor");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 350);
        setLocationRelativeTo(null); // Center on screen

        // Set up dark theme colors
        Color backgroundColor = new Color(30, 30, 30);
        Color foregroundColor = new Color(0, 255, 0); // Hacker Green
        Font font = new Font("Monospaced", Font.BOLD, 16);

        // Main panel setup
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(7, 1, 10, 10)); // Increase rows to 7
        mainPanel.setBackground(backgroundColor);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Initialize labels
        cpuLabel = createLabel("CPU: Loading...", font, foregroundColor);
        memoryLabel = createLabel("Memory: Loading...", font, foregroundColor);
        tempLabel = createLabel("Temp: Loading...", font, foregroundColor);
        networkRxLabel = createLabel("Net Rx: Loading...", font, foregroundColor);
        networkTxLabel = createLabel("Net Tx: Loading...", font, foregroundColor);

        double totalGb = monitor.getTotalMemory() / 1024.0 / 1024 / 1024;
        totalMemoryLabel = createLabel(String.format("Total Mem : %.2f GB", totalGb), font, foregroundColor);
        uptimeLabel = createLabel("Uptime: Loading...", font, foregroundColor);

        // Add labels to panel
        mainPanel.add(cpuLabel);
        mainPanel.add(memoryLabel);
        mainPanel.add(tempLabel);
        mainPanel.add(totalMemoryLabel);
        mainPanel.add(networkRxLabel);
        mainPanel.add(networkTxLabel);
        mainPanel.add(uptimeLabel);

        add(mainPanel);

        // Start the background update thread
        startUpdateLoop();
    }

    private JLabel createLabel(String text, Font font, Color color) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setForeground(color);
        label.setHorizontalAlignment(SwingConstants.LEFT);
        return label;
    }

    private void startUpdateLoop() {
        Thread updateThread = new Thread(() -> {
            DecimalFormat df = new DecimalFormat("0.00");
            
            // Initialize network tracking
            long[] prevNetwork = monitor.getNetworkTraffic();
            long lastTime = System.currentTimeMillis();

            while (true) {
                try {
                    // Sleep for 1 second
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }

                long currentTime = System.currentTimeMillis();

                // Fetch data
                // getCpuUsage now calculates load since the last call (approx 1 sec ago)
                double cpu = monitor.getCpuUsage();
                double mem = monitor.getMemoryUsage();
                double temp = monitor.getCpuTemperature();
                long uptime = monitor.getUptime();

                // Calculate Network Speed
                long[] currentNetwork = monitor.getNetworkTraffic();
                long timeDiff = currentTime - lastTime;
                if (timeDiff == 0) timeDiff = 1;

                double rxSpeed = (currentNetwork[0] - prevNetwork[0]) * 1000.0 / timeDiff / 1024.0; // KB/s
                double txSpeed = (currentNetwork[1] - prevNetwork[1]) * 1000.0 / timeDiff / 1024.0; // KB/s

                prevNetwork = currentNetwork;
                lastTime = currentTime;

                // Update UI on the Event Dispatch Thread
                SwingUtilities.invokeLater(() -> {
                    cpuLabel.setText("CPU Usage : " + df.format(cpu) + "%");
                    memoryLabel.setText("Memory    : " + df.format(mem) + "%");
                    tempLabel.setText("CPU Temp  : " + df.format(temp) + "°C");
                    networkRxLabel.setText("Net Rx    : " + df.format(rxSpeed) + " KB/s");
                    networkTxLabel.setText("Net Tx    : " + df.format(txSpeed) + " KB/s");
                    uptimeLabel.setText(formatUptime(uptime));
                });
            }
        });
        updateThread.setDaemon(true); // Ensure thread closes when app closes
        updateThread.start();
    }

    private String formatUptime(long uptime) {
        long days = uptime / 86400;
        long hours = (uptime % 86400) / 3600;
        long minutes = (uptime % 3600) / 60;
        long seconds = uptime % 60;
        return String.format("Uptime    : %d days, %02d:%02d:%02d", days, hours, minutes, seconds);
    }

    public static void main(String[] args) {
        // Run GUI construction on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            new GuiApp().setVisible(true);
        });
    }
}