package domain.classes;
import domain.utils.SHA_256;

/**
 *Esta clase representa a un Usuario dentro del sistema. Cada usuario está identificado por su nombre y tiene una contraseña.
 * @author Alejandro Amat
 */
public class User {

    private String username;
    private String password;


    /**
     * Constructor de Usuario utilizando su nombre y contraseña.
     * @param username
     * @param password
     */
    public User (String username, String password){

        this.username = username;
        this.password = password;

    }

    /**
     *
     * @return: Retorna el nombre de usuario del usuario.
     */
    public String getUsername() {
        return this.username;
    }

    /**
     *
     * @return: Retorna la contraseña del usuario.
     */
    public String getPassword() { return this.password;}


    /**
     * Método para cambiar nombre de Usuario.
     * @param newUsername - Nuevo nombre.
     */
    public void changeUsername (String newUsername){
        this.username = newUsername;
    }

    /**
     * Método para cambiar de contraseña. Se almacena el Hash (SHA_256) de esta.
     * @param newPassword - Nueva Contraseña.
     */
    public void changePassword (String newPassword){ this.password = SHA_256.passwordToHash(newPassword);}



}

