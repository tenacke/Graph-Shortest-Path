import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Node {
    private final String name;
    private final List<String> neighbours;
    private final List<Integer> weights;
    private final boolean isFlag;

    public Node(String name, boolean isFlag) { // creating a simple node for the graph
        this.name = name;
        neighbours = new LinkedList<>();
        weights = new LinkedList<>();
        this.isFlag = isFlag;
    }

    public String getName() {
        return name;
    }

    public Iterator<String> getNeighbours() {
        return neighbours.listIterator();
    }

    public Iterator<Integer> getWeights() {
        return weights.listIterator();
    }

    public void addNeighbour(String neighbour, Integer weight){ // neighbour and its edge are added to a linked list here
        neighbours.add(neighbour);
        weights.add(weight);
    }

    public boolean isFlag() {
        return isFlag;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
