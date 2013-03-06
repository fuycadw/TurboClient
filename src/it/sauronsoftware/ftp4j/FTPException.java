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


/**
 * This class helps in represent FTP error codes and messages.
 *
 * @author Carlo Pelliccia
 */
public class FTPException extends Exception {

    private static final long serialVersionUID = 1L;

    private int code;

    private String message;

    public FTPException(int code) {
        this.code = code;
    }

    public FTPException(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public FTPException(FTPReply reply) {
        StringBuilder message = new StringBuilder();
        String[] lines = reply.getMessages();
        for (int i = 0; i < lines.length; i++) {
            if (i > 0) {
                message.append(System.getProperty("line.separator"));
            }
            message.append(lines[i]);
        }
        this.code = reply.getCode();
        this.message = message.toString();
    }

    /**
     * Returns the code of the occurred FTP error.
     *
     * @return The code of the occurred FTP error.
     */
    public int getCode() {
        return code;
    }

    /**
     * Returns the message of the occurred FTP error.
     *
     * @return The message of the occurred FTP error.
     */
    public String getMessage() {
        return message;
    }

    public String toString() {
        return getClass().getName() + " [code=" + code + ", message= "
                + message + "]";
    }

}
