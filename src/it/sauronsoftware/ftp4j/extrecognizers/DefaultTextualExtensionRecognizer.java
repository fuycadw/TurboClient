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

package it.sauronsoftware.ftp4j.extrecognizers;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

/**
 * <p>
 * This is the default FTPTextualExtensionRecognizer for every new FTPClient
 * object. It recognizes as textual these extensions:
 * </p>
 * <p/>
 * <pre>
 * abc acgi aip asm asp c c cc cc com conf cpp csh css cxx def el etx f f f77
 * f90 f90 flx for for g h h hh hh hlb htc htm html htmls htt htx idc jav jav
 * java java js ksh list log lsp lst lsx m m mar mcf p pas php pl pl pm py rexx
 * rt rt rtf rtx s scm scm sdml sgm sgm sgml sgml sh shtml shtml spc ssi talk
 * tcl tcsh text tsv txt uil uni unis uri uris uu uue vcs wml wmls wsc xml zsh
 * </pre>
 * <p/>
 * <p>
 * These extensions are loaded from the file textualexts within the package. The
 * file can be manipulated to add or remove extensions, but it's more convenient
 * to plug a ParametricTextualExtensionRecognizer instance in the client.
 * </p>
 *
 * @author Carlo Pelliccia
 */
public class DefaultTextualExtensionRecognizer extends
        ParametricTextualExtensionRecognizer {

    /**
     * Lock object.
     */
    private static final Object lock = new Object();

    /**
     * The singleton instance.
     */
    private static DefaultTextualExtensionRecognizer instance = null;

    /**
     * This one returns the default instance of the class.
     *
     * @return An instance of the class.
     */
    public static DefaultTextualExtensionRecognizer getInstance() {
        synchronized (lock) {
            if (instance == null) {
                instance = new DefaultTextualExtensionRecognizer();
            }
        }
        return instance;
    }

    /**
     * It builds the instance.
     */
    private DefaultTextualExtensionRecognizer() {
        BufferedReader r = null;
        try {
            r = new BufferedReader(new InputStreamReader(getClass()
                    .getResourceAsStream("textualexts")));
            String line;
            while ((line = r.readLine()) != null) {
                StringTokenizer st = new StringTokenizer(line);
                while (st.hasMoreTokens()) {
                    addExtension(st.nextToken());
                }
            }
        } catch (Exception e) {
            ;
        } finally {
            if (r != null) {
                try {
                    r.close();
                } catch (Throwable t) {
                    ;
                }
            }
        }
    }

}
