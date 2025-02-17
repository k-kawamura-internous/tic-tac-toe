package app;

public class Board {
	protected String[][] board = new String[3][3];
	// そろうパターン
	protected static final int[][] COMPLETE_PATTERNS = {
			{0, 3, 6}, // 1列目 (1)
			{1, 4, 7}, // 2列目 (2)
			{2, 5, 8}, // 3列目 (3)
			{0, 1, 2}, // 1行目 (4)
			{3, 4, 5}, // 2行目 (5)
			{6, 7, 8}, // 3行目 (6)
			{0, 4, 8}, // 斜め：左上から右下 (7)
			{2, 4, 6}  // 斜め：右上から左下 (8)
	};
	protected static final String USER_MARK = "O";
	protected static final String COM_MARK = "X";
	
	// マスがすでに選択されているかをチェック
	protected boolean checkAlreadySelected(int no) {
		String square = board[no / 3][no % 3];
		if(USER_MARK.equals(square) 
				|| COM_MARK.equals(square)) {
			return true;
		}
		return false;
	}
	
	// 盤面を出力する
	protected void printBoard() {
		// 表の出力
		System.out.println(createLine());

		// 配列の中身を表示
		for (int i = 0; i < 3; i++) {
			String row = "|";
			for (int j = 0; j < 3; j++) {
				row += " " + board[i][j] + " |";
			}
			System.out.println(row);
			System.out.println(createLine());
		}
	}
	
	// 盤面用の線を表示する
	private String createLine() {
		String line = "+";
		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < 3; j++) {
				line += "-";
			}
			line += "+"; 
		}
		return line;
	}
	
	// マス目にマークを記入する
	protected void makeMove(String playerMark, int no){
		board[no / 3][no % 3] = playerMark;
	}
	
	// 盤面が満杯かどうかをチェック
	protected boolean isBoardFull() {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (!USER_MARK.equals(board[i][j]) && !COM_MARK.equals(board[i][j])) {
					return false;
				}
			}
		}
		return true;
	}
	
}
