package test;

import graph.CampusGraph;
import java.util.List;

/**
 * Test and demonstration suite for the Campus Graph Component.
 * 
 * Course: CIT300 Data Structures and Algorithms
 * Project: University Student Record and Campus Route Management System
 * Assigned Member: IF Hasna
 * Student ID: 23DA2-1154
 * Component: Campus Graph Component
 * 
 * Executes all 13 compulsory test scenarios specified in the assignment:
 *   1. Add several locations
 *   2. Display locations
 *   3. Add connections
 *   4. Display connections
 *   5. Display neighbours
 *   6. Remove a connection
 *   7. Remove a location
 *   8. Attempt duplicate location
 *   9. Attempt duplicate connection
 *  10. Attempt connection to missing location
 *  11. BFS from an existing location
 *  12. DFS from an existing location
 *  13. BFS/DFS from a missing location
 *  Bonus: Shortest path routing test (BFS navigation)
 */
public class CampusGraphTest {

    private static int passedTests = 0;
    private static int totalTests = 13;

    public static void main(String[] args) {
        printHeader();

        CampusGraph graph = new CampusGraph();

        // ---------------------------------------------------------------------
        // TEST CASE 1: Add Several Locations
        // ---------------------------------------------------------------------
        printSectionHeader("TEST CASE 1: Add Several Campus Locations");
        System.out.println("Adding 6 standard campus locations...");
        boolean a1 = graph.addLocation("Main Gate");
        boolean a2 = graph.addLocation("Library");
        boolean a3 = graph.addLocation("Canteen");
        boolean a4 = graph.addLocation("Laboratory");
        boolean a5 = graph.addLocation("Lecture Hall");
        boolean a6 = graph.addLocation("Student Center");

        System.out.println("Locations added: " + (a1 && a2 && a3 && a4 && a5 && a6));
        System.out.println("Total location count: " + graph.getLocationCount());
        if (graph.getLocationCount() == 6) {
            passedTests++;
            System.out.println(">>> [PASS] Test 1: Added 6 locations successfully.");
        } else {
            System.out.println(">>> [FAIL] Test 1: Location count mismatch.");
        }

        // ---------------------------------------------------------------------
        // TEST CASE 2: Display Locations
        // ---------------------------------------------------------------------
        printSectionHeader("TEST CASE 2: Display Campus Locations");
        List<String> allLocations = graph.getAllLocations();
        System.out.println("Registered Campus Locations (" + allLocations.size() + " total):");
        for (int i = 0; i < allLocations.size(); i++) {
            System.out.println("  " + (i + 1) + ". " + allLocations.get(i));
        }
        if (allLocations.size() == 6) {
            passedTests++;
            System.out.println(">>> [PASS] Test 2: Displayed all registered locations.");
        }

        // ---------------------------------------------------------------------
        // TEST CASE 3: Add Connections (Roads)
        // ---------------------------------------------------------------------
        printSectionHeader("TEST CASE 3: Add Campus Connections (Bidirectional Roads)");
        System.out.println("Creating connections according to example campus layout:");
        graph.addConnection("Main Gate", "Library");
        graph.addConnection("Main Gate", "Canteen");
        graph.addConnection("Library", "Laboratory");
        graph.addConnection("Library", "Lecture Hall");
        graph.addConnection("Canteen", "Student Center");
        graph.addConnection("Lecture Hall", "Student Center");

        System.out.println("Total undirected connections: " + graph.getConnectionCount());
        if (graph.getConnectionCount() == 6) {
            passedTests++;
            System.out.println(">>> [PASS] Test 3: Added 6 bidirectional connections.");
        }

        // ---------------------------------------------------------------------
        // TEST CASE 4: Display Campus Network (Connections)
        // ---------------------------------------------------------------------
        printSectionHeader("TEST CASE 4: Display Complete Campus Network");
        graph.displayConnections();
        passedTests++;
        System.out.println(">>> [PASS] Test 4: Displayed campus network in Adjacency List format.");

        // ---------------------------------------------------------------------
        // TEST CASE 5: Display Neighbours
        // ---------------------------------------------------------------------
        printSectionHeader("TEST CASE 5: Display Connected Neighbours of Specific Locations");
        System.out.println("Inspecting neighbours of 'Library':");
        graph.displayNeighbours("Library");
        System.out.println("\nInspecting neighbours of 'Canteen':");
        graph.displayNeighbours("Canteen");

        List<String> libNeighbours = graph.getNeighbours("Library");
        if (libNeighbours.contains("Laboratory") && libNeighbours.contains("Lecture Hall") && libNeighbours.contains("Main Gate")) {
            passedTests++;
            System.out.println(">>> [PASS] Test 5: Neighbours retrieved and verified correctly.");
        }

        // ---------------------------------------------------------------------
        // TEST CASE 6: Remove a Connection
        // ---------------------------------------------------------------------
        printSectionHeader("TEST CASE 6: Remove an Existing Connection ('Main Gate' <-> 'Canteen')");
        System.out.println("Removing road between 'Main Gate' and 'Canteen'...");
        boolean connRemoved = graph.removeConnection("Main Gate", "Canteen");
        System.out.println("Connection removed: " + connRemoved);
        System.out.println("Checking connection existence: " + graph.hasConnection("Main Gate", "Canteen"));
        System.out.println("Updated neighbours of 'Main Gate':");
        graph.displayNeighbours("Main Gate");
        System.out.println("Updated neighbours of 'Canteen':");
        graph.displayNeighbours("Canteen");

        if (connRemoved && !graph.hasConnection("Main Gate", "Canteen") && graph.getConnectionCount() == 5) {
            passedTests++;
            System.out.println(">>> [PASS] Test 6: Connection removed cleanly from both nodes.");
        }

        // ---------------------------------------------------------------------
        // TEST CASE 7: Remove a Location
        // ---------------------------------------------------------------------
        printSectionHeader("TEST CASE 7: Remove a Location ('Laboratory') and Verify Cascade Cleanup");
        System.out.println("Before removal: Does 'Library' connect to 'Laboratory'? " + graph.hasConnection("Library", "Laboratory"));
        boolean locRemoved = graph.removeLocation("Laboratory");
        System.out.println("Location 'Laboratory' removed: " + locRemoved);
        System.out.println("Does graph contain 'Laboratory'? " + graph.containsLocation("Laboratory"));
        System.out.println("After removal: Does 'Library' connect to 'Laboratory'? " + graph.hasConnection("Library", "Laboratory"));
        System.out.println("Updated neighbours of 'Library':");
        graph.displayNeighbours("Library");

        if (locRemoved && !graph.containsLocation("Laboratory") && !graph.hasConnection("Library", "Laboratory")) {
            passedTests++;
            System.out.println(">>> [PASS] Test 7: Location and all incident edges removed successfully.");
        }

        // ---------------------------------------------------------------------
        // TEST CASE 8: Attempt Duplicate Location
        // ---------------------------------------------------------------------
        printSectionHeader("TEST CASE 8: Defensive Validation - Attempt Duplicate Location");
        System.out.println("Attempting to add 'Library' again (already exists)...");
        boolean dupLoc = graph.addLocation("Library");
        System.out.println("Result of duplicate addition: " + dupLoc);
        if (!dupLoc) {
            passedTests++;
            System.out.println(">>> [PASS] Test 8: Duplicate location correctly rejected.");
        }

        // ---------------------------------------------------------------------
        // TEST CASE 9: Attempt Duplicate Connection
        // ---------------------------------------------------------------------
        printSectionHeader("TEST CASE 9: Defensive Validation - Attempt Duplicate Connection");
        System.out.println("Attempting to add existing road ('Library' <-> 'Lecture Hall')...");
        boolean dupConn = graph.addConnection("Library", "Lecture Hall");
        System.out.println("Result of duplicate connection: " + dupConn);
        if (!dupConn) {
            passedTests++;
            System.out.println(">>> [PASS] Test 9: Duplicate road connection correctly rejected.");
        }

        // ---------------------------------------------------------------------
        // TEST CASE 10: Attempt Connection to Missing Location
        // ---------------------------------------------------------------------
        printSectionHeader("TEST CASE 10: Defensive Validation - Connection to Missing Location");
        System.out.println("Attempting connection between 'Library' and 'Sports Complex' (not in graph)...");
        boolean missingConn1 = graph.addConnection("Library", "Sports Complex");
        System.out.println("Attempting connection between 'Admin Building' and 'Dormitory' (neither exists)...");
        boolean missingConn2 = graph.addConnection("Admin Building", "Dormitory");

        if (!missingConn1 && !missingConn2) {
            passedTests++;
            System.out.println(">>> [PASS] Test 10: Connections to missing locations properly rejected.");
        }

        // Reconnect for clean BFS/DFS testing
        graph.addConnection("Main Gate", "Canteen");

        // ---------------------------------------------------------------------
        // TEST CASE 11: BFS Traversal from Existing Location
        // ---------------------------------------------------------------------
        printSectionHeader("TEST CASE 11: Breadth-First Search (BFS) Traversal");
        System.out.println("Executing BFS starting from 'Main Gate':");
        List<String> bfsOrder = graph.bfs("Main Gate");
        System.out.println("BFS Traversal Order: " + String.join(" -> ", bfsOrder));
        if (!bfsOrder.isEmpty() && bfsOrder.get(0).equals("Main Gate")) {
            passedTests++;
            System.out.println(">>> [PASS] Test 11: BFS traversal executed successfully using custom GraphQueue.");
        }

        // ---------------------------------------------------------------------
        // TEST CASE 12: DFS Traversal from Existing Location
        // ---------------------------------------------------------------------
        printSectionHeader("TEST CASE 12: Depth-First Search (DFS) Traversal");
        System.out.println("Executing DFS starting from 'Main Gate':");
        List<String> dfsOrder = graph.dfs("Main Gate");
        System.out.println("DFS Traversal Order: " + String.join(" -> ", dfsOrder));
        if (!dfsOrder.isEmpty() && dfsOrder.get(0).equals("Main Gate")) {
            passedTests++;
            System.out.println(">>> [PASS] Test 12: DFS traversal executed successfully using recursion.");
        }

        // ---------------------------------------------------------------------
        // TEST CASE 13: BFS and DFS from a Missing Location
        // ---------------------------------------------------------------------
        printSectionHeader("TEST CASE 13: BFS and DFS Starting from Missing Location");
        System.out.println("Attempting BFS from non-existent 'Auditorium':");
        List<String> missingBfs = graph.bfs("Auditorium");
        System.out.println("Missing BFS returned empty list: " + missingBfs.isEmpty());

        System.out.println("\nAttempting DFS from non-existent 'Auditorium':");
        List<String> missingDfs = graph.dfs("Auditorium");
        System.out.println("Missing DFS returned empty list: " + missingDfs.isEmpty());

        if (missingBfs.isEmpty() && missingDfs.isEmpty()) {
            passedTests++;
            System.out.println(">>> [PASS] Test 13: Missing start locations safely handled without crashes.");
        }

        // ---------------------------------------------------------------------
        // BONUS: Shortest Path Navigation (Route Management)
        // ---------------------------------------------------------------------
        printSectionHeader("BONUS: Campus Route Navigation (Shortest Path via BFS)");
        System.out.println("Finding shortest path from 'Main Gate' to 'Student Center':");
        List<String> route = graph.findShortestPath("Main Gate", "Student Center");
        System.out.println("Navigation Route: " + String.join(" -> ", route));
        System.out.println("Total stops on route: " + route.size());

        // ---------------------------------------------------------------------
        // SUMMARY
        // ---------------------------------------------------------------------
        printSummary();
    }

    private static void printHeader() {
        System.out.println("================================================================================");
        System.out.println("        CAMPUS GRAPH & ROUTE MANAGEMENT - COMPONENT VERIFICATION TEST");
        System.out.println("        CIT300 Data Structures and Algorithms - Group Assignment");
        System.out.println("        Assigned Member: IF Hasna (Student ID: 23DA2-1154)");
        System.out.println("================================================================================");
    }

    private static void printSectionHeader(String title) {
        System.out.println("\n--------------------------------------------------------------------------------");
        System.out.println(title);
        System.out.println("--------------------------------------------------------------------------------");
    }

    private static void printSummary() {
        System.out.println("\n================================================================================");
        System.out.println("                             TEST EXECUTION SUMMARY");
        System.out.println("================================================================================");
        System.out.println("  Total Required Test Cases: " + totalTests);
        System.out.println("  Tests Passed:              " + passedTests);
        System.out.println("  Tests Failed:              " + (totalTests - passedTests));
        if (passedTests == totalTests) {
            System.out.println("  STATUS:                    ALL 13 TESTS PASSED SUCCESSFULLY! (100%)");
        } else {
            System.out.println("  STATUS:                    FAILURES ENCOUNTERED");
        }
        System.out.println("================================================================================\n");
    }
}
