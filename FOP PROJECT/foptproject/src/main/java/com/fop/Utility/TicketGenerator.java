package com.fop.Utility;

import com.google.zxing.WriterException;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.color.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.element.Paragraph;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * 
 * @author WeiXin
 */
public class TicketGenerator {
    
    public ByteArrayOutputStream genTicket(ByteArrayOutputStream ops,String refId, String transactionTimeStamp, String movieName, String hall, String date, String time,String type, String seats, String FnB) throws IOException, WriterException{
        String src = "src/main/resources/com/fop/Templates/MovieTicketTemplate.pdf";
        
        // get qr code image data
        ByteArrayOutputStream qr = qrGenerator.genQR(refId, movieName, time, date, hall, seats);
        ImageData qrData = ImageDataFactory.create(qr.toByteArray()); // convert to byte array and fit into ImageData object
 
        // read from existing pdf
        PdfDocument doc = new PdfDocument(new PdfReader(src),new PdfWriter(ops)); // write the pdf into buffer
        
        // custom font and color
        PdfFont montserratFont = PdfFontFactory.createFont("src/main/resources/com/fop/font/Montserrat/Montserrat-Regular.ttf",true);
        Color white = new DeviceRgb(255,255,255);
        
        // add details
        // offset y is from the bottom
        float y = 1000; // adjustment for y offset of all texts
        double y2 = 33; // minor addjustment for y offset of the texts under movie section
        PdfCanvas canvas = new PdfCanvas(doc.getFirstPage()).setColor(white,true);
        
        // drawing
        canvas.beginText().setFontAndSize(montserratFont,20)
                .moveText(745,y-194)
                .showText(refId)
                .endText();
        canvas.beginText().setFontAndSize(montserratFont,20)
                .moveText(745,y-286)
                .showText(transactionTimeStamp)
                .endText();
        canvas.beginText().setFontAndSize(montserratFont,20)
                .moveText(745,y-378)
                .showText(movieName)
                .endText();
        canvas.beginText().setFontAndSize(montserratFont,20)
                .moveText(745,y-378-y2)
                .showText(hall)
                .endText();
        canvas.beginText().setFontAndSize(montserratFont,20)
                .moveText(745,y-378-2*y2)
                .showText(String.format("%s %s",date,time))
                .endText();
        canvas.beginText().setFontAndSize(montserratFont,20)
                .moveText(745,y-378-3*y2)
                .showText("Kuala Lumpur - MidValley")
                .endText();
        canvas.beginText().setFontAndSize(montserratFont,20)
                .moveText(745,y-568)
                .showText(type)
                .endText();
        canvas.beginText().setFontAndSize(montserratFont,20)
                .moveText(745,y-663)
                .showText(seats)
                .endText();
        
        if(FnB != null){
            for(int i = 0 ; i < FnB.split("\n").length ; i++){
                canvas.beginText().setFontAndSize(montserratFont,20)
                    .moveText(745,y-758-i*24)
                    .showText(FnB.split("\n")[i])
                    .endText();
            }
        }
        
        canvas.addImage(qrData,1230,100,false); // false for inline
        
        
        // close the pdf document
        doc.close();
        
        return ops;
    }
}
