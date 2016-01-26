package game;

public class Main {

	public static void main(String[] args) {
		
		StaticBoard brett = new StaticBoard(10,10);
		brett.populateBoard();
		
		for(int i = 0; i < 10; i++) {
			for(int j = 0; j < 10; j++) {
				if(brett.getBoardGrid()[i][j].getIsAlive() == true) {
					System.out.print("O ");
				} else {
					System.out.print("X ");
				}
				
			}
			System.out.println("");
		}
		
		while(true) {
			brett.nextGeneration();
			
			for(int i = 0; i < 10; i++) {
				for(int j = 0; j < 10; j++) {
					if(brett.getBoardGrid()[i][j].getIsAlive() == true) {
						System.out.print("O ");
					} else {
						System.out.print("X ");
					}
					
				}
				System.out.println("");
			}
			
			System.out.println("");
			System.out.println("");
		}
		
		
	}

	public static void drawBoard() {
		/* Les antall rows & columns fra board,
		 * lag en grid av x * y antall rektangler
		 * og iterer igjennom den for � tegne om cellen lever eller ikke gj�r det.
		 */
	}
	
}
