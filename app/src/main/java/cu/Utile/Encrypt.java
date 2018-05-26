package cu.Utile;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class Encrypt {

    public String get_SHA_512_SecurePassword(String passwordToHash){

        String salt="#c@%10I";
        String generatedPassword = null;

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(salt.getBytes("UTF-8"));
            byte[] bytes = md.digest(passwordToHash.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for (byte aByte : bytes) {
                sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        }
        catch (NoSuchAlgorithmException | UnsupportedEncodingException e){
            e.printStackTrace();
        }
        return generatedPassword;
    }


}
