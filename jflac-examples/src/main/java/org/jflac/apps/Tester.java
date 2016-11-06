package org.jflac.apps;

/* libFLAC - Free Lossless Audio Codec library
 * Copyright (C) 2000,2001,2002,2003  Josh Coalson
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Library General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Library General Public License for more details.
 *
 * You should have received a copy of the GNU Library General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA  02111-1307, USA.
 */

import java.io.FileInputStream;
import java.io.IOException;

import org.jflac.FLACDecoder;
import org.jflac.FrameListener;
import org.jflac.frame.Frame;
import org.jflac.io.RandomFileInputStream;
import org.jflac.metadata.Metadata;
import org.jflac.metadata.SeekPoint;
import org.jflac.metadata.StreamInfo;

/**
 * Test FLAC file application.
 * @author kc7bfi
 */
public class Tester implements FrameListener {
    private int errors = 0;
    
    /**
     * Analyze an input FLAC file.
     * @param inFileName The input file name
     * @throws IOException thrown if error reading file
     */
    public void test(String inFileName) throws IOException {
        FileInputStream is = new FileInputStream(inFileName);
        FLACDecoder decoder = new FLACDecoder(is);
        decoder.addFrameListener(this);
        decoder.decode();        
        System.out.println(errors + " errors found!");
        is.close();
    }
    /**
     * Seek testing 
     * @param inFileName
     * @param time
     * @throws IOException
     */
    public void test2(String inFileName, long time) throws IOException {
    	RandomFileInputStream is = new RandomFileInputStream(inFileName);
        FLACDecoder decoder = new FLACDecoder(is);
        decoder.readMetadata();;
        StreamInfo si = decoder.getStreamInfo();
        long pos = time*si.getSampleRate();
        System.out.println("Seeking to "+pos+" sample");
        SeekPoint sp = decoder.seek(pos);
        System.out.println("Found point "+sp);
        if (sp != null)
        	decoder.decodeFrames();
        System.out.println(errors + " errors found!");
        is.close();
    }
    
    /**
     * Process metadata records.
     * @param metadata the metadata block
     * @see org.jflac.FrameListener#processMetadata(org.jflac.metadata.MetadataBase)
     */
    public void processMetadata(Metadata metadata) {
    }
    
    /**
     * Process data frames.
     * @param frame the data frame
     * @see org.jflac.FrameListener#processFrame(org.jflac.frame.Frame)
     */
    public void processFrame(Frame frame) {
    }
   
    /**
     * Called for each frame error detected.
     * @param msg   The error message
     * @see org.jflac.FrameListener#processError(java.lang.String)
     */
    public void processError(String msg) {
        errors++;
        System.out.println(msg);
    }
    
    /**
     * Main routine.
     * <p>args[0] is the FLAC file name to analyse
     * @param args  Command arguments
     */
    public static void main(String[] args) {
        try {
            Tester tester = new Tester();
            System.out.println("FLAX Tester for " + args[0]);
            if (args.length > 1) {
            	if ("2".equals(args[1]) && args.length > 2)
            		tester.test2(args[0], Long.parseLong(args[2]));
            } else
            	tester.test(args[0]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
