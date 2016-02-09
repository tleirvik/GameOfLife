package GameOfLife;

public class GameOfLife2D extends GameOfLife{

	private Board boardGrid;
        private boolean isDynamic;
        /**
         * a
         * @param isDynamic
         * @param r
         * @param c 
         */
        public GameOfLife2D(boolean isDynamic, int r, int c) {
            super(r, c);
            this.isDynamic = isDynamic;
            
            // sjekker at vi ikke får et tomt array eller 1 x 1 grid
            if(r < 2) {
                r = 10;
            }
            if(c < 2) {
                c = 10;
            }
            
            // bestemmer hvilket brettype som er valgt
            if(isDynamic)
                boardGrid = new DynamicBoard(r, c);
            else
                boardGrid = new FixedBoard(r, c);
        }
        

	@Override
	public void populateBoard() {
		boolean[][] grid = {
                    {true, false, false, true, false, true, false, false, true, false},
                    {true, false, false, true, false, true, false, false, true, false},
                    {true, false, false, true, false, true, false, false, true, false},
                    {true, false, false, true, false, true, false, false, true, false},
                    {true, false, false, true, false, true, false, false, true, false},
                    {true, false, false, true, false, true, false, false, true, false},
                    {true, false, false, true, false, true, false, false, true, false},
                    {true, false, false, true, false, true, false, false, true, false},
                    {true, false, false, true, false, true, false, false, true, false},
                    {true, false, false, true, false, true, false, false, true, false}
                };
                
                for(boolean[] rad : grid) {
                    for(int i = 0; i < rad.length; i++) {
                        if(rad[i])
                            System.out.println("[] ");
                        else
                            System.out.println("X ");
                    }
                    System.out.println();
                }
	}

	@Override
	public void nextGeneration() {
		

	}

	@Override
	public int countNeighbours() {
		
		return 0;
	}


}
