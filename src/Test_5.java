

import java.util.Calendar;

import com.db4o.Db4o;
import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.config.EmbeddedConfiguration;

public class Test_5 {

  public static void main(String[] args) {
    // Met la profondeur d'activation des factures à 2
    // ce qui permettra de récupérer les en-têtes de facture
    // et les lignes de la facture mais pas les produits.
    // (par défaut la profondeur est 5).
    ObjectContainer bd = null;
    try {
      EmbeddedConfiguration configuration = Db4oEmbedded.newConfiguration();
      configuration.common().objectClass(Calendar.class).callConstructor(true);
      configuration.common().objectClass(Facture.class).maximumActivationDepth(5);
      bd = Db4oEmbedded.openFile(configuration, "Facture");
      // Récupère la facture).
      ObjectSet<Facture> factures = 
        bd.queryByExample(new Facture("08/5674", null, null));
      Facture facture = factures.next();
      // Affiche la facture pour voir.
      // Lève une NullPointerException car db4o n'ayant pas chargé
      // les articles en mémoire, a aussi mis les références des articles
      // à null dans les lignes de facture. On peut le vérifier
      // simplement en changeant légèrement la méthode de
      // toString() de Facture ou Lignefacture en prenant en compte
      // le fait que des références à un article peuvent être null.
      // Si on fait passer la profondeur d'activation à 4, on n'a plus
      // l'exception mais le prix total de la facture est calculée à 0.
      // On mettant la profondeur d'activation à 5 on récupère les
      // prix des articles et le prix total affiché est correct.
      System.out.println(facture);
    }
    finally {
      if (bd != null) {
        bd.close();
      }
    }

  }

}