package FileManagement;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;

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
		rleString = new StringBuffer();
		Charset charset = Charset.forName("UTF-8");

		try  (BufferedWriter bw = Files.newBufferedWriter(file.toPath(), charset)) {
			encodeMetaData();
			writeToFile(bw);



		} catch (FileNotFoundException fnfE) {

		} catch (IOException ioE) {

		}




		return true;
	}

	public void encodeMetaData() {
		rleString.append("#N " + metadata.getName() + "\r\n");
		rleString.append("#O " + metadata.getAuthor() + "\r\n");
		String[] comments = metadata.getComment().split("\\r?\\n");

		for(String comment : comments) {
			rleString.append("#C " + comment + "\r\n");
		}
		System.out.println(rleString.toString());
	}

	private void encodeBoardSize() {
		rleString,append("x = " + board[0].length + ", " + board.length);
	}

	private void encodeRule() {
		String[] rule = metadata.getRules();
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
		System.out.println(rleString);
	}

	private void writeToFile(BufferedWriter bw) {

	}

	private void closeFile() {

	}
}
