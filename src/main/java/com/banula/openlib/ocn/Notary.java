package com.banula.openlib.ocn;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jayway.jsonpath.*;
import com.banula.openlib.ocn.model.ValuesToSign;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.Hash;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class Notary {

    public List<String> fields = new ArrayList<>();
    public String hash = "";
    public String rsv = "";
    public String signatory = "";
    public List<Rewrite> rewrites = new ArrayList<>();

    private static final Configuration jsonPathConfig = Configuration.defaultConfiguration()
            .addOptions(Option.SUPPRESS_EXCEPTIONS);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.findAndRegisterModules();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    public static byte[] compress(byte[] input) throws Exception {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try (GZIPOutputStream gzipOutputStream = new GZIPOutputStream(byteArrayOutputStream)) {
            gzipOutputStream.write(input);
        }
        return byteArrayOutputStream.toByteArray();
    }

    public static byte[] decompress(byte[] input) throws Exception {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try (GZIPInputStream gzipInputStream = new GZIPInputStream(new ByteArrayInputStream(input))) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = gzipInputStream.read(buffer)) > 0) {
                byteArrayOutputStream.write(buffer, 0, length);
            }
        }
        return byteArrayOutputStream.toByteArray();
    }

    public static Notary deserialize(String ocnSignature) throws Exception {
        byte[] decoded = Base64.getDecoder().decode(ocnSignature);
        byte[] decompressed = decompress(decoded);
        return objectMapper.readValue(decompressed, Notary.class);
    }

    public String serialize() throws Exception {
        byte[] serialized = objectMapper.writeValueAsBytes(this);
        byte[] compressed = compress(serialized);
        return Base64.getEncoder().encodeToString(compressed);
    }

    public Notary sign(ValuesToSign<?> valuesToSign, String privateKey) {
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        Credentials credentials = Credentials.create(privateKey);
        fields = new ArrayList<>();
        String message = walk("$", valuesToSign);
        hash = Hash.sha3String(message);
        rsv = Helpers.signStringMessage(hash, credentials.getEcKeyPair());
        signatory = credentials.getAddress();
        return this;
    }

    // verify signature that comes from the other party, or from OCN node in case of
    // changing body content (Sessions module for example)
    // TODO use a requests of sessions module to test if signature comes from OCN
    // node indeed and other fields are nested in the body
    public boolean verifySignature() throws Exception {
        String signerPublicAddress = Helpers.signerOfMessage(hash, rsv);
        return Helpers.toChecksumAddress(signatory).equals(Helpers.toChecksumAddress(signerPublicAddress));
    }

    private String walk(String jsonPath, Object value) {
        return walk(jsonPath, value, "");
    }

    private String walk(String jsonPath, Object value, String message) {
        String mutableMsg = message;

        if (value != null && !"".equals(value)) {
            if (value instanceof List) {
                mutableMsg = walkThroughListLike(jsonPath, (List<?>) value, mutableMsg);
            } else if (value instanceof Set) {
                mutableMsg = walkThroughListLike(jsonPath, new ArrayList<>((Set<?>) value), mutableMsg);
            } else if (value instanceof Object[]) {
                mutableMsg = walkThroughListLike(jsonPath, Arrays.asList((Object[]) value), mutableMsg);
            } else if (value instanceof Map) {
                for (Map.Entry<?, ?> entry : ((Map<?, ?>) value).entrySet()) {
                    mutableMsg = walk(jsonPath + "['" + entry.getKey() + "']", entry.getValue(), mutableMsg);
                }
            } else if (value instanceof String || value instanceof Boolean || value instanceof Number
                    || value instanceof Character) {
                // Assuming fields is a class member List<String>
                fields.add(jsonPath);
                mutableMsg += value;
            } else {
                // convert custom types to map
                try {
                    String valueAsJsonString = objectMapper.writeValueAsString(value);
                    Map<String, Object> valueAsMap = objectMapper.readValue(valueAsJsonString,
                            new TypeReference<Map<String, Object>>() {
                            });
                    mutableMsg = walk(jsonPath, valueAsMap, mutableMsg);
                } catch (JsonProcessingException e) {
                    e.printStackTrace(); // Handle the exception appropriately
                }
            }
        }
        return mutableMsg;
    }

    private String walkThroughListLike(String jsonPath, List<?> value, String message) {
        String mutableMsg = message;
        for (int index = 0; index < value.size(); index++) {
            Object subValue = value.get(index);
            mutableMsg = walk(jsonPath + "[" + index + "]", subValue, mutableMsg);
        }
        return mutableMsg;
    }
}