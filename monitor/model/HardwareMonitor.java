package monitor.model;

public class HardwareMonitor {
    private final oshi.SystemInfo systemInfo;
    private final oshi.hardware.HardwareAbstractionLayer hardware;
    private final oshi.software.os.OperatingSystem os;
    private long[] prevTicks;

    public HardwareMonitor() {
        this.systemInfo = new oshi.SystemInfo();
        this.hardware = systemInfo.getHardware();
        this.os = systemInfo.getOperatingSystem();
        // Initialize ticks
        this.prevTicks = hardware.getProcessor().getSystemCpuLoadTicks();
    }

    public double getCpuUsage() {
        long[] currentTicks = hardware.getProcessor().getSystemCpuLoadTicks();
        // Calculate load between the previous call and now
        double load = hardware.getProcessor().getSystemCpuLoadBetweenTicks(prevTicks) * 100;
        // Update previous ticks for the next call
        prevTicks = currentTicks;
        return load;
    }

    public double getMemoryUsage() {
        oshi.hardware.GlobalMemory memory = hardware.getMemory();
        long total = memory.getTotal();
        long available = memory.getAvailable();
        return ((double) (total - available) / total) * 100;
    }

    public String getProcessorName() {
        return hardware.getProcessor().getProcessorIdentifier().getName();
    }

    public long getTotalMemory() {
        return hardware.getMemory().getTotal();
    }

    public double getCpuTemperature() {
        return hardware.getSensors().getCpuTemperature();
    }

    public long getUptime() {
        return os.getSystemUptime();
    }

    public long[] getNetworkTraffic() {
        long rx = 0, tx = 0;
        for (oshi.hardware.NetworkIF net : hardware.getNetworkIFs()) {
            net.updateAttributes();
            rx += net.getBytesRecv();
            tx += net.getBytesSent();
        }
        return new long[]{rx, tx};
    }

    public String getOsName() {
        return String.valueOf(os);
    }
}
