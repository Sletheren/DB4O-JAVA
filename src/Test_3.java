import java.io.File;
import java.math.BigDecimal;
import java.util.Calendar;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.config.EmbeddedConfiguration;

/**
 * Cr�ation et sauvegarde d'une facture
 */
public class Test_3 {

  public static void main(String[] args) {
    // Cr�ation des articles de la facture
    Stylo stylo = new Stylo("S001", "Stylo noir Marker", 40, "noir");
    Ramette ramette = new Ramette("R987", "Ramette Laser", 10, 80);
    Lot lot = new Lot("L67", "Lot �criture", 20);
    lot.add(stylo, 10);
    lot.add(ramette, 10);    
    // Cr�ation de la facture
    Calendar date = Calendar.getInstance();
    date.set(2008, 8, 25);
    Facture facture = 
      new Facture("08/5674", "Roger Martin", date);
    facture.addLigne(stylo, 8);
    facture.addLigne(ramette, 5);
    facture.addLigne(lot, 2);
    System.out.println(facture);
    
    // Sauvegarde de la facture dans la base de donn�es
    // Remet la base � 0
    // new File("facture.data").delete();
    // Configure db4o pour qu'il sauvegarde les champs transient.
    // Sinon, le prix sera de 0 dans la base et l'appel d'une m�thode 
    // de Calendar r�cup�r� dans la base provoque une NullPointerException.
    // D� au fait que BigDecimal et Calendar ont des champs transient
    // (voir doc de db4o pour plus de d�tails).
	// Avec la version 7.12 et le JDK 6 il semble que BigDecimal ne pose
	// plus de probl�me. A v�rifier sur votre machine...
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