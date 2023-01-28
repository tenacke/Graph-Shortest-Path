import java.io.*;
import java.util.*;

public class project4 {
    private final BufferedReader input;
    private final BufferedWriter output;
    private final Map<String, Node> nodes;
    private final Set<String> flags;
    private int flagSize;
    private String start;
    private String end;
    private String flagStart;
    private int pathLength;
    private int flagLength;

    public project4(String input, String output) throws IOException {
        this.input = new BufferedReader(new FileReader(input));
        this.output = new BufferedWriter(new FileWriter(output));
        this.nodes = new HashMap<>();
        this.flags = new HashSet<>();
    }

    private void readInputs() throws IOException { // input reading with buffered-reader
//        long a = System.nanoTime();
        int size = Integer.parseInt(input.readLine());
        flagSize = Integer.parseInt(input.readLine());
        String[] startLine = input.readLine().split(" ");
        start = startLine[0]; end = startLine[1];
        String[] flags = input.readLine().split(" ");
        flagStart = flags[0];
        Collections.addAll(this.flags, flags);
        for (int i = 0; i < size; i++) { // for the node
            String[] newLine = input.readLine().split(" ");
            String name = newLine[0];
            Node current = nodes.get(name);
            if (current == null){ // if the node is already created we don't need to create again
                current = new Node(name, this.flags.contains(name));
                nodes.put(name, current);
            }
            for (int j = 1; j <= newLine.length/2; j++) { // for its neighbours
                String neighbourName = newLine[2*j-1];
                Integer weight = Integer.valueOf(newLine[2*j]);
                Node neighbour = nodes.get(neighbourName);
                if (neighbour == null){ // if the node is already created we don't need to create again
                    neighbour = new Node(neighbourName, this.flags.contains(neighbourName));
                    nodes.put(neighbourName, neighbour);
                }
                current.addNeighbour(neighbourName, weight); // add neighbour both sides its undirected graph
                neighbour.addNeighbour(name, weight);
            }
        }
//        System.out.println("Initialization: " + (double) (System.nanoTime() - a)/1000000000L);
    }

    private int dijkstra(){ // First part initialization
        Map<String, Integer> distances = new HashMap<>();
        Set<String> visited = new HashSet<>();
        Heap<HeapObject> queue = new Heap<>(Comparator.comparing(HeapObject::priority));
        distances.put(start, 0);
        queue.offer(new HeapObject(start, 0));
        while (!queue.isEmpty()){ // dequeue part started
            String current = queue.poll().key();
            if (visited.contains(current)) // its already visited that means we already found the shortest path to that node
                continue;
            int currentWeight = distances.get(current);
            if (current.equals(end)) // we found sink
                return currentWeight;
            visited.add(current);
            Node currentNode = nodes.get(current);
            assert currentNode != null;
            Iterator<String> neighbours = currentNode.getNeighbours();
            Iterator<Integer> weights = currentNode.getWeights();
            while (neighbours.hasNext()){ // iterate through neighbours
                String neighbour = neighbours.next();
                int weight = weights.next() + currentWeight;
                if (!visited.contains(neighbour) ){ // if it's not visited and weight is smaller than where in the map
                    if (weight < distances.getOrDefault(neighbour, Integer.MAX_VALUE)){
                        distances.put(neighbour, weight);
                        queue.offer(new HeapObject(neighbour, weight)); // no matter what add to the queue
                    }
                }
            }
        }
        return distances.getOrDefault(end, -1); // if end is not in the map, so we cant find it, impossible case
    }

    private int flags(){ // Flags part initialization
        Map<String, Integer> distances = new HashMap<>();
        Set<String> visitedFlags = new HashSet<>();
        Heap<HeapObject> queue = new Heap<>(Comparator.comparingInt(HeapObject::priority));
        distances.put(flagStart, 0);
        queue.offer(new HeapObject(flagStart, 0));
        int finalPathLength = 0;
        while (!queue.isEmpty() && visitedFlags.size() != flagSize){ // dequeue started
            String current = queue.poll().key();
            Node currentNode = nodes.get(current);
            int currentDistance = distances.get(current);
            if (currentNode.isFlag()){ // if its flag do some operations
                if (visitedFlags.contains(current)) // its already visited
                    continue;
                else { // add the final length we found one flag
                    visitedFlags.add(current);
                    finalPathLength += currentDistance;
//                    System.out.println(current + " " + currentDistance + " " + finalPathLength);
                }
            }
            Iterator<String> neighbours = currentNode.getNeighbours();
            Iterator<Integer> weights = currentNode.getWeights();
            while (neighbours.hasNext()){ // iterate through neighbours
                String neighbour = neighbours.next();
                int weight = weights.next();
                if (visitedFlags.contains(neighbour)) // this one is already visited flag
                    continue;
                if (!currentNode.isFlag()) // make sure to add the distance of the previous node if it's not flag
                    weight += distances.get(current);
                if (weight < distances.getOrDefault(neighbour, Integer.MAX_VALUE)){ // check the distance in the map
                    distances.put(neighbour, weight);
                    queue.offer(new HeapObject(neighbour, weight));
                }
            }
        }
        if (visitedFlags.size() != flagSize) // this means there are some unvisited flags remaining, so it is an impossible case
            return -1;
        return finalPathLength;
    }

    private record HeapObject(String key, int priority){} // simple priority object for the heap

    public static void main(String[] args) throws IOException {
        project4 project4 = new project4(args[0], args[1]);
        project4.readInputs();
        project4.pathLength = project4.dijkstra(); // runs first part
        project4.flagLength = project4.flags(); // runs second part
//        System.out.println(project4.flagLength);
        project4.output.write(project4.pathLength + "\n" + project4.flagLength + "\n");
        project4.output.close();
    }
}
