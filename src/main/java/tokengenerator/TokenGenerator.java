package tokengenerator;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.Jwts;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class TokenGenerator {

    public static String generateToken(Long accountId, Long msisdn, String imei) {

        String jwtPayLoad = generateJwtPayload(accountId, msisdn, imei);

        // TODO what exactly
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.add(Calendar.MINUTE, 50000);

        return Jwts.builder()
                .setSubject(jwtPayLoad)
                .setExpiration(calendar.getTime())
                .signWith(SignatureAlgorithm.HS256, "gcs".getBytes())
                .compact();
    }


    public static String generateJwtPayload(Long accountId, Long msisdn, String imei) {
        return accountId +
                ":" +
                msisdn +
                ":" +
                imei;
    }
}
