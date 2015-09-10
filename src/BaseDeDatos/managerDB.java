package BaseDeDatos;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import userint.UI;

public class managerDB {
	private static Connection connect = null;
	private static Statement statement = null;

	private static boolean ErroresVisibles = true;

	public static List<String> buscar( String tabla, String campo, String valorCampo, String campo_retornar, boolean exact){
		// Consulta por un campo especifico en una tabla; es un caso tribial del "buscarExacto" al que se le pasan una lista de campos
		
		List<String> resultado = new ArrayList<String>();

		try {

			if (managerDB.conectionWasSucefull())
			{
				String scripInsertar;
				if (exact)
					scripInsertar="select distinct * from " + tabla + " where " + campo + " like '"+ valorCampo + "'";
				else
					scripInsertar="select distinct * from " + tabla + " where " + campo + " like '%"+ valorCampo + "%'";
				ResultSet resultSet = statement.executeQuery(scripInsertar);

				while (resultSet.next())
				{  
						resultado.add(resultSet.getString(campo_retornar));
				}
				managerDB.desconectar();			
			}

		} catch (Exception e)
		{
			if (ErroresVisibles)
				JOptionPane.showMessageDialog(null, e.toString());		

			resultado = null;
		}

		return resultado;
	}
	public static List<List<String>> buscar( String tabla,String campo, String valorCampo, List<String> campos_retornar, boolean exact){
		List<List<String>> resultado = new ArrayList<List<String>>();
		
		try {

			if (managerDB.conectionWasSucefull())
			{
				String scripInsertar;
				if (exact)
					scripInsertar="select * from " + tabla + " where " + campo + " like '"+ valorCampo + "'";
				else
					scripInsertar="select * from " + tabla + " where " + campo + " like '%"+ valorCampo + "%'";
				

				ResultSet resultSet = statement.executeQuery(scripInsertar);

				while (resultSet.next())
				{  
					List<String> li = new ArrayList<String>();
					for (int i=0; i<campos_retornar.size();i++)	
						li.add(resultSet.getString(campos_retornar.get(i)));
					resultado.add(li);
				}
				managerDB.desconectar();			
			}

		} catch (Exception e)
		{
			if (ErroresVisibles)
				JOptionPane.showMessageDialog(null, e.toString());		

			resultado = null;
		}
		
		
		
		
		
		
		/*
		try 
		{
			if (managerDB.conectionWasSucefull())
			{
				String scripInsertar="select * from " + tabla;
				ResultSet resultSet = statement.executeQuery(scripInsertar);

				while (resultSet.next())
				{  
					List<String> row = new ArrayList<String>();
					for (String string : campos_retornar)
						row.add(resultSet.getString(string));
					resultado.add(row);
				}

				managerDB.desconectar();
			}			
		}
		
		catch (Exception e)
		{
			if (ErroresVisibles)
				e.printStackTrace();
		
			resultado=null;
		}
		
		*/
		
		

		return resultado;
	}
	
	public static List<List<String>> consultar( String tabla,String condicion, List<String> campos_retornar){
		// Realiza una consulta con los campos ingresados, si la consulta es invalida retorna una lista null.
		List<List<String>> resultado = new ArrayList<List<String>>();		
		try {

			if (managerDB.conectionWasSucefull())
			{
				String scripInsertar="select distinct * from " + tabla + " where " + condicion;
				ResultSet resultSet = statement.executeQuery(scripInsertar);
				int j=0;

				while (resultSet.next())
				{  
					resultado.add(new ArrayList<String>());
					for (int i=0; i<campos_retornar.size();i++)					
						resultado.get(j).add(resultSet.getString(campos_retornar.get(i)));
					j++;
				}
				managerDB.desconectar();			
			}

		} catch (Exception e)
		{
			if (ErroresVisibles)
				JOptionPane.showMessageDialog(null, e.toString());		
			resultado = null;
		}
		return resultado;
	}
		
	public static boolean exist( String tabla, String campo, String valorCampo){
		boolean resultado = false;
		try 
		{
			if (managerDB.conectionWasSucefull())
			{	
				String scripInsertar="select * from " + tabla + " where " + campo + " like '%" + valorCampo + "%'";
				ResultSet resultSet = statement.executeQuery(scripInsertar);

				if (resultSet.next())
					resultado=true;

				managerDB.desconectar();
			}
		} 
		catch (Exception e)
		{
			if (ErroresVisibles)
				JOptionPane.showMessageDialog(null, e.toString());		
		}
		return resultado;
	}

	public static boolean existWithSameConection( String tabla, String campo, String valorCampo){
		boolean resultado = false;
		try 
		{
			ResultSet resultSet = statement.executeQuery("select "+campo+" from " + tabla + " where " + campo + " like '%" + valorCampo + "%'");
			return resultSet.next();
			
		} 
		catch (Exception e)
		{
			if (ErroresVisibles)
				JOptionPane.showMessageDialog(null, e.toString());		
		}
		return resultado;
	}
	
	public static boolean executeScript_Void(String Script){
		// Es para ejecutar scripts que no devuelvan resultados, como por ejemplo insertar, actualizar, borrar datos o crear una datos a una tabla. 
		// Retorna un valor booleano que indica si fallo la ejecucion.
		boolean resultado=false;
		if (!Script.equals(""))		
		try
		{
			if (managerDB.conectionWasSucefull())
			{
				statement.execute(Script);
				managerDB.desconectar();
				resultado = true;
			}
		} 
		catch (Exception e)
		{
			if (ErroresVisibles)
				JOptionPane.showMessageDialog(null, e.toString());	
			
		}

		return resultado;
	}
	public static boolean executeScripts_Void(List<String> Script){
		// Es para ejecutar scripts que no devuelvan resultados, como por ejemplo insertar, actualizar, borrar datos o crear una datos a una tabla. 
		// Retorna un valor booleano que indica si fallo la ejecucion.
		boolean resultado=false;
		
		try
		{
			if (managerDB.conectionWasSucefull())
			{
				for (String string : Script) {
					statement.execute(string);	
				}
				
				managerDB.desconectar();
				resultado = true;
			}
		} 
		catch (Exception e)
		{
			if (ErroresVisibles)
				JOptionPane.showMessageDialog(null, e.toString());	
		}

		return resultado;
	}
	public static List<String> executeScript_Query( String Script, String campo_retornar){
		// Es la complementaria de la anterior, funciones que retornan como consultas.

		List<String> resultado = new ArrayList<String>();
		ResultSet resultSet = null;
		try{
			if (managerDB.conectionWasSucefull())
			{
				resultSet = statement.executeQuery(Script);
												
				while (resultSet.next())
					resultado.add(resultSet.getString(campo_retornar));
			}
			managerDB.desconectar();

		} catch (Exception e)
		{
			if (ErroresVisibles)
				JOptionPane.showMessageDialog(null, e.toString());	
			resultado=null;
		}
		return resultado;
	}
/**
 * Se pasa por parámetro el nombre de la tabla, (el esquema se no es necesario)
 * @param table
 */
	public static void borrarTabla(String table){
		if (existTable(table))
			executeScript_Void("DROP TABLE `"+UI.getSchemaName()+"`.`"+table+"`;");
	};
	
	public static List<List<String>> executeScript_Query( String Script,List<String> campo_retornar){
		// Es la complementaria de la anterior, funciones que retornan como consultas.

		List<List<String>> resultado = new ArrayList<List<String>>();
				ResultSet resultSet = null;
		try{
			if (managerDB.conectionWasSucefull())
			{
				resultSet = statement.executeQuery(Script);
				List <String> prov=null;						
				while (resultSet.next())
				{
					prov = new ArrayList<String>();
					for (String list : campo_retornar) {
						prov.add(resultSet.getString(list));
					}
					
				resultado.add(prov);}
			}
			managerDB.desconectar();

		} catch (Exception e)
		{
			if (ErroresVisibles)
				JOptionPane.showMessageDialog(null, e.toString());	
			resultado= null;
		}
		return resultado;
	}

	public static boolean conectionWasSucefull()
	{
		try{
			Class.forName("com.mysql.jdbc.Driver");

			// Conectar a la base de datos
			connect = DriverManager.getConnection("jdbc:mysql://localhost/", UI.getRootBD(), UI.getPasswordBD());
			statement = connect.createStatement();
			return true;
		}

		catch (Exception e) 
		{
			if (ErroresVisibles)
				JOptionPane.showMessageDialog(null, e.toString());				

			return false;
		}
	}
	public static void desconectar()
	{
		try 
		{
			statement.close();
			connect.close();
		} 
		catch (SQLException e) 
		{
			if (ErroresVisibles)
				e.printStackTrace();	
		}
	}

	public static boolean existTable(String tableName){
		// Es la complementaria de la anterior, funciones que retornan como consultas.

		ResultSet resultSet = null;
		boolean exit=false;
		try{
			if (managerDB.conectionWasSucefull())
			{
				resultSet = statement.executeQuery("SELECT * FROM `"+UI.getSchemaName()+"`.`"+tableName+"` limit 1;");
												
				if (resultSet.next())
					exit=true;
			}
			managerDB.desconectar();

		return exit;
		} catch (Exception e){ 
			return exit;
		}
	}
	
	/**
	 * Crea un ejecutable para que correr el script "filename"
	 * @param path Ubicacion del archivo
	 * @param filename Nombre del archivo
	 * @return false si se produjo un error, de otra forma retorna true
	 */
	public static boolean createExecutorFile(String path, String filename){
		
		
		try{
		Writer writer = null;
		writer = new BufferedWriter(new OutputStreamWriter(	new FileOutputStream(path+filename+".bat"), "utf-8"));
		writer.write("@echo off\n");
		writer.write("SET MYSQL_EXE=\""+UI.getEnvirometValue(UI.UBICACION_MYSQL)+"mysql.exe\"\n");
		writer.write("SET DB_USER="+UI.getRootBD()+"\n");
		writer.write("SET DB_PWD="+UI.getPasswordBD()+"\n");
		writer.write("CALL %MYSQL_EXE% --user=%DB_USER% --password=%DB_PWD% < "+UI.getPathFiles()+"auxFile.sql\n");
		writer.write("IF %ERRORLEVEL% NEQ 0 ECHO Error executing SQL file\n");
		writer.write("echo. > "+UI.getPathFiles()+"done.prov\n");
		writer.write("exit ");
		writer.close();
		return true;}
	catch(Exception e1){
		JOptionPane.showMessageDialog(null, e1.toString());		
		return false;}	
	}
	
	public static void waitUntisFinishConsole(String path, String nameFile)
	{

		boolean taskDone=false;
		try 
		{
			while(!taskDone)
				try{
					FileReader fR =new FileReader(path + nameFile);
					taskDone=true;
					fR.close();}
				catch ( IOException e1){
					Thread.sleep(1000);}
			Thread.sleep(100);
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}

