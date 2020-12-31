import java.util.LinkedList;
import java.util.Queue;

public class PercolationBFS extends PercolationDFSFast {
    /**
     * Initialize a grid so that all cells are blocked.
     * Calls the constructor from PercolationDFSFast
     * @param n is the size of the simulated (square) grid
     */
    private Queue<Integer> holder;
    public PercolationBFS(int n) {
        super(n);
        holder = new LinkedList<>();
    }

    /**
     * don't do anything if the site at (row,col) is out of bounds, full, or already open
     * otherwise, mark the site as full, and add a value corresponding to the site into a Queue
     * Dequeue a cell, check the cell's four neighbors
     * If the neighboring cell is open and not full, it should be marked as full and enqueued
     * @param row is the row coordinate of the cell being checked/marked
     * @param col is teh column coordinate of the cell being checked/marked
     */
    public void dfs(int row, int col) {
        // out of bounds?
        if (! inBounds(row,col)) return;

        // full or NOT open, don't process
        if (isFull(row, col) || !isOpen(row, col))
            return;

        myGrid[row][col] = FULL;
        holder.add(row*myGrid.length+col);
        int value = -1;
        while(holder.size() > 0)
        {
            value = holder.remove();
            int newRow = value/myGrid.length;
            int newCol= value%myGrid.length;
            int[] rarray = {-1,1,0,0};
            int[] carray = {0,0,1,-1};
            for(int i = 0; i < rarray.length; i++)
            {
                if(inBounds(newRow+rarray[i],newCol+carray[i]) && !isFull(newRow+rarray[i],newCol+carray[i]) && isOpen(newRow+rarray[i],newCol+carray[i]))
                {
                    myGrid[newRow+rarray[i]][newCol+carray[i]] = FULL;
                    holder.add(((newRow+rarray[i]))*myGrid.length+(newCol+carray[i]));
                }
            }
        }

    }
}
