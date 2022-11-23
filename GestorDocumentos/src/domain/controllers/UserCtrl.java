package domain.controllers;
import domain.classes.User;
import domain.exceptions.UserExists;
import domain.utils.SHA_256;
import java.util.HashMap;

import domain.exceptions.*;

/**
 * Esta clase gestiona y maneja cómo interactuar con los usuarios. Añadir, eliminar o modificarlos.
 * @author Alejandro Amat
 */
public class UserCtrl {
    private HashMap<String, User> users;
    private String actual_user;

    /**
     * Constructor de la clase. Crea la instancia del HashMap de usuarios.
     */
    public UserCtrl() {
        this.users = new HashMap<String, User>();
    }


    /**
     * Función que da de alta a un nuevo usuario mediante el nombre pasado y la contraseña. Se pasa el Hash por temas de seguridad.
     * @param username - Nombre de usuario
     * @param password - Contraseña
     * @throws - UserExists, UserFormat
     */
    public void signUp(String username, String password) throws UserExists, UserFormat {
        if (this.users.containsKey(username)) throw new UserExists(username);
        if((username.equals("") || password.equals(""))) throw new UserFormat();

        users.put(username, new User(username, SHA_256.passwordToHash(password)));
    }

    /**
     * Elimina al usuario logeado utilizando el nombre pasado por parámetro
     * @param username - nombre de usuario
     * @throws - UserNotFound
     */
    public void delete(String username) throws UserNotFound{
        if (!this.users.containsKey(username)) throw new UserNotFound(username);

        if(actual_user==username) actual_user = null;
        users.remove(username);

    }

    /**
     * Método para hacer un 'log in' en la aplicación. Comprueba que la contraseña y nombre de usuario sean los indicados y que no hay nadie conectado.
     * @param username - Nombre de Usuario
     * @param password - Contraseña del usuario
     * @throws- UserNotFound,PasswordIncorrect,UserConnected
     */
    public void logIn(String username, String password) throws  UserNotFound, PasswordIncorrect, UserConnected{
        if (!this.users.containsKey(username)) throw new UserNotFound(username);

        User user = users.get(username);
        if(!user.getPassword().equals(SHA_256.passwordToHash(password))) throw new PasswordIncorrect(password);

        if(actual_user != null) throw new UserConnected();

        this.actual_user = username;
    }


    /***
     * Método para camboar Usuario. El usuario debe estar registrado, no puede haber otro usuaruio con ese nombre y la contraseña debe ser correcta. Se elimina el usuario antiguo
     * y se crea otro con el nuevo nombre y la antigua contraseña. Se actualiza el usuario actual.
     * @param oldUsername - Antiguo nombre de usuario
     * @param newUsername - Nuevo nombre de usuario
     * @param password - Contraseña del usuario
     * @throws - UserNotFound,UserExists,UserFormat,PasswordIncorrect
     */
    public void changeUsername(String oldUsername, String newUsername, String password) throws UserNotFound, UserExists , UserFormat,PasswordIncorrect {

           if( !this.users.containsKey(oldUsername)) throw new UserNotFound(oldUsername);
           if( this.users.containsKey(newUsername)) throw new UserExists(newUsername);
           if(newUsername == "") throw new UserFormat();



        User user = this.users.get(oldUsername);
        if(user.getPassword().equals(SHA_256.passwordToHash(password))) {

            if(actual_user != null && actual_user.equals(oldUsername)) actual_user = newUsername;
            user.changeUsername(newUsername);
            this.signUp(newUsername, password);
            this.delete(oldUsername);

           return;
        }

        throw new PasswordIncorrect(password);
    }

    /**
     * Método para cambiar de contraseña. Solo se debe comprobar que existe usuario con nombre y conntraseña pasada por parámetros y modificcar la contraseña.
     * @param username - Nombre de usuario
     * @param oldPassword - Contraseña a modificar (antigua)
     * @param newPassword - Nueva contraseña
     * @throws - UserNotFound,UserFormat,PasswordIncorrect
     */
    public void changePassword(String username, String oldPassword, String newPassword) throws UserNotFound, UserFormat , PasswordIncorrect{
        if (!this.users.containsKey(username)) throw new UserNotFound(username);
        if( newPassword=="") throw new UserFormat();

        User user = this.users.get(username);
        if(user.getPassword().equals(SHA_256.passwordToHash(oldPassword))) this.users.get(username).changePassword(newPassword);
        else throw new PasswordIncorrect(oldPassword);


    }

    /**
     * Método para salir de estado de conexión. Se revisa si se está conectado actualmente y se pone a NULL al usuario actual.
     * @return int --> Indica el error y 0 cuando se ha relizado correctamente.
     */

    public void logOut () throws UserNotFound,UserConnected{
        if(this.actual_user == null) throw new UserNotFound(actual_user);

        this.actual_user = null;
    }

    /**
     * Métodos Get() para los dos atributos de la clase
     * @return User, Map.
     */
    public String getActual_user() {
        return this.actual_user;
    }

    public HashMap<String, User> getUsers() {
        return this.users;
    }
}