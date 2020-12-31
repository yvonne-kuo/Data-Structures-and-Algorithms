public class PercolationDFSFast extends PercolationDFS {
    /**
     * Initialize a grid so that all cells are blocked.
     * Calls the constructor from PercolationFastDFS
     * @param n is the size of the simulated (square) grid
     */
    public PercolationDFSFast(int n) {
        super(n);
    }

    /**
     * if the site opened is at the first row, or the site is adjacent to a full, open site already, mark the site as full
     * make only one recursive call by breaking out of the loop checking adjacent sites if one full adjacent site is found
     * @param row int corresponding to the row location on the 2D array
     * @param col int corresponding to the column location on the 2D array
     */
    public void updateOnOpen(int row, int col) {
        int[] rarray = {-1,1,0,0};
        int[] carray = {0,0,1,-1};
        for(int i = 0; i < rarray.length; i++)
        {
            if(row == 0 || (inBounds(row+rarray[i],col+carray[i])&&isFull(row+rarray[i],col+carray[i])))
            {
                dfs(row,col);
                break;
            }
        }

    }
}
