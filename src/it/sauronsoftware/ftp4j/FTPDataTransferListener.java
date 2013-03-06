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
 * This interface describes the methods requested by an object that can listen
 * data transfer operations. You can supply an object implementing this
 * interface to any upload/download method of the client.
 *
 * @author Carlo Pelliccia
 */
public interface FTPDataTransferListener {

    /**
     * Called to notify the listener that the transfer operation has been
     * initialized.
     */
    public void started();

    /**
     * Called to notify the listener that some bytes have been transmitted.
     *
     * @param length The number of the bytes transmitted since the last time the
     *               method was called (or since the begin of the operation, at the
     *               first call received).
     */
    public void transferred(int length);

    /**
     * Called to notify the listener that the transfer operation has been
     * successfully complete.
     */
    public void completed();

    /**
     * Called to notify the listener that the transfer operation has been
     * aborted.
     */
    public void aborted();

    /**
     * Called to notify the listener that the transfer operation has failed due
     * to an error.
     */
    public void failed();

}
