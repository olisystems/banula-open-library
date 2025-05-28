package com.banula.openlib.web3.utils;

import lombok.extern.slf4j.Slf4j;
import org.web3j.crypto.Hash;
import org.web3j.utils.Numeric;

import java.io.ByteArrayOutputStream;
import java.io.NotSerializableException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

@Slf4j
public class Hashing {
    public static <T extends Serializable> String keccak256Hash(T object) throws Exception {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(object);
            oos.flush();

            byte[] hash = Hash.sha3(bos.toByteArray());
            return Numeric.toHexString(hash);
        } catch (Exception ex) {
            throw new Exception("Error while hashing the object");
        }
    }

    public static String keccak256Hash(String data) {
        byte[] hash = Hash.sha3(data.getBytes());
        return Numeric.toHexString(hash);
    }
}
