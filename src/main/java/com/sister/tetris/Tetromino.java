package com.sister.tetris;

import java.util.Arrays;
import java.util.Random;
/** 四格方块*/
public abstract class Tetromino {

	protected  Cell[] cells = new Cell[4];
	//旋转状态
	protected State[] states;
	protected int index = 10000;
	/*私有构造方法*/
	private Tetromino() {

	}

	/*获取旋转状态*/
	private class State {
		int row1;
		int col1;
		int row2;
		int col2;
		int row3;
		int col3;
		public State(int row1, int col1, int row2, int col2, int row3, int col3) {
			super();
			this.row1 = row1;
			this.col1 = col1;
			this.row2 = row2;
			this.col2 = col2;
			this.row3 = row3;
			this.col3 = col3;
		}

	}

	/*向右转*/
	public void rotateRight() {
		/*
		 *1.获得当前轴
		 * 2.获得下个状态的变化数据
		 * 3.利用加法事项数据变化
		 */

		Cell o = cells[0];
		int row = o.getRow();
		int col = o.getCol();
		index++;
		State s = states[index%states.length];
		cells[1].setRow(row+s.row1);
		cells[1].setCol(col+s.col1);
		cells[2].setRow(row+s.row2);
		cells[2].setCol(col+s.col2);
		cells[3].setRow(row+s.row3);
		cells[3].setCol(col+s.col3);

	}

	public void rotateLeft() {

		Cell o = cells[0];
		int row = o.getRow();
		int col = o.getCol();
		index--;
		State s = states[index%states.length];
		cells[1].setRow(row+s.row1);
		cells[1].setCol(col+s.col1);
		cells[2].setRow(row+s.row2);
		cells[2].setCol(col+s.col2);
		cells[3].setRow(row+s.row3);
		cells[3].setCol(col+s.col3);
	}

	/**简单工厂法生成一个四格方块，只能通过这个工厂方法创建四格方块的实例
	 * 也就是说，只能使用这个方法生成方块，别无他法
	 *
	 */
	public static Tetromino randomOne() {
		Random random = new Random();
		int type = random.nextInt(7);
		switch (type) {

			case 0: return new I();   //静态方法只能使用静态内部类
			case 1: return new O();
			case 2: return new J();
			case 3: return new L();
			case 4: return new Z();
			case 5: return new S();
			case 6: return new T();
		}

		return null;
	}

	public void softDrop() {
		for (int i=0;i<cells.length;i++) {
			cells[i].drop();
		}
	}

	public void softMoveLeft() {
		for (int i=0;i<cells.length;i++) {
			cells[i].moveLeft();
		}
	}

	public void softMoveRight() {
		for (int i=0;i<cells.length;i++) {
			cells[i].moveRight();
		}
	}

	/*重写toString方法*/
	public String toString () {
		return Arrays.toString(cells);
	}
	/*利用内部类封装了子类实现的细节*/
	private static class I extends Tetromino {
		public I() {
			cells[0] = new Cell(0, 4, Tetris.I);
			cells[1] = new Cell(0, 3, Tetris.I);
			cells[2] = new Cell(0, 5, Tetris.I);
			cells[3] = new Cell(0, 6, Tetris.I);
			states = new State[2];
			states[0] = new State(0,-1,0,1,0,2);
			states[1] = new State(-1,0,1,0,2,0);
		}
	}

	private static class J extends Tetromino {
		public J() {
			cells[0] = new Cell(0, 4, Tetris.J);
			cells[1] = new Cell(0, 3, Tetris.J);
			cells[2] = new Cell(0, 5, Tetris.J);
			cells[3] = new Cell(1, 5, Tetris.J);
			states = new State[4];
			states[0] = new State(0,-1,0,1,1,1);
			states[1] = new State(-1,0,1,0,1,-1);
			states[2] = new State(0,1,0,-1,-1,-1);
			states[3] = new State(1,0,-1,0,-1,1);
		}
	}

	private static class L extends Tetromino {
		public L() {
			cells[0] = new Cell(0, 4, Tetris.L);
			cells[1] = new Cell(0, 3, Tetris.L);
			cells[2] = new Cell(0, 5, Tetris.L);
			cells[3] = new Cell(1, 3, Tetris.L);
			states = new State[4];
			states[0] = new State(0,-1,0,1,1,-1);
			states[1] = new State(-1,0,1,0,-1,-1);
			states[2] = new State(0,1,0,-1,-1,1);
			states[3] = new State(1,0,-1,0,1,1);
		}
	}

	private static class O extends Tetromino {
		public O() {
			cells[0] = new Cell(0, 4, Tetris.O);
			cells[1] = new Cell(0, 5, Tetris.O);
			cells[2] = new Cell(1, 4, Tetris.O);
			cells[3] = new Cell(1, 5, Tetris.O);
			states = new State[2];
			states[0] = new State(0,1,1,0,1,1);
			states[1] = new State(0,1,1,0,1,1);
		}
	}

	private static class S extends Tetromino {
		public S() {
			cells[0] = new Cell(0, 4, Tetris.S);
			cells[1] = new Cell(0, 5, Tetris.S);
			cells[2] = new Cell(1, 3, Tetris.S);
			cells[3] = new Cell(1, 4, Tetris.S);
			states = new State[2];
			states[0] = new State(0,1,1,-1,1,0);
			states[1] = new State(1,0,-1,-1,0,-1);
		}
	}

	private static class T extends Tetromino {
		public T() {
			cells[0] = new Cell(0,4,Tetris.T);
			cells[1] = new Cell(0,3,Tetris.T);
			cells[2] = new Cell(0,5,Tetris.T);
			cells[3] = new Cell(1,4,Tetris.T);
			states = new State[4];
			states[0] = new State(0,-1,0,1,1,0);
			states[1] = new State(-1,0,1,0,0,-1);
			states[2] = new State(0,1,0,-1,-1,0);
			states[3] = new State(1,0,-1,0,0,1);
		}
	}

	private static class Z extends Tetromino {
		public Z() {
			cells[0] = new Cell(1, 4, Tetris.Z);
			cells[1] = new Cell(0, 3, Tetris.Z);
			cells[2] = new Cell(0, 4, Tetris.Z);
			cells[3] = new Cell(1, 5, Tetris.Z);
			states = new State[2];
			states[0] = new State(0,-1,1,0,1,1);
			states[1] = new State(-1,0,0,-1,1,-1);
		}
	}

}
