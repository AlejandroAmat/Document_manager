package domain.testing;


import domain.classes.User;
import org.junit.Test;
import org.junit.BeforeClass;
import static org.junit.Assert.*;

import java.util.ArrayList;


/**
 * Clase de Test para Usuario. validación de Getters y ChangePassword/Username
 * @author Alejandro Amat
 */
public class UserTest {



    private static ArrayList<User> users = new ArrayList<User>();

    /**
     * Se definen las usuarios a evaluar (Nombre y Contraseña)
     */
    @BeforeClass
    public static void init(){

        users.add(new User("javier","FTnh@907fn1x"));
        users.add(new User ("Manolo","zcuK919k5Z*P"));
        users.add(new User("Beatriz","kVPX3!^8s9$$"));
        users.add(new User("Alejandro","sdugh3ded33e3"));
        users.add(new User ("María Dolores","i3d97623683egb3e308dh38dh3d63drt"));
        users.add(new User ("Luffy","2'e*___-d3d0oj263e5faa//(%"));

    }

    /**
     * El Test verifica que se obtienen las contraseñas correctamente
     */
    @Test
    public void getPassword(){

        assertEquals(users.get(0).getPassword(),"FTnh@907fn1x");
        assertEquals(users.get(1).getPassword(),"zcuK919k5Z*P");
        assertEquals(users.get(2).getPassword(),"kVPX3!^8s9$$");
        assertEquals(users.get(3).getPassword(),"sdugh3ded33e3");
        assertEquals(users.get(4).getPassword(),"i3d97623683egb3e308dh38dh3d63drt");
        assertEquals(users.get(5).getPassword(),"2'e*___-d3d0oj263e5faa//(%");


    }
    /**
     * El Test verifica que se obtienen las nombres de usuario correctamente
     */
    @Test
    public void getUsername(){


        assertEquals(users.get(2).getUsername(),"Beatriz");
        assertEquals(users.get(3).getUsername(),"Alejandro");
        assertEquals(users.get(4).getUsername(),"María Dolores");
        assertEquals(users.get(5).getUsername(),"Luffy");


    }

    /**
     * El Test verifica que se obtiene la contraseña correctamente tras cambiarla
     */
    @Test
    public void changePassword(){

        users.get(0).changePassword("s2e83dh3dih3dkpjm239d3h");
        users.get(1).changePassword("jsdisdh3dh3dh3du8i32hd");

        assertEquals(users.get(0).getPassword(),"969c06c1c7966b8d4f139485cf267afd1379804a1ef048a84583127aba7cfc90");
        assertEquals(users.get(1).getPassword(),"ee356dedca82e8d3cea0689f7e88cecf8fbebe167640308be358a141a4f076f5");


    }
    /**
     * El Test verifica que se obtiene el nombre de usuario corecto tras cambiarlo
     */
    @Test
    public void changeUsername(){

        users.get(0).changeUsername("Luffy");
        users.get(1).changeUsername("Jacobo");

        assertEquals(users.get(0).getUsername(),"Luffy");
        assertEquals(users.get(1).getUsername(),"Jacobo");


    }


}

