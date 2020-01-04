package tests;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import matrix.ALS;
import matrix.Data;
import matrix.Matrix;
import matrix.Parser;
import matrix.Pivot;

public class Main {
	public static void main (String[] args) throws IOException {
		int productsAmount = 10;
		int d = 3;
		double lambda = 0.1;
		int iters = 100;
		String[] category = {"Book", "Music", "DVD", "Video"};
		
		Matrix ratings = Pivot.pivot(Parser.parseData(++productsAmount, category[0]));

		Matrix P = new Matrix(d, ratings.getColumns());
		P.fillWithRandomStuff();
		Matrix U = new Matrix(d, ratings.getRows());
		U.fillWithRandomStuff();
		
		/*
		Matrix ratings = new Matrix(5, 10);
		ratings.setCell(4, 0, 4);
		ratings.setCell(5, 0, 6);
		ratings.setCell(4, 0, 7);
		ratings.setCell(4, 1, 0);
		ratings.setCell(4, 1, 2);
		ratings.setCell(4, 1, 5);
		ratings.setCell(4, 1, 9);
		ratings.setCell(5, 2, 0);
		ratings.setCell(4, 2, 1);
		ratings.setCell(5, 2, 2);
		ratings.setCell(4, 2, 3);
		ratings.setCell(4, 2, 5);
		ratings.setCell(4, 2, 6);
		ratings.setCell(4, 2, 7);
		ratings.setCell(4, 2, 8);
		ratings.setCell(4, 2, 9);
		ratings.setCell(5, 3, 1);
		ratings.setCell(5, 3, 2);
		ratings.setCell(5, 3, 4);
		ratings.setCell(5, 3, 7);
		ratings.setCell(5, 3, 9);
		ratings.setCell(5, 4, 1);
		ratings.setCell(5, 4, 2);
		ratings.setCell(5, 4, 4);
		ratings.setCell(5, 4, 7);
		ratings.setCell(5, 4, 9);
		
		Matrix U = new Matrix(d, 3);
		Matrix P = new Matrix(d, productsAmount);
		double[] tempP = {0.93119636, 0.01215318, 0.82254304, 0.92704314, 0.72097256,
		               0.1119594 , 0.05907673, 0.27337659, 0.51578453, 0.47299487,
		              0.1671686 , 0.02328032, 0.64793332, 0.46310597, 0.98508579,
		               0.23390272, 0.34862754, 0.29751156, 0.81994987, 0.32293732,
		              0.72302848, 0.91165485, 0.70980305, 0.20125138, 0.33071352,
		               0.40941998, 0.6984816 , 0.94986196, 0.52719633, 0.66722182};
		double[] tempU = {0.02930222, 0.90635812, 0.71271017,
		                  0.03319273, 0.2316068 , 0.96492267,
		                  0.35638381, 0.42064508, 0.83929454};
		int row = -1;
		for(int i = 0; i < tempP.length; i++) {
			if(i % productsAmount == 0)
				row++;
			P.setCell(tempP[i], row, i % productsAmount);
		}
		
		int roww = -1;
		for(int i = 0; i < tempU.length; i++) {
			if(i % 3 == 0)
				roww++;
			U.setCell(tempU[i], roww, i % 3);
		}
		*/
		
		ALS.full(productsAmount, iters, lambda, d, ratings, U, P);
		
		//Matrix test = ALS.recommendationsTest(productsAmount, iters, lambda, d, ratings, U, P);
		//test.print();
		ratings.print();
		U.print();
		P.print();
		/*Matrix newU = ALS.getNewU(d, reg, U, P, ratings);
		Matrix newP = ALS.getNewP(d, reg, U, P, ratings);
		
		newU.print();
		newP.print();*/
	}
}