
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Lot formé de plusieurs articles.
 *
 */
public class Lot extends Article {
  /**
   * Pourcentage de réduction par rapport aux prix des articles
   * qui composent le lot.
   */
  private BigDecimal reduction;
  /**
   * Juste pour éviter de le recalculer à chaque calcul du prix du lot.
   */
  private BigDecimal pourcentage;
  private Collection<ComposantLot> composants = 
    new ArrayList<ComposantLot>();

  public Lot(String reference, String descriptif, BigDecimal reduction) {
    super(reference, descriptif);
    this.setReduction(reduction);
  }

  public Lot(String reference, String descriptif, int reduction) {
    this(reference, descriptif, new BigDecimal(reduction));
  }

  /**
   * Ajoute nb article au lot.
   * @param article
   * @param nb
   */
  public void add(Article article, int nb) {
    composants.add(new ComposantLot(article, nb));
  }

  /**
   * Le prix d'un lot est calculée d'après le prix 
   * <b>actuel</b> des articles qui le compose. 
   */
  @Override
  /**
   * Calcule le prix du lot.
   */
  public BigDecimal getPU() {
    BigDecimal total = new BigDecimal(0);
    for (ComposantLot composant : composants) {
      total = total.add(composant.getPrix());
    }
    return total.multiply(pourcentage);
  }

//public Collection<ComposantLot> getComposants() {
//return composants;
//}

//public void setComposants(Collection<ComposantLot> composants) {
//this.composants = composants;
//}

  public BigDecimal getReduction() {
    return reduction;
  }

  /**
   * Met la réduction en pourcentage. Par exemple, 
   * <code>setreduction(20)</code>
   * indique que la réduction sera de 20%.
   * @param reduction
   */
  public void setReduction(BigDecimal reduction) {
    this.reduction = reduction;
    // Pour ne pas le recalculer à chaque calcul du prix du lot.
    this.pourcentage = new BigDecimal(1).subtract(reduction.divide(new BigDecimal(100), new MathContext(2)));
  }

  /**
   * Classe interne static (elle ne référence pas un objet particulier
   * de la classe Lot englobante).
   * Composant d'un lot : un certain nombre d'articles de même type. Par exemple,
   * 10 stylos de référence 340.
   */
  public static class ComposantLot {
    private Article article;
    private int nb;

    public ComposantLot() {
    }

    public ComposantLot(Article article, int nb) {
      this.article = article;
      this.nb = nb;
    }

    public Article getArticle() {
      return article;
    }

    public void setArticle(Article article) {
      this.article = article;
    }

    public int getNb() {
      return nb;
    }

    public void setNb(int nb) {
      this.nb = nb;
    }

    public BigDecimal getPrix() {
      return article.getPU().multiply(new BigDecimal(nb), new MathContext(2));
    }

    @Override
    public String toString() {
      return "[ComposantLot;" + article + ";" + nb + "]";

    }
  }

}
