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

	// ボードのマス目を選択(ユーザー)
	private int selectNoForUser() {
		// マス目を出力
		printBoard();
		System.out.println("1～9のマスのうち、空いているマスの番号を選び、入力してください");

		int no = -1;
		while (true) {
			// 数字を入力する
			no = inputNo();
			// 1～9までの数値が入力されているか確認
			if (!checkNo(no)) {
				System.out.println("指定範囲外の数値です");
				continue;
			}
			// 配列の要素番号にするために-1する
			no = no - 1;
			// 入力された数字がすでに選択されているマス目かを確認
			if(checkAlreadySelected(no)) {
				System.out.println("既に選択されています,別の数字を選択してください");
				continue;
			}
			break;
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
	int inputNo() {
		int no = -1;
		while (true) {
			System.out.print("1から9の数字を入力してください: ");

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

	// 1～9の数字が入力されているかをチェック
	private boolean checkNo(int no) {
		return 1 <= no && no <= 9;
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
