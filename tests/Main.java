package tests;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import matrix.ALS;
import matrix.Data;
import matrix.Matrix;
import matrix.Parser;
import matrix.Pivot;

public class Main {
	public static void main(String[] args) throws IOException {
		int productsAmount = 10;
		int d = 3;
		double lambda = 0.1;
		int iters = 120;
		String[] category = { "Book", "Music", "DVD", "Video" };
		
		Matrix R = Pivot.pivot(Parser.parseData(++productsAmount, category[0]));
		Matrix P = new Matrix(d, R.getColumns()); P.fillWithRandomStuff();
		Matrix U = new Matrix(d, R.getRows()); U.fillWithRandomStuff();
		System.out.println("Kolumny: " + R.getColumns() + ", Wiersze: "+ R.getRows());
		Matrix T = ALS.prepTestMatrix(R);
		System.out.print("Iloœæ u¿ytkowników: " + U.getColumns() + "\nIloœæ produktów: " + P.getColumns() + "\n");
		ALS.full(iters, lambda, d, R, U, P);
		Matrix R2 = U.transpose().times(P);
		T.print();
		//System.out.println("Macierz przewidzianych ocen:");
		R2.print();
		//Matrix T = ALS.recommendationsTest(iters, lambda, d, R, U, P);
		//T.print();
		System.out.println("Mean squared error: " + calculateMSE(R, R2));
	}
		 
/*
		double[][] tutR = { { 0, 0, 0, 0, 4, 0, 5, 4, 0, 0 }, { 4, 0, 4, 0, 0, 4, 0, 0, 0, 4 },
				{ 5, 4, 5, 5, 0, 5, 5, 5, 5, 5 }, { 0, 5, 5, 0, 5, 0, 0, 5, 0, 5 }, { 0, 5, 5, 0, 5, 0, 0, 5, 0, 5 } };
		Matrix R = new Matrix(tutR);

		double[][] tutP = {
				{ 0.93119636, 0.01215318, 0.82254304, 0.92704314, 0.72097256, 0.1119594, 0.05907673, 0.27337659,
						0.51578453, 0.47299487 },
				{ 0.1671686, 0.02328032, 0.64793332, 0.46310597, 0.98508579, 0.23390272, 0.34862754, 0.29751156,
						0.81994987, 0.32293732 },
				{ 0.72302848, 0.91165485, 0.70980305, 0.20125138, 0.33071352, 0.40941998, 0.6984816, 0.94986196,
						0.52719633, 0.66722182 } };

		double[][] tutU = { { 0.02930222, 0.90635812, 0.71271017, 0.53543654, 0.84654834 },
				{ 0.03319273, 0.2316068, 0.96492267, 0.65463874, 0.32423875 },
				{ 0.35638381, 0.42064508, 0.83929454, 0.55634457, 0.34575434 } };
		Matrix U = new Matrix(tutU);
		Matrix P = new Matrix(tutP);

		ALS.full(productsAmount, iters, lambda, d, R, U, P);

		Matrix R2 = U.transpose().times(P);
		System.out.println("Macierz przewidzianych ocen:");
		R2.print();
		System.out.println("Mean squared error: " + calculateMSE(R, R2));
	}*/

	public static double calculateMSE(Matrix R, Matrix R2) {
		double error = 0;
		int count = R2.getRows() * R2.getColumns();
		for (int i = 0; i < R2.getRows(); i++) {
			for (int j = 0; j < R2.getColumns(); j++) {
				double value = R.getCell(i, j);
				double predictedValue = R2.getCell(i, j);
				if (value != 0) {
					error += Math.pow(value - predictedValue, 2);
				}
			}
		}
		return error / count;
	}
}