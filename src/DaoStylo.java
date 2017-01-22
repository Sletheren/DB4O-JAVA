

import java.util.List;

import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.ext.ExtObjectContainer;

/**
 * DAO pour les stylos.
 */
public class DaoStylo {
  private static DaoStylo dao;
  /**
   * Joue le rôle de la connexion à la BD.
   */
  private ObjectContainer objectContainer;
  
  /**
   * Renvoie un DAO pour les stylos.
   */
  public static DaoStylo getDao() {
      if (dao == null) {
          dao = new DaoStylo();
      }
      return dao;
  }
  
  /**
   * Supprime les données correspondant à un stylo dans la base de données.
   * Précondition : le paramètre stylo est un stylo qui a déjà
   * une identité dans la base.
   * Postcondition : les données qui ont cette identité sont
   * supprimées de la base.
   * @param stylo stylo qui a une identité dans la base qui correspond
   * aux données à supprimer.
   * @throws Exception si le stylo passé en paramètre n'a pas d'identité 
   * dans la base ou pour une autre raison.
   */
  public void delete(Stylo stylo) throws Exception {
    ExtObjectContainer eoc = objectContainer.ext();
    if (! eoc.isStored(stylo)) {
      throw new Exception(stylo + " n'a pas d'identité dans la base");
    }
    objectContainer.delete(stylo);
  }
  
  /**
   * Supprime les données correspondant à la référence passée en paramètre.
   * @param reference la référence des données à supprimer.
   * @return true ssi des données avaient cette référence et ont
   * été supprimées.
   * @throws Exception
   */
  public boolean delete(String reference) throws Exception {
    Stylo stylo = findByReference(reference);
    if (stylo != null) {
      objectContainer.delete(stylo);
      return true;
    }
    return false;
  }
  
  /**
   * Recherche le stylo qui a une certaine référence.
   * @param reference
   * @return
   */
  public Stylo findByReference(String reference) {
    ObjectSet<Stylo> objectSet =
      objectContainer.queryByExample(new Stylo(reference, null, null, null));
    return objectSet.next();
  }

  /**
   * Retourne tous les stylos
   * @return une liste de tous les stylos.
   * @throws Exception
   */
  public List<Stylo> findAll() throws Exception {
    return objectContainer.query(Stylo.class);
  }

  /**
   * Ajoute un nouveau stylo dans la base de données.
   * Précondition : le stylo passé en paramètre n'a pas déjà
   * une identité dans la base.
   * Postcondition : les données contenues dans stylo sont ajoutées
   * dans la base.
   * @param stylo stylo qui contient les données à insérer dans la 
   * base de données.
   * @throws Exception
   */
  public void insert(Stylo stylo) throws Exception {
    ExtObjectContainer eoc = objectContainer.ext();
    if (eoc.isStored(stylo)) {
      throw new Exception(stylo + " a déjà une identité dans la base");
    }
    objectContainer.store(stylo);
  }

  /**
   * Met à jour un stylo déjà dans la base de données.
   * Précondition : le paramètre stylo est un stylo qui a déjà
   * une identité dans la base.
   * Postcondition : les données contenues dans stylo sont mises
   * à jour dans la base.
   * @param stylo le stylo qui a une identité dans la base et qui contient
   * les informations à modifier dans la base.
   * @return un stylo qui contient les données sur le stylo telles qu'elles
   * étaient dans la base avant la modification.
   * @throws Exception
   */
  public Stylo update(Stylo stylo) throws Exception {
    ExtObjectContainer eoc = objectContainer.ext();
    if (! eoc.isStored(stylo)) {
      throw new Exception(stylo + " n'a pas d'identité dans la base");
    }
    eoc.store(stylo);
    return eoc.peekPersisted(stylo, 2, true);
  }
  
  /**
   * Retourne la liste de tous les stylos qui correspondent à
   * un stylo "exemple" passé en paramètre.
   * @param exemple stylo "exemple".
   * @return la liste des stylos qui correspondent à l'exemple.
   * @throws Exception
   */
  public List<Stylo> findByExample(Stylo exemple) throws Exception {
    return objectContainer.queryByExample(exemple);
  }
  
  /**
   * Retourne la connexion en cours utilisée par le DAO.
   * @return
   */
  public ObjectContainer getConnexion() {
    return objectContainer;
  }

  /**
   * Passe une connexion à une base de données au DAO. 
   * Ne fait rien si le DAO a une connexion en cours ouverte.
   * @param objectContainer la nouvelle connexion.
   */
  public void setConnexion(ObjectContainer objectContainer) {
    if (this.objectContainer != null && ! this.objectContainer.ext().close()) {
      return;
    }
    this.objectContainer = objectContainer;
  }
}