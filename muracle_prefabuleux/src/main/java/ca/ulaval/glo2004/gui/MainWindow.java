package ca.ulaval.glo2004.gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;


/**
 * @author Jerome Levesque
 */
public class MainWindow extends JFrame {

	private double distLigneGrille = 1;
	private char coteSelected = ' ';
	private int murSelected = -1;
	private Point accessoireSelected = null;

	public MainWindow(){
		initComponents();
	}

	private void initComponents() {
		//menu
		JMenuBar menuBar = new JMenuBar();
		JButton openProjectButton = new JButton();
		JButton saveProjectButton = new JButton();
		JButton exportButton = new JButton();
		JToggleButton showGrilleButton = new JToggleButton();
		JToggleButton addAccessoireButton = new JToggleButton();
		JComboBox<String> selectionAccessoireComboBox = new JComboBox<>(new String[] {"Fenêtre", "Porte", "Prise électrique", "Retour d'air"});
		JButton retourVueHautButton = new JButton();
		JToggleButton changeVueButton = new JToggleButton();
		JButton undoButton = new JButton();
		JButton redoButton = new JButton();

		JSplitPane splitPaneH = new JSplitPane();
		JSplitPane splitPaneV = new JSplitPane();

		//Panneau de dessin
		JPanel drawingPanel = new JPanel();

		//seulement pour demo
		JButton coteButton = new JButton();
		JToggleButton murButton = new JToggleButton();
		JToggleButton accessoireButton = new JToggleButton();

		//panneau des parametres
		JPanel parametresPanel = new JPanel();
		JLabel parametreLabel = new JLabel();

		//parametre modifiable
		JPanel parametresModifPanel = new JPanel();

		JLabel largSalleLabel = new JLabel();
		JTextField largSalleTextField = new JTextField();
		JLabel largSalleEndLabel = new JLabel();

		JLabel longSalleLabel = new JLabel();
		JTextField longSalleTextField = new JTextField();
		JLabel longSalleEndLabel = new JLabel();

		JLabel hSalleLabel = new JLabel();
		JTextField hSalleTextField = new JTextField();
		JLabel hSalleEndLabel = new JLabel();

		JLabel epMursLabel = new JLabel();
		JTextField epMursTextField = new JTextField();
		JLabel epMursEndLabel = new JLabel();

		JSeparator sepParam1 = new JSeparator();

		JLabel epTrouLabel = new JLabel();
		JTextField epTrouTextField = new JTextField();
		JLabel epTrouEndLabel = new JLabel();

		JLabel hRetourAirLabel = new JLabel();
		JTextField hRetourAirTextField = new JTextField();
		JLabel hRetourAirEndLabel = new JLabel();

		JLabel distSolRetourAirLabel = new JLabel();
		JTextField distSolRetourAirTextField = new JTextField();
		JLabel distSolRetourAirEndLabel = new JLabel();

		JLabel largAccesLabel = new JLabel();
		JTextField largAccesTextField = new JTextField();
		JLabel largAccesEndLabel = new JLabel();

		JLabel hAccesLabel = new JLabel();
		JTextField hAccesTextField = new JTextField();
		JLabel hAccesEndLabel = new JLabel();

		JSeparator sepParam2 = new JSeparator();

		JLabel distGrilleLabel = new JLabel();
		JTextField distGrilleTextField = new JTextField();
		JLabel distGrilleEndLabel = new JLabel();

		//panneau de configuration
		JPanel configurationPanel = new JPanel();
		JLabel configurationLabel = new JLabel();
		JPanel configurationPlanPanel = new JPanel();

		JLabel longPlisLabel = new JLabel();
		JTextField longPlisTextField = new JTextField();
		JLabel longPlisEndLabel = new JLabel();

		JLabel margeEpLabel = new JLabel();
		JTextField margeEpTextField = new JTextField();
		JLabel margeEpEndLabel = new JLabel();

		JLabel margeLargLabel = new JLabel();
		JTextField margeLargTextField = new JTextField();
		JLabel margeLargEndLabel = new JLabel();

		JLabel anglePlisLabel = new JLabel();
		JTextField anglePlisTextField = new JTextField();
		JLabel anglePlisEndLabel = new JLabel();

		JSeparator sepConfig = new JSeparator();

		JToggleButton voirPlanButton = new JToggleButton();

		{
			this.setTitle("Muracle");
			//var frame1ContentPane = this.getContentPane();
			this.getContentPane().setLayout(new GridLayout());

			//======== menuBar ========
			{

				//---- ouvrir projet ----
				openProjectButton.setText("Ouvrir projet");
				openProjectButton.setPreferredSize(new Dimension(140, 22));
				openProjectButton.setMaximumSize(new Dimension(100, 32767));
				openProjectButton.setHorizontalTextPosition(SwingConstants.CENTER);
				openProjectButton.setRequestFocusEnabled(false);
				openProjectButton.setFocusPainted(false);
				openProjectButton.addActionListener(e -> {
					JFileChooser fileChooser = new JFileChooser();
					fileChooser.setDialogTitle("Ouverture de Projet");
					fileChooser.setFileFilter(new FileNameExtensionFilter("*.txt", "txt"));
					int returnValue = fileChooser.showOpenDialog(this);
					if (returnValue == JFileChooser.APPROVE_OPTION) {
						File fichier = fileChooser.getSelectedFile();
						System.out.println("Ouverture du fichier : " + fichier.getAbsolutePath());
					}
				});
				menuBar.add(openProjectButton);

				//---- sauvergarder projet ----
				saveProjectButton.setText("Sauvegarder projet");
				saveProjectButton.setMaximumSize(new Dimension(120, 32767));
				saveProjectButton.setPreferredSize(new Dimension(160, 22));
				saveProjectButton.setHorizontalTextPosition(SwingConstants.CENTER);
				saveProjectButton.setRequestFocusEnabled(false);
				saveProjectButton.addActionListener(e -> {
					JFileChooser fileChooser = new JFileChooser();
					fileChooser.setDialogTitle("Sauvegarde de Projet");
					fileChooser.setFileFilter(new FileNameExtensionFilter("*.txt", "txt"));
					int returnValue = fileChooser.showSaveDialog(this);
					if (returnValue == JFileChooser.APPROVE_OPTION) {
						File fichier = fileChooser.getSelectedFile();
						if(!fileChooser.getSelectedFile().getAbsolutePath().endsWith(".txt"))
							fichier = new File(fileChooser.getSelectedFile() + ".txt");
						try(FileWriter fw = new FileWriter(fichier)) {
							fw.write("test");
						} catch (Exception except) {
							except.printStackTrace();
						}
						System.out.println("Sauvegarde du fichier : " + fichier.getAbsolutePath());
					}
				});
				menuBar.add(saveProjectButton);

				//---- exporter plan ----
				exportButton.setText("Exporter plans");
				exportButton.setPreferredSize(new Dimension(140, 22));
				exportButton.setMaximumSize(new Dimension(100, 32767));
				exportButton.setHorizontalTextPosition(SwingConstants.CENTER);
				exportButton.setRequestFocusEnabled(false);
				exportButton.addActionListener(e -> {
					JFileChooser fileChooser = new JFileChooser();
					fileChooser.setDialogTitle("Exporter les plans");
					fileChooser.setFileFilter(new FileNameExtensionFilter("*.svg", "SVG"));
					int returnValue = fileChooser.showSaveDialog(this);
					if (returnValue == JFileChooser.APPROVE_OPTION) {
						File fichier = fileChooser.getSelectedFile();
						if(!fileChooser.getSelectedFile().getAbsolutePath().endsWith(".svg"))
							fichier = new File(fileChooser.getSelectedFile() + ".svg");

						//a faire
						try {
							XMLOutputFactory factory = XMLOutputFactory.newInstance();
							XMLStreamWriter writer = factory.createXMLStreamWriter(new FileOutputStream(fichier));
							writer.writeStartDocument();

							writer.writeStartElement("svg");
							writer.writeAttribute("xmlns", "http://www.w3.org/2000/svg");
							writer.writeAttribute("width", "600");
							writer.writeAttribute("height", "400");

							//example du plan d'un mur

							//plan
							writer.writeEmptyElement("path");
							writer.writeAttribute("d", "M 400 100 L 400 300 L 500 300 L 500 100 z" +
									"M 100 100 L 100 300 L 200 300 L 200 100 z");
							writer.writeAttribute("fill", "white");
							writer.writeAttribute("stroke", "black");
							writer.writeAttribute("stroke-width", "1");

							//plis
							writer.writeEmptyElement("path");
							writer.writeAttribute("d", "M 410 100 L 410 90 L 490 90 L 490 100 z" +
									"M 410 300 L 410 310 L 490 310 L 490 300 z" +
									"M 100 110 L 90 110 L 90 290 L 100 290 z" +
									"M 200 110 L 210 110 L 210 290 L 200 290 z");
							writer.writeAttribute("fill", "white");
							writer.writeAttribute("stroke", "orange");
							writer.writeAttribute("stroke-width", "1");

							//replis
							writer.writeEmptyElement("path");
							writer.writeAttribute("d", "M 410 90 L 430 80 L 470 80 L 490 90 z" +
									"M 410 310 L 430 320 L 470 320 L 490 310 z" +
									"M 90 110 L 80 120 L 80 280 L 90 290 z" +
									"M 210 110 L 220 120 L 220 280 L 210 290 z");
							writer.writeAttribute("fill", "white");
							writer.writeAttribute("stroke", "blue");
							writer.writeAttribute("stroke-width", "1");

							writer.writeEndDocument();

							writer.close();

						} catch (IOException | XMLStreamException ex) {
							throw new RuntimeException(ex);
						}

						System.out.println("Exportation des plans au fichier : " + fichier.getAbsolutePath());
					}
				});
				menuBar.add(exportButton);

				//---- afficher grille ----
				showGrilleButton.setText("Afficher grille");
				showGrilleButton.setMaximumSize(new Dimension(100, 32767));
				showGrilleButton.setPreferredSize(new Dimension(140, 22));
				showGrilleButton.setHorizontalTextPosition(SwingConstants.CENTER);
				showGrilleButton.setRequestFocusEnabled(false);
				showGrilleButton.addActionListener(e -> {
					if (!showGrilleButton.isSelected()) {
						showGrilleButton.setSelected(false);
						System.out.println("Grille retirer");
					}
					else {
						showGrilleButton.setSelected(true);
						System.out.println("Affichage de la grille");
					}
				});
				menuBar.add(showGrilleButton);

				//---- ajouter accessoire ----
				addAccessoireButton.setText("Ajouter Accessoire");
				addAccessoireButton.setMaximumSize(new Dimension(100, 32767));
				addAccessoireButton.setPreferredSize(new Dimension(140, 22));
				addAccessoireButton.setHorizontalTextPosition(SwingConstants.CENTER);
				addAccessoireButton.setRequestFocusEnabled(false);
				addAccessoireButton.setVisible(false);
				addAccessoireButton.addActionListener(e -> {
					if (!addAccessoireButton.isSelected()) {
						addAccessoireButton.setSelected(false);
						selectionAccessoireComboBox.setVisible(false);
					}
					else {
						addAccessoireButton.setSelected(true);
						selectionAccessoireComboBox.setVisible(true);
					}
				});
				menuBar.add(addAccessoireButton);

				//---- choix du type d'accessoire ----
				selectionAccessoireComboBox.setMaximumSize(new Dimension(100, 32767));
				selectionAccessoireComboBox.setPreferredSize(new Dimension(140, 22));
				selectionAccessoireComboBox.setRequestFocusEnabled(false);
				selectionAccessoireComboBox.setVisible(false);
				menuBar.add(selectionAccessoireComboBox);

				//---- changer vue ----
				changeVueButton.setText("Changer de vue");
				changeVueButton.setMaximumSize(new Dimension(100, 32767));
				changeVueButton.setPreferredSize(new Dimension(140, 22));
				changeVueButton.setHorizontalTextPosition(SwingConstants.CENTER);
				changeVueButton.setRequestFocusEnabled(false);
				changeVueButton.setVisible(false);
				changeVueButton.addActionListener(e -> {
					if (!changeVueButton.isSelected()) {
						changeVueButton.setSelected(false);
						System.out.println("Vue changé pour extérieur");
					}
					else {
						changeVueButton.setSelected(true);
						System.out.println("Vue changé pour intérieur");
					}
				});
				menuBar.add(changeVueButton);

				//---- retour vue dessus ----
				retourVueHautButton.setText("Retour vue dessus");
				retourVueHautButton.setMaximumSize(new Dimension(100, 32767));
				retourVueHautButton.setPreferredSize(new Dimension(140, 22));
				retourVueHautButton.setHorizontalTextPosition(SwingConstants.CENTER);
				retourVueHautButton.setRequestFocusEnabled(false);
				retourVueHautButton.setVisible(false);
				retourVueHautButton.addActionListener(e -> {
					changeVueButton.setSelected(false);
					addAccessoireButton.setVisible(false);
					retourVueHautButton.setVisible(false);
					changeVueButton.setVisible(false);
					murButton.setVisible(false);
					accessoireButton.setVisible(false);

					if (murSelected != -1)
						murButton.doClick();
					if (accessoireSelected != null)
						accessoireButton.doClick();
					if (voirPlanButton.isSelected())
						splitPaneH.setLeftComponent(drawingPanel);
					if (addAccessoireButton.isSelected())
						addAccessoireButton.doClick();

					coteButton.setVisible(true);
					for (int i = 0; i < 22; i++)
						parametresModifPanel.getComponent(i).setVisible(true);
					sepParam2.setVisible(true);
				});
				menuBar.add(retourVueHautButton);

				menuBar.add(Box.createHorizontalGlue());

				//---- undo ----
				undoButton.setPreferredSize(new Dimension(60, 22));
				undoButton.setMaximumSize(new Dimension(50, 32767));
				undoButton.setHorizontalTextPosition(SwingConstants.CENTER);
				undoButton.setRequestFocusEnabled(false);
				try {
					Image image = ImageIO.read(Objects.requireNonNull(getClass().getResource("/images/undoRev.png")));
					image = image.getScaledInstance( 40, 22,  Image.SCALE_SMOOTH ) ;
					undoButton.setIcon(new ImageIcon(image));
				} catch (Exception except) {
					except.printStackTrace();
				}
				undoButton.addActionListener(e -> System.out.println("undo effectué"));
				menuBar.add(undoButton);

				//---- redo ----
				redoButton.setPreferredSize(new Dimension(60, 22));
				redoButton.setMaximumSize(new Dimension(50, 32767));
				redoButton.setRequestFocusEnabled(false);
				redoButton.setHorizontalTextPosition(SwingConstants.CENTER);
				try {
					Image image = ImageIO.read(Objects.requireNonNull(getClass().getResource("/images/redoRev.png")));
					image = image.getScaledInstance( 40, 22,  Image.SCALE_SMOOTH ) ;
					redoButton.setIcon(new ImageIcon(image));
				} catch (Exception except) {
					except.printStackTrace();
				}
				redoButton.addActionListener(e -> System.out.println("redo effectué"));
				menuBar.add(redoButton);
			}
			this.setJMenuBar(menuBar);

			//======== splitPaneH ========
			{
				splitPaneH.setDividerSize(20);
				splitPaneH.setBorder(new EmptyBorder(20, 20, 20, 20));
				splitPaneH.setMinimumSize(new Dimension(1100, 500));
				splitPaneH.setResizeWeight(1.0);

				//======== Panneau de dessin ========
				{
					drawingPanel.setMinimumSize(new Dimension(800, 500));
					drawingPanel.setBackground(Color.white);
					drawingPanel.setLayout(new GridBagLayout());
					((GridBagLayout) drawingPanel.getLayout()).columnWidths = new int[] {0, 0};
					((GridBagLayout) drawingPanel.getLayout()).rowHeights = new int[] {0, 0};
					((GridBagLayout) drawingPanel.getLayout()).columnWeights = new double[] {0.0, 1.0E-4};
					((GridBagLayout) drawingPanel.getLayout()).rowWeights = new double[] {0.0, 1.0E-4};

					//temporaire (pour demo)
					{
						coteButton.setText("Vue de côté (demo)");
						coteButton.addActionListener(e -> {
							addAccessoireButton.setVisible(true);
							retourVueHautButton.setVisible(true);
							changeVueButton.setVisible(true);
							murButton.setVisible(true);
							accessoireButton.setVisible(true);

							coteSelected = 'S';
							coteButton.setVisible(false);
							for (int i = 0; i < 22; i++)
								parametresModifPanel.getComponent(i).setVisible(false);
							sepParam2.setVisible(false);
						});
						drawingPanel.add(coteButton, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
								GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

						murButton.setText("Selectionner un mur (demo)");
						murButton.setVisible(false);
						murButton.addActionListener(e -> {
							if (murButton.isSelected()){
								murSelected = 1;
								voirPlanButton.setVisible(true);
								sepConfig.setVisible(true);
								if (accessoireButton.isSelected())
									accessoireButton.doClick();
							}
							else {
								murSelected = -1;
								sepConfig.setVisible(false);
								voirPlanButton.setVisible(false);
							}
						});
						drawingPanel.add(murButton, new GridBagConstraints(0, 1, 2, 1, 0.0, 0.0,
								GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));

						accessoireButton.setText("Selectionner un accessoire (demo)");
						accessoireButton.setVisible(false);
						accessoireButton.addActionListener(e -> {
							if (accessoireButton.isSelected()){
								accessoireSelected = new Point(1,1);
								for (int i = 22; i < 28; i++)
									parametresModifPanel.getComponent(i).setVisible(true);
								sepParam2.setVisible(true);
								if (murButton.isSelected())
									murButton.doClick();
							}
							else {
								accessoireSelected = null;
								for (int i = 22; i < 28; i++)
									parametresModifPanel.getComponent(i).setVisible(false);
								sepParam2.setVisible(false);
							}
						});
						drawingPanel.add(accessoireButton, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0,
								GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
					}

					splitPaneH.setLeftComponent(drawingPanel);
				}

				//======== splitPaneV ========
				{
					splitPaneV.setOrientation(JSplitPane.VERTICAL_SPLIT);
					splitPaneV.setDividerSize(20);
					splitPaneV.setMinimumSize(new Dimension(340, 500));
					splitPaneV.setDividerLocation(440);
					splitPaneV.setMaximumSize(new Dimension(340, 2147483647));

					//======== panneau des paramètres ========
					{
						parametresPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
						parametresPanel.setMinimumSize(new Dimension(300, 440));
						parametresPanel.setMaximumSize(new Dimension(300, 32767));
						parametresPanel.setPreferredSize(new Dimension(300, 440));
						parametresPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 20));

						//---- parametreLabel ----
						parametreLabel.setText("Param\u00e8tres");
						parametreLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
						parametreLabel.setHorizontalAlignment(SwingConstants.CENTER);
						parametresPanel.add(parametreLabel);

						//======== parametresModifPanel ========
						{
							parametresModifPanel.setLayout(new GridBagLayout());
							((GridBagLayout) parametresModifPanel.getLayout()).columnWidths = new int[] {0, 0, 0, 0};
							((GridBagLayout) parametresModifPanel.getLayout()).rowHeights = new int[] {0, 0, 0, 0, 0, 0, 0};
							((GridBagLayout) parametresModifPanel.getLayout()).columnWeights = new double[] {1.0, 1.0, 1.0, 1.0E-4};
							((GridBagLayout) parametresModifPanel.getLayout()).rowWeights = new double[] {1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0E-4};

							//---- largSalleLabel ----
							largSalleLabel.setText("Largeur de la salle");
							parametresModifPanel.add(largSalleLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.BOTH,
									new Insets(0, 0, 10, 10), 0, 0));

							//---- largSalleTextField ----
							largSalleTextField.setColumns(5);
							parametresModifPanel.add(largSalleTextField, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.BOTH,
									new Insets(0, 0, 10, 10), 0, 0));
							largSalleTextField.addActionListener(e -> {
								if (!largSalleTextField.getText().isEmpty())
									System.out.println(largSalleLabel.getText() + " update à " + largSalleTextField.getText() + " " + largSalleEndLabel.getText());
							});

							//---- largSalleEndLabel ----
							largSalleEndLabel.setText("po");
							parametresModifPanel.add(largSalleEndLabel, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.BOTH,
									new Insets(0, 0, 10, 0), 0, 0));

							//---- longSalleLabel ----
							longSalleLabel.setText("Longueur de la salle");
							parametresModifPanel.add(longSalleLabel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.BOTH,
									new Insets(0, 0, 10, 10), 0, 0));

							//---- longSalleTextField ----
							longSalleTextField.setColumns(5);
							parametresModifPanel.add(longSalleTextField, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.BOTH,
									new Insets(0, 0, 10, 10), 0, 0));
							longSalleTextField.addActionListener(e -> {
								if (!longSalleTextField.getText().isEmpty())
									System.out.println(longSalleLabel.getText() + " update à " + longSalleTextField.getText() + " " + longSalleEndLabel.getText());
							});

							//---- longSalleEndLabel ----
							longSalleEndLabel.setText("po");
							parametresModifPanel.add(longSalleEndLabel, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.BOTH,
									new Insets(0, 0, 10, 0), 0, 0));

							//---- hSalleLabel ----
							hSalleLabel.setText("Hauteur de la salle");
							parametresModifPanel.add(hSalleLabel, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.BOTH,
									new Insets(0, 0, 10, 10), 0, 0));

							//---- hSalleTextField ----
							hSalleTextField.setColumns(5);
							parametresModifPanel.add(hSalleTextField, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.BOTH,
									new Insets(0, 0, 10, 10), 0, 0));
							hSalleTextField.addActionListener(e -> {
								if (!hSalleTextField.getText().isEmpty())
									System.out.println(hSalleLabel.getText() + " update à " + hSalleTextField.getText() + " " + hSalleEndLabel.getText());
							});

							//---- hSalleEndLabel ----
							hSalleEndLabel.setText("po");
							parametresModifPanel.add(hSalleEndLabel, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.BOTH,
									new Insets(0, 0, 10, 0), 0, 0));

							//---- epMursLabel ----
							epMursLabel.setText("\u00c9paisseur des murs de la salle");
							parametresModifPanel.add(epMursLabel, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.BOTH,
									new Insets(0, 0, 10, 10), 0, 0));

							//---- epMursTextField ----
							epMursTextField.setColumns(5);
							parametresModifPanel.add(epMursTextField, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.BOTH,
									new Insets(0, 0, 10, 10), 0, 0));
							epMursTextField.addActionListener(e -> {
								if (!epMursTextField.getText().isEmpty()) {
									System.out.println(epMursLabel.getText() + " update à " + epMursTextField.getText() + " " + epMursEndLabel.getText());
								}
							});

							//---- epMursEndLabel ----
							epMursEndLabel.setText("po");
							parametresModifPanel.add(epMursEndLabel, new GridBagConstraints(2, 3, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.BOTH,
									new Insets(0, 0, 10, 0), 0, 0));

							parametresModifPanel.add(sepParam1, new GridBagConstraints(0, 4, 3, 1, 0, 0,
									GridBagConstraints.CENTER, GridBagConstraints.BOTH,
									new Insets(20, 0, 20, 0), 0, 0));

							//---- epTrouLabel ----
							epTrouLabel.setText("\u00c9paisseur trou des retours d'air");
							parametresModifPanel.add(epTrouLabel, new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0,
								GridBagConstraints.CENTER, GridBagConstraints.BOTH,
								new Insets(0, 0, 10, 10), 0, 0));

							//---- epTrouTextField ----
							epTrouTextField.setColumns(5);
							parametresModifPanel.add(epTrouTextField, new GridBagConstraints(1, 5, 1, 1, 0.0, 0.0,
								GridBagConstraints.CENTER, GridBagConstraints.BOTH,
								new Insets(0, 0, 10, 10), 0, 0));
							epTrouTextField.addActionListener(e -> {
								if (!epTrouTextField.getText().isEmpty())
									System.out.println(epTrouLabel.getText() + " update à " + epTrouTextField.getText() + " " + epTrouEndLabel.getText());
							});

							//---- epTrouEndLabel ----
							epTrouEndLabel.setText("po");
							parametresModifPanel.add(epTrouEndLabel, new GridBagConstraints(2, 5, 1, 1, 0.0, 0.0,
								GridBagConstraints.CENTER, GridBagConstraints.BOTH,
								new Insets(0, 0, 10, 0), 0, 0));

							//---- hRetourAirLabel ----
							hRetourAirLabel.setText("Hauteur des retour d'air");
							parametresModifPanel.add(hRetourAirLabel, new GridBagConstraints(0, 6, 1, 1, 0.0, 0.0,
								GridBagConstraints.CENTER, GridBagConstraints.BOTH,
								new Insets(0, 0, 10, 10), 0, 0));

							//---- hRetourAirTextField ----
							hRetourAirTextField.setColumns(5);
							parametresModifPanel.add(hRetourAirTextField, new GridBagConstraints(1, 6, 1, 1, 0.0, 0.0,
								GridBagConstraints.CENTER, GridBagConstraints.BOTH,
								new Insets(0, 0, 10, 10), 0, 0));
							hRetourAirTextField.addActionListener(e -> {
								if (!hRetourAirTextField.getText().isEmpty())
									System.out.println(hRetourAirLabel.getText() + " update à " + hRetourAirTextField.getText() + " " + hRetourAirEndLabel.getText());
							});

							//---- hRetourAirEndLabel ----
							hRetourAirEndLabel.setText("po");
							parametresModifPanel.add(hRetourAirEndLabel, new GridBagConstraints(2, 6, 1, 1, 0.0, 0.0,
								GridBagConstraints.CENTER, GridBagConstraints.BOTH,
								new Insets(0, 0, 10, 0), 0, 0));

							//---- distSolRetourAirLabel ----
							distSolRetourAirLabel.setText("Distance sol des retours d'air");
							parametresModifPanel.add(distSolRetourAirLabel, new GridBagConstraints(0, 7, 1, 1, 0.0, 0.0,
								GridBagConstraints.CENTER, GridBagConstraints.BOTH,
								new Insets(0, 0, 10, 10), 0, 0));

							//---- distSolRetourAirTextField ----
							distSolRetourAirTextField.setColumns(5);
							parametresModifPanel.add(distSolRetourAirTextField, new GridBagConstraints(1, 7, 1, 1, 0.0, 0.0,
								GridBagConstraints.CENTER, GridBagConstraints.BOTH,
								new Insets(0, 0, 10, 10), 0, 0));
							distSolRetourAirTextField.addActionListener(e -> {
								if (!distSolRetourAirTextField.getText().isEmpty())
									System.out.println(distSolRetourAirLabel.getText() + " update à " + distSolRetourAirTextField.getText() + " " + distSolRetourAirEndLabel.getText());
							});

							//---- distSolRetourAirEndLabel ----
							distSolRetourAirEndLabel.setText("po");
							parametresModifPanel.add(distSolRetourAirEndLabel, new GridBagConstraints(2, 7, 1, 1, 0.0, 0.0,
								GridBagConstraints.CENTER, GridBagConstraints.BOTH,
								new Insets(0, 0, 10, 0), 0, 0));


							//---- largAccesLabel ----
							largAccesLabel.setText("Largeur de <type acessoire (demo)>");
							parametresModifPanel.add(largAccesLabel, new GridBagConstraints(0, 8, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.BOTH,
									new Insets(0, 0, 10, 10), 0, 0));

							//---- largAccesTextField ----
							largAccesTextField.setColumns(5);
							parametresModifPanel.add(largAccesTextField, new GridBagConstraints(1, 8, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.BOTH,
									new Insets(0, 0, 10, 10), 0, 0));
							largAccesTextField.addActionListener(e -> {
								if (!largAccesTextField.getText().isEmpty()) {
									System.out.println(largAccesLabel.getText() + " update à " + largAccesTextField.getText() + " " + largAccesEndLabel.getText());
								}
							});

							//---- largAccesEndLabel ----
							largAccesEndLabel.setText("po");
							parametresModifPanel.add(largAccesEndLabel, new GridBagConstraints(2, 8, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.BOTH,
									new Insets(0, 0, 10, 0), 0, 0));

							//---- hAccesLabel ----
							hAccesLabel.setText("Hauteur de <type acessoire (demo)>");
							parametresModifPanel.add(hAccesLabel, new GridBagConstraints(0, 9, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.BOTH,
									new Insets(0, 0, 10, 10), 0, 0));

							//---- hAccesTextField ----
							hAccesTextField.setColumns(5);
							parametresModifPanel.add(hAccesTextField, new GridBagConstraints(1, 9, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.BOTH,
									new Insets(0, 0, 10, 10), 0, 0));
							hAccesTextField.addActionListener(e -> {
								if (!hAccesTextField.getText().isEmpty()) {
									System.out.println(hAccesLabel.getText() + " update à " + hAccesTextField.getText() + " " + hAccesEndLabel.getText());
								}
							});

							//---- hAccesEndLabel ----
							hAccesEndLabel.setText("po");
							parametresModifPanel.add(hAccesEndLabel, new GridBagConstraints(2, 9, 1, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.BOTH,
									new Insets(0, 0, 0, 0), 0, 0));

							parametresModifPanel.add(sepParam2, new GridBagConstraints(0, 10, 3, 1, 0, 0,
								GridBagConstraints.CENTER, GridBagConstraints.BOTH,
								new Insets(20, 0, 20, 0), 0, 0));

							//---- distGrilleLabel ----
							distGrilleLabel.setText("Distance des lignes de la grille");
							parametresModifPanel.add(distGrilleLabel, new GridBagConstraints(0, 11, 1, 1, 0.0, 0.0,
								GridBagConstraints.CENTER, GridBagConstraints.BOTH,
								new Insets(0, 0, 0, 10), 0, 0));

							//---- distGrilleTextField ----
							distGrilleTextField.setColumns(5);
							parametresModifPanel.add(distGrilleTextField, new GridBagConstraints(1, 11, 1, 1, 0.0, 0.0,
								GridBagConstraints.CENTER, GridBagConstraints.BOTH,
								new Insets(0, 0, 0, 10), 0, 0));
							distGrilleTextField.addActionListener(e -> {
								if (!distGrilleTextField.getText().isEmpty()) {
									System.out.println(distGrilleLabel.getText() + " update à " + distGrilleTextField.getText() + " " + distGrilleEndLabel.getText());
								}
							});

							//---- distGrilleEndLabel ----
							distGrilleEndLabel.setText("po");
							parametresModifPanel.add(distGrilleEndLabel, new GridBagConstraints(2, 11, 1, 1, 0.0, 1.0,
								GridBagConstraints.CENTER, GridBagConstraints.BOTH,
								new Insets(0, 0, 0, 0), 0, 0));
						}
						for (int i = 22; i < 28; i++)
							parametresModifPanel.getComponent(i).setVisible(false);
						parametresPanel.add(parametresModifPanel);
					}
					splitPaneV.setTopComponent(parametresPanel);

					//======== panneau de configuration ========
					{
						configurationPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
						configurationPanel.setMinimumSize(new Dimension(300, 260));
						configurationPanel.setMaximumSize(new Dimension(300, 32767));
						configurationPanel.setPreferredSize(new Dimension(300, 200));
						configurationPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 20));

						//---- configurationLabel ----
						configurationLabel.setText("Configuration plan");
						configurationLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
						configurationLabel.setHorizontalAlignment(SwingConstants.CENTER);
						configurationPanel.add(configurationLabel);

						//======== configurationPlanPanel ========
						{
							configurationPlanPanel.setLayout(new GridBagLayout());
							((GridBagLayout) configurationPlanPanel.getLayout()).columnWidths = new int[] {0, 0, 0, 0};
							((GridBagLayout) configurationPlanPanel.getLayout()).rowHeights = new int[] {0, 0, 0, 0, 0};
							((GridBagLayout) configurationPlanPanel.getLayout()).columnWeights = new double[] {0.01, 0.01, 0.01, 1.0E-4};
							((GridBagLayout) configurationPlanPanel.getLayout()).rowWeights = new double[] {0.01, 0.01, 0.01, 0.01, 1.0E-4};

							//---- longPlisLabel ----
							longPlisLabel.setText("Longueur des plis");
							configurationPlanPanel.add(longPlisLabel, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
								GridBagConstraints.WEST, GridBagConstraints.NONE,
								new Insets(0, 0, 10, 10), 0, 0));

							//---- longPlisTextField ----
							longPlisTextField.setColumns(5);
							configurationPlanPanel.add(longPlisTextField, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
								GridBagConstraints.CENTER, GridBagConstraints.BOTH,
								new Insets(0, 0, 10, 10), 0, 0));
							longPlisTextField.addActionListener(e -> {
								if (!longPlisTextField.getText().isEmpty())
									System.out.println(longPlisLabel.getText() + " update à " + longPlisTextField.getText() + " " + longPlisEndLabel.getText());
							});

							//---- longPlisEndLabel ----
							longPlisEndLabel.setText("po");
							configurationPlanPanel.add(longPlisEndLabel, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
								GridBagConstraints.CENTER, GridBagConstraints.NONE,
								new Insets(0, 0, 10, 0), 0, 0));

							//---- margeEpLabel ----
							margeEpLabel.setText("Marge \u00e9paisseur des mat\u00e9riaux  ");
							configurationPlanPanel.add(margeEpLabel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
								GridBagConstraints.WEST, GridBagConstraints.NONE,
								new Insets(0, 0, 10, 10), 0, 0));

							//---- margeEpTextField ----
							margeEpTextField.setColumns(5);
							configurationPlanPanel.add(margeEpTextField, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
								GridBagConstraints.CENTER, GridBagConstraints.BOTH,
								new Insets(0, 0, 10, 10), 0, 0));
							margeEpTextField.addActionListener(e -> {
								if (!margeEpTextField.getText().isEmpty())
									System.out.println(margeEpLabel.getText() + " update à " + margeEpTextField.getText() + " " + margeEpEndLabel.getText());
							});

							//---- margeEpEndLabel ----
							margeEpEndLabel.setText("po");
							configurationPlanPanel.add(margeEpEndLabel, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0,
								GridBagConstraints.CENTER, GridBagConstraints.NONE,
								new Insets(0, 0, 10, 0), 0, 0));

							//---- margeLargLabel ----
							margeLargLabel.setText("Marge largeur des replis");
							configurationPlanPanel.add(margeLargLabel, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
								GridBagConstraints.WEST, GridBagConstraints.NONE,
								new Insets(0, 0, 10, 10), 0, 0));

							//---- margeLargTextField ----
							margeLargTextField.setColumns(5);
							configurationPlanPanel.add(margeLargTextField, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0,
								GridBagConstraints.CENTER, GridBagConstraints.BOTH,
								new Insets(0, 0, 10, 10), 0, 0));
							margeLargTextField.addActionListener(e -> {
								if (!margeLargTextField.getText().isEmpty())
									System.out.println(margeLargLabel.getText() + " update à " + margeLargTextField.getText() + " " + margeLargEndLabel.getText());
							});

							//---- margeLargEndLabel ----
							margeLargEndLabel.setText("po");
							configurationPlanPanel.add(margeLargEndLabel, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0,
								GridBagConstraints.CENTER, GridBagConstraints.NONE,
								new Insets(0, 0, 10, 0), 0, 0));

							//---- anglePlisLabel ----
							anglePlisLabel.setText("Angle des plis");
							configurationPlanPanel.add(anglePlisLabel, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0,
								GridBagConstraints.WEST, GridBagConstraints.NONE,
								new Insets(0, 0, 0, 10), 0, 0));

							//---- anglePlisTextField ----
							anglePlisTextField.setColumns(5);
							configurationPlanPanel.add(anglePlisTextField, new GridBagConstraints(1, 3, 1, 1, 0.0, 0.0,
								GridBagConstraints.CENTER, GridBagConstraints.BOTH,
								new Insets(0, 0, 0, 10), 0, 0));
							anglePlisTextField.addActionListener(e -> {
								if (!anglePlisTextField.getText().isEmpty())
									System.out.println(anglePlisLabel.getText() + " update à " + anglePlisTextField.getText() + " " + anglePlisEndLabel.getText());
							});

							//---- anglePlisEndLabel ----
							anglePlisEndLabel.setText("deg");
							configurationPlanPanel.add(anglePlisEndLabel, new GridBagConstraints(2, 3, 1, 1, 0.0, 0.0,
								GridBagConstraints.CENTER, GridBagConstraints.NONE,
								new Insets(0, 0, 0, 0), 0, 0));

							sepConfig.setVisible(false);
							configurationPlanPanel.add(sepConfig, new GridBagConstraints(0, 4, 3, 1, 0, 0,
									GridBagConstraints.CENTER, GridBagConstraints.BOTH,
									new Insets(20, 0, 20, 0), 0, 0));

							//---- voir preview plan ----
							voirPlanButton.setText("Voir Plan");
							voirPlanButton.setPreferredSize(new Dimension(160, 22));
							voirPlanButton.setHorizontalTextPosition(SwingConstants.CENTER);
							voirPlanButton.setRequestFocusEnabled(false);
							voirPlanButton.setVisible(false);
							voirPlanButton.addActionListener(e -> {
								if (voirPlanButton.isSelected()){
									JLabel plan = new JLabel("Ceci est un plan (demo)", JLabel.CENTER);
									plan.setBackground(Color.WHITE);
									plan.setOpaque(true);
									splitPaneH.setLeftComponent(plan);
								}
								else splitPaneH.setLeftComponent(drawingPanel);
							});
							configurationPlanPanel.add(voirPlanButton, new GridBagConstraints(0, 5, 3, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.NONE,
									new Insets(0, 0, 0, 0), 0, 0));
						}
						configurationPanel.add(configurationPlanPanel);
					}
					splitPaneV.setBottomComponent(configurationPanel);
				}
				splitPaneH.setRightComponent(splitPaneV);
			}
			this.getContentPane().add(splitPaneH);
			this.setSize(1600, 900);
			this.setLocationRelativeTo(this.getOwner());
		}
	}
}
