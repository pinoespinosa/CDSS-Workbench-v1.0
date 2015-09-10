package Parsers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

public class ParserCVS {

	@SuppressWarnings("resource")
	public static List<List <String>> parseCVS (String path, Vector<Integer> columnas){
		/** Lee un archivo CVS y lo retorna como una lista de lista de valores, en columnas se indican los indices de las
		 * columnas que se quiere considerar*/
		
		List<List <String>> resultado = new ArrayList<List<String>>();
			
        BufferedReader reader=null;
		try {
			reader = new BufferedReader(new FileReader(path));
	        String line = null;
	        String[] list;
	        reader.readLine();

	        List<String> fila = new ArrayList<String>();
	        
	        while ( (line = reader.readLine()) != null) 
	        {
	        	fila=new ArrayList<String>();
	           	list = line.split(";");
	        	for (Integer indice : columnas) {
	        		String palabra = list[indice];
	        		while (palabra.endsWith(" "))
	        			palabra=palabra.substring(0,palabra.length()-1);		
					fila.add(palabra);
				}        
	        resultado.add(fila);
	        }
	        }
			catch (Exception e) {
				e.printStackTrace();			}
		
		return resultado;
	}
	
	public static List < List <String>> quitarRepetidos (List<List<String>> l)
	{
		Set < List <String>> conjunto = new HashSet<List<String>>();
		conjunto.addAll(l);
		l.clear();
		l.addAll(conjunto);

		return l;
		
	}
}
