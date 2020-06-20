import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

public class MainCryptoQrServices {


    //AES 256 SECRET KEY GENERATOR
    public static SecretKey generateSecretKey(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        //GENERACION DE PARTE RANDOM CON SALT Y BASE 64
        SecureRandom srandom = new SecureRandom();
        byte[] salt = new byte[12];
        srandom.nextBytes(salt);
        //GENRACION DE PASSWORD
        char[] passwordChar = password.toCharArray();
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(passwordChar, salt, 11000, 256);
        SecretKey key = factory.generateSecret(spec);
        return new SecretKeySpec(key.getEncoded(), "AES");
    }

    //AES 256 DECRYPTION
    public static byte[] decryptData(SecretKey secretKey, byte[] encryptedData)
            throws NoSuchPaddingException,
            NoSuchAlgorithmException,
            InvalidAlgorithmParameterException,
            InvalidKeyException,
            BadPaddingException,
            IllegalBlockSizeException,
            InvalidKeySpecException {
        //Wrap the data into a byte buffer to ease the reading process
        ByteBuffer byteBuffer = ByteBuffer.wrap(encryptedData);
        int noonceSize = byteBuffer.getInt();
        //Make sure that the file was encrypted properly
        if (noonceSize < 12 || noonceSize >= 16) {
            throw new IllegalArgumentException("Nonce size is incorrect. Make sure that the incoming data is an AES encrypted file.");
        }
        byte[] iv = new byte[12];
        byteBuffer.get(iv);
        //Prepare your key/password
        //SecretKey secretKey = generateSecretKey(key, iv);
        //get the rest of encrypted data
        byte[] cipherBytes = new byte[byteBuffer.remaining()];
        byteBuffer.get(cipherBytes);
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec parameterSpec = new GCMParameterSpec(128, iv);
        //Encryption mode on!
        cipher.init(Cipher.DECRYPT_MODE, secretKey, parameterSpec);
        //Encrypt the data
        return cipher.doFinal(cipherBytes);
    }

    //AES DATA ENCRYPTION
    public static byte[] encryptData(SecretKey secretKey, byte[] data) throws NoSuchPaddingException,
            NoSuchAlgorithmException,
            InvalidAlgorithmParameterException,
            InvalidKeyException,
            BadPaddingException,
            IllegalBlockSizeException, InvalidKeySpecException {
        //Prepare the nonce
        SecureRandom secureRandom = new SecureRandom();
        //Noonce should be 32 bytes
        byte[] iv = new byte[12];
        secureRandom.nextBytes(iv);
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec parameterSpec = new GCMParameterSpec(128, iv);
        //Encryption mode on!
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, parameterSpec);
        //Encrypt the data
        byte[] encryptedData = cipher.doFinal(data);
        //Concatenate everything and return the final data
        ByteBuffer byteBuffer = ByteBuffer.allocate(4 + iv.length + encryptedData.length);

        byteBuffer.putInt(iv.length);
        byteBuffer.put(iv);
        byteBuffer.put(encryptedData);
        return byteBuffer.array();
    }

    public void storeSecretKey(SecretKey secretKey) throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException, InvalidKeySpecException {
        //INITIALIZE EMPTY KEYSTORE
        KeyStore keyStore = KeyStore.getInstance("JCEKS");
        keyStore.load(null, null);


        //SETTING ENTRY KEY TO A KEYSTORE
        keyStore.setKeyEntry("app-cryptoqr-secret", secretKey, "keystorepassword".toCharArray(), null);


        //SAVING PHYSICAL KEYSTORE FILE
        keyStore.store(new FileOutputStream("C:/Users/rrojas/IdeaProjects/CryptoQR-services/src/main/resources/cryptoQRKeyStore.jceks"), "mono40cryptoqr".toCharArray());
    }

  /*  public SecretKeySpec loadSecretKey() throws KeyStoreException , NoSuchAlgorithmException, CertificateException, IOException, UnrecoverableKeyException {
        KeyStore ks = KeyStore.getInstance("JCEKS");
        ks.load(new FileInputStream("C:/Users/rrojas/Documents/PrivateWorkspace/tpagoKeyStoreRojas.jceks"),"prueba100".toCharArray() );
        Key keyLoad = ks.getKey("app-neo-secret", "prueba100".toCharArray());
        return new SecretKeySpec(keyLoad.getEncoded(), "AES");
    }*/

    public SecretKeySpec loadSecretKeyFromPath(String path, String keystorePassword) throws KeyStoreException , NoSuchAlgorithmException, CertificateException, IOException, UnrecoverableKeyException {
        KeyStore ks = KeyStore.getInstance("JCEKS");
        ks.load(new FileInputStream(path),keystorePassword.toCharArray() );
        Key keyLoad = ks.getKey("app-neo-secret", keystorePassword.toCharArray());
        return new SecretKeySpec(keyLoad.getEncoded(), "AES");
    }


    public static void main(String[] args) throws UnrecoverableKeyException, CertificateException, NoSuchAlgorithmException, KeyStoreException, IOException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, InvalidKeySpecException, NoSuchPaddingException {
        MainCryptoQrServices crypto = new MainCryptoQrServices();
        String tokenRaw = "AAAADKrxxy6oCwp/n/e5xRG89GLzmm6t9FM2YTgxGrna1QNMjhFe3IROoXItq5ENm3STNL5D7HcyKb0/zGiMCthXJ4anvZrDQSzFkNK0MGehkWBxFfmKfSy07aOl4ZS+aGDEwQCh+UUD3Ln3FH6M0Qp/GU1DVc5sAZnZccZcgxg5wYH55JL7mR3rVdZas6TSf59SGVfdG7D5yKu1jqkaBqjS3cj7iFHgtiAFJElbThhNtSbr4+PcH6uH";

        //SecretKeySpec secretKeySpec = crypto.loadSecretKeyFromPath("C:/Users/rrojas/IdeaProjects/CryptoQR-services/src/main/resources/cryptoQRKeyStore.jceks", "keystorepassword");
        String keyWs = "/Utimji2Y15H0nFJLbUTwDn57z2dhlfaaUADzoc9y1I=";

        //genera secret key a partir de secretKeySpec
        final byte[] symKeyData = DatatypeConverter.parseBase64Binary(keyWs);
        final SecretKeySpec symKey = new SecretKeySpec(symKeyData, "AES");

        SecretKey llave = new SecretKeySpec(DatatypeConverter.parseBase64Binary(keyWs), "AES");

        //Se desdencripta la data encriptada utilizando la misma llave
        byte[] desencriptedToken = decryptData(llave, DatatypeConverter.parseBase64Binary(tokenRaw));
        final String mensajeDesencriptado = Base64.getEncoder().encodeToString(desencriptedToken);

        String decodedString = new String(desencriptedToken);
        System.out.println("MENSAJE DESENCRIPTADO USANDO LLAVE PRIVADA ==> " + decodedString);
        System.out.println("\n");
    }

}
