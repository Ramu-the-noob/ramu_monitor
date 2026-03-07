package monitor;

import monitor.model.HardwareMonitor;

public class App {
    public static void main(String[] args) throws InterruptedException {
        // Check if user wants to run GUI mode
        if (args.length > 0 && args[0].equalsIgnoreCase("gui")) {
            javax.swing.SwingUtilities.invokeLater(() -> {
                new GuiApp().setVisible(true);
            });
            return; // Exit the main thread so CLI loop doesn't start
        }

        System.out.println("Starting Ramu-Monitor...");

        HardwareMonitor monitor = new HardwareMonitor();

        // Initialize variables for Network Speed calculation
        long[] prevNetwork = monitor.getNetworkTraffic();
        long lastTime = System.currentTimeMillis();

        // Initial sleep to allow CPU usage calculation to have a baseline
        Thread.sleep(1000);

        while (true) {
            long currentTime = System.currentTimeMillis();
            double cpuUsage = monitor.getCpuUsage();
            double memUsage = monitor.getMemoryUsage();
            double cpuTemp = monitor.getCpuTemperature();
            long uptime = monitor.getUptime();

            // Calculate Network Speed (Bytes per second)
            long[] currentNetwork = monitor.getNetworkTraffic();
            long timeDiff = currentTime - lastTime;
            if (timeDiff == 0) timeDiff = 1; // Avoid division by zero
            
            long rxSpeed = (currentNetwork[0] - prevNetwork[0]) * 1000 / timeDiff;
            long txSpeed = (currentNetwork[1] - prevNetwork[1]) * 1000 / timeDiff;
            
            // Update previous state for next loop
            prevNetwork = currentNetwork;
            lastTime = currentTime;

            // ANSI Escape codes to clear screen (works in most Linux terminals)
            System.out.print("\033[H\033[2J");
            System.out.flush();

            System.out.println("=== Ramu Monitor (" + monitor.getOsName() + ") ===");
            System.out.println("Processor name is " + monitor.getProcessorName());
            System.out.println("----------------------------------");

            System.out.printf("Cpu usage is %.2f%%%n", cpuUsage);

            System.out.printf("Cpu Temp  is %.1f C%n", cpuTemp);

            System.out.printf("Memory usage is %.2f%%%n", memUsage);

            System.out.println("Total memory is " + monitor.getTotalMemory() + " bytes");

            System.out.printf("Total memory in GB is : %.2f GB%n", (monitor.getTotalMemory() / 1024.0 / 1024 / 1024));
            System.out.println("----------------------------------");
            System.out.printf("Network Rx: %.2f KB/s%n", rxSpeed / 1024.0);
            System.out.printf("Network Tx: %.2f KB/s%n", txSpeed / 1024.0);
            System.out.printf("Uptime    : %d days, %02d:%02d:%02d%n", 
                uptime / 86400, (uptime % 86400) / 3600, (uptime % 3600) / 60, uptime % 60);

            System.out.println("----------------------------------");

            Thread.sleep(1000); // Update every 1 second
        }
    }
}