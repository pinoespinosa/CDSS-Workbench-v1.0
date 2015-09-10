package userint;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog.ModalExclusionType;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.xml.soap.SOAPException;

import Auxiliares.SimpleFile;
import Auxiliares.TableModel_withClases;
import BaseDeDatos.Expresion;
import BaseDeDatos.managerDB;
import Elements.Test;
import Parsers.ParserCVS;
import Parsers.ParserSNOMED;

public class UI {

	public static final String 	
			UBICACION_MYSQL="ubicacionMYSQL",
			USUARIO="usuario",
			PASSWORD="password",
			ESQUEMA="esquema",
			CARPETA_SNOMED="carpetaSnomed",
			CARPETA_FILES="carpetaFiles",
			IP_WEB_SERVICE="ipWebService";

	JTextPane textPane;

	private JFrame frmTesis;
	private JTable table;
	private static JTextField txt_userBD, txt_passwordBD;
	private static JTextField txt_esquema;
	private TableModel_withClases model;

	private JButton 
	btn_tRelaciones_crear,
	btn_tConceptos_crear;
	private static JTextField txt_pathSnomed;
	private static JTextField txt_pathFiles;
	private JTextField txtCarpeta;
	private JTextField txtArchivo;
	private static Hashtable<String, String> valores;
	private UI_Controller controller;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField txt_ipSoapWebService;





	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UI window = new UI();
					window.frmTesis.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public UI() {
		valores = new Hashtable<String, String>();		
		
		// Leo el archivo de configuracion, lo parseo y lo almaceno en la hash de valores
		List<String> confFile = SimpleFile.readFile(new File("").getAbsolutePath(), "\\conf.ini");
		for (String string : confFile){
			valores.put(string.split("_")[0], string.substring(string.indexOf("_")+1));	}
		controller=new UI_Controller();
		
		
		// Chequeo que esten todas las variables para iniciar la aplicacion
		if (controller.containsAllEnviromentVariables(valores))
			initialize();
		else
			JOptionPane.showMessageDialog(null, "Existe un error en el archivo de configuración. La aplicacion se detuvo.");
	}

	/**
	 * Initialize the contents of the frame.
	 * @wbp.parser.entryPoint
	 */
	private void initialize() {


		frmTesis = new JFrame();
		frmTesis.setModalExclusionType(ModalExclusionType.APPLICATION_EXCLUDE);
		frmTesis.setTitle("CDSS-Workbench v1.0");
		frmTesis.setBounds(100, 100, 670, 797);
		frmTesis.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmTesis.getContentPane().setLayout(new BoxLayout(frmTesis.getContentPane(), BoxLayout.X_AXIS));

		Expresion.valores.setDefault(1.0);
		Expresion.valores.put("clorhidrato",0.1);	
		Expresion.valores.put("vitamina",0.1);	
		Expresion.valores.put("cloruro",0.1);
		Expresion.valores.put("fosfato",0.1);


		final JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		frmTesis.getContentPane().add(tabbedPane);

		JPanel panel = new JPanel();
		tabbedPane.addTab("1.Crear BD", null, panel, null);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{0, 203, 147, 0, 0};
		gbl_panel.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_panel.columnWeights = new double[]{1.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{1.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);

		btn_tConceptos_crear = new JButton("1.1 - Instanciar tabla Conceptos");
		btn_tConceptos_crear.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent arg0) {
				doActionController(btn_tConceptos_crear, "instanciarTablaConceptos");
			}
		});
		
		final JButton btn_1ClicConstructor = new JButton("1Click Constructor");
		btn_1ClicConstructor.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				doActionController(btn_1ClicConstructor, "instanciarEsquema",false);
				doActionController(btn_1ClicConstructor, "instanciarTablaConceptos",false);
				doActionController(btn_1ClicConstructor, "instanciarTablaDescripciones",false);
				doActionController(btn_1ClicConstructor, "instanciarTablaCalificadores",false);
				
				doActionController(btn_1ClicConstructor, "instanciarBDAleman",false);
				doActionController(btn_1ClicConstructor, "instanciarTablaAnmat",false);
				doActionController(btn_1ClicConstructor, "instanciarTablaExcepciones",false);
				
				doActionController(btn_1ClicConstructor, "instanciarTablaInteracciones",false);
				doActionController(btn_1ClicConstructor, "instanciarTablasDosificaciones",false);
				doActionController(btn_1ClicConstructor, "instanciarTablasInteraccionesCondiciones",false);
				doActionController(btn_1ClicConstructor, "instanciarTablasReportes",false);
				
				
				doActionController(btn_1ClicConstructor, "createProcesatedTableFromDrogasSnomed",false);
				doActionController(btn_1ClicConstructor, "createProcesatedTableFromCalificadoresSnomed",false);
				doActionController(btn_1ClicConstructor, "reProcesatedTableDrogasToCreateSimplefiedTableDrugs",false);
				
				doActionController(btn_1ClicConstructor, "instanciarTablasCasoTesting",false);
			
			}
		});
		GridBagConstraints gbc_btnNewButton_3 = new GridBagConstraints();
		gbc_btnNewButton_3.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnNewButton_3.gridwidth = 2;
		gbc_btnNewButton_3.insets = new Insets(0, 0, 5, 5);
		gbc_btnNewButton_3.gridx = 1;
		gbc_btnNewButton_3.gridy = 1;
		panel.add(btn_1ClicConstructor, gbc_btnNewButton_3);

		Component horizontalStrut_1 = Box.createHorizontalStrut(20);
		GridBagConstraints gbc_horizontalStrut_1 = new GridBagConstraints();
		gbc_horizontalStrut_1.insets = new Insets(0, 0, 5, 0);
		gbc_horizontalStrut_1.gridx = 3;
		gbc_horizontalStrut_1.gridy = 2;
		panel.add(horizontalStrut_1, gbc_horizontalStrut_1);

		Component verticalGlue = Box.createVerticalGlue();
		GridBagConstraints gbc_verticalGlue = new GridBagConstraints();
		gbc_verticalGlue.fill = GridBagConstraints.HORIZONTAL;
		gbc_verticalGlue.insets = new Insets(0, 0, 5, 5);
		gbc_verticalGlue.gridx = 1;
		gbc_verticalGlue.gridy = 3;
		panel.add(verticalGlue, gbc_verticalGlue);

		final JButton btnCrearEsquema = new JButton("1.0 - Crear esquema");
		btnCrearEsquema.setToolTipText("Crea un esquema donde se intancia todo el proyecto");
		btnCrearEsquema.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent arg0) {
				doActionController(btnCrearEsquema, "instanciarEsquema");
			}
		});

		GridBagConstraints gbc_btnCrearEsquema = new GridBagConstraints();
		gbc_btnCrearEsquema.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnCrearEsquema.gridwidth = 2;
		gbc_btnCrearEsquema.insets = new Insets(0, 0, 5, 5);
		gbc_btnCrearEsquema.gridx = 1;
		gbc_btnCrearEsquema.gridy = 4;
		panel.add(btnCrearEsquema, gbc_btnCrearEsquema);

		Component horizontalStrut = Box.createHorizontalStrut(20);
		GridBagConstraints gbc_horizontalStrut = new GridBagConstraints();
		gbc_horizontalStrut.insets = new Insets(0, 0, 5, 5);
		gbc_horizontalStrut.gridx = 0;
		gbc_horizontalStrut.gridy = 5;
		panel.add(horizontalStrut, gbc_horizontalStrut);
		GridBagConstraints gbc_btn_tConceptos_crear = new GridBagConstraints();
		gbc_btn_tConceptos_crear.fill = GridBagConstraints.HORIZONTAL;
		gbc_btn_tConceptos_crear.gridwidth = 2;
		gbc_btn_tConceptos_crear.insets = new Insets(0, 0, 5, 5);
		gbc_btn_tConceptos_crear.gridx = 1;
		gbc_btn_tConceptos_crear.gridy = 5;
		panel.add(btn_tConceptos_crear, gbc_btn_tConceptos_crear);

		btn_tRelaciones_crear = new JButton("1.2 - Instanciar tabla Relaciones");
		btn_tRelaciones_crear.setEnabled(false);
		btn_tRelaciones_crear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		btn_tRelaciones_crear.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {

				String createStament= "CREATE  TABLE `"+ txt_esquema.getText() +"`.`relations` (" +
						"  `id` VARCHAR(20) NOT NULL ," +
						"  `effectiveTime` VARCHAR(45) NOT NULL ," +
						"	`active` VARCHAR(45) NULL ," +
						"	`moduleId` VARCHAR(45) NULL ," +
						"  `sourceId` VARCHAR(45) NULL ," +
						"  `destinationId` VARCHAR(45) NULL ," +
						"  `relationshipGroup` VARCHAR(45) NULL ," +
						"  `typeId` VARCHAR(45) NULL ," +
						"  `characteristicTypeId` VARCHAR(45) NULL ," +
						"  `modifierId` VARCHAR(45) NULL ," +
						"  PRIMARY KEY (`id`, `effectiveTime`) );",
						dropStament=  "DROP TABLE `"+ txt_esquema.getText() +"`.`relations`;",
						insertStament= "INSERT INTO `"+ txt_esquema.getText() +"`.`relations` VALUES";
				boolean[] string = {false,false,false,false,false,false,false,false,false,false};				

				btn_tRelaciones_crear.setText("En ejecucion...");	
				btn_tRelaciones_crear.updateUI();
				JOptionPane.showMessageDialog(frmTesis, "Esta acción puede demorar algunos minutos, por favor no cierre el programa.");						

				makeSQL(dropStament, insertStament, createStament, string, getPathSnomed()+"RF2Release/Snapshot/Terminology/","sct2_Relationship_Snapshot_INT_","",2);

				JOptionPane.showMessageDialog(frmTesis, "Se finalizó correctamente.");						
				btn_tRelaciones_crear.setText("1.2 - Instanciar tabla Relaciones");	
				btn_tRelaciones_crear.updateUI();			



			}
		});
		GridBagConstraints gbc_btn_tRelaciones_crear = new GridBagConstraints();
		gbc_btn_tRelaciones_crear.gridwidth = 2;
		gbc_btn_tRelaciones_crear.fill = GridBagConstraints.HORIZONTAL;
		gbc_btn_tRelaciones_crear.insets = new Insets(0, 0, 5, 5);
		gbc_btn_tRelaciones_crear.gridx = 1;
		gbc_btn_tRelaciones_crear.gridy = 6;
		panel.add(btn_tRelaciones_crear, gbc_btn_tRelaciones_crear);

		final JButton btn_filtro_activos = new JButton("2.0 - Creo drogas_snomed (Snomed + Manuales)");
		btn_filtro_activos.setToolTipText("Creo la tabla procesando los datos datos de drogas en la tabla snomed y las drogas manuales ");

		btn_filtro_activos.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				btn_filtro_activos.setText("En ejecucion...");	
				btn_filtro_activos.updateUI();
				JOptionPane.showMessageDialog(frmTesis, "Esta acción puede demorar algunos minutos, por favor no cierre el programa.");						

				controller.createProcesatedTableFromDrogasSnomed();

				JOptionPane.showMessageDialog(frmTesis, "Se finalizó correctamente.");						
				btn_filtro_activos.setText("2.1 - Realizar Filtrado por Actividad");	
				btn_filtro_activos.updateUI();			


			}
		});

		final JButton btnCargarTablaDescripciones = new JButton("1.3 - Instanciar tabla Descripcion (Drogas)");
		btnCargarTablaDescripciones.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {

				btnCargarTablaDescripciones.setText("En ejecucion...");	
				btnCargarTablaDescripciones.updateUI();
				JOptionPane.showMessageDialog(frmTesis, "Esta acción puede demorar algunos minutos, por favor no cierre el programa.");						
				try {
					controller.instanciarTablaDescripciones();
				} catch (Exception e2) {
					JOptionPane.showMessageDialog(null,e2);
					// TODO: handle exception

				}


				JOptionPane.showMessageDialog(frmTesis, "Se finalizó correctamente.");						
				btnCargarTablaDescripciones.setText("1.3 - Instanciar tabla Descripcion");	
				btnCargarTablaDescripciones.updateUI();			

			}

		});
		GridBagConstraints gbc_btnCargarTablaDescripciones = new GridBagConstraints();
		gbc_btnCargarTablaDescripciones.gridwidth = 2;
		gbc_btnCargarTablaDescripciones.fill = GridBagConstraints.BOTH;
		gbc_btnCargarTablaDescripciones.insets = new Insets(0, 0, 5, 5);
		gbc_btnCargarTablaDescripciones.gridx = 1;
		gbc_btnCargarTablaDescripciones.gridy = 7;
		panel.add(btnCargarTablaDescripciones, gbc_btnCargarTablaDescripciones);

		final JButton btn_CrearAnmat = new JButton("1.4 - Instanciar tabla ANMAT");
		btn_CrearAnmat.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent arg0) {

				btn_CrearAnmat.setText("En ejecucion...");	
				btn_CrearAnmat.updateUI();
				JOptionPane.showMessageDialog(frmTesis, "Esta acción puede demorar algunos minutos, por favor no cierre el programa.");						
				controller.instanciarTablaAnmat();	
				btn_CrearAnmat.setText("1.4 - Instanciar tabla ANMAT");
				btn_CrearAnmat.updateUI();
				JOptionPane.showMessageDialog(frmTesis, "Se finalizó correctamente.");

			}
		});

		final JButton btnNewButton = new JButton("1.4 - Instanciar tabla Descripcion (Calificadores)");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				doActionController(btnNewButton, "instanciarTablaCalificadores");
			}
		});
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnNewButton.gridwidth = 2;
		gbc_btnNewButton.insets = new Insets(0, 0, 5, 5);
		gbc_btnNewButton.gridx = 1;
		gbc_btnNewButton.gridy = 8;
		panel.add(btnNewButton, gbc_btnNewButton);
		GridBagConstraints gbc_btn_CrearAnmat = new GridBagConstraints();
		gbc_btn_CrearAnmat.fill = GridBagConstraints.HORIZONTAL;
		gbc_btn_CrearAnmat.gridwidth = 2;
		gbc_btn_CrearAnmat.insets = new Insets(0, 0, 5, 5);
		gbc_btn_CrearAnmat.gridx = 1;
		gbc_btn_CrearAnmat.gridy = 9;
		panel.add(btn_CrearAnmat, gbc_btn_CrearAnmat);

		final JButton btnNewButton_4 = new JButton("1.5 - Instanciar tabla de excepciones");
		btnNewButton_4.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent arg0) {
				doActionController(btnNewButton_4, "instanciarTablaExcepciones");			
			}
		});
		GridBagConstraints gbc_btnNewButton_4 = new GridBagConstraints();
		gbc_btnNewButton_4.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnNewButton_4.gridwidth = 2;
		gbc_btnNewButton_4.insets = new Insets(0, 0, 5, 5);
		gbc_btnNewButton_4.gridx = 1;
		gbc_btnNewButton_4.gridy = 10;
		panel.add(btnNewButton_4, gbc_btnNewButton_4);

		final JButton btnNewButton_6 = new JButton("1.6 - Instanciar tabla interacciones");
		btnNewButton_6.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				doActionController(btnNewButton_6, "instanciarTablaInteracciones");
			}
		});
		GridBagConstraints gbc_btnNewButton_6 = new GridBagConstraints();
		gbc_btnNewButton_6.gridwidth = 2;
		gbc_btnNewButton_6.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnNewButton_6.insets = new Insets(0, 0, 5, 5);
		gbc_btnNewButton_6.gridx = 1;
		gbc_btnNewButton_6.gridy = 11;
		panel.add(btnNewButton_6, gbc_btnNewButton_6);

		Component verticalGlue_1 = Box.createVerticalGlue();
		GridBagConstraints gbc_verticalGlue_1 = new GridBagConstraints();
		gbc_verticalGlue_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_verticalGlue_1.insets = new Insets(0, 0, 5, 5);
		gbc_verticalGlue_1.gridx = 1;
		gbc_verticalGlue_1.gridy = 12;
		panel.add(verticalGlue_1, gbc_verticalGlue_1);
		GridBagConstraints gbc_btn_filtro_activos = new GridBagConstraints();
		gbc_btn_filtro_activos.fill = GridBagConstraints.HORIZONTAL;
		gbc_btn_filtro_activos.gridwidth = 2;
		gbc_btn_filtro_activos.insets = new Insets(0, 0, 5, 5);
		gbc_btn_filtro_activos.gridx = 1;
		gbc_btn_filtro_activos.gridy = 13;
		panel.add(btn_filtro_activos, gbc_btn_filtro_activos);
		

		
		final JButton btn_instanciarBdAleman = new JButton("3. - Instanciar bdAleman");
		btn_instanciarBdAleman.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent arg0) {
				doActionController(btn_instanciarBdAleman, "instanciarBDAleman");
			}
		});

		final JButton btnAgregar = new JButton("2.5 - Agregar drogas padre en la tabla drogas");
		btnAgregar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				doActionController(btnAgregar, "reProcesatedTableDrogasToCreateSimplefiedTableDrugs");
			}
		});

		final JButton btnNewButton_2 = new JButton("2.1 - Creo calificadores_snomed (Snomed + Manuales)");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				doActionController(btnNewButton_2, "createProcesatedTableFromCalificadoresSnomed");
			}
		});
		GridBagConstraints gbc_btnNewButton_2 = new GridBagConstraints();
		gbc_btnNewButton_2.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnNewButton_2.gridwidth = 2;
		gbc_btnNewButton_2.insets = new Insets(0, 0, 5, 5);
		gbc_btnNewButton_2.gridx = 1;
		gbc_btnNewButton_2.gridy = 14;
		panel.add(btnNewButton_2, gbc_btnNewButton_2);
		GridBagConstraints gbc_btnAgregar = new GridBagConstraints();
		gbc_btnAgregar.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnAgregar.gridwidth = 2;
		gbc_btnAgregar.insets = new Insets(0, 0, 5, 5);
		gbc_btnAgregar.gridx = 1;
		gbc_btnAgregar.gridy = 15;
		panel.add(btnAgregar, gbc_btnAgregar);

		Component verticalGlue_3 = Box.createVerticalGlue();
		GridBagConstraints gbc_verticalGlue_3 = new GridBagConstraints();
		gbc_verticalGlue_3.insets = new Insets(0, 0, 5, 5);
		gbc_verticalGlue_3.gridx = 1;
		gbc_verticalGlue_3.gridy = 16;
		panel.add(verticalGlue_3, gbc_verticalGlue_3);
		GridBagConstraints gbc_btnNewButton_31 = new GridBagConstraints();
		gbc_btnNewButton_31.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnNewButton_31.gridwidth = 2;
		gbc_btnNewButton_31.insets = new Insets(0, 0, 5, 5);
		gbc_btnNewButton_31.gridx = 1;
		gbc_btnNewButton_31.gridy = 17;
		panel.add(btn_instanciarBdAleman, gbc_btnNewButton_31);
		
		final JButton btnInsertarTablasDosis = new JButton("Insertar tablas dosis");
		btnInsertarTablasDosis.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				doActionController(btnInsertarTablasDosis, "instanciarTablasDosificaciones");
			}
		});
		GridBagConstraints gbc_btnInsertarTablasDosis = new GridBagConstraints();
		gbc_btnInsertarTablasDosis.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnInsertarTablasDosis.gridwidth = 2;
		gbc_btnInsertarTablasDosis.insets = new Insets(0, 0, 5, 5);
		gbc_btnInsertarTablasDosis.gridx = 1;
		gbc_btnInsertarTablasDosis.gridy = 18;
		panel.add(btnInsertarTablasDosis, gbc_btnInsertarTablasDosis);
		
		final JButton btnInsertarTablasInteracciones = new JButton("Insertar tablas interacciones condicion");
		btnInsertarTablasInteracciones.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				doActionController(btnInsertarTablasInteracciones, "instanciarTablasInteraccionesCondiciones");
			}
		});
		GridBagConstraints gbc_btnInsertarTablasInteracciones = new GridBagConstraints();
		gbc_btnInsertarTablasInteracciones.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnInsertarTablasInteracciones.gridwidth = 2;
		gbc_btnInsertarTablasInteracciones.insets = new Insets(0, 0, 5, 5);
		gbc_btnInsertarTablasInteracciones.gridx = 1;
		gbc_btnInsertarTablasInteracciones.gridy = 19;
		panel.add(btnInsertarTablasInteracciones, gbc_btnInsertarTablasInteracciones);
		
		final JButton btnInsertarTablasTestreportes = new JButton("Insertar tablas test/reportes");
		btnInsertarTablasTestreportes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				doActionController(btnInsertarTablasTestreportes, "instanciarTablasReportes");
			}
		});
		GridBagConstraints gbc_btnInsertarTablasTestreportes = new GridBagConstraints();
		gbc_btnInsertarTablasTestreportes.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnInsertarTablasTestreportes.gridwidth = 2;
		gbc_btnInsertarTablasTestreportes.insets = new Insets(0, 0, 5, 5);
		gbc_btnInsertarTablasTestreportes.gridx = 1;
		gbc_btnInsertarTablasTestreportes.gridy = 20;
		panel.add(btnInsertarTablasTestreportes, gbc_btnInsertarTablasTestreportes);
		
		Component verticalGlue_2 = Box.createVerticalGlue();
		GridBagConstraints gbc_verticalGlue_2 = new GridBagConstraints();
		gbc_verticalGlue_2.insets = new Insets(0, 0, 0, 5);
		gbc_verticalGlue_2.gridx = 1;
		gbc_verticalGlue_2.gridy = 22;
		panel.add(verticalGlue_2, gbc_verticalGlue_2);

		JPanel panel_2 = new JPanel();
		tabbedPane.addTab("2.Buscar diccionario", null, panel_2, null);
		GridBagLayout gbl_panel_2 = new GridBagLayout();
		gbl_panel_2.columnWidths = new int[]{0, 0, 0, 0, 0};
		gbl_panel_2.rowHeights = new int[]{0, 0, 0, 0};
		gbl_panel_2.columnWeights = new double[]{0.0, 1.0, 1.0, 0.0, Double.MIN_VALUE};
		gbl_panel_2.rowWeights = new double[]{1.0, 0.0, 0.0, Double.MIN_VALUE};
		panel_2.setLayout(gbl_panel_2);

		final JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane_1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		GridBagConstraints gbc_scrollPane_1 = new GridBagConstraints();
		gbc_scrollPane_1.gridwidth = 2;
		gbc_scrollPane_1.insets = new Insets(0, 0, 5, 5);
		gbc_scrollPane_1.fill = GridBagConstraints.BOTH;
		gbc_scrollPane_1.gridx = 1;
		gbc_scrollPane_1.gridy = 0;
		panel_2.add(scrollPane_1, gbc_scrollPane_1);

		String[] titulos = {"Certeza","Termino no hallado","Sugerencia","Es equivaltente? SI","Es equivaltente? NO" };
		model = new TableModel_withClases(titulos);
		table = new JTable(model);

		scrollPane_1.setViewportView(table);
		JButton btnNewButton_1 = new JButton("Buscar equiv Drogas");
		btnNewButton_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {

				List<Expresion> provi = new ArrayList<Expresion>();


				List<String> lista_med_faltan = managerDB.executeScript_Query(			"select distinct drug.droga_nombre " +
						"from "+ txt_esquema.getText() +".drogas_anmat as drug " +
						"where not exists ( " +
						"	select * " +
						"	from "+ txt_esquema.getText() +".drogas_snomed as au " +
						"	where au.iddrogas_Anmat = drug.droga_nombre) " +
						"	;","droga_nombre");


				List<String> valores = new ArrayList<String>(); valores.add("iddrogas_Anmat"); valores.add("iddrogas_Snomed");			
				List<List<String>> lista_med_existen = managerDB.consultar(txt_esquema.getText() +".drogas_snomed", "true", valores);

				List<String> exclusion = new ArrayList<String>();
				exclusion.add("de");
				exclusion.add("cloruro");
				exclusion.add("clorhidrato");
				exclusion.add("sulfato");
				exclusion.add("cloruro");


				lista_med_existen=lista_med_existen.subList(0, lista_med_existen.size());

				int cant=0;
				float valor;
				for (String string : lista_med_faltan) 
				{
					cant++;
					System.out.println((float)cant/lista_med_faltan.size());
					for (List<String> string2 : lista_med_existen) {
						valor = Expresion.getCorrelacionT1(string, string2.get(0),exclusion, (float)0.9, (float)0.1);
						if (valor>0.2)
							provi.add( new Expresion(string,string2.get(0),valor,""));


					}

				}	


				Collections.sort(provi);


				System.out.println("el tamaño es " + provi.size());

				Object[] aux = {"a","e","b","c","d"};
				for (Expresion string : provi) {
					aux[0] = string.getCorrelacion()*100 + "%";
					aux[1] = string.getTerminoSugerido();
					aux[2] = string.getTerminoOriginal();
					aux[3] = new Boolean(string.getCorrelacion()>0.9);
					aux[4] = new Boolean(false);
					model.addRow(aux);

				}
				//				table.setModel(model);				
				table.updateUI();
			}
		});
		GridBagConstraints gbc_btnNewButton_1 = new GridBagConstraints();
		gbc_btnNewButton_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnNewButton_1.insets = new Insets(0, 0, 5, 5);
		gbc_btnNewButton_1.gridx = 1;
		gbc_btnNewButton_1.gridy = 1;
		panel_2.add(btnNewButton_1, gbc_btnNewButton_1);

		JButton btnRegistrarEquivalencias = new JButton("Registrar equivalencias");
		GridBagConstraints gbc_btnRegistrarEquivalencias = new GridBagConstraints();
		gbc_btnRegistrarEquivalencias.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnRegistrarEquivalencias.insets = new Insets(0, 0, 5, 5);
		gbc_btnRegistrarEquivalencias.gridx = 2;
		gbc_btnRegistrarEquivalencias.gridy = 1;
		panel_2.add(btnRegistrarEquivalencias, gbc_btnRegistrarEquivalencias);

		JButton btnBuscarEquivMedicamentos = new JButton("Buscar equiv Medicamentos");
		btnBuscarEquivMedicamentos.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {

			controller.buscarMedicamentos(table);

			}});
		GridBagConstraints gbc_btnBuscarEquivMedicamentos = new GridBagConstraints();
		gbc_btnBuscarEquivMedicamentos.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnBuscarEquivMedicamentos.insets = new Insets(0, 0, 0, 5);
		gbc_btnBuscarEquivMedicamentos.gridx = 1;
		gbc_btnBuscarEquivMedicamentos.gridy = 2;
		panel_2.add(btnBuscarEquivMedicamentos, gbc_btnBuscarEquivMedicamentos);

		JButton btnRegistrarMedic = new JButton("Registrar medic");
		btnRegistrarMedic.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent arg0) {

				for (int i=0; i<model.getRowCount();i++)
					if(model.getValueAt(i, 3).equals(true))
					{
						String ver = "INSERT INTO `testapp`.`medicamentos_manfar` values (" + model.getValueAt(i,5) + ",'" + model.getValueAt(i,1) + "','A');";
						System.out.println(ver);
						managerDB.executeScript_Void(ver);
					}


			}
		});
		GridBagConstraints gbc_btnRegistrarMedic = new GridBagConstraints();
		gbc_btnRegistrarMedic.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnRegistrarMedic.insets = new Insets(0, 0, 0, 5);
		gbc_btnRegistrarMedic.gridx = 2;
		gbc_btnRegistrarMedic.gridy = 2;
		panel_2.add(btnRegistrarMedic, gbc_btnRegistrarMedic);

		JPanel panel_1 = new JPanel();
		tabbedPane.addTab("3.Consultar", null, panel_1, null);
		GridBagLayout gbl_panel_1 = new GridBagLayout();
		gbl_panel_1.columnWidths = new int[]{0, 0, 0, 0};
		gbl_panel_1.rowHeights = new int[]{0, 0, 0, 0};
		gbl_panel_1.columnWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
		gbl_panel_1.rowWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
		panel_1.setLayout(gbl_panel_1);

		JLabel lblMedicamento = new JLabel("Nuevo medicamento");
		GridBagConstraints gbc_lblMedicamento = new GridBagConstraints();
		gbc_lblMedicamento.insets = new Insets(0, 0, 5, 5);
		gbc_lblMedicamento.anchor = GridBagConstraints.EAST;
		gbc_lblMedicamento.gridx = 0;
		gbc_lblMedicamento.gridy = 0;
		panel_1.add(lblMedicamento, gbc_lblMedicamento);

		final JComboBox<String> lista_todos_medicamentos = new JComboBox<String>();
		GridBagConstraints gbc_lista_todos_medicamentos = new GridBagConstraints();
		gbc_lista_todos_medicamentos.insets = new Insets(0, 0, 5, 5);
		gbc_lista_todos_medicamentos.fill = GridBagConstraints.HORIZONTAL;
		gbc_lista_todos_medicamentos.gridx = 1;
		gbc_lista_todos_medicamentos.gridy = 0;
		panel_1.add(lista_todos_medicamentos, gbc_lista_todos_medicamentos);

		JButton btnAadir = new JButton("A\u00F1adir");
		btnAadir.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {		
				textPane.setText(textPane.getText() + lista_todos_medicamentos.getSelectedItem().toString() + "\n");
			}
		});
		GridBagConstraints gbc_btnAadir = new GridBagConstraints();
		gbc_btnAadir.insets = new Insets(0, 0, 5, 0);
		gbc_btnAadir.gridx = 2;
		gbc_btnAadir.gridy = 0;
		panel_1.add(btnAadir, gbc_btnAadir);

		JLabel lblMedicamentos = new JLabel("Medicamentos");
		GridBagConstraints gbc_lblMedicamentos = new GridBagConstraints();
		gbc_lblMedicamentos.insets = new Insets(0, 0, 5, 5);
		gbc_lblMedicamentos.gridx = 0;
		gbc_lblMedicamentos.gridy = 1;
		panel_1.add(lblMedicamentos, gbc_lblMedicamentos);

		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPane.gridwidth = 2;
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 1;
		gbc_scrollPane.gridy = 1;
		panel_1.add(scrollPane, gbc_scrollPane);

		textPane = new JTextPane();
		scrollPane.setViewportView(textPane);

		JButton btnChequearInteracciones = new JButton("Chequear Interacciones");
		GridBagConstraints gbc_btnChequearInteracciones = new GridBagConstraints();
		gbc_btnChequearInteracciones.insets = new Insets(0, 0, 0, 5);
		gbc_btnChequearInteracciones.gridx = 1;
		gbc_btnChequearInteracciones.gridy = 2;
		panel_1.add(btnChequearInteracciones, gbc_btnChequearInteracciones);

		JPanel panel_3 = new JPanel();
		tabbedPane.addTab("4.Contrasena", null, panel_3, null);
		GridBagLayout gbl_panel_3 = new GridBagLayout();
		gbl_panel_3.columnWidths = new int[]{0, 52, 0, 0, 0};
		gbl_panel_3.rowHeights = new int[]{24, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_panel_3.columnWeights = new double[]{0.0, 1.0, 1.0, 0.0, Double.MIN_VALUE};
		gbl_panel_3.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel_3.setLayout(gbl_panel_3);

		JLabel lblPuerto = new JLabel("Usuario");
		lblPuerto.setToolTipText("");
		GridBagConstraints gbc_lblPuerto = new GridBagConstraints();
		gbc_lblPuerto.anchor = GridBagConstraints.EAST;
		gbc_lblPuerto.insets = new Insets(0, 0, 5, 5);
		gbc_lblPuerto.gridx = 1;
		gbc_lblPuerto.gridy = 1;
		panel_3.add(lblPuerto, gbc_lblPuerto);

		txt_userBD = new JTextField();
		txt_userBD.setText(getEnvirometValue(UI.USUARIO));
		GridBagConstraints gbc_txt_user = new GridBagConstraints();
		gbc_txt_user.insets = new Insets(0, 0, 5, 5);
		gbc_txt_user.fill = GridBagConstraints.HORIZONTAL;
		gbc_txt_user.gridx = 2;
		gbc_txt_user.gridy = 1;
		panel_3.add(txt_userBD, gbc_txt_user);
		txt_userBD.setColumns(10);

		JLabel lblPassword = new JLabel("Password");
		GridBagConstraints gbc_lblPassword = new GridBagConstraints();
		gbc_lblPassword.anchor = GridBagConstraints.EAST;
		gbc_lblPassword.insets = new Insets(0, 0, 5, 5);
		gbc_lblPassword.gridx = 1;
		gbc_lblPassword.gridy = 2;
		panel_3.add(lblPassword, gbc_lblPassword);

		txt_passwordBD = new JTextField();
		txt_passwordBD.setText(getEnvirometValue(UI.PASSWORD));
		GridBagConstraints gbc_txt_password = new GridBagConstraints();
		gbc_txt_password.insets = new Insets(0, 0, 5, 5);
		gbc_txt_password.fill = GridBagConstraints.HORIZONTAL;
		gbc_txt_password.gridx = 2;
		gbc_txt_password.gridy = 2;
		panel_3.add(txt_passwordBD, gbc_txt_password);
		txt_passwordBD.setColumns(10);

		JLabel lblEsquema = new JLabel("Esquema");
		GridBagConstraints gbc_lblEsquema = new GridBagConstraints();
		gbc_lblEsquema.anchor = GridBagConstraints.EAST;
		gbc_lblEsquema.insets = new Insets(0, 0, 5, 5);
		gbc_lblEsquema.gridx = 1;
		gbc_lblEsquema.gridy = 3;
		panel_3.add(lblEsquema, gbc_lblEsquema);

		txt_esquema = new JTextField();
		txt_esquema.setText(getEnvirometValue(UI.ESQUEMA));
		GridBagConstraints gbc_txt_esquema = new GridBagConstraints();
		gbc_txt_esquema.insets = new Insets(0, 0, 5, 5);
		gbc_txt_esquema.fill = GridBagConstraints.HORIZONTAL;
		gbc_txt_esquema.gridx = 2;
		gbc_txt_esquema.gridy = 3;
		panel_3.add(txt_esquema, gbc_txt_esquema);
		txt_esquema.setColumns(10);

		JLabel lblCarpetaArchivosSnomed = new JLabel("Carpeta Archivos Snomed");
		GridBagConstraints gbc_lblCarpetaArchivosSnomed = new GridBagConstraints();
		gbc_lblCarpetaArchivosSnomed.insets = new Insets(0, 0, 5, 5);
		gbc_lblCarpetaArchivosSnomed.anchor = GridBagConstraints.EAST;
		gbc_lblCarpetaArchivosSnomed.gridx = 1;
		gbc_lblCarpetaArchivosSnomed.gridy = 4;
		panel_3.add(lblCarpetaArchivosSnomed, gbc_lblCarpetaArchivosSnomed);

		txt_pathSnomed = new JTextField();
		txt_pathSnomed.setText(getEnvirometValue(UI.CARPETA_SNOMED));

		GridBagConstraints gbc_txt_pathSnomed = new GridBagConstraints();
		gbc_txt_pathSnomed.insets = new Insets(0, 0, 5, 5);
		gbc_txt_pathSnomed.fill = GridBagConstraints.HORIZONTAL;
		gbc_txt_pathSnomed.gridx = 2;
		gbc_txt_pathSnomed.gridy = 4;
		panel_3.add(txt_pathSnomed, gbc_txt_pathSnomed);
		txt_pathSnomed.setColumns(10);

		JLabel lblCarpetaArchivoAnmat = new JLabel("Carpeta Archivo Anmat");
		GridBagConstraints gbc_lblCarpetaArchivoAnmat = new GridBagConstraints();
		gbc_lblCarpetaArchivoAnmat.insets = new Insets(0, 0, 5, 5);
		gbc_lblCarpetaArchivoAnmat.anchor = GridBagConstraints.EAST;
		gbc_lblCarpetaArchivoAnmat.gridx = 1;
		gbc_lblCarpetaArchivoAnmat.gridy = 5;
		panel_3.add(lblCarpetaArchivoAnmat, gbc_lblCarpetaArchivoAnmat);

		txt_pathFiles = new JTextField();
		txt_pathFiles.setText(getEnvirometValue(UI.CARPETA_FILES));
		GridBagConstraints gbc_txt_pathAnmat = new GridBagConstraints();
		gbc_txt_pathAnmat.insets = new Insets(0, 0, 5, 5);
		gbc_txt_pathAnmat.fill = GridBagConstraints.HORIZONTAL;
		gbc_txt_pathAnmat.gridx = 2;
		gbc_txt_pathAnmat.gridy = 5;
		panel_3.add(txt_pathFiles, gbc_txt_pathAnmat);
		txt_pathFiles.setColumns(10);
		
		JLabel lblIpSoapWebservice = new JLabel("IP SOAP WebService");
		
		GridBagConstraints gbc_lblIpSoapWebservice = new GridBagConstraints();
		gbc_lblIpSoapWebservice.insets = new Insets(0, 0, 5, 5);
		gbc_lblIpSoapWebservice.anchor = GridBagConstraints.EAST;
		gbc_lblIpSoapWebservice.gridx = 1;
		gbc_lblIpSoapWebservice.gridy = 6;
		panel_3.add(lblIpSoapWebservice, gbc_lblIpSoapWebservice);
		
		txt_ipSoapWebService = new JTextField();
		txt_ipSoapWebService.setText(getEnvirometValue(UI.IP_WEB_SERVICE));
		GridBagConstraints gbc_txt_ipSoapWebService = new GridBagConstraints();
		gbc_txt_ipSoapWebService.insets = new Insets(0, 0, 5, 5);
		gbc_txt_ipSoapWebService.fill = GridBagConstraints.HORIZONTAL;
		gbc_txt_ipSoapWebService.gridx = 2;
		gbc_txt_ipSoapWebService.gridy = 6;
		panel_3.add(txt_ipSoapWebService, gbc_txt_ipSoapWebService);
		txt_ipSoapWebService.setColumns(10);

		JLabel lblNewLabel = new JLabel("Funciones Adicionales");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.gridwidth = 2;
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.gridx = 1;
		gbc_lblNewLabel.gridy = 10;
		panel_3.add(lblNewLabel, gbc_lblNewLabel);

		txtCarpeta = new JTextField();
		txtCarpeta.setText("C:\\Users\\ARGENTINA\\Google Drive\\PC\\Tesis\\SnomedCT_Release_INT_20140731\\");
		GridBagConstraints gbc_txtCarpeta = new GridBagConstraints();
		gbc_txtCarpeta.insets = new Insets(0, 0, 5, 5);
		gbc_txtCarpeta.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtCarpeta.gridx = 1;
		gbc_txtCarpeta.gridy = 11;
		panel_3.add(txtCarpeta, gbc_txtCarpeta);
		txtCarpeta.setColumns(10);

		txtArchivo = new JTextField();
		txtArchivo.setText("anmat.txt");
		GridBagConstraints gbc_txtArchivo = new GridBagConstraints();
		gbc_txtArchivo.insets = new Insets(0, 0, 5, 5);
		gbc_txtArchivo.fill = GridBagConstraints.HORIZONTAL;
		gbc_txtArchivo.gridx = 2;
		gbc_txtArchivo.gridy = 11;
		panel_3.add(txtArchivo, gbc_txtArchivo);
		txtArchivo.setColumns(10);

		JButton btnNewButton_5 = new JButton("filtrar");
		btnNewButton_5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				List<String> lista = SimpleFile.readFile(txtCarpeta.getText(), txtArchivo.getText());
				HashSet<String> a = new HashSet<String>();
				a.addAll(lista);
				lista.clear();
				lista.addAll(a);
				Collections.sort(lista);
				SimpleFile.writeFile(txtCarpeta.getText(), txtArchivo.getText(), lista);

			}
		});
		GridBagConstraints gbc_btnNewButton_5 = new GridBagConstraints();
		gbc_btnNewButton_5.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnNewButton_5.gridwidth = 2;
		gbc_btnNewButton_5.insets = new Insets(0, 0, 0, 5);
		gbc_btnNewButton_5.gridx = 1;
		gbc_btnNewButton_5.gridy = 12;
		panel_3.add(btnNewButton_5, gbc_btnNewButton_5);

		JPanel panel_4 = new JPanel();
		tabbedPane.addTab("5. Buscar Interacciones", null, panel_4, null);
		GridBagLayout gbl_panel_4 = new GridBagLayout();
		gbl_panel_4.columnWidths = new int[]{0, 0};
		gbl_panel_4.rowHeights = new int[]{0, 0};
		gbl_panel_4.columnWeights = new double[]{0.0, Double.MIN_VALUE};
		gbl_panel_4.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		panel_4.setLayout(gbl_panel_4);

		JButton btnRastrearInteracciones = new JButton("Rastrear Interacciones");
	
		GridBagConstraints gbc_btnRastrearInteracciones = new GridBagConstraints();
		gbc_btnRastrearInteracciones.gridx = 0;
		gbc_btnRastrearInteracciones.gridy = 0;
		panel_4.add(btnRastrearInteracciones, gbc_btnRastrearInteracciones);
		
		JPanel panel_5 = new JPanel();
		tabbedPane.addTab("Testing", null, panel_5, null);
		GridBagLayout gbl_panel_5 = new GridBagLayout();
		gbl_panel_5.columnWidths = new int[]{0, 0, 0, 0};
		gbl_panel_5.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_panel_5.columnWeights = new double[]{1.0, 1.0, 0.0, Double.MIN_VALUE};
		gbl_panel_5.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel_5.setLayout(gbl_panel_5);
		
		final JComboBox<Test> comboBoxTextFiles = new JComboBox<Test>();
		GridBagConstraints gbc_comboBox = new GridBagConstraints();
		gbc_comboBox.insets = new Insets(0, 0, 5, 5);
		gbc_comboBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBox.gridx = 0;
		gbc_comboBox.gridy = 1;
		panel_5.add(comboBoxTextFiles, gbc_comboBox);
		
		List<String> listFileTest = UI_Controller.loadTestFiles(getEnvirometValue(UI.CARPETA_FILES));
		for (String string : listFileTest) {
			comboBoxTextFiles.addItem(new Test(string));
		}
		
		JButton btnRunTest = new JButton("Run Test !");

		
		final JLabel lblResultText = new JLabel("");
		lblResultText.setOpaque(true);
		lblResultText.setHorizontalAlignment(SwingConstants.CENTER);
		
		GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
		gbc_lblNewLabel_1.fill = GridBagConstraints.BOTH;
		gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_1.gridx = 1;
		gbc_lblNewLabel_1.gridy = 1;
		panel_5.add(lblResultText, gbc_lblNewLabel_1);
		GridBagConstraints gbc_btnRunTest = new GridBagConstraints();
		gbc_btnRunTest.insets = new Insets(0, 0, 5, 0);
		gbc_btnRunTest.gridx = 2;
		gbc_btnRunTest.gridy = 1;
		panel_5.add(btnRunTest, gbc_btnRunTest);
		
		textField = new JTextField();
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.insets = new Insets(0, 0, 5, 5);
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.gridx = 0;
		gbc_textField.gridy = 2;
		panel_5.add(textField, gbc_textField);
		textField.setColumns(10);
		
		textField_1 = new JTextField();
		GridBagConstraints gbc_textField_1 = new GridBagConstraints();
		gbc_textField_1.insets = new Insets(0, 0, 5, 5);
		gbc_textField_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_1.gridx = 1;
		gbc_textField_1.gridy = 2;
		panel_5.add(textField_1, gbc_textField_1);
		textField_1.setColumns(10);
		
		final JButton btnInstanciarcasostesting = new JButton("InstanciarCasosTesting");
		btnInstanciarcasostesting.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				doActionController(btnInstanciarcasostesting, "instanciarTablasCasoTesting",false);
			}
		});
		GridBagConstraints gbc_btnInstanciarcasostesting = new GridBagConstraints();
		gbc_btnInstanciarcasostesting.insets = new Insets(0, 0, 5, 0);
		gbc_btnInstanciarcasostesting.gridwidth = 3;
		gbc_btnInstanciarcasostesting.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnInstanciarcasostesting.gridx = 0;
		gbc_btnInstanciarcasostesting.gridy = 4;
		panel_5.add(btnInstanciarcasostesting, gbc_btnInstanciarcasostesting);
		
		final JButton btnTestingDosis = new JButton("Testing Dosis");
		btnTestingDosis.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				doActionController(btnTestingDosis, "runCasoTestingDosis",false);
			}
		});
		GridBagConstraints gbc_btnTestingDosis = new GridBagConstraints();
		gbc_btnTestingDosis.insets = new Insets(0, 0, 5, 0);
		gbc_btnTestingDosis.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnTestingDosis.gridwidth = 3;
		gbc_btnTestingDosis.gridx = 0;
		gbc_btnTestingDosis.gridy = 5;
		panel_5.add(btnTestingDosis, gbc_btnTestingDosis);
		
		JButton btnNewButton_3 = new JButton("Testing Med");
		btnNewButton_3.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				doActionController(btnTestingDosis, "runCasoTestingMed",false);
			}
		});
		GridBagConstraints gbc_btnNewButton_63 = new GridBagConstraints();
		gbc_btnNewButton_63.insets = new Insets(0, 0, 5, 0);
		gbc_btnNewButton_63.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnNewButton_63.gridwidth = 3;
		gbc_btnNewButton_63.gridx = 0;
		gbc_btnNewButton_63.gridy = 6;
		panel_5.add(btnNewButton_3, gbc_btnNewButton_63);
		
		JButton btnNewButton_7 = new JButton("Test Masivo");
		btnNewButton_7.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				controller.runCasoTestingMedFragmento();
			}
		});
		btnNewButton_7.setToolTipText("Se ejecuta un test con 1000 consultas");
		GridBagConstraints gbc_btnNewButton_7 = new GridBagConstraints();
		gbc_btnNewButton_7.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnNewButton_7.gridwidth = 3;
		gbc_btnNewButton_7.insets = new Insets(0, 0, 0, 5);
		gbc_btnNewButton_7.gridx = 0;
		gbc_btnNewButton_7.gridy = 7;
		panel_5.add(btnNewButton_7, gbc_btnNewButton_7);



		GridBagConstraints gbc_chckbxNewCheckBox = new GridBagConstraints();
		gbc_chckbxNewCheckBox.insets = new Insets(0, 0, 0, 5);
		gbc_chckbxNewCheckBox.gridx = 0;
		gbc_chckbxNewCheckBox.gridy = 4;

		List<String> lista_med = managerDB.executeScript_Query("SELECT concat(man.id_manfar,\" -  \" ,upper(man.nombre)) as nombre FROM "+txt_esquema.getText()+".medicamentos_manfar as man  group by man.id_manfar;", "nombre");

		if (lista_med!=null)
			for (String string : lista_med) {
				lista_todos_medicamentos.addItem(string);
			}



		btnRunTest.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				try {
					if(((Test)comboBoxTextFiles.getSelectedItem()).runTest()){
						lblResultText.setBackground(Color.GREEN);
						lblResultText.setText("Successful");
						}
					else{
						lblResultText.setBackground(Color.RED);
						lblResultText.setText("Fail");
						}
				} catch (SOAPException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});



	}

	public void doActionController(JButton button, String controller_metodo){
		doActionController(button, controller_metodo,true);
	}
	/**
	 * 
	 * 
	 * @param button Boton que origina el evento
	 * @param controller_metodo Metodo a invocar en el controlador
	 * @param activeUI Flag que determina si se muestran dialogos de inicio y fin del proceso
	 */
	public void doActionController(JButton button, String controller_metodo, boolean activeUI){
		String texto = button.getText();
		button.setText("En ejecucion...");	
		button.updateUI();
		if (activeUI)
			JOptionPane.showMessageDialog(frmTesis, "Esta acción puede demorar algunos minutos, por favor no cierre el programa.");						
		try {
			controller.getClass().getMethod(controller_metodo).invoke(controller);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (activeUI)
			JOptionPane.showMessageDialog(frmTesis, "Se finalizó correctamente.");						
		button.setText(texto);	
		button.updateUI();	
	}

	public void makeSQL(String dropStament, String insertStament, String createStament, boolean[] isString, String patho, String nameFile, String filtroRow, int columnaActive){

		/* Elimino si existe la tabla previamente */
		managerDB.executeScript_Void(dropStament);

		/* Listo los archivos que hay en la carpeta para hallar el que se llama descripcion*/ 
		File[] listOfFiles = new File(patho).listFiles();
		int i=0;
		while ( i<listOfFiles.length && !listOfFiles[i].toString().contains(nameFile) )
			i++;

		// Si encontre el archivo creo la tabla, genero un scrip de insertar, lo ejecuto desde la consola y lo borro e inserto los datos
		if (i<listOfFiles.length){

			managerDB.executeScript_Void(createStament);				

			if (managerDB.createExecutorFile("C:\\", "prueba")){
				ParserSNOMED.fileToSqlScript(patho,listOfFiles[i].getName().toString(),insertStament, filtroRow,columnaActive);
				System.out.println(patho + listOfFiles[i].getName().toString());
				try{
					Thread.sleep(1000);  

					Runtime.getRuntime().exec("cmd /c start C:\\prueba.bat");
					managerDB.waitUntisFinishConsole("C://","done.prov" );

					// Borro todos los archivos luego de que termine el script
					File file = new File("C:\\done.prov"); 	file.delete();
					//			file = new File("C:\\auxFile.sql");    	file.delete();
					file = new File("C:\\prueba.bat");		file.delete();
				}
				catch (Exception e1) {
					e1.printStackTrace();}				}
		}
	}
	public void makeSQLSinonimos(String insertStament, boolean[] isString, String patho, String nameFile, int columnaActive, HashSet<String> listaDrogasID, HashSet<String> listaDrogasIDTermino){

		/* Listo los archivos que hay en la carpeta para hallar el que se llama descripcion*/ 
		File[] listOfFiles = new File(patho).listFiles();
		int i=0;
		while ( i<listOfFiles.length && !listOfFiles[i].toString().contains(nameFile) )
			i++;

		// Si encontre el archivo creo la tabla, genero un scrip de insertar, lo ejecuto desde la consola y lo borro e inserto los datos
		if (i<listOfFiles.length){


			if (managerDB.createExecutorFile("C:\\", "prueba")){
				ParserSNOMED.fileToSqlScriptSinonimos(patho,listOfFiles[i].getName().toString(),insertStament, columnaActive, listaDrogasID, listaDrogasIDTermino);
				System.out.println(patho + listOfFiles[i].getName().toString());
				try{
					Thread.sleep(1000);  

					Runtime.getRuntime().exec("cmd /c start C:\\prueba.bat");
					managerDB.waitUntisFinishConsole("C://","done.prov" );

					// Borro todos los archivos luego de que termine el script
					File file = new File("C:\\done.prov"); 	file.delete();
					//			file = new File("C:\\auxFile.sql");    	file.delete();
					file = new File("C:\\prueba.bat");		file.delete();
				}
				catch (Exception e1) {
					e1.printStackTrace();}				}
		}
	}

	private void addManfar(String path, Vector <Integer> col, boolean ordenInverso, String letra ){

		List<List<String>> bdManfar = ParserCVS.parseCVS(path,col);

		bdManfar = ParserCVS.quitarRepetidos(bdManfar);

		List<String> a = new ArrayList<String>(); a.add("id_manfar"); a.add("nombre");

		List<List<String>> enlaBD = managerDB.buscar("`"+txt_esquema.getText()+ "`.`medicamentos_manfar`", "id_manfar", "", a, false);

		System.out.println(bdManfar.size()) ;

		for (List<String> list : enlaBD) {
			if (bdManfar.contains(list)){
				bdManfar.remove(list);}
		}

		System.out.println(bdManfar.size()) ;


		String script = "INSERT INTO `"+txt_esquema.getText()+"`.`medicamentos_manfar` VALUES ";


		for (List<String> list : bdManfar) {
			if (ordenInverso)
				script += "('" + list.get(1) + "','" + list.get(0) + "','"+letra+"'),\n";
			else
				script += "('" + list.get(0) + "','" + list.get(1) + "','"+letra+"'),\n";

		}


		script = script.substring(0, script.length()-2);
		script+=";";

		System.out.println(script);
		managerDB.executeScript_Void(script);


	}
	public static String getSchemaName(){
		return txt_esquema.getText();
	}
	public static String getPathSnomed(){
		if  (!(txt_pathSnomed.getText().endsWith("/")||txt_pathSnomed.getText().endsWith("\\")) )
			return txt_pathSnomed.getText() + "/" ;
		else
			return txt_pathSnomed.getText();

	}
	public static String getPathFiles(){
		if  (!(txt_pathFiles.getText().endsWith("/")||txt_pathFiles.getText().endsWith("\\")) )
			return txt_pathFiles.getText() + "/" ;
		else
			return txt_pathFiles.getText();
	}
	public static String getRootBD(){
		return txt_userBD.getText();
	}
	public static String getPasswordBD(){
		return txt_passwordBD.getText();
	}
	public static String getEnvirometValue(String variable){
		return valores.get(variable);
	}
	
}
