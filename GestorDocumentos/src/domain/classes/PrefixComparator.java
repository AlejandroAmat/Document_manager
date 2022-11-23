package domain.classes;

/**
 * Clase que extiende de Comparator<T> y se encarga de comparar un prefijo dado con el String de autor.
 * @author Alejandro
 *
 */
public class PrefixComparator extends Comparator<String> {


    private String reference;

    //-------------------------------------------

    /**
     * Constructor de la clase. Inicializa el prefijo con el valor pasado por parámetro.
     * @param prefix - Prefijo a considerar en la comparación.
     */
    public PrefixComparator (String prefix){
        this.reference = prefix;
    }


    /**
     * Función que sobreecribe al método abstracto de Comparator donde se mira si el nombre del Autor pasado por parámetro contiene el prefijo de la instancia. Si el prefijo
     * está vacío se considera que son todos los nombres.
     * @param author - Nombre del autor
     * @return boolean. Si el nombre del autor contiene el prefijo de la clase.
    */
     @Override
     public boolean compare(String author) {

        if(this.reference.length()==0) return true;
        if(author.length()<this.reference.length()) return false;
        String author_prefix = author.substring(0,this.reference.length());
        if(author_prefix.equalsIgnoreCase(this.reference)) {
        this.addToList(author);
        return true;
    }
        return false;
}


}