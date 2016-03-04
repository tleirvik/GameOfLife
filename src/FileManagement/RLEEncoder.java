package FileManagement;

import java.io.File;
import GameOfLife.Board;
import GameOfLife.MetaData;

/**
 *
 *
 *
 */
public class RLEEncoder {
	private byte[][] board;
	private MetaData metadata;
	private File file;
	private StringBuffer rleString;


	/**
	 *
	 * @param b
	 * @param f
	 */
	public RLEEncoder(Board b, File f) {
		metadata = b.getMetaData();
		board = b.getCellArray();
	}

	/**
	 *
	 */
	public boolean encode() {





		return true;
	}

	private void openFile() {

	}

	private void encodeMetaData() {

	}

	private void encodeBoardSize() {

	}

	private void encodeRules() {

	}

	private void encodeBoard() {

	}

	private void writeToFile() {

	}

	private void closeFile() {

	}
}
