package com.m2ez.tetris;
import java.awt.image.BufferedImage;
/**
 * 格子类
 */
public class Cell {
	private int row;
	private int col;
	private BufferedImage image;
	public Cell(int row,int col,BufferedImage image){
		this.row=row;
		this.col=col;
		this.image=image;
	}
	public void moveRight() {
		col++;
	}
	public void moveLeft(){
		col--;
	}
	public void drop(){
		row++;
	}
	public int getRow() {
		return row;
	}
	public void setRow(int row) {
		this.row = row;
	}
	public int getCol() {
		return col;
	}
	public void setCol(int col) {
		this.col = col;
	}
	public BufferedImage getImage() {
		return image;
	}
	public void setImage(BufferedImage image) {
		this.image = image;
	}
	public String toString(){
		return row+","+col;
	}
}
