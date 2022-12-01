package muracle.vue;

import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.intellijthemes.FlatNordIJTheme;
import muracle.domaine.MuracleController;
import muracle.utilitaire.FractionError;
import muracle.utilitaire.PouceError;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.text.DecimalFormat;
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

	protected MuracleController controller;
	protected boolean isDarkMode = true;

	private boolean isRightClicDrag;

	public MainWindow() {
		{
			try {
				controller = new MuracleController();
			} catch (FractionError | PouceError e) {
				throw new RuntimeException(e);
			}
		}
		initComponents();
	}

	private void initComponents() {
		//menu
		JMenuBar menuBar = new JMenuBar();
		JButton newProjectButton =  new JButton();
		JButton openProjectButton = new JButton();
		JButton saveProjectButton = new JButton();
		JButton exportButton = new JButton();
		JToggleButton showGrilleButton = new JToggleButton();
		JToggleButton addSeparateurButton = new JToggleButton();
		JToggleButton addAccessoireButton = new JToggleButton();
		JComboBox<String> selectionAccessoireComboBox = new JComboBox<>(new String[] {"Fenêtre", "Porte"});
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

		//params cote
		JTextField largCoteTextField = new JTextField();
		JTextField hCoteTextField = new JTextField();

		//params mur
		JTextField largMurTextField = new JTextField();
		JTextField hMurTextField = new JTextField();
		JTextField poidsMurTextField = new JTextField();

		JSeparator sepParam1 = new JSeparator();

		// params retours d'air
		JTextField epTrouTextField = new JTextField();
		JTextField hRetourAirTextField = new JTextField();
		JTextField distSolRetourAirTextField = new JTextField();

		// params separateurs
		JTextField posSepTextField = new JTextField();

		// params accessoires
		JTextField typeAccesTextField = new JTextField();
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
				if (controller.isVueCote()) {
					largCoteTextField.setText(controller.getSelectedCoteReadOnly().largeur.toString());
					hCoteTextField.setText(controller.getSelectedCoteReadOnly().hauteur.toString());
				}
				if (controller.isSeparateurSelected())
					if (controller.isVueExterieur())
						posSepTextField.setText(controller.getSelectedSeparateur().toString());
					else
						posSepTextField.setText(controller.getSelectedSepInverse().toString());
				if (controller.isAccessoireSelected()) {
					if (controller.isVueExterieur())
						posXAccesTextField.setText(controller.getSelectedAccessoireReadOnly().position.getX().toString());
					else
						posXAccesTextField.setText(controller.getSelectedAccesPosXInverse().toString());
					typeAccesTextField.setText(controller.getSelectedAccessoireReadOnly().type);
					posYAccesTextField.setText(controller.getSelectedAccessoireReadOnly().position.getY().toString());
					largAccesTextField.setText(controller.getSelectedAccessoireReadOnly().largeur.toString());
					hAccesTextField.setText(controller.getSelectedAccessoireReadOnly().hauteur.toString());
					margeAccesTextField.setText(controller.getSelectedAccessoireReadOnly().marge.toString());
				}
				if (controller.isMurSelected()) {
					largMurTextField.setText(controller.getSelectedMurReadOnly().Largeur.toString());
					hMurTextField.setText(controller.getSelectedMurReadOnly().Hauteur.toString());
					DecimalFormat df = new DecimalFormat("0.00");
					if (controller.isVueExterieur())
						poidsMurTextField.setText(String.valueOf(df.format(controller.getSelectedMurReadOnly().PanneauExt.getPoids())));
					else
						poidsMurTextField.setText(String.valueOf(df.format(controller.getSelectedMurReadOnly().PanneauInt.getPoids())));
				}
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
				hAccesTextField.setEnabled(true);
				posXAccesTextField.setEnabled(true);
				posYAccesTextField.setEnabled(true);
				sepConfig.setVisible(false);
				voirPlanButton.setVisible(false);

				for (int i = 0; i < parametresModifPanel.getComponentCount(); i++)
					parametresModifPanel.getComponent(i).setVisible(true);
				if (controller.isVueDessus()) {
					if (controller.isSeparateurSelected()) {
						for (int i = 0; i < 37; i++)
							parametresModifPanel.getComponent(i).setVisible(false);
						for (int i = 40; i < parametresModifPanel.getComponentCount() - 4; i++)
							parametresModifPanel.getComponent(i).setVisible(false);
						deleteButton.setVisible(true);
					}
					else {
						for (int i = 12; i < 27; i++)
							parametresModifPanel.getComponent(i).setVisible(false);
						for (int i = 37; i < parametresModifPanel.getComponentCount() - 4; i++)
							parametresModifPanel.getComponent(i).setVisible(false);
					}
				}
				else {
					for (int i = 0; i < 37; i++)
						parametresModifPanel.getComponent(i).setVisible(false);
					if (controller.isSeparateurSelected()) {
						for (int i = 40; i < parametresModifPanel.getComponentCount() - 4; i++)
							parametresModifPanel.getComponent(i).setVisible(false);
						deleteButton.setVisible(true);
					}
					else if (controller.isAccessoireSelected()) {
						for (int i = 28; i < 40; i++)
							parametresModifPanel.getComponent(i).setVisible(false);
						if (!controller.getSelectedAccessoireReadOnly().type.equals("Fenêtre"))
							for (int i = 55; i < 58; i++)
								parametresModifPanel.getComponent(i).setVisible(false);
						if (controller.getSelectedAccessoireReadOnly().type.equals("Retour d'air")) {
							hAccesTextField.setEnabled(false);
							posXAccesTextField.setEnabled(false);
							posYAccesTextField.setEnabled(false);
						}
						if (controller.getSelectedAccessoireReadOnly().type.equals("Porte"))
							posYAccesTextField.setEnabled(false);
						deleteButton.setVisible(true);
					}
					else if (controller.isMurSelected()) {
						for (int i = 31; i < parametresModifPanel.getComponentCount() - 4; i++)
							parametresModifPanel.getComponent(i).setVisible(false);
						for (int i = 18; i < 27; i++)
							parametresModifPanel.getComponent(i).setVisible(true);
						sepConfig.setVisible(true);
						voirPlanButton.setVisible(true);
					}
					else {
						for (int i = 22; i < parametresModifPanel.getComponentCount() - 4; i++)
							parametresModifPanel.getComponent(i).setVisible(false);
						for (int i = 12; i < 18; i++)
							parametresModifPanel.getComponent(i).setVisible(true);
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
				//---- nouveau projet ----
				newProjectButton.setText("Nouveau projet");
				newProjectButton.setPreferredSize(new Dimension(120, 22));
				newProjectButton.setMaximumSize(new Dimension(120, 32767));
				newProjectButton.setHorizontalTextPosition(SwingConstants.CENTER);
				newProjectButton.setRequestFocusEnabled(false);
				newProjectButton.setFocusPainted(false);
				newProjectButton.addActionListener(e -> {
					controller.creerProjet();
					updater.updateTextFields();
					updater.updateButtons();
					updater.updateParamsShown();
					drawingPanel.updateParametre();
					drawingPanel.repaint();
				});
				menuBar.add(newProjectButton);

				//---- ouvrir projet ----
				openProjectButton.setText("Ouvrir projet");
				openProjectButton.setPreferredSize(new Dimension(120, 22));
				openProjectButton.setMaximumSize(new Dimension(120, 32767));
				openProjectButton.setHorizontalTextPosition(SwingConstants.CENTER);
				openProjectButton.setRequestFocusEnabled(false);
				openProjectButton.setFocusPainted(false);
				openProjectButton.addActionListener(e -> {
					controller.ouvrirProjet(this);
					updater.updateTextFields();
					updater.updateButtons();
					updater.updateParamsShown();
					drawingPanel.updateParametre();
					drawingPanel.repaint();
				});
				menuBar.add(openProjectButton);

				//---- sauvergarder projet ----
				saveProjectButton.setText("Sauvegarder projet");
				saveProjectButton.setMaximumSize(new Dimension(140, 32767));
				saveProjectButton.setPreferredSize(new Dimension(140, 22));
				saveProjectButton.setHorizontalTextPosition(SwingConstants.CENTER);
				saveProjectButton.setRequestFocusEnabled(false);
				saveProjectButton.addActionListener(e -> controller.sauvegarderProjet(this));
				menuBar.add(saveProjectButton);

				//---- exporter plan ----
				exportButton.setText("Exporter plans");
				exportButton.setPreferredSize(new Dimension(120, 22));
				exportButton.setMaximumSize(new Dimension(120, 32767));
				exportButton.setHorizontalTextPosition(SwingConstants.CENTER);
				exportButton.setRequestFocusEnabled(false);
				exportButton.addActionListener(e -> controller.exporterPlan(this));
				menuBar.add(exportButton);

				//---- afficher grille ----
				showGrilleButton.setText("Afficher grille");
				showGrilleButton.setMaximumSize(new Dimension(120, 32767));
				showGrilleButton.setPreferredSize(new Dimension(120, 22));
				showGrilleButton.setHorizontalTextPosition(SwingConstants.CENTER);
				showGrilleButton.setRequestFocusEnabled(false);
				showGrilleButton.addActionListener(e -> {
					showGrilleButton.setSelected(showGrilleButton.isSelected());
					controller.reverseIsGrilleShown();
					drawingPanel.repaint();
				});
				menuBar.add(showGrilleButton);

				//---- ajouter Separateur ----
				addSeparateurButton.setText("Ajouter séparateur");
				addSeparateurButton.setMaximumSize(new Dimension(140, 32767));
				addSeparateurButton.setPreferredSize(new Dimension(140, 22));
				addSeparateurButton.setHorizontalTextPosition(SwingConstants.CENTER);
				addSeparateurButton.setRequestFocusEnabled(false);
				addSeparateurButton.addActionListener(e -> {
					addSeparateurButton.setSelected(addSeparateurButton.isSelected());
					if (addAccessoireButton.isSelected())
						addAccessoireButton.doClick();
				});
				menuBar.add(addSeparateurButton);

				//---- ajouter accessoire ----
				addAccessoireButton.setText("Ajouter Accessoire");
				addAccessoireButton.setMaximumSize(new Dimension(140, 32767));
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
						if (addSeparateurButton.isSelected())
							addSeparateurButton.setSelected(false);
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
				changeVueButton.setMaximumSize(new Dimension(120, 32767));
				changeVueButton.setPreferredSize(new Dimension(120, 22));
				changeVueButton.setHorizontalTextPosition(SwingConstants.CENTER);
				changeVueButton.setRequestFocusEnabled(false);
				changeVueButton.setVisible(false);
				changeVueButton.addActionListener(e -> {
					changeVueButton.setSelected(changeVueButton.isSelected());
					controller.setIsVueExterieur(!changeVueButton.isSelected());
					drawingPanel.miroir();
					drawingPanel.repaint();
					if (changeVueButton.isSelected()) {
						selectionAccessoireComboBox.addItem("Prise électrique");
						selectionAccessoireComboBox.addItem("Retour d'air");
					}
					else {
						selectionAccessoireComboBox.setSelectedItem("Fenêtre");
						selectionAccessoireComboBox.removeItem("Prise électrique");
						selectionAccessoireComboBox.removeItem("Retour d'air");
					}
					if (controller.isMurSelected()) {
						DecimalFormat df = new DecimalFormat("0.00");
						if (controller.isVueExterieur())
							poidsMurTextField.setText(String.valueOf(df.format(controller.getSelectedMurReadOnly().PanneauExt.getPoids())));
						else
							poidsMurTextField.setText(String.valueOf(df.format(controller.getSelectedMurReadOnly().PanneauInt.getPoids())));
					}
					if (controller.isAccessoireSelected()) {
						if (controller.isVueExterieur())
							posXAccesTextField.setText(controller.getSelectedAccessoireReadOnly().position.getX().toString());
						else
							posXAccesTextField.setText(controller.getSelectedAccesPosXInverse().toString());
					}
					if (controller.isSeparateurSelected()) {
						if (controller.isVueExterieur())
							posSepTextField.setText(controller.getSelectedSeparateur().toString());
						else
							posSepTextField.setText(controller.getSelectedSepInverse().toString());
					}
					updater.updateParamsShown();
				});
				menuBar.add(changeVueButton);

				//---- retour vue dessus ----
				retourVueHautButton.setText("Retour vue dessus");
				retourVueHautButton.setMaximumSize(new Dimension(140, 32767));
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
						String rev = "Rev";
						String mode = "moon";
						if (!isDarkMode) {
							UIManager.setLookAndFeel(new FlatIntelliJLaf());
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
					}catch (Exception exception){
						exception.printStackTrace();
					}
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

				JLabel tokenSpacer = new JLabel("");
				tokenSpacer.setPreferredSize(new Dimension(1, 20));

				menuBar.add(tokenSpacer);
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

					drawingPanel.addMouseMotionListener(new MouseMotionListener() {
						@Override
						public void mouseDragged(MouseEvent e) {
							if (isRightClicDrag)
								drawingPanel.moved(e);
							else {
								if (controller.isSeparateurSelected() || controller.isAccessoireSelected()) {
									controller.dragging(drawingPanel.movedItem(e));
									drawingPanel.repaint();
								}
							}
						}

						@Override
						public void mouseMoved(MouseEvent e) {

						}
					});
					drawingPanel.addMouseListener(new MouseListener() {
						@Override
						public void mouseClicked(MouseEvent e) {
						}

						@Override
						public void mousePressed(MouseEvent e) {

							if (e.getButton() == 3){//click droit
								drawingPanel.press(e);
								isRightClicDrag = true;
							}
							else  if (e.getButton() == 1) {//click gauche
								drawingPanel.pressItem(e);
								isRightClicDrag = false;

								boolean token = controller.isVueDessus();
								controller.interactComponent(drawingPanel.coordPixelToPouce(e),
										addSeparateurButton.isSelected(), addAccessoireButton.isSelected(), (String) selectionAccessoireComboBox.getSelectedItem());
								updater.updateParamsShown();
								updater.updateButtons();
								updater.updateTextFields();
								if (token != controller.isVueDessus())
									drawingPanel.updateParametre();
								drawingPanel.repaint();

								controller.startDragging();
							}
						}

						@Override
						public void mouseReleased(MouseEvent e) {
							if (isRightClicDrag)
								drawingPanel.release(e);
							else {
								if (controller.isSeparateurSelected() || controller.isAccessoireSelected())
									controller.endDraggging();
								drawingPanel.releaseItem();
							}
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
								drawingPanel.updateParametre();
								drawingPanel.repaint();
							});

							//---- longSalleParam ----
							addParams(parametresModifPanel, "Longueur de la salle", longSalleTextField, "po", posY++);
							longSalleTextField.addActionListener(e -> {
								controller.setDimensionSalle(largSalleTextField.getText(), longSalleTextField.getText(),
										hSalleTextField.getText(), epMursTextField.getText());
								longSalleTextField.setText(controller.getDimensionSalle(1));
								drawingPanel.updateParametre();
								drawingPanel.repaint();
							});

							//---- hSalleParam ----
							addParams(parametresModifPanel, "Hauteur de la salle", hSalleTextField, "po", posY++);
							hSalleTextField.addActionListener(e -> {
								controller.setDimensionSalle(largSalleTextField.getText(), longSalleTextField.getText(),
										hSalleTextField.getText(), epMursTextField.getText());
								hSalleTextField.setText(controller.getDimensionSalle(2));
								drawingPanel.updateParametre();
								drawingPanel.repaint();
							});

							//---- epMursParam ----
							addParams(parametresModifPanel, "\u00c9paisseur des murs de la salle", epMursTextField, "po", posY++);
							epMursTextField.addActionListener(e -> {
								controller.setDimensionSalle(largSalleTextField.getText(), longSalleTextField.getText(),
										hSalleTextField.getText(), epMursTextField.getText());
								epMursTextField.setText(controller.getDimensionSalle(3));
								drawingPanel.updateParametre();
								drawingPanel.repaint();
							});

							//---- largCoteParam ----
							addParams(parametresModifPanel, "Largeur du côté", largCoteTextField, "po", posY++);
							largCoteTextField.setEnabled(false);

							//---- hCoteParam ----
							addParams(parametresModifPanel, "Hauteur du côté", hCoteTextField, "po", posY++);
							hCoteTextField.setEnabled(false);

							//---- largMurParam ----
							addParams(parametresModifPanel, "Largeur du Mur", largMurTextField, "po", posY++);
							largMurTextField.setEnabled(false);

							//---- hMurParam ----
							addParams(parametresModifPanel, "Hauteur du Mur", hMurTextField, "po", posY++);
							hMurTextField.setEnabled(false);

							//---- poidsMurParam ----
							addParams(parametresModifPanel, "Poids du Mur", poidsMurTextField, "lb", posY++);
							poidsMurTextField.setEnabled(false);

							//separateur
							parametresModifPanel.add(sepParam1, new GridBagConstraints(0, posY++, 3, 1, 0, 0,
									GridBagConstraints.CENTER, GridBagConstraints.BOTH,
									new Insets(20, 0, 20, 0), 0, 0));

							//---- epTrouParam ----
							addParams(parametresModifPanel, "\u00c9paisseur trou des retours d'air", epTrouTextField, "po", posY++);
							epTrouTextField.addActionListener(e -> {
								controller.setParametreRetourAir(hRetourAirTextField.getText(),
										epTrouTextField.getText(), distSolRetourAirTextField.getText());
								epTrouTextField.setText(controller.getSalleReadOnly().epaisseurTrouRetourAir.toString());
								drawingPanel.repaint();
							});

							//---- hRetourAirParam ----
							addParams(parametresModifPanel, "Hauteur des retours d'air", hRetourAirTextField, "po", posY++);
							hRetourAirTextField.addActionListener(e -> {
								controller.setParametreRetourAir(hRetourAirTextField.getText(),
										epTrouTextField.getText(), distSolRetourAirTextField.getText());
								hRetourAirTextField.setText(controller.getSalleReadOnly().hauteurRetourAir.toString());
								drawingPanel.repaint();
							});

							//---- distSolRetourAirParam ----
							addParams(parametresModifPanel, "Distance sol des retours d'air", distSolRetourAirTextField, "po", posY++);
							distSolRetourAirTextField.addActionListener(e -> {
								controller.setParametreRetourAir(hRetourAirTextField.getText(),
										epTrouTextField.getText(), distSolRetourAirTextField.getText());
								distSolRetourAirTextField.setText(controller.getSalleReadOnly().distanceTrouRetourAir.toString());
								drawingPanel.repaint();
							});

							//---- params separateurs ----
							addParams(parametresModifPanel, "Position du Séparateur", posSepTextField, "po", posY++);
							posSepTextField.addActionListener(e -> {
								if (controller.isSeparateurSelected()) {
									controller.moveSeparateur(posSepTextField.getText());
									if (controller.isVueExterieur())
										posSepTextField.setText(controller.getSelectedSeparateur().toString());
									else
										posSepTextField.setText(controller.getSelectedSepInverse().toString());
									drawingPanel.repaint();
								}
							});

							// params accessoires

							//---- typeAccesParam ----
							addParams(parametresModifPanel, "Type de l'accessoire", typeAccesTextField, "", posY++);
							typeAccesTextField.setEnabled(false);

							//---- posXAccesParam ----
							addParams(parametresModifPanel, "Position X de l'accessoire", posXAccesTextField, "po", posY++);
							posXAccesTextField.addActionListener(e -> {
								if (controller.isAccessoireSelected()) {
									controller.moveAccessoire(posXAccesTextField.getText(), posYAccesTextField.getText());
									if (controller.isVueExterieur())
										posXAccesTextField.setText(controller.getSelectedAccessoireReadOnly().position.getX().toString());
									else
										posXAccesTextField.setText(controller.getSelectedAccesPosXInverse().toString());
									drawingPanel.repaint();
								}
							});

							//---- posYAccesParam ----
							addParams(parametresModifPanel, "Position Y de l'acessoire", posYAccesTextField, "po", posY++);
							posYAccesTextField.addActionListener(e -> {
								if (controller.isAccessoireSelected()) {
									controller.moveAccessoire(posXAccesTextField.getText(), posYAccesTextField.getText());
									posYAccesTextField.setText(controller.getSelectedAccessoireReadOnly().position.getY().toString());
									drawingPanel.repaint();
								}
							});

							//---- largAccesParam ----
							addParams(parametresModifPanel, "Largeur de l'acessoire", largAccesTextField, "po", posY++);
							largAccesTextField.addActionListener(e -> {
								if (controller.isAccessoireSelected()) {
									controller.setDimensionAccessoire(largAccesTextField.getText(), hAccesTextField.getText(), margeAccesTextField.getText());
									largAccesTextField.setText(controller.getSelectedAccessoireReadOnly().largeur.toString());
									//affecte parfois
									if (controller.isVueExterieur())
										posXAccesTextField.setText(controller.getSelectedAccessoireReadOnly().position.getX().toString());
									else
										posXAccesTextField.setText(controller.getSelectedAccesPosXInverse().toString());
									drawingPanel.repaint();
								}
							});

							//---- hAccesParam ----
							addParams(parametresModifPanel, "Hauteur de l'acessoire", hAccesTextField, "po", posY++);
							hAccesTextField.addActionListener(e -> {
								if (controller.isAccessoireSelected()) {
									controller.setDimensionAccessoire(largAccesTextField.getText(), hAccesTextField.getText(), margeAccesTextField.getText());
									hAccesTextField.setText(controller.getSelectedAccessoireReadOnly().hauteur.toString());
									posYAccesTextField.setText(controller.getSelectedAccessoireReadOnly().position.getY().toString()); //affecte parfois
									drawingPanel.repaint();
								}
							});

							//---- margeAccesParam ----
							addParams(parametresModifPanel, "Marge de la Fenêtre", margeAccesTextField, "po", posY++);
							margeAccesTextField.addActionListener(e -> {
								if (controller.isAccessoireSelected() && controller.getSelectedAccessoireReadOnly().type.equals("Fenêtre")) {
									controller.setDimensionAccessoire(largAccesTextField.getText(), hAccesTextField.getText(), margeAccesTextField.getText());
									margeAccesTextField.setText(controller.getSelectedAccessoireReadOnly().marge.toString());
									drawingPanel.repaint();
								}
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
								if (controller.isAccessoireSelected()) {
									controller.removeAccessoire();
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
						for (int i = 12; i < 27; i++)
							parametresModifPanel.getComponent(i).setVisible(false);
						for (int i = 37; i < parametresModifPanel.getComponentCount() - 4; i++)
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
								drawingPanel.repaint();
							});

							//---- margeEpParam ----
							addParams(configurationPlanPanel, "Marge \u00e9paisseur des mat\u00e9riaux", margeEpTextField, "po", posY++);
							margeEpTextField.addActionListener(e -> {
								controller.setParametrePlan(margeEpTextField.getText(), margeLargTextField.getText(),
										anglePlisTextField.getText(), longPlisTextField.getText());
								margeEpTextField.setText(controller.getParametrePlan(1));
								drawingPanel.repaint();
							});

							//---- margeLargParam ----
							addParams(configurationPlanPanel, "Marge largeur des replis", margeLargTextField, "po", posY++);
							margeLargTextField.addActionListener(e -> {
								controller.setParametrePlan(margeEpTextField.getText(), margeLargTextField.getText(),
										anglePlisTextField.getText(), longPlisTextField.getText());
								margeLargTextField.setText(controller.getParametrePlan(2));
								drawingPanel.repaint();
							});

							//---- anglePlisParam ----
							addParams(configurationPlanPanel, "Angle des plis", anglePlisTextField, "deg", posY++);
							anglePlisTextField.addActionListener(e -> {
								controller.setParametrePlan(margeEpTextField.getText(), margeLargTextField.getText(),
										anglePlisTextField.getText(), longPlisTextField.getText());
								anglePlisTextField.setText(controller.getParametrePlan(3));
								drawingPanel.repaint();
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

		if (intro.equals("Type de l'accessoire")) {
			textField.setColumns(8);
			panel.add(textField, new GridBagConstraints(1, posY, 2, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 10, 0), 0, 0));
		}
		else {
			textField.setColumns(5);
			textField.addFocusListener(new FocusAdapter() {
				public void focusLost(FocusEvent e) {
					textField.postActionEvent();
				}
			});
			panel.add(textField, new GridBagConstraints(1, posY, 1, 1, 0.0, 0.0,
					GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 10, 10), 0, 0));
		}

		JLabel endLabel = new JLabel(end);
		panel.add(endLabel, new GridBagConstraints(2, posY, 1, 1, 0.0, 0.0,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 10, 0), 0, 0));
	}
}
