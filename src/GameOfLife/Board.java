package GameOfLife;

/**
 * Abstract Board class
 *
 *
 */
@Deprecated
public abstract class Board {

    private int rows;
    private int columns;
    private MetaData metadata;


    /**
     *  Constructor; sets rows & columns
     * @param rows
     * @param columns
     */
    public Board(int rows, int columns) {

            this.rows = rows;
            this.columns = columns;
    }


    /**
     *
     * @return
     */
    public int getRows() {
            return rows;
    }


    /**
     *
     * @return
     */
    public int getColumns() {
            return columns;
    }



    /**
     *  This method returns meta data from the
     *  board.
     *
     * @return MetaData Meta data from the board contained in
     * the class
     */
    public MetaData getMetaData() {
    return metadata;
}

    /**
     * This method sets the meta data of the board
     * @param metaData Object of type MetaData
     * @return void
     */
    public void setMetaData(MetaData m) {
            metadata = m;
    }
}
