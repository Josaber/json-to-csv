/*
 * Copyright 2012-2014 Dristhi software
 * Copyright 2015 Arkni Brahim
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may obtain
 * a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package org.jsontocsv;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.jsontocsv.parser.JSONFlattener;
import org.jsontocsv.writer.CSVWriter;

import static org.jsontocsv.writer.CSVWriter.collectHeaders;
import static org.jsontocsv.writer.CSVWriter.getHeaderString;
import static org.jsontocsv.writer.CSVWriter.getSeperatedColumns;

public class Main {

    public static void main(String[] args) throws Exception {

        traverseFolder("files/");
    }

    static boolean header = true;

    public static void traverseFolder(String path) {
        File file = new File(path);
        List<Map<String, String>> flatJson;
        if (file.exists()) {
            File[] files = file.listFiles();
            if (files.length != 0) {
                for (File fileSub : files) {
                    if (fileSub.isDirectory()) {
                        traverseFolder(fileSub.getAbsolutePath());
                    } else {
                        if (fileSub.getAbsolutePath().toLowerCase().endsWith(".json")) {
                            flatJson = JSONFlattener.parseJson(fileSub, "UTF-8");
                            String csvString = "";
                            if (header) {
                                csvString = getHeaderString(flatJson);
                                header = false;
                            }
                            for (Map<String, String> map : flatJson) {
                                csvString = csvString + getSeperatedColumns(collectHeaders(flatJson), map, ",") + "\n";
                            }

                            CSVWriter.writeToFile(csvString, "files/result.csv");
                        }
                    }
                }
            }
        }
    }
}
