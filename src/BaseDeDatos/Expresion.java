package BaseDeDatos;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import Auxiliares.ExtendedHashMap;
import Auxiliares.ListManager;

public class Expresion implements Comparable<Object>{

	
	public static ExtendedHashMap<String, Double> valores = new ExtendedHashMap<String, Double>();
	
	public List<String> getListaOriginal() {
		return listaOriginal;
	}


	public void setListaOriginal(List<String> listaOriginal) {
		this.listaOriginal = listaOriginal;
	}


	public List<String> getListaSugerencia() {
		return listaSugerencia;
	}


	public void setListaSugerencia(List<String> listaSugerencia) {
		this.listaSugerencia = listaSugerencia;
	}


	public String getTerminoOriginal() {
		return terminoOriginal;
	}


	public void setTerminoOriginal(String terminoOriginal) {
		this.terminoOriginal = terminoOriginal;
	}


	public String getTerminoSugerido() {
		return terminoSugerido;
	}


	public void setTerminoSugerido(String terminoSugerido) {
		this.terminoSugerido = terminoSugerido;
	}


	public float getCorrelacion() {
		return correlacion;
	}


	public void setCorrelacion(float correlacion) {
		this.correlacion = correlacion;
	}

	private String 	terminoOriginal, terminoSugerido, idSnomed;
	float 	correlacion;
	public List<String> listaOriginal, listaSugerencia;


	public Expresion(String termOriganal, String termSugerido, float correlac, String idSn){
		terminoOriginal=termOriganal;
		terminoSugerido=termSugerido;		
		correlacion=correlac;
		idSnomed=idSn;

		String[] aux =	termOriganal.split(" ");
		listaOriginal = new ArrayList<String>();

		for (String string : aux) {
			listaOriginal.add(string);
		}

		aux = termSugerido.split(" ");
		listaSugerencia = new ArrayList<String>();

		for (String string : aux) {
			listaSugerencia.add(string);
		}

	}


	public static float getCorrelacionT1 (List<String> lista1, List<String> lista2,List<String> exclusiones, float v1, float v2){

		float sumatoria=0, totalL1=0, totalL2=0, totalLong, restLtras, 	restContar=0;

				
		ListManager.removeRepetidos(lista1);
		ListManager.removeRepetidos(lista2);

		// Quito las palabras que se omiten
		for (String string : exclusiones) {
			lista1.remove(string);
			lista2.remove(string);}

		
		// Obtengo la longitud de la frase mas larga
		for (String string : lista1) {
			totalL1+=Math.pow(string.length()*valores.get(string), 2);}
		for (String string : lista2) {
			totalL2+=Math.pow(string.length()*valores.get(string), 2);}
		totalLong=Math.max(totalL1, totalL2);


		for (String string : lista1) {
			if (lista2.contains(string)){
				sumatoria+= Math.pow(string.length()*valores.get(string), 2);
				restContar++;}
		}

		restLtras = (sumatoria/totalLong);
		restContar = restContar/Math.max(lista1.size(), lista2.size());

		if (restLtras == 1)
			return 1;
		else	
			return (float) (restLtras*v1 + restContar*v2);
	}

	public static float getCorrelacionT2 (List<String> lista1, List<String> lista2,List<String> exclusiones, float v1, float v2){

		float totalL1=0, totalL2=0, totalLong, restContar=0;

		ListManager.removeRepetidos(lista1);
		ListManager.removeRepetidos(lista2);

		// Quito las palabras que se omiten
		for (String string : exclusiones) {
			lista1.remove(string);
			lista2.remove(string);}

		// Obtengo la longitud de la frase mas larga
		for (String string : lista1) {
			totalL1+=Math.pow(string.length()*valores.get(string), 2);}
		for (String string : lista2) {
			totalL2+=Math.pow(string.length()*valores.get(string), 2);}
		totalLong=Math.max(totalL1, totalL2);


		
		
		
		
		
		String p1="";
		for (String string :lista1) {
			p1+=string;
		}
		
		String p2="";
		for (String string :lista2) {
			p2+=string;
		}
		
		restContar=longestSubstr(p1,p2);
				
				
		return restContar;
	}

	
	public static float longestSubstr(String text1, String text2) {
			 
		int mayor=0;
			
			int size=1;
						
			while ((size<text1.length())&&(size<text2.length()) && (text1.substring(0, size).equals(text2.substring(0,size))))
			{	size++;
	
			}																																																			

			float sizTotal= Math.max((float)text1.length(),(float) text2.length());
 			if (size>4 || 		((size-1)/sizTotal)>0.6){
 			//	System.out.println(text1+" "+text2+" "+text1.substring(0,size) + "-" + text2.substring(0,size) + "- tamaño:" + (size-1) + " " + (size/sizTotal));
 				if (((size-1)/sizTotal)>0.6)		
 	 			
 					return (size-1)/sizTotal;
 	 			else
 	 				return 1;
 				
 				
 			}
 			else {
				 return 0;
			}
	
 			
 				
	}
	
	public static float getCorrelacionT1 (String frase1, String frase2, List<String> exclusiones, float v1, float v2){

		String [] aux = frase1.split(" ");

		List<String> lista1 = new ArrayList<String>(), lista2 = new ArrayList<String>();

		for (String string : aux) {
			lista1.add(string);
		}

		aux = frase2.split(" ");

		for (String string : aux) {
			lista2.add(string);
		}

		
		return Expresion.getCorrelacionT1(lista1, lista2, exclusiones, v1, v2);
	}
																																																					

	public static float getCorrelacionT2 (String frase1, String frase2, List<String> exclusiones, float v1, float v2){

		String [] aux = frase1.split(" ");

		List<String> lista1 = new ArrayList<String>(), lista2 = new ArrayList<String>();;

		for (String string : aux) {
			lista1.add(string);
		}

		aux = frase2.split(" ");

		for (String string : aux) {
			lista2.add(string);
		}

		return Expresion.longestSubstr(frase1, frase2);
	}



	public static double getCantLetrasIguales(List<String> l1,List<String> l2){
		double numeroa=0, numerob=0, resta=0, restb=0;

		for (String string : l1) {
			if (l2.contains(string))
				numeroa=numeroa+string.length();
			resta=resta+string.length();
		}

		for (String string : l2) {
			if (l1.contains(string)){
				numerob=numerob+string.length();
			}
			restb=restb+string.length();
		}

		return	Math.min(numeroa, numerob)/Math.max(resta, restb);
	}



	@Override
	public int compareTo(Object arg0) {
		if (this.getCorrelacion() == ((Expresion) arg0).getCorrelacion())
		{

			Double a = getCantLetrasIguales(listaOriginal, listaSugerencia);
			Double b = getCantLetrasIguales(((Expresion) arg0).getListaSugerencia() ,((Expresion) arg0).getListaOriginal());

			return -1*a.compareTo(b);

		}
		else
			if (this.getCorrelacion() < ((Expresion) arg0).getCorrelacion())
				return 1;
			else
				return -1;
	}


	public static Hashtable<String,String> removeNumbersInListofPhrases(Hashtable<String,String> l){
		
		
		Hashtable<String,String> nuevos = new Hashtable<String, String>();
		List<String> paraSacar = new ArrayList<String>(), paraPoner = new ArrayList<String>();

		for (String ele : l.keySet()) {
			
			String [] aux = ele.split(" ");

			List<String> lista1 = new ArrayList<String>();

			for (String string : aux) {
				lista1.add(string);
			}

			List<String> borrar = new ArrayList<String>();

			for (String string3 : lista1) {
				try{

					String aux1 = string3.replaceAll("\\.","");
					aux1 = Integer.decode(aux1).toString();
					String aux2 =  string3.replaceAll("\\.","");																																													
					System.out.print("Comparo " + aux1 + " con " + string3.replace("\\.", ""));
					if (aux1.equals(aux2) || string3.length()<3)
					{
						System.out.println("true");
						borrar.add(string3);
					}
					else
						System.out.println("false");

					System.out.println(ele.toString());
					

				}
				catch(Exception e){
					//e.printStackTrace();
				}
			}

		
			lista1.removeAll(borrar);
			
			paraSacar.add(ele);
			String rearmado="";


			for (String string : lista1) {
				rearmado+=string+" ";
			}
			paraPoner.add(rearmado);
			
			nuevos.put(rearmado, l.get(ele));
		}
		System.out.println(paraPoner.toString());
		
		l.clear();
		l.putAll(nuevos);
		return l;

	}

	public static List<List<String>> removeNumbersInListofPhrases2(List<List<String>> l){

		List<List<String>>  paraPoner = new ArrayList<List<String>>();

		for (int i=0; i<l.size(); i++)
		{
			String [] aux = l.get(i).get(0).split(" ");

			List<String> lista1 = new ArrayList<String>();

			for (String string : aux) {
				lista1.add(string);
			}

			List<String> borrar = new ArrayList<String>();

			for (String string3 : lista1) {
				try{

					String aux1 = string3.replaceAll("\\.","");
					aux1 = Integer.decode(aux1).toString();
					String aux2 =  string3.replaceAll("\\.","");																																													
					System.out.print("Comparo " + aux1 + " con " + string3.replace("\\.", ""));
					if (aux1.equals(aux2) || string3.length()<3)
					{
						System.out.println("true");
						borrar.add(string3);
					}
					else
						System.out.println("false");					

				}
				catch(Exception e){
					//e.printStackTrace();
				}
			}

		
			lista1.removeAll(borrar);
			
			String rearmado="";


			for (String string : lista1) {
				rearmado+=string+" ";
			}

			List<String> aa = new ArrayList<String>();
			aa.add(rearmado);
			aa.add(l.get(i).get(1));
			aa.add(l.get(i).get(0));
			paraPoner.add(aa);

		}
		l.clear();
		System.out.println(paraPoner.toString());
		l.addAll(paraPoner);
		return l;

	}


	public String getIdSnomed() {
		return idSnomed;
	}


	public void setIdSnomed(String idSnomed) {
		this.idSnomed = idSnomed;
	}

}


