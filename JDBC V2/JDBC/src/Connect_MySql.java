import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;


public class Connect_MySql {

	public static void main(String[] args) {
		String pilote = "com.mysql.jdbc.Driver";
		
		try {
			Class.forName(pilote);
		} catch (ClassNotFoundException e) {
			System.out.println("Erreur lors du chargement du pilote: " + e.toString());
		}
		Connection cnxDirect=null;
		
		// Etablir et manipuler la connexion
		try {
			cnxDirect = DriverManager.getConnection("jdbc:mysql://localhost/comcli?allowMultiQueries=true","root","");
			System.out.println("Connexion effectuée. Etat de la connexion: " + !cnxDirect.isClosed() + "\nConexion valide: " + cnxDirect.isValid(10));
			cnxDirect.setReadOnly(true);
			System.out.println("La connexion est en readonly: "+ cnxDirect.isReadOnly());
			cnxDirect.close();
			System.out.println("Connexion fermée: " + cnxDirect.isClosed());
		} catch (SQLException e) {
			System.out.println("Erreur pendant la connexion: " + e.toString());
		}
		
		try
		{
			cnxDirect = DriverManager.getConnection("jdbc:mysql://localhost/comcli?allowMultiQueries=true","root","");
			AffichageWarning(cnxDirect);		// SQLWarnings: les avertissements
			InfoBase(cnxDirect);				// Information sur la base
			testExecute(cnxDirect);			//  Exécution d’instructions de base avec l’objet Statement
			testExecuteBatch(cnxDirect);		//  Exécution d’instructions par lot
			testExecuteMultiple(cnxDirect);	// Exécuter plusieurs instructions séparées par des ‘;‘
			testRequeteParam(cnxDirect);		// Exécution d’instructions paramétrées avec PreparedStatement
			testPreparedStatement(cnxDirect);	// Exécution d’instructions paramétrées avec PreparedStatement
			testProcedureStockee(cnxDirect);	// Exécution de procédures stockées avec l’objet CallableStatement
			testInfosResultSet(cnxDirect);	// Utilisation des jeux d’enregistrements avec l’interface ResultSet
			testPositionRS(cnxDirect);		// Méthodes de gestion du pointeur d’enregistrements
			testLectureRS(cnxDirect);			// Lecture des données dans un ResultSet
			testModificationRS(cnxDirect);	// Modification des données dans un ResultSet
			
			
		} catch (SQLException e) {
			System.out.println("Erreur pendant la connexion: " + e.toString());
		}
	}
	
	
	
	
	
	
	// SQLWarnings: les avertissements
	public static void AffichageWarning(Connection cnx){
		try {
			SQLWarning warning =  cnx.getWarnings();
			while (warning != null){
				System.out.println(warning.getMessage() + " " + warning.getSQLState() + " " + String.valueOf(warning.getErrorCode()));
				warning = warning.getNextWarning();
				cnx.clearWarnings();
			}
		} catch (SQLException e) {
			System.out.println("Erreur pendant la connexion: " + e.toString());
		}
	}
	
	// Information sur la base
	public static void InfoBase(Connection cnx){
		try{		
			DatabaseMetaData DBMD =  cnx.getMetaData();
			String tableString = "";
			ResultSet RS = DBMD.getTables(null, null, null,  new String[] {"TABLE"});
			while(RS.next()){
				tableString += "   " + RS.getString(3);
			}
			String proceduresStockees = "";
			RS = DBMD.getProcedures(null, null, null);
			while(RS.next()){
				proceduresStockees += "   " + RS.toString();
			}
			RS.close();
			System.out.println("Type de base : " + "foo" 
								+ "\nversion: " + DBMD.getDatabaseProductVersion() 
								+ "\nnom du pilote: " + DBMD.getDriverName()
								+ "\nnom de l'utilisateur: " + DBMD.getUserName()
								+ "\nURL de connexion: " + DBMD.getURL()
								+ "\ntables de base: " + tableString
								+ "\nprocédures stockées: " + proceduresStockees);
		} catch (SQLException e) {
			System.out.println("Erreur pendant la connexion: " + e.toString());
		}
	}
	
	// Exécution d’instructions de base avec l’objet Statement
	public static void testExecute(Connection cnx){
		try{
			Scanner reader = new Scanner(System.in);
			System.out.println("Saisissez la requête à effectuer, ou 'TEST' pour afficher les résultats de requêtes test préfaites:");
			String reputil = reader.nextLine();
			Statement req;
			if (reputil.toLowerCase().equals("test")){
				System.out.println("\nRequête SELECT * FROM client:");
				req = cnx.createStatement();
				traitementRequete(req, "SELECT * FROM client");
				req.close();
				
				System.out.println("\nRequête UPDATE client SET NCLI = 'B063' WHERE NCLI = 'B062'");
				req = cnx.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
				traitementRequete(req, "UPDATE client SET NCLI = 'B063' WHERE NCLI = 'B062'");
				req.close();
			} else {
				req = cnx.createStatement();
				traitementRequete(req, reputil);
			}
		} catch (SQLException e) {
			System.out.println("Erreur pendant la connexion: " + e.toString());
		}
	}
	
	public static void traitementRequete(Statement req, String sql){
		try{
			req.execute(sql);
			ResultSet RS = req.getResultSet();
			System.out.println("Résultat de la requête " + sql + ":");
			if (req.getUpdateCount() < 0) {
				// Cas du SELECT
				int rowcount = 0;
				if  (RS.last()){
					rowcount = RS.getRow();
					RS.beforeFirst();
				}
				while (RS.next()){
					int colCount = RS.getMetaData().getColumnCount();
					String colonne = "";
					for (int i = 1; i <= colCount ; i++){
						colonne += RS.getString(i) + " ";
					}
					System.out.println(colonne);
				}
				System.out.println(rowcount + " lignes retournées");
			} else {
				// Cas de l'UPDATE
				// L'instruction executeUpdate retourne soit le nombre de lignes, soit 0 si la requête ne retourne rien
				System.out.println("Nombre de lignes: " + req.getUpdateCount());
			}
			
			
		} catch (SQLException e) {
			System.out.println("Erreur pendant la connexion: " + e.toString());
		}
	}

	// Exécution d’instructions par lot
	public static void testExecuteBatch(Connection cnx){
		try{			
			// Saisies utilisateur
			Scanner reader = new Scanner(System.in);
			Statement req = cnx.createStatement();
			String reputil = "";
			System.out.println("Saisissez les requêtes à effectuer, ou 'run' pour les exécuter:");
			while (!reputil.toLowerCase().equals("run")) {
				 reputil = reader.nextLine();
				 if (!reputil.toLowerCase().equals("run")) {
					 req.addBatch(reputil);
				 }
			}
			
			// Affichage du retour de la requête
			int retourReq[] =  req.executeBatch();
			req.close();
			int temp = 0;
			String retour = "";
			for (int code: retourReq){
				temp++;
				if (code == Statement.EXECUTE_FAILED) {
					retour = "EXECUTE_FAILED";
				} else {
					retour = "SUCCESS_NO_INFO";
				}
				System.out.println("Requête " + temp + ": " + retour);
			}
		} catch (SQLException e) {
			System.out.println("Erreur pendant la connexion: " + e.toString());
		} finally {
			System.out.println("Requêtes terminées");
		}
		
	}
	
	// Exécuter plusieurs instructions séparées par des ‘;’
	public static void testExecuteMultiple(Connection cnx){
		try{
			Scanner reader = new Scanner(System.in);
			Statement req = cnx.createStatement();
			String reputil = "";
			System.out.println("Saisissez les requêtes à effectuer, séparées par des ';':");
			reputil = reader.nextLine();
			boolean hasMoreResultSets = req.execute(reputil);
			int nbReq = 0;
			while ( hasMoreResultSets ) {
				ResultSet RS = req.getResultSet();
				nbReq+=1;
				System.out.println("\nRésultat de la requête " + nbReq + " :");
				if (req.getUpdateCount() < 0) {
					// Cas du SELECT
					int rowcount = 0;
					if  (RS.last()){
						rowcount = RS.getRow();
						RS.beforeFirst();
					}
					while (RS.next()){
						int colCount = RS.getMetaData().getColumnCount();
						String colonne = "";
						for (int i = 1; i <= colCount ; i++){
							colonne += RS.getString(i) + " ";
						}
						System.out.println(colonne);
					}
					System.out.println(rowcount + " lignes retournées");
				} else {
					// Cas de l'UPDATE
					// L'instruction executeUpdate retourne soit le nombre de lignes, soit 0 si la requête ne retourne rien
					System.out.println("Nombre de lignes: " + req.getUpdateCount());
				}
				 hasMoreResultSets = req.getMoreResults();
			}
		} catch (SQLException e) {
			System.out.println("Erreur pendant la connexion: " + e.toString());			
		}
	}
	
	// Exécution d’instructions paramétrées avec preparedStatement
	public static void testRequeteParam(Connection cnx){
		try{
			Scanner reader = new Scanner(System.in);
			PreparedStatement req = cnx.prepareStatement("select * from client where compte=?", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			int reputil;
			try {
				System.out.println("Saisissez le montant du compte des clients à rechercher:");
				reputil = reader.nextInt();
				req.setInt(1, reputil);
				req.executeQuery();
				System.out.println("Requete terminée!");
				
			} catch (InputMismatchException e) {
				System.out.println("Vous avez saisi autre chose qu'un nombre !");
			}
			
			
		} catch (SQLException e) {
			System.out.println("Erreur pendant la connexion: " + e.toString());			
		}
	}
	
	// Exécution d’instructions paramétrées avec PreparedStatement
	public static void testPreparedStatement(Connection cnx) {
		Scanner reader = new Scanner(System.in);
		String ncli;
		String nom;
		
		System.out.println("Saisissez le numéro de client:");
		ncli = reader.nextLine();
		
		System.out.println(("Saisissez le nouveau nom pour ce client:"));
		nom = reader.nextLine();
		
		try{
			//"UPDATE client SET NCLI = 'B063' WHERE NCLI = 'B062'"
			PreparedStatement req = cnx.prepareStatement("UPDATE client SET NOM = ? WHERE NCLI = ?", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			req.setString(1, nom);
			req.setString(2, ncli);
			req.executeUpdate();
		} catch (SQLException e) {
			System.out.println("Erreur pendant la connexion: " + e.toString());			
		}
	}

	// Exécution de procédures stockées avec l’objet CallableStatement
	public static void testProcedureStockee(Connection cnx){
		Scanner reader = new Scanner(System.in);
		String ncli;
		
		System.out.println("Saisissez le numéro de client:");
		ncli = reader.nextLine();
		try{
			CallableStatement reqa = cnx.prepareCall("{call commandesParClients(?)}");
			CallableStatement reqb = cnx.prepareCall("{call nbCommandes(?, ?)}");
			reqa.setString(1, ncli);
			reqb.setString(1, ncli);
			reqb.registerOutParameter(2, java.sql.Types.INTEGER);
			reqa.executeQuery();
			reqb.executeQuery();
			
			ResultSet RS = reqa.getResultSet();
			System.out.println(reqb.getInt(2) + " commandes trouvées pour le client " + ncli + " :");
			int rowcount = 0;
			if  (RS.last()){
				rowcount = RS.getRow();
				RS.beforeFirst();
			}
			while (RS.next()){
				int colCount = RS.getMetaData().getColumnCount();
				String colonne = "";
				for (int i = 1; i <= colCount ; i++){
					colonne += RS.getString(i) + " ";
				}
				System.out.println(colonne);
			}
			
			
			
		} catch (SQLException e) {
			System.out.println("Erreur pendant la connexion: " + e.toString());			
		}
		
		
	}
	
	//Utilisation des jeux d’enregistrements avec l’interface ResultSet
	public static void infosResultSet(ResultSet rs){
		String donnees = "";
		try{
			switch (rs.getType()){
				case ResultSet.TYPE_FORWARD_ONLY:
					donnees += "TYPE_FORWARD_ONLY";
					break;
				case ResultSet.TYPE_SCROLL_INSENSITIVE:
					donnees += "TYPE_SCROLL_INSENSITIVE";
					break;
				case ResultSet.TYPE_SCROLL_SENSITIVE:
					donnees += "TYPE_SCROLL_SENSITIVE";
					break;
			}	
			switch (rs.getConcurrency()) {
				case ResultSet.CONCUR_READ_ONLY:
					donnees +="     CONCUR_READ_ONLY";
					break;
				case ResultSet.CONCUR_UPDATABLE:
					donnees +="     CONCUR_UPDATABLE";
					break;
			}
			
			System.out.println("Infos ResultSet: " + donnees);
		} catch (SQLException e) {
			System.out.println("Erreur pendant la connexion: " + e.toString());			
		}
	}
	
	//Utilisation des jeux d’enregistrements avec l’interface ResultSet
	public static void testInfosResultSet(Connection cnx){
		try{
			PreparedStatement stm = cnx.prepareStatement("select * from client", ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
			infosResultSet(stm.executeQuery());
			stm = cnx.prepareStatement("select * from client", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			infosResultSet(stm.executeQuery());
			stm = cnx.prepareStatement("select * from client", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			infosResultSet(stm.executeQuery());
			stm = cnx.prepareStatement("select * from client", ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
			infosResultSet(stm.executeQuery());
			stm = cnx.prepareStatement("select * from client", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
			infosResultSet(stm.executeQuery());
			stm = cnx.prepareStatement("select * from client", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			infosResultSet(stm.executeQuery());
			
		} catch (SQLException e) {
			System.out.println("Erreur pendant la connexion: " + e.toString());			
		}
	}
	
	// Méthodes de gestion du pointeur d’enregistrements
	public static void positionRS(ResultSet rs){
		try{
			rs.next();
			System.out.println("Avant toute manipulation, on est au rang " + rs.getRow());
			rs.absolute(3);
			System.out.println("On se déplace au rang " + rs.getRow());
			rs.relative(-1);
			System.out.println("On remonte d'un rang, on est au rang " + rs.getRow());
			rs.beforeFirst();
			rs.next();
			System.out.println("On revient au rang " + rs.getRow());
			rs.afterLast();
			rs.previous();
			System.out.println("On part au dernier rang, le " + rs.getRow());
		} catch (SQLException e) {
			System.out.println("Erreur pendant la connexion: " + e.toString());			
		}
	}
	
	// Méthodes de gestion du pointeur d’enregistrements
	public static void testPositionRS(Connection cnx){
		try{
			PreparedStatement stm = cnx.prepareStatement("select * from client", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			positionRS(stm.executeQuery());
		} catch (SQLException e) {
			System.out.println("Erreur pendant la connexion: " + e.toString());			
		}
	}
	
	// Lecture des données dans un ResultSet
	public static void lectureRS(ResultSet rs, int updateCount){
		try{
			if (updateCount < 0) {
				// Cas du SELECT
				int rowcount = 0;
				if  (rs.last()){
					rowcount = rs.getRow();
					rs.beforeFirst();
				}
				while (rs.next()){
					int colCount = rs.getMetaData().getColumnCount();
					String colonne = "";
					for (int i = 1; i <= colCount ; i++){
						colonne += rs.getString(i) + " ";
					}
					System.out.println(colonne);
				}
				System.out.println(rowcount + " lignes retournées");
			} else {
				// Cas de l'UPDATE
				// L'instruction executeUpdate retourne soit le nombre de lignes, soit 0 si la requête ne retourne rien
				System.out.println("Nombre de lignes: " + updateCount);
			}
		} catch (SQLException e) {
			System.out.println("Erreur pendant la connexion: " + e.toString());			
		}
	}
	
	// Lecture des données dans un ResultSet
	public static void testLectureRS(Connection cnx){
		try{
			PreparedStatement stm = cnx.prepareStatement("select * from produit", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			lectureRS(stm.executeQuery(), stm.getUpdateCount());
		} catch (SQLException e) {
			System.out.println("Erreur pendant la connexion: " + e.toString());			
		}
	}
	
	//
	public static ResultSet modificationTS(ResultSet rs){
		try{
			rs.absolute(4);
			int colCount = rs.getMetaData().getColumnCount();
			String colonne = "Ligne 4: ";
			for (int i = 1; i <= colCount ; i++){
				colonne += rs.getString(i) + " ";
			}			
			Scanner reader = new Scanner(System.in);
			System.out.println(colonne + "\nSaisissez le nom de la colonne à modifier :");
			String nomCol = reader.nextLine();
			System.out.println("\nSaisissez la nouvelle valeur :");
			rs.updateString(nomCol, reader.nextLine());
			rs.updateRow();

			
		} catch (SQLException e) {
			System.out.println("Erreur pendant la connexion: " + e.toString());			
		} finally {
			return rs;
		}
	}
	
	//
	public static void testModificationRS(Connection cnx) {
		try{
			PreparedStatement stm = cnx.prepareStatement("select * from produit", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
			System.out.println("ResultSet Original:");
			lectureRS(stm.executeQuery(), stm.getUpdateCount());
			
			System.out.println("\nResultSet Modifié:");
			lectureRS(modificationTS(stm.executeQuery()), stm.getUpdateCount());
		} catch (SQLException e) {
			System.out.println("Erreur pendant la connexion: " + e.toString());			
		}
	}
	
}
