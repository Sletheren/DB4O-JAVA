

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.config.EmbeddedConfiguration;

/**
 * Supprimer tous les articles concern�s par une facture.
 */
public class Test_6 {

  public static void main(String[] args) {
    ObjectContainer bd = null;
    try {
      // R�cup�re la facture num�ro 08/5674
      EmbeddedConfiguration configuration = Db4oEmbedded.newConfiguration();
      configuration.common().objectClass(Calendar.class).callConstructor(true);
      configuration.common().objectClass(Facture.class).minimumActivationDepth(6);
      bd = Db4oEmbedded.openFile(configuration, "Facture");
      ObjectSet<Facture> factures = 
        bd.queryByExample(new Facture("08/5674", null, null));
      Facture facture = factures.next();
      // R�cup�re tous les articles concern�s par la facture
      List<Facture.LigneFacture> lignes = facture.getLignes();
      List<Article> articles = new ArrayList<Article>();
      for (Facture.LigneFacture ligne : lignes) {
        articles.add(ligne.getArticle());
      }
      // Supprime dans la BD tous les articles de la facture.
      // Ca ne serait pas possible avec un SGBDR � cause des contraintes d'int�grit�.
      for (Article article : articles) {
        bd.delete(article);
      }
      bd.commit();
    }
    finally {
      if (bd != null) {
        bd.close();
      }
    }
    try {
      EmbeddedConfiguration configuration = Db4oEmbedded.newConfiguration();
      configuration.common().objectClass(Calendar.class).callConstructor(true);
      configuration.common().objectClass(Facture.class).minimumActivationDepth(6);
      bd = Db4oEmbedded.openFile(configuration, "Facture");
      // R�cup�re la facture (par d�faut la profondeur est 5).
      ObjectSet<Facture> factures = 
        bd.queryByExample(new Facture("08/5674", null, null));
      Facture facture = factures.next();
      // Affiche la facture pour voir
      // Provoque une NullPointerException.
      System.out.println(facture);
    }
    finally {
      if (bd != null) {
        bd.close();
      }
    }
  }
}