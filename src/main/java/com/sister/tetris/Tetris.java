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
 * ����˹������
 */

public class Tetris extends JPanel {

	private static final long serialVersionUID = 1L;
	/* ����*/
	private int score;
	/* ����������*/
	private int lines;
	public static final int ROWS = 20;
	public static final int COLS = 10;
	/* �ö�ά���鶨�����˹�����ǽ ǽ��Tertis�����*/
	private Cell[][] walls = new Cell[20][10];
	/*����������ĸ񷽿�*/
	private Tetromino tetromino;
	/* ��һ��������ĸ񷽿�*/
	private Tetromino nextOne;
	//ͼƬ�زĵļ���
	//��Ϸ����ͼƬ
	public static BufferedImage backGround;
	public static BufferedImage gameOver;
	public static BufferedImage I;
	public static BufferedImage J;
	public static BufferedImage L;
	public static BufferedImage O;
	public static BufferedImage S;
	public static BufferedImage T;
	public static BufferedImage Z;

	/*ʹ�þ�̬����������Ϸ��ͼƬ��Դ*/
	static {
		try {
			//���´�����Դ�package�ж�ȡ tetris.png ΪͼƬ����
			//ͼƬ�ļ����������com.tarena.tertis��װ��
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

	/*@Override��дpaint������Graphics g�󶨵�������*/
	public void paint(Graphics g) {
		//drawImage ��ͼ backGround ����ͼƬ
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

	//���嵥Ԫ��Ĵ�С����
	public static final int CELL_SIZE = 26;

	/*��ǽ*/
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

	/*������������ķ���*/
	private void drawTetromino(Graphics g) {
		//�������
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

	/*���Ƽ�������ķ���*/
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

	//��������Font����ɫ;
	public static final int FONT_COLOR = 0x667799;
	//��������Ĵ�С
	public static final int FONT_SIZE = 30;

	/*����Score ��Lines */
	private	void drawScore(Graphics g) {
		int x = 290;
		int y = 160;
		g.setColor(new Color(FONT_COLOR));
		/*Font����*/
		Font font = g.getFont();
		//�����ֺŴ�������
		font = new Font (font.getName(),Font.BOLD,FONT_SIZE);
		g.setFont(font);
		try {
			g.drawString("Scores=" + score +   "��" , x, y);
		} catch ( Exception e) {
			e.printStackTrace();
		}
		y+=56;
		g.drawString("Lines=" + lines + "��", x, y);
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

	/*������Ϸ��ʼ�ķ���*/
	public void action() {
		//tetromino = Tetromino.randomOne();
		//nextOne = Tetromino.randomOne();
		//������䣬���ڵ���ǽ�Ļ���
		//walls[19][3] = new Cell(19,3,S);
		//�������
		//System.out.println(tetromino);
		gameStartAction();
		/*�������̼���*/
		KeyListener l = new KeyAdapter() {
			/*�ڰ�������ʱ��ִ��*/
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


				//��ü��̵�keyֵ
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
				/**����ĵ���paint()�������ƽ��桡�������£�������40��ִ��softDrop() -
				 * �޸ĸ��ӵ����ݣ�repaint()-�������paint()-���ݵ�ǰ���ݣ����Ѿ��޸ĸ���
				 * �����ݣ����ƽ���
				 */
				repaint();
			}

		};
		//�ڵ�ǰ����ϰ��˼��̼���l
		this.addKeyListener(l);
		//Ϊ��ǰ�������������뽹��
		this.requestFocus();
	}

	/*�����ƶ����̿��Ʒ���*/
	private void moveRightAction () {
		tetromino.softMoveRight();
		if (outOfBounds() || coincide()) {
			tetromino.softMoveLeft();
		}
	}

	/* �����Ƿ񳬳���Χ�ļ���*/
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

	/*�����������ķ����Ƿ���ǽ�ϵķ����غϣ��غϼ��*/
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

	/**�����ƶ����̿��Ʒ���*/
	private void moveLeftAction() {
		tetromino.softMoveLeft();
		if (outOfBounds() || coincide()) {
			tetromino.softMoveRight();
		}
	}

	/*��������*/
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

	/*Tetris��������������̿��Ʒ���*/
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

	/*����ܷ�����*/
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

	/*��ǽ����*/
	private void landToWall() {
		Cell[] cells = tetromino.cells;
		for (int i=0;i<cells.length;i++) {
			Cell cell = cells[i];
			int row = cell.getRow();
			int col = cell.getCol();
			walls[row][col] = cell;
		}
	}

	//�������������ĵ÷�
	public static final int[] SCORE_SIZE = {0,10,50,100,400};

	/*���ٵ�Ԫ��������һ��*/
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

	/*���һ���Ƿ��������*/
	private boolean fullLines(int row) {
		Cell[] cells = walls[row];
		for (int i=0;i<cells.length;i++) {
			if (cells[i]==null) {
				return false;
			}
		}
		return true;
	}


	/*�Ƴ�����*/
	private void removeLines(int row) {
		for (int i=row;i>0;i--) {
			System.arraycopy(walls[i-1], 0, walls[i], 0, walls[i-1].length);
		}
		Arrays.fill(walls[0], null);
	}

	/*������ת����*/
	private void rotateRightAction() {
		tetromino.rotateRight();
		if (outOfBounds() || coincide()) {
			tetromino.rotateLeft();
		}
	}

	/*������ת����*/
	private void rotateLeftAction() {
		tetromino.rotateLeft();
		if (outOfBounds() || coincide()) {
			tetromino.rotateRight();
		}
	}

	//������ͣ.����,timer����
	private boolean pause = false;
	private boolean finish = false;
	private Timer timer;

	/*��Ϸ��ʼ����*/
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

	/*��ǽ�㷨*/
	private void cleanWall() {
		for(int row=0;row<walls.length;row++) {
			Arrays.fill(walls[row], null);
		}
	}

	/*��ͣ����*/
	private void pauseAction() {
		pause = true;
		//ȡ��timer�󶨵�һ������
		timer.cancel();
	}

	/*continue����*/
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

	/*�����Ϸ�Ƿ����*/
	private void checkGameOver() {
		if (walls[0][4]!=null) {
			finish = true;
			timer.cancel();
			repaint();
		}
	}

	/*Tetris���main�������ڳ�ʼ������*/
	public static void main(String[] args) {

		JFrame frame = new JFrame("����˹����");
		frame.setSize(525, 590);
		//�ޱ߿�
		frame.setUndecorated(false);
		//������ǰ
		frame.setAlwaysOnTop(true);
		//����
		frame.setLocationRelativeTo(null);
		//frame.setLocation(420, 94); //����
		Tetris tetris = new Tetris();
		//��Ӷ���˹������Ϸ�����ڿ���
		frame.add(tetris);
		//���ñ�����ɫ
		tetris.setBackground(new Color(0xFFFF00));
		//�ᾡ�����paint()����
		frame.setVisible(true);
		//����Ĭ�Ϲرմ��ڿ�ʱͬʱֹͣ���г���
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		tetris.action();
	}
}
