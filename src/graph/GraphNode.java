package graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Represents a single campus location (vertex) in the university campus graph.
 * 
 * Each GraphNode maintains its location name and an adjacency list of names of
 * directly connected neighboring campus locations.
 * 
 * Course: CIT300 Data Structures and Algorithms
 * Project: University Student Record and Campus Route Management System
 * Assigned Member: IF Hasna
 * Student ID: 23DA2-1154
 * Component: Campus Graph Component
 */
public class GraphNode {

    private final String name;
    private final List<String> neighbors;

    /**
     * Constructs a GraphNode with the specified location name.
     *
     * @param name The unique name of the campus location.
     * @throws IllegalArgumentException if the name is null or blank.
     */
    public GraphNode(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Location name cannot be null or empty.");
        }
        this.name = name.trim();
        this.neighbors = new ArrayList<>();
    }

    /**
     * Returns the name of this campus location.
     *
     * @return Location name.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns an unmodifiable view of neighboring location names.
     *
     * @return List of connected neighbors.
     */
    public List<String> getNeighbors() {
        return Collections.unmodifiableList(neighbors);
    }

    /**
     * Adds a neighbor connection if it is not already present.
     *
     * @param neighbor The name of the neighbor location to connect.
     * @return true if added, false if already exists or invalid.
     */
    public boolean addNeighbor(String neighbor) {
        if (neighbor == null || neighbor.trim().isEmpty()) {
            return false;
        }
        String cleanNeighbor = neighbor.trim();
        if (cleanNeighbor.equalsIgnoreCase(this.name)) {
            return false; // Prevent self-loop
        }
        for (String existing : neighbors) {
            if (existing.equalsIgnoreCase(cleanNeighbor)) {
                return false; // Duplicate connection
            }
        }
        return neighbors.add(cleanNeighbor);
    }

    /**
     * Removes a neighbor connection.
     *
     * @param neighbor The name of the neighbor location to disconnect.
     * @return true if removed, false if not found.
     */
    public boolean removeNeighbor(String neighbor) {
        if (neighbor == null || neighbor.trim().isEmpty()) {
            return false;
        }
        String cleanNeighbor = neighbor.trim();
        return neighbors.removeIf(n -> n.equalsIgnoreCase(cleanNeighbor));
    }

    /**
     * Checks if this node is directly connected to the specified neighbor.
     *
     * @param neighbor The name of the neighbor location to check.
     * @return true if directly connected, false otherwise.
     */
    public boolean hasNeighbor(String neighbor) {
        if (neighbor == null || neighbor.trim().isEmpty()) {
            return false;
        }
        String cleanNeighbor = neighbor.trim();
        for (String existing : neighbors) {
            if (existing.equalsIgnoreCase(cleanNeighbor)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the degree (number of direct connections) of this location.
     *
     * @return The number of neighbors.
     */
    public int getDegree() {
        return neighbors.size();
    }

    /**
     * Clears all neighbor connections.
     */
    public void clearNeighbors() {
        neighbors.clear();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GraphNode graphNode = (GraphNode) o;
        return name.equalsIgnoreCase(graphNode.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name.toLowerCase());
    }

    @Override
    public String toString() {
        if (neighbors.isEmpty()) {
            return name + " -> (No direct connections)";
        }
        return name + " -> " + String.join(", ", neighbors);
    }
}
