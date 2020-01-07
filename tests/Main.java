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
		int productsAmount = 1000;
		int d = 15;
		double lambda = 0.1;
		int iters = 80;
		long millisActualTime;
		long executionTime;
		
		String[] category = { "Book", "Music", "DVD", "Video" };
		
		Matrix R = Pivot.pivot(Parser.parseData(++productsAmount, category[0]));
		Matrix P = new Matrix(d, R.getColumns()); P.fillWithRandomStuff();
		Matrix U = new Matrix(d, R.getRows()); U.fillWithRandomStuff();
		Matrix T = ALS.prepTestMatrix(R);
		//System.out.print("Iloœæ u¿ytkowników: " + U.getColumns() + "\nIloœæ produktów: " + P.getColumns() + "\n");
		
		millisActualTime = System.currentTimeMillis();
		ALS.full(iters, lambda, d, R, U, P);
		executionTime = System.currentTimeMillis() - millisActualTime;
		
		Matrix R2 = ALS.recommendationsTest(iters, lambda, d, R, U, P);
		
		System.out.println("Error: " + ALS.calculateError(T, R2, iters, lambda, d) + ", czas wykonania: " + executionTime);
	}
	
}