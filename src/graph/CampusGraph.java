package graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * CampusGraph represents the physical layout of a university campus as an
 * undirected graph using an Adjacency List representation.
 * 
 * Vertices: Campus locations (e.g., Library, Canteen, Main Gate).
 * Edges: Bidirectional roads, paths, or walkways connecting locations.
 * 
 * Key Features:
 *  - Dynamic addition and removal of campus locations (vertices)
 *  - Dynamic addition and removal of campus paths/roads (edges)
 *  - Breadth-First Search (BFS) using a custom FIFO GraphQueue
 *  - Depth-First Search (DFS) using recursion
 *  - Unweighted shortest route discovery between two campus locations
 *  - Robust defensive validation against duplicates, null/blank inputs, missing locations
 * 
 * Course: CIT300 Data Structures and Algorithms
 * Project: University Student Record and Campus Route Management System
 * Assigned Member: IF Hasna
 * Student ID: 23DA2-1154
 * Component: Campus Graph Component
 */
public class CampusGraph {

    // Map storing location nodes; LinkedHashMap preserves insertion order for display
    private final Map<String, GraphNode> nodes;

    /**
     * Initializes an empty CampusGraph.
     */
    public CampusGraph() {
        this.nodes = new LinkedHashMap<>();
    }

    // =========================================================================
    // VERTEX (LOCATION) OPERATIONS
    // =========================================================================

    /**
     * Adds a new campus location to the graph.
     *
     * @param location The name of the campus location to add.
     * @return true if successfully added; false if invalid or duplicate.
     */
    public boolean addLocation(String location) {
        if (location == null || location.trim().isEmpty()) {
            System.out.println("[ERROR] Location name cannot be null or empty.");
            return false;
        }

        String cleanName = location.trim();
        String lookupKey = cleanName.toLowerCase();

        if (nodes.containsKey(lookupKey)) {
            System.out.println("[WARNING] Location '" + cleanName + "' already exists in the campus network.");
            return false;
        }

        nodes.put(lookupKey, new GraphNode(cleanName));
        return true;
    }

    /**
     * Removes a campus location from the graph.
     * Automatically removes all incident connections pointing to this location
     * from all other neighboring locations to preserve graph integrity.
     *
     * @param location The name of the campus location to remove.
     * @return true if successfully removed; false if location was not found.
     */
    public boolean removeLocation(String location) {
        if (location == null || location.trim().isEmpty()) {
            System.out.println("[ERROR] Location name cannot be null or empty.");
            return false;
        }

        String cleanName = location.trim();
        String lookupKey = cleanName.toLowerCase();

        GraphNode nodeToRemove = nodes.get(lookupKey);
        if (nodeToRemove == null) {
            System.out.println("[ERROR] Cannot remove: Location '" + cleanName + "' does not exist in the campus network.");
            return false;
        }

        // Clean up connections from all neighbors pointing to this node
        for (String neighborName : nodeToRemove.getNeighbors()) {
            GraphNode neighborNode = nodes.get(neighborName.toLowerCase());
            if (neighborNode != null) {
                neighborNode.removeNeighbor(nodeToRemove.getName());
            }
        }

        // Remove the vertex itself from the graph map
        nodes.remove(lookupKey);
        System.out.println("[SUCCESS] Location '" + cleanName + "' and all associated connections removed.");
        return true;
    }

    /**
     * Checks if a specific campus location exists in the graph.
     *
     * @param location The name of the location to check.
     * @return true if the location exists, false otherwise.
     */
    public boolean containsLocation(String location) {
        if (location == null || location.trim().isEmpty()) {
            return false;
        }
        return nodes.containsKey(location.trim().toLowerCase());
    }

    /**
     * Returns the total count of campus locations (vertices) in the graph.
     *
     * @return Total vertex count.
     */
    public int getLocationCount() {
        return nodes.size();
    }

    /**
     * Returns a list of all campus location names currently in the graph.
     *
     * @return List of location names.
     */
    public List<String> getAllLocations() {
        List<String> list = new ArrayList<>();
        for (GraphNode node : nodes.values()) {
            list.add(node.getName());
        }
        return list;
    }

    // =========================================================================
    // EDGE (CONNECTION / ROAD) OPERATIONS
    // =========================================================================

    /**
     * Adds an undirected connection (bidirectional road) between two campus locations.
     * When connecting A and B, roads are created for both A -> B and B -> A.
     *
     * @param location1 The first campus location.
     * @param location2 The second campus location.
     * @return true if connection added; false if invalid, missing, or duplicate.
     */
    public boolean addConnection(String location1, String location2) {
        if (location1 == null || location1.trim().isEmpty() || location2 == null || location2.trim().isEmpty()) {
            System.out.println("[ERROR] Connection locations cannot be null or empty.");
            return false;
        }

        String clean1 = location1.trim();
        String clean2 = location2.trim();

        if (clean1.equalsIgnoreCase(clean2)) {
            System.out.println("[ERROR] Cannot connect location '" + clean1 + "' to itself (no self-loops).");
            return false;
        }

        GraphNode node1 = nodes.get(clean1.toLowerCase());
        GraphNode node2 = nodes.get(clean2.toLowerCase());

        if (node1 == null || node2 == null) {
            String missing = (node1 == null && node2 == null)
                    ? clean1 + " and " + clean2
                    : (node1 == null ? clean1 : clean2);
            System.out.println("[ERROR] Cannot add connection: Missing location(s): [" + missing + "].");
            return false;
        }

        if (node1.hasNeighbor(node2.getName())) {
            System.out.println("[WARNING] Connection between '" + node1.getName() + "' and '" + node2.getName() + "' already exists.");
            return false;
        }

        // Undirected graph: add edge in both directions
        node1.addNeighbor(node2.getName());
        node2.addNeighbor(node1.getName());
        return true;
    }

    /**
     * Removes an undirected connection (road) between two campus locations.
     *
     * @param location1 The first campus location.
     * @param location2 The second campus location.
     * @return true if connection removed; false if invalid or not connected.
     */
    public boolean removeConnection(String location1, String location2) {
        if (location1 == null || location1.trim().isEmpty() || location2 == null || location2.trim().isEmpty()) {
            System.out.println("[ERROR] Connection locations cannot be null or empty.");
            return false;
        }

        String clean1 = location1.trim();
        String clean2 = location2.trim();

        GraphNode node1 = nodes.get(clean1.toLowerCase());
        GraphNode node2 = nodes.get(clean2.toLowerCase());

        if (node1 == null || node2 == null) {
            System.out.println("[ERROR] Cannot remove connection: One or both locations do not exist.");
            return false;
        }

        if (!node1.hasNeighbor(node2.getName())) {
            System.out.println("[ERROR] Cannot remove: No direct road exists between '" + node1.getName() + "' and '" + node2.getName() + "'.");
            return false;
        }

        node1.removeNeighbor(node2.getName());
        node2.removeNeighbor(node1.getName());
        System.out.println("[SUCCESS] Removed connection between '" + node1.getName() + "' and '" + node2.getName() + "'.");
        return true;
    }

    /**
     * Checks if a direct connection (road) exists between two locations.
     *
     * @param location1 First location.
     * @param location2 Second location.
     * @return true if directly connected; false otherwise.
     */
    public boolean hasConnection(String location1, String location2) {
        if (location1 == null || location2 == null) return false;
        GraphNode node1 = nodes.get(location1.trim().toLowerCase());
        GraphNode node2 = nodes.get(location2.trim().toLowerCase());
        if (node1 == null || node2 == null) return false;
        return node1.hasNeighbor(node2.getName());
    }

    /**
     * Returns the total count of unique undirected roads/connections in the campus network.
     *
     * @return Total edge count.
     */
    public int getConnectionCount() {
        int totalDegree = 0;
        for (GraphNode node : nodes.values()) {
            totalDegree += node.getDegree();
        }
        return totalDegree / 2; // Handshaking lemma for undirected graphs
    }

    // =========================================================================
    // DISPLAY OPERATIONS
    // =========================================================================

    /**
     * Displays the complete campus route network as an Adjacency List.
     */
    public void displayConnections() {
        System.out.println("Campus Network:");
        if (nodes.isEmpty()) {
            System.out.println("  (Campus Network is currently empty - no locations added)");
            return;
        }
        for (GraphNode node : nodes.values()) {
            List<String> neighbors = node.getNeighbors();
            if (neighbors.isEmpty()) {
                System.out.println(node.getName() + " -> (No direct connections)");
            } else {
                System.out.println(node.getName() + " -> " + String.join(", ", neighbors));
            }
        }
    }

    /**
     * Convenience alias for displayConnections().
     */
    public void displayCampusNetwork() {
        displayConnections();
    }

    /**
     * Displays the direct neighbors connected to a specific campus location.
     *
     * @param location The location whose neighbors should be displayed.
     */
    public void displayNeighbours(String location) {
        if (location == null || location.trim().isEmpty()) {
            System.out.println("[ERROR] Location name cannot be null or empty.");
            return;
        }

        GraphNode node = nodes.get(location.trim().toLowerCase());
        if (node == null) {
            System.out.println("[ERROR] Location '" + location.trim() + "' does not exist in the campus network.");
            return;
        }

        List<String> neighbors = node.getNeighbors();
        if (neighbors.isEmpty()) {
            System.out.println("[" + node.getName() + "] has no direct connections (isolated location).");
        } else {
            System.out.println("Connected locations from [" + node.getName() + "]: " + String.join(", ", neighbors));
        }
    }

    /**
     * Retrieves the list of direct neighbor names for a given location.
     *
     * @param location Location name.
     * @return List of neighboring location names, or empty list if missing/none.
     */
    public List<String> getNeighbours(String location) {
        if (location == null) return Collections.emptyList();
        GraphNode node = nodes.get(location.trim().toLowerCase());
        if (node == null) return Collections.emptyList();
        return node.getNeighbors();
    }

    // =========================================================================
    // GRAPH TRAVERSALS (BFS & DFS)
    // =========================================================================

    /**
     * Performs a Breadth-First Search (BFS) traversal starting from a campus location.
     * Uses our custom singly-linked GraphQueue implementation without external libraries.
     *
     * Time Complexity: O(V + E)
     * Space Complexity: O(V)
     *
     * @param startingLocation Location where traversal begins.
     * @return List of location names in BFS order.
     */
    public List<String> bfs(String startingLocation) {
        if (startingLocation == null || startingLocation.trim().isEmpty()) {
            System.out.println("[WARNING] BFS Traversal failed: Starting location cannot be null or empty.");
            return Collections.emptyList();
        }

        String cleanStart = startingLocation.trim();
        GraphNode startNode = nodes.get(cleanStart.toLowerCase());
        if (startNode == null) {
            System.out.println("[WARNING] BFS Traversal failed: Starting location '" + cleanStart + "' does not exist in the campus network.");
            return Collections.emptyList();
        }

        List<String> traversalOrder = new ArrayList<>();
        Set<String> visited = new HashSet<>();
        GraphQueue<GraphNode> queue = new GraphQueue<>();

        visited.add(startNode.getName().toLowerCase());
        queue.enqueue(startNode);

        while (!queue.isEmpty()) {
            GraphNode current = queue.dequeue();
            traversalOrder.add(current.getName());

            for (String neighborName : current.getNeighbors()) {
                GraphNode neighborNode = nodes.get(neighborName.toLowerCase());
                if (neighborNode != null && !visited.contains(neighborNode.getName().toLowerCase())) {
                    visited.add(neighborNode.getName().toLowerCase());
                    queue.enqueue(neighborNode);
                }
            }
        }

        return traversalOrder;
    }

    /**
     * Performs a Depth-First Search (DFS) traversal starting from a campus location
     * using recursion.
     *
     * Time Complexity: O(V + E)
     * Space Complexity: O(V)
     *
     * @param startingLocation Location where traversal begins.
     * @return List of location names in DFS order.
     */
    public List<String> dfs(String startingLocation) {
        if (startingLocation == null || startingLocation.trim().isEmpty()) {
            System.out.println("[WARNING] DFS Traversal failed: Starting location cannot be null or empty.");
            return Collections.emptyList();
        }

        String cleanStart = startingLocation.trim();
        GraphNode startNode = nodes.get(cleanStart.toLowerCase());
        if (startNode == null) {
            System.out.println("[WARNING] DFS Traversal failed: Starting location '" + cleanStart + "' does not exist in the campus network.");
            return Collections.emptyList();
        }

        List<String> traversalOrder = new ArrayList<>();
        Set<String> visited = new HashSet<>();
        dfsRecursive(startNode, visited, traversalOrder);
        return traversalOrder;
    }

    private void dfsRecursive(GraphNode current, Set<String> visited, List<String> traversalOrder) {
        visited.add(current.getName().toLowerCase());
        traversalOrder.add(current.getName());

        for (String neighborName : current.getNeighbors()) {
            GraphNode neighborNode = nodes.get(neighborName.toLowerCase());
            if (neighborNode != null && !visited.contains(neighborNode.getName().toLowerCase())) {
                dfsRecursive(neighborNode, visited, traversalOrder);
            }
        }
    }

    // =========================================================================
    // ROUTE NAVIGATION / SHORTEST PATH (BFS)
    // =========================================================================

    /**
     * Finds the shortest route (minimum road segments) between two campus locations
     * using Breadth-First Search (BFS) path reconstruction.
     *
     * Directly fulfills integration with Member 1 & 2 for student campus navigation.
     *
     * @param startLocation Starting campus location.
     * @param endLocation Destination campus location.
     * @return Ordered list of locations on the shortest route, or empty list if no path exists.
     */
    public List<String> findShortestPath(String startLocation, String endLocation) {
        if (startLocation == null || endLocation == null) {
            return Collections.emptyList();
        }

        GraphNode startNode = nodes.get(startLocation.trim().toLowerCase());
        GraphNode endNode = nodes.get(endLocation.trim().toLowerCase());

        if (startNode == null || endNode == null) {
            return Collections.emptyList();
        }

        if (startNode.equals(endNode)) {
            return Collections.singletonList(startNode.getName());
        }

        Map<String, String> parentMap = new HashMap<>();
        Set<String> visited = new HashSet<>();
        GraphQueue<GraphNode> queue = new GraphQueue<>();

        visited.add(startNode.getName().toLowerCase());
        queue.enqueue(startNode);

        boolean found = false;

        while (!queue.isEmpty()) {
            GraphNode current = queue.dequeue();

            if (current.equals(endNode)) {
                found = true;
                break;
            }

            for (String neighborName : current.getNeighbors()) {
                GraphNode neighborNode = nodes.get(neighborName.toLowerCase());
                if (neighborNode != null && !visited.contains(neighborNode.getName().toLowerCase())) {
                    visited.add(neighborNode.getName().toLowerCase());
                    parentMap.put(neighborNode.getName().toLowerCase(), current.getName());
                    queue.enqueue(neighborNode);
                }
            }
        }

        if (!found) {
            return Collections.emptyList();
        }

        // Reconstruct path backward from destination to start
        List<String> path = new ArrayList<>();
        String step = endNode.getName();
        while (step != null) {
            path.add(step);
            step = parentMap.get(step.toLowerCase());
        }
        Collections.reverse(path);
        return path;
    }

    /**
     * Checks if a reachable path exists between two locations.
     *
     * @param location1 Starting location.
     * @param location2 Target location.
     * @return true if connected path exists, false otherwise.
     */
    public boolean isConnected(String location1, String location2) {
        List<String> path = findShortestPath(location1, location2);
        return !path.isEmpty();
    }

    /**
     * Resets and clears the entire campus graph.
     */
    public void clear() {
        nodes.clear();
    }

    /**
     * Checks if the campus graph is empty (no locations).
     *
     * @return true if empty, false otherwise.
     */
    public boolean isEmpty() {
        return nodes.isEmpty();
    }
}
