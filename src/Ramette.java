
import java.math.BigDecimal;

/**
 * Ramette de feuilles de papier.
 */
public class Ramette extends ArticleUnitaire {
  private int grammage;

  /**
   * Crée une nouvelle ramette.
   * @param reference référence de la ramette.
   * @param descriptif description de la ramette.
   * @param pu prix unitaire de la ramette.
   * @param grammage grammage de la ramette.
   */
  public Ramette(String reference, String descriptif,
      BigDecimal pu, int grammage) {
    super(reference, descriptif, pu);
    this.grammage = grammage;
  }

  public Ramette(String reference, String descriptif,
      int pu, int grammage) {
    this(reference, descriptif, new BigDecimal(pu), grammage);
  }

  public int getGrammage() {
    return grammage;
  }
  
  public void setGrammage(int grammage) {
    this.grammage = grammage;
  }

  @Override
  public String toString() {
    return super.toString() + ";grammage=" + grammage;
  }
}
