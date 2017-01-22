import java.util.Calendar;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.config.EmbeddedConfiguration;


/**
 * Affiche les articles et les factures de la BD.
 */
public class AfficheBD {

  public static void main(String[] args) {
    ObjectContainer bd = null;
    try {
	  EmbeddedConfiguration configuration = Db4oEmbedded.newConfiguration();
      configuration.common().objectClass(Calendar.class).callConstructor(true);
      bd = Db4oEmbedded.openFile(configuration, "Facture");
      System.out.println("Les articles :");
      ObjectSet<Article> articles = bd.query(Article.class);
      int n = 0;
      for (Article article : articles) {
        System.out.println("Article " + ++n + " :");
        System.out.println(article);
      }
      ObjectSet<Facture> factures = bd.query(Facture.class);
      n = 0;
      System.out.println("Les factures :");
      for (Facture facture : factures) {
        System.out.println("Facture " + ++n + " :");
        System.out.println(facture);
      }
    }
    finally {
      if (bd != null) {
        bd.close();
      }
    }
  }
}