package Game;

public class Main {

	public static void main(String[] args) {
		
		Board brett = new Board(10,10);
		brett.populateBoard();
		
		for(int i = 0; i < 10; i++) {
			for(int j = 0; j < 10; j++) {
				if(brett.boardGrid[i][j].isAlive == true) {
					System.out.print("o");
				} else {
					System.out.print("x");
				}
				
			}
			System.out.println("");
		}
	}

	public static void drawBoard() {
		/* Les antall rows & columns fra board,
		 * lag en grid av x * y antall rektangler
		 * og iterer igjennom den for å tegne om cellen lever eller ikke gjør det.
		 */
	}
	
}
