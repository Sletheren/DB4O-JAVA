import java.util.List;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;

class Naissance{	
	private int jour;
	private int mois;
	private int annee;
	private String Lieu;
	
	public int getJour() {
		return jour;
	}
	public void setJour(int jour) {
		this.jour = jour;
	}
	public int getMois() {
		return mois;
	}
	public void setMois(int mois) {
		this.mois = mois;
	}
	public int getAnnee() {
		return annee;
	}
	public void setAnnee(int annee) {
		this.annee = annee;
	}
	public String getLieu() {
		return Lieu;
	}
	public void setLieu(String lieu) {
		Lieu = lieu;
	}
	public Naissance() {
		super();
	}
	public Naissance(int jour, int mois, int annee, String lieu) {
		super();
		this.jour = jour;
		this.mois = mois;
		this.annee = annee;
		Lieu = lieu;
	}
	
	@Override
	public String toString() {
		return "Naissance [jour=" + jour + ", mois=" + mois + ", annee=" + annee + ", Lieu=" + Lieu + "]";
	}
}

class Addresse {
	
	private int Numero;
	private String quartier;
	public int getNumero() {
		return Numero;
	}
	public void setNumero(int numero) {
		Numero = numero;
	}
	public String getQuartier() {
		return quartier;
	}
	public void setQuartier(String quartier) {
		this.quartier = quartier;
	}
	public Addresse(int numero, String quartier) {
		super();
		Numero = numero;
		this.quartier = quartier;
	}
	public Addresse() {
		super();
	}
	@Override
	public String toString() {
		return "Addresse [Numero=" + Numero + ", quartier=" + quartier + "]";
	}
}
	
	class Personne {
		
		private String nom,prenom;
		private Addresse addresse;
		private Naissance naissance;
		public String getNom() {
			return nom;
		}
		public void setNom(String nom) {
			this.nom = nom;
		}
		public String getPrenom() {
			return prenom;
		}
		public void setPrenom(String prenom) {
			this.prenom = prenom;
		}
		public Addresse getAddresse() {
			return addresse;
		}
		public void setAddresse(Addresse addresse) {
			this.addresse = addresse;
		}
		public Naissance getNaissance() {
			return naissance;
		}
		public void setNaissance(Naissance naissance) {
			this.naissance = naissance;
		}
		public Personne() {
			super();
		}
		public Personne(String nom, String prenom, Addresse addresse, Naissance naissance) {
			super();
			this.nom = nom;
			this.prenom = prenom;
			this.addresse = addresse;
			this.naissance = naissance;
		}
		@Override
		public String toString() {
			return "Personne [nom=" + nom + ", prenom=" + prenom + ", addresse=" + addresse + ", naissance=" + naissance
					+ "]";
		}
		
		
		
		
	}
	
	

public class Test_1 {
	
	public static void listResult(List<?> results){
		
		System.out.println("Nombre d'enregistrement est : "+results.size());

		for (Object object : results) {
			System.out.println(object.toString());
		}
	}

	public static void main(String[] args) {
		
		ObjectContainer db = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(),"B:/eclipse/workspace/DB4OTest/Personne");
		
		try{
			Naissance n1= new Naissance(20, 1, 1993, "laayoune");
			Addresse a1 = new Addresse(38, "HAY SAADA");
			Personne p1 = new Personne("rakhma","abdelghafour", a1, n1);
			db.store(n1);
			db.store(a1);
			db.store(p1);
			
			Naissance n2= new Naissance(20, 2, 1992, "Laayoune");
			Addresse a2 = new Addresse(50, "AL MASSIRA");
			Personne p2 = new Personne("delai","faycal", a2, n2);
			db.store(n2);
			db.store(a2);
			db.store(p2);
			
			
			//Affichage :
			
			Personne p = new Personne();
			ObjectSet results = db.queryByExample(p);
			listResult(results);
			
			
			//recherche par nom
			
			Personne pp1 = new Personne("Test", null, null, null);
			ObjectSet results2 = db.queryByExample(pp1);
			listResult(results2);
			
		}finally{
			db.close();
		}
	}

}
