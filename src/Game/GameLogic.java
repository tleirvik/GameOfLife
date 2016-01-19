package Game;

import java.util.Random;

public class GameLogic {
	
	static boolean seedGenerator(/*long seed*/) {
		//La bruker lage sin egen seed
		//Bruk datamaskinens klosse?
		
		Random rand = new Random();
		
		int randInt = rand.nextInt(2);
		
		if(randInt == 1) {
			return false;
		} else {
			return true;
		}
	}
	
	
	
}
