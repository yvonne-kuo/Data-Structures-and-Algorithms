public class PercolationUF implements IPercolate {
    private boolean[][] myGrid; //boolean 2D array indicating if the cell is open
    private int myOpenCount;    //the number of cells open
    private IUnionFind myFinder;//a IUnionFind object to keep track of full and open cells
    private final int VTOP;     //a value representing the top of the grid
    private final int VBOTTOM;  //a value representing the bottom of the grid

    /**
     * initialize all instance variables: all values of the 2D boolean array should be false
     * number of cells open should be 0
     * myFinder references the IUnionFind object
     * set the values for VTOP and VBOTTOM
     * @param finder the IUnionFind object used to initialize a global instance variable to keep track of full and open cells
     * @param size the size of the grid
     */
    public PercolationUF(IUnionFind finder,int size)
    {
        myGrid = new boolean[size][size];
        myOpenCount = 0;
        finder.initialize((size*size)+2);
        myFinder = finder;
        VTOP = size*size;
        VBOTTOM = (size*size)+1;
    }

    /**
     * if the cell at (row,col) is out of bounds or already open, do nothing
     * otherwise, open the cell by setting its cell site as true
     * if the cell is at the very top or very bottom of the grid, merge it with VTOP or VBOTTOM
     * merge neighboring cells that are open
     * @param row row index in range [0,N-1]
     * @param col column index in range [0,N-1]
     */
    public void open(int row, int col) {
        if (! inBounds(row,col)) {
            throw new IndexOutOfBoundsException(
                    String.format("(%d,%d) not in bounds", row,col));
        }
        if(isOpen(row,col))
        {
            return;
        }
        myGrid[row][col] = true;
        myOpenCount++;
        if(row == 0)
        {
            myFinder.union(getIndex(row,col),VTOP);
        }
        if(row == myGrid.length-1)
        {
            myFinder.union(getIndex(row,col),VBOTTOM);
        }
        int[] rarray = {-1,1,0,0};
        int[] carray = {0,0,1,-1};
        for(int i = 0; i < rarray.length; i++) {
            if (inBounds(row + rarray[i], col + carray[i]) && isOpen(row + rarray[i], col + carray[i])) {
                myFinder.union(getIndex(row,col),getIndex(row + rarray[i],col + carray[i]));
            }
        }
    }

    /**
     * if cell at (row,col) is out of bounds, don't do anything
     * otherwise, return if the cell at (row,col) is open
     * @param row row index in range [0,N-1]
     * @param col col index in range [0,N-1]
     * @return if the indicated cell is open
     */

    @Override
    public boolean isOpen(int row, int col) {
        if (! inBounds(row,col)) {
            throw new IndexOutOfBoundsException(
                    String.format("(%d,%d) not in bounds", row,col));
        }
        return myGrid[row][col];
    }

    /**
     * if the cell at (row,col) is out of bounds, don't do anything
     * otherwise, check if the cell isFull by seeing if it's been merged with a set containing VTOP
     * @param row row index in range [0,N-1]
     * @param col column index in range [0,N-1]
     * @return boolean indicating whether the cell at (row,col) is full/connected to VTOP
     */
    @Override
    public boolean isFull(int row, int col) {
        if (! inBounds(row,col)) {
            throw new IndexOutOfBoundsException(
                    String.format("(%d,%d) not in bounds", row,col));
        }
        return myFinder.connected(getIndex(row,col),VTOP);
    }

    /**
     * creates a value associated with the cell at (row,col) by using the equation: value = row*myGrid.length+col
     * @param row integer representing the row on the 2D array where the cell is located
     * @param col integer representing the column on the 2D array where the cell is located
     * @return an associated int representing the cell at (row,col)
     */

    public int getIndex(int row, int col)
    {
        return (row*myGrid.length)+col;
    }

    /**
     * check if the grid percolates by seeing if VTOP and VBOTTOM are connected/in the same set
     * @return if the conditions are met for the system to percolate: VTOP and VBOTTOM are connected
     */

    @Override
    public boolean percolates() {
        return myFinder.connected(VTOP,VBOTTOM);
    }

    /**
     * @return an int representing the total number of cells that have been opened
     */
    @Override
    public int numberOfOpenSites() {
        return myOpenCount;
    }

    /**
     * check to see if the cell at (row, col) is within the bounds of the grid
     * @param row int representing the row on the 2D array of the cell
     * @param col int representing the col on the 2D array of the cell
     * @return whether the cell exists within the bounds of the grid
     */
    protected boolean inBounds(int row, int col) {
        if (row < 0 || row >= myGrid.length) return false;
        if (col < 0 || col >= myGrid[0].length) return false;
        return true;
    }
}
