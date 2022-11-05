package muracle.vue;

import muracle.domaine.MuracleController;
import muracle.utilitaire.FractionError;
import muracle.utilitaire.PouceError;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.Objects;


/**
 * @author Jerome Levesque
 */
public class MainWindow extends JFrame {

	private char coteSelected = ' ';
	private int murSelected = -1;
	private Point accessoireSelected = null;

	protected MuracleController controller = new MuracleController();

	public MainWindow() throws FractionError, PouceError {
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
		DrawingPanel drawingPanel = new DrawingPanel(this);

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
				openProjectButton.addActionListener(e -> controller.ouvrirProjet(this));
				menuBar.add(openProjectButton);

				//---- sauvergarder projet ----
				saveProjectButton.setText("Sauvegarder projet");
				saveProjectButton.setMaximumSize(new Dimension(120, 32767));
				saveProjectButton.setPreferredSize(new Dimension(160, 22));
				saveProjectButton.setHorizontalTextPosition(SwingConstants.CENTER);
				saveProjectButton.setRequestFocusEnabled(false);
				saveProjectButton.addActionListener(e -> controller.sauvegarderProjet(this));
				menuBar.add(saveProjectButton);

				//---- exporter plan ----
				exportButton.setText("Exporter plans");
				exportButton.setPreferredSize(new Dimension(140, 22));
				exportButton.setMaximumSize(new Dimension(100, 32767));
				exportButton.setHorizontalTextPosition(SwingConstants.CENTER);
				exportButton.setRequestFocusEnabled(false);
				exportButton.addActionListener(e -> controller.exporterPlan(this));
				menuBar.add(exportButton);

				//---- afficher grille ----
				showGrilleButton.setText("Afficher grille");
				showGrilleButton.setMaximumSize(new Dimension(100, 32767));
				showGrilleButton.setPreferredSize(new Dimension(140, 22));
				showGrilleButton.setHorizontalTextPosition(SwingConstants.CENTER);
				showGrilleButton.setRequestFocusEnabled(false);
				showGrilleButton.addActionListener(e -> {
					showGrilleButton.setSelected(showGrilleButton.isSelected());
					controller.reverseIsGrilleShown();
					drawingPanel.repaint();
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

					controller.selectCote(' ');
					drawingPanel.repaint();
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
					drawingPanel.setBorder(BorderFactory.createLineBorder(Color.black, 4));
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

							controller.selectCote('N');
							coteButton.setVisible(false);
							for (int i = 0; i < 22; i++)
								parametresModifPanel.getComponent(i).setVisible(false);
							sepParam2.setVisible(false);

							drawingPanel.repaint();
						});
						drawingPanel.add(coteButton, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0,
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
						/*drawingPanel.add(murButton, new GridBagConstraints(0, 1, 2, 1, 0.0, 0.0,
								GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
*/
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
						/*drawingPanel.add(accessoireButton, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0,
								GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));*/
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
							largSalleTextField.setText(controller.getSalle().getLargeur().toString());
							largSalleTextField.addActionListener(e -> {
								controller.setDimensionSalle(largSalleTextField.getText(), longSalleTextField.getText(),
										hSalleTextField.getText(), epMursTextField.getText());
								largSalleTextField.setText(controller.getSalle().getLargeur().toString());
								drawingPanel.repaint();
							});
							largSalleTextField.addFocusListener(new FocusAdapter() {
								public void focusLost(FocusEvent e) {
									largSalleTextField.postActionEvent();
								}
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
							longSalleTextField.setText(controller.getSalle().getLongueur().toString());
							longSalleTextField.addActionListener(e -> {
								controller.setDimensionSalle(largSalleTextField.getText(), longSalleTextField.getText(),
										hSalleTextField.getText(), epMursTextField.getText());
								longSalleTextField.setText(controller.getSalle().getLongueur().toString());
								drawingPanel.repaint();
							});
							longSalleTextField.addFocusListener(new FocusAdapter() {
								public void focusLost(FocusEvent e) {
									longSalleTextField.postActionEvent();
								}
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
							hSalleTextField.setText(controller.getSalle().getHauteur().toString());
							hSalleTextField.addActionListener(e -> {
								controller.setDimensionSalle(largSalleTextField.getText(), longSalleTextField.getText(),
										hSalleTextField.getText(), epMursTextField.getText());
								hSalleTextField.setText(controller.getSalle().getHauteur().toString());
								drawingPanel.repaint();
							});
							hSalleTextField.addFocusListener(new FocusAdapter() {
								public void focusLost(FocusEvent e) {
									hSalleTextField.postActionEvent();
								}
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
							epMursTextField.setText(controller.getSalle().getProfondeur().toString());
							epMursTextField.addActionListener(e -> {
								controller.setDimensionSalle(largSalleTextField.getText(), longSalleTextField.getText(),
										hSalleTextField.getText(), epMursTextField.getText());
								epMursTextField.setText(controller.getSalle().getProfondeur().toString());
								drawingPanel.repaint();
							});
							epMursTextField.addFocusListener(new FocusAdapter() {
								public void focusLost(FocusEvent e) {
									epMursTextField.postActionEvent();
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
							epTrouTextField.setText(controller.getSalle().getEpaisseurTrouRetourAir().toString());
							epTrouTextField.addActionListener(e -> {
								controller.setParametreRetourAir(hRetourAirTextField.getText(),
										epTrouTextField.getText(), distSolRetourAirTextField.getText());
								epTrouTextField.setText(controller.getSalle().getEpaisseurTrouRetourAir().toString());
								drawingPanel.repaint();
							});
							epTrouTextField.addFocusListener(new FocusAdapter() {
								public void focusLost(FocusEvent e) {
									epTrouTextField.postActionEvent();
								}
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
							hRetourAirTextField.setText(controller.getSalle().getHauteurRetourAir().toString());
							hRetourAirTextField.addActionListener(e -> {
								controller.setParametreRetourAir(hRetourAirTextField.getText(),
										epTrouTextField.getText(), distSolRetourAirTextField.getText());
								hRetourAirTextField.setText(controller.getSalle().getHauteurRetourAir().toString());
							});
							hRetourAirTextField.addFocusListener(new FocusAdapter() {
								public void focusLost(FocusEvent e) {
									hRetourAirTextField.postActionEvent();
								}
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
							distSolRetourAirTextField.setText(controller.getSalle().getDistanceTrouRetourAir().toString());
							distSolRetourAirTextField.addActionListener(e -> {
								controller.setParametreRetourAir(hRetourAirTextField.getText(),
										epTrouTextField.getText(), distSolRetourAirTextField.getText());
								distSolRetourAirTextField.setText(controller.getSalle().getDistanceTrouRetourAir().toString());
							});
							distSolRetourAirTextField.addFocusListener(new FocusAdapter() {
								public void focusLost(FocusEvent e) {
									distSolRetourAirTextField.postActionEvent();
								}
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
							distGrilleTextField.setText(controller.getDistLigneGrille().toString());
							distGrilleTextField.addActionListener(e -> {
								controller.setDistLigneGrille(distGrilleTextField.getText());
								distGrilleTextField.setText(controller.getDistLigneGrille().toString());
								drawingPanel.repaint();
							});
							distGrilleTextField.addFocusListener(new FocusAdapter() {
								public void focusLost(FocusEvent e) {
									distGrilleTextField.postActionEvent();
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
							longPlisTextField.setText(controller.getParametrePlan(0));
							longPlisTextField.addActionListener(e -> {
								controller.setParametrePlan(margeEpTextField.getText(), margeLargTextField.getText(),
										anglePlisTextField.getText(), longPlisTextField.getText());
								longPlisTextField.setText(controller.getParametrePlan(0));
							});
							longPlisTextField.addFocusListener(new FocusAdapter() {
								public void focusLost(FocusEvent e) {
									longPlisTextField.postActionEvent();
								}
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
							margeEpTextField.setText(controller.getParametrePlan(1));
							margeEpTextField.addActionListener(e -> {
								controller.setParametrePlan(margeEpTextField.getText(), margeLargTextField.getText(),
										anglePlisTextField.getText(), longPlisTextField.getText());
								margeEpTextField.setText(controller.getParametrePlan(1));
							});
							margeEpTextField.addFocusListener(new FocusAdapter() {
								public void focusLost(FocusEvent e) {
									margeEpTextField.postActionEvent();
								}
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
							margeLargTextField.setText(controller.getParametrePlan(2));
							margeLargTextField.addActionListener(e -> {
								controller.setParametrePlan(margeEpTextField.getText(), margeLargTextField.getText(),
										anglePlisTextField.getText(), longPlisTextField.getText());
								margeLargTextField.setText(controller.getParametrePlan(2));
							});
							margeLargTextField.addFocusListener(new FocusAdapter() {
								public void focusLost(FocusEvent e) {
									margeLargTextField.postActionEvent();
								}
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
							anglePlisTextField.setText(controller.getParametrePlan(3));
							anglePlisTextField.addActionListener(e -> {
								controller.setParametrePlan(margeEpTextField.getText(), margeLargTextField.getText(),
										anglePlisTextField.getText(), longPlisTextField.getText());
								anglePlisTextField.setText(controller.getParametrePlan(3));
							});
							anglePlisTextField.addFocusListener(new FocusAdapter() {
								public void focusLost(FocusEvent e) {
									anglePlisTextField.postActionEvent();
								}
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
