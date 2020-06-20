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

/**
 * Main para creacion de token
 */
//public class Main {

//    public static void main(String[] args) {
//
//        Long accountid = new Long(1);
//        Long msisdn = new Long("8294407970");
//
//        String token = TokenGenerator.generateToken(accountid, msisdn, "123456789012345");
//
//        System.out.println(token);
//    }

/**
 * Main para encriptacion/desencriptacion
 */
public class Main {


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
        keyStore.setKeyEntry("app-neo-secret", secretKey, "contrasenaKeyStore".toCharArray(), null);


        //SAVING PHYSICAL KEYSTORE FILE
        keyStore.store(new FileOutputStream("C:/Users/rrojas/Documents/PrivateWorkspace/tpagoKeyStoreRojas.jceks"), "contrasenaKeyStore".toCharArray());
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

    public static void main(String[] args) throws NoSuchAlgorithmException, KeyStoreException, IOException, CertificateException, UnrecoverableKeyException, InvalidKeyException, InvalidKeySpecException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, NoSuchPaddingException {

        Main m = new Main();
        SecretKey secretKey = generateSecretKey("palabraocultadentrodelKey");
        m.storeSecretKey(secretKey);
        SecretKeySpec secretKeySpec = m.loadSecretKeyFromPath("C:/Users/rrojas/Documents/PrivateWorkspace/tpagoKeyStoreRojas.jceks", "contrasenaKeyStore");
        String tokenRaw = "eyJ0eXAiOiJFUzI1NiIsImFsZyI6IkhTMjU2In0.eyJzdWIiOiJ7XCJhdWRcIjpcInRQYWdvXCIsXCJzdWJcIjpcIjEwMDgwXCIsXCJpc3NcIjpcIkdDU1wiLFwiZXhwXCI6XCJUaHUgQXVnIDAxIDA5OjUwOjEzIEJPVCAyMDE5XCIsXCJpYXRcIjpcIlRodSBBdWcgMDEgMDk6NDA6MTMgQk9UIDIwMTlcIixcImp0aVwiOlwiOTdhZjQ0ZTYtOWE3NC00NzE3LTg0NTYtMzNiNWI5OWM0ZjI1XCIsXCJtZXJjaGFudC1kZXNjcmlwdGlvblwiOlwiRWxlY3Ryb2RvbWVzdGljb3NcIn0iLCJleHAiOjE1NjQ2Njc0MTN9.9uPgCu7akr21qkle6kApaLjrdsXENAOduPnkx9xj2xk";
        System.out.println("MENSAJE ORIGINAL ==> " + tokenRaw);

        //Se encripta la data del token utilizando la llave privada
        byte[] encriptedToken = encryptData(secretKeySpec, tokenRaw.getBytes());
        System.out.println("MENSAJE ENCRIPTADO USANDO LLAVE PRIVADA ==> " + Base64.getEncoder().encodeToString(encriptedToken));
        System.out.println("\n");

        //Se desdencripta la data encriptada utilizando la misma llave
        byte[] desencriptedToken = decryptData(secretKeySpec, encriptedToken);
        final String mensajeDesencriptado = Base64.getEncoder().encodeToString(desencriptedToken);
        System.out.println(Base64.getDecoder());


        String decodedString = new String(desencriptedToken);
        System.out.println("MENSAJE DESENCRIPTADO USANDO LLAVE PRIVADA ==> " + decodedString);
        System.out.println("\n");


 /*

        System.out.println("*********************************************\n");
        System.out.println("******************** DEMO DE USO *************************\n");
        System.out.println("*********************************************\n");
        System.out.println("BIENVENIDO AL ENCRIPTOR TOOL\n");
        System.out.println("ENVIAR 4 parametros separados por espacios\n");
        System.out.println("Parametro 1 = Ruta del KeyStore con nombre y formato del keystore (E.j = C:/Users/rrojas/Documents/PrivateWorkspace/tpagoKeyStore.jceks) \n");
        System.out.println("Parametro 2 = Contrasena del keystore (E.j = prueb@100 )\n");
        System.out.println("Parametro 3 = Contrasena del key (E.j = C@ntrasenaDelKey )\n");
        System.out.println("Parametro 4 = Palabra secreta del key (E.j = app-neo-secret)\n");
        System.out.println("Parametro 5 = String del token jwt a encriptar ( E.j = eyJ0eXAiOiJFUzI1NiIsImFsZyI6IkhTMjU2In0.eyJzdWIiOiJ7XCJhdWRcIjpcInRQYWdvXCIsXCJzdWJcIjpcIjEwMDgwXCIsXCJpc3NcIjpcIkdDU1wiLFwiZXhwXCI6XCJUaHUgQXVnIDAxIDA5OjUwOjEzIEJPVCAyMDE5XCIsXCJpYXRcIjpcIlRodSBBdWcgMDEgMDk6NDA6MTMgQk9UIDIwMTlcIixcImp0aVwiOlwiOTdhZjQ0ZTYtOWE3NC00NzE3LTg0NTYtMzNiNWI5OWM0ZjI1XCIsXCJtZXJjaGFudC1kZXNjcmlwdGlvblwiOlwiRWxlY3Ryb2RvbWVzdGljb3NcIn0iLCJleHAiOjE1NjQ2Njc0MTN9.9uPgCu7akr21qkle6kApaLjrdsXENAOduPnkx9xj2xk) \n");
*/
        /*String rutaKeystore = "C:/Users/rrojas/Documents/PrivateWorkspace/tpagoKeyStore.jceks";
        String contrasenaKeystore = "prueba100";
        String secretoKeystore = "app-neo-secret";
        String secretoKey = "prueba100";
        String valorEncriptar = "eyJ0eXAiOiJFUzI1NiIsImFsZyI6IkhTMjU2In0.eyJzdWIiOiJ7XCJhdWRcIjpcInRQYWdvXCIsXCJzdWJcIjpcIjEwMDgwXCIsXCJpc3NcIjpcIkdDU1wiLFwiZXhwXCI6XCJUaHUgQXVnIDAxIDA5OjUwOjEzIEJPVCAyMDE5XCIsXCJpYXRcIjpcIlRodSBBdWcgMDEgMDk6NDA6MTMgQk9UIDIwMTlcIixcImp0aVwiOlwiOTdhZjQ0ZTYtOWE3NC00NzE3LTg0NTYtMzNiNWI5OWM0ZjI1XCIsXCJtZXJjaGFudC1kZXNjcmlwdGlvblwiOlwiRWxlY3Ryb2RvbWVzdGljb3NcIn0iLCJleHAiOjE1NjQ2Njc0MTN9.9uPgCu7akr21qkle6kApaLjrdsXENAOduPnkx9xj2xk";
        */
 /*
        String rutaKeystore = args[0];
        String contrasenaKeystore = args[1];
        String secretoKey = args[2];
        String secretoKeystore = args[3];
        String valorEncriptar = args[4];
*/

        //String tokenRaw = "eyJ0eXAiOiJFUzI1NiIsImFsZyI6IkhTMjU2In0.eyJzdWIiOiJ7XCJhdWRcIjpcInRQYWdvXCIsXCJzdWJcIjpcIjEwMDgwXCIsXCJpc3NcIjpcIkdDU1wiLFwiZXhwXCI6XCJUaHUgQXVnIDAxIDA5OjUwOjEzIEJPVCAyMDE5XCIsXCJpYXRcIjpcIlRodSBBdWcgMDEgMDk6NDA6MTMgQk9UIDIwMTlcIixcImp0aVwiOlwiOTdhZjQ0ZTYtOWE3NC00NzE3LTg0NTYtMzNiNWI5OWM0ZjI1XCIsXCJtZXJjaGFudC1kZXNjcmlwdGlvblwiOlwiRWxlY3Ryb2RvbWVzdGljb3NcIn0iLCJleHAiOjE1NjQ2Njc0MTN9.9uPgCu7akr21qkle6kApaLjrdsXENAOduPnkx9xj2xk";

        //System.out.println("\n");
        //Palabra inicial antes de encriptar
        //String palabraInicial = DatatypeConverter.parseAnySimpleType("KeyParamMannIndia%");
        //System.out.println("PALABRA INICIAL PARA LLAVE ==>" + palabraInicial);
        //System.out.println("\n");


        //Se genera clave secreta
        //SecretKey secretKey = generateSecretKey(palabraInicial, new byte[12]);

        //Se encripta la data del token utilizando la llave privada
        //byte[] encriptedToken = encryptData(secretKey, tokenRaw.getBytes());
        //System.out.println("MENSAJE ENCRIPTADO USANDO LLAVE PRIVADA ==> " + Base64.getEncoder().encodeToString(encriptedToken));
        //System.out.println("\n");

        //inicializo y guardo la clave en el keystore
        //new Main().storeSecretKey(secretKey);

        //leo y cargo secret key desde el keystore
 //       SecretKeySpec secretKeySpec = new Main().loadSecretKeyFromPath(rutaKeystore, contrasenaKeystore, secretoKey, secretoKeystore);

        //retornar llave original a partir de llave almacenada en keystore
//        SecretKey originalKey = new SecretKeySpec(secretKeySpec.getEncoded(), "AES");
        //Genero la llave a partir de la llave secreta que me devuelve keystore
        //SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        //SecretKey secretKey = factory.translateKey(secretKeySpec);
        //SecretKey secretKey = new SecretKeySpec(factory.translateKey(secretKeySpec), "AES");

        //Un token de ejemplo antes de encriptar
 //       System.out.println("DATA TOKEN ANTES DE ENCRIPTAR ==>" + valorEncriptar);
  //      System.out.println("\n");

        //Se encripta la data del token utilizando la llave privada
  //      byte[] encriptedToken = encryptData(originalKey, valorEncriptar.getBytes());
 //       System.out.println("MENSAJE ENCRIPTADO USANDO LLAVE PRIVADA ==> " + Base64.getEncoder().encodeToString(encriptedToken));
 //       System.out.println("\n");

        //Se desdencripta la data encriptada utilizando la misma llave
        //byte[] desencriptedToken = decryptData(originalKey, encriptedToken);
        //final String mensajeDesencriptado = Base64.getEncoder().encodeToString(desencriptedToken);
        //System.out.println(Base64.getDecoder());


        //String decodedString = new String(desencriptedToken);
        //System.out.println("MENSAJE DESENCRIPTADO USANDO LLAVE PRIVADA ==> " + decodedString);
        //System.out.println("\n");

        //Desencriptado cuando viene desde el web service en tipo string
       // String keyWs = "6nvtDUpRzly8OG1DJnVmEcsurDd5z8qLhxOmj7UjL4A=";

        //genera secret key a partir de secretKeySpec
        //final byte[] symKeyData = DatatypeConverter.parseBase64Binary(keyWs);
        //final SecretKeySpec symKey = new SecretKeySpec(symKeyData, "AES");

        //SecretKey llave = new SecretKeySpec(DatatypeConverter.parseBase64Binary(keyWs), "AES");

        //Se desdencripta la data encriptada utilizando la misma llave
        //byte[] desencriptedToken = decryptData(llave, encriptedToken);
        //final String mensajeDesencriptado = Base64.getEncoder().encodeToString(desencriptedToken);

        //String decodedString = new String(desencriptedToken);
        //System.out.println("MENSAJE DESENCRIPTADO USANDO LLAVE PRIVADA ==> " + decodedString);
        //System.out.println("\n");



    }
}

//}
