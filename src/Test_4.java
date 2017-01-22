
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
      // R�cup�re la facture (par d�faut la profondeur est 5).
      ObjectSet<Facture> factures = 
        bd.queryByExample(new Facture(null, null, null));
      System.out.println("Nb factures trouv�es : " + factures.size());
      Facture facture = null;
      if (! factures.hasNext()) {
        System.out.println("La facture cherch�e n'est pas dans la base de donn�es");
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
          System.out.println("ligne trouv�e");
          ligne.setNb(ligne.getNb() + 1);
          break;
        }
      }
      // Ne modifie que l'en-t�te de la facture !
      bd.store(facture);
      bd.commit();
      System.out.println("Facture apr�s la modification, en m�moire centrale :");
      System.out.println(facture);
      // R�cup�re la facture dans la BD pour voir
      // si la modification a bien �t� faite
      factures = bd.queryByExample(new Facture("08/5674", null, null));
      facture = factures.next();
      // Affiche la facture pour voir.
      // Il semble que la modification a bien eu lieu...
      System.out.println("Facture apr�s la modification, retrouv�e par une rercherche par l'exemple, avant de fermer la base :");
      System.out.println(facture);
    }
    finally {
      if (bd != null) {
        bd.close();
      }
    }
    // V�rifions encore une fois...
    try {
      EmbeddedConfiguration configuration = Db4oEmbedded.newConfiguration();
      configuration.common().objectClass(Calendar.class).callConstructor(true);
      configuration.common().objectClass(Facture.class).minimumActivationDepth(6);
      bd = Db4oEmbedded.openFile(configuration, "Facture");
      ObjectSet<Facture> factures = 
        bd.queryByExample(new Facture("08/5674", null, null));
      Facture facture = factures.next();
      // Affiche la facture pour voir... que la modification n'a pas �t� faite !
      System.out.println("Facture trouv�e par une rercherche par l'exemple, apr�s avoir ferm� la base :");
      System.out.println(facture);
      /* 
       * Explication : lors de ce get, la ligne de facture en m�moire modifi�e n'est
       * pas connue du nouvel ObjectContainer. Celui-ci doit donc aller
       * chercher la ligne dans la BD. 
       * Pour le get d'avant (juste apr�s la modification), l'ObjectContainer
       * connaissait la ligne en m�moire modifi�e car elle avait �t� r�cup�r�e
       * par lui juste avant. C'est cette ligne modifi�e en m�moire que le get
       * renvoie et on croit donc qu'elle a �t� modifi�e dans la BD.
       * En fait, la profondeur de modification est de 1 par d�faut et donc
       * le set de l'en-t�te de la facture n'a pas enregistr� la modification
       * de la ligne.
       * On va tester tout ceci ci-dessous en recommen�ant la m�me manipulation
       * mais avec une profondeur modification de 2.
       */
    }
    finally {
      if (bd != null) {
        bd.close();
      }
    }
    // Modifie la profondeur de modification pour la classe Facture
    // une autre fa�on de faire plus souple serait de fixer 
    // la profondeur de modification au moment du store
    // (voir ci-dessous).
    EmbeddedConfiguration configuration = Db4oEmbedded.newConfiguration();
    configuration.common().objectClass(Facture.class).minimumActivationDepth(6);
    configuration.common().objectClass(Calendar.class).callConstructor(true);
    configuration.common().objectClass(Facture.class).updateDepth(3);
    try {
      bd = Db4oEmbedded.openFile(configuration, "Facture");
      // R�cup�re la facture (par d�faut la profondeur est 5).
      ObjectSet<Facture> factures = 
        bd.queryByExample(new Facture("08/5674", null, null));
      Facture facture = factures.next();
      // Affiche la facture pour voir
      System.out.println("Facture retrouv�e :");
      System.out.println(facture);
      // Modifie la ligne de la facture
      // Il faut d'abord retrouver la bonne ligne.
      List<Facture.LigneFacture> lignesFacture = facture.getLignes();
      for (Facture.LigneFacture ligne : lignesFacture) {
        if (ligne.getArticle().getReference().equals("S001")) {
          System.out.println("ligne trouv�e");
          ligne.setNb(ligne.getNb() + 1);
          break;
        }
      }
      // Il aurait aussi �t� possible de modifier ponctuellement
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
    // V�rifions encore une fois...
    try {
      configuration = Db4oEmbedded.newConfiguration();
      configuration.common().objectClass(Facture.class).minimumActivationDepth(6);
      configuration.common().objectClass(Calendar.class).callConstructor(true);
      bd = Db4oEmbedded.openFile(configuration, "Facture");
      ObjectSet<Facture> factures = 
        bd.queryByExample(new Facture("08/5674", null, null));
      Facture facture = factures.next();
      // Affiche la facture pour voir... que la modification a �t� faite !
      System.out.println("Apr�s configuration de la profondeur de modification :");
      System.out.println(facture);
    }
    finally {
      if (bd != null) {
        bd.close();
      }
    }

  }
}
