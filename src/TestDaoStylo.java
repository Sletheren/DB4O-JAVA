import java.io.File;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;

import com.db4o.Db4o;
import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.config.Configuration;
import com.db4o.config.EmbeddedConfiguration;

/**
 * Test de DaoStylo pour Db4o.
 */
public class TestDaoStylo {

  public static void main(String[] args)  {
    // Réinitialise la base de données.
    new File("Dao").delete();
    DaoStylo daoStylo = DaoStylo.getDao();
    Stylo stylo = new Stylo("S34", "Petit Stylo", 190, "violet");
    ObjectContainer c = null;
    try {
      c = Db4oEmbedded.openFile("Dao");
      System.out.println("valeur de c = " + c);
      daoStylo.setConnexion(c);
      daoStylo.insert(stylo);
      daoStylo.insert(new Stylo("S35", "Stylo feutre", 40, "rouge"));
      c.commit();
      Stylo st = daoStylo.findByExample(stylo).get(0);
      System.out.println("Le stylo récupéré dans la base :");
      System.out.println(st);
      st.setCouleur("vert");
      Stylo sAncien = daoStylo.update(st);
      System.out.println("Les données actuelles dans la base :");
      System.out.println(sAncien);
      c.commit();
    }
    catch(Exception e) {
      e.printStackTrace();
      if (c != null) {
        c.rollback();
      }
    }
    finally {
      if (c != null) {
        c.close();
      }
    }
    try {
      // Commenter pour voir le comportement si la connexion est fermée.
      c = Db4oEmbedded.openFile("Dao");
      daoStylo.setConnexion(c);
      // Supprime un des stylos
      daoStylo.delete(daoStylo.findByReference("S35"));
      List<Stylo> liste = daoStylo.findAll();
      System.out.println("Les données dans la base :");
      for (Stylo s : liste) {
        System.out.println(s);
      }
      // Devrait lever une exception.
//      daoStylo.update(new Stylo("ff", "sfsd", 52, "rouge"));
      c.commit();
    }
    catch(Exception e) {
      e.printStackTrace();
      c.rollback();
    }
    finally {
      if (c != null) {
        c.close();
      }
    }
  }
}