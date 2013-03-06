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

/**
 * This interface describes how to build objects used to intercept any
 * communication between the client and the server. It is useful to catch what
 * happens behind. A FTPCommunicationListener can be added to any FTPClient
 * object by calling its addCommunicationListener() method.
 *
 * @author Carlo Pelliccia
 * @see it.sauronsoftware.ftp4j.FTPClient#addCommunicationListener(it.sauronsoftware.ftp4j.FTPCommunicationListener)
 */
public interface FTPCommunicationListener {

    /**
     * Called every time a telnet statement has been sent over the network to
     * the remote FTP server.
     *
     * @param statement The statement that has been sent.
     */
    public void sent(String statement);

    /**
     * Called every time a telnet statement is received by the client.
     *
     * @param statement The received statement.
     */
    public void received(String statement);

}
