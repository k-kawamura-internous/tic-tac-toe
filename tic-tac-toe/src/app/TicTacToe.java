package app;

import java.util.Random;
import java.util.Scanner;

public class TicTacToe extends Board{
	private Scanner sc = new Scanner(System.in);

	public TicTacToe() {
		// ボードを初期化する
		int no = 1;
		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < 3; j++) {
				board[i][j] = String.valueOf(no);
				no++;
			}
		}
	}

	// Mainクラスから呼ばれるメソッド
	public void execute() {
		playGame();
	}

	//ゲームを進めるメソッド
	private void playGame() {
		System.out.println("3目並べ開始");
		System.out.println("O:ユーザー、X:COM");

		String result = "";
		while (true) {
			playTurn(USER_MARK);
			result = check();
			if (isGameOver(result)) {  // 勝利または引き分けの条件
				break;
			}

			playTurn(COM_MARK);
			result = check();
			if (isGameOver(result)) {  // 勝利または引き分けの条件
				break;
			}
		}

		// ゲームが終わったのでリソースを開放する
		if (sc != null) {
			sc.close();
		}

		System.out.println("ゲーム終了");
		finalJudge(result);
	}

	// ボードのマス目を選択
	private int selectNo(String playerMark) {
		int no = -1;
		if(USER_MARK.equals(playerMark)) {
			no = selectNoForUser();
		}else {
			no = selectNoForCOM();
		}
		return no;
	}
	
	private int selectNoForUser() {
		// マス目を出力
		printBoard();
		System.out.println("1～9のマスのうち、空いているマスの番号を選び、入力してください");
		String line = null;
		int no = -1;
		boolean isCorrectNo = false;
		while (!isCorrectNo) {
			line = inputNo();
			boolean isNumeric = checkNo(line);
			if (!isNumeric) {
				System.out.println("1～9の数字で指定してください");
				continue;
			}
			no = Integer.parseInt(line) - 1;
			if(checkAlreadySelected(no)) {
				System.out.println("既に選択されています,別の数字を選択してください");
				continue;
			}
			isCorrectNo = true;
		}
		return no;
	}

	private int selectNoForCOM() {
		int no = -1;

		boolean isCorrectNo = false;
		Random rand = new Random();
		while (!isCorrectNo) {
			no = rand.nextInt(9);

			if(checkAlreadySelected(no)) {
				continue;
			}
			isCorrectNo = true;
		}
		return no;
	}

	//
	private String inputNo() {
		String line = null;
		while (line == null) {
			line = sc.nextLine();
		}
		return line;
	}

	// 1～9の数字が入力されているかをチェック
	private boolean checkNo(String line) {
		return "123456789".contains(line);
	}

	// COMとユーザーのターン
	private void playTurn(String playerMark) {
		if (COM_MARK.equals(playerMark)) {
			System.out.print("COMのターン...");
		} else {
			System.out.println("ユーザーのターン");
		}
		
		// マスを選択する
		int no = selectNo(playerMark);
		// マスにマークを入力
		makeMove(playerMark, no);
		if (COM_MARK.equals(playerMark)) {
			// 考えている風にするため1.5秒ストップする
			try {
				Thread.sleep(1500);
			} catch (InterruptedException e) {
				System.out.println("システムエラーです");
			}
			System.out.println("完了");
		}

	}

	// ゲームに決着がついたか確認する
	private String check() {
		for (int[] pattern : COMPLETE_PATTERNS) {
			// ユーザーが勝利したか確認
			if (board[pattern[0] / 3][pattern[0] % 3].equals(USER_MARK)
					&& board[pattern[1] / 3][pattern[1] % 3].equals(USER_MARK)
					&& board[pattern[2] / 3][pattern[2] % 3].equals(USER_MARK)) {
				return USER_MARK;
			}
			
			// COMが勝利したか確認
			if (board[pattern[0] / 3][pattern[0] % 3].equals(COM_MARK)
					&& board[pattern[1] / 3][pattern[1] % 3].equals(COM_MARK)
					&& board[pattern[2] / 3][pattern[2] % 3].equals(COM_MARK)) {
				return COM_MARK;
			}
		}

		// 引き分け
		if (isBoardFull()) {
			return "-";
		}
		return ""; // 決着がついていない
	}

	// ゲームが終了したか確認
	private boolean isGameOver(String result) {
		return !result.equals("");
	}

	private void finalJudge(String result)	 {
		// ゲーム結果を出力する
		printBoard();
		if(USER_MARK.equals(result)) {
			System.out.println("ユーザーの勝利");
		} else if(COM_MARK.equals(result)) {
			System.out.println("COMの勝利");
		} else {
			System.out.println("引き分けです");
		}

	}
}
