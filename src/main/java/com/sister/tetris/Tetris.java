package com.sister.tetris;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
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
 * 俄罗斯方块类
 */

public class Tetris extends JPanel {

	private static final long serialVersionUID = 1L;
	/* 分数*/
	private int score;
	/* 消除的行数*/
	private int lines;
	public static final int ROWS = 20;
	public static final int COLS = 10;
	/* 用二维数组定义俄罗斯方块的墙 墙是Tertis的组件*/
	private Cell[][] walls = new Cell[20][10];
	/*正在下落的四格方块*/
	private Tetromino tetromino;
	/* 下一个下落的四格方块*/
	private Tetromino nextOne;
	//图片素材的加载
	//游戏背景图片
	public static BufferedImage backGround;
	public static BufferedImage gameOver;
	public static BufferedImage I;
	public static BufferedImage J;
	public static BufferedImage L;
	public static BufferedImage O;
	public static BufferedImage S;
	public static BufferedImage T;
	public static BufferedImage Z;

	/*使用静态代码块加载游戏的图片资源*/
	static {
		try {
			//如下代码可以从package中读取 tetris.png 为图片对象
			//图片文件必须放置在com.tarena.tertis包装中
			backGround = ImageIO.read(Tetris.class.getResource("tetris.png"));
			gameOver = ImageIO.read(Tetris.class.getResource("game-over.png"));
			I = ImageIO.read(Tetris.class.getResource("I.png"));
			J = ImageIO.read(Tetris.class.getResource("J.png"));
			L = ImageIO.read(Tetris.class.getResource("L.png"));
			O = ImageIO.read(Tetris.class.getResource("O.png"));
			S = ImageIO.read(Tetris.class.getResource("S.png"));
			T = ImageIO.read(Tetris.class.getResource("T.png"));
			Z = ImageIO.read(Tetris.class.getResource("Z.png"));

		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*@Override重写paint方法将Graphics g绑定到画板上*/
	public void paint(Graphics g) {
		//drawImage 画图 backGround 背景图片
		g.drawImage(backGround, 0, 0, null);
		g.translate(15, 15);
		drawWall(g);
		drawTetromino(g);
		drawNextOne(g);
		drawScore(g);
		g.translate(-15,-15);
		if (finish) {
			g.drawImage(gameOver, 0, 0, null);
		}

	}

	//定义单元格的大小常量
	public static final int CELL_SIZE = 26;

	/*画墙*/
	private void drawWall(Graphics g) {
		for (int row=0;row<ROWS;row++) {
			for (int col=0;col<COLS;col++) {
				int x = col*CELL_SIZE;
				int y = row*CELL_SIZE;
				Cell cell = walls[row][col];
				if (cell==null) {
					g.setColor(new Color(0));
					g.drawRect(x, y, CELL_SIZE, CELL_SIZE);
				}else {
					g.drawImage(cell.getImage(), x-1, y-1, null);
				}
			}
		}
	}

	/*绘制正在下落的方块*/
	private void drawTetromino(Graphics g) {
		//调试语句
		//System.out.println(tetromino);
		if (tetromino==null) {
			return;
		}
		Cell[] cells = tetromino.cells;
		for (int i=0;i<cells.length;i++) {
			Cell cell = cells[i];
			int x = cell.getCol()*CELL_SIZE;
			int y = cell.getRow()*CELL_SIZE;
			g.drawImage(cell.getImage(), x-1, y-1, null);
		}
	}

	/*绘制即将下落的方法*/
	private void drawNextOne(Graphics g) {
		if (nextOne==null) {
			return;
		}
		Cell[]cells = nextOne.cells;
		for (int i=0;i<cells.length;i++) {
			Cell cell = cells[i];
			int x = (cell.getCol() + 10)*CELL_SIZE;
			int y = (cell.getRow() + 1)*CELL_SIZE;
			g.drawImage(cell.getImage(), x-1, y-1, null);
		}
	}

	//定义字体Font的颜色;
	public static final int FONT_COLOR = 0x667799;
	//定义字体的大小
	public static final int FONT_SIZE = 30;

	/*绘制Score 和Lines */
	private	void drawScore(Graphics g) {
		int x = 290;
		int y = 160;
		g.setColor(new Color(FONT_COLOR));
		/*Font字体*/
		Font font = g.getFont();
		//用新字号创建字体
		font = new Font (font.getName(),Font.BOLD,FONT_SIZE);
		g.setFont(font);
		try {
			g.drawString("Scores=" + score +   "分" , x, y);
		} catch ( Exception e) {
			e.printStackTrace();
		}
		y+=56;
		g.drawString("Lines=" + lines + "行", x, y);
		y+=56;
		String str = "[P]Pause";
		if (pause) {
			str = "[C]Continue";
		}
		if (finish) {
			str = "[S]Start";
		}
		g.drawString(str, x, y);
	}

	/*定义游戏开始的方法*/
	public void action() {
		//tetromino = Tetromino.randomOne();
		//nextOne = Tetromino.randomOne();
		//调试语句，用于调试墙的绘制
		//walls[19][3] = new Cell(19,3,S);
		//调试语句
		//System.out.println(tetromino);
		gameStartAction();
		/*启动键盘监听*/
		KeyListener l = new KeyAdapter() {
			/*在按键按下时候执行*/
			public void keyPressed(KeyEvent e) {
				int key = e.getKeyCode();
				if (key == KeyEvent.VK_Q) {
					System.exit(0);
				}

				if (finish) {
					if (key == KeyEvent.VK_S) {
						gameStartAction();
						repaint();
					}
					return;
				}

				if (pause) {
					if (key == KeyEvent.VK_C) {
						continueAction();
						repaint();
					}
					return;
				}


				//获得键盘的key值
				//System.out.println(key);
				switch (key) {
					case KeyEvent.VK_P:
						pauseAction();
						break;
					case KeyEvent.VK_DOWN:
						softDropAction();
						break;
					case KeyEvent.VK_LEFT:
						moveLeftAction();
						break;
					case KeyEvent.VK_RIGHT:
						moveRightAction();
						break;
					case KeyEvent.VK_SPACE:
						hardDropAction();
						break;
					case KeyEvent.VK_UP:
						rotateRightAction();
						break;
					case KeyEvent.VK_Z:
						rotateLeftAction();
						break;
				}
				/**尽快的调用paint()方法绘制界面　按键按下－发现是40－执行softDrop() -
				 * 修改格子的数据－repaint()-尽快调用paint()-根据当前数据（是已经修改更改
				 * 的数据）绘制界面
				 */
				repaint();
			}

		};
		//在当前面板上绑定了键盘监听l
		this.addKeyListener(l);
		//为当前面板请求键盘输入焦点
		this.requestFocus();
	}

	/*向右移动流程控制方法*/
	private void moveRightAction () {
		tetromino.softMoveRight();
		if (outOfBounds() || coincide()) {
			tetromino.softMoveLeft();
		}
	}

	/* 左右是否超出范围的计算*/
	private boolean outOfBounds() {
		Cell[] cells = tetromino.cells;
		for (int i=0;i<cells.length;i++) {
			int col = cells[i].getCol();
			int row = cells[i].getRow();
			if (col<0 || col>9 || row>=ROWS) {
				return true;
			}
		}
		return false;
	}

	/*检查正在下落的方块是否与墙上的方块重合，重合检查*/
	private boolean coincide() {
		Cell[] cells = tetromino.cells;
		for (int i=0;i<cells.length;i++) {
			Cell cell = cells[i];
			int row = cell.getRow();
			int col = cell.getCol();
			if (row>=0 && row<= ROWS && col>=0 && col<=COLS && walls[row][col]!=null) {
				return true;
			}
		}
		return false;
	}

	/**向左移动流程控制方法*/
	private void moveLeftAction() {
		tetromino.softMoveLeft();
		if (outOfBounds() || coincide()) {
			tetromino.softMoveRight();
		}
	}

	/*快速下落*/
	private void hardDropAction() {
		while(canDrop()) {
			tetromino.softDrop();
		}
		landToWall();
		destoryLines();
		checkGameOver();
		tetromino = nextOne;
		nextOne = Tetromino.randomOne();

	}

	/*Tetris类中添加下落流程控制方法*/
	private void softDropAction() {
		if (canDrop()) {
			tetromino.softDrop();
		}else {
			landToWall();
			destoryLines();
			checkGameOver();
			tetromino = nextOne;
			nextOne = Tetromino.randomOne();
		}
	}

	/*检查能否下落*/
	private boolean canDrop() {
		Cell[] cells = tetromino.cells;
		for (int i=0;i<cells.length;i++) {
			Cell cell = cells[i];
			int row = cell.getRow();
			int col = cell.getCol();
			if (row == ROWS-1 || walls[row+1][col]!=null) {
				return false;
			}
		}

//		for (int i=0;i<cells.length;i++) {
//			Cell cell = cells[i];
//			int row = cell.getRow();
//			int col = cell.getCol();
//			if (walls[row+1][col]!=null) {
//				return false;
//			}
//		}
		return true;
	}

	/*上墙方法*/
	private void landToWall() {
		Cell[] cells = tetromino.cells;
		for (int i=0;i<cells.length;i++) {
			Cell cell = cells[i];
			int row = cell.getRow();
			int col = cell.getCol();
			walls[row][col] = cell;
		}
	}

	//定义销毁行数的得分
	public static final int[] SCORE_SIZE = {0,10,50,100,400};

	/*销毁单元格已满的一行*/
	private void destoryLines() {
		int lines = 0;
		for (int i=0;i<walls.length;i++) {
			if (fullLines(i)) {
				removeLines(i);
				lines++;
			}
		}
		this.lines += lines;
		this.score += SCORE_SIZE[lines];
	}

	/*检查一行是否填充满格*/
	private boolean fullLines(int row) {
		Cell[] cells = walls[row];
		for (int i=0;i<cells.length;i++) {
			if (cells[i]==null) {
				return false;
			}
		}
		return true;
	}


	/*移除满行*/
	private void removeLines(int row) {
		for (int i=row;i>0;i--) {
			System.arraycopy(walls[i-1], 0, walls[i], 0, walls[i-1].length);
		}
		Arrays.fill(walls[0], null);
	}

	/*向右旋转控制*/
	private void rotateRightAction() {
		tetromino.rotateRight();
		if (outOfBounds() || coincide()) {
			tetromino.rotateLeft();
		}
	}

	/*向左旋转控制*/
	private void rotateLeftAction() {
		tetromino.rotateLeft();
		if (outOfBounds() || coincide()) {
			tetromino.rotateRight();
		}
	}

	//定义暂停.结束,timer控制
	private boolean pause = false;
	private boolean finish = false;
	private Timer timer;

	/*游戏开始控制*/
	private void gameStartAction() {
		cleanWall();
		pause = false;
		finish = false;
		lines = 0;
		score = 0;
		tetromino = Tetromino.randomOne();
		nextOne = Tetromino.randomOne();
		timer = new Timer();
		timer.schedule(new TimerTask() {
			public void run() {
				softDropAction();
				repaint();
			}
		}, 700, 700);
	}

	/*清墙算法*/
	private void cleanWall() {
		for(int row=0;row<walls.length;row++) {
			Arrays.fill(walls[row], null);
		}
	}

	/*暂停控制*/
	private void pauseAction() {
		pause = true;
		//取消timer绑定的一切流程
		timer.cancel();
	}

	/*continue控制*/
	private void continueAction() {
		pause = false;
		timer = new Timer();
		timer.schedule(new TimerTask() {
			public void run() {
				softDropAction();
				repaint();
			}
		}, 700, 700);
	}

	/*检测游戏是否结束*/
	private void checkGameOver() {
		if (walls[0][4]!=null) {
			finish = true;
			timer.cancel();
			repaint();
		}
	}

	/*Tetris类的main方法用于初始化界面*/
	public static void main(String[] args) {

		JFrame frame = new JFrame("俄罗斯方块");
		frame.setSize(525, 590);
		//无边框
		frame.setUndecorated(false);
		//总在最前
		frame.setAlwaysOnTop(true);
		//居中
		frame.setLocationRelativeTo(null);
		//frame.setLocation(420, 94); //居中
		Tetris tetris = new Tetris();
		//添加俄罗斯方块游戏到窗口框中
		frame.add(tetris);
		//设置背景颜色
		tetris.setBackground(new Color(0xFFFF00));
		//会尽快调用paint()方法
		frame.setVisible(true);
		//设置默认关闭窗口框时同时停止运行程序
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		tetris.action();
	}
}
