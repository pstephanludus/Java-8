
public interface Classable extends Comparable<Object>{
	
	public static final int INFERIEUR=-1;
	public static final int EGAL=0;
	public static final int SUPERIEUR=1;
	public static final int ERREUR=-99;

	default int compare(Object obj){
		if (this.compareTo(obj) < 0) {
			return INFERIEUR;
		} else if (this.compareTo(obj) == 0) {
			return EGAL;
		} else if (this.compareTo(obj) > 0) {
			return SUPERIEUR;
		} else {
			return ERREUR;
		}
	}
	
	public static void tri(Classable[] tablo){
		int i,j;
		Classable c;
		for(i=0;i<tablo.length;i++){
			for(j = i + 1; j < tablo.length; j++){
				if(tablo[j].compare(tablo[i])==Classable.INFERIEUR){
					c = tablo[j];
					tablo[j] = tablo[i];
					tablo[i] = c;
				} else if (tablo[j].compare(tablo[i])==Classable.ERREUR){
				}
			}
		}
	}
	
	
}
