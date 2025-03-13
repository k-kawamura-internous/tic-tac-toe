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
		// 先攻後攻選択する
		int firstOrsecond = selectTurn();
		
		USER_MARK = firstOrsecond == 1 ? "O" : "X";
		COM_MARK = firstOrsecond == 2 ?  "O" : "X";
		
		// ゲーム
		playGame();
	}
	private int selectTurn() {
		System.out.println("先攻・後攻を選択してください");
		System.out.println("1:先攻、2:後攻");

		int no = -1;
		while (true) {
			// 数字を入力する
			no = inputNo(1, 2);
			if (checkNo(no, 1, 2)) {
				return no;
			}
			System.out.println("指定範囲外の数値です");
		}
	}
	//ゲームを進めるメソッド
	private void playGame() {
		System.out.println("3目並べ開始");
		String str = USER_MARK.equals("O") ? "O:ユーザー、X:COM" : "O:COM、X:ユーザー";
		System.out.println(str);

		String result = "";
		while (true) {
			playTurn("O");
			result = check();
			if (isGameOver(result)) {  // 勝利または引き分けの条件
				break;
			}

			playTurn("X");
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

	// ボードのマス目を選択(ユーザー)
	private int selectNoForUser() {
		// マス目を出力
		printBoard();
		System.out.println("1～9のマスのうち、空いているマスの番号を選び、入力してください");

		int no = -1;
		boolean isCorrectNo = false;
		while (!isCorrectNo) {
			no = inputNo(1, 9);
			isCorrectNo = checkNo(no, 1, 9);
			if (!isCorrectNo) {
				System.out.println("指定範囲外の数値です");
				continue;
			}
			no = no - 1;
		}
		return no;
	}

	// ボードのマス目を選択(COM)
	private int selectNoForCOM() {
		int no = -1;

		boolean isCorrectNo = false;
		Random rand = new Random();
		while (!isCorrectNo) {
			// 1～9の数字をランダムで選択
			no = rand.nextInt(9);

			// 既に選択されたマス目の場合はcontinueする
			if(checkAlreadySelected(no)) {
				continue;
			}
			isCorrectNo = true;
		}
		return no;
	}

	// 番号を入力する
	int inputNo(int min, int max) {
		int no = -1;
		while (true) {
			System.out.print(min + "から"+ max +"の数字を入力してください: ");

			// ユーザーの入力を受け取る
			if (sc.hasNextInt()) {  // 数字が入力されたかチェック
				no = sc.nextInt();
				break;
			} else {
				// 数字以外の入力があった場合
				System.out.println("無効な入力です");
				sc.next(); // 不正な入力を読み飛ばす
			}
		}
		return no;
	}

	// 指定の範囲の数字が入力されているかをチェック
	private boolean checkNo(int no, int min, int max) {
		return min <= no && no <= max;
	}

	// COMとユーザーのターン
	private void playTurn(String playerMark) {
		if (USER_MARK.equals(playerMark)) {
			System.out.println("ユーザーのターン");
		} else {
			System.out.print("COMのターン...");
		}

		// マスを選択する
		int no = selectNo(playerMark);
		// マスにマークを入力
		makeMove(playerMark, no);
		
		// プレイヤーCOMの場合
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
			// それぞれのマス目の値を取得する
			String square1 = board[pattern[0] / 3][pattern[0] % 3];
			String square2 = board[pattern[1] / 3][pattern[1] % 3];
			String square3 = board[pattern[2] / 3][pattern[2] % 3];
			
			// ユーザーが勝利したか確認
			if (USER_MARK.equals(square1)
					&& USER_MARK.equals(square2)
					&& USER_MARK.equals(square3)) {
				return USER_MARK;
			}

			// COMが勝利したか確認
			if (COM_MARK.equals(square1)
					&& COM_MARK.equals(square2)
					&& COM_MARK.equals(square3)) {
				return COM_MARK;
			}
		}

		// 引き分けか確認
		if (isBoardFull()) {
			return "-";
		}
		return ""; // 決着がついていない
	}

	// ゲームが終了したか確認
	private boolean isGameOver(String result) {
		return !result.equals("");
	}

	// 最終結果を表示する
	private void finalJudge(String result)	 {
		// ゲーム盤面を出力する
		printBoard();
		// ゲームの勝敗を出力する
		if(USER_MARK.equals(result)) {
			System.out.println("ユーザーの勝利");
		} else if(COM_MARK.equals(result)) {
			System.out.println("COMの勝利");
		} else {
			System.out.println("引き分け");
		}

	}
}
