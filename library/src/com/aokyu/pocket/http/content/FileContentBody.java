/*
 * Copyright (c) 2015 Yu AOKI
 *
 * This software may be modified and distributed under the terms
 * of the MIT license. See the LICENSE file for details.
 */

package com.aokyu.pocket.http.content;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * This class holds a file path and converts a file content to the specified format if needed.
 */
public class FileContentBody extends AbstractMessageBody {

    private final String mFilePath;

    /**
     * Creates the message body for the specified file path.
     *
     * @param filePath The file path.
     */
    public FileContentBody(String filePath) {
        mFilePath = filePath;
    }

    @Override
    public MessageStream toMessageStream(OutputStream output) {
        // Output the file content as a binary data.
        return new BinaryDataStream(output, mFilePath);
    }

    /**
     * This class is used to write the file content into the output stream.
     */
    private static final class BinaryDataStream extends MessageStream {

        private static final int EOF = -1;

        private final String mFilePath;

        public BinaryDataStream(OutputStream output, String filePath) {
            super(output);
            mFilePath = filePath;
        }

        @Override
        public int write() throws IOException {
            int numberOfBytes = 0;
            numberOfBytes += writeData(mStream, mFilePath);
            return numberOfBytes;
        }

        private int writeData(DataOutputStream stream, String filePath) throws IOException {
            int numberOfBytes = 0;
            FileInputStream input = null;
            BufferedInputStream reader = null;
            try {
                input = new FileInputStream(filePath);
                reader = new BufferedInputStream(input);
                int available = 0;
                while ((available = reader.available()) > 0) {
                    byte[] bytes = new byte[available];
                    if (reader.read(bytes) == EOF) {
                        numberOfBytes += writeBytes(stream, bytes);
                        break;
                    } else {
                        numberOfBytes += writeBytes(stream, bytes);
                    }
                }
            } finally {
                try {
                    if (reader != null) {
                        reader.close();
                    }

                    if (input != null) {
                        input.close();
                    }
                } catch (IOException e) {
                }
            }
            return numberOfBytes;
        }

        private int writeBytes(DataOutputStream stream, byte[] bytes) throws IOException {
            int numberOfBytes = bytes.length;
            stream.write(bytes);
            return numberOfBytes;
        }

    }
}
