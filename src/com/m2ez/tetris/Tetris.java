package com.m2ez.tetris;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * 俄罗斯方块游戏类
 */

public class Tetris extends JPanel {
	private int index;
	private int level;
	private int speed;
	private int interval = 10;
	private Timer timer;
	/** 游戏得分数组 */
	private static int[] scoreTable = { 0, 1, 10, 100, 500 };
	/** 游戏状态 */
	int state = 0;
	private static final int RUNNING = 0;
	private static final int PAUSE = 1;
	private static final int GAME_OVER = 2;
	/** 总分 */
	private int score;
	/** 销毁行数 */
	private int lines;
	/** 行高 */
	public static final int ROWS = 20;
	/** 列宽 */
	public static final int COLS = 10;
	/** 格子宽高 */
	public static final int CELL_SIZE = 26;
	/** 墙 */
	private Cell[][] wall = new Cell[ROWS][COLS];
	/** 正在下落的方块,七种方块之一 */
	private Tetromino tetromino;
	/** 下一个准备出场的方块 */
	private Tetromino nextOne;
	/** 背景 */
	private static BufferedImage background;
	public static BufferedImage T;
	public static BufferedImage S;
	public static BufferedImage Z;
	public static BufferedImage I;
	public static BufferedImage L;
	public static BufferedImage J;
	public static BufferedImage O;
	public static BufferedImage pause;
	public static BufferedImage gameOver;
	static {
		try {
			background = ImageIO.read(Tetris.class.getResource("tetris.png"));
			T = ImageIO.read(Tetris.class.getResource("T.png"));
			S = ImageIO.read(Tetris.class.getResource("S.png"));
			Z = ImageIO.read(Tetris.class.getResource("Z.png"));
			I = ImageIO.read(Tetris.class.getResource("I.png"));
			L = ImageIO.read(Tetris.class.getResource("L.png"));
			J = ImageIO.read(Tetris.class.getResource("J.png"));
			O = ImageIO.read(Tetris.class.getResource("O.png"));
			pause = ImageIO.read(Tetris.class.getResource("pause.png"));
			gameOver = ImageIO.read(Tetris.class.getResource("game-over.png"));
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	/** 检查当前游戏中正在下落的方块是不是出界 */
	private boolean outOfBounds() {
		if (this.tetromino == null) {
			return true;
		}
		final Cell[] cells = this.tetromino.cells;
		for (int i = 0; i < cells.length; i++) {
			final Cell cell = cells[i];
			final int row = cell.getRow();
			final int col = cell.getCol();
			if (col < 0 || col >= COLS || row < 0 || row >= ROWS) {
				return true;
			}
		}
		return false;
	}

	/** 打印方法 */
	public void paint(final Graphics g) {
		// g.setColor(Color.BLUE);
		g.drawImage(background, 0, 0, null);
		g.translate(15, 15);
		paintWall(g);
		paintTettomino(g);
		paintNextOne(g);
		paintLines(g);
		paintScore(g);
		paintLevel(g);
		paintState(g);
	}

	/** 打印状态界面 */
	private void paintState(Graphics g) {
		switch (state) {
		case GAME_OVER:
			g.drawImage(gameOver, -15, -15, null);
			break;
		case PAUSE:
			g.drawImage(pause, -15, -15, null);
			break;
		}

	}

	/** 游戏动作 */
	public void action() {
		// wall[8][5] = new Cell(8, 5, I);
		nextOne = Tetromino.randomOne();
		tetromino = Tetromino.randomOne();
		state = RUNNING;
		// 添加定时控制器
		timer = new Timer();
		TimerTask task = new TimerTask() {
			public void run() {
				index++;
				level = lines / 100 + 1;
				speed = 41 - level;
				speed = speed <= 0 ? 1 : speed;
				if (index % speed == 0) {
					// 只有游戏状态RUNONING时才自动下落
					if (state == RUNNING) {
						softDropAction();
					}
				}
				repaint();
			}
		};
		timer.schedule(task, 0, interval);
		// 实现键盘监听接口，创建监听器对象

		final KeyListener l = new KeyListener() {
			/** 按键按下时候执行的方法 */
			public void keyPressed(KeyEvent e) {
				int key = e.getKeyCode();

				switch (state) {
				case RUNNING:
					processRunning(key);
					break;
				case PAUSE:
					processPause(key);
					break;
				case GAME_OVER:
					processGameOver(key);
					break;
				}
			}

			/** 按键抬起后执行的方法 */
			public void keyReleased(KeyEvent e) {

			}

			/** 字母按键输入以后执行的方法 */
			public void keyTyped(KeyEvent e) {

			}
		};
		// 注册监听器对象
		this.addKeyListener(l);
		// 获得输入焦点
		this.setFocusable(true);
		// 等待键盘消息
	}

	/** 游戏结束时执行的按键 */
	protected void processGameOver(int key) {
		switch (key) {
		case KeyEvent.VK_S:
			state = RUNNING;
			index = score = lines = 0;
			wall = new Cell[ROWS][COLS];
			this.nextOne = Tetromino.randomOne();
			this.tetromino = Tetromino.randomOne();
			break;
		case KeyEvent.VK_Q:
			System.exit(0);
			break;
		}
		repaint();
	}

	/** 游戏暂停时执行的按键 */
	protected void processPause(int key) {
		switch (key) {
		case KeyEvent.VK_C:
			index = 0;
			state = RUNNING;
			break;
		case KeyEvent.VK_Q:
			System.exit(0);
			break;
		}
		repaint();
	}

	/** 游戏运行时执行的按键 */
	protected void processRunning(int key) {
		switch (key) {
		case KeyEvent.VK_RIGHT:
			moveRightAction();
			break;
		case KeyEvent.VK_LEFT:
			moveLeftAction();
			break;
		case KeyEvent.VK_DOWN:
			softDropAction();
			break;
		case KeyEvent.VK_SPACE:
			hardDropAction();
			break;
		case KeyEvent.VK_UP:
			rotateAction();
			break;
		case KeyEvent.VK_P:
			state = PAUSE;
			break;
		case KeyEvent.VK_Q:
			System.exit(0);
			break;
		}
		repaint();
	}

	/** 旋转方法 */
	private void rotateAction() {
		tetromino.rotateRight();
		if (outOfBounds() || concide()) {
			tetromino.rotateLeft();
		}
	}

	/** 左移方法 */
	private void moveRightAction() {
		if (tetromino == null) {
			return;
		}
		tetromino.moveRight();
		if (outOfBounds() || concide()) {
			tetromino.moveLeft();
		}
	}

	/** 右移方法 */
	private void moveLeftAction() {
		if (tetromino == null) {
			return;
		}
		tetromino.moveLeft();
		if (outOfBounds() || concide()) {
			tetromino.moveRight();
		}
	}

	/** 软下落，按键一次下落一格 */
	private void softDropAction() {
		if (tetromino == null) {
			return;
		}
		if (canDrop()) {
			tetromino.softDrop();
		} else {
			landIntoWall();
			final int lines = destroyLines();
			this.score += scoreTable[lines];
			this.lines += lines;
			if (isGameOver()) {
				// System.out.println("Bye!(T_T)");
				state = GAME_OVER;
			} else {
				tetromino = nextOne;
				nextOne = Tetromino.randomOne();
			}
		}
	}

	/** 硬下落，按键一次下落到最下面 */
	private void hardDropAction() {
		while (canDrop()) {
			tetromino.softDrop();
		}
		landIntoWall();
		final int lines = destroyLines();
		this.score += scoreTable[lines];
		this.lines += lines;
		if (isGameOver()) {
			// System.out.println("Bye!(T_T)");
			state = GAME_OVER;
		} else {
			tetromino = nextOne;
			nextOne = Tetromino.randomOne();
		}
	}

	/** 判断游戏是不是结束 */
	private boolean isGameOver() {
		final Cell[] cells = nextOne.cells;
		for (int i = 0; i < cells.length; i++) {
			final Cell cell = cells[i];
			final int row = cell.getRow();
			final int col = cell.getCol();
			if (wall[row][col] != null) {
				return true;
			}
		}

		return false;
	}

	/** 销毁已经满的行 */
	private int destroyLines() {
		int lines = 0;
		for (int row = 0; row < ROWS; row++) {
			if (fullCells(row)) {
				deleteRow(row);
				lines++;
			}
		}
		return lines;
	}

	/** 删除行方法 */
	private void deleteRow(final int row) {
		for (int i = row; i >= 1; i--) {
			System.arraycopy(wall[i - 1], 0, wall[i], 0, COLS);
		}
		Arrays.fill(wall[0], null);
	}

	/** 判断是否填满整行方法 */
	private boolean fullCells(final int row) {
		final Cell[] line = wall[row];
		for (int i = 0; i < line.length; i++) {
			final Cell cell = line[i];
			if (cell == null) {
				return false;
			}
		}
		return true;
	}

	/** 当前下落的方块进入到墙里面 */
	private void landIntoWall() {
		final Cell[] cells = tetromino.cells;
		for (int i = 0; i < cells.length; i++) {
			final Cell cell = cells[i];
			final int row = cell.getRow();
			final int col = cell.getCol();
			wall[row][col] = cell;
		}
	}

	/** 检查当前方块能否下落 */
	private boolean canDrop() {
		final Cell[] cells = tetromino.cells;
		for (int i = 0; i < cells.length; i++) {
			final Cell cell = cells[i];
			final int row = cell.getRow();
			if (row == ROWS - 1) {
				return false;
			}
		}
		for (int i = 0; i < cells.length; i++) {
			final Cell cell = cells[i];
			final int row = cell.getRow();
			final int col = cell.getCol();
			if (wall[row + 1][col] != null) {
				return false;
			}
		}
		return true;
	}

	/** 检查当前方块是否与其他方块重合 */
	private boolean concide() {
		if (this.tetromino == null) {
			return false;
		}
		final Cell[] cells = this.tetromino.cells;
		for (int i = 0; i < cells.length; i++) {
			final Cell cell = cells[i];
			final int row = cell.getRow();
			final int col = cell.getCol();
			if (wall[row][col] != null) {
				return true;
			}
		}
		return false;
	}

	/** 绘制总分 */
	private void paintScore(final Graphics g) {
		int x = 291;
		int y = 165;
		Font f = new Font(Font.SERIF, Font.BOLD, 30);
		g.setFont(f);
		int color = 0x667799;
		g.setColor(new Color(color));
		g.drawString("SCORE:" + score, x, y);
	}

	/** 绘制销毁行数 */
	private void paintLines(final Graphics g) {
		int x = 291;
		int y = 226;
		Font f = new Font(Font.SERIF, Font.BOLD, 30);
		g.setFont(f);
		int color = 0x667799;
		g.setColor(new Color(color));
		g.drawString("LINES:" + lines, x, y);
	}

	/** 绘制等级 */
	private void paintLevel(final Graphics g) {
		int x = 291;
		int y = 226 + 56;
		Font f = new Font(Font.SERIF, Font.BOLD, 30);
		g.setFont(f);
		int color = 0x667799;
		g.setColor(new Color(color));
		g.drawString("LEVEL:" + level, x, y);
	}

	/** 绘制下一个方块 */
	private void paintNextOne(final Graphics g) {
		if (nextOne == null) {
			return;
		}
		final Cell[] cells = this.nextOne.cells;
		for (int i = 0; i < cells.length; i++) {
			final Cell cell = cells[i];// Cell变量引用了Cell[]中每一个变量
			final int x = (cell.getCol() + 10) * CELL_SIZE;
			final int y = (cell.getRow() + 1) * CELL_SIZE;
			g.drawImage(cell.getImage(), x, y, null);
		}
	}

	/** 绘制正在下落的方块 */
	private void paintTettomino(final Graphics g) {
		if (tetromino == null) {
			return;
		}
		final Cell[] cells = this.tetromino.cells;
		for (int i = 0; i < cells.length; i++) {
			final Cell cell = cells[i];// Cell变量引用了Cell[]中每一个变量
			final int x = cell.getCol() * CELL_SIZE;
			final int y = cell.getRow() * CELL_SIZE;
			g.drawImage(cell.getImage(), x, y, null);
		}
	}

	/** 绘制格子线 */
	private void paintWall(final Graphics g) {
		for (int row = 0; row < ROWS; row++) {
			for (int col = 0; col < COLS; col++) {
				final Cell c = wall[row][col];
				final int x = col * CELL_SIZE;
				final int y = row * CELL_SIZE;
				if (c == null) {
					// g.drawRect(x, y, CELL_SIZE, CELL_SIZE);
				} else {
					g.drawImage(c.getImage(), x, y, null);
				}
			}
		}
	}

	public static void main(final String[] args) {
		final JFrame frame = new JFrame();
		final Tetris tetris = new Tetris();
		frame.setSize(535, 575);// 设置窗口尺寸
		frame.setLocationRelativeTo(null);// 居中
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// 关闭窗口的行为，关闭窗口程序结束，适用于单窗口程序
		tetris.setBackground(Color.black);// 设置背景颜色
		frame.add(tetris);// 添加面板
		frame.setVisible(true);// 显示面板
		tetris.action();// 监听键盘动作方法
	}
}
