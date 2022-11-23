package domain.utils;

import java.security.MessageDigest;

/**
 * Clase con método estático para pasar de un String a un Hash mediante el algoritmo de SHA-256.
 * @author Alejandro Amat
 */
public class SHA_256 {


    /**
     * Método para pasar de String a Hash. Utiliza la clase MessageDigest para el algoritmo SHA_256. Realiza un digest mediante SHA_256 de la contraseña en bytes
     * y posteriormente itera todas las posiciones del hash en bytes y las pasa a String. Se utiliza StringBuilder dado que no es immutable (A diferencia de String).
     * @param password - Contraseña para pasar a Hash.
     * @return String - Retorna el Hash de la contraseña.
     * @throws - RuntimeException
     *
     */
    public static String passwordToHash(final String password){
        try{
            final MessageDigest digest = MessageDigest.getInstance("SHA-256");
            final byte[] hash = digest.digest(password.getBytes("UTF-8"));
            final StringBuilder hexString = new StringBuilder();
            for (int i = 0; i < hash.length; i++) {
                final String hex = Integer.toHexString(0xff & hash[i]);
                if(hex.length() == 1)
                    hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }



}


