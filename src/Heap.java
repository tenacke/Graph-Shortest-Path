import java.util.Arrays;
import java.util.Comparator;

public class Heap<T> {
    private final int DEFAULT_CAPACITY = 100;
    private Object[] heapArray;
    private Comparator<? super T> comparator;
    private int length;

    public Heap(Comparator<? super T> comparator) { // rewritten heap due to unsolvable bugs
        heapArray = new Object[DEFAULT_CAPACITY];
        length = 0;
        this.comparator = comparator;
    }

    private void percolateDown(int hole){ // percolating the bubble towards the leafs
        int child;
        Object temp = heapArray[hole];
        for (; 2* hole <= length; hole = child){
            child = hole * 2;
            if (child != length && comparator.compare((T) heapArray[child+1], (T) heapArray[child]) < 0)
                child++;
            if (comparator.compare((T) heapArray[child], (T) temp) < 0){
                heapArray[hole] = heapArray[child];
            }
            else break;
        }
        heapArray[hole] = temp;
    }

    public boolean isEmpty(){
        return length == 0;
    }

    public void offer(T elem){ // adding operation
        assert elem != null;
        if (length == heapArray.length-1)
            heapArray = Arrays.copyOf(heapArray, 2*heapArray.length + 1);

        int hole = ++length;
        for (heapArray[0] = elem; comparator.compare(elem, (T) heapArray[hole/2]) < 0; hole/=2) {
            heapArray[hole] = heapArray[hole / 2];
        }
        heapArray[hole] = elem;
    }

    public T poll(){ // deleteMin operation
        if (isEmpty())
            return null;
        T elem = (T) heapArray[1];
        heapArray[1] = heapArray[length--];
        percolateDown(1);
        return elem;
    }
}
