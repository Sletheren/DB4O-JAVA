

import java.util.Calendar;
import java.util.List;

import com.db4o.Db4o;
import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.config.Configuration;
import com.db4o.config.EmbeddedConfiguration;
import com.db4o.diagnostic.DiagnosticToConsole;
import com.db4o.internal.ObjectContainerBase;
import com.db4o.internal.query.Db4oQueryExecutionListener;
import com.db4o.internal.query.NQOptimizationInfo;
import com.db4o.query.Predicate;
/**
 * Recherche des factures qui contiennent l'article de référence S001.
 */
public class Test_8 {

  public static void main(String[] args) {
    ObjectContainer bd = null;
    // Recherche avec une interrogation par l'exemple.
    // Il faut construire une pseudo-facture qui a le stylo dans
    // une ligne de facture. On triche un peu car on sait que l'article
    // de référence S001 est un stylo.
    try {
      EmbeddedConfiguration configuration = Db4oEmbedded.newConfiguration();
      configuration.common().objectClass(Calendar.class).callConstructor(true);
      configuration.common().objectClass(Facture.class).minimumActivationDepth(6);
      bd = Db4oEmbedded.openFile(configuration, "Facture");
      Facture fact = new Facture(null, null, null);
      fact.addLigne(new Stylo("S001", null, null, null), 0);
      ObjectSet<Facture> facts = bd.queryByExample(fact);
      System.out.println("Les factures qui contiennent S001 :");
      System.out.println("Recherche par l'exemple :");
      for (Facture facture : facts) {
        System.out.println(facture);
      }
      System.out.println("Fin de la recherche par l'exemple....");
    }
    finally {
      if (bd != null) {
        bd.close();
      }

      // Recherche avec une requête native.
      // Ajoute un écouteur pour avoir des informations sur le fonctionnement
      // de db4o, et en particulier pour savoir si la requête native est optimisée.
      EmbeddedConfiguration configuration = Db4oEmbedded.newConfiguration();
      configuration.common().objectClass(Calendar.class).callConstructor(true);
      configuration.common().objectClass(Facture.class).minimumActivationDepth(6);
      configuration.common().diagnostic().addListener(new DiagnosticToConsole());
      try {
        bd = Db4oEmbedded.openFile(configuration, "Facture");
        // Ajoute un écouteur d'exécution de query. Il fait double emploi
        // avec le l'écouteur de diagnostic ci-dessus ; il cible mieux 
        // les informations à afficher.
        // On voit que la requête ne peut être optimisée (trop complexe).
        ((ObjectContainerBase)bd).getNativeQueryHandler().addListener(
            new Db4oQueryExecutionListener() {
              public void notifyQueryExecuted(NQOptimizationInfo info) {
                System.out.println(info.message());
              }
            });

        List<Facture> factures = bd.query(new Predicate<Facture>() {
          @Override
          /**
           * Renvoie vrai si la facture contient l'article de référence S001.
           */
          public boolean match(Facture candidate) {
            List<Facture.LigneFacture> lignes = candidate.getLignes();
            for (Facture.LigneFacture ligne : lignes) {
              if (ligne.getArticle().getReference().equals("S001")) {
                return true;
              }
            }
            return false;
          }
        });
        System.out.println("Les factures qui contiennent S001 :");
        System.out.println("Recherche native :");
        for (Facture facture : factures) {
          System.out.println(facture);
        }
      } finally {
        if (bd != null) {
          bd.close();
        }
      }
    }
  }
}