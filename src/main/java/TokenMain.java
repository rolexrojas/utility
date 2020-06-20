import tokengenerator.TokenGenerator;

import javax.jms.JMSException;

public class TokenMain {

    public static void main(String[] args) throws JMSException {

       // String tokenGenerator =  TokenGenerator.generateToken(6240L,8094380771L, null);
        //System.out.println(tokenGenerator);
        String creditCardAlias = "DIFERI 6123";

        if(!creditCardAlias.contains("DIFERI")) {
            System.out.println("INSERTALO los otros");
        }

    }
}
