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
 * This class represents FTP server replies in a manageable object oriented way.
 *
 * @author Carlo Pelliccia
 */
public class FTPReply {

    /**
     * The reply code.
     */
    private int code = 0;

    /**
     * The reply message(s).
     */
    private String[] messages;

    /**
     * Build the reply.
     *
     * @param code    The code of the reply.
     * @param message The textual message(s) in the reply.
     */
    FTPReply(int code, String[] messages) {
        this.code = code;
        this.messages = messages;
    }

    /**
     * Returns the code of the reply.
     *
     * @return The code of the reply.
     */
    public int getCode() {
        return code;
    }

    /**
     * Returns true if the code of the reply is in the range of success codes
     * (2**).
     *
     * @return true if the code of the reply is in the range of success codes
     *         (2**).
     */
    public boolean isSuccessCode() {
        int aux = code - 200;
        return aux >= 0 && aux < 100;
    }

    /**
     * Returns the textual message(s) of the reply.
     *
     * @return The textual message(s) of the reply.
     */
    public String[] getMessages() {
        return messages;
    }

    public String toString() {
        StringBuilder buffer = new StringBuilder();
        buffer.append(getClass().getName());
        buffer.append(" [code=");
        buffer.append(code);
        buffer.append(", message=");
        for (int i = 0; i < messages.length; i++) {
            if (i > 0) {
                buffer.append(" ");
            }
            buffer.append(messages[i]);
        }
        buffer.append("]");
        return buffer.toString();
    }

}
