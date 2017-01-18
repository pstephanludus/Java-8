import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.time.LocalDate;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Fenetre_Boite_Dialogue extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Fenetre_Boite_Dialogue() {
		this.setTitle("Boites de dialogue");
		this.setBounds(0,0,400,400);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
}
	
	public static void main(String[] args){
		
		// Boîtes de saisie
		String clientPrenom = JOptionPane.showInputDialog(null, "Saisir le prénom du client", "Entrée",JOptionPane.QUESTION_MESSAGE);
		String clientNom = JOptionPane.showInputDialog(null, "Saisir le nom du client", "Entrée",JOptionPane.WARNING_MESSAGE);
		
			// Gestion du clic sur annuler
		if (clientPrenom == null) clientPrenom = "John";
		if (clientNom == null) clientNom = "Doe";
		
		JOptionPane.showMessageDialog(null, "Vous souhaitez rechercher le client " + clientPrenom + " " + clientNom, "Recherche de client", JOptionPane.INFORMATION_MESSAGE);
	
		
		Personne[] personnes = new Personne[4];
		personnes[0] = new Personne("Lennon", "John", LocalDate.of(1940,10,9));
		personnes[1] = new Personne("McCartney", "Paul", LocalDate.of(1942,6,18));
		personnes[2] = new Personne("Harrison", "George", LocalDate.of(1943,2,25));
		personnes[3] = new Personne("Starr", "Ringo", LocalDate.of(1940,7,7));
		
		// Boîte de message
		Personne personneRecherchee = new Personne(clientNom, clientPrenom, LocalDate.now());
		boolean trouve = false;
		for (int i=0; i < personnes.length; i++){
			if (personneRecherchee.compareTo(personnes[i]) == 0) {
				trouve = true;
				break;
			}
		}
		if (trouve) {
			JOptionPane.showMessageDialog(null, "Le client " + personneRecherchee.toString() + " est un membre des Beatles !", "Recherche d'un client", JOptionPane.WARNING_MESSAGE);
		} else {
			JOptionPane.showMessageDialog(null, "Le client est introuvable", "Recherche d'un client", JOptionPane.WARNING_MESSAGE);
		}
		
		
		
		// Boîte de saisie liste
		try{
			String BeatlesChoisi = ((Personne)JOptionPane.showInputDialog(null, "Sélectionner le client", "Recherche d'un client", JOptionPane.WARNING_MESSAGE, null, personnes, personnes[0])).toString();
			JOptionPane.showMessageDialog(null, "Vous avez choisi le membre " + BeatlesChoisi, "Beatles choisi !", JOptionPane.INFORMATION_MESSAGE);
		} catch (NullPointerException npe){
			
			JOptionPane.showMessageDialog(null, "Vous avez annulé, vous n'aimez pas les Beatles ?", "Annulation", JOptionPane.INFORMATION_MESSAGE);
		}
		
		Fenetre_Boite_Dialogue fbd = new Fenetre_Boite_Dialogue();
		JButton boutonQuitter = new JButton("Quitter");
		boutonQuitter.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				// Boîte de confirmation
				switch(JOptionPane.showConfirmDialog(null, "Voulez-vous quiter sans enregistrer ?"," Fermeture de l'application",JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE)){
					case JOptionPane.YES_OPTION: JOptionPane.showMessageDialog(null, "Vous avez appuyé sur Oui!", "Fermeture de l'application", JOptionPane.INFORMATION_MESSAGE);
						fbd.dispatchEvent(new WindowEvent(fbd, WindowEvent.WINDOW_CLOSING));
						break;
					case JOptionPane.NO_OPTION: JOptionPane.showMessageDialog(null, "Vous avez appuyé sur Non!", "Application non fermée", JOptionPane.INFORMATION_MESSAGE);
						break;
					case JOptionPane.CANCEL_OPTION: JOptionPane.showMessageDialog(null, "Vous avez appuyé sur Annuler !", "Application non fermée", JOptionPane.INFORMATION_MESSAGE);
						break;
					case JOptionPane.CLOSED_OPTION: JOptionPane.showMessageDialog(null, "Vous avez fermé la fenêtre !", "Fermeture de l'application", JOptionPane.INFORMATION_MESSAGE);
						fbd.dispatchEvent(new WindowEvent(fbd, WindowEvent.WINDOW_CLOSING));
						break;
				}				
			}
			
		});
		fbd.getContentPane().add(boutonQuitter, BorderLayout.CENTER);
		fbd.setVisible(true);
	}

}
