import model.BitsDevice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import org.dom4j.*;
import org.dom4j.io.SAXReader;

public class BitNextCheck {



    public static BitsDevice getBitNext(BitsDevice bitsDevice){

        if(bitsDevice.getBitOne() == null && bitsDevice.getBitTwo() == null){
            return new BitsDevice(false,false);
        }else if(!bitsDevice.getBitOne() && !bitsDevice.getBitTwo()){
            return new BitsDevice(false,true);
        }else if (!bitsDevice.getBitOne() && bitsDevice.getBitTwo()){
            return new BitsDevice(true,true);
        }else if (bitsDevice.getBitOne() && !bitsDevice.getBitTwo()){
            return new BitsDevice(false,false);
        }else {
            return new BitsDevice(true,false);
        }
    }

    private static String regenerateContent(HashMap<String, Object> inpMap, String messageBody, String messageParam) {

            String[] messageParamArray = messageParam.split(",");
            ArrayList<Object> list = new ArrayList<Object>();

            for (int messageParamArrayCount = 0; messageParamArrayCount < messageParamArray.length; messageParamArrayCount++) {
                list.add(inpMap.get(messageParamArray[messageParamArrayCount]));
            }
            if (list == null) {
                return messageBody.replaceAll("@###@","");
            }
            Iterator<Object> iter = list.iterator();
            String property;

            while (iter.hasNext()) {

                property = String.valueOf(iter.next());
                if (messageBody.indexOf("@###@") > 0) {
                    messageBody = messageBody.replaceFirst("@###@", property);
                } else {
                    break;
                }
            }

            messageBody = messageBody.replaceAll("@###@", "");
            return messageBody;
    }

    public static void main(String[] args) throws Exception {


        String smsConfigXMLPath = "<message>"
                + "<message-body>Estimado cliente, usted acaba de pagar su servicio @###@ por el monto de Q@###@ utilizando tPago. Transaccion #@###@</message-body>"
                + "<message-parameters>nic,amount,Transaction_ID</message-parameters>"
                + "</message>";

       String svcName = "BILLPAYMENT";
        String messageBody = "Estimado cliente, usted acaba de pagar su servicio @###@ por el monto de Q@###@ utilizando tPago. Transaccion #@###@";
        String messageParam = "nic,amount,transaction_id";

        String messageId = "1";
        //Document document = parse(smsConfigXMLPath);
         //messageBody  = document.valueOf("//message[message-header[service='"+svcName+"'][message-id ='"+messageId+"']]/message-body");
         //messageParam = document.valueOf("//message[message-header[service='"+svcName+"'][message-id ='"+messageId+"']]/message-parameters");
//
        HashMap<String, Object> inpMapHashMap = new HashMap<>();
        inpMapHashMap.put("nic", "2620");
        inpMapHashMap.put("amount", "410");
        inpMapHashMap.put("Transaction_ID", "1282020");
        //String messageBody = "";
         String body = regenerateContent(inpMapHashMap, smsConfigXMLPath, messageParam);
System.out.println(body);



/*
         BitsDevice bitsDevice  = new BitsDevice(null, null);
        BitsDevice bitNextResult = getBitNext(bitsDevice);

        System.out.println("CASO NULL NULL ===   BIT ONE => " + bitNextResult.getBitOne() + "|| BIT TWO => " + bitNextResult.getBitTwo());

        BitsDevice bitsDevice1  = new BitsDevice(true, true);
        BitsDevice bitNextResult1 = getBitNext(bitsDevice1);

        System.out.println("CASO TRUE TRUE ===   BIT ONE => " + bitNextResult1.getBitOne() + "|| BIT TWO => " + bitNextResult1.getBitTwo());

        BitsDevice bitsDevice2  = new BitsDevice(true, false);
        BitsDevice bitNextResult2 = getBitNext(bitsDevice2);

        System.out.println("CASO TRUE FALSE ===    BIT ONE => " + bitNextResult2.getBitOne() + "|| BIT TWO => " + bitNextResult2.getBitTwo());

        BitsDevice bitsDevice3  = new BitsDevice(false, true);
        BitsDevice bitNextResult3 = getBitNext(bitsDevice3);

        System.out.println("CASO FALSE TRUE ===    BIT ONE => " + bitNextResult3.getBitOne() + "|| BIT TWO => " + bitNextResult3.getBitTwo());

        BitsDevice bitsDevice4  = new BitsDevice(false, false);
        BitsDevice bitNextResult4 = getBitNext(bitsDevice4);

        System.out.println("CASO FALSE FALSE ===    BIT ONE => " + bitNextResult4.getBitOne() + "|| BIT TWO => " + bitNextResult4.getBitTwo());*/

    }

    private static Document parse(String smsConfigXMLPath) throws Exception {
        Document document = null;
        try{
            SAXReader reader = new SAXReader();
            document = reader.read(smsConfigXMLPath);
        }catch(Exception e){
            throw(e);
        }
        return document;
    }

}




