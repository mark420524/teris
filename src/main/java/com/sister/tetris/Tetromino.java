package com.sister.tetris;

import java.util.Arrays;
import java.util.Random;
/** �ĸ񷽿�*/
public abstract class Tetromino {

	protected  Cell[] cells = new Cell[4];
	//��ת״̬
	protected State[] states;
	protected int index = 10000;
	/*˽�й��췽��*/
	private Tetromino() {

	}

	/*��ȡ��ת״̬*/
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

	/*����ת*/
	public void rotateRight() {
		/*
		 *1.��õ�ǰ��
		 * 2.����¸�״̬�ı仯����
		 * 3.���üӷ��������ݱ仯
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

	/**�򵥹���������һ���ĸ񷽿飬ֻ��ͨ������������������ĸ񷽿��ʵ��
	 * Ҳ����˵��ֻ��ʹ������������ɷ��飬��������
	 *
	 */
	public static Tetromino randomOne() {
		Random random = new Random();
		int type = random.nextInt(7);
		switch (type) {

			case 0: return new I();   //��̬����ֻ��ʹ�þ�̬�ڲ���
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

	/*��дtoString����*/
	public String toString () {
		return Arrays.toString(cells);
	}
	/*�����ڲ����װ������ʵ�ֵ�ϸ��*/
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
