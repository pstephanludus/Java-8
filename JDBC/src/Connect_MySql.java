import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.Scanner;


public class Connect_MySql {

	public static void main(String[] args) {
		String pilote = "com.mysql.jdbc.Driver";
		
		try {
			Class.forName(pilote);
		} catch (ClassNotFoundException e) {
			System.out.println("Erreur lors du chargement du pilote");
		}
		Connection cnxDirect=null;
		
		// Etablir et manipuler la connexion
		try {
			cnxDirect = DriverManager.getConnection("jdbc:mysql://localhost/comcli?allowMultiQueries=true","root","");
			System.out.println("Connexion effectu�e. Etat de la connexion: " + !cnxDirect.isClosed() + "\nConexion valide: " + cnxDirect.isValid(10));
			cnxDirect.setReadOnly(true);
			System.out.println("La connexion est en readonly: "+ cnxDirect.isReadOnly());
			cnxDirect.close();
			System.out.println("Connexion ferm�e: " + cnxDirect.isClosed());
		} catch (SQLException e) {
			System.out.println("Erreur pendant la connexion");
		}
		
		try
		{
			cnxDirect = DriverManager.getConnection("jdbc:mysql://localhost/comcli?allowMultiQueries=true","root","");
			AffichageWarning(cnxDirect);	// SQLWarnings: les avertissements
			InfoBase(cnxDirect);			// Information sur la base
			//testExecute(cnxDirect);			//  Ex�cution d�instructions de base avec l�objet Statement
			testExecuteBatch(cnxDirect);		//  Ex�cution d�instructions par lot
			
		} catch (SQLException e) {
			System.out.println("Erreur pendant la connexion");
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
			System.out.println("Erreur pendant la connexion");
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
								+ "\nproc�dures stock�es: " + proceduresStockees);
		} catch (SQLException e) {
			System.out.println("Erreur pendant la connexion");
		}
	}
	
	// Ex�cution d�instructions de base avec l�objet Statement
	public static void testExecute(Connection cnx){
		try{
			Scanner reader = new Scanner(System.in);
			System.out.println("Saisissez la requ�te � effectuer, ou 'TEST' pour afficher les r�sultats de requ�tes test pr�faites:");
			String reputil = reader.nextLine();
			Statement req;
			if (reputil.toLowerCase().equals("test")){
				System.out.println("\nRequ�te SELECT * FROM client:");
				req = cnx.createStatement();
				traitementRequete(req, "SELECT * FROM client");
				req.close();
				
				System.out.println("\nRequ�te UPDATE client SET NCLI = 'B063' WHERE NCLI = 'B062'");
				req = cnx.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
				traitementRequete(req, "UPDATE client SET NCLI = 'B063' WHERE NCLI = 'B062'");
				req.close();
			} else {
				req = cnx.createStatement();
				traitementRequete(req, reputil);
			}
		} catch (SQLException e) {
			System.out.println("Erreur pendant la connexion");
		}
	}
	
	public static void traitementRequete(Statement req, String sql){
		try{
			req.execute(sql);
			ResultSet RS = req.getResultSet();
			System.out.println("R�sultat de la requ�te " + sql + ":");
			if (req.getUpdateCount() < 0) {
				// Cas du SELECT
				int rowcount = 0;
				if  (RS.last()){
					rowcount = RS.getRow();
					RS.beforeFirst();
				}
				System.out.println(rowcount);
				while (RS.next()){
					int colCount = RS.getMetaData().getColumnCount();
					String colonne = "";
					for (int i = 1; i <= colCount ; i++){
						colonne += RS.getString(i) + " ";
					}
					System.out.println(colonne);
				}
				System.out.println(rowcount + " lignes retourn�es");
			} else {
				// Cas de l'UPDATE
				// L'instruction executeUpdate retourne soit le nombre de lignes, soit 0 si la requ�te ne retourne rien
				System.out.println("Nombre de lignes: " + req.getUpdateCount());
			}
			
			
		} catch (SQLException e) {
			System.out.println("Erreur pendant la connexion");
		}
	}

	public static void testExecuteBatch(Connection cnx){
		try{			
			// Saisies utilisateur
			Scanner reader = new Scanner(System.in);
			Statement req = cnx.createStatement();
			String reputil = "";
			System.out.println("Saisissez les requ�tes � effectuer, ou 'run' pour les ex�cuter:");
			while (!reputil.toLowerCase().equals("run")) {
				 reputil = reader.nextLine();
				 if (!reputil.toLowerCase().equals("run")) {
					 req.addBatch(reputil);
				 }
			}
			
			// Affichage du retour de la requ�te
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
				System.out.println("Requ�te " + temp + ": " + retour);
			}
		} catch (SQLException e) {
			System.out.println("Erreur pendant la connexion");
		} finally {
			System.out.println("Requ�tes termin�es");
		}
		
	}
	
	public static void testExecuteMultiple(){
		try{
			
			
			
		} catch (SQLException e) {
			System.out.println("Erreur pendant la connexion");			
		} catch (IOException e) {
			
		}
	}
	
	
}
