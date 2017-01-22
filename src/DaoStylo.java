

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
   * Joue le r�le de la connexion � la BD.
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
   * Supprime les donn�es correspondant � un stylo dans la base de donn�es.
   * Pr�condition : le param�tre stylo est un stylo qui a d�j�
   * une identit� dans la base.
   * Postcondition : les donn�es qui ont cette identit� sont
   * supprim�es de la base.
   * @param stylo stylo qui a une identit� dans la base qui correspond
   * aux donn�es � supprimer.
   * @throws Exception si le stylo pass� en param�tre n'a pas d'identit� 
   * dans la base ou pour une autre raison.
   */
  public void delete(Stylo stylo) throws Exception {
    ExtObjectContainer eoc = objectContainer.ext();
    if (! eoc.isStored(stylo)) {
      throw new Exception(stylo + " n'a pas d'identit� dans la base");
    }
    objectContainer.delete(stylo);
  }
  
  /**
   * Supprime les donn�es correspondant � la r�f�rence pass�e en param�tre.
   * @param reference la r�f�rence des donn�es � supprimer.
   * @return true ssi des donn�es avaient cette r�f�rence et ont
   * �t� supprim�es.
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
   * Recherche le stylo qui a une certaine r�f�rence.
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
   * Ajoute un nouveau stylo dans la base de donn�es.
   * Pr�condition : le stylo pass� en param�tre n'a pas d�j�
   * une identit� dans la base.
   * Postcondition : les donn�es contenues dans stylo sont ajout�es
   * dans la base.
   * @param stylo stylo qui contient les donn�es � ins�rer dans la 
   * base de donn�es.
   * @throws Exception
   */
  public void insert(Stylo stylo) throws Exception {
    ExtObjectContainer eoc = objectContainer.ext();
    if (eoc.isStored(stylo)) {
      throw new Exception(stylo + " a d�j� une identit� dans la base");
    }
    objectContainer.store(stylo);
  }

  /**
   * Met � jour un stylo d�j� dans la base de donn�es.
   * Pr�condition : le param�tre stylo est un stylo qui a d�j�
   * une identit� dans la base.
   * Postcondition : les donn�es contenues dans stylo sont mises
   * � jour dans la base.
   * @param stylo le stylo qui a une identit� dans la base et qui contient
   * les informations � modifier dans la base.
   * @return un stylo qui contient les donn�es sur le stylo telles qu'elles
   * �taient dans la base avant la modification.
   * @throws Exception
   */
  public Stylo update(Stylo stylo) throws Exception {
    ExtObjectContainer eoc = objectContainer.ext();
    if (! eoc.isStored(stylo)) {
      throw new Exception(stylo + " n'a pas d'identit� dans la base");
    }
    eoc.store(stylo);
    return eoc.peekPersisted(stylo, 2, true);
  }
  
  /**
   * Retourne la liste de tous les stylos qui correspondent �
   * un stylo "exemple" pass� en param�tre.
   * @param exemple stylo "exemple".
   * @return la liste des stylos qui correspondent � l'exemple.
   * @throws Exception
   */
  public List<Stylo> findByExample(Stylo exemple) throws Exception {
    return objectContainer.queryByExample(exemple);
  }
  
  /**
   * Retourne la connexion en cours utilis�e par le DAO.
   * @return
   */
  public ObjectContainer getConnexion() {
    return objectContainer;
  }

  /**
   * Passe une connexion � une base de donn�es au DAO. 
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