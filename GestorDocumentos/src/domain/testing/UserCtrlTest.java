package domain.testing;

import domain.controllers.UserCtrl;
import domain.exceptions.*;
import org.junit.Test;

import static org.junit.Assert.*;


/**
 * Clase de Test para Control de Usuario. validación de SignUp(), LogIn(), ChangePassword(), changeUsername(), delete(); No se usan Stubs ni Mocks porque la clase Usuario
 * es muy simple y solo tiene getters y setters.
 * @author Alejandro Amat
 */
public class UserCtrlTest {


    private static UserCtrl ctrl = new UserCtrl();


    /**
     * Estos Tests verifican las funciones de darse de alta y eliminar usuairo.
     */

    @Test
    public void SignUpUserFormatPassword() {

        try {
            ctrl.signUp("Pedro", "");

            fail();
        } catch (UserFormat e) {
            assertEquals("The username/password can not be empty.", e.getMessage());
        } catch (UserExists e) {
            fail();
        }
    }

    @Test
    public void SignUpUserFormatName() {

        try {
            ctrl.signUp("", "Alex12345");

            fail();
        } catch (UserFormat e) {
            assertEquals("The username/password can not be empty.", e.getMessage());
        } catch (UserExists e) {
            fail();
        }
    }

    @Test
    public void SignUpUserAlreadyExists() {

        try {
            ctrl.signUp("Alex", "Alex12345");
            ctrl.signUp("Alex", "ajaja");

            fail();
        } catch (UserFormat e) {
            fail();
        } catch (UserExists e) {
            assertEquals("The user Alex is already registered.", e.getMessage());
        }
    }

    @Test
    public void SignUpUserCorrect() {

        try {
            ctrl.signUp("Juan", "Alex12345");
            ctrl.signUp("Jose", "Alex12345");
            assertTrue(ctrl.getUsers().get("Jose").getPassword().equals("36902646454afa7f45b718eaa04a2825d9561ed8ea7ee8f827b7b8474fea8333"));

        } catch (UserFormat | UserExists e) {
            fail();
        }
    }

    @Test
    public void DeleteNonExistentUserAndDeleteCorrectly() {
        try {
            ctrl.signUp("Alvaro", "Alex12345");
            ctrl.signUp("Nines", "Alex12345");
            assertEquals("36902646454afa7f45b718eaa04a2825d9561ed8ea7ee8f827b7b8474fea8333", ctrl.getUsers().get("Nines").getPassword());

            ctrl.delete("Nines");
            assertFalse(ctrl.getUsers().containsKey("Nines"));
            ctrl.delete("Alvaro");

            //Aquí debería lanzar excepción:
            ctrl.delete("Miquel");
            fail();

        } catch (UserFormat | UserExists e) {
            fail();
        } catch (UserNotFound e) {
            assertEquals("The user Miquel does not exist.", e.getMessage());
        }
    }


    // }

    /**
     * Los Tests verifican el correcto flujo de darse de alta, conectarse a la aplicación y eliminarse.
     */
    @Test
    public void LogInUserNotFound() {
        try {
            ctrl.signUp("Borja", "Alex12345");
            ctrl.signUp("Ginés", "Alex12345");

            ctrl.logIn("Sebas", "jeej");
            fail();

        } catch (UserFormat | UserExists | PasswordIncorrect | UserConnected e) {
            fail();
        } catch (UserNotFound e) {
            assertEquals("The user Sebas does not exist.", e.getMessage());
        }
    }

    @Test
    public void LogInPasswordIncorrect() {
        try {
            ctrl.signUp("Neo", "Alex12345");
            ctrl.signUp("Anderson", "Alex12345");

            ctrl.logIn("Neo", "jeej");
            fail();

        } catch (UserFormat | UserExists | UserNotFound | UserConnected e) {
            fail();
        } catch (PasswordIncorrect e) {
            assertEquals("The password jeej does not correspond with the user.", e.getMessage());
        }
    }

    @Test
    public void LogInUserAlreadyConnected() throws UserNotFound, UserConnected {
        try {
            ctrl.signUp("Uma", "Alex12345");
            ctrl.signUp("Thurman", "Alex12345");

            ctrl.logIn("Uma", "Alex12345");

            ctrl.logIn("Thurman", "Alex12345");
            fail();

        } catch (UserFormat | UserExists | UserNotFound | PasswordIncorrect e) {
            fail();
        } catch (UserConnected e) {
            ctrl.logOut(); //De cara a otros tests
            assertEquals("There is already a User connected", e.getMessage());
        }
    }

    @Test
    public void LogInAndLogOutCorrect() {
        try {
            ctrl.signUp("Tom", "Alex12345");
            ctrl.signUp("Hanks", "Alex12345");

            ctrl.logIn("Tom", "Alex12345");
            ctrl.logOut();


            ctrl.logIn("Hanks", "Alex12345");
            ctrl.logOut();


        } catch (UserFormat | UserExists | UserNotFound | PasswordIncorrect | UserConnected e) {
            fail();
        }
    }

    @Test
    public void LogOutIncorrectWhileDelete() {
        try {
            ctrl.signUp("Leo", "Alex12345");
            ctrl.signUp("DiCaprio", "Alex12345");

            ctrl.logIn("Leo", "Alex12345");
            ctrl.delete("Leo");

            ctrl.logIn("DiCaprio", "Alex12345");
            ctrl.delete("DiCaprio");

            //Aquí tiene que petar
            ctrl.logOut();
            fail();

        } catch (UserFormat | UserExists | PasswordIncorrect | UserConnected e) {
            fail();
        } catch (UserNotFound e) {
            assertEquals("The user null does not exist.", e.getMessage());
        }
    }


    /**
     * Los test verifican el correcto flujo del cambio de nombre de usuario. Si este está conectado actualmente, si se cambia a un nombre de usuario ya existente o
     * si no introduce bien los datos.
     */

    @Test
    public void ChangeUsernameUserNotFound() {
        try {
            ctrl.signUp("Mathew", "Alex12345");
            ctrl.signUp("McCaunageh3udbdeb", "Alex12345");

            ctrl.changeUsername("Sebas", "jjj", "Mathew");
            fail();

        } catch (UserFormat | UserExists | PasswordIncorrect e) {
            fail();
        } catch (UserNotFound e) {
            assertEquals("The user Sebas does not exist.", e.getMessage());
        }
    }


    @Test
    public void ChangeUsernameUserExists() {
        try {
            ctrl.signUp("Lisa", "Alex12345");
            ctrl.signUp("Kurdrow", "Alex12345");

            ctrl.changeUsername("Lisa", "Kurdrow", "jjj");
            fail();

        } catch (UserFormat | PasswordIncorrect | UserNotFound e) {
            fail();
        } catch (UserExists e) {
            assertEquals("The user Kurdrow is already registered.", e.getMessage());
        }
    }

    @Test
    public void ChangeUsernameUserEmpty() {
        try {
            ctrl.signUp("Matt", "Alex12345");
            ctrl.signUp("Leblanc", "Alex12345");

            ctrl.changeUsername("Matt", "", "Alex12345");
            fail();

        } catch (UserExists | PasswordIncorrect | UserNotFound e) {
            fail();
        } catch (UserFormat e) {
            assertEquals("The username/password can not be empty.", e.getMessage());
        }
    }

    @Test
    public void ChangeUsernameUserWrongPassword() {
        try {
            ctrl.signUp("Jennifer", "Alex12345");
            ctrl.signUp("Anniston", "Alex12345");

            ctrl.changeUsername("Jennifer", "hehe", "Alxscedwed345");
            fail();

        } catch (UserExists | UserFormat | UserNotFound e) {
            fail();
        } catch (PasswordIncorrect e) {
            assertEquals("The password Alxscedwed345 does not correspond with the user.", e.getMessage());
        }
    }

    @Test
    public void ChangeUsernameAndLogIn() {
        try {
            ctrl.signUp("Sandra", "Alex12345");
            ctrl.signUp("Bullock", "Alex12345");

            ctrl.changeUsername("Sandra", "Perry", "Alex12345");
            ctrl.logIn("Perry", "Alex12345");
            ctrl.delete("Perry");

            ctrl.signUp("Sandra", "Alex12345");

        } catch (UserExists | UserFormat | UserNotFound | PasswordIncorrect | UserConnected e) {
            fail();
        }
    }

    /**
     * Los Tests verifican el correcto flujo del cambio de contraseña. Importante valorar los escenarios de volverse a conectar y siempre que se pide contraseña.
     */

    @Test
    public void ChangePasswordUserNotFound() {
        try {
            ctrl.signUp("Joaquin", "Alex12345");
            ctrl.signUp("Phoenix", "Alex12345");

            ctrl.changePassword("Sebas", "Alex12345", "dede");
            fail();

        } catch (UserFormat | UserExists | PasswordIncorrect e) {
            fail();
        } catch (UserNotFound e) {
            assertEquals("The user Sebas does not exist.",e.getMessage());
        }
    }

    @Test
    public void ChangePasswordWrongPassword() {
        try {
            ctrl.signUp("Robert", "Alex12345");
            ctrl.signUp("De Niro", "Alex12345");

            ctrl.changePassword("Robert", "Alex1234defsarfrfw", "Alxscedwed345");
            fail();

        } catch (UserExists | UserFormat | UserNotFound e) {
            fail();
        } catch (PasswordIncorrect e) {
            assertEquals("The password Alex1234defsarfrfw does not correspond with the user.", e.getMessage());
        }
    }


    @Test
    public void ChangePasswordEmpty() {
        try {
            ctrl.signUp("Robbie", "Alex12345");
            ctrl.signUp("Williams", "Alex12345");

            ctrl.changePassword("Robbie", "Alex12345", "");
            fail();

        } catch (UserExists |PasswordIncorrect | UserNotFound e) {
            fail();
        } catch (UserFormat  e) {
            assertEquals("The username/password can not be empty.",e.getMessage());
        }
    }


    @Test
    public void ChangePasswordAndLogIn() {
        try {
            ctrl.signUp("Rusell", "Alex12345");
            ctrl.signUp("Crowe", "Alex12345");

            ctrl.changeUsername("Rusell", "God", "Alex12345");
            ctrl.logIn("God", "Alex12345");
            ctrl.logOut();

            ctrl.changePassword("God","Alex12345", "IamGod");
            ctrl.logIn("God", "IamGod");

            ctrl.delete("God");
            ctrl.logIn("Crowe", "Alex12345");

        } catch (UserExists | UserFormat | UserNotFound | PasswordIncorrect | UserConnected e) {
            fail();
        }
    }
}