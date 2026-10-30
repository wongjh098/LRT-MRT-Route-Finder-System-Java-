import java.util.*;

/**
 * Graph class representing an unweighted, undirected MRT/LRT network
 * using an adjacency list.
 *
 * The adjacency list is implemented as:
 * HashMap<String, ArrayList<String>>
 */
public class Graph {

    private final Map<String, ArrayList<String>> adjacencyList;

    /**
     * Creates an empty graph.
     */
    public Graph() {
        adjacencyList = new HashMap<>();
    }

    /**
     * Adds a new station to the graph.
     *
     * @param station the station name to add
     * @return true if the station was added, false if it already exists
     */
    public boolean addStation(String station) {
        if (station == null || station.trim().isEmpty()) {
            return false;
        }

        station = station.trim();

        if (adjacencyList.containsKey(station)) {
            return false;
        }

        adjacencyList.put(station, new ArrayList<>());
        return true;
    }

    /**
     * Removes a station and all incident connections from the graph.
     *
     * @param station the station name to remove
     * @return true if the station was removed, false if it did not exist
     */
    public boolean removeStation(String station) {
        if (station == null || !adjacencyList.containsKey(station)) {
            return false;
        }

        adjacencyList.remove(station);

        // Remove the station from every other adjacency list
        for (ArrayList<String> neighbours : adjacencyList.values()) {
            neighbours.remove(station);
        }

        return true;
    }

    /**
     * Adds an undirected connection between two existing stations.
     *
     * @param stationA first station
     * @param stationB second station
     * @return true if the connection was added, false otherwise
     */
    public boolean addConnection(String stationA, String stationB) {
        if (stationA == null || stationB == null) {
            return false;
        }

        stationA = stationA.trim();
        stationB = stationB.trim();

        if (!adjacencyList.containsKey(stationA) || !adjacencyList.containsKey(stationB)) {
            return false;
        }

        if (stationA.equals(stationB)) {
            return false;
        }

        if (adjacencyList.get(stationA).contains(stationB)) {
            return false; // duplicate connection
        }

        adjacencyList.get(stationA).add(stationB);
        adjacencyList.get(stationB).add(stationA);
        return true;
    }

    /**
     * Removes an undirected connection between two existing stations.
     *
     * @param stationA first station
     * @param stationB second station
     * @return true if the connection was removed, false otherwise
     */
    public boolean removeConnection(String stationA, String stationB) {
        if (stationA == null || stationB == null) {
            return false;
        }

        stationA = stationA.trim();
        stationB = stationB.trim();

        if (!adjacencyList.containsKey(stationA) || !adjacencyList.containsKey(stationB)) {
            return false;
        }

        boolean removedA = adjacencyList.get(stationA).remove(stationB);
        boolean removedB = adjacencyList.get(stationB).remove(stationA);

        return removedA || removedB;
    }

    /**
     * Checks whether a station exists in the graph.
     *
     * @param station station name
     * @return true if the station exists
     */
    public boolean hasStation(String station) {
        return station != null && adjacencyList.containsKey(station.trim());
    }

    /**
     * Finds the shortest route between two stations using Breadth First Search.
     *
     * @param start       starting station
     * @param destination destination station
     * @return list of station names representing the route, or an empty list if no route exists
     */
    public List<String> findRoute(String start, String destination) {
        if (start == null || destination == null) {
            return Collections.emptyList();
        }

        start = start.trim();
        destination = destination.trim();

        if (!adjacencyList.containsKey(start) || !adjacencyList.containsKey(destination)) {
            return Collections.emptyList();
        }

        if (start.equals(destination)) {
            return Collections.singletonList(start);
        }

        Queue<String> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();
        Map<String, String> parent = new HashMap<>();

        queue.add(start);
        visited.add(start);
        parent.put(start, null);

        while (!queue.isEmpty()) {
            String current = queue.poll();

            for (String neighbour : adjacencyList.get(current)) {
                if (!visited.contains(neighbour)) {
                    visited.add(neighbour);
                    parent.put(neighbour, current);
                    queue.add(neighbour);

                    if (neighbour.equals(destination)) {
                        return reconstructRoute(parent, start, destination);
                    }
                }
            }
        }

        return Collections.emptyList();
    }

    /**
     * Reconstructs the route from start to destination using the parent map.
     *
     * @param parent      parent map created during BFS
     * @param start       starting station
     * @param destination destination station
     * @return route as a list of stations
     */
    private List<String> reconstructRoute(Map<String, String> parent,
                                          String start,
                                          String destination) {
        LinkedList<String> route = new LinkedList<>();
        String current = destination;

        while (current != null) {
            route.addFirst(current);
            current = parent.get(current);
        }

        return route;
    }

    /**
     * Displays the entire graph in a readable adjacency list format.
     */
    public void displayGraph() {
        System.out.println("Current MRT/LRT Graph (Adjacency List):");

        List<String> stationNames = new ArrayList<>(adjacencyList.keySet());
        Collections.sort(stationNames);

        for (String station : stationNames) {
            List<String> neighbours = new ArrayList<>(adjacencyList.get(station));
            Collections.sort(neighbours);

            if (neighbours.isEmpty()) {
                System.out.println(station + " -> (no connections)");
            } else {
                System.out.println(station + " -> " + String.join(", ", neighbours));
            }
        }
    }
}
