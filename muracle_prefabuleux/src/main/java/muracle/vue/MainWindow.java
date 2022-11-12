package muracle.vue;

import com.formdev.flatlaf.intellijthemes.FlatCyanLightIJTheme;
import com.formdev.flatlaf.intellijthemes.FlatNordIJTheme;
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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.Objects;


/**
 * @author Jerome Levesque
 */
public class MainWindow extends JFrame {

	interface Updater {
		void updateTextFields();
		void updateButtons();
		void updateParamsShown();
	}
	private int murSelected = -1;
	private Point accessoireSelected = null;

	protected MuracleController controller = new MuracleController();
	protected boolean isDarkMode = true;

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
		JToggleButton addSeparateurButton = new JToggleButton();
		JToggleButton addAccessoireButton = new JToggleButton();
		JComboBox<String> selectionAccessoireComboBox = new JComboBox<>(new String[] {"Fenêtre", "Porte", "Prise électrique", "Retour d'air"});
		JButton retourVueHautButton = new JButton();
		JToggleButton changeVueButton = new JToggleButton();
		JButton lookButton = new JButton();
		JButton undoButton = new JButton();
		JButton redoButton = new JButton();

		JSplitPane splitPaneH = new JSplitPane();
		JSplitPane splitPaneV = new JSplitPane();

		//Panneau de dessin
		DrawingPanel drawingPanel = new DrawingPanel(this);
		drawingPanel.updateParametre();

		//seulement pour demo
		JButton coteButton = new JButton();
		JToggleButton murButton = new JToggleButton();
		JToggleButton accessoireButton = new JToggleButton();

		//panneau des parametres
		JPanel parametresPanel = new JPanel();
		JLabel parametreLabel = new JLabel();

		//parametre modifiable
		JPanel parametresModifPanel = new JPanel();

		//params salle
		JTextField largSalleTextField = new JTextField();
		JTextField longSalleTextField = new JTextField();
		JTextField hSalleTextField = new JTextField();
		JTextField epMursTextField = new JTextField();

		JSeparator sepParam1 = new JSeparator();

		// params retours d'air
		JTextField epTrouTextField = new JTextField();
		JTextField hRetourAirTextField = new JTextField();
		JTextField distSolRetourAirTextField = new JTextField();

		// params separateurs
		JTextField posSepTextField = new JTextField();

		// params accessoires
		JTextField posXAccesTextField = new JTextField();
		JTextField posYAccesTextField = new JTextField();
		JTextField largAccesTextField = new JTextField();
		JTextField hAccesTextField = new JTextField();
		JTextField margeAccesTextField = new JTextField();

		JSeparator sepParam2 = new JSeparator();

		// param grille
		JTextField distGrilleTextField = new JTextField();

		JButton deleteButton = new JButton();


		//panneau de configuration
		JPanel configurationPanel = new JPanel();
		JLabel configurationLabel = new JLabel();
		JPanel configurationPlanPanel = new JPanel();

		// configs
		JTextField longPlisTextField = new JTextField();
		JTextField margeEpTextField = new JTextField();
		JTextField margeLargTextField = new JTextField();
		JTextField anglePlisTextField = new JTextField();

		JSeparator sepConfig = new JSeparator();

		JToggleButton voirPlanButton = new JToggleButton();

		Updater updater = new Updater() {
			@Override
			public void updateTextFields() {
				largSalleTextField.setText(controller.getDimensionSalle(0));
				longSalleTextField.setText(controller.getDimensionSalle(1));
				hSalleTextField.setText(controller.getDimensionSalle(2));
				epMursTextField.setText(controller.getDimensionSalle(3));
				hRetourAirTextField.setText(controller.getParametreRetourAir(0));
				epTrouTextField.setText(controller.getParametreRetourAir(1));
				distSolRetourAirTextField.setText(controller.getParametreRetourAir(2));
				distGrilleTextField.setText(controller.getDistLigneGrille().toString());
				longPlisTextField.setText(controller.getParametrePlan(0));
				margeEpTextField.setText(controller.getParametrePlan(1));
				margeLargTextField.setText(controller.getParametrePlan(2));
				anglePlisTextField.setText(controller.getParametrePlan(3));
				if (controller.isSeparateurSelected())
					posSepTextField.setText(controller.getSelectedSeparateur().toString());
			}

			@Override
			public void updateButtons() {
				if (controller.isVueDessus() && retourVueHautButton.isVisible())
					retourVueHautButton.doClick();
				if (controller.isVueCote() && !retourVueHautButton.isVisible()) {
					addAccessoireButton.setVisible(true);
					retourVueHautButton.setVisible(true);
					changeVueButton.setVisible(true);
					murButton.setVisible(true);
					accessoireButton.setVisible(true);
					coteButton.setVisible(false);
				}
				changeVueButton.setSelected(!controller.isVueExterieur());
			}

			@Override
			public void updateParamsShown() {
				for (int i = 0; i < parametresModifPanel.getComponentCount(); i++)
					parametresModifPanel.getComponent(i).setVisible(true);
				if (controller.isVueDessus()) {
					if (controller.isSeparateurSelected()) {
						for (int i = 0; i < 22; i++)
							parametresModifPanel.getComponent(i).setVisible(false);
						for (int i = 25; i < parametresModifPanel.getComponentCount() - 4; i++)
							parametresModifPanel.getComponent(i).setVisible(false);
						deleteButton.setVisible(true);
					}
					else
						for (int i = 22; i < parametresModifPanel.getComponentCount() - 4; i++)
							parametresModifPanel.getComponent(i).setVisible(false);
				}
				else {
					for (int i = 0; i < 22; i++)
						parametresModifPanel.getComponent(i).setVisible(false);
					if (controller.isSeparateurSelected()) {
						for (int i = 25; i < parametresModifPanel.getComponentCount() - 4; i++)
							parametresModifPanel.getComponent(i).setVisible(false);
						deleteButton.setVisible(true);
					}
					else if (controller.isAccessoireSelected()) {
						for (int i = 22; i < 25; i++)
							parametresModifPanel.getComponent(i).setVisible(false);
						deleteButton.setVisible(true);
					}
					else {
						for (int i = 22; i < parametresModifPanel.getComponentCount() - 3; i++)
							parametresModifPanel.getComponent(i).setVisible(false);
						if (controller.isMurSelected()) {
							sepConfig.setVisible(true);
							voirPlanButton.setVisible(true);
						}
						else {
							sepConfig.setVisible(false);
							voirPlanButton.setVisible(false);
						}
					}
				}
			}
		};

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
					controller.ouvrirProjet(this);
					updater.updateTextFields();
					updater.updateButtons();
					updater.updateParamsShown();
					drawingPanel.repaint();
				});
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

				//---- ajouterSeparateur grille ----
				addSeparateurButton.setText("Ajouter séparateur");
				addSeparateurButton.setMaximumSize(new Dimension(100, 32767));
				addSeparateurButton.setPreferredSize(new Dimension(140, 22));
				addSeparateurButton.setHorizontalTextPosition(SwingConstants.CENTER);
				addSeparateurButton.setRequestFocusEnabled(false);
				addSeparateurButton.addActionListener(e -> {
					addSeparateurButton.setSelected(addSeparateurButton.isSelected());
					// do something
				});
				menuBar.add(addSeparateurButton);

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
					changeVueButton.setSelected(changeVueButton.isSelected());
					controller.setIsVueExterieur(!changeVueButton.isSelected());
					repaint();
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
					if (changeVueButton.isSelected())
						changeVueButton.doClick();
					addAccessoireButton.setVisible(false);
					retourVueHautButton.setVisible(false);
					changeVueButton.setVisible(false);
					murButton.setVisible(false);
					accessoireButton.setVisible(false);


					if (voirPlanButton.isSelected())
						voirPlanButton.doClick();
					if (addAccessoireButton.isSelected())
						addAccessoireButton.doClick();

					coteButton.setVisible(true);
					controller.setIsVueDessus(true);
					updater.updateParamsShown();
					drawingPanel.updateParametre();
					drawingPanel.repaint();
				});
				menuBar.add(retourVueHautButton);

				menuBar.add(Box.createHorizontalGlue());

				//---- changer look ----
				lookButton.setPreferredSize(new Dimension(30, 22));
				lookButton.setMaximumSize(new Dimension(30, 32767));
				lookButton.setHorizontalTextPosition(SwingConstants.CENTER);
				lookButton.setRequestFocusEnabled(false);
				try {
					Image image = ImageIO.read(Objects.requireNonNull(getClass().getResource("/images/moon.png")));
					image = image.getScaledInstance( 16, 16,  Image.SCALE_SMOOTH ) ;
					lookButton.setIcon(new ImageIcon(image));
				} catch (Exception except) {
					except.printStackTrace();
				}
				lookButton.addActionListener(e -> {
					isDarkMode = !isDarkMode;
					try {
						String rev = "rev";
						String mode = "moon";
						if (!isDarkMode) {
							UIManager.setLookAndFeel(new FlatCyanLightIJTheme());
							rev = "";
							mode = "sun";
						}
						else
							UIManager.setLookAndFeel(new FlatNordIJTheme());
						SwingUtilities.updateComponentTreeUI(this);
						Image image = ImageIO.read(Objects.requireNonNull(getClass().getResource("/images/" + mode + ".png")));
						image = image.getScaledInstance( 16, 16,  Image.SCALE_SMOOTH ) ;
						lookButton.setIcon(new ImageIcon(image));
						image = ImageIO.read(Objects.requireNonNull(getClass().getResource("/images/undo" + rev + ".png")));
						image = image.getScaledInstance( 40, 22,  Image.SCALE_SMOOTH ) ;
						undoButton.setIcon(new ImageIcon(image));
						image = ImageIO.read(Objects.requireNonNull(getClass().getResource("/images/redo" + rev + ".png")));
						image = image.getScaledInstance( 40, 22,  Image.SCALE_SMOOTH ) ;
						redoButton.setIcon(new ImageIcon(image));
					}catch (Exception ignored){}
				});
				menuBar.add(lookButton);

				//---- undo ----
				undoButton.setPreferredSize(new Dimension(50, 22));
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
				undoButton.addActionListener(e -> {
					try {
						controller.undoChange();
						updater.updateTextFields();
						updater.updateButtons();
						updater.updateParamsShown();
						drawingPanel.updateParametre();
						drawingPanel.repaint();
					} catch (IOException | ClassNotFoundException ex) {
						throw new RuntimeException(ex);
					}
				});
				menuBar.add(undoButton);

				//---- redo ----
				redoButton.setPreferredSize(new Dimension(50, 22));
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
				redoButton.addActionListener(e -> {
					try {
						controller.redoChange();
						updater.updateTextFields();
						updater.updateButtons();
						updater.updateParamsShown();
						drawingPanel.updateParametre();
						drawingPanel.repaint();
					} catch (IOException | ClassNotFoundException ex) {
						throw new RuntimeException(ex);
					}
				});
				menuBar.add(redoButton);
			}
			this.setJMenuBar(menuBar);

			//======== splitPaneH ========
			{
				splitPaneH.setDividerSize(20);
				splitPaneH.setBorder(new EmptyBorder(20, 20, 20, 20));
				splitPaneH.setMinimumSize(new Dimension(1100, 500));
				splitPaneH.setResizeWeight(1.0);
				splitPaneH.setEnabled(true);

				//======== Panneau de dessin ========
				{
					drawingPanel.setMinimumSize(new Dimension(800, 500));
					drawingPanel.setBorder(BorderFactory.createLineBorder(Color.black, 2));
					drawingPanel.setLayout(new GridBagLayout());
					((GridBagLayout) drawingPanel.getLayout()).columnWidths = new int[] {0, 0};
					((GridBagLayout) drawingPanel.getLayout()).rowHeights = new int[] {0, 0};
					((GridBagLayout) drawingPanel.getLayout()).columnWeights = new double[] {0.0, 1.0E-4};
					((GridBagLayout) drawingPanel.getLayout()).rowWeights = new double[] {0.0, 1.0E-4};

					drawingPanel.addMouseListener(new MouseListener() {
						@Override
						public void mouseClicked(MouseEvent e) {
							boolean token = controller.isVueDessus();
							System.out.println(drawingPanel.coordPixelToPouce(e));
							controller.interactComponent(drawingPanel.coordPixelToPouce(e),
									addSeparateurButton.isSelected(), addAccessoireButton.isSelected());
							updater.updateParamsShown();
							updater.updateButtons();
							updater.updateTextFields();
							if (token != controller.isVueDessus())
								drawingPanel.updateParametre();
							drawingPanel.repaint();
						}

						@Override
						public void mousePressed(MouseEvent e) {

						}

						@Override
						public void mouseReleased(MouseEvent e) {

						}

						@Override
						public void mouseEntered(MouseEvent e) {

						}

						@Override
						public void mouseExited(MouseEvent e) {

						}
					});

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

							int posY = 0;

							//---- largSalleParam ----
							addParams(parametresModifPanel, "Largeur de la salle", largSalleTextField, "po", posY++);
							largSalleTextField.addActionListener(e -> {
								controller.setDimensionSalle(largSalleTextField.getText(), longSalleTextField.getText(),
										hSalleTextField.getText(), epMursTextField.getText());
								largSalleTextField.setText(controller.getDimensionSalle(0));
								drawingPanel.repaint();
							});

							//---- longSalleParam ----
							addParams(parametresModifPanel, "Longueur de la salle", longSalleTextField, "po", posY++);
							longSalleTextField.addActionListener(e -> {
								controller.setDimensionSalle(largSalleTextField.getText(), longSalleTextField.getText(),
										hSalleTextField.getText(), epMursTextField.getText());
								longSalleTextField.setText(controller.getDimensionSalle(1));
								drawingPanel.repaint();
							});

							//---- hSalleParam ----
							addParams(parametresModifPanel, "Hauteur de la salle", hSalleTextField, "po", posY++);
							hSalleTextField.addActionListener(e -> {
								controller.setDimensionSalle(largSalleTextField.getText(), longSalleTextField.getText(),
										hSalleTextField.getText(), epMursTextField.getText());
								hSalleTextField.setText(controller.getDimensionSalle(2));
								drawingPanel.repaint();
							});

							//---- epMursParam ----
							addParams(parametresModifPanel, "\u00c9paisseur des murs de la salle", epMursTextField, "po", posY++);
							epMursTextField.addActionListener(e -> {
								controller.setDimensionSalle(largSalleTextField.getText(), longSalleTextField.getText(),
										hSalleTextField.getText(), epMursTextField.getText());
								epMursTextField.setText(controller.getDimensionSalle(3));
								drawingPanel.repaint();
							});

							//separateur
							parametresModifPanel.add(sepParam1, new GridBagConstraints(0, posY++, 3, 1, 0, 0,
									GridBagConstraints.CENTER, GridBagConstraints.BOTH,
									new Insets(20, 0, 20, 0), 0, 0));

							//---- epTrouParam ----
							addParams(parametresModifPanel, "\u00c9paisseur trou des retours d'air", epTrouTextField, "po", posY++);
							epTrouTextField.addActionListener(e -> {
								controller.setParametreRetourAir(hRetourAirTextField.getText(),
										epTrouTextField.getText(), distSolRetourAirTextField.getText());
								epTrouTextField.setText(controller.getSalle().getEpaisseurTrouRetourAir().toString());
								drawingPanel.repaint();
							});

							//---- hRetourAirParam ----
							addParams(parametresModifPanel, "Hauteur des retours d'air", hRetourAirTextField, "po", posY++);
							hRetourAirTextField.addActionListener(e -> {
								controller.setParametreRetourAir(hRetourAirTextField.getText(),
										epTrouTextField.getText(), distSolRetourAirTextField.getText());
								hRetourAirTextField.setText(controller.getSalle().getHauteurRetourAir().toString());
								drawingPanel.repaint();
							});

							//---- distSolRetourAirParam ----
							addParams(parametresModifPanel, "Distance sol des retours d'air", distSolRetourAirTextField, "po", posY++);
							distSolRetourAirTextField.addActionListener(e -> {
								controller.setParametreRetourAir(hRetourAirTextField.getText(),
										epTrouTextField.getText(), distSolRetourAirTextField.getText());
								distSolRetourAirTextField.setText(controller.getSalle().getDistanceTrouRetourAir().toString());
								drawingPanel.repaint();
							});

							//---- params separateurs ----
							addParams(parametresModifPanel, "Position du Séparateur", posSepTextField, "po", posY++);
							posSepTextField.addActionListener(e -> {
								if (controller.isSeparateurSelected()) {
									controller.moveSeparateur(posSepTextField.getText());
									posSepTextField.setText(controller.getSelectedSeparateur().toString());
									drawingPanel.repaint();
								}
							});

							// params accessoires
							//---- posXAccesParam ----
							addParams(parametresModifPanel, "Position X de <type acessoire (demo)>", posXAccesTextField, "po", posY++);
							posXAccesTextField.addActionListener(e -> {
								controller.moveAccessoire(posXAccesTextField.getText(), posYAccesTextField.getText());
								posXAccesTextField.setText(controller.getSelectedAccessoire().getPosition().getX().toString());
								drawingPanel.repaint();
							});

							//---- posYAccesParam ----
							addParams(parametresModifPanel, "Position Y de <type acessoire (demo)>", posYAccesTextField, "po", posY++);
							posYAccesTextField.addActionListener(e -> {
								controller.moveAccessoire(posXAccesTextField.getText(), posYAccesTextField.getText());
								posYAccesTextField.setText(controller.getSelectedAccessoire().getPosition().getY().toString());
								drawingPanel.repaint();
							});

							//---- largAccesParam ----
							addParams(parametresModifPanel, "Largeur de <type acessoire (demo)>", largAccesTextField, "po", posY++);
							largAccesTextField.addActionListener(e -> {
								controller.setDimensionAccessoire(largAccesTextField.getText(), hAccesTextField.getText(), margeAccesTextField.getText());
								largAccesTextField.setText(controller.getSelectedAccessoire().getLargeur().toString());
								drawingPanel.repaint();
							});

							//---- hAccesParam ----
							addParams(parametresModifPanel, "Hauteur de <type acessoire (demo)>", hAccesTextField, "po", posY++);
							hAccesTextField.addActionListener(e -> {
								controller.setDimensionAccessoire(largAccesTextField.getText(), hAccesTextField.getText(), margeAccesTextField.getText());
								hAccesTextField.setText(controller.getSelectedAccessoire().getHauteur().toString());
								drawingPanel.repaint();
							});

							//---- margeAccesParam ----
							addParams(parametresModifPanel, "Marge de la Fenêtre", margeAccesTextField, "po", posY++);
							margeAccesTextField.addActionListener(e -> {
								//controller.setDimensionAccessoire(largAccesTextField.getText(), hAccesTextField.getText(), margeAccesTextField.getText());
								//margeAccesTextField.setText(controller.getSelectedAccessoire()..toString());
								//drawingPanel.repaint();
							});

							// ---- delete (accessoire ou separateur) ----
							deleteButton.setText("Supprimer");
							deleteButton.setPreferredSize(new Dimension(160, 22));
							deleteButton.setHorizontalTextPosition(SwingConstants.CENTER);
							deleteButton.setRequestFocusEnabled(false);
							deleteButton.addActionListener(e -> {
								if (controller.isSeparateurSelected()) {
									controller.removeSeparateur();
								}
								updater.updateButtons();
								updater.updateParamsShown();
								drawingPanel.repaint();
							});
							parametresModifPanel.add(deleteButton, new GridBagConstraints(0, posY++, 3, 1, 0.0, 0.0,
									GridBagConstraints.CENTER, GridBagConstraints.NONE,
									new Insets(10, 0, 0, 0), 0, 0));

							parametresModifPanel.add(sepParam2, new GridBagConstraints(0, posY++, 3, 1, 0, 0,
								GridBagConstraints.CENTER, GridBagConstraints.BOTH,
								new Insets(20, 0, 20, 0), 0, 0));


							//---- distGrilleParam ----
							addParams(parametresModifPanel, "Distance des lignes de la grille", distGrilleTextField, "po", posY);
							distGrilleTextField.addActionListener(e -> {
								controller.setDistLigneGrille(distGrilleTextField.getText());
								distGrilleTextField.setText(controller.getDistLigneGrille().toString());
								drawingPanel.repaint();
							});
						}
						for (int i = 22; i < parametresModifPanel.getComponentCount() - 4; i++)
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

							int posY = 0;

							//---- longPlisParam ----
							addParams(configurationPlanPanel, "Longueur des plis", longPlisTextField, "po", posY++);
							longPlisTextField.addActionListener(e -> {
								controller.setParametrePlan(margeEpTextField.getText(), margeLargTextField.getText(),
										anglePlisTextField.getText(), longPlisTextField.getText());
								longPlisTextField.setText(controller.getParametrePlan(0));
							});

							//---- margeEpParam ----
							addParams(configurationPlanPanel, "Marge \u00e9paisseur des mat\u00e9riaux", margeEpTextField, "po", posY++);
							margeEpTextField.addActionListener(e -> {
								controller.setParametrePlan(margeEpTextField.getText(), margeLargTextField.getText(),
										anglePlisTextField.getText(), longPlisTextField.getText());
								margeEpTextField.setText(controller.getParametrePlan(1));
							});

							//---- margeLargParam ----
							addParams(configurationPlanPanel, "Marge largeur des replis", margeLargTextField, "po", posY++);
							margeLargTextField.addActionListener(e -> {
								controller.setParametrePlan(margeEpTextField.getText(), margeLargTextField.getText(),
										anglePlisTextField.getText(), longPlisTextField.getText());
								margeLargTextField.setText(controller.getParametrePlan(2));
							});

							//---- anglePlisParam ----
							addParams(configurationPlanPanel, "Angle des plis", anglePlisTextField, "deg", posY++);
							anglePlisTextField.addActionListener(e -> {
								controller.setParametrePlan(margeEpTextField.getText(), margeLargTextField.getText(),
										anglePlisTextField.getText(), longPlisTextField.getText());
								anglePlisTextField.setText(controller.getParametrePlan(3));
							});

							sepConfig.setVisible(false);
							configurationPlanPanel.add(sepConfig, new GridBagConstraints(0, posY++, 3, 1, 0, 0,
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
							configurationPlanPanel.add(voirPlanButton, new GridBagConstraints(0, posY, 3, 1, 0.0, 0.0,
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
		addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				closing();
			}
		});
		updater.updateTextFields();
	}

	private void closing() {
		controller.fermerProjet(this);
	}

	private void addParams(JPanel panel, String intro, JTextField textField, String end, int posY) {
		JLabel introLabel = new JLabel(intro);
		panel.add(introLabel, new GridBagConstraints(0, posY, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 10, 10), 0, 0));

		textField.setColumns(5);
		textField.addFocusListener(new FocusAdapter() {
			public void focusLost(FocusEvent e) {
				textField.postActionEvent();
			}
		});
		panel.add(textField, new GridBagConstraints(1, posY, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 10, 10), 0, 0));

		JLabel endLabel = new JLabel(end);
		panel.add(endLabel, new GridBagConstraints(2, posY, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 10, 0), 0, 0));
	}
}
