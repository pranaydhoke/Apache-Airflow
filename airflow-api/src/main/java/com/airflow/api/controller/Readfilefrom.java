/**
 * 
 */
package com.airflow.api.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author pdhoke
 *
 */
public class Readfilefrom {

	/**
	 * @param args
	 * 
	 * 
	 */
	private static final String UPLOAD_FOLDER = "D:\\temp\\";
	public static void main(String[] args) {
		FileInputStream fileInputStream = null;

        try {
			/*
			 * //C:\\temp\\1.txt ‪//C:\temp\1.log
			 */        	//C:\\Users\\pdhoke\\Desktop\\airflow\\logs\\THIRD_PARTY_PDL\\say_hello\\2020-06-08T115325.946717+0000\\1.txt
            File file = new File("C:\\temp\\1.log");
            byte[] bFile = new byte[(int) file.length()];

            //read file into bytes[]
            fileInputStream = new FileInputStream(file);
            fileInputStream.read(bFile);

            //save bytes[] into a file
            writeBytesToFile(bFile, UPLOAD_FOLDER + "test2.txt");
           

            System.out.println("Done");

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

	}
	private static void writeBytesToFile(byte[] bFile, String fileDest) {

        try (FileOutputStream fileOuputStream = new FileOutputStream(fileDest)) {
            fileOuputStream.write(bFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
