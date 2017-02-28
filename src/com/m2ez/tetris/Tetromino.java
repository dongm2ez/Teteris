package com.m2ez.tetris;

import java.util.Random;

/**
 * 方块类
 */

public abstract class Tetromino {
    protected Cell[] cells = new Cell[4];
    /**
     * 存储旋转状态
     */
    protected State[] states;
    /**
     * 存储旋转序号,定义直接量1000是防止玩家左转出现异常
     */
    protected int index = 10000;

    /**
     * 方块状态
     */
    protected class State {
        int row0, col0, row1, col1, row2, col2, row3, col3;

        public State(int row0, int col0, int row1, int col1, int row2,
                     int col2, int row3, int col3) {
            super();
            this.row0 = row0;
            this.col0 = col0;
            this.row1 = row1;
            this.col1 = col1;
            this.row2 = row2;
            this.col2 = col2;
            this.row3 = row3;
            this.col3 = col3;
        }
    }

    /**
     * 右旋转方法
     */
    public void rotateRight() {
        index++;
        int n = index % states.length;
        State s = states[n];// s1
        Cell o = cells[0];// 轴
        int row = o.getRow();
        int col = o.getCol();
        cells[1].setRow(row + s.row1);
        cells[1].setCol(col + s.col1);
        cells[2].setRow(row + s.row2);
        cells[2].setCol(col + s.col2);
        cells[3].setRow(row + s.row3);
        cells[3].setCol(col + s.col3);
    }

    /**
     * 左旋转方法
     */
    public void rotateLeft() {
        index--;
        int n = index % states.length;
        State s = states[n % 4];// s1
        Cell o = cells[0];// 轴
        int row = o.getRow();
        int col = o.getCol();
        cells[1].setRow(row + s.row1);
        cells[1].setCol(col + s.col1);
        cells[2].setRow(row + s.row2);
        cells[2].setCol(col + s.col2);
        cells[3].setRow(row + s.row3);
        cells[3].setCol(col + s.col3);
    }

    /**
     * 随机生成某个方块
     *
     * @return 生成某个类型的方块
     */
    public static Tetromino randomOne() {
        Random r = new Random();
        int type = r.nextInt(7);
        switch (type) {
            case 0:
                return new T();
            case 1:
                return new I();
            case 2:
                return new S();
            case 3:
                return new Z();
            case 4:
                return new J();
            case 5:
                return new L();
            case 6:
                return new O();
        }
        return null;
    }

    /**
     * 右移动方法
     */
    public void moveRight() {
        for (int i = 0; i < cells.length; i++) {
            Cell cell = cells[i];
            cell.moveRight();
        }
    }

    /**
     * 左移动方法
     */
    public void moveLeft() {
        for (int i = 0; i < cells.length; i++) {
            Cell cell = cells[i];
            cell.moveLeft();
        }
    }

    /**
     * 下落方法
     */
    public void softDrop() {
        for (int i = 0; i < cells.length; i++) {
            Cell cell = cells[i];
            cell.drop();
        }
    }
}

/**
 * T方块
 */
class T extends Tetromino {
    T() {
        cells[0] = new Cell(0, 4, Tetris.T);
        cells[1] = new Cell(0, 3, Tetris.T);
        cells[2] = new Cell(0, 5, Tetris.T);
        cells[3] = new Cell(1, 4, Tetris.T);
        // 旋转状态
        states = new State[4];
        states[0] = new State(0, 0, 0, -1, 0, 1, 1, 0);
        states[1] = new State(0, 0, -1, 0, 1, 0, 0, -1);
        states[2] = new State(0, 0, 0, 1, 0, -1, -1, 0);
        states[3] = new State(0, 0, 1, 0, -1, 0, 0, 1);
    }
}

/**
 * I方块
 */
class I extends Tetromino {
    I() {
        cells[0] = new Cell(0, 4, Tetris.I);
        cells[1] = new Cell(0, 3, Tetris.I);
        cells[2] = new Cell(0, 5, Tetris.I);
        cells[3] = new Cell(0, 6, Tetris.I);
        states = new State[2];
        states[0] = new State(0, 0, 0, -1, 0, 1, 0, 2);
        states[1] = new State(0, 0, -1, 0, 1, 0, 2, 0);
    }
}

/**
 * J方块
 */
class J extends Tetromino {
    J() {
        cells[0] = new Cell(0, 4, Tetris.J);
        cells[1] = new Cell(0, 3, Tetris.J);
        cells[2] = new Cell(0, 5, Tetris.J);
        cells[3] = new Cell(1, 5, Tetris.J);
        states = new State[]{new State(0, 0, 0, 1, 0, -1, -1, -1),
                new State(0, 0, 1, 0, -1, 0, -1, 1),
                new State(0, 0, 0, -1, 0, 1, 1, 1),
                new State(0, 0, -1, 0, 1, 0, 1, -1)};
    }
}

/**
 * L方块
 */
class L extends Tetromino {
    L() {
        cells[0] = new Cell(0, 4, Tetris.L);
        cells[1] = new Cell(0, 3, Tetris.L);
        cells[2] = new Cell(0, 5, Tetris.L);
        cells[3] = new Cell(1, 3, Tetris.L);
        states = new State[]{new State(0, 0, 0, 1, 0, -1, -1, 1),
                new State(0, 0, 1, 0, -1, 0, 1, 1),
                new State(0, 0, 0, -1, 0, 1, 1, -1),
                new State(0, 0, -1, 0, 1, 0, -1, -1)};
    }
}

/**
 * O方块
 */
class O extends Tetromino {
    O() {
        cells[0] = new Cell(0, 4, Tetris.O);
        cells[1] = new Cell(0, 5, Tetris.O);
        cells[2] = new Cell(1, 4, Tetris.O);
        cells[3] = new Cell(1, 5, Tetris.O);
        states = new State[]{new State(0, 0, 0, 1, 1, 0, 1, 1)};
    }
}

/**
 * S方块
 */
class S extends Tetromino {
    S() {
        cells[0] = new Cell(1, 4, Tetris.S);
        cells[1] = new Cell(0, 5, Tetris.S);
        cells[2] = new Cell(0, 6, Tetris.S);
        cells[3] = new Cell(1, 5, Tetris.S);
        states = new State[]{new State(0, 0, 0, 1, 1, -1, 1, 0),
                new State(0, 0, -1, 0, 1, 1, 0, 1)};
    }
}

/**
 * Z方块
 */
class Z extends Tetromino {
    Z() {
        cells[0] = new Cell(1, 4, Tetris.Z);
        cells[1] = new Cell(0, 3, Tetris.Z);
        cells[2] = new Cell(0, 4, Tetris.Z);
        cells[3] = new Cell(1, 5, Tetris.Z);
        states = new State[]{new State(0, 0, -1, -1, -1, 0, 0, 1),
                new State(0, 0, -1, 1, 0, 1, 1, 0)};
    }
}
