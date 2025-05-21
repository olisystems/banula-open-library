package com.banula.web3;

import com.banula.web3.utils.Hashing;
import org.junit.jupiter.api.Test;

import java.io.Serializable;

import static org.junit.jupiter.api.Assertions.assertEquals;


class TestDataObject implements Serializable {
    String field1;
    String field2;
}

public class UtilsTest {

    @Test
    public void test_HashSha3Object() throws Exception {
        TestDataObject data = new TestDataObject();
        data.field1 = "TEST DATA 1";
        data.field2 = "TEST DATA 2";

        String hashedObject = Hashing.keccak256Hash(data);

        assertEquals(hashedObject.length() > 0, true, "Length of hashed object must be greater than 0");
        assertEquals(hashedObject.length() == 66, true, "Length of hashed object must be 32 bytes + 2 bytes prefix (0x)");
    }


}
