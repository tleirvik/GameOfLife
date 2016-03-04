package FileManagement;

import static org.junit.Assert.*;

import java.io.File;
import org.junit.Test;
import GameOfLife.MetaData;

public class RLEEncoderTest {
	private MetaData metadata;
	private StringBuffer rleString;


	@Test
	public void testEncodeMetaData() {
		rleString = new StringBuffer();
		//byte[][] board = new byte[5][5];

		byte[][] board = {
				{1,0,0,1},
				{1,0,0,1},
				{1,0,0,1},
				{1,0,0,1},
				{1,0,0,1}
		};

		int count = 0;
		int lastCell = board[0][0];

		for(int row = 0; row < board.length; row++) {
			for(int col = 0; col < board[0].length; col++) {
				if(board[row][col] == lastCell) {
					count++;
					break;
				}
				String nextChar = (board[row][col] == 1) ? "o" : "b";
				rleString.append(nextChar);
			}
			rleString.append("$");
		}
		System.out.println(rleString);
		/*
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
		}*/
	}
		/*metadata = new MetaData();

		metadata.setComment("grger\n fewjfowei \n fjiweooifw");
		metadata.setAuthor("Ole");
		metadata.setName("TEST!");

		rleString = new StringBuffer();
		rleString.append("#N " + metadata.getName() + "\r\n");
		rleString.append("#O " + metadata.getAuthor() + "\r\n");
		String[] comments = metadata.getComment().split("\\r?\\n");

		for(String comment : comments) {
			rleString.append("#C " + comment + "\r\n");
		}
		System.out.println(rleString.toString());
	}*/

}
