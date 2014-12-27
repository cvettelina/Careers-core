package core.application.util;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;

import org.jboss.resteasy.util.Base64;

public class Main {

    /**
     * @param args
     * @throws InvalidKeySpecException 
     * @throws NoSuchAlgorithmException 
     */
    public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeySpecException {
       String pass = "test";
       String token="3qkmkmv9uum66kh2pk2sk2gua9";
       
       System.out.println(Hash.createHash(pass));
       System.out.println(Base64.encodeBytes(token.getBytes()));
       System.out.println((new Date()).getTime());

    }

}
