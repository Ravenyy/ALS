package matrix;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.regex.*;

public class Parser {
	
	public static List<Data> parseData(int recordLimit, String category) throws IOException {
		List<Data> list = new ArrayList<Data>();
		int productCounter = 0;
		int flag = 0;
		int startSubstring = 0;
		int endSubstring = 0;
		String username = new String();
		String productAsin = new String();
		int rating = 0;
		
		String buff = new String();
		BufferedReader br = new BufferedReader(new FileReader("amazon-meta.txt"));	
		String line;
		
		while (productCounter < recordLimit) {
			line = br.readLine();
			if(line.contains("group: " + category)) {
				flag = 1;
				productCounter++;
			}
			if(flag == 1) {
				if(line.indexOf("total: 0") >= 0) {
					list.add(new Data(productCounter, null, 0, category));
				}
				buff = "cutomer:";
				if(line.indexOf(buff) >= 0) {
					startSubstring = line.indexOf(buff) + buff.length();
					endSubstring = line.indexOf(buff) + buff.length() + 15;
					username = line.substring(startSubstring, endSubstring).replaceAll("\\s+", "");
					rating = Integer.parseInt(line.substring((endSubstring + 10), (endSubstring + 12)).replaceAll("\\s+", ""));
					//userDict.put(userCounter, username);
					list.add(new Data(productCounter, username, rating, category));
				}	
			}
			if(line.contains("Id:  ")) {
				flag = 0;
			}
		}
		return list;	
	}
}