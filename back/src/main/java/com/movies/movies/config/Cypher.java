package com.movies.movies.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

@Configuration
public class Cypher {

    @Bean
    public KeyPair keyPair() throws NoSuchAlgorithmException {
        //Configuración del algoritmo de generación de claves RSA
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        //Se asigna el tamaño de bits con el que se esta cifrando decifrando
        keyPairGenerator.initialize(2048);
        return keyPairGenerator.generateKeyPair();
    }
}
