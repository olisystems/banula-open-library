package com.banula.ocn;

import com.banula.ocn.model.SignableHeaders;
import com.banula.ocn.model.ValuesToSign;
import org.junit.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.springframework.http.HttpHeaders;
import org.web3j.crypto.Credentials;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class HelpersTest {
    private static String message = "hello, world!";
    private static String signature = "0x1ac5512e04b65b000c4313ca2850cc32b88ca856245ba05b03e167b35e27abb50f12377e6b3b6919499b3855deff3a3cdbc34140bbe5b014ddd5d20c672c1d2a1b";
    private static Credentials credentials = Credentials.create("0x0138006a73a6187729ef2a4acfcd39b5d5dfb4f28a37c7911113ae380b51b5d5");

    private static String privateKey = "c87509a1c067bbde78beb793e6fa76530b6382a4c0241e5e4a9ec0a0f44dc0d3";

    @Test
    public void test_signStringMessage_plainText() {
        String result = Helpers.signStringMessage(message, credentials.getEcKeyPair());
        assertEquals(signature, result);
    }

    @Test
    public void test_verifyMessage() throws Exception {
        String result = Helpers.signerOfMessage(message, signature);
        assertEquals(credentials.getAddress(), result);
    }

    @Test
    public void test_verifyRequest_linkedHashMap() throws Exception {
        SignableHeaders headers = new SignableHeaders();
        headers.setCorrelationId("456");
        headers.setFromCountryCode("DE");
        headers.setFromPartyId("ABC");
        headers.setToCountryCode("DE");
        headers.setToPartyId("XYZ");

        HashMap<String, String> body = new LinkedHashMap<>();
        body.put("id","1");
        body.put("city", "Essen");

        ValuesToSign values = new ValuesToSign();

        values.headers = headers;
        values.body = body;

        Notary notary = new Notary();
        String signature = notary.sign(values, privateKey).serialize();

        String expectedResult = "GwwCAORhynr9ca0sQtLXty4gROuqHepNpxyBtyWWtGXNb1c4OB4ccnag9gVY21/ok721DYYRDseTGza/ndyNJ7+ujmjv3nif5o+rqZ3cwaU2zPo5XfV1fr8aE4//vtK5max+8rr/buT3GbzPFDK33yd340n6Z5323yd3cHcJz7w9wwDul68hB4pFuaaEyBwT1s5F1Qt6b7FXtICUqdcUe2BzvbEqM2PwqcElrNs3DOB+5e4TGjp2yNkau5CCkJLmXixINyTNpCFo9JJj5iLeUatdfAkxkFcWH1NVFLSM1kqvGh1zR0xUexOMFKrLRJU1GcUmqLk1qVoqNi9wCdv09MH7vP6GAdyvHCi67KpjYYmczSfnrMaEotkpFS5iHQkuYbWfddptg2G8+w8=";

        assertEquals(signature, expectedResult);
    }

    @Test
    public void test_verifyRequest_genericObject() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Correlation-ID", "456");
        headers.set("OCPI-from-country-code", "DE");
        headers.set("OCPI-from-party-id", "ABC");
        headers.set("OCPI-to-country-code", "DE");
        headers.set("OCPI-to-party-id", "XYZ");

        GenericOcnBody body = new GenericOcnBody();
        body.city = "Essen";
        body.id = "1";

        ValuesToSign values = new ValuesToSign(headers.toSingleValueMap(), body, null);

        Notary notary = new Notary();
        String signature = notary.sign(values, privateKey).serialize();

        String expectedResult = "GwwCAORhynr9ca0sQtLXty4gROuqHepNpxyBtyWWtGXNb1c4OB4ccnag9gVY21/ok721DYYRDseTGza/ndyNJ7+ujmjv3nif5o+rqZ3cwaU2zPo5XfV1fr8aE4//vtK5max+8rr/buT3GbzPFDK33yd340n6Z5323yd3cHcJz7w9wwDul68hB4pFuaaEyBwT1s5F1Qt6b7FXtICUqdcUe2BzvbEqM2PwqcElrNs3DOB+5e4TGjp2yNkau5CCkJLmXixINyTNpCFo9JJj5iLeUatdfAkxkFcWH1NVFLSM1kqvGh1zR0xUexOMFKrLRJU1GcUmqLk1qVoqNi9wCdv09MH7vP6GAdyvHCi67KpjYYmczSfnrMaEotkpFS5iHQkuYbWfddptg2G8+w8=";

        assertEquals(signature, expectedResult);
    }
}
