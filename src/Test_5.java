

import java.util.Calendar;

import com.db4o.Db4o;
import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.config.EmbeddedConfiguration;

public class Test_5 {

  public static void main(String[] args) {
    // Met la profondeur d'activation des factures � 2
    // ce qui permettra de r�cup�rer les en-t�tes de facture
    // et les lignes de la facture mais pas les produits.
    // (par d�faut la profondeur est 5).
    ObjectContainer bd = null;
    try {
      EmbeddedConfiguration configuration = Db4oEmbedded.newConfiguration();
      configuration.common().objectClass(Calendar.class).callConstructor(true);
      configuration.common().objectClass(Facture.class).maximumActivationDepth(5);
      bd = Db4oEmbedded.openFile(configuration, "Facture");
      // R�cup�re la facture).
      ObjectSet<Facture> factures = 
        bd.queryByExample(new Facture("08/5674", null, null));
      Facture facture = factures.next();
      // Affiche la facture pour voir.
      // L�ve une NullPointerException car db4o n'ayant pas charg�
      // les articles en m�moire, a aussi mis les r�f�rences des articles
      // � null dans les lignes de facture. On peut le v�rifier
      // simplement en changeant l�g�rement la m�thode de
      // toString() de Facture ou Lignefacture en prenant en compte
      // le fait que des r�f�rences � un article peuvent �tre null.
      // Si on fait passer la profondeur d'activation � 4, on n'a plus
      // l'exception mais le prix total de la facture est calcul�e � 0.
      // On mettant la profondeur d'activation � 5 on r�cup�re les
      // prix des articles et le prix total affich� est correct.
      System.out.println(facture);
    }
    finally {
      if (bd != null) {
        bd.close();
      }
    }

  }

}