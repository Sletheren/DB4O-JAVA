
import java.math.BigDecimal;

/**
 * Articles unitaires (en opposition aux lots).
 * Le prix et la description ne sont pas des "variables calculées"
 * d'après d'autres attributs de l'article.
 */
public abstract class ArticleUnitaire extends Article {
  private BigDecimal pu;
  
  public ArticleUnitaire(String reference, String descriptif, BigDecimal pu) {
    super(reference, descriptif);
    this.pu = pu;
  }

  public BigDecimal getPU() {
    return pu;
  }

  public void setPU(BigDecimal pu) {
    this.pu = pu;
  }

  // Inutile de redéfinir toString() car le PU
  // est déjà affiché par la classe mère (Article)

}
