/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */


package org.apache.isis.runtimes.dflt.objectstores.nosql.file.server;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;


public class DataWriter {

    //   private static final Logger LOG = Logger.getLogger(DataWriter.class);
    
    private static final String ENCODING = "utf-8";
 
    private final List<FileContent> files;

    public DataWriter(List<FileContent> files) {
        this.files = files;
    }

    public void writeData() throws IOException {
        for (FileContent content : files) {
            if (Util.isDelete(content.command)) {
                File f = Util.dataFile(content.type, content.id);
                f.delete();
            } else {
                writeFile(content);
            }
        }
    }

    // TODO to be consistent use PrintWriter
    private void writeFile(FileContent content) throws IOException {
        FileOutputStream output = null;
        File file = Util.dataFile(content.type, content.id);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdir();
        }
        try {
            output = new FileOutputStream(file);
            output.write(content.type.getBytes(ENCODING));
            output.write(' ');
            output.write(content.id.getBytes(ENCODING));
            output.write(' ');
            output.write(content.newVersion.getBytes(ENCODING));
            output.write('\n');
            output.write(content.data.getBytes(ENCODING));
        } finally {
            closeSafely(output);
        }
    }

    private static void closeSafely(FileOutputStream output) {
        if (output != null) {
            try {
                output.flush();
                output.close();
            } catch (IOException e) {
                // throw new ObjectAdapterRuntimeException(e);
            }
        }
    }

    public void close() {
        // TODO Auto-generated method stub
        
    }

}

