
import java.math.BigDecimal;

/**
 * Articles unitaires (en opposition aux lots).
 * Le prix et la description ne sont pas des "variables calcul�es"
 * d'apr�s d'autres attributs de l'article.
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

  // Inutile de red�finir toString() car le PU
  // est d�j� affich� par la classe m�re (Article)

}
