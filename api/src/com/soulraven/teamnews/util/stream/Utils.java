package com.soulraven.teamnews.util.stream;

import java.io.InputStream;
import java.io.OutputStream;

public final class Utils {

    private Utils() { }

    public static void copyStream(final InputStream is, final OutputStream os) {
        final int buffer_size = 1024;
        try {
            byte[] bytes = new byte[buffer_size];
            for ( ; ; ) {
                int count = is.read(bytes, 0, buffer_size);
                if (count == -1)
                    break;
                os.write(bytes, 0, count);
            }
        } catch (Exception ex) {
        }
    }
}