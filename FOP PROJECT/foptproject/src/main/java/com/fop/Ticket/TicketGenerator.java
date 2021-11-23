/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.fop.Ticket;

import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.color.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import java.io.ByteArrayOutputStream;
import java.io.IOException;


public class TicketGenerator {
    
    public ByteArrayOutputStream genTicket(ByteArrayOutputStream ops,String refId, String transactionDate, String transactionTime, String movieName, String hall, String date, String time,String type, String seats, String FnB) throws IOException{
        String src = "src/main/resources/com/fop/Templates/MovieTicketTemplate.pdf";
        
        // read from existing pdf
        PdfDocument doc = new PdfDocument(new PdfReader(src),new PdfWriter(ops)); // write the pdf into buffer
        
        // custom font and color
        PdfFont montserratFont = PdfFontFactory.createFont("src/main/resources/com/fop/font/Montserrat Family/Montserrat-Regular.ttf",true);
        Color white = new DeviceRgb(255,255,255);
        
        // add details
        // offset y is from the bottom
        float y = 1000; // adjustment for y offset of all texts
        double y2 = 33; // minor addjustment for y offset of the texts under movie section
        PdfCanvas canvas = new PdfCanvas(doc.getFirstPage()).setColor(white,true);
    
        canvas.beginText().setFontAndSize(montserratFont,20)
                .moveText(745,y-194)
                .showText(refId)
                .endText();
        canvas.beginText().setFontAndSize(montserratFont,20)
                .moveText(745,y-286)
                .showText(String.format("%s %s",transactionDate,transactionTime))
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
        canvas.beginText().setFontAndSize(montserratFont,20)
                .moveText(745,y-758)
                .showText(FnB)
                .endText();
     
        // close the pdf document
        doc.close();
        
        return ops;
    }
}