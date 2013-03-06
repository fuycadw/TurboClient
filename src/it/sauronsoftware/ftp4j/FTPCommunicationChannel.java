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

import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;

/**
 * This class is used to represent a communication channel with a FTP server.
 *
 * @author Carlo Pelliccia
 * @version 1.1
 */
public class FTPCommunicationChannel {

    /**
     * The FTPCommunicationListener objects registered on the channel.
     */
    private final ArrayList communicationListeners = new ArrayList();

    /**
     * The connection.
     */
    private Socket connection = null;

    /**
     * The name of the charset that has to be used to encode and decode the
     * communication.
     */
    private String charsetName = null;

    /**
     * The stream-reader channel established with the remote server.
     */
    private NVTASCIIReader reader = null;

    /**
     * The stream-writer channel established with the remote server.
     */
    private NVTASCIIWriter writer = null;

    /**
     * It builds a FTP communication channel.
     *
     * @param connection  The underlying connection.
     * @param charsetName The name of the charset that has to be used to encode and
     *                    decode the communication.
     * @throws java.io.IOException If a I/O error occurs.
     */
    public FTPCommunicationChannel(Socket connection, String charsetName)
            throws IOException {
        this.connection = connection;
        this.charsetName = charsetName;
        InputStream inStream = connection.getInputStream();
        OutputStream outStream = connection.getOutputStream();
        // Wrap the streams into reader and writer objects.
        reader = new NVTASCIIReader(inStream, charsetName);
        writer = new NVTASCIIWriter(outStream, charsetName);
    }

    /**
     * This method adds a FTPCommunicationListener to the object.
     *
     * @param listener The listener.
     */
    public void addCommunicationListener(FTPCommunicationListener listener) {
        communicationListeners.add(listener);
    }

    /**
     * This method removes a FTPCommunicationListener previously added to the
     * object.
     *
     * @param listener The listener to be removed.
     */
    public void removeCommunicationListener(FTPCommunicationListener listener) {
        communicationListeners.remove(listener);
    }

    /**
     * Closes the channel.
     */
    public void close() {
        try {
            connection.close();
        } catch (Exception e) {
            ;
        }
    }

    /**
     * This method returns a list with all the FTPCommunicationListener used by
     * the client.
     *
     * @return A list with all the FTPCommunicationListener used by the client.
     */
    public FTPCommunicationListener[] getCommunicationListeners() {
        int size = communicationListeners.size();
        FTPCommunicationListener[] ret = new FTPCommunicationListener[size];
        for (int i = 0; i < size; i++) {
            ret[i] = (FTPCommunicationListener) communicationListeners.get(i);
        }
        return ret;
    }

    /**
     * This method reads a line from the remote server.
     *
     * @return The string read.
     * @throws java.io.IOException If an I/O error occurs during the operation.
     */
    private String read() throws IOException {
        // Read the line from the server.
        String line = reader.readLine();
        if (line == null) {
            throw new IOException("FTPConnection closed");
        }
        // Call received() method on every communication listener
        // registered.
        for (Object communicationListener : communicationListeners) {
            FTPCommunicationListener l = (FTPCommunicationListener) communicationListener;
            l.received(line);
        }
        // Return the line read.
        return line;
    }

    /**
     * This method sends a command line to the server.
     *
     * @param command The command to be sent.
     * @throws java.io.IOException If an I/O error occurs.
     */
    public void sendFTPCommand(String command) throws IOException {
        writer.writeLine(command);
        for (Object communicationListener : communicationListeners) {
            FTPCommunicationListener l = (FTPCommunicationListener) communicationListener;
            l.sent(command);
        }
    }

    /**
     * This method reads and parses a FTP reply statement from the server.
     *
     * @return The reply from the server.
     * @throws java.io.IOException If an I/O error occurs.
     * @throws it.sauronsoftware.ftp4j.FTPIllegalReplyException
     *                             If the server doesn't reply in a FTP-compliant way.
     */
    public FTPReply readFTPReply() throws IOException, FTPIllegalReplyException {
        int code = 0;
        ArrayList messages = new ArrayList();
        do {
            String statement;
            do {
                statement = read();
            } while (statement.trim().length() == 0);
            if (statement.startsWith("\n")) {
                statement = statement.substring(1);
            }
            int l = statement.length();
            if (code == 0 && l < 3) {
                throw new FTPIllegalReplyException();
            }
            int aux;
            try {
                aux = Integer.parseInt(statement.substring(0, 3));
            } catch (Exception e) {
                if (code == 0) {
                    throw new FTPIllegalReplyException();
                } else {
                    aux = 0;
                }
            }
            if (code != 0 && aux != 0 && aux != code) {
                throw new FTPIllegalReplyException();
            }
            if (code == 0) {
                code = aux;
            }
            if (aux > 0) {
                if (l > 3) {
                    char s = statement.charAt(3);
                    String message = statement.substring(4, l);
                    messages.add(message);
                    if (s == ' ') {
                        break;
                    } else if (s == '-') {
                    } else {
                        throw new FTPIllegalReplyException();
                    }
                } else if (l == 3) {
                    break;
                } else {
                    messages.add(statement);
                }
            } else {
                messages.add(statement);
            }
        } while (true);
        int size = messages.size();
        String[] m = new String[size];
        for (int i = 0; i < size; i++) {
            m[i] = (String) messages.get(i);
        }
        return new FTPReply(code, m);
    }

    /**
     * Changes the current charset.
     *
     * @param charsetName The new charset.
     * @throws java.io.IOException If I/O error occurs.
     * @since 1.1
     */
    public void changeCharset(String charsetName) throws IOException {
        this.charsetName = charsetName;
        reader.changeCharset(charsetName);
        writer.changeCharset(charsetName);
    }

    /**
     * Applies SSL encryption to the communication channel.
     *
     * @param sslSocketFactory The SSLSocketFactory used to produce the SSL connection.
     * @throws java.io.IOException If a I/O error occurs.
     * @since 1.4
     */
    public void ssl(SSLSocketFactory sslSocketFactory) throws IOException {
        String host = connection.getInetAddress().getHostName();
        int port = connection.getPort();
        connection = sslSocketFactory.createSocket(connection, host, port, true);
        InputStream inStream = connection.getInputStream();
        OutputStream outStream = connection.getOutputStream();
        reader = new NVTASCIIReader(inStream, charsetName);
        writer = new NVTASCIIWriter(outStream, charsetName);
    }

}
