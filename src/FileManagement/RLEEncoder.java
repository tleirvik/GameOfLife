package FileManagement;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

import GameOfLife.Board;
import GameOfLife.MetaData;
import GameOfLife.ViewController;

/**
 *
 *
 *
 */
public class RLEEncoder {
	private byte[][] board;
	private MetaData metadata;
	private Path filePath;
	private StringBuffer rleString;

	/**
	 *
	 * @param b
	 * @param f
	 */
	public RLEEncoder(Board b, File f) {
		metadata = b.getMetaData();
		board = b.getCellArray();
		filePath = f.toPath();
	}

	/**
	 * Creates a StringBuffer and encodes the board as well as the associated metadata before writing
	 * the string to the file specified in the constructor.
	 *
	 */
	public boolean encode() {
		rleString = new StringBuffer();
		Charset charset = Charset.forName("UTF-8");

		try  (BufferedWriter bw = Files.newBufferedWriter(filePath, charset)) {
			encodeMetaData();
			encodeBoardSize();
			encodeRuleString();
			encodeBoard();
			
			writeToFile(bw);
		} catch (IOException ioE) {
            ViewController.infoBox("Error!", "An unknown error occoured!", "The following error occured when trying to save the game: " + ioE.getMessage());
            return false;
		}




		return true;
	}

	/**
     * Encodes the associated metadata and adds it to the StringBuffer.<p>
     *
     *This method is not meant to be called directly, but rather through the encode() method.
     */
	public void encodeMetaData() {
		rleString.append("#N " + metadata.getName() + "\r\n");
		rleString.append("#O " + metadata.getAuthor() + "\r\n");
		String[] comments = metadata.getComment().split("\\r?\\n");

		for(String comment : comments) {
			rleString.append("#C " + comment + "\r\n");
		}
		System.out.println(rleString.toString());
	}

	/**
     * Encodes the board size and adds it to the StringBuffer.<p>
     *
     *This method is not meant to be called directly, but rather through the encode() method.
     */
	private void encodeBoardSize() {
		rleString.append("x = " + board[0].length + ", " + board.length);
	}

	/**
     * Encodes the game's rulestring and adds it to the StringBuffer.<p>
     *
     *This method is not meant to be called directly, but rather through the encode() method.
     */
	private void encodeRuleString() {
		String[] rule = metadata.getRuleString();
		rleString.append("rule = B" + rule[0] + "/S" + rule[1] + "\r\n");
	}

	private void encodeBoard() {
		int count = 0;
		int lastCell = board[0][0];

		for(int row = 0; row < board.length; row++) {
			for(int col = 0; col < board.length; col++) {
				if(col == board.length-1) {
					String nextChar = (board[row][col] == 1) ? "o" : "b";
					if(count == 1) {
						rleString.append(nextChar);
					} else {
						rleString.append(count + nextChar);
					}
					lastCell = board[row][col];
					count = 0;
				}
				if(board[row][col] == lastCell) {
					count++;
				} else {
					String nextChar = (board[row][col] == 1) ? "o" : "b";
					if(count == 1) {
						rleString.append(nextChar);
					} else {
						rleString.append(count + nextChar);
					}
					lastCell = board[row][col];
					count = 0;
				}
			}
			rleString.append("$");
		}
	}

	private void writeToFile(BufferedWriter bw) {
		System.out.println(rleString);
	}
}
