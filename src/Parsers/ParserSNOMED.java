package Parsers;
/** 
		mysql -u root -p  < out4.sql
 * */
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashSet;

import javax.swing.JOptionPane;

import userint.UI;
import BaseDeDatos.managerDB;

public class ParserSNOMED {
	
	private static final int MAX_SQL_BLOCK_SIZE = 500;
	
		/** 
		 * Este método lee un archivo txt y genera un archivo de salida sql para insertar 
		 * en una base de datos
		 * */


	/**
	 * Este método lee un archivo txt y genera un archivo de salida sql para insertar en una base de datos. Es un caso simplificado de fileToSqlScriptSinonimos(path, fileOrigen, insertStament, activeColumnIndex, null, null);
 	 * @param path
	 * @param fileOrigen
	 * @param insertStament
	 * @param activeColumnIndex
	 */
	public static void fileToSqlScript(String path, String fileOrigen, String insertStament, String filter, int activeColumnIndex){
		fileToSqlScriptGeneric(path, fileOrigen, insertStament, activeColumnIndex, null, null,filter);
	}
	
	public static void fileToSqlScriptSinonimos(String path, String fileOrigen, String insertStament, int activeColumnIndex, HashSet<String> listaDrogasID, HashSet<String> listaDrogasIDTermino){
		fileToSqlScriptGeneric(path, fileOrigen, insertStament, activeColumnIndex, listaDrogasID, listaDrogasIDTermino,null);
	}		
	
	/**
	 * Este método lee un archivo txt de sinónimos de palabras en la BD y genera un archivo de salida sql para insertar
	 * verificando que en la base de datos se halle el concepto y que el término no se halle. 
	 * @param path
	 * @param fileOrigen
	 * @param insertStament
	 * @param activeColumnIndex
	 * @param listaDrogasID
	 * @param listaDrogasIDTermino
	 */
	private static void fileToSqlScriptGeneric(String path, String fileOrigen, String insertStament, int activeColumnIndex, HashSet<String> listaDrogasID, HashSet<String> listaDrogasIDTermino, String filtro){
		// Falta corregir el temita de las comas, y hacer un drop table y que cree la tabla de paso.																																					

		BufferedReader reader=null;
		try {
			
			
			// Creo el archivo auxFile
	        Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(UI.getPathFiles()+"auxFile.sql"), "utf-8"));
	        
		    // Escribo el insertStament
		    writer.write(insertStament);
	        
			// Creo el lector y comienzo a leer
		    reader = new BufferedReader(new FileReader(path+fileOrigen));
			reader.readLine();
			int count=0;
			
			String line = null;
			if (managerDB.conectionWasSucefull()){
			while ( (line = reader.readLine()) != null) {
				line= transformSpecialCharacters(line);     	
				
					// Agrego al bloque SQL																																						
					String[] list = line.split("	");
					if ( 	((activeColumnIndex>-1 && list[activeColumnIndex].equals("1")) || (activeColumnIndex<0) )&& 								// Verifica que este activo
							(listaDrogasID==null || listaDrogasID.contains(list[4])) && 																// Contiene la droga
							(filtro==null || filtro.isEmpty()|| list[7].contains(filtro) ) &&																				// Recorto 
							(listaDrogasIDTermino==null || (!listaDrogasIDTermino.contains(list[0]))) ){												// Si no está repetido Contiene el término
						if (filtro==null)
							addRowToInsertSentenceWithLimitedSize(writer, count, list, filtro,-1);
						else
							addRowToInsertSentenceWithLimitedSize(writer, count, list, filtro,7);
						count++;
					}
					
				if ( count==MAX_SQL_BLOCK_SIZE)
				{
					// Creo un nuevo bloque SQL
					writer.write(";\n"+insertStament);
					count=0;
				}
					
			}
			}
			managerDB.desconectar();
			writer.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public static boolean otra(String value){
		return managerDB.exist(UI.getSchemaName()+".detalle", "id", "value");	
		
	}

	public static void executeSqlScript(){
		try
		{
			Thread.sleep(1000);  

			Runtime.getRuntime().exec("cmd /c start "+UI.getPathFiles()+"prueba.bat");
			managerDB.waitUntisFinishConsole(UI.getPathFiles(),"done.prov" );

			// Borro todos los archivos luego de que termine el script
			File file = new File(UI.getPathFiles()+"done.prov"); 	file.delete();
			//file = new File(UI.getPathFiles()+"auxFile.sql");    	file.delete();
			file = new File(UI.getPathFiles()+"prueba.bat");		file.delete();
		}
		catch (Exception e1) {
			StringWriter errors = new StringWriter();
			e1.printStackTrace(new PrintWriter(errors));
			JOptionPane.showMessageDialog(null, errors.toString() );	}
	}
	
	private static String transformSpecialCharacters(String line){
		return line	.replaceAll("\"", "`")
					.replaceAll("Ã¡", "a")
					.replaceAll("Ã©", "e")
					.replaceAll("Ã­", "i")
					.replaceAll("Ã³", "o")
					.replaceAll("Ãº", "u");
	}
	
	private static void addRowToInsertSentenceWithLimitedSize(Writer buffer, int count, String[] listaValores, String filtro, int posicionFiltro){
		String res="";
		
		if (count != 0)
			res = ",(";
		else
			res = "(";

		// Por cada valor agrego el valor y una coma al final
		for (int i=0; i<listaValores.length;i++){
			if (posicionFiltro!=i)
				res+= "\"" + listaValores[i] + "\",";
			else
				res+= "\"" + listaValores[i].replace(filtro,"") + "\",";
		}
		
		// Como ya no hay mas valores para la fila salco la última coma y cierro paréntisis
		res=res.substring(0,res.length()-1);
		res+=")";
	
	
		try {
			buffer.write(res+"\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
				
		}	
}

