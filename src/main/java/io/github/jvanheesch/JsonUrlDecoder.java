package io.github.jvanheesch;

import lzma.sdk.lzma.Decoder;
import lzma.streams.LzmaInputStream;
import org.msgpack.core.MessagePack;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

import static org.apache.commons.io.IOUtils.copy;

public class JsonUrlDecoder {
    public String decode(String jsonUrlOutput) throws IOException {
        try (
                ByteArrayOutputStream output = new ByteArrayOutputStream();
                LzmaInputStream compressedIn = new LzmaInputStream(
                        new BufferedInputStream(new ByteArrayInputStream(Base64.getUrlDecoder().decode(jsonUrlOutput))),
                        new Decoder()
                )
        ) {
            copy(compressedIn, output);

            byte[] decompressed = output.toByteArray();

            String unpacked = MessagePack.newDefaultUnpacker(decompressed)
                    .unpackValue()
                    .toString();
            return unpacked;
        }
    }

    public static void main(String[] args) throws IOException {
        System.out.println(new JsonUrlDecoder().decode("XQAAAAInAAAAAAAAAABBqUpG89Dgqch-2PqNvGIss_u5rozp3jMYFE6lULPGajbOedh61kNZUlTL__9YBAAA"));
    }
}
