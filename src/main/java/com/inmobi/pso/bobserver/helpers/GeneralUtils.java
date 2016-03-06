package com.inmobi.pso.bobserver.helpers;

import com.sun.jersey.core.header.FormDataContentDisposition;

import net.glxn.qrgen.QRCode;
import net.glxn.qrgen.image.ImageType;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;

/*
 * Created by mukthar.ahmed on 12/1/15.
 */
public class GeneralUtils {
    private static Logger LOG = LoggerFactory.getLogger(GeneralUtils.class.getName());

    public GeneralUtils(Logger log) { LOG = log; }


    // ##########################################################################################
    public static void QRCodeGenerator(String qrCodeString, String qrCodeImageFilePath) {
        LOG.debug("+ Generating QR Code @ " + qrCodeImageFilePath);

        File qrCodeImgFile = new File(qrCodeImageFilePath);
        qrCodeImgFile.deleteOnExit();
        try {
            qrCodeImgFile.createNewFile();
        } catch (IOException e) {
            StringWriter stack = new StringWriter();
            e.printStackTrace(new PrintWriter(stack));
            LOG.debug(stack.toString());
        }

        ByteArrayOutputStream out = QRCode.from(qrCodeString).to(ImageType.PNG).stream();

        try {
            FileOutputStream fout = new FileOutputStream(qrCodeImgFile);
            fout.write(out.toByteArray());
            fout.flush();
            fout.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        LOG.debug("\n\nSuccessfully created QR Code.");

    }

    // ##########################################################################################
    // overload QRCodeGenerator with json object input type
    public static void QRCodeGenerator(JSONObject qrCodeString, String qrCodeImageFilePath) {
        LOG.debug("+ Generating QR Code @ " + qrCodeImageFilePath);

        File qrCodeImgFile = new File(qrCodeImageFilePath);
        qrCodeImgFile.deleteOnExit();
        try {
            qrCodeImgFile.createNewFile();
        } catch (IOException e) {
            StringWriter stack = new StringWriter();
            e.printStackTrace(new PrintWriter(stack));
            LOG.debug(stack.toString());
        }

        ByteArrayOutputStream out = QRCode.from(qrCodeString.toString()).to(ImageType.PNG).stream();

        try {
            FileOutputStream fout = new FileOutputStream(qrCodeImgFile);
            fout.write(out.toByteArray());
            fout.flush();
            fout.close();

        } catch (Exception e) {
            StringWriter stack = new StringWriter();
            e.printStackTrace(new PrintWriter(stack));
            LOG.debug(stack.toString());
        }
        LOG.debug("\n\nSuccessfully created QR Code.");
    }

    // ###########################################################################################
    public static void writeToFile(String fileContent, String hostedAdFile) {
        LOG.debug("Writing to file: " + hostedAdFile);

        try {
            File file = new File(hostedAdFile);

            file.createNewFile();
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);

            bw.write( fileContent );

            bw.close();

            LOG.debug("Written to file.");

        } catch (Exception e) {
            StringWriter stack = new StringWriter();
            e.printStackTrace(new PrintWriter(stack));
            LOG.debug(stack.toString());
        }
    }

    // ##########################################################################################
    public static void initBobstaticApp(String bobHomeLocal, String adStaticWebappPath) {
        LOG.debug("Init bob-static ads environment with" +
                ", Bob Home: " + bobHomeLocal + ", Bob static app path: " + adStaticWebappPath);

        File bobStaticAdsPath = new File(bobHomeLocal + "/../" + adStaticWebappPath);
        if (!bobStaticAdsPath.exists()) {
            bobStaticAdsPath.mkdirs();

            try {
                FileUtils.copyDirectory(
                        new File(bobHomeLocal + "/lib"),
                        new File(bobStaticAdsPath + "/lib")
                );

                FileUtils.copyDirectory(
                        new File(bobHomeLocal + "/images"),
                        new File(bobStaticAdsPath + "/images")
                );

            } catch (IOException e) {
                StringWriter stack = new StringWriter();
                e.printStackTrace(new PrintWriter(stack));
                LOG.debug(stack.toString());
            }
        }

    }   // end method()


    public static boolean fileUploadUtils(String folderPath, InputStream fis,
                                       FormDataContentDisposition fdcd) {

        LOG.debug("Uploading file = " + fdcd.getFileName() + "\" to the dir = " + folderPath    );
        OutputStream outpuStream = null;
        String fileName = fdcd.getFileName();
        String filePath = folderPath + fileName;

        if (new File(filePath).exists()) {
            return false;
        }

        try {
            int read = 0;
            byte[] bytes = new byte[1024];
            outpuStream = new FileOutputStream(new File(filePath));
            while ((read = fis.read(bytes)) != -1) {
                outpuStream.write(bytes, 0, read);
            }
            outpuStream.flush();
            outpuStream.close();

        } catch(IOException iox){
            StringWriter stack = new StringWriter();
            iox.printStackTrace(new PrintWriter(stack));
            LOG.debug(stack.toString());

        } finally {
            if(outpuStream != null){
                try{
                    outpuStream.close();
                } catch(Exception ex){
                    StringWriter stack = new StringWriter();
                    ex.printStackTrace(new PrintWriter(stack));
                    LOG.debug(stack.toString());
                }
            }
        }

        return true;

    }   // end upload


    // ##########################################################################################
    public static void main(String[] args) {
        LOG.debug(String.valueOf(System.currentTimeMillis()));
        String name = String.format("%s.%s", RandomStringUtils.randomAlphanumeric(8), "file");
        LOG.debug(name);
    }


}
