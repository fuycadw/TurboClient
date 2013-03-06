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
 * This interface describes how to implement a textual extension recognizer,
 * which can be plugged into a FTPClient object calling its
 * setTextualExtensionsRecognizer() method.
 *
 * @author Carlo Pelliccia
 * @see it.sauronsoftware.ftp4j.FTPClient#setTextualExtensionRecognizer(it.sauronsoftware.ftp4j.FTPTextualExtensionRecognizer)
 */
public interface FTPTextualExtensionRecognizer {

    /**
     * This method returns true if the given file extension is recognized to be
     * a textual one.
     *
     * @param ext The file extension, always in lower-case.
     * @return true if the given file extension is recognized to be a textual
     *         one.
     */
    public boolean isTextualExt(String ext);

}
