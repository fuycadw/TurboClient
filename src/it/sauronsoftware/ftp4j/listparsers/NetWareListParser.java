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

package it.sauronsoftware.ftp4j.listparsers;

import it.sauronsoftware.ftp4j.FTPFile;
import it.sauronsoftware.ftp4j.FTPListParseException;
import it.sauronsoftware.ftp4j.FTPListParser;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This parser can handle NetWare list responses.
 *
 * @author Carlo Pelliccia
 */
public class NetWareListParser implements FTPListParser {

    private static final Pattern PATTERN = Pattern
            .compile("^(d|-)\\s+\\[.{8}\\]\\s+\\S+\\s+(\\d+)\\s+"
                    + "(?:(\\w{3})\\s+(\\d{1,2}))\\s+(?:(\\d{4})|(?:(\\d{1,2}):(\\d{1,2})))\\s+"
                    + "([^\\\\/*?\"<>|]+)$");

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat(
            "MMM dd yyyy HH:mm", Locale.US);

    public FTPFile[] parse(String[] lines) throws FTPListParseException {
        int size = lines.length;
        // What's the date today?
        Calendar now = Calendar.getInstance();
        // Ok, starts parsing.
        int currentYear = now.get(Calendar.YEAR);
        FTPFile[] ret = new FTPFile[size];
        for (int i = 0; i < size; i++) {
            Matcher m = PATTERN.matcher(lines[i]);
            if (m.matches()) {
                String typeString = m.group(1);
                String sizeString = m.group(2);
                String monthString = m.group(3);
                String dayString = m.group(4);
                String yearString = m.group(5);
                String hourString = m.group(6);
                String minuteString = m.group(7);
                String nameString = m.group(8);
                // Parse the data.
                ret[i] = new FTPFile();
                if (typeString.equals("-")) {
                    ret[i].setType(FTPFile.TYPE_FILE);
                } else if (typeString.equals("d")) {
                    ret[i].setType(FTPFile.TYPE_DIRECTORY);
                } else {
                    throw new FTPListParseException();
                }
                long fileSize;
                try {
                    fileSize = Long.parseLong(sizeString);
                } catch (Throwable t) {
                    throw new FTPListParseException();
                }
                ret[i].setSize(fileSize);
                if (dayString.length() == 1) {
                    dayString = "0" + dayString;
                }
                StringBuilder mdString = new StringBuilder();
                mdString.append(monthString);
                mdString.append(' ');
                mdString.append(dayString);
                mdString.append(' ');
                boolean checkYear = false;
                if (yearString == null) {
                    mdString.append(currentYear);
                    checkYear = true;
                } else {
                    mdString.append(yearString);
                    checkYear = false;
                }
                mdString.append(' ');
                if (hourString != null && minuteString != null) {
                    if (hourString.length() == 1) {
                        hourString = "0" + hourString;
                    }
                    if (minuteString.length() == 1) {
                        minuteString = "0" + minuteString;
                    }
                    mdString.append(hourString);
                    mdString.append(':');
                    mdString.append(minuteString);
                } else {
                    mdString.append("00:00");
                }
                Date md;
                try {
                    synchronized (DATE_FORMAT) {
                        md = DATE_FORMAT.parse(mdString.toString());
                    }
                } catch (ParseException e) {
                    throw new FTPListParseException();
                }
                if (checkYear) {
                    Calendar mc = Calendar.getInstance();
                    mc.setTime(md);
                    if (mc.after(now) && mc.getTimeInMillis() - now.getTimeInMillis() > 24L * 60L * 60L * 1000L) {
                        mc.set(Calendar.YEAR, currentYear - 1);
                        md = mc.getTime();
                    }
                }
                ret[i].setModifiedDate(md);
                ret[i].setName(nameString);
            } else {
                throw new FTPListParseException();
            }
        }
        return ret;
    }

}
