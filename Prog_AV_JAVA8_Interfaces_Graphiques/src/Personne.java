import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
 
public class Personne implements Classable{
 
    private String nom;
    private String prenom;
    private LocalDate date_naiss;
 
   
    public Personne()
    {
        this.nom = null;
        this.prenom = null;
        this.date_naiss = null;
    }
   
    public Personne(String nom, String prenom, LocalDate date_naiss)
    {
        this.nom = nom;
        this.prenom = prenom;
        this.date_naiss = date_naiss;
    }
      
    public long calculAge()
    {
		return date_naiss.until(LocalDate.now(), ChronoUnit.YEARS);
    }
   
    public void affichage()
    {
       
            System.out.println("nom :" + this.nom);
            System.out.println("prenom :" + this.prenom);
            System.out.println("age :" + calculAge());
    }
   
    public void affichage(boolean francais)
    {
        if(francais)
        {
            System.out.println("nom :" + this.nom);
            System.out.println("prenom :" + this.prenom);
            System.out.println("age :" + calculAge());
        }
        else
        {
            System.out.println("Name :" + this.nom);
            System.out.println("First name :" + this.prenom);
            System.out.println("age :" + calculAge());
        }
    }
   
    public String getNom(){
        return this.nom;
    }
   
    public String getPrenom(){
        return this.prenom;
    }
    public LocalDate getDateNaissance(){
    return this.date_naiss;
    }
   
    public void setNom(String n){
        this.nom = n;
    }
   
    public void setPrenom(String n){
        this.prenom = n;
    }
   
    public void setDateNaissance(LocalDate d){
        this.date_naiss = d;
    }
 
   
    public static void main(String[] args)
    {
        Personne anon = new Personne();
       
        anon.setNom("Non");
        anon.setPrenom("Ja");
       
       
    }

	@Override
	public int compareTo(Object obj) {
		if (! this.nom.equals(((Personne)obj).getNom())){
			return this.nom.compareTo(((Personne)obj).getNom());
		} else {
			return this.prenom.compareTo(((Personne)obj).getPrenom());
		}
	}
	
	@Override
	public String toString(){
		return this.getPrenom() + " " + this.getNom() ; //+ " (" + this.getDateNaissance() + ")";
	}
   
	public String nomPrenom(){
		return this.getNom() + " " + this.getPrenom();
	}
}