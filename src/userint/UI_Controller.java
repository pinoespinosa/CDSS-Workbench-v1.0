package userint;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JTable;

import Auxiliares.SimpleFile;
import Auxiliares.TableModel_withClases;
import BaseDeDatos.Expresion;
import BaseDeDatos.managerDB;
import Elements.Test;
import Parsers.ParserSNOMED;

public class UI_Controller {

	// ************************************************** Instanciacion de tablas con datos en crudo *****************************************************************************

	// ---------------------------------  Tablas de SNOMED  ---------------------------------
	public void instanciarTablaConceptos(){
		String 	insertStament= "INSERT INTO `"+UI.getSchemaName()+"`.`concepts` VALUES",
				createStament= "CREATE TABLE `"+UI.getSchemaName()+"`.`concepts` (" +
						"`id` varchar(20) NOT NULL," +
						"`effectiveTime` int(11) NOT NULL," +
						"`active` tinyint(4) DEFAULT NULL," +
						"`moduleId` varchar(20) DEFAULT NULL," +
						"`definitionStatusId` varchar(20) DEFAULT NULL, " +
						"PRIMARY KEY (`id`,`effectiveTime`));";

		makeSQL("concepts", insertStament, createStament, UI.getPathSnomed()+"RF2Release"+File.separator+"Snapshot"+File.separator+"Terminology"+File.separator,"sct2_Concept_Snapshot_INT_",null,2);
	}
	public void instanciarTablaDescripciones(){

		String  createStament= "CREATE  TABLE `"+UI.getSchemaName()+"`.`detalle` (" +
				"  `id` VARCHAR(20) NOT NULL ," +
				"`effectiveTime` VARCHAR(45) NOT NULL ," +
				"`active` VARCHAR(45) NULL ," +
				"`moduleId` VARCHAR(45) NULL ," +
				"`conceptId` VARCHAR(45) NULL ," +
				"`languageCode` VARCHAR(45) NULL ," +
				"`typeId` VARCHAR(45) NULL ," +
				"`term` VARCHAR(400) NULL ," +
				"`caseSignificanceId` VARCHAR(45) NULL ," +
				"PRIMARY KEY (`id`, `effectiveTime`) );",
				insertStament= "INSERT INTO `"+UI.getSchemaName()+"`.`detalle` VALUES";


		// Agrego las drogas en español e ingles
		makeSQL("detalle", insertStament, createStament, UI.getPathSnomed()+"RF2Release"+File.separator+"Snapshot"+File.separator+"Terminology"+File.separator,"sct2_Description_Snapshot-en_INT_"," (substance)",2);
		makeSQL("", insertStament, "", UI.getPathSnomed()+"RF2Release"+File.separator+"Snapshot"+File.separator+"Terminology"+File.separator,"sct2_Description_SpanishExtensionSnapshot-es_INT_"," (sustancia)",2);

		// Levanto los id para traer los sinonimos conceptuales, siempre que la palabra no esté empleada de antemano
		HashSet<String> listaDrogasID = new HashSet <String>();
		listaDrogasID.addAll( managerDB.executeScript_Query("SELECT DISTINCT conceptId FROM "+UI.getSchemaName()+".detalle;", "conceptID"));
		HashSet<String> listaDrogasIDTermino = new HashSet <String>();
		listaDrogasIDTermino.addAll(managerDB.executeScript_Query("SELECT DISTINCT id FROM "+UI.getSchemaName()+".detalle;", "id"));

		// Agrego los sinonimos
		makeSQLSinonimos(insertStament, UI.getPathSnomed()+"RF2Release"+File.separator+"Snapshot"+File.separator+"Terminology"+File.separator, "sct2_Description_Snapshot-en_INT_", 2, listaDrogasID, listaDrogasIDTermino);
		makeSQLSinonimos(insertStament, UI.getPathSnomed()+"RF2Release"+File.separator+"Snapshot"+File.separator+"Terminology"+File.separator, "sct2_Description_SpanishExtensionSnapshot-es_INT_", 2, listaDrogasID, listaDrogasIDTermino);

	}
	public void instanciarTablaCalificadores(){

		String  createStament= "CREATE  TABLE `"+UI.getSchemaName()+"`.`detalle2` (" +																	
				"  `id` VARCHAR(20) NOT NULL ," +
				"`effectiveTime` VARCHAR(45) NOT NULL ," +
				"`active` VARCHAR(45) NULL ," +
				"`moduleId` VARCHAR(45) NULL ," +
				"`conceptId` VARCHAR(45) NULL ," +
				"`languageCode` VARCHAR(45) NULL ," +
				"`typeId` VARCHAR(45) NULL ," +
				"`term` VARCHAR(400) NULL ," +
				"`caseSignificanceId` VARCHAR(45) NULL ," +
				"PRIMARY KEY (`id`, `effectiveTime`) );",
				insertStament= "INSERT INTO `"+UI.getSchemaName()+"`.`detalle2` VALUES";


		// Agrego los calificadores en español e ingles
		makeSQL("detalle2", insertStament, createStament, UI.getPathSnomed()+"RF2Release"+File.separator+"Snapshot"+File.separator+"Terminology"+File.separator+"","sct2_Description_Snapshot-en_INT_"," dose form (qualifier value)",2);
		makeSQL("", insertStament, "", UI.getPathSnomed()+"RF2Release"+File.separator+"Snapshot"+File.separator+"Terminology"+File.separator+"","sct2_Description_SpanishExtensionSnapshot-es_INT_"," (calificador)",2);

		// Levanto los id de los calificadores en ing/esp que ya cree
		HashSet<String> listaDrogasID = new HashSet <String>();
		listaDrogasID.addAll( managerDB.executeScript_Query("SELECT DISTINCT conceptId FROM "+UI.getSchemaName()+".detalle2;", "conceptID"));
		HashSet<String> listaDrogasIDTermino = new HashSet <String>();
		listaDrogasIDTermino.addAll(managerDB.executeScript_Query("SELECT DISTINCT id FROM "+UI.getSchemaName()+".detalle2;", "id"));

		// Agrego los calificadores sinónimos
		makeSQLSinonimos(insertStament, UI.getPathSnomed()+"RF2Release"+File.separator+"Snapshot"+File.separator+"Terminology"+File.separator+"", "sct2_Description_Snapshot-en_INT_", 2, listaDrogasID,listaDrogasIDTermino);
		makeSQLSinonimos(insertStament, UI.getPathSnomed()+"RF2Release"+File.separator+"Snapshot"+File.separator+"Terminology"+File.separator+"", "sct2_Description_SpanishExtensionSnapshot-es_INT_", 2, listaDrogasID,listaDrogasIDTermino);
		
	}

	// ---------------------------------  Otras tablas  ---------------------------------
	public void instanciarEsquema(){
		managerDB.executeScript_Void("CREATE SCHEMA " + UI.getSchemaName());
	}
	public void instanciarBDAleman(){

		String createStament= "CREATE TABLE `"+  UI.getSchemaName() +"`.`medicamentos_manfar` (`id_manfar` varchar(25) NOT NULL, `nombre` varchar(200) NOT NULL,`unidades` int(11) NOT NULL, `formaFarmaceutica` varchar(400) NOT NULL,`original` varchar(1) NOT NULL, PRIMARY KEY (`id_manfar`,`nombre`,`original`), KEY `gg` (`nombre`))",
				insertStament= "INSERT INTO `"+  UI.getSchemaName() +"`.`medicamentos_manfar` VALUES ";							

		// Lo que debo hacer es eliminar la lista de medicamentos repetidos del Hosp. Aleman y los inserto en la BD		
		SimpleFile.removeRepetidosFile(UI.getPathFiles(), "aleman_medicamentos_originales.txt");
		makeSQL("medicamentos_manfar", insertStament, createStament, UI.getPathFiles(),"aleman_medicamentos_originales",null,-1);

		// Hago lo mismo para los que agregue manualmente
		SimpleFile.removeRepetidosFile(UI.getPathFiles(), "aleman_medicamentos_manuales.txt");
		makeSQL("", insertStament, "", UI.getPathFiles(),"aleman_medicamentos_manuales",null,-1);

		// Hago lo mismo para los que agregue automatico
		SimpleFile.removeRepetidosFile(UI.getPathFiles(), "aleman_medicamentos_automaticos.txt");
		makeSQL("", insertStament, "", UI.getPathFiles(),"aleman_medicamentos_automaticos",null,-1);

		SimpleFile.removeRepetidosFile(UI.getPathFiles(), "aleman_medicamentos_shortTail.txt");
		makeSQL("", insertStament, "", UI.getPathFiles(),"aleman_medicamentos_shortTail",null,-1);		
	};	
	public void instanciarTablaAnmat(){

		String 	insertStament= "INSERT INTO `"+UI.getSchemaName()+"`.`drogas_anmat` VALUES",

				createStament= "CREATE TABLE `"+UI.getSchemaName()+"`.`drogas_anmat` (" +
						"`nro_certificado_anmat` int(11) NOT NULL, " +
						"`indexCertif` int(11) NOT NULL, " +
						"`idManfar` varchar(100) DEFAULT NULL, " +
						"`medicamento_nombre` varchar(100) NOT NULL, " +
						"`formaFarmaceutica` varchar(100) NOT NULL, " +
						"`presentacion_cantidad` DOUBLE DEFAULT NULL, " +
						"`presentacion_magnitud` varchar(150) NOT NULL, " +
						"`presentacion_unidades` int(11) NOT NULL, " +
						"`droga_nombre` varchar(150) NOT NULL," +
						"`droga_cantidad` DOUBLE DEFAULT NULL," +
						"`droga_magnitud` varchar(100) DEFAULT NULL," +
						"`presentacion_texto` varchar(250) DEFAULT NULL," +
						"`drogas_texto` varchar(200) DEFAULT NULL," +
						"PRIMARY KEY (`nro_certificado_anmat`,`medicamento_nombre`,`formaFarmaceutica`,`presentacion_texto`,`drogas_texto`,`presentacion_unidades`,`indexCertif`), " +
						"KEY `a` (`droga_nombre`),   KEY `b` (`medicamento_nombre`) " +
						");";

		makeSQL("drogas_anmat", insertStament, createStament, UI.getPathFiles(),"anmat",null,-1);	
	}
	public void instanciarTablaExcepciones(){
		managerDB.executeScript_Void("CREATE TABLE `"+UI.getSchemaName()+"`.`interacciones_excepcion` (" +
				"  `idDrogaAleman1` varchar(45) NOT NULL," +
				"  `idDrogaAleman2` varchar(45) NOT NULL," +
				"  `detalle` varchar(400) DEFAULT NULL," +
				"  PRIMARY KEY (`idDrogaAleman1`,`idDrogaAleman2`))");
	}
	public void instanciarTablaInteracciones(){
		// Cargo los datos en crudo del archivo de interacciones en una tabla provisoria ********************************************************
		managerDB.executeScript_Void(	"CREATE TABLE `"+UI.getSchemaName()+"`.`interacciones2` (" +
				"  `idDroga1` varchar(50) DEFAULT NULL," +
				"  `idDroga2` varchar(50) DEFAULT NULL," +
				"  `gravedadNumero` varchar(1) NOT NULL," +
				"  `gravedadPalabra` varchar(20) NOT NULL," +
				"  `explicacionIntereccion` varchar(2000) DEFAULT NULL," +
				"  `tratamiento` varchar(2000) DEFAULT NULL," +
				"  `nameDroga1` varchar(200) NOT NULL," +
				"  `nameDroga2` varchar(200) NOT NULL," +
				"  PRIMARY KEY (`gravedadNumero`,`gravedadPalabra`,`nameDroga2`,`nameDroga1`)," +
				"  KEY `s` (`idDroga1`))");
		
		String insertStament= "INSERT INTO `"+UI.getSchemaName()+"`.`interacciones2` VALUES";
		makeSQL("", insertStament, "", UI.getPathFiles(),"interacciones","",-1);

		// Creo la tabla final y le cargo los datos de la provisoria organizados de mayor a menor *************************************************

		managerDB.executeScript_Void(	"CREATE TABLE `"+UI.getSchemaName()+"`.`interacciones` (" +
				"  `idDroga1` varchar(50) DEFAULT NULL," +
				"  `idDroga2` varchar(50) DEFAULT NULL," +
				"  `gravedadNumero` varchar(1) NOT NULL," +
				"  `gravedadPalabra` varchar(20) NOT NULL," +
				"  `explicacionIntereccion` varchar(2000) DEFAULT NULL," +
				"  `tratamiento` varchar(2000) DEFAULT NULL," +
				"  `nameDroga1` varchar(200) NOT NULL," +
				"  `nameDroga2` varchar(200) NOT NULL," +
				"  PRIMARY KEY (`gravedadNumero`,`gravedadPalabra`,`nameDroga2`,`nameDroga1`)," +
				"  KEY `s` (`idDroga1`))");
																																																						
		managerDB.executeScript_Void("	" +
			"insert into "+UI.getSchemaName()+".interacciones(" +
				"select * " +
				"from (" +
				"SELECT * " +
				"FROM "+UI.getSchemaName()+".interacciones2 " +
				"where idDroga1<idDroga2 " +
				
				"union " +
				
				"SELECT idDroga2, idDroga1, gravedadNumero, gravedadPalabra, " +
				"explicacionIntereccion, tratamiento, nameDroga2, nameDroga1 " +
				"FROM "+UI.getSchemaName()+".interacciones2 " +
				"where idDroga1>idDroga2) as pino " +
				"group by gravedadNumero, gravedadPalabra, nameDroga1, nameDroga2" +
				")");	
	
		managerDB.borrarTabla("interacciones2");
		
	}
	public void instanciarTablasDosificaciones(){
	String createStament=	"CREATE TABLE `"+UI.getSchemaName()+"`.`dosis` (" +
							"`idDroga` int(11) NOT NULL," +
							"`edad_minima` int(11) NOT NULL," +
							"`edad_maxima` int(11) NOT NULL," +
							"`peso_minimo` int(11) NOT NULL," +
							"`peso_maximo` int(11) NOT NULL," +
							"`sexo` varchar(1) NOT NULL," +
							"`formula` varchar(200) NOT NULL," +
							"`cantidad_minima` double NOT NULL," +
							"`cantidad_maxima` double NOT NULL DEFAULT '1'," +
							"`consecuencia` varchar(45) NOT NULL," +
							"`gravedad` int(1) NOT NULL,"+
							"`explicacion` varchar(500) NOT NULL,"+
							"PRIMARY KEY (`idDroga`,`edad_minima`,`edad_maxima`,`peso_minimo`,`peso_maximo`,`sexo`,`formula`,`cantidad_minima`,`cantidad_maxima`))";
	
	String insertStament= "INSERT INTO `"+UI.getSchemaName()+"`.`dosis` VALUES";
	makeSQL("", insertStament, createStament, UI.getPathFiles(),"dosis","",-1);

	
	String createStament2 = "CREATE TABLE `"+UI.getSchemaName()+"`.`dosis_aplicada` (  " +
							"`timestamp` varchar(50) NOT NULL," +
							"`id_droga` varchar(200) DEFAULT NULL," +
							"`cantidad` float DEFAULT NULL," +
							"`peso` int(11) DEFAULT NULL," +
							"`edad` int(11) DEFAULT NULL," +
							"`sexo` varchar(1) DEFAULT NULL," +
							"PRIMARY KEY (`timestamp`))";
	
	String createStament3= 	"CREATE TABLE `"+UI.getSchemaName()+"`.`cache_dosis_aplicadas_pocesadas` (" +
							"`timestamp` varchar(50) NOT NULL," +
							"`id_droga` varchar(45) DEFAULT NULL," +
							"`dosis` float DEFAULT NULL," +
							"`sexo` varchar(1) DEFAULT NULL," +
							"`rango_edad` int(11) DEFAULT NULL," +
							"`rango_peso` int(11) DEFAULT NULL," +
							"PRIMARY KEY (`timestamp`)" +
							");";
	
//	managerDB.executeScript_Void(createStament);
	managerDB.executeScript_Void(createStament2);
	managerDB.executeScript_Void(createStament3);
	
	}
	public void instanciarTablasInteraccionesCondiciones(){
		String script="CREATE TABLE `"+UI.getSchemaName()+"`.`condition_interactions` (" +
				"`id_droga` int(11) NOT NULL," +
				"`id_condition` varchar(45) NOT NULL," +
				"`nivel_certeza` varchar(1) DEFAULT NULL," +
				"`consecuencia` varchar(1000) DEFAULT NULL," +
				"`recomendacion` varchar(1000) DEFAULT NULL," +
				"`bibliografia` varchar(100) DEFAULT NULL," +
				"`comentario_adicional` varchar(45) DEFAULT NULL," +
				"PRIMARY KEY (`id_droga`,`id_condition`)" +
				")";
		
		String script2="CREATE TABLE `"+UI.getSchemaName()+"`.`condition_snomed` (" +
				"`idcontitions_snomed` varchar(45) NOT NULL," +
				"`idconditions_word` varchar(45) NOT NULL," +
				"PRIMARY KEY (`idcontitions_snomed`,`idconditions_word`)" +
				")";
		managerDB.executeScript_Void(script);
		managerDB.executeScript_Void(script2);
		
	}
	public void instanciarTablasReportes(){
		String script="CREATE TABLE `"+UI.getSchemaName()+"`.`reportes` (" +
				"`fecha` varchar(50) NOT NULL," +
				"`reporte` varchar(250) NOT NULL," +
				"PRIMARY KEY (`fecha`,`reporte`)," +
				"KEY `aa` (`reporte`)" +
				")";
		String script2="CREATE TABLE `"+UI.getSchemaName()+"`.`test` (" +
				"`id` int(11) NOT NULL," +
				"`dosis` varchar(45) DEFAULT NULL," +
				"`testcol` int(11) NOT NULL," +
				"PRIMARY KEY (`testcol`,`id`)" +
				")";
		
		String script3 = "CREATE TABLE `"+UI.getSchemaName()+"`.`medicamentos_manfar_sugerencias` (" +
				"  `id_manfar` varchar(25) NOT NULL," +
				"  `id_certif_anmat` varchar(45) NOT NULL," +
				"  `id_index_anmat` varchar(45) NOT NULL," +
				"  `id_medico` varchar(45) NOT NULL," +
				"  `timestamp` varchar(45) DEFAULT NULL," +
				"  PRIMARY KEY (`id_manfar`,`id_certif_anmat`,`id_index_anmat`,`id_medico`)) ";
		
		managerDB.executeScript_Void(script);
		managerDB.executeScript_Void(script2);
		managerDB.executeScript_Void(script3);
	}
	public void instanciarTablasCasoTesting(){
		String 	insertStament= "INSERT INTO `"+UI.getSchemaName()+"`.`testing_data` VALUES", 
				createStament="CREATE TABLE `"+UI.getSchemaName()+"`.`testing_data` (" +
				"  `idRegistro` int(11) NOT NULL," +
				"  `id_grupo` varchar(45) DEFAULT NULL," +
				"  `idIngreso` varchar(10) DEFAULT NULL," +
				"  `manfarId` varchar(45) DEFAULT NULL," +
				"  `posologia` varchar(1000) DEFAULT NULL," +
				"  `dosis` varchar(45) DEFAULT NULL," +
				"  `mg_dosis` varchar(45) DEFAULT NULL," +
				"  `unidades` varchar(45) DEFAULT NULL," +
				"  PRIMARY KEY (`idRegistro`)" +
				");";
		makeSQL("testing_data", insertStament, createStament, UI.getPathFiles(),"testingcase",null,-1);	
	}
	public void runCasoTestingDosis(){

		boolean exito=false;

		List<String> idMedicamentos = managerDB.executeScript_Query("SELECT manfarId, cant" +
				" FROM `"+ UI.getSchemaName() +"`.medicamentos_mas_consultado left join " +
				" pruebainstall2.reportes2 ON reporte like concat('%=',manfarId) " +
				" where reporte IS NULL " +
				" order by cant desc; ", "manfarId");
		
		for (String id : idMedicamentos) {
			exito=false;
			while (!exito){
				try {
					System.out.println(id);
					String input = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:web=\"http://Web\">" +
							"   <soapenv:Header/>" +
							"   <soapenv:Body>" +
							"      <web:getDoseEvents>" +
							"         <web:idMedico>pino</web:idMedico>" +
							"          <web:peso>67</web:peso>" +
							"         <web:edad>72</web:edad>" +
							"         <web:sexo>M</web:sexo>" +
							"         <web:medicaciones>"+id+"_10</web:medicaciones>" +
							"         <web:conditions>emb</web:conditions>" +
							"      </web:getDoseEvents>" +
							"   </soapenv:Body>" +
							"</soapenv:Envelope>";

					
					
					boolean a =Test.runTest(input, "vacio");

					exito=true;
				} catch (Exception e) {
					System.out.println("fail "+ id );
					exito=false;
				//	e.printStackTrace();
				}
			}
		}


	}
	public void runCasoTestingMed(){
		boolean exito=false;

		List<String> idMedicamentos = managerDB.executeScript_Query("SELECT manfarId, cant" +
				" FROM `"+UI.getSchemaName()+"`.medicamentos_mas_consultado left join " +
				" `"+UI.getSchemaName()+"`.reportes2 ON reporte like concat('%=',manfarId) " +
				" where reporte IS NULL " +
				" order by cant desc; ", "manfarId");
		
		for (String id : idMedicamentos) {
			exito=false;
			while (!exito){
				try {
					System.out.println(id);
					String input = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:web=\"http://Web\">" +
							"   <soapenv:Header/>" +
							"   <soapenv:Body>" +
							"      <web:getInteractionsWithProblems>" +
							"         <web:idMedico>pino</web:idMedico>" +
							"          <web:peso>67</web:peso>" +
							"         <web:edad>72</web:edad>" +
							"         <web:sexo>M</web:sexo>" +
							"         <web:medicaciones>"+id+"_10</web:medicaciones>" +
							"         <web:conditions>emb</web:conditions>" +
							"      </web:getInteractionsWithProblems>" +
							"   </soapenv:Body>" +
							"</soapenv:Envelope>";

					
					
					boolean a =Test.runTest(input, "vacio");

					exito=true;
				} catch (Exception e) {
					System.out.println("fail "+ id );
					exito=false;
				//	e.printStackTrace();
				}
			}
		}

	}
	
	public void runCasoTestingMedFragmento(){
		boolean exito=false;

		List<String> idMedicamentos = managerDB.executeScript_Query("SELECT manfarId" +
				" FROM `"+UI.getSchemaName()+"`.testing_data limit 50000; ", "manfarId");
		
		for (String id : idMedicamentos) {
			exito=false;
			while (!exito){
				try {
					System.out.println(id);
					String input = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:web=\"http://Web\">" +
							"   <soapenv:Header/>" +
							"   <soapenv:Body>" +
							"      <web:getInteractionsWithProblems>" +
							"         <web:idMedico>pino</web:idMedico>" +
							"          <web:peso>67</web:peso>" +
							"         <web:edad>72</web:edad>" +
							"         <web:sexo>M</web:sexo>" +
							"         <web:medicaciones>"+id+"_10</web:medicaciones>" +
							"         <web:conditions>emb</web:conditions>" +
							"      </web:getInteractionsWithProblems>" +
							"   </soapenv:Body>" +
							"</soapenv:Envelope>";

					
					
					Test.runTest(input, "vacio");

					exito=true;
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("fail "+ id );
					exito=false;
				//	e.printStackTrace();
				}
			}
		}

	}

	// ********************************************** Procesamiento y transformación de las tablas de datos ************************************************************************
	public void createProcesatedTableFromDrogasSnomed(){
		String createStament= 	"CREATE  TABLE `"+ UI.getSchemaName() +"`.`drogas_snomed` (" +
											"`iddrogas_Snomed` varchar(50) NOT NULL, " +
											"`iddrogas_Anmat` varchar(200) NOT NULL, " +
											"`tipo` varchar(20) NOT NULL, " +
											"PRIMARY KEY (`iddrogas_Snomed`,`iddrogas_Anmat`)," +
											"KEY `Secundario` (`iddrogas_Anmat`))";
		managerDB.borrarTabla("drogas_snomed");
		managerDB.executeScript_Void(createStament);
		managerDB.executeScript_Void(	"insert into `"+ UI.getSchemaName() +"`.`drogas_snomed` (" +
				"select distinct con.id as iddrogas_Snomed, lower(de.term) as iddrogas_Anmat, 'originales' " +
				"from "+ UI.getSchemaName()+".detalle as de, "+UI.getSchemaName()+".concepts as con " +
				"where  de.conceptId =con.id and con.active=1 )");
		
		managerDB.borrarTabla("detalle");

		// Inserto las manuales
		makeSQL("", "INSERT INTO `"+UI.getSchemaName()+"`.`drogas_snomed` VALUES", "",  UI.getPathFiles(),"snomed_drogas_manuales",null,-1);

		
		

	}
	public void createProcesatedTableFromCalificadoresSnomed(){

		String createStament= 	"	CREATE TABLE `"+ UI.getSchemaName() +"`.`calificadores_snomed` (" +
				"	`idcalificador_Snomed` varchar(50) NOT NULL," +
				"  	`detalle_calificador` varchar(200) NOT NULL," +
				"  	`tipo` varchar(20) NOT NULL, " +
				"	`ancestro_calificador` varchar(45) DEFAULT NULL," +
				"  	PRIMARY KEY (`idcalificador_Snomed`,`detalle_calificador`)," +
				"  	KEY `Secundario` (`detalle_calificador`))";

		managerDB.borrarTabla("calificadores_snomed");
		managerDB.executeScript_Void(createStament);
		managerDB.executeScript_Void(	"insert into `"+UI.getSchemaName() +"`.`calificadores_snomed` (" +
											"select distinct con.id as iddrogas_Snomed, lower(de.term) as iddrogas_Anmat, 'originales', '' " +
											"from "+UI.getSchemaName()+".detalle2 as de, "+UI.getSchemaName()+".concepts as con " +
											"where  de.conceptId =con.id and con.active=1 )");
		
		makeSQL("", "INSERT INTO `"+UI.getSchemaName()+"`.`calificadores_snomed` VALUES", "", UI.getPathFiles(),"snomed_califica_manuales",null,-1);

		managerDB.borrarTabla("detalle2");
		managerDB.borrarTabla("concepts");
	
	}
	public void reProcesatedTableDrogasToCreateSimplefiedTableDrugs(){
		managerDB.borrarTabla("droga_formaSimplificada");
		
		// Creo una tabla auxiliar para acelerar la consulta y le inserto los valores de las drogas que empiezan de clor..

		managerDB.executeScript_Void("CREATE TABLE "+UI.getSchemaName()+".drogas_snomed_filtradas (" +
				"`iddrogas_Snomed` varchar(50) NOT NULL," +
				"`iddrogas_Anmat` varchar(200) NOT NULL," +
				"`tipo` varchar(20) NOT NULL," +
				"PRIMARY KEY (`iddrogas_Snomed`,`iddrogas_Anmat`)," +
				"KEY `Secundario` (`iddrogas_Anmat`));" );

		managerDB.executeScript_Void(" insert into "+UI.getSchemaName()+".drogas_snomed_filtradas  " +
				"(" +
				"select * " +
				"from "+UI.getSchemaName()+".drogas_snomed as sno " +
				"where 	" +
				"		sno.iddrogas_Anmat like 'clorhidrato de %' or " +
				"		sno.iddrogas_Anmat like '% clorhidrato' or " +
				"		sno.iddrogas_Anmat like 'cloruro de %' or " +
				"		sno.iddrogas_Anmat like 'oxido de %'" +
				");");	
	

		managerDB.executeScript_Void(" insert into "+UI.getSchemaName()+".drogas_snomed_filtradas  " +
				"(" +
				"select distinct 'null', anm.droga_nombre, 'T'" +
				"from "+UI.getSchemaName()+".drogas_anmat as anm inner join " +
					""+	UI.getSchemaName()+".drogas_snomed_filtradas as fil on fil.iddrogas_Anmat <> anm.droga_nombre " +
				"where 	" +
				"		(anm.droga_nombre like'%oxido%' or " +
				"		anm.droga_nombre like'%clorhidrato%' or " +
				"		anm.droga_nombre like'%cloruro%')  )");	
		
		
		// Creo la tabla donde van a ir los valores
		managerDB.executeScript_Void("CREATE TABLE "+UI.getSchemaName()+".droga_formaSimplificada (" +
				"`idDrogaOrigen` varchar(50) NOT NULL, " +
				"`idDrogaAncestro` varchar(50) NOT NULL, " +
				"`DrogaOrigen` varchar(200) DEFAULT NULL, " +
				"`DrogaAncestro` varchar(200) DEFAULT NULL, " +
				"PRIMARY KEY (`idDrogaOrigen`,`idDrogaAncestro`,`DrogaOrigen`));");

		// Inserto los valores resultantes de esta consulta
		managerDB.executeScript_Void(	"insert into `"+UI.getSchemaName()+"`.`droga_formaSimplificada` " +
				"(" +
				"select distinct sno1.iddrogas_Snomed, sno2.iddrogas_Snomed, sno1.iddrogas_Anmat, sno2.iddrogas_Anmat " +
				"from `"+UI.getSchemaName()+"`.`drogas_snomed_filtradas` as sno1 inner join `"+UI.getSchemaName()+"`.`drogas_snomed`  as sno2 on (sno1.iddrogas_Anmat like concat('%',sno2.iddrogas_Anmat,'%')) " +
				"where " +
				"(" +
				"sno1.iddrogas_Anmat like concat('clorhidrato de ',sno2.iddrogas_Anmat) or " +
				"sno1.iddrogas_Anmat like concat(sno2.iddrogas_Anmat,' clorhidrato') or " +
				"sno1.iddrogas_Anmat like concat('cloruro de ',sno2.iddrogas_Anmat) or " +
				"sno1.iddrogas_Anmat like concat('oxido de ',sno2.iddrogas_Anmat) " +
				")" +
				"and sno1.iddrogas_Snomed <> sno2.iddrogas_Snomed " +
				"group by sno1.iddrogas_Snomed, sno2.iddrogas_Snomed " +
				");");

		// Elimino la tabla que cree provisoria
		managerDB.executeScript_Void(" drop table "+UI.getSchemaName()+".drogas_snomed_filtradas;");

		// TODO Insertar los manuales
	}
	
	// ************************************************** Funciones auxiliares *****************************************************************************
	private void makeSQL(String tableNameToDrop, String insertStament, String createStament, String patho, String nameFile, String filtroRow, int columnaActive)
	{
		try{

			/* Elimino si existe la tabla previamente */
			if (!tableNameToDrop.isEmpty())
				managerDB.borrarTabla(tableNameToDrop);

			/* Listo los archivos que hay en la carpeta para hallar el que se llama descripcion*/ 
			File[] listOfFiles = new File(patho).listFiles();
			int i=0;
			while ( i<listOfFiles.length && !listOfFiles[i].toString().contains(nameFile) )
				i++;

			// Si encontre el archivo creo la tabla, genero un scrip de insertar, lo ejecuto desde la consola y lo borro e inserto los datos
			if (i<listOfFiles.length)
			{
				managerDB.executeScript_Void(createStament);				

				if (managerDB.createExecutorFile(UI.getPathFiles(), "prueba")){
					ParserSNOMED.fileToSqlScript(patho,listOfFiles[i].getName().toString(),insertStament, filtroRow,columnaActive);
					ParserSNOMED.executeSqlScript();
				}
			}
			else
				JOptionPane.showMessageDialog(null, "No se pudo hallar el archivo " + nameFile );	

		}
		catch (Exception e){		
			StringWriter errors = new StringWriter();
			e.printStackTrace(new PrintWriter(errors));
			JOptionPane.showMessageDialog(null, errors.toString() );}	
	}
	private void makeSQLSinonimos(String insertStament, String path, String nameFile, int columnaActive, HashSet<String> listaDrogasID, HashSet<String> listaDrogasIDTermino){

		/* Listo los archivos que hay en la carpeta para hallar el que se llama descripcion*/ 
		File[] listOfFiles = new File(path).listFiles();
		int i=0;
		while ( i<listOfFiles.length && !listOfFiles[i].toString().contains(nameFile) )
			i++;

		// Si encontre el archivo creo la tabla, genero un scrip de insertar, lo ejecuto desde la consola y lo borro e inserto los datos
		if (i<listOfFiles.length){
			if (managerDB.createExecutorFile(UI.getPathFiles(), "prueba")){
				ParserSNOMED.fileToSqlScriptSinonimos(path,listOfFiles[i].getName().toString(),insertStament, columnaActive, listaDrogasID, listaDrogasIDTermino);
				ParserSNOMED.executeSqlScript();
			}
		}
	}

	// ************************************************** Funciones adicionales *****************************************************************************
	public void buscarMedicamentos(JTable table){
		
		TableModel_withClases model;
		String[] titul = {"Certeza","Termino no hallado","Sugerencia","Es equivaltente? SI","Es equivaltente? NO","id_snomed" };
		model = new TableModel_withClases(titul);
		table.setModel(model);

		List<Expresion> provi = new ArrayList<Expresion>();

		managerDB.executeScript_Void("drop TABLE `"+UI.getSchemaName()+"`.`aux`;");
		managerDB.executeScript_Void("CREATE TABLE `"+UI.getSchemaName()+"`.`aux` ( `id_manfar` varchar(25) NOT NULL,PRIMARY KEY (`id_manfar`));");
		
		managerDB.executeScript_Void("" +
				"INSERT INTO `"+UI.getSchemaName()+"`.`aux` " +
						"( " +
							"SELECT distinct man.id_manfar " +
							"FROM `"+UI.getSchemaName()+"`.medicamentos_manfar as man " +
									"inner join `"+UI.getSchemaName()+"`.drogas_anmat as an on (man.nombre = an.medicamento_nombre) " +
						");");


		List<String> valores = new ArrayList<String>(); valores.add("nombre");	valores.add("id_manfar");		

		List<List<String>> lista_med_faltan = managerDB.executeScript_Query(			
				"select distinct ma.nombre, ma.id_manfar " +
				"from `"+UI.getSchemaName()+"`.`medicamentos_manfar` as ma " +
				"where not exists ( " +
				"					SELECT *  FROM `"+UI.getSchemaName()+"`.`aux` as man  " +
				"					where ma.id_manfar=man.id_manfar )" +
				"	   AND ma.original='S';",valores);


		
		List<String> valores2 = new ArrayList<String>(); valores2.add("medicamento_nombre"); valores2.add("presentacion_texto"); valores2.add("formaFarmaceutica");		

		List<List<String>> lista_med_existen2 = managerDB.executeScript_Query(			
				"select distinct ma.medicamento_nombre, ma.presentacion_texto, ma.formaFarmaceutica  " +
				"from `"+UI.getSchemaName()+"`.`drogas_anmat` as ma " +
				";",valores2);
		
		Hashtable<String, String> lista_med_existen = new Hashtable<String, String>();

		Hashtable<String, String> medicamentos = new Hashtable<String, String>();
		
		
		for (List<String> string : lista_med_existen2) {
			medicamentos.put(string.get(0), string.get(1) + " " + string.get(2));
		}
		
		
		
		
		//List<List<String>> lista_med_existen = managerDB.consultar("`drugtesis`.`drogas_anmat`", "true", valores);
		List<String> exclusion = new ArrayList<String>();


		exclusion.add("AMP");
		exclusion.add("O\\.5");
		exclusion.add("O\\.10");
		exclusion.add("O\\.3");

		exclusion.add("emulsion");
		exclusion.add("MG");			
		exclusion.add("AMP");
		exclusion.add("FCO");
		exclusion.add("COMP");
		exclusion.add("X");
		exclusion.add("GRS");
		exclusion.add("1\\.000\\.000");
		exclusion.add("F\\.AMP\\.");
		exclusion.add("UI");
		exclusion.add("GRAGEAS");
		exclusion.add("INY");
		exclusion.add("SOL");
		exclusion.add("PERIF");
		exclusion.add("0\\.25");
		exclusion.add("0\\.50");
		exclusion.add("0\\.050");


	
		

		lista_med_existen= Expresion.removeNumbersInListofPhrases(medicamentos);
		lista_med_faltan= Expresion.removeNumbersInListofPhrases2(lista_med_faltan);

	//	lista_med_existen= lista_med_existen.subList(0, 500);

		for (List<String> faltante : lista_med_faltan) 
		{


			for (String sugerencia : lista_med_existen.keySet()) {


				float valor = Expresion.getCorrelacionT2(faltante.get(0), sugerencia,exclusion, (float)1.0, (float)0.0);
				if (valor > 0){
//					String[] titul = {"Certeza","Termino no hallado","Sugerencia","Es equivaltente? SI","Es equivaltente? NO","id_snomed" };
					provi.add( new Expresion(faltante.get(2),sugerencia + "_" + lista_med_existen.get(sugerencia).toString(),valor,faltante.get(1)));
				}
			}

		}	


		Collections.sort(provi);




		Object[] aux = {"a","e","b","c","d","e"};
		for (Expresion string : provi) {
			aux[0] = string.getCorrelacion()*100 + "%";
			aux[1] = string.getTerminoSugerido();
			aux[2] = string.getTerminoOriginal();
			aux[3] = new Boolean(false);
			aux[4] = new Boolean(false);
			aux[5] = string.getIdSnomed();
			model.addRow(aux);

		}
		//				table.setModel(model);				
		table.updateUI();
		table.repaint();
	
	}

	public static List<String> loadTestFiles(String path){
		/* Listo los archivos que hay en la carpeta para hallar el que se llama descripcion*/ 
		File[] listOfFiles = new File(path).listFiles();
		
		List<String> listFile = new ArrayList<String>(); 

		
		for (File file : listOfFiles) {
			if ( file.getName().startsWith("testFile"))
				listFile.add(file.getName());
		}
	
	return listFile; 	
		
	}
	public boolean containsAllEnviromentVariables(Hashtable<String, String> valores) {
		return valores.keySet().containsAll(Arrays.asList(UI.UBICACION_MYSQL, UI.USUARIO, UI.PASSWORD, UI.ESQUEMA, UI.CARPETA_FILES,UI.CARPETA_SNOMED,UI.ESQUEMA,UI.IP_WEB_SERVICE));
		
	}	
}
