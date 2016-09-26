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
import java.io.UnsupportedEncodingException;

/**
 * This class holds multipart data and converts data to the specified format if needed.
 */
public class MultipartContentBody extends AbstractMessageBody {

    private final String mBoundary;

    private MultipartElements mElements = new MultipartElements();

    public MultipartContentBody(String boundary) {
        mBoundary = boundary;
    }

    public void addAll(MultipartElements elements) {
        mElements.addAll(elements);
    }

    public void add(MultipartElement element) {
        mElements.add(element);
    }

    public void add(String filePath, String contentType) {
        add(new MultipartElement(filePath, contentType));
    }

    @Override
    public MessageStream toMessageStream(OutputStream output) {
        // Output the data as a multipart data with the boundary.
        return new MultipartDataStream(output, mBoundary, mElements);
    }

    /**
     * This class is used to write the multipart data into the output stream.
     */
    private static final class MultipartDataStream extends MessageStream {

        private static final String DEFAULT_ENCODING = "UTF-8";

        private static final String CRLF = "\r\n";

        private static final int EOF = -1;

        private final String mBoundary;
        private final MultipartElements mElements;

        public MultipartDataStream(OutputStream output,
                String boundary, MultipartElements elements) {
            super(output);
            mBoundary = boundary;
            mElements = elements;
        }

        @Override
        public int write() throws IOException {
            int numberOfBytes = 0;
            for (MultipartElement element : mElements) {
                numberOfBytes += writeData(mStream, element);
            }

            byte[] tailBoundary = getTailBoundary(DEFAULT_ENCODING);
            numberOfBytes += writeBytes(mStream, tailBoundary);
            return numberOfBytes;
        }

        private int writeData(DataOutputStream stream, MultipartElement element)
                throws IOException {
            int numberOfBytes = 0;
            FileInputStream input = null;
            BufferedInputStream reader = null;
            try {
                byte[] headBoundary = getHeadBoundary(DEFAULT_ENCODING);
                numberOfBytes += writeBytes(stream, headBoundary);
                String header = element.getHeader();
                byte[] headerBytes = header.getBytes(DEFAULT_ENCODING);
                numberOfBytes += writeBytes(stream, headerBytes);
                String filePath = element.getFilePath();
                input = new FileInputStream(filePath);
                reader = new BufferedInputStream(input);
                int available;
                while ((available = reader.available()) > 0) {
                    byte[] bytes = new byte[available];
                    if (reader.read(bytes) == EOF) {
                        numberOfBytes += writeBytes(stream, bytes);
                        break;
                    } else {
                        numberOfBytes += writeBytes(stream, bytes);
                    }
                }
                byte[] lineFeed = CRLF.getBytes(DEFAULT_ENCODING);
                numberOfBytes += writeBytes(stream, lineFeed);
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

        private byte[] getHeadBoundary(String encoding) throws UnsupportedEncodingException {
            String boundary = new StringBuilder()
                .append("--")
                .append(mBoundary)
                .append(CRLF)
                .toString();
            return boundary.getBytes(encoding);
        }

        private byte[] getTailBoundary(String encoding) throws UnsupportedEncodingException {
            String boundary = new StringBuilder()
                .append("--")
                .append(mBoundary)
                .append("--")
                .append(CRLF)
                .toString();
            return boundary.getBytes(encoding);
        }

    }
}
