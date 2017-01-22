
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Facture {
  private String numero;
  private String nomClient;
  private Calendar date;
  /**
   * Lignes de la facture.
   * Les lignes sont ordonn�es.
   * Une seule ligne par article.
   */
  private List<LigneFacture> lignes = new ArrayList<LigneFacture>();

  public Facture(String numero, String client, Calendar date) {
    this.numero = numero;
    this.nomClient = client;
    this.date = date;
  }

  public String getNumero() {
    return numero;
  }

  public Calendar getDate() {
    return date;
  }

  public void setDate(Calendar date) {
    this.date = date;
  }

  public List<LigneFacture> getLignes() {
    return lignes;
  }

  public LigneFacture getLigne(int i) {
    return lignes.get(i);
  }

  public String getNomClient() {
    return nomClient;
  }

  public void setNomClient(String nomClient) {
    this.nomClient = nomClient;
  }

  /**
   * Ajouter une ligne � la facture. La ligne est ajout�e apr�s 
   * les lignes d�j� ajout�es.
   * @param article
   * @param nombreArticle
   * @return l'ancienne ligne qui concernait cet article, ou 
   * <code>null</code> si aucune ligne ne concernait cet article.
   */
  public LigneFacture addLigne(Article article, int nombreArticles) {
    // Pour le cas o� une ligne de la facture concerne le m�me article.
    LigneFacture ancienneLigneFacture = supprimerLigne(article);
    int ancienNombreArticles = 0;
    if (ancienneLigneFacture != null) {
      ancienNombreArticles = ancienneLigneFacture.getNb();
    }
    lignes.add(new LigneFacture(article, nombreArticles + ancienNombreArticles));
    return ancienneLigneFacture;
  }
  
  /**
   * Ins�rer une ligne dans la facture.
   * Si une ligne de la facture concerne d�j� cet article, elle est supprim�e
   * et la nouvelle ligne contient la somme des nombres d'articles.
   * @param numLigne num�ro de la ligne ajout�e. Les lignes
   * d�j� existantes sont d�cal�es pour ins�rer cette nouvelle ligne.
   * @param article
   * @param nombreArticle
   * @return l'ancienne ligne qui concernait cet article, ou 
   * <code>null</code> si aucune ligne ne concernait cet article.
   * @exception IndexOutOfBoundsException si le num�ro de ligne 
   * est < 0 ou > au nombre de lignes de la facture.
   */
  public LigneFacture insertLigne(int numLigne, Article article, int nombreArticles) {
    // Pour le cas o� une ligne de la facture concerne le m�me article.
    LigneFacture ancienneLigneFacture = supprimerLigne(article);
    int ancienNombreArticles = 0;
    if (ancienneLigneFacture != null) {
      ancienNombreArticles = ancienneLigneFacture.getNb();
    }
    lignes.add(numLigne, new LigneFacture(article, nombreArticles + ancienNombreArticles));
    return ancienneLigneFacture;
  }
  
  /**
   * Supprime une ligne. Les lignes suivantes sont d�cal�es.
   * @param numLigne num�ro de la ligne � supprimer.
   * @return la ligne supprim�e.
   * @exception IndexOutOfBoundsException si le num�ro de ligne 
   * ne correspond � aucune ligne de la facture.
   */
  public LigneFacture supprimerLigne(int numLigne) {
    return lignes.remove(numLigne);
  }
  
  /**
   * Supprime la ligne qui concerne un article.
   * @param article
   * @return la ligne supprim�e, ou <code>null</code> si aucune ligne
   * ne concerne l'article.
   */
  public LigneFacture supprimerLigne(Article article) {
    // Recherche la ligne qui concerne cet article
    for (int i = 0; i < lignes.size(); i++) {
      LigneFacture ligneFacture = lignes.get(i);
      if (ligneFacture.getArticle() ==  article) {
        return supprimerLigne(i);
      }
    }
    // Aucune ligne ne concerne cet article
    return null;
  }

  /**
   * Calcule le prix total de la facture.
   * @return le prix total de la facture.
   */
  public BigDecimal getPrixTotal() {
    BigDecimal total = new BigDecimal(0);
    for (LigneFacture ligne : lignes) {
      total = total.add(ligne.getPrix());
    }
    return total;
  }

  @Override
  public String toString() {
    StringBuilder s = new StringBuilder("[Facture;numero=" + numero
    		+ ";nomClient=" + nomClient
        + ";date=" + String.format("%tD", date) + ";prix total=" + getPrixTotal());
//    Mis en commentaire car l'affichage de la facture exemple provoque
//    une NullPointerException (il manque des infos)
//        + getPrixTotal());
    for (LigneFacture ligne : lignes) {
      s.append(ligne.toString());
    }
    s.append("]");
    return s.toString();
  }

  /**
   * Classe interne qui repr�sente une ligne de facture.
   * Elle est static puisqu'elle ne contient aucune r�f�rence � la 
   * classe englobante
   */
  public static class LigneFacture {
    private Article article;
    private int nb;

    /**
     * Cr�e une ligne de facture.
     * 
     * @param refArticle
     */
    public LigneFacture(Article refArticle, int nb) {
      this.article = refArticle;
      this.nb = nb;
    }

    private BigDecimal getPrix() {
      // Calcule avec une pr�cision de 2 chiffres apr�s la virgule.
      return article.getPU().multiply(new BigDecimal(nb), new MathContext(2));
    }

    @Override
    public String toString() {
      return "[LigneFacture;article=" + article.getReference() + ";nb=" + nb
          + "]";
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
  }
}
