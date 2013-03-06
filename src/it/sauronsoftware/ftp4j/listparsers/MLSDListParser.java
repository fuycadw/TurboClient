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


package it.sauronsoftware.ftp4j.listparsers;

import it.sauronsoftware.ftp4j.FTPFile;
import it.sauronsoftware.ftp4j.FTPListParseException;
import it.sauronsoftware.ftp4j.FTPListParser;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import java.util.StringTokenizer;

/**
 * This parser can handle the standard MLST/MLSD responses (RFC 3659).
 *
 * @author Carlo Pelliccia
 * @since 1.5
 */
public class MLSDListParser implements FTPListParser {

    /**
     * Date format 1 for MLSD date facts (supports millis).
     */
    private static final DateFormat MLSD_DATE_FORMAT_1 = new SimpleDateFormat("yyyyMMddhhmmss.SSS Z");

    /**
     * Date format 2 for MLSD date facts (doesn't support millis).
     */
    private static final DateFormat MLSD_DATE_FORMAT_2 = new SimpleDateFormat("yyyyMMddhhmmss Z");

    public FTPFile[] parse(String[] lines) throws FTPListParseException {
        ArrayList list = new ArrayList();
        for (String line : lines) {
            FTPFile file = parseLine(line);
            if (file != null) {
                list.add(file);
            }
        }
        int size = list.size();
        FTPFile[] ret = new FTPFile[size];
        for (int i = 0; i < size; i++) {
            ret[i] = (FTPFile) list.get(i);
        }
        return ret;
    }

    /**
     * Parses a line ad a MLSD response element.
     *
     * @param line The line.
     * @return The file, or null if the line has to be ignored.
     * @throws it.sauronsoftware.ftp4j.FTPListParseException
     *          If the line is not a valid MLSD entry.
     */
    private FTPFile parseLine(String line) throws FTPListParseException {
        // Divides facts and name.
        ArrayList list = new ArrayList();
        StringTokenizer st = new StringTokenizer(line, ";");
        while (st.hasMoreElements()) {
            String aux = st.nextToken().trim();
            if (aux.length() > 0) {
                list.add(aux);
            }
        }
        if (list.size() == 0) {
            throw new FTPListParseException();
        }
        // Extracts the file name.
        String name = (String) list.remove(list.size() - 1);
        // Parses the facts.
        Properties facts = new Properties();
        for (Object aList : list) {
            String aux = (String) aList;
            int sep = aux.indexOf('=');
            if (sep == -1) {
                throw new FTPListParseException();
            }
            String key = aux.substring(0, sep).trim();
            String value = aux.substring(sep + 1, aux.length()).trim();
            if (key.length() == 0 || value.length() == 0) {
                throw new FTPListParseException();
            }
            facts.setProperty(key, value);
        }
        // Type.
        int type;
        String typeString = facts.getProperty("type");
        if (typeString == null) {
            throw new FTPListParseException();
        } else if ("file".equalsIgnoreCase(typeString)) {
            type = FTPFile.TYPE_FILE;
        } else if ("dir".equalsIgnoreCase(typeString)) {
            type = FTPFile.TYPE_DIRECTORY;
        } else if ("cdir".equalsIgnoreCase(typeString)) {
            // Current directory. Skips...
            return null;
        } else if ("pdir".equalsIgnoreCase(typeString)) {
            // Parent directory. Skips...
            return null;
        } else {
            // Unknown... (link?)... Skips...
            return null;
        }
        // Last modification date.
        Date modifiedDate = null;
        String modifyString = facts.getProperty("modify");
        if (modifyString != null) {
            modifyString += " +0000";
            try {
                synchronized (MLSD_DATE_FORMAT_1) {
                    modifiedDate = MLSD_DATE_FORMAT_1.parse(modifyString);
                }
            } catch (ParseException e1) {
                try {
                    synchronized (MLSD_DATE_FORMAT_2) {
                        modifiedDate = MLSD_DATE_FORMAT_2.parse(modifyString);
                    }
                } catch (ParseException e2) {
                    ;
                }
            }
        }
        // Size.
        long size = 0;
        String sizeString = facts.getProperty("size");
        if (sizeString != null) {
            try {
                size = Long.parseLong(sizeString);
            } catch (NumberFormatException e) {
                ;
            }
            if (size < 0) {
                size = 0;
            }
        }
        // Done!
        FTPFile ret = new FTPFile();
        ret.setType(type);
        ret.setModifiedDate(modifiedDate);
        ret.setSize(size);
        ret.setName(name);
        return ret;
    }

}
