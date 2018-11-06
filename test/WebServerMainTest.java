import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;

public class WebServerMainTest {
    String[] argsTrue = new String[2];
    String[] argsBadFile = new String[2];
    String[] argsBadPort = new String[2];

    @Before
    public void setUp() {
        argsTrue[0] = "../Resources/www/";
        argsTrue[1] = "8888";
        argsBadFile[0] = "../NoSuchDir/";
        argsBadFile[1] = "8888";
        argsBadPort[0] = "../Resources/www/";
        argsBadPort[1] = "I am not a Port";
    }

    @Test
    public void argsCheck() {
        //idk what to do here XD
    }
}