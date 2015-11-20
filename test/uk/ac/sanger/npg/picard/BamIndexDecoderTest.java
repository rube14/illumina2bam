/*
 * Copyright (C) 2011 GRL
 *
 * This library is free software. You can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package uk.ac.sanger.npg.picard;

import java.io.File;
import java.io.IOException;
import java.util.TimeZone;
import java.util.ArrayList;
import net.sf.samtools.SAMFileReader;
import net.sf.samtools.SAMProgramRecord;
import net.sf.samtools.SAMReadGroupRecord;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import org.junit.Test;
import uk.ac.sanger.npg.bam.util.CheckMd5;
import net.sf.samtools.*;

/**
 *
 * @author gq1@sanger.ac.uk
 */

public class BamIndexDecoderTest {
    
    public BamIndexDecoderTest() {
        TimeZone.setDefault(TimeZone.getTimeZone("GMT"));
        SAMFileReader.setDefaultValidationStringency(SAMFileReader.ValidationStringency.SILENT);
    }

    /**
     * Test of instanceMain method and program record.
     */
    @Test
    public void testCheckBarcodeQualityMethod() throws IOException {
        
        System.out.println("test check barcode quality method");
        
        BamIndexDecoder decoder = new BamIndexDecoder();
        assertEquals("NNGATCTG", decoder.checkBarcodeQuality("CAGATCTG", "%#144=D@"));
    }

    /**
     * Test of instanceMain method and program record.
     */
    @Test
    public void testMainWithOutputBam() throws IOException {
        
        System.out.println("instanceMain - not split output by barcode");
        
        BamIndexDecoder decoder = new BamIndexDecoder();
        
        String outputName = "testdata/6383_8/6383_8";
        File outputDir = new File("testdata/6383_8");
        outputDir.mkdir();
        
        String[] args = {
            "I=testdata/bam/6383_8.sam",
            "O=" + outputName + ".sam" ,
            "BARCODE_FILE=testdata/decode/6383_8.tag",
            "METRICS_FILE=" + outputName + ".metrics",
            "CREATE_MD5_FILE=true",
            "TMP_DIR=testdata/",
            "VALIDATION_STRINGENCY=SILENT",
            "CHANGE_RG_NAME=true",
            "BARCODE_TAG_NAME=RT"
        };

        decoder.instanceMain(args);
        System.out.println(decoder.getCommandLine());
        assertEquals(decoder.getCommandLine(), "uk.ac.sanger.npg.picard.BamIndexDecoder INPUT=testdata/bam/6383_8.sam OUTPUT=testdata/6383_8/6383_8.sam BARCODE_TAG_NAME=RT BARCODE_FILE=testdata/decode/6383_8.tag METRICS_FILE=testdata/6383_8/6383_8.metrics CHANGE_RG_NAME=true TMP_DIR=[testdata] VALIDATION_STRINGENCY=SILENT CREATE_MD5_FILE=true    BARCODE_QUALITY_TAG_NAME=QT MAX_MISMATCHES=1 MIN_MISMATCH_DELTA=1 MAX_NO_CALLS=2 CONVERT_LOW_QUALITY_TO_NO_CALL=false MAX_LOW_QUALITY_TO_CONVERT=15 VERBOSITY=INFO QUIET=false COMPRESSION_LEVEL=5 MAX_RECORDS_IN_RAM=500000 CREATE_INDEX=false");
        File outputFile = new File(outputName + ".sam");
        File outputMetrics = new File(outputName + ".metrics");
        File outputMd5 = new File(outputName + ".sam.md5");

        SAMFileReader samFileReader = new SAMFileReader(outputFile);
        ArrayList<SAMProgramRecord> pgrl = new ArrayList<SAMProgramRecord>();
        for (SAMProgramRecord r: samFileReader.getFileHeader().getProgramRecords()){
          if(r.getProgramName().equals("BamIndexDecoder")) {pgrl.add(r);}
        }
        assertEquals(1, pgrl.size());
        SAMProgramRecord pgr = pgrl.get(0);
        for (SAMReadGroupRecord r: samFileReader.getFileHeader().getReadGroups()){
          assertEquals(pgr.getId(), r.getAttribute("PG"));
        }
        samFileReader.close();

        assertEquals("a71494cca1990ab452ab71d55c54e9d9", CheckMd5.getBamMd5AfterRemovePGVersion(outputFile, "BamIndexDecoder"));
        
        outputFile.delete();
        outputMetrics.delete();
        outputMd5.delete();
        
        outputDir.deleteOnExit();
    }

    /**
     * Test of a barcode containing an N
     */
    @Test
    public void testMainWithBarcodeContainingAnN() throws IOException {
        
        System.out.println("instanceMain - not split output by barcode");
        
        BamIndexDecoder decoder = new BamIndexDecoder();
        
        String outputName = "testdata/6383_8/6383_8";
        File outputDir = new File("testdata/6383_8");
        outputDir.mkdir();
        
        String[] args = {
            "I=testdata/bam/6383_8.sam",
            "O=" + outputName + ".sam" ,
            "BARCODE_FILE=testdata/decode/6383_8_N.tag",
            "METRICS_FILE=" + outputName + ".metrics",
            "CREATE_MD5_FILE=true",
            "TMP_DIR=testdata/",
            "VALIDATION_STRINGENCY=SILENT",
            "CHANGE_RG_NAME=true",
            "BARCODE_TAG_NAME=RT"
        };

        decoder.instanceMain(args);
        System.out.println(decoder.getCommandLine());
        assertEquals(decoder.getCommandLine(), "uk.ac.sanger.npg.picard.BamIndexDecoder INPUT=testdata/bam/6383_8.sam OUTPUT=testdata/6383_8/6383_8.sam BARCODE_TAG_NAME=RT BARCODE_FILE=testdata/decode/6383_8_N.tag METRICS_FILE=testdata/6383_8/6383_8.metrics CHANGE_RG_NAME=true TMP_DIR=[testdata] VALIDATION_STRINGENCY=SILENT CREATE_MD5_FILE=true    BARCODE_QUALITY_TAG_NAME=QT MAX_MISMATCHES=1 MIN_MISMATCH_DELTA=1 MAX_NO_CALLS=2 CONVERT_LOW_QUALITY_TO_NO_CALL=false MAX_LOW_QUALITY_TO_CONVERT=15 VERBOSITY=INFO QUIET=false COMPRESSION_LEVEL=5 MAX_RECORDS_IN_RAM=500000 CREATE_INDEX=false");
        File outputFile = new File(outputName + ".sam");
        File outputMetrics = new File(outputName + ".metrics");
        File outputMd5 = new File(outputName + ".sam.md5");

        SAMFileReader samFileReader = new SAMFileReader(outputFile);
        ArrayList<SAMProgramRecord> pgrl = new ArrayList<SAMProgramRecord>();
        for (SAMProgramRecord r: samFileReader.getFileHeader().getProgramRecords()){
          if(r.getProgramName().equals("BamIndexDecoder")) {pgrl.add(r);}
        }
        assertEquals(1, pgrl.size());
        SAMProgramRecord pgr = pgrl.get(0);
        for (SAMReadGroupRecord r: samFileReader.getFileHeader().getReadGroups()){
          assertEquals(pgr.getId(), r.getAttribute("PG"));
        }
        samFileReader.close();

        assertEquals("cd5a5abd5f2f77977db8d55bef1d8f7b", CheckMd5.getBamMd5AfterRemovePGVersion(outputFile, "BamIndexDecoder"));
        
        outputFile.delete();
        outputMetrics.delete();
        outputMd5.delete();
        
        outputDir.deleteOnExit();
    }

    /**
     * Test of instanceMain method and program record to split output bam file
     */
    @Test
    public void testMainWithOutputDir() throws IOException {
        
        System.out.println("instanceMain - split output by tag");
        
        BamIndexDecoder decoder = new BamIndexDecoder();

        String outputName = "testdata/6383_8_split";
        File outputDir = new File("testdata/6383_8_split");
        outputDir.mkdir();
        
        String[] args = {
            "I=testdata/bam/6383_8.sam",
            "OUTPUT_DIR=" + outputName,
            "OUTPUT_PREFIX=6383_8",
            "OUTPUT_FORMAT=bam",            
            "BARCODE_FILE=testdata/decode/6383_8.tag",
            "METRICS_FILE=" + outputName + "/6383_8.metrics",
            "CREATE_MD5_FILE=true",
            "TMP_DIR=testdata/",
            "VALIDATION_STRINGENCY=SILENT",
            "BARCODE_TAG_NAME=RT",
            "BARCODE_QUALITY_TAG_NAME=QT",
            "CONVERT_LOW_QUALITY_TO_NO_CALL=true"
        };

        decoder.instanceMain(args);
        System.out.println(decoder.getCommandLine());
        assertEquals(decoder.getCommandLine(), "uk.ac.sanger.npg.picard.BamIndexDecoder INPUT=testdata/bam/6383_8.sam OUTPUT_DIR=testdata/6383_8_split OUTPUT_PREFIX=6383_8 OUTPUT_FORMAT=bam BARCODE_TAG_NAME=RT BARCODE_QUALITY_TAG_NAME=QT BARCODE_FILE=testdata/decode/6383_8.tag METRICS_FILE=testdata/6383_8_split/6383_8.metrics CONVERT_LOW_QUALITY_TO_NO_CALL=true TMP_DIR=[testdata] VALIDATION_STRINGENCY=SILENT CREATE_MD5_FILE=true    MAX_MISMATCHES=1 MIN_MISMATCH_DELTA=1 MAX_NO_CALLS=2 CHANGE_RG_NAME=false MAX_LOW_QUALITY_TO_CONVERT=15 VERBOSITY=INFO QUIET=false COMPRESSION_LEVEL=5 MAX_RECORDS_IN_RAM=500000 CREATE_INDEX=false");
         
        File outputMetrics = new File(outputName + "/6383_8.metrics");
        outputMetrics.delete();
        String [] md5s = {"e3e0b1cb89b50e69fb2c54daae226975", "d67e70c8be789e3658d7a60c7b239910", "6970326102dafc28ef8fb2c09844c4de"};
        for (int i=0;i<3;i++){
            File outputFile = new File(outputName + "/6383_8#" + i + ".bam");
            SAMFileReader f  = new SAMFileReader(outputFile);
            SAMRecordIterator inIterator = f.iterator();
            while(inIterator.hasNext()){
                SAMRecord record = inIterator.next();
                String readName = record.getReadName();
                assertFalse("RG has not changed", readName.matches("(.*)#(.*)"));
            }
            f.close();
            outputFile.deleteOnExit();
            File outputMd5 = new File(outputName + "/6383_8#" + i + ".bam.md5");
            outputMd5.deleteOnExit();
            assertEquals(md5s[i], CheckMd5.getBamMd5AfterRemovePGVersion(outputFile, "BamIndexDecoder"));
        }
        
        outputDir.deleteOnExit();
    }

    /**
     * Test of CHANGE_RG_NAME paramater
     */
    @Test
    public void testMainWithChangeRGName() throws IOException {
        
        System.out.println("instanceMain - split output by tag");
        
        BamIndexDecoder decoder = new BamIndexDecoder();

        String outputName = "testdata/6383_8_split";
        File outputDir = new File("testdata/6383_8_split");
        outputDir.mkdir();
        
        String[] args = {
            "I=testdata/bam/6383_8.sam",
            "OUTPUT_DIR=" + outputName,
            "OUTPUT_PREFIX=6383_8",
            "OUTPUT_FORMAT=bam",            
            "BARCODE_FILE=testdata/decode/6383_8.tag",
            "METRICS_FILE=" + outputName + "/6383_8.metrics",
            "CREATE_MD5_FILE=true",
            "TMP_DIR=testdata/",
            "VALIDATION_STRINGENCY=SILENT",
            "BARCODE_TAG_NAME=RT",
            "BARCODE_QUALITY_TAG_NAME=QT",
            "CHANGE_RG_NAME=true",
            "CONVERT_LOW_QUALITY_TO_NO_CALL=true"
        };

        decoder.instanceMain(args);
        System.out.println(decoder.getCommandLine());
        assertEquals(decoder.getCommandLine(), "uk.ac.sanger.npg.picard.BamIndexDecoder INPUT=testdata/bam/6383_8.sam OUTPUT_DIR=testdata/6383_8_split OUTPUT_PREFIX=6383_8 OUTPUT_FORMAT=bam BARCODE_TAG_NAME=RT BARCODE_QUALITY_TAG_NAME=QT BARCODE_FILE=testdata/decode/6383_8.tag METRICS_FILE=testdata/6383_8_split/6383_8.metrics CONVERT_LOW_QUALITY_TO_NO_CALL=true CHANGE_RG_NAME=true TMP_DIR=[testdata] VALIDATION_STRINGENCY=SILENT CREATE_MD5_FILE=true    MAX_MISMATCHES=1 MIN_MISMATCH_DELTA=1 MAX_NO_CALLS=2 MAX_LOW_QUALITY_TO_CONVERT=15 VERBOSITY=INFO QUIET=false COMPRESSION_LEVEL=5 MAX_RECORDS_IN_RAM=500000 CREATE_INDEX=false");
         
        File outputMetrics = new File(outputName + "/6383_8.metrics");
        outputMetrics.delete();
        String [] md5s = {"cc4c5e5c2a1746a804d26d08bc44f512", "02d1cf6c350ff9580f5ee3921d3b1550", "0a7bea441793db682f611b9da0dea90e"};
        for (int i=0;i<3;i++){
            File outputFile = new File(outputName + "/6383_8#" + i + ".bam");
            SAMFileReader f  = new SAMFileReader(outputFile);
            SAMRecordIterator inIterator = f.iterator();
            while(inIterator.hasNext()){
                SAMRecord record = inIterator.next();
                String readName = record.getReadName();
                assertTrue("RG has changed", readName.matches("(.*)#(.*)"));
            }
            f.close();
            outputFile.deleteOnExit();
            File outputMd5 = new File(outputName + "/6383_8#" + i + ".bam.md5");
            outputMd5.deleteOnExit();
            assertEquals(md5s[i], CheckMd5.getBamMd5AfterRemovePGVersion(outputFile, "BamIndexDecoder"));
        }
        
        outputDir.deleteOnExit();
    }}
