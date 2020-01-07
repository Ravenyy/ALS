package matrix;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class ALS {
	
    public static Matrix prepTestMatrix(Matrix R) {
    	
    	Matrix T = new Matrix(R.getRows(), R.getColumns());
    	for(int i = 0; i < R.getRows(); i++)
    		for(int j = 0; j < R.getColumns(); j++)
    			T.setCell(R.getCell(i, j), i, j);
    	
    	
        int[] pos = new int[2];
        int counter = 0;
        for(int i = 0; i < R.getRows(); i++) {
            counter = 0;
            for(int j = 0; j < R.getColumns(); j++) {
                if(R.getCell(i, j) != 0.0) {
                    pos[0] = i; pos[1] = j;
                    counter++;
                }
            }
            if(counter >= 2) {
                T.setCell(0.0, pos[0], pos[1]);
            }
        }
        return T;
    }

	public static Matrix recommendationsTest(int iters, double lambda, int d, Matrix ratings, Matrix U, Matrix P) {
		Matrix test = new Matrix(ratings.getRows(), ratings.getColumns());
		double functionResult = 0;
		double multStuff = 0;
		updateU(d, lambda, U, P, ratings);
		updateP(d, lambda, U, P, ratings);
		for(int i = 0; i < U.getColumns(); i++)
			for(int j = 0; j < P.getColumns(); j++) {
				multStuff = Matrix.multiplyVectors(U.getColumn(i), P.getColumn(j));
				functionResult = round(multStuff, 2);
				test.setCell(functionResult, i, j);
			}
		return test;
	}
	
	public static void full(int iters, double lambda, int d, Matrix ratings, Matrix U, Matrix P) throws IOException {
		double prev_result = 0;
		double result = 0;
		Matrix test = new Matrix(ratings.getRows(), ratings.getColumns());
		for(int i = 0; i < iters; i++) {
			updateU(d, lambda, U, P, ratings);
			updateP(d, lambda, U, P, ratings);
			prev_result = result;
			result = calculateTarget(U, P, lambda, ratings);
			toFile(("result" + iters + "_"+ lambda + "_" + d + ".txt"), (i + "; " + result));
			System.out.println("RESULT: " + result);
		}
	}
	
	public static double calculateError(Matrix R, Matrix R2, int iters, double lambda, int d) {
		double error = 0;
		int count = 0;
		for (int i = 0; i < R2.getRows(); i++) {
			for (int j = 0; j < R2.getColumns(); j++) {
				double value = R.getCell(i, j);
				double predictedValue = R2.getCell(i, j);
				if (value != 0) {
					count++;
					error += Math.abs(value - predictedValue);
				}
			}
		}
		return error / count;
	}
	
    public static double calculateTarget(Matrix U, Matrix P, double lambda, Matrix ratings) {
        double result, a, b, c;
        a = b = c = 0;
        Matrix colU;
        Matrix colP;
        for(int i = 0; i < U.getColumns(); i++) {
            colU = U.getColumn(i);
            b += Math.pow(colU.vectorNorm(), 2);
            for(int j = 0; j < P.getColumns(); j++) {
                colP = P.getColumn(j);
                if(ratings.getCell(i, j) != 0) 
                    a += Math.pow((ratings.getCell(i, j) - Matrix.multiplyVectors(colU, colP)), 2);
            }
        }
        for(int j = 0; j < P.getColumns(); j++) {
            colP = P.getColumn(j);
            c += Math.pow(colP.vectorNorm(), 2);
        }

        result = a + lambda * (b + c);
        //System.out.println("a = " + a + "\tb = " + b + "\tc = " + c);
        //System.out.println("Result = " + result);
        return result;
    }
		
	//krok 4 algorytmu z Zad3.pdf i tutoriala
	public static void updateU(int d, double reg, Matrix U, Matrix P, Matrix ratings) {
	int u = U.getColumns();
	int p = P.getColumns();
	
	for(int user = 0; user < u; user++) {
		//I_u - lista indeksow u¿ytkownikow, którzy ocenili dany przedmiot
		int findItemIndex = 0;
		List<Integer> I_u = new ArrayList<Integer>();
		for(int i = 0; i < p; i++) {
			if(ratings.getCell(user, i) != 0)
				I_u.add(findItemIndex);
			findItemIndex++;
		}
		
		//P_I_u - kolumny z macierzy P o indeksach I_u
		Matrix P_I_u = new Matrix(d, I_u.size());
		List<Double> P_I_uTempList = new ArrayList<Double>();
		for(int i = 0; i < d; i++)
			for(int j = 0; j < p; j++)
				if(I_u.contains(j))
					P_I_uTempList.add(P.getCell(i, j));
		
		//Ten ma³y potwór przerabia listê na Matrix
		int row = -1;
		for(int i = 0; i < P_I_uTempList.size(); i++) {
			if(i % P_I_u.getColumns() == 0)
				row++;
			P_I_u.setCell(P_I_uTempList.get(i), row, i % P_I_u.getColumns());
		}
		
		//Macierz A_u
		Matrix P_I_u_T = P_I_u.transpose();
		Matrix E = Matrix.identityMatrix(d);
		Matrix A_u = P_I_u.times(P_I_u_T).plus(E.multiplyCells(reg));
		
		//Macierz V_u, czyli kolumny z macierzy P o indeksach z I_u pomno¿one przez ocenê
		List<Matrix> tempRatings = new ArrayList<Matrix>();
		Matrix col = new Matrix(d, 1);
		for(int i = 0; i < I_u.size(); i++)
			for(int j = 0; j < p; j ++) 
				if(j == I_u.get(i)) {
					col = P_I_u.getColumn(i).multiplyCells(ratings.getCell(user, j));
					tempRatings.add(col);
				}
		Matrix V_u = new Matrix(d, 1);
		for(Matrix mat : tempRatings)
			V_u = V_u.plus(mat);
		
		//Wynik kroku 4. z algorytmu z Zad3.pdf i tutoriala
		Matrix result = A_u.gaussPG(V_u);
		
		
		/*System.out.print("Lista Iu: " + I_u + "\n");
		System.out.print("Macierz Au\n"); A_u.print();
		System.out.print("Macierz PIu\n"); P_I_u.print();
		System.out.print("Macierz Vu\n"); V_u.print();
		System.out.print("result col\n"); result.print();*/
		
		
		//Podstawiamy kolumnê z wynikiem na odpowiedni¹ pozycje w macierzy U
		U.switchColumn(result, user);
		}
	}
	
	//Krok 5 algorytmu, to samo co getNewU tylko z innymi danymi
	public static void updateP(int d, double reg, Matrix U, Matrix P, Matrix ratings) {
		
		int u = U.getColumns();
		int p = P.getColumns();
		
		for(int product = 0; product < p; product++) {
			//I_p - lista indeksów u¿ytkownikow, którzy ocenili dany produkt
			int findUserIndex = 0;
			List<Integer> I_p = new ArrayList<Integer>();
			for(int i = 0; i < u; i++) {
				if(ratings.getCell(i, product) != 0)
					I_p.add(findUserIndex);
				findUserIndex++;
			}

		//U_I_p - kolumny z macierzy U o indeksach I_p
		List<Double> U_I_pTempList = new ArrayList<Double>();
		for(int i = 0; i < d; i++)
			for(int j = 0; j < u; j++)
				if(I_p.contains(j))
					U_I_pTempList.add(U.getCell(i, j));

		Matrix U_I_p = new Matrix(d, I_p.size());
		int row = -1;
		for(int i = 0; i < U_I_pTempList.size(); i++) {
			if(i % U_I_p.getColumns() == 0)
				row++;
			U_I_p.setCell(U_I_pTempList.get(i), row, i % U_I_p.getColumns());
				}

		//A_p
		Matrix U_I_p_T = U_I_p.transpose();
		Matrix E = Matrix.identityMatrix(d);
		Matrix A_p = U_I_p.times(U_I_p_T).plus(E.multiplyCells(reg));

		//Macierz V_p, czyli kolumny z macierzy U o indeksach z I_p pomno¿one przez ocenê
		List<Matrix> tempRatings = new ArrayList<Matrix>();
		Matrix col = new Matrix(d, 1);
		for(int i = 0; i < I_p.size(); i++)
			for(int j = 0; j < u; j ++) 
				if(j == I_p.get(i)) {
					col = U_I_p.getColumn(i).multiplyCells(ratings.getCell(j, product));
					tempRatings.add(col);
				}
			
		Matrix V_p = new Matrix(d, 1);
		for(Matrix mat : tempRatings)
			V_p = V_p.plus(mat);

		//Wynik kroku 6.
		Matrix result = A_p.gaussPG(V_p);
		
		
		/*System.out.print("Lista Ip: " + I_p + "\n");
		System.out.print("Macierz UIp\n"); U_I_p.print();
		System.out.print("Macierz Ap\n"); A_p.print();
		System.out.print("Macierz Vp\n"); V_p.print();
		System.out.print("result col\n"); result.print();*/
		

		//Podstawiamy kolumnê z wynikiem na odpowiednia pozycjê macierzy P
		P.switchColumn(result, product);
		}
	}
	
	//Zaokr¹glanie wyniku do testów, z https://stackoverflow.com/questions/2808535/round-a-double-to-2-decimal-places
	public static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    BigDecimal bd = BigDecimal.valueOf(value);
	    bd = bd.setScale(places, RoundingMode.HALF_UP);
	    return bd.doubleValue();
	}
	
	private static void toFile(String fileName, String content) {
		try(FileWriter fw = new FileWriter(fileName, true);
			BufferedWriter bw = new BufferedWriter(fw);
			PrintWriter out = new PrintWriter(bw))
		{
			out.println(content);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}