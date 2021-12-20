/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fop.Utility;

import java.io.IOException;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;

/**
 * 
 * @author WeiXin
 */
public class qrGenerator {

    public static ByteArrayOutputStream genQR(String refID, String movieID, String time, String date, String hall, String seat) throws WriterException, UnsupportedEncodingException, IOException {
        ByteArrayOutputStream ops = new ByteArrayOutputStream();
        String timestamp = time + " " + date;
        String compiledData = String.format("%s#%s#%s#%s#%s#", refID, timestamp, movieID, hall, seat);

        BitMatrix canvas = new MultiFormatWriter().encode(
                new String(compiledData.getBytes("utf-8"), "utf-8"),
                BarcodeFormat.QR_CODE, 130, 130
        );

        MatrixToImageWriter.writeToStream(
                canvas,
                "png",
                ops
        );

        return ops;
    }
}
