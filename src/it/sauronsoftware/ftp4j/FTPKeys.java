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
 * Static property keys used by the library.
 *
 * @author Carlo Pelliccia
 * @since 1.3
 */
interface FTPKeys {

    /**
     * The key used to retrieve the system property with the port range for
     * active data transfers. The value has to be in the
     * <em>startPort-endPort</em> form.
     */
    public String ACTIVE_DT_PORT_RANGE = "ftp4j.activeDataTransfer.portRange";

    /**
     * The key used to retrieve the system property with the host IPv4 address
     * for active data transfers. The value has to be in the <em>x.x.x.x</em>
     * form.
     */
    public String ACTIVE_DT_HOST_ADDRESS = "ftp4j.activeDataTransfer.hostAddress";

    /**
     * The key used to retrieve the system property with the accept timeout for
     * active data transfars. The value should be ms. Default value is 30000. A
     * 0 value stands for infinite.
     */
    public String ACTIVE_DT_ACCEPT_TIMEOUT = "ftp4j.activeDataTransfer.acceptTimeout";

    /**
     * The key used to retrieve the system property that can force the client to
     * exchange data by connecting to the IP address suggested by the server
     * after a PASV command. To avoid frequently reported NAT problems, ftp4j
     * connects always to the host supplied in the
     * {@link it.sauronsoftware.ftp4j.FTPClient#connect(String)} or
     * {@link it.sauronsoftware.ftp4j.FTPClient#connect(String, int)} methods. The response of a PASV
     * command is used only to decode the port for the connection. By using the
     * value &quot;true&quot;, &quot;yes&quot; or &quot;1&quot; on this system
     * property, ftp4j will change its behaviour and it will connect to the IP
     * address returned from the server.
     *
     * @since 1.5
     */
    public String PASSIVE_DT_USE_SUGGESTED_ADDRESS = "ftp4j.passiveDataTransfer.useSuggestedAddress";

}
