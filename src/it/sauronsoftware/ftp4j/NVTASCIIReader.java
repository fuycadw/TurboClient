/*******************************************************************************
 * Copyright (c) 2012, 2013 Vlad Mihalachi
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 *
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

/*
 * Copyright (c) 2013 Vlad Mihalachi.
 */

package it.sauronsoftware.ftp4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * This is an NVT-ASCII character stream reader.
 *
 * @author Carlo Pelliccia
 * @version 1.1
 */
class NVTASCIIReader extends Reader {

    /**
     * This system line separator chars sequence.
     */
    private static final String SYSTEM_LINE_SEPARATOR = System
            .getProperty("line.separator");

    /**
     * The wrapped stream.
     */
    private InputStream stream;

    /**
     * The underlying reader.
     */
    private Reader reader;

    /**
     * Builds the reader.
     *
     * @param stream      The underlying stream.
     * @param charsetName The name of a supported charset.
     * @throws java.io.IOException If an I/O error occurs.
     */
    public NVTASCIIReader(InputStream stream, String charsetName)
            throws IOException {
        this.stream = stream;
        reader = new InputStreamReader(stream, charsetName);
    }

    public void close() throws IOException {
        synchronized (this) {
            reader.close();
        }
    }

    public int read(char[] cbuf, int off, int len) throws IOException {
        synchronized (this) {
            return reader.read(cbuf, off, len);
        }
    }

    /**
     * Changes the current charset.
     *
     * @param charsetName The new charset.
     * @throws java.io.IOException If I/O error occurs.
     * @since 1.1
     */
    public void changeCharset(String charsetName) throws IOException {
        synchronized (this) {
            reader = new InputStreamReader(stream, charsetName);
        }
    }

    /**
     * Reads a line from the stream.
     *
     * @return The line read, or null if the end of the stream is reached.
     * @throws java.io.IOException If an I/O error occurs.
     */
    public String readLine() throws IOException {
        StringBuilder buffer = new StringBuilder();
        int previous = -1;
        int current = -1;
        do {
            int i = reader.read();
            if (i == -1) {
                if (buffer.length() == 0) {
                    return null;
                } else {
                    return buffer.toString();
                }
            }
            previous = current;
            current = i;
            if (/* previous == '\r' && */current == '\n') {
                // End of line.
                return buffer.toString();
            } else if (previous == '\r' && current == 0) {
                // Literal new line.
                buffer.append(SYSTEM_LINE_SEPARATOR);
            } else if (current != 0 && current != '\r') {
                buffer.append((char) current);
            }
        } while (true);
    }

}
