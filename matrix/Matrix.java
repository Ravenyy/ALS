package matrix;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.util.stream.DoubleStream;

@SuppressWarnings("unchecked")
public class Matrix {

	private int rows;
	private int columns;
	private double matrix[][];
	
	public Matrix(int rows, int columns) {
		this.rows = rows;
		this.columns = columns;
        this.matrix = new double[rows][columns];

		for(int i = 0; i < rows; i++) {
			for(int j = 0; j < columns; j++)
				matrix[i][j] = 0.0;
		}
    }

	public Matrix(double[][] tab) {
		this.rows = tab.length;
        this.columns = tab[0].length;
        this.matrix = tab.clone();
	}
		
	public Matrix(double[] tab) {
		this.rows = tab.length;
        this.columns = 1;
        this.matrix = new double[rows][columns];
        for(int i = 0; i < rows; i++)
        	this.matrix[i][0] = tab[i];
	}

	public Matrix(Matrix matrix) {
		this.rows = matrix.getRows();
		this.columns = matrix.getColumns();
		this.matrix = new double[rows][columns];
		this.matrix = matrix.getMatrix().clone();
	}
	
	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public int getColumns() {
		return columns;
	}

	public void setColumns(int columns) {
		this.columns = columns;
	}

	public double[][] getMatrix() {
		return matrix;
	}

	public void setMatrix(double[][] matrix) {
		this.matrix = matrix;
	}
	
	public void setCell(double value, int i, int j) {
		this.matrix[i][j] = value;
	}
	
	public double getCell(int row, int column) {
		return matrix[row][column];
	}
	
	public Matrix getColumn(int index) {
		Matrix A = new Matrix(this.getRows(), 1);
		for(int i = 0; i < this.getRows(); i++)
			A.setCell(this.getCell(i, index), i, 0);
		return A;
	}
	
	public Matrix getRow(int index) {
		Matrix A = new Matrix(this.getColumns(), 1);
		for(int i = 0; i < this.getColumns(); i++)
			A.setCell(this.getCell(index, i), i, 0);
		return A;
	}
	
	public void fillMatrix(double x) {
		for(int i = 0; i < rows; i++)
			for(int j = 0; j < columns; j++)
				matrix[i][j] = x;
	}
	
	public void fillWithRandomStuff() {
		for(int i = 0; i < rows; i++)
			for(int j = 0; j < columns; j++) 
				this.setCell(Math.random(), i, j);
	}
	
	public static Matrix identityMatrix(int d) {
		Matrix A = new Matrix(d, d);
		for(int i = 0; i < d; i++)
			for(int j = 0; j < d; j++) {
				if(i == j)
					A.setCell(1, i, j);
				else
					A.setCell(0, i, j);
			}
		return A;
	}
	
	public void switchColumn(Matrix column, int index) {
		Matrix A = this;
		for(int i = 0; i < A.getRows(); i++)
			A.setCell(column.getCell(i, 0), i, index);
	}

	public Matrix transpose() {  
		Matrix A = new Matrix(columns, rows);
		for (int i = 0; i < rows; i++)
			for (int j = 0; j < columns; j++)
				A.matrix[j][i] = this.matrix[i][j];
		return A;
	}
	
	public void swapRows(int i, int j) {
		double[] tmp = matrix[i];
        matrix[i] = matrix[j];
        matrix[j] = tmp;
	}
	
	public void swapColumns(int i, int j) {
		double tmp;
		for(int k = 0; k < rows; k++) {
			tmp = matrix[k][i];
			matrix[k][i] = matrix[k][j];
			matrix[k][j] = tmp;
		}
	}
	
	public void absVector() {  
		for (int i = 0; i < this.getRows(); i++)
			this.setCell(Math.abs(this.getCell(i, 0)), i, 0);
	}
	
	public double vectorAvg() {
		double sum = 0.0;
		for(int i = 0; i < this.rows; i++)
			for(int j = 0; j < this.columns; j++)
				sum = sum + this.getCell(i, j);
		return sum / (this.rows * this.columns);
	}
	
	public double vectorNorm() {
		double sum = 0.0;
		for(int i = 0; i < this.rows; i++)
			for(int j = 0; j< this.columns; j++)
				sum = sum + (this.getCell(i, j) * this.getCell(i, j));
		return Math.sqrt(sum);
	}
	
	public static double multiplyVectors(Matrix A, Matrix B) {
		double result = 0;
		for(int i = 0; i < A.getColumns(); i++)
			result += A.getCell(i, 0) * B.getCell(i, 0);
		return result;
	}
		
	public Matrix plus(Matrix B) {
		Matrix A = this;
		if (B.rows != A.rows || B.columns != A.columns) throw new RuntimeException("Wrong matrix dimensions");
		Matrix W = new Matrix(rows, columns);
		for (int i = 0; i < rows; i++)
			for (int j = 0; j < columns; j++)
				W.setCell(A.matrix[i][j] + B.matrix[i][j], i, j);
		return W;
	}
	
	public Matrix minus(Matrix B) {
		Matrix A = this;
		if (B.rows != A.rows || B.columns != A.columns) throw new RuntimeException("Wrong matrix dimensions");
		Matrix W = new Matrix(rows, columns);
		for (int i = 0; i < rows; i++)
			for (int j = 0; j < columns; j++)
				W.setCell(A.matrix[i][j] - B.matrix[i][j], i, j);
		return W;
	}

	public Matrix times(Matrix B){
		Matrix A = this;
		if (A.columns != B.rows) throw new RuntimeException("Wrong matrix dimensions");
		Matrix W = new Matrix(A.rows, B.columns);
		for (int i = 0; i < W.rows; i++)
			for (int j = 0; j < W.columns; j++)
				for (int k = 0; k < A.columns; k++)
					W.setCell(W.matrix[i][j] + (A.matrix[i][k] * B.matrix[k][j]), i, j);
		return W;
	}
	
	public Matrix multiplyCells(double value) {
		Matrix A = this;
		Matrix B = new Matrix(A.rows, A.columns);
		for(int i = 0; i < A.rows; i++)
			for(int j = 0; j < A.columns; j++)
				B.setCell(value*A.getCell(i, j), i, j);
		return B;
	}
	
	public void toFile(String fileName) {
		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(fileName);
			PrintWriter printWriter = new PrintWriter(fileWriter);
			printWriter.print(this.toString());
			printWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void print() {
		Matrix A = this;
		for(int i = 0; i < A.rows; i++) {
			for(int j = 0; j < A.columns; j++)
				System.out.print(A.getCell(i, j) + ", ");
		System.out.println();
		}
		System.out.println();
	}
	
	public void removeMinusZeros(Matrix m) {
		for(int i = 0; i < m.getRows(); i++)
			for(int j = 0; j < m.getColumns(); j++)
				if(m.getCell(i, j) == -0.0)
					m.matrix[i][j] = Math.abs(m.matrix[i][j]);
	}
	
	private void gaussSetResult(Matrix vector, Matrix result, Matrix matrix) {
		for (int i = vector.rows - 1; i >= 0; i--) {
			double sum = 0.0;

			for (int j = i + 1; j < vector.rows; j++)
				sum = (sum + (matrix.matrix[i][j] * result.matrix[j][0]));
			
			result.matrix[i][0] = ((vector.matrix[i][0] - sum) / matrix.matrix[i][i]);
		}
	}
	
	private void buildSteppedMatrix(Matrix matrix, Matrix vector, int i) {
		double param = 0.0;
		for (int j = i + 1; j < vector.rows; j++) {
			param = (matrix.matrix[j][i] / matrix.matrix[i][i]);
			vector.matrix[j][0] = (vector.matrix[j][0] - (param * vector.matrix[i][0]));

			for (int k = i; k < vector.rows; k++)
				matrix.matrix[j][k] = (matrix.matrix[j][k] - (param * matrix.matrix[i][k]));
		}
	}
	
	// eliminacja gaussa bez wyboru elementu podstawowego
	public Matrix gaussG(Matrix vector) {
		Matrix matrix = new Matrix(this);
        Matrix result = new Matrix(vector.rows, 1);

        for (int i = 0; i < vector.rows; i++)
        	buildSteppedMatrix(matrix, vector, i);
        
        gaussSetResult(vector, result, matrix);
        
        return result;
    }
	
	// eliminacja gaussa z czesciowym wyborem elementu podstawowego
	public Matrix gaussPG(Matrix vector) {
        Matrix matrix = new Matrix(this);
        Matrix result = new Matrix(vector.rows, 1);

        for (int i = 0; i < vector.rows; i++) {
        	int max = i;
        	for (int j = i + 1; j < vector.rows; j++)
        		if (Math.abs(matrix.matrix[j][i]) == Math.abs(matrix.matrix[max][i]))
        			max = j;
        	
        	matrix.swapRows(i, max);
        	vector.swapRows(i, max);

        	buildSteppedMatrix(matrix, vector, i);
        }

        gaussSetResult(vector, result, matrix);

        return result;
    }

	// eliminacja gaussa z pelnym wyborem elemenetu podstawowego
    public Matrix gaussFG( Matrix vector) {
        Matrix matrix = new Matrix(this);
        Matrix result = new Matrix(vector.rows, 1);
        Matrix originalResult = new Matrix(vector.rows, 1);

        int[] originalPosition;
        originalPosition= new int[vector.rows];

        for (int j = 0; j < vector.rows; j++)
            originalPosition[j]=j;

        
        for (int i = 0; i < vector.rows; i++) {
        	int maxRow = i;
        	int maxColumn = i;

        	for (int j = i; j < matrix.rows; j++) {
        		for (int k = i; k < matrix.columns; k++) {
        			if (Math.abs(matrix.matrix[j][k]) == Math.abs(matrix.matrix[maxRow][maxColumn])) {
        				maxRow = j;
        				maxColumn = k;
        			}
        		}
        	}

        	int tmp = originalPosition[i];
        	originalPosition[i] = originalPosition[maxColumn];
        	originalPosition[maxColumn] = tmp;
        	
        	matrix.swapRows(i,  maxRow);
        	matrix.swapColumns(i, maxColumn);
        	vector.swapRows(i, maxRow);

        	buildSteppedMatrix(matrix, vector, i);
        }

        gaussSetResult(vector, result, matrix);

        for (int j = 0; j < vector.rows; j++)
        	originalResult.matrix[originalPosition[j]][0] = result.matrix[j][0];

        return originalResult;
    }
    
	// Opt budowa macierzy schodkowej 
	private void buildSteppedMatrixOpt(Matrix matrix, Matrix vector, int i) {
		double param = 0;
		for (int j = i + 1; j < vector.rows; j++) {
			if(matrix.matrix[j][i] != 0) {
				param = (matrix.matrix[j][i] / matrix.matrix[i][i]);
				vector.matrix[j][0] = (vector.matrix[j][0] - (param * vector.matrix[i][0]));
					
				for (int k = i; k < vector.rows; k++)
					matrix.matrix[j][k] = (matrix.matrix[j][k] - (param * matrix.matrix[i][k]));
			}
		}
	}

	// Opt eliminacja gaussa z czesciowym wyborem elementu podstawowego 
	public Matrix gaussPGOpt(Matrix vector) {
		Matrix result = new Matrix(vector.rows, 1);

		for (int i = 0; i < vector.rows; i++) {
			int max = i;
			for (int j = i + 1; j < vector.rows; j++)
				if (Math.abs(this.matrix[j][i]) > Math.abs(this.matrix[max][i]))
					max = j;
	        	
			this.swapRows(i, max);
			vector.swapRows(i, max);
	        	
			buildSteppedMatrixOpt(this, vector, i);
		}
		gaussSetResult(vector, result, this);

		return result;
	}
}