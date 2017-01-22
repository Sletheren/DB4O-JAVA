
import java.util.Calendar;
import java.util.List;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.config.EmbeddedConfiguration;
import com.db4o.ext.ExtObjectContainer;


/**
 * Modification du nombre de produit d'une facture.
 */
public class Test_4 {

  public static void main(String[] args) {
    ObjectContainer bd = null;
    try {
      EmbeddedConfiguration configuration = Db4oEmbedded.newConfiguration();
      configuration.common().objectClass(Calendar.class).callConstructor(true);
      configuration.common().objectClass(Facture.class).minimumActivationDepth(7);
      bd = Db4oEmbedded.openFile(configuration, "Facture");
      // Récupère la facture (par défaut la profondeur est 5).
      ObjectSet<Facture> factures = 
        bd.queryByExample(new Facture(null, null, null));
      System.out.println("Nb factures trouvées : " + factures.size());
      Facture facture = null;
      if (! factures.hasNext()) {
        System.out.println("La facture cherchée n'est pas dans la base de données");
        return;
      }
      facture = factures.next();
      // Affiche la facture pour voir
      System.out.println("Facture avant la modification :");
      System.out.println(facture);
      // Modifie la ligne de la facture
      // Il faut d'abord retrouver la bonne ligne.
      List<Facture.LigneFacture> lignesFacture = facture.getLignes();
      for (Facture.LigneFacture ligne : lignesFacture) {
        if (ligne.getArticle().getReference().equals("S001")) {
          System.out.println("ligne trouvée");
          ligne.setNb(ligne.getNb() + 1);
          break;
        }
      }
      // Ne modifie que l'en-tête de la facture !
      bd.store(facture);
      bd.commit();
      System.out.println("Facture après la modification, en mémoire centrale :");
      System.out.println(facture);
      // Récupère la facture dans la BD pour voir
      // si la modification a bien été faite
      factures = bd.queryByExample(new Facture("08/5674", null, null));
      facture = factures.next();
      // Affiche la facture pour voir.
      // Il semble que la modification a bien eu lieu...
      System.out.println("Facture après la modification, retrouvée par une rercherche par l'exemple, avant de fermer la base :");
      System.out.println(facture);
    }
    finally {
      if (bd != null) {
        bd.close();
      }
    }
    // Vérifions encore une fois...
    try {
      EmbeddedConfiguration configuration = Db4oEmbedded.newConfiguration();
      configuration.common().objectClass(Calendar.class).callConstructor(true);
      configuration.common().objectClass(Facture.class).minimumActivationDepth(6);
      bd = Db4oEmbedded.openFile(configuration, "Facture");
      ObjectSet<Facture> factures = 
        bd.queryByExample(new Facture("08/5674", null, null));
      Facture facture = factures.next();
      // Affiche la facture pour voir... que la modification n'a pas été faite !
      System.out.println("Facture trouvée par une rercherche par l'exemple, après avoir fermé la base :");
      System.out.println(facture);
      /* 
       * Explication : lors de ce get, la ligne de facture en mémoire modifiée n'est
       * pas connue du nouvel ObjectContainer. Celui-ci doit donc aller
       * chercher la ligne dans la BD. 
       * Pour le get d'avant (juste après la modification), l'ObjectContainer
       * connaissait la ligne en mémoire modifiée car elle avait été récupérée
       * par lui juste avant. C'est cette ligne modifiée en mémoire que le get
       * renvoie et on croit donc qu'elle a été modifiée dans la BD.
       * En fait, la profondeur de modification est de 1 par défaut et donc
       * le set de l'en-tête de la facture n'a pas enregistré la modification
       * de la ligne.
       * On va tester tout ceci ci-dessous en recommençant la même manipulation
       * mais avec une profondeur modification de 2.
       */
    }
    finally {
      if (bd != null) {
        bd.close();
      }
    }
    // Modifie la profondeur de modification pour la classe Facture
    // une autre façon de faire plus souple serait de fixer 
    // la profondeur de modification au moment du store
    // (voir ci-dessous).
    EmbeddedConfiguration configuration = Db4oEmbedded.newConfiguration();
    configuration.common().objectClass(Facture.class).minimumActivationDepth(6);
    configuration.common().objectClass(Calendar.class).callConstructor(true);
    configuration.common().objectClass(Facture.class).updateDepth(3);
    try {
      bd = Db4oEmbedded.openFile(configuration, "Facture");
      // Récupère la facture (par défaut la profondeur est 5).
      ObjectSet<Facture> factures = 
        bd.queryByExample(new Facture("08/5674", null, null));
      Facture facture = factures.next();
      // Affiche la facture pour voir
      System.out.println("Facture retrouvée :");
      System.out.println(facture);
      // Modifie la ligne de la facture
      // Il faut d'abord retrouver la bonne ligne.
      List<Facture.LigneFacture> lignesFacture = facture.getLignes();
      for (Facture.LigneFacture ligne : lignesFacture) {
        if (ligne.getArticle().getReference().equals("S001")) {
          System.out.println("ligne trouvée");
          ligne.setNb(ligne.getNb() + 1);
          break;
        }
      }
      // Il aurait aussi été possible de modifier ponctuellement
      // ici la profondeur de modification :
//       ((ExtObjectContainer)bd).store(facture, 3);
      bd.store(facture);
      bd.commit();
    }
    finally {
      if (bd != null) {
        bd.close();
      }
    }
    // Vérifions encore une fois...
    try {
      configuration = Db4oEmbedded.newConfiguration();
      configuration.common().objectClass(Facture.class).minimumActivationDepth(6);
      configuration.common().objectClass(Calendar.class).callConstructor(true);
      bd = Db4oEmbedded.openFile(configuration, "Facture");
      ObjectSet<Facture> factures = 
        bd.queryByExample(new Facture("08/5674", null, null));
      Facture facture = factures.next();
      // Affiche la facture pour voir... que la modification a été faite !
      System.out.println("Après configuration de la profondeur de modification :");
      System.out.println(facture);
    }
    finally {
      if (bd != null) {
        bd.close();
      }
    }

  }
}
