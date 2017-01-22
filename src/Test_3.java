import java.io.File;
import java.math.BigDecimal;
import java.util.Calendar;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.config.EmbeddedConfiguration;

/**
 * Création et sauvegarde d'une facture
 */
public class Test_3 {

  public static void main(String[] args) {
    // Création des articles de la facture
    Stylo stylo = new Stylo("S001", "Stylo noir Marker", 40, "noir");
    Ramette ramette = new Ramette("R987", "Ramette Laser", 10, 80);
    Lot lot = new Lot("L67", "Lot écriture", 20);
    lot.add(stylo, 10);
    lot.add(ramette, 10);    
    // Création de la facture
    Calendar date = Calendar.getInstance();
    date.set(2008, 8, 25);
    Facture facture = 
      new Facture("08/5674", "Roger Martin", date);
    facture.addLigne(stylo, 8);
    facture.addLigne(ramette, 5);
    facture.addLigne(lot, 2);
    System.out.println(facture);
    
    // Sauvegarde de la facture dans la base de données
    // Remet la base à 0
    // new File("facture.data").delete();
    // Configure db4o pour qu'il sauvegarde les champs transient.
    // Sinon, le prix sera de 0 dans la base et l'appel d'une méthode 
    // de Calendar récupéré dans la base provoque une NullPointerException.
    // Dû au fait que BigDecimal et Calendar ont des champs transient
    // (voir doc de db4o pour plus de détails).
	// Avec la version 7.12 et le JDK 6 il semble que BigDecimal ne pose
	// plus de problème. A vérifier sur votre machine...
    EmbeddedConfiguration configuration = Db4oEmbedded.newConfiguration();
    // configuration.objectClass(BigDecimal.class).storeTransientFields(true);
    // configuration.objectClass(Calendar.class).storeTransientFields(true);
    ObjectContainer bd = null;
    try {
	  bd = Db4oEmbedded.openFile(configuration, "Facture");
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