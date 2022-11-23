package domain.drivers;
import domain.classes.User;
import domain.controllers.UserCtrl;
import domain.exceptions.*;

import java.util.Scanner;

/**
 * Clase Driver para ejecutar las funcionalidades del controlador de Usuario. Menú con distintas opciones para añadir, modificar, conectar y desconectar usuarios. Indica
 * por pantalla el resultado de las operaciones. Se ejecuta constantemente hasta que se escoge la función salir.
 * @author Alejandro Amat
 */
public class UserCtrlDriver {


    final String ANSI_RED = "\u001B[31m";
    final String ANSI_RESET = "\u001B[0m";
    final String ANSI_GREEN = "\u001B[32m";
    String options;
    String username;
    String password;
    String new_name;
    Scanner scanner;
    int success_error;

    UserCtrl user_ctrl;

    /**
     * Método constructor de la clase. Se inicializan las variables.
     */
    public UserCtrlDriver(){
        this.scanner = new Scanner(System.in);
        this.success_error = 0;
        this.options = "";
        this.username = "";
        this.password= "";
        this.new_name = "";
        this.user_ctrl = new UserCtrl();
    }

    /**
     * método main. El programa se ejecuta desde aquí. Se tiene un bucle con las opciones de:
     * -Sign in (crear nuevo usuario y añadirlo al controlador)
     * -Delete (Eliminar al usuario del sistema)
     * -Log in (Conectarse con el usuario especificado)
     * -Cambiar Contraseña
     * -Cambiar Usuario
     * -Log out
     * -Exit
     * Se analizan los distintos errores retornados por el controlador y hay un prompt que indica constatemente qué información incluir en cada momento.
     * @param args
     */
    public static void main(String[] args){



        UserCtrlDriver control_driver = new UserCtrlDriver();



        while (!control_driver.options.equals("salir") ){
            System.out.println("\n  Menú Principal Driver Usuario \n ------------------------------- \n " +
                    "(i)    Sign Up : 'signUp' \n (ii)   Eliminar Usuario : 'delete' \n (iii)  Log In : 'logIn' \n (iv)   Cambiar nombre de usuario : 'changeUsername' \n " +
                    "(v)    Cambiar contraseña : 'changePassword' \n (vi)   Log Out : 'logOut' \n (vii)  Salir del Sistema : 'salir'   ");
            control_driver.options = control_driver.scanner.nextLine();

           switch (control_driver.options.toLowerCase()){
               case "signup":
                   control_driver.SignUp();
                   break;
               case "delete":
                   control_driver.Delete();
                   break;
               case "login" :
                   control_driver.LogIn();
                   break;
               case "changeusername":
                   control_driver.ChangeUsername();
                   break;
               case "changepassword":
                   control_driver.ChangePassword();
                   break;
               case "logout":
                   control_driver.LogOut();
                   break;
               case "salir" :
                   System.out.println("Exit");
                   break;
               default : System.out.println("No existe esa opción");
           }

        }

    }
    /**
     * Signup
     * @throws UserExists, UserFormat
     */
    public void SignUp() {
        try {
            System.out.println(" \n Nombre de Usuario:");
            this.username = this.scanner.nextLine();
            System.out.println("Contraseña");
            this.password = this.scanner.nextLine();

            this.user_ctrl.signUp(this.username, this.password);

        } catch(UserExists | UserFormat ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Delete
     * @throws UserNotFound
     */
    public void Delete(){
        try {
            System.out.println(" \n Nombre de Usuario:");
            this.username = this.scanner.nextLine();

            this.user_ctrl.delete(this.username);
        }
        catch (UserNotFound ex){
            ex.printStackTrace();
        }

    }

    /**
     * Log In
     * @throws UserConnected,UserNotFound,PasswordIncorrect
     */
    public void LogIn(){
        try {
            System.out.println(" \n Nombre de Usuario:");
            this.username = this.scanner.nextLine();
            System.out.println("Contraseña");
            this.password = this.scanner.nextLine();

            this.user_ctrl.logIn(this.username, this.password);
        }
        catch(UserConnected | UserNotFound | PasswordIncorrect e){
            e.printStackTrace();
        }

    }

    /**
     * Change Username
     * @throws UserNotFound,UserExists,UserFormat,PasswordIncorrect
     */
    public void ChangeUsername(){
        try {
            System.out.println(" \n Nombre de Usuario:");
            this.username = this.scanner.nextLine();
            System.out.println("Contraseña:");
            this.password = this.scanner.nextLine();
            System.out.println(" \n Nuevo nombre de Usuario:");
            this.new_name = this.scanner.nextLine();

            this.user_ctrl.changeUsername(this.username, this.new_name, this.password);
        }
        catch(UserNotFound | UserExists | UserFormat | PasswordIncorrect e){
            e.printStackTrace();
        }

    }

    /**
     * ChangePassword
     * @throws UserNotFound,UserFormat,PasswordIncorrect
     */
    public void ChangePassword(){
        try {
            System.out.println(" \n Nombre de Usuario:");
            this.username = this.scanner.nextLine();
            System.out.println("Contraseña Actual:");
            this.password = this.scanner.nextLine();
            System.out.println(" \n Nueva Contraseña:");
            this.new_name = this.scanner.nextLine();

            this.user_ctrl.changePassword(this.username, this.password, this.new_name);
        }
        catch(UserNotFound | UserFormat | PasswordIncorrect e){
            e.printStackTrace();
        }

    }

    /**
     * Log Out
     */
    public void LogOut(){
        try {
            this.user_ctrl.logOut();
        }
        catch (UserNotFound | UserConnected e){
            e.printStackTrace();
        }

    }

}
