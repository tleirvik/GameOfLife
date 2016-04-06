package GameOfLife;

/**
 *
 * @author Robin
 *
 * Endret, men ble ikke merkbart raskere med egen klasse som implementerer Runnable
 */
public class NextGenerationWorker implements Runnable {

    private GameController gameController;
    
    public NextGenerationWorker(GameController gameController) {
        this.gameController = gameController;
    }
    
    @Override
    public void run() {
        gameController.play();
    }    
}
