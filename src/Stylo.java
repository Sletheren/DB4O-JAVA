
import java.math.BigDecimal;

/**
 * Stylo.
 */
public class Stylo extends ArticleUnitaire {
  private String couleur;

  /**
   * Crée un nouveau stylo.
   * @param reference référence du stylo.
   * @param descriptif description du stylo.
   * @param pu prix unitaire du stylo.
   * @param couleur couleur du stylo.
   */
  public Stylo(String reference, String descriptif,
      BigDecimal pu, String couleur) {
    super(reference, descriptif, pu);
    this.couleur = couleur;
  }
  
  public Stylo(String reference, String descriptif,
      int pu, String couleur) {
    this(reference, descriptif, new BigDecimal(pu), couleur);
    this.couleur = couleur;
  }
  

  public String getCouleur() {
    return couleur;
  }
  
  public void setCouleur(String couleur) {
    this.couleur = couleur;
  }

    @Override
  public String toString() {
    return super.toString() + ";couleur=" + couleur;
  }

}
