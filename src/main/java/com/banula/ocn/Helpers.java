package com.banula.ocn;

import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;
import org.web3j.crypto.Sign;
import org.web3j.utils.Numeric;

import java.math.BigInteger;

public class Helpers {

    public static String signStringMessage(String message, ECKeyPair ecKeyPair) {
        byte[] messageBytes = message.getBytes();
        Sign.SignatureData signatureData = Sign.signPrefixedMessage(messageBytes, ecKeyPair);
        String r = Numeric.cleanHexPrefix(Numeric.toHexString(signatureData.getR()));
        String s = Numeric.cleanHexPrefix(Numeric.toHexString(signatureData.getS()));
        String v = Numeric.cleanHexPrefix(Numeric.toHexString(signatureData.getV()));
        return "0x" + r + s + v;
    }

    public static String signerOfMessage(String message, String signature) throws Exception {
        byte[] r = Numeric.hexStringToByteArray(signature.substring(2, 66));
        byte[] s = Numeric.hexStringToByteArray(signature.substring(66, 130));
        byte[] v = Numeric.hexStringToByteArray(signature.substring(130, 132));
        Sign.SignatureData signatureData = new Sign.SignatureData(v, r, s);
        BigInteger signingKey = Sign.signedPrefixedMessageToKey(message.getBytes(), signatureData);
        String address = Keys.getAddress(signingKey).toString();
        return "0x" + address;
    }



    public static String toChecksumAddress(String address) {
        return Keys.getAddress(address);
    }
}