
import java.util.List;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.query.Predicate;

/**
 * Modification de données et requêtes natives.
 */
class Personne2 {
	
	private String nom,prenom;
	private int age;
	public Personne2(){
		
	}
	public Personne2(String nom, String prenom, int age) {
		super();
		this.nom = nom;
		this.prenom = prenom;
		this.age = age;
	}
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
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	@Override
	public String toString() {
		return "Personne2 [nom=" + nom + ", prenom=" + prenom + ", age=" + age + "]";
	}
	
	
	
}
public class Test_2 {

  public static void main(String[] args) {
    // Changer l'âge de Dupond. Il faut commencer par le retrouver dans la base.
    ObjectContainer bd = null;
    try {
      bd = Db4oEmbedded.openFile("Personne2");
      Personne2 p = new Personne2("Dupond", "Pierre", 0);
      bd.store(p);
      // Le critère de recherche ne concerne pas l'âge car la valeur est 0.
      ObjectSet<Personne2> personnes = bd.queryByExample(p);
      Personne2 dupond = personnes.next();
      dupond.setAge(38);
      bd.store(dupond);
      bd.commit();
      // Retrouver toutes les personnes de plus de 30 ans par une requête native.
      List<Personne2> liste =
        bd.query(new Predicate<Personne2>() {
          @Override
          public boolean match(Personne2 candidat) {
            return candidat.getAge() > 30;
          }
        });
      // Afficher le résultat de la requête
      for (Personne2 personne : liste) {
        System.out.println(personne);
      }
    }
    finally {
      if (bd != null) {
        bd.close();
      }
    }
  }
}
