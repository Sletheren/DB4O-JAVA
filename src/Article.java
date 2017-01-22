
import java.math.BigDecimal;

/**
 * Un article vendu dans le magasin.
 * On suppose que tout article a une r�f�rence.
 */
public abstract class Article {
  private String reference;
  private String descriptif;
  
  public Article(String reference, String descriptif) {
    this.reference = reference;
    this.descriptif = descriptif;
  }

  public String getReference() {
    return reference;
  }

  public void setDescriptif(String descriptif) {
    this.descriptif = descriptif;
  }

  public String getDescriptif() {
    return descriptif;
  }

  // Les m�thodes suivantes correspondent � des attributs de tout
  // article, mais qui sont "calcul�es" dans la classe Lot,
  // et pas associ�es � une variable d'�tat priv�e
  public abstract BigDecimal getPU();

  public String toString() {
    return "[" + this.getClass().getName() 
      + ";ref=" + reference
      + ";descriptif=" + getDescriptif()
      + ";PU=" + getPU() + "]";
  }
}
