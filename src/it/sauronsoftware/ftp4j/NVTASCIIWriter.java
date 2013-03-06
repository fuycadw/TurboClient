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


package it.sauronsoftware.ftp4j;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.StringTokenizer;

/**
 * This is an NVT-ASCII character stream writer.
 *
 * @author Carlo Pelliccia
 * @version 1.1
 */
class NVTASCIIWriter extends Writer {

    /**
     * NTV line separator.
     */
    private static final String LINE_SEPARATOR = "\r\n";

    /**
     * The wrapped stream.
     */
    private OutputStream stream;

    /**
     * The underlying writer.
     */
    private Writer writer;

    /**
     * Builds the writer.
     *
     * @param stream      The underlying stream.
     * @param charsetName The name of a supported charset.
     * @throws java.io.IOException If an I/O error occurs.
     */
    public NVTASCIIWriter(OutputStream stream, String charsetName)
            throws IOException {
        this.stream = stream;
        this.writer = new OutputStreamWriter(stream, charsetName);
    }

    /**
     * Causes this writer to be closed.
     *
     * @throws java.io.IOException
     */
    public void close() throws IOException {
        synchronized (this) {
            writer.close();
        }
    }

    public void flush() throws IOException {
        synchronized (this) {
            writer.flush();
        }
    }

    public void write(char[] cbuf, int off, int len) throws IOException {
        synchronized (this) {
            writer.write(cbuf, off, len);
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
            writer = new OutputStreamWriter(stream, charsetName);
        }
    }

    /**
     * Writes a line in the stream.
     *
     * @param str The line.
     * @throws java.io.IOException If an I/O error occurs.
     */
    public void writeLine(String str) throws IOException {
        StringBuilder buffer = new StringBuilder();
        boolean atLeastOne = false;
        StringTokenizer st = new StringTokenizer(str, LINE_SEPARATOR);
        int count = st.countTokens();
        for (int i = 0; i < count; i++) {
            String line = st.nextToken();
            if (line.length() > 0) {
                if (atLeastOne) {
                    buffer.append('\r');
                    buffer.append((char) 0);
                }
                buffer.append(line);
                atLeastOne = true;
            }
        }
        if (buffer.length() > 0) {
            String statement = buffer.toString();
            // Sends the statement to the server.
            writer.write(statement);
            writer.write(LINE_SEPARATOR);
            writer.flush();
        }
    }

}
