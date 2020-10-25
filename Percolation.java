import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private boolean[][] grid;

    //To help determine whether the system percolates
    private WeightedQuickUnionUF sites;

    //To avoid backwash by checking if site is full
    private WeightedQuickUnionUF fullSite;

    private int size;
    private static int emptySites = 0;
    private int topVirtualSite;
    private int bottomVirtualSite;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        this.size = n;
        this.emptySites = 0;
        grid = new boolean[n + 1][n + 1];

        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                grid[row][col] = false;
            }
        }
        grid[0][n] = true;
        grid[n][0] = true;

        if (n <= 0)
            throw new IllegalArgumentException("Enter an n that is greater than 0!");

        //Create one virtual site connected to all top row elements
        //and one connected to all bottom row elements
        sites = new WeightedQuickUnionUF(n * n + 2);
        fullSite = new WeightedQuickUnionUF(n * n + 1);

        //Create virtual sites for better algorithm
        //Allows you to check if virtual site of top cell
        // is connected to virtual site of bottom cell
        topVirtualSite = n * n;
        bottomVirtualSite = n * n + 1;
    }

    //Helper method to check if the given indices are valid
    private void withinBound(int row, int col) {
        if (row < 0 || col < 0 || row >= this.size || col >= this.size) {
            throw new IllegalArgumentException("Index is out of bounds");
        }
    }

    //Convert coordinates to index
    private int CoordtoIndex(int row, int col) {
        //corner cases: Virtual top and bottom sites
        if ((row == 0) && (col == this.size)) {
            int index = this.size * this.size;
            return index;
        }
        if ((col == 0) && (row == this.size)) {
            int index = this.size * this.size + 1;
            return index;
        }
        int index = row * this.size + col;
        return index;
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        withinBound(row, col);
        int index = CoordtoIndex(row, col);

        //If site is already open then return
        if (isOpen(row, col)) {
            return;
        }

        //If site is not open yet, then open
        if (!isOpen(row, col)) {
            grid[row][col] = true;
            emptySites++;

            //opening the neighbor sites:
            // top and bottom rows, up, down, right, left respectively

            //Top row -> Bottom Virtual Site
            if (row == 0) {
                sites.union(index, topVirtualSite);
                fullSite.union(index, topVirtualSite);
            }
            //Bottom row -> Top Virtual Site
            if (row == this.size - 1) {
                sites.union(index, bottomVirtualSite);
            }
            //Up
            if ((row - 1 >= 0) && (isOpen(row - 1, col))) {
                sites.union(index, CoordtoIndex(row - 1, col));
                fullSite.union(index, CoordtoIndex(row - 1, col));
            }
            //Down
            if ((row + 1 < this.size) && (isOpen(row + 1, col))) {
                sites.union(index, CoordtoIndex(row + 1, col));
                fullSite.union(index, CoordtoIndex(row + 1, col));
            }
            //Right
            if ((col + 1 < this.size) && (isOpen(row, col + 1))) {
                sites.union(index, CoordtoIndex(row, col + 1));
                fullSite.union(index, CoordtoIndex(row, col + 1));
            }
            //Left
            if ((col - 1 >= 0) && (isOpen(row, col - 1))) {
                sites.union(index, CoordtoIndex(row, col - 1));
                fullSite.union(index, CoordtoIndex(row, col - 1));
            }
        }
        else {
            throw new IllegalArgumentException();
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        withinBound(row, col);
        return grid[row][col];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        withinBound(row, col);
        boolean full = false;
        if (isOpen(row, col)) {
            int index = CoordtoIndex(row, col);
            full = fullSite.connected(index, topVirtualSite);
        }
        return full;
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return emptySites;
    }

    // does the system percolate?
    public boolean percolates() {
        return sites.connected(bottomVirtualSite, topVirtualSite);

    }

    // unit testing (required)
    public static void main(String[] args) {
        Percolation perc = new Percolation(5);

        perc.open(2, 3);
        System.out.println(perc.isOpen(2, 3)); //true
        System.out.println(perc.isOpen(4, 4)); //false
        System.out.println(perc.isOpen(0, 4)); //false

        //Testing CoordtoIndex Private Helper Method
        // System.out.println(perc.CoordtoIndex(2, 3)); //13
        // System.out.println(perc.CoordtoIndex(0, 4)); //4
        // System.out.println(perc.CoordtoIndex(5, 3)); //28

        perc.open(4, 0);
        perc.open(3, 0);
        perc.open(2, 0);
        perc.open(1, 0);
        perc.open(0, 0);

        System.out.println(perc.numberOfOpenSites()); //6

        System.out.println(perc.isFull(3, 0)); //true

        System.out.println(perc.percolates()); //true

    }
}

