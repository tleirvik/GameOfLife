package GameOfLife;


/**
 * Created by tleirvik on 06.04.2016.
 */
public class Statistics {
    private FixedBoard fixedBoard;
    private byte[][] pattern;
    private GameOfLife gol;
    private MetaData metaData;
    private int[] stats;
    private int iterations;

    public Statistics(byte[][] pattern, int interations, MetaData metaData) {
        this.pattern = pattern;
        // fixedBoard = new FixedBoard(pattern, gol.getMetaData());
        this.iterations = iterations;
        this.metaData = metaData;
    }
    public void run() {
        gol = new GameOfLife(pattern, metaData);

        // Run x times
        for (int i = 0; i < 10; i++) {
            gol.updateWithStats(10);
            System.out.println("nextgen with stats - " + i);

        }
    }
    public int[] getStats() {
        return gol.getStatsArray();
    }


}
