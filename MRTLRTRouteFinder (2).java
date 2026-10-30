import java.util.*;

/**
 * Console-based MRT/LRT Route Finder System.
 *
 * The main class provides a menu-driven interface to:
 * - Add and remove stations
 * - Add and remove connections
 * - Find the shortest route using BFS
 * - Display the graph
 */
public class MRTLRTRouteFinder {

    private static final int EXIT_OPTION = 7;

    public static void main(String[] args) {
        Graph graph = new Graph();
        initialiseSampleData(graph);

        Scanner scanner = new Scanner(System.in);
        int choice = 0;  // Initialize to avoid compile error

        System.out.println("===========================================");
        System.out.println("   MRT/LRT Route Finder System");
        System.out.println("   AMCS2034 Data Structures and Algorithms");
        System.out.println("===========================================");
        System.out.println("Simplified Klang Valley MRT/LRT network loaded.");
        System.out.println();

        do {
            displayMenu();
            System.out.print("Enter your choice: ");
            String input = scanner.nextLine().trim();

            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid menu option. Please enter a number from 1 to 7.");
                System.out.println();
                continue;
            }

            switch (choice) {
                case 1:
                    addStation(graph, scanner);
                    break;
                case 2:
                    removeStation(graph, scanner);
                    break;
                case 3:
                    addConnection(graph, scanner);
                    break;
                case 4:
                    removeConnection(graph, scanner);
                    break;
                case 5:
                    findRoute(graph, scanner);
                    break;
                case 6:
                    graph.displayGraph();
                    break;
                case EXIT_OPTION:
                    System.out.println("Exiting MRT/LRT Route Finder System. Thank you.");
                    break;
                default:
                    System.out.println("Invalid menu option. Please enter a number from 1 to 7.");
            }

            System.out.println();
        } while (choice != EXIT_OPTION);

        scanner.close();
    }

    /**
     * Displays the main menu.
     */
    private static void displayMenu() {
        System.out.println("========== MENU ==========");
        System.out.println("1. Add Station");
        System.out.println("2. Remove Station");
        System.out.println("3. Add Connection");
        System.out.println("4. Remove Connection");
        System.out.println("5. Find Route");
        System.out.println("6. Display Graph");
        System.out.println("7. Exit");
        System.out.println("==========================");
    }

    /**
     * Prompts the user to add a new station.
     */
    private static void addStation(Graph graph, Scanner scanner) {
        System.out.print("Enter station name to add: ");
        String station = scanner.nextLine().trim();

        if (station.isEmpty()) {
            System.out.println("Station name cannot be empty.");
            return;
        }

        if (graph.addStation(station)) {
            System.out.println("Station '" + station + "' added successfully.");
        } else {
            System.out.println("Station '" + station + "' already exists or is invalid.");
        }
    }

    /**
     * Prompts the user to remove an existing station.
     */
    private static void removeStation(Graph graph, Scanner scanner) {
        System.out.print("Enter station name to remove: ");
        String station = scanner.nextLine().trim();

        if (station.isEmpty()) {
            System.out.println("Station name cannot be empty.");
            return;
        }

        if (graph.removeStation(station)) {
            System.out.println("Station '" + station + "' removed. All incident connections were also removed.");
        } else {
            System.out.println("Station '" + station + "' does not exist.");
        }
    }

    /**
     * Prompts the user to add a connection between two stations.
     */
    private static void addConnection(Graph graph, Scanner scanner) {
        System.out.print("Enter first station: ");
        String stationA = scanner.nextLine().trim();

        System.out.print("Enter second station: ");
        String stationB = scanner.nextLine().trim();

        if (stationA.isEmpty() || stationB.isEmpty()) {
            System.out.println("Station names cannot be empty.");
            return;
        }

        if (graph.addConnection(stationA, stationB)) {
            System.out.println("Connection added: " + stationA + " <-> " + stationB);
        } else {
            System.out.println("Unable to add connection. Ensure both stations exist, the connection does not already exist, and the station is not connected to itself.");
        }
    }

    /**
     * Prompts the user to remove a connection between two stations.
     */
    private static void removeConnection(Graph graph, Scanner scanner) {
        System.out.print("Enter first station: ");
        String stationA = scanner.nextLine().trim();

        System.out.print("Enter second station: ");
        String stationB = scanner.nextLine().trim();

        if (stationA.isEmpty() || stationB.isEmpty()) {
            System.out.println("Station names cannot be empty.");
            return;
        }

        if (graph.removeConnection(stationA, stationB)) {
            System.out.println("Connection removed: " + stationA + " <-> " + stationB);
        } else {
            System.out.println("Unable to remove connection. Ensure both stations exist and the connection exists.");
        }
    }

    /**
     * Prompts the user to find a route between two stations.
     */
    private static void findRoute(Graph graph, Scanner scanner) {
        System.out.print("Enter starting station: ");
        String start = scanner.nextLine().trim();

        System.out.print("Enter destination station: ");
        String destination = scanner.nextLine().trim();

        if (start.isEmpty() || destination.isEmpty()) {
            System.out.println("Station names cannot be empty.");
            return;
        }

        if (!graph.hasStation(start) || !graph.hasStation(destination)) {
            System.out.println("One or both stations do not exist in the graph.");
            return;
        }

        List<String> route = graph.findRoute(start, destination);

        if (route.isEmpty()) {
            System.out.println("No route exists between " + start + " and " + destination + ".");
            return;
        }

        if (route.size() == 1) {
            System.out.println("Start and destination are the same station.");
            System.out.println("Route: " + start);
            System.out.println("Number of connections: 0");
            System.out.println("Stations visited: 1");
        } else {
            System.out.println("Route found:");
            System.out.println(String.join(" -> ", route));
            System.out.println("Number of connections: " + (route.size() - 1));
            System.out.println("Stations visited: " + route.size());
        }
    }

    /**
     * Initialises the graph with a simplified but logically consistent
     * subset of the Klang Valley MRT/LRT network.
     *
     * The subset includes:
     * - Part of the Kelana Jaya LRT line
     * - Part of the Ampang/Sri Petaling LRT line
     * - Part of the KL Monorail line
     */
    private static void initialiseSampleData(Graph graph) {
        // Kelana Jaya Line stations
        String[] kelanaJayaStations = {
            "Ampang Park", "KLCC", "Kampung Baru", "Dang Wangi",
            "Masjid Jamek", "Pasar Seni", "KL Sentral"
        };

        // Ampang/Sri Petaling Line stations
        String[] ampangLineStations = {
            "Sentul Timur", "Sentul", "Titiwangsa", "PWTC",
            "Sultan Ismail", "Bandaraya", "Plaza Rakyat",
            "Hang Tuah", "Pudu", "Chan Sow Lin"
        };

        // KL Monorail Line stations
        String[] monorailStations = {
            "Chow Kit", "Medan Tuanku", "Bukit Nanas", "Raja Chulan",
            "Bukit Bintang", "Imbi", "Maharajalela", "Tun Sambanthan"
        };

        for (String station : kelanaJayaStations) {
            graph.addStation(station);
        }

        for (String station : ampangLineStations) {
            graph.addStation(station);
        }

        for (String station : monorailStations) {
            graph.addStation(station);
        }

        // Kelana Jaya Line connections
        addConnectionSilently(graph, "Ampang Park", "KLCC");
        addConnectionSilently(graph, "KLCC", "Kampung Baru");
        addConnectionSilently(graph, "Kampung Baru", "Dang Wangi");
        addConnectionSilently(graph, "Dang Wangi", "Masjid Jamek");
        addConnectionSilently(graph, "Masjid Jamek", "Pasar Seni");
        addConnectionSilently(graph, "Pasar Seni", "KL Sentral");

        // Ampang/Sri Petaling Line connections
        addConnectionSilently(graph, "Sentul Timur", "Sentul");
        addConnectionSilently(graph, "Sentul", "Titiwangsa");
        addConnectionSilently(graph, "Titiwangsa", "PWTC");
        addConnectionSilently(graph, "PWTC", "Sultan Ismail");
        addConnectionSilently(graph, "Sultan Ismail", "Bandaraya");
        addConnectionSilently(graph, "Bandaraya", "Masjid Jamek");
        addConnectionSilently(graph, "Masjid Jamek", "Plaza Rakyat");
        addConnectionSilently(graph, "Plaza Rakyat", "Hang Tuah");
        addConnectionSilently(graph, "Hang Tuah", "Pudu");
        addConnectionSilently(graph, "Pudu", "Chan Sow Lin");

        // KL Monorail Line connections
        addConnectionSilently(graph, "Titiwangsa", "Chow Kit");
        addConnectionSilently(graph, "Chow Kit", "Medan Tuanku");
        addConnectionSilently(graph, "Medan Tuanku", "Bukit Nanas");
        addConnectionSilently(graph, "Bukit Nanas", "Raja Chulan");
        addConnectionSilently(graph, "Raja Chulan", "Bukit Bintang");
        addConnectionSilently(graph, "Bukit Bintang", "Imbi");
        addConnectionSilently(graph, "Imbi", "Hang Tuah");
        addConnectionSilently(graph, "Hang Tuah", "Maharajalela");
        addConnectionSilently(graph, "Maharajalela", "Tun Sambanthan");
        addConnectionSilently(graph, "Tun Sambanthan", "KL Sentral");
    }

    /**
     * Helper method used during initialisation so that no confirmation
     * message is printed for every sample station or connection.
     */
    private static void addConnectionSilently(Graph graph, String stationA, String stationB) {
        graph.addConnection(stationA, stationB);
    }
}
