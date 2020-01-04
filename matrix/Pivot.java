package matrix;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Pivot {
	
	public static Matrix pivot(List<Data> list){
		
		List<String> listAllUsers = new ArrayList<String>();
		List<Integer> listAllItems = new ArrayList<Integer>();
		for(Data data : list) {
			String temp = data.getUsername();
			int tempInt = data.getItemId();
			if(!listAllUsers.contains(temp))
				listAllUsers.add(temp);
			if(!listAllItems.contains(tempInt))
				listAllItems.add(tempInt);
		}
		
		Matrix mat = new Matrix(listAllUsers.size(), listAllItems.size());
		
		for(Data data : list) {
			int item = data.getItemId();
			String user = data.getUsername();
			int rating = data.getRating();
			int userPos = 0;
			int itemPos = 0;
			for(String username : listAllUsers)
				if(user == username)
					userPos = listAllUsers.indexOf(user);
			for(int itemId : listAllItems)
				if(item == itemId)
					itemPos = listAllItems.indexOf(item);
			mat.setCell(rating, userPos, itemPos);
		}
		return mat;
	}
}
