

import java.util.Calendar;

import com.db4o.Db4o;
import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.config.EmbeddedConfiguration;

/**
 * Gestion des identités et requêtes natives complexes.
 */
public class Test_7 {

  public static void main(String[] args) {
    EmbeddedConfiguration configuration = Db4oEmbedded.newConfiguration();
    configuration.common().objectClass(Calendar.class).callConstructor(true);
    configuration.common().objectClass(Facture.class).minimumActivationDepth(6);
    ObjectContainer bd = null;
    try {
      // Création des articles de la facture qui ne sont pas déjà
      // dans la BD.
      // On écrit le code pour récupérer les articles qui sont
      // déjà dans la BD.
      bd = Db4oEmbedded.openFile(configuration, "Facture");
      // Les objets ramenés par get sont activés (pris en charge
      // par l'ObjectContainer).
      ObjectSet<Stylo> stylos = 
        bd.queryByExample(new Stylo("S001", null, null, null));
      Stylo stylo = null;
      if (stylos.hasNext()) {
        stylo = stylos.next();
      }
      else {
        stylo = new Stylo("S001", "Stylo noir Marker", 40, "noir");
      }
      ObjectSet<Ramette> ramettes = 
        bd.queryByExample(new Ramette("R54", null, 0, 0));
      Ramette ramette = null;
      if (ramettes.hasNext()) {
        ramette = ramettes.next();
      }
      else {
        ramette = new Ramette("R54", "Ramette vert", 10, 80);
      }
      // Création de la facture
      Calendar date = Calendar.getInstance();
      date.set(2008, 9, 5);
      Facture facture = 
        new Facture("08/5700", "Pierre François", date);
      facture.addLigne(stylo, 7);
      facture.addLigne(ramette, 20);
      System.out.println(facture);
      bd.store(facture);
      bd.commit();
    }
    finally {
      if (bd != null) {
        bd.close();
      }
    }
  }
}