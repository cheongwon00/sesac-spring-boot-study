package kr.co.private_box.domain;

import java.security.SecureRandom;

public class PrivateUrlFactory {
    private static final String base56Characters = "23456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnpqrstuvwxyz";
    private static final int URL_LENGTH = 8;
    private static final SecureRandom RANDOM = new SecureRandom();

    public static String generatePrivateUrl(){
        StringBuilder shortenUrlKey = new StringBuilder();
        for(int count=0; count<URL_LENGTH; count++){
            int base56CharactersIndex = RANDOM.nextInt(0,base56Characters.length());
            char base56Character = base56Characters.charAt(base56CharactersIndex);
            shortenUrlKey.append(base56Character);
        }
        return shortenUrlKey.toString();
    }
}
