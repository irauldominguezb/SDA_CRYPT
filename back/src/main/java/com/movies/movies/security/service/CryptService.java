package com.movies.movies.security.service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

@Service
public class CryptService {
    // Obtiene el valor de la propiedad "crypt.secret" del archivo de configuración
    // y lo asigna a la variable private String secretKey.
    @Value("${crypt.secret}")
    private String secretKey;


    // Define el algoritmo de cifrado simétrico AES con modo de operación CBC
    // y relleno PKCS5Padding. Este algoritmo se utilizará en la encriptación y desencriptación.
    private static final String ALGORITHM = "AES/CBC/PKCS5Padding";

    // Inicializa un vector de inicialización (IV) con un tamaño de 16 bytes.
    // El IV es utilizado en el modo de operación CBC para añadir aleatoriedad
    // a la primera parte del mensaje cifrado, mejorando la seguridad.
    private static final byte[] IV = new byte[16];


    // Genera una clave secreta (SecretKeySpec) a partir de la clave secreta en formato String.
    private SecretKeySpec generateSecretKeyFromString() throws NoSuchAlgorithmException {
        // Obtiene una instancia del algoritmo de resumen criptográfico SHA-256
        MessageDigest sha = MessageDigest.getInstance("SHA-256");

        // Aplica la función hash SHA-256 a la clave secreta y obtener los bytes resultantes
        byte[] keyBytes = sha.digest(secretKey.getBytes(StandardCharsets.UTF_8));

        // Reduce la longitud de los bytes resultantes a 16 bytes utilizando Arrays.copyOf
        keyBytes = Arrays.copyOf(keyBytes, 16);

        // Crea y devuelve un objeto SecretKeySpec utilizando los bytes resultantes
        // como clave y el algoritmo de cifrado AES.
        return new SecretKeySpec(keyBytes, "AES");
    }


    // Encripta un objeto utilizando el algoritmo AES y la clave generada a partir de la clave secreta.
    public String encrypt(Object data) throws Exception {
        // Convierte el objeto de datos a una cadena de texto
        String plaintext = convertToString(data);

        // Genera una clave secreta utilizando el método correspondiente
        SecretKey key = generateSecretKeyFromString();

        // Obtiene una instancia del objeto Cipher para el algoritmo AES
        Cipher cipher = Cipher.getInstance(ALGORITHM);

        // Crea un objeto IvParameterSpec con el vector de inicialización (IV)
        IvParameterSpec ivParams = new IvParameterSpec(IV);

        // Inicializa el Cipher en el modo de cifrado (ENCRYPT_MODE) con la clave y el IV
        cipher.init(Cipher.ENCRYPT_MODE, key, ivParams);

        // Realiza la operación de cifrado sobre la cadena de texto y obtener los bytes cifrados
        byte[] encryptedBytes = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));

        // Codifica los bytes cifrados en formato Base64 y devolver el resultado como una cadena
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }


    // Desencripta un texto cifrado utilizando el algoritmo AES y la clave generada a partir de la clave secreta.
    public String decrypt(String encryptedText) throws Exception {
        // Genera una clave secreta utilizando el método correspondiente
        SecretKey key = generateSecretKeyFromString();

        // Obtiene una instancia del objeto Cipher para el algoritmo AES
        Cipher cipher = Cipher.getInstance(ALGORITHM);

        // Crear un objeto IvParameterSpec con el vector de inicialización (IV)
        IvParameterSpec ivParams = new IvParameterSpec(IV);

        // Inicializa el Cipher en el modo de descifrado (DECRYPT_MODE) con la clave y el IV
        cipher.init(Cipher.DECRYPT_MODE, key, ivParams);

        // Decodifica el texto cifrado en formato Base64 y realizar la operación de descifrado
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedText));

        // Convierte los bytes descifrados a una cadena de texto utilizando UTF-8
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }

    // Esta función valida que los datos sean strings.
    private String convertToString(Object data) {
        if (data instanceof Number) {
            return String.valueOf(data);
        } else if (data instanceof String) {
            return (String) data;
        } else if(data != null){
            return String.valueOf(data);
        }else {
            throw new IllegalArgumentException("Unsupported data type");
        }
    }
}
