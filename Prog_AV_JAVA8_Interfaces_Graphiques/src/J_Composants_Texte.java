import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class J_Composants_Texte extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public J_Composants_Texte() {
		this.setTitle("Editeur de texte");
		this.setBounds(0,0,800,800);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public static void main(String[] args){
		J_Composants_Texte jct = new J_Composants_Texte();
		JTextArea jta = new JTextArea();
		jct.getContentPane().add(jta, BorderLayout.CENTER);
		
		/* JTextArea avec les options par défaut.
		 * On remarque que le wordwrap n'est pas automatique, les lignes trop longues ne revenienent pas automatiquement à la ligne (On peut cependant revenir à la ligne manuellement en utilisant la touche entrée)
		 * Options du wordwrap:
		 * SetLineWrap: à false (par défaut), le texte ne revient pas à la ligne. A true, le texte revient à la ligne, en coupant le texte par caractères
		 * SetWrapStyleWord: à false (par défaut), si le texte revient à la ligne, il le fait en coupant les caractères. A true, il revient à la ligne en coupant par mots
		 */
		
		JPanel panelBoutons = new JPanel();
		panelBoutons.setLayout(new GridLayout(0,2));		
		JCheckBox retLigne = new JCheckBox("Retours à la ligne automatiques");
		JCheckBox retMot = new JCheckBox("Retours entre deux mots");
		ActionListener retour = new ActionListener(){
		      public void actionPerformed(ActionEvent e) {
			        if (e.getSource() instanceof JCheckBox){
			        	if (e.getSource() == retLigne){
			        		jta.setLineWrap(retLigne.isSelected());
			        	} else if(e.getSource() == retMot){
			        		jta.setWrapStyleWord(retMot.isSelected());
			        	}
			        };
			  }
		};
		retLigne.addActionListener(retour);
		retMot.addActionListener(retour);
		panelBoutons.add(retLigne);
		panelBoutons.add(retMot);
		
		// CheckBox gras et italique
		
		JCheckBox bold = new JCheckBox("Gras");
		JCheckBox italic = new JCheckBox("Italique");
		ActionListener police = new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				int attributs = 0;
				if (e.getSource() instanceof JCheckBox){
					if (e.getSource() == bold || e.getSource() == italic) {
						if (bold.isSelected()){
							attributs += Font.BOLD;
						}
						if (italic.isSelected()){
							attributs += Font.ITALIC;
						}
					}						
					jta.setFont(new Font(jta.getFont().getName(), attributs, jta.getFont().getSize()));
				}
			}
		};
		bold.addActionListener(police);
		italic.addActionListener(police);
		panelBoutons.add(bold);
		panelBoutons.add(italic);
		//jct.add(panelBoutons, BorderLayout.SOUTH);

		// RadioBoutons pour la couleur du texte et de la zone de texte
		
		JPanel panelCouleurs = new JPanel();
		panelCouleurs.setLayout(new GridLayout(0,2));
		panelCouleurs.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		JLabel fond = new JLabel("  Fond  ");
		JLabel texte = new JLabel("  Texte  ");
		JRadioButton redBack = new JRadioButton("Rouge");
		JRadioButton redText = new JRadioButton("Rouge");
		JRadioButton greenBack = new JRadioButton("Vert");
		JRadioButton greenText = new JRadioButton("Vert");
		JRadioButton blueBack = new JRadioButton("Bleu");
		JRadioButton blueText = new JRadioButton("Bleu");
		JRadioButton whiteBack = new JRadioButton("Blanc");
		JRadioButton blackText = new JRadioButton("Noir");
		
		ActionListener color = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() instanceof JRadioButton){
					if (redBack.isSelected()){
						jta.setBackground(Color.RED);
					} else if (greenBack.isSelected()) {
						jta.setBackground(Color.GREEN);
					} else if (blueBack.isSelected()) {
						jta.setBackground(Color.BLUE);
					} else if (whiteBack.isSelected()){
						jta.setBackground(Color.WHITE);
					}
					
					if (redText.isSelected()){
						jta.setForeground(Color.RED);
					} else if (greenText.isSelected()) {
						jta.setForeground(Color.GREEN);
					} else if (blueText.isSelected()) {
						jta.setForeground(Color.BLUE);
					} else if (blackText.isSelected()){
						jta.setForeground(Color.BLACK);
					}
				}
			}
		};
		redBack.addActionListener(color);
		redText.addActionListener(color);
		blueBack.addActionListener(color);
		blueText.addActionListener(color);
		greenBack.addActionListener(color);
		greenText.addActionListener(color);
		whiteBack.addActionListener(color);
		blackText.addActionListener(color);
		ButtonGroup groupeBack = new ButtonGroup();
		ButtonGroup groupeText = new ButtonGroup();
		groupeBack.add(redBack);
		groupeBack.add(blueBack);
		groupeBack.add(greenBack);
		groupeBack.add(whiteBack);
		groupeText.add(redText);
		groupeText.add(greenText);
		groupeText.add(blueText);
		groupeText.add(blackText);
		panelCouleurs.add(fond);
		panelCouleurs.add(texte);
		panelCouleurs.add(redBack);
		panelCouleurs.add(redText);
		panelCouleurs.add(greenBack);
		panelCouleurs.add(greenText);
		panelCouleurs.add(blueBack);
		panelCouleurs.add(blueText);
		panelCouleurs.add(whiteBack);
		panelCouleurs.add(blackText);
		jct.add(panelCouleurs, BorderLayout.EAST);
		
		// Jlist pour la police
		JPanel panelPolices = new JPanel();
		panelPolices.setLayout(new GridLayout(0,2));
		Font[] polices = {new Font("Gras", Font.BOLD, 20), new Font("Mini", 0, 5), new Font("Italique", Font.ITALIC, 15), new Font("Standard", 0, 15)};
		String[] policesNoms = {"Grosse police", "Mini", "Police italienne","Standard"};
		JList<String> listePolices = new JList<String>(policesNoms);
		listePolices.addListSelectionListener(new ListSelectionListener(){
			public void valueChanged(ListSelectionEvent arg0) {
				jta.setFont(polices[listePolices.getSelectedIndex()]);
			}
		});
		JScrollPane jsp = new JScrollPane(listePolices);
		jsp.setPreferredSize(new Dimension(50,80));
		
		// JComboBox pour la taille de police
		String[] tailles = {"6","8","10","12","13","14","15","16","18","20"};
		JComboBox<String> listeTaillePolice = new JComboBox<String>(tailles);
		ActionListener taillePolice = new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() instanceof JComboBox) {
					if (e.getSource() == listeTaillePolice) {
						jta.setFont(new Font(jta.getFont().getName(), jta.getFont().getStyle(), Integer.parseInt((String) listeTaillePolice.getSelectedItem())));
					}
				}
				
			}
			
		};
		listeTaillePolice.addActionListener(taillePolice);
		listeTaillePolice.setPreferredSize(new Dimension(50,20));
		panelPolices.add(jsp);
		panelPolices.add(listeTaillePolice);	
		JPanel panelBoutonsPolice = new JPanel();
		panelBoutonsPolice.setLayout(new BoxLayout(panelBoutonsPolice, BoxLayout.Y_AXIS));
		panelBoutonsPolice.add(panelBoutons);
		panelBoutonsPolice.add(panelPolices);
		jct.add(panelBoutonsPolice, BorderLayout.SOUTH);
		jct.setVisible(true);
		
	}

}
