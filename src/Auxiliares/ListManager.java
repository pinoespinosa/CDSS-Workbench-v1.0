package Auxiliares;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ListManager {

	public static List<String> replace (List<String> lista, String expToReplace, String expToWrite){
	
		List<String> b = new ArrayList<String>();
		
		for (int i=0; i< lista.size();i++)
			if (lista.get(i).contains(expToReplace))
				b.add(lista.get(i).replace(expToReplace,expToWrite));
			else
				b.add(lista.get(i));

			lista.clear();
			lista.addAll(b);
			b.clear();
		
		return b;
		
	}
	
	public static List<String> removeRepetidos(List<String> a){
		
		Set<String> conjunto =new HashSet<String>();
		conjunto.addAll(a);
		a.clear();
		a.addAll(conjunto);
		
		return a;
		
	}
	
	
	
}
