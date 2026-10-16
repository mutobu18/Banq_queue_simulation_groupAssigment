
import java.util.Random;

public class BankQueueSimulation {

    // Represents one customer in the queue
    static class Customer {
        int id;
        double iat;
        double arrivalTime;
        double serviceTime;
        double serviceBeginTime;
        double waitingTime;
        double serviceEndTime;
        double timeInSystem;
        double idleTime;

        public Customer(int id, double iat, double arrivalTime,
                        double serviceTime) {
            this.id = id;
            this.iat = iat;
            this.arrivalTime = arrivalTime;
            this.serviceTime = serviceTime;
        }
    }

    public static void main(String[] args) {

        // ==========================================
        // 1. INPUT SCREEN
        // ==========================================

        System.out.println("==========================================================");
        System.out.println("       BANK SINGLE-SERVER QUEUE SIMULATION SYSTEM");
        System.out.println("==========================================================");
        System.out.println("----------------------- INPUT SCREEN ---------------------");
        System.out.println("System Scenario         : Bank Customer Queue Simulation");
        System.out.println("Target Customers        : 100");
        System.out.println("Inter-Arrival Time      : Uniform Distribution (1, 5) minutes");
        System.out.println("Service Time            : Uniform Distribution (1, 6) minutes");
        System.out.println("----------------------------------------------------------");
        System.out.println("Status: Parameters loaded successfully.");
        System.out.println("Running simulation...\n");

        // ==========================================
        // 2. INITIALIZE SIMULATION
        // ==========================================

        int totalCustomers = 100;

        Customer[] customers = new Customer[totalCustomers];
        Random rand = new Random();

        double prevArrivalTime = 0.0;
        double prevServiceEndTime = 0.0;

        // ==========================================
        // 3. GENERATE CUSTOMERS AND SIMULATE QUEUE
        // ==========================================

        for (int i = 0; i < totalCustomers; i++) {

            // First customer arrives at time zero.
            // Other customers have inter-arrival times
            // uniformly distributed between 1 and 5 minutes.

            double iat;

            if (i == 0) {
                iat = 0.0;
            } else {
                iat = 1.0 + (rand.nextDouble() * 4.0);
            }

            double arrivalTime = prevArrivalTime + iat;

            // Service time is uniformly distributed
            // between 1 and 6 minutes.

            double serviceTime = 1.0 + (rand.nextDouble() * 5.0);

            Customer c = new Customer(
                    i + 1,
                    iat,
                    arrivalTime,
                    serviceTime
            );

            // Determine when service begins.

            if (i == 0) {
                c.serviceBeginTime = c.arrivalTime;
                c.idleTime = 0.0;
            } else {
                c.serviceBeginTime = Math.max(
                        c.arrivalTime,
                        prevServiceEndTime
                );

                c.idleTime = Math.max(
                        0.0,
                        c.arrivalTime - prevServiceEndTime
                );
            }

            // Calculate waiting time, service completion,
            // and total time spent in the system.

            c.waitingTime =
                    c.serviceBeginTime - c.arrivalTime;

            c.serviceEndTime =
                    c.serviceBeginTime + c.serviceTime;

            c.timeInSystem =
                    c.serviceEndTime - c.arrivalTime;

            // Update the previous customer's times.

            prevArrivalTime = c.arrivalTime;
            prevServiceEndTime = c.serviceEndTime;

            customers[i] = c;
        }

        // ==========================================
        // 4. OUTPUT SCREEN: SIMULATION TABLE
        // ==========================================

        System.out.println("\n==========================================================");
        System.out.println("          OUTPUT SCREEN: SIMULATION TABLE");
        System.out.println("==========================================================");

        System.out.printf(
                "%-6s | %-8s | %-12s | %-12s | %-12s | %-12s | %-12s | %-10s%n",
                "Cust",
                "IAT",
                "Arrival",
                "Service",
                "Begin",
                "Wait",
                "End",
                "Idle"
        );

        System.out.println(
                "------------------------------------------------------------------------------------------------------"
        );

        double totalWaitTime = 0.0;
        double totalServiceTime = 0.0;
        double totalTimeInSystem = 0.0;
        double totalIdleTime = 0.0;

        for (Customer c : customers) {

            totalWaitTime += c.waitingTime;
            totalServiceTime += c.serviceTime;
            totalTimeInSystem += c.timeInSystem;
            totalIdleTime += c.idleTime;

            // Display the first 15 and last 10 customers.

            if (c.id <= 15 || c.id > 90) {

                System.out.printf(
                        "%-6d | %-8.2f | %-12.2f | %-12.2f | %-12.2f | %-12.2f | %-12.2f | %-10.2f%n",
                        c.id,
                        c.iat,
                        c.arrivalTime,
                        c.serviceTime,
                        c.serviceBeginTime,
                        c.waitingTime,
                        c.serviceEndTime,
                        c.idleTime
                );

            } else if (c.id == 16) {

                System.out.println(
                        "  ...  |   ...    |     ...      |     ...      |     ...      |     ...      |     ...      |    ..."
                );
            }
        }

        // ==========================================
        // 5. OUTPUT SCREEN: QUEUE STATISTICS
        // ==========================================

        double avgWaitTime =
                totalWaitTime / totalCustomers;

        double avgServiceTime =
                totalServiceTime / totalCustomers;

        double avgTimeInSystem =
                totalTimeInSystem / totalCustomers;

        double totalSimulationTime =
                customers[totalCustomers - 1].serviceEndTime;

        double serverUtilization =
                (totalServiceTime / totalSimulationTime) * 100.0;

        System.out.println("\n==========================================================");
        System.out.println("             QUEUE STATISTICS SUMMARY");
        System.out.println("==========================================================");

        System.out.printf(
                "Total Simulation Time      : %.2f minutes%n",
                totalSimulationTime
        );

        System.out.printf(
                "Average Waiting Time       : %.2f minutes per customer%n",
                avgWaitTime
        );

        System.out.printf(
                "Average Service Time       : %.2f minutes per customer%n",
                avgServiceTime
        );

        System.out.printf(
                "Average Time in System     : %.2f minutes per customer%n",
                avgTimeInSystem
        );

        System.out.printf(
                "Total Server Idle Time     : %.2f minutes%n",
                totalIdleTime
        );

        System.out.printf(
                "Server Utilization Rate    : %.2f%%%n",
                serverUtilization
        );

        System.out.println("==========================================================");
        System.out.println("             SIMULATION COMPLETED");
        System.out.println("==========================================================");
    }
}
