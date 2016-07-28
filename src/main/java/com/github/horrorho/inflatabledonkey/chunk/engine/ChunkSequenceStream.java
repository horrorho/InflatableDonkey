///*
// * The MIT License
// *
// * Copyright 2016 Ahseya.
// *
// * Permission is hereby granted, free of charge, to any person obtaining a copy
// * of this software and associated documentation files (the "Software"), to deal
// * in the Software without restriction, including without limitation the rights
// * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// * copies of the Software, and to permit persons to whom the Software is
// * furnished to do so, subject to the following conditions:
// *
// * The above copyright notice and this permission notice shall be included in
// * all copies or substantial portions of the Software.
// *
// * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
// * THE SOFTWARE.
// */
//package com.github.horrorho.inflatabledonkey.chunk.engine;
//
//import com.github.horrorho.inflatabledonkey.chunk.Chunk;
//import java.io.IOException;
//import java.io.InputStream;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// *
// * @author Ahseya
// */
//public class ChunkSequenceStream extends InputStream {
//
//    private final List<Chunk> chunks;
//
//    public ChunkSequenceStream(List<Chunk> chunks) {
//        this.chunks = new ArrayList<>(chunks);
//    }
//
//    @Override
//    public boolean markSupported() {
//        return super.markSupported(); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    @Override
//    public synchronized void reset() throws IOException {
//        super.reset(); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    @Override
//    public synchronized void mark(int readlimit) {
//        super.mark(readlimit); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    @Override
//    public void close() throws IOException {
//        super.close(); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    @Override
//    public int available() throws IOException {
//        return super.available(); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    @Override
//    public long skip(long n) throws IOException {
//        return super.skip(n); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    @Override
//    public int read(byte[] b, int off, int len) throws IOException {
//        return super.read(b, off, len); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    @Override
//    public int read(byte[] b) throws IOException {
//        return super.read(b); //To change body of generated methods, choose Tools | Templates.
//    }
//
//}
