package domain.classes;
import domain.exceptions.InvalidWordFormat;
import domain.utils.Pair;

import java.util.*;

public class SimilitudeComp extends Comparator<Document> {
    /**
     * Nombre màxim de documents a retornar per getList().
     */
    private int k;

    /**
     * Vector de referència per a les comparacions.
     * Ens la proporcionen a la constructora, o bé el construïm a partir d'un document.
     */
    private HashMap<String, Float> referenceVec; // Vector used as a reference (all the cosine similarities are computed with respect to this vector) it's <String word, int weight>

    /**
     * Distància euclediana del vector de referència.
     * Precalculada a la constructora per a estalviar càlculs durant les comparacions.
     */
    private Float referenceVecEucledianNorm;

    /**
     * Llista de documents i les seves similituds respecte al vector referència.
     * No necessàriament ordenada.
     */
    private LinkedList<Pair<Document, Float>> list; // {Document, CosineSimilarity}

    /**
     * Estratègia de càlcul de pesos.
     * Determina quina tipus de pesos demanem al document, però no influeix en els càlculs.
     */
    private int weightType;

//--------------------------------------------------------

    /**
     * Funció necesaria pel mergeSortList().
     * @param l Índex del document de l'esquerra.
     * @param m Índex del document del centre.
     * @param r Índex del document de la detra.
     */
    void merge(int l, int m, int r) {
        int i,j,x;
        LinkedList<Pair<Document,Float>> B = new LinkedList<>(list);
        i = l; j = m+1; x = l;

        while (i <= m && j <= r)
            if (B.get(i).getSecond() > B.get(j).getSecond())
                list.set(x++, B.get(i++));
            else
                list.set(x++, B.get(j++));

        while (i <= m)
            list.set(x++, B.get(i++));
    }

    /**
     * Ordena els documents de la list de més similar a menys similar
     * @param l Índex de l'element més de l'esquerra a ordenar.
     * @param r Índex de l'element més de la dreata a ordenar.
     */
    private void mergeSortList(int l, int r) { //ordenar los elementos de la lista dependiendo de
        if (l < r) {
            int m = (l+r)/2;
            mergeSortList(l,m);
            mergeSortList(m+1, r);
            merge(l,m,r);
        }
    }

    /**
     * Mana a ordenar els documents de la llista i en retorna min(k, list.size()) Documents més similars
     * @return Aquesta funció retorna el llistat de documents ja ordenats.
     */
    private LinkedList<Document> getSortedList() {
        mergeSortList(0,list.size()-1);
        LinkedList<Document> tmp = new LinkedList<>();
        int NRetDocs = Math.min(list.size(), k);
        for (int i = 0; i < NRetDocs; ++i)
            tmp.add(list.get(i).getFirst());
        return tmp;
    }

    /**
     * Constructora amb Document com a referència.
     * @param doc Document a usar com a referència. Totes les similituds són calculades tenint en compte el contingut d'aquest document.
     * @param k Número de documents més similars que volem de tornada.
     * @param weightType 1 o 2 depenent del tipus de weights que es vulguin usar per a ordenar els documents.
     * @throws InvalidWordFormat Quan alguna de les paraules del document a comparar com a referència no és UTF-8.
     */
    public SimilitudeComp(Document doc, int k , int weightType) throws InvalidWordFormat {
        this.k = k;
        this.referenceVec = new HashMap<>();
        this.list = new LinkedList<>();
        this.weightType = weightType;
        String[] words = doc.getWords(); //We are assured no repeated words are going to appear
        Float tf;
        referenceVecEucledianNorm = 0F;
        for (String s : words) {
            if (weightType == 1) tf = doc.getWeight1(s);
            else tf = doc.getWeight2(s);
            referenceVec.put(s, tf);
            referenceVecEucledianNorm += tf*tf;
        }
        referenceVecEucledianNorm = (float)Math.sqrt(referenceVecEucledianNorm);
    }

    /**
     *
     * @param ref Hasmap que s'usa com vector de referència. Ha de contenir:
     *            - Paraula
     *            - Pes de la paraula
     * @param k Número de documents més similars que volem de tornada.
     * @param weightType 1 o 2 depenent del tipus de weights que es vulguin usar per a ordenar els documents.
     */
    public SimilitudeComp(HashMap<String, Float> ref, int k, int weightType) {
        this.k = k;
        this.referenceVec = ref;
        this.list = new LinkedList<>();
        this.weightType = weightType;
        //this.comparedDocs = new LinkedList<>();
        referenceVecEucledianNorm = 0F;
        for (Map.Entry<String, Float> set : ref.entrySet()) {
            Float tf = set.getValue();
            referenceVecEucledianNorm += tf*tf;
        }
        referenceVecEucledianNorm = (float)Math.sqrt(referenceVecEucledianNorm);
    }

    /**
     * Compara el document amb la referència i el guarda per quan es demani la llista de similituds.
     * @param document Document a comparar amb la referència.
     * @return Si s'han comparat menys de k documents, la funció retorna true. En cas que ja haguem comparat k documents o més, la funció retorna false.
     * @throws InvalidWordFormat quan alguna de les paraules del document a comparar no és UTF-8
     */
    @Override
    public boolean compare(Document document) throws InvalidWordFormat {
        //retrieval of tfs and computation of cosinus Similarity
        HashMap<String, Float> incomingVec = new HashMap<>();
        Float dotProd = 0F, docEucledianNorm = 0F;
        String w;
        Float weight;
        for (Map.Entry<String, Float> set : referenceVec.entrySet()) {
            w = set.getKey();          //word
            if (weightType == 1) weight = document.getWeight1(w);
            else weight = document.getWeight2(w);
            dotProd += set.getValue()*weight;   //dot product of the two vectors
            docEucledianNorm += weight*weight;  //eucledian norm of the incoming document
        }

        docEucledianNorm = (float)Math.sqrt(docEucledianNorm);
        // determine the cosine similarity of the incoming document with respect to the current
        float cosSim;
        if (docEucledianNorm.equals(0F)) cosSim = 0F;
        else cosSim = dotProd / (referenceVecEucledianNorm * docEucledianNorm);

        Pair<Document, Float> newListEntry = new Pair<>(document, cosSim);
        list.add(newListEntry);
        // return status of the comparison list. Returns true as long as the list's capacity is below k
        return list.size() < k;
    }

    /**
     * @return Retorna una llista de documents ordenats per similitud. La llista mesura min(k, docsComparats.size()).
     */
    @Override
    public LinkedList<Document> getList() {
        return getSortedList();
    }
}
