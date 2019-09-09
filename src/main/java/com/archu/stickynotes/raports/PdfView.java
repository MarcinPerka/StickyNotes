package com.archu.stickynotes.raports;

import com.archu.stickynotes.role.Role;
import com.archu.stickynotes.user.User;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public class PdfView extends AbstractPdfView {

    @Override
    protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer, HttpServletRequest request, HttpServletResponse response) throws DocumentException {
        response.setHeader("Content-Disposition", "attachment; filename=\"usersRaport.pdf\"");

        @SuppressWarnings("unchecked")
        List<User> users = (List<User>) model.get("users");

        document.add(new Paragraph("User details"));
        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100.0f);
        table.setWidths(new float[]{3.0f, 2.0f, 2.0f, 2.0f, 1.0f});
        table.setSpacingBefore(10);

        Font font = FontFactory.getFont(FontFactory.HELVETICA);
        font.setColor(BaseColor.WHITE);

        // define table header cell
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(BaseColor.GRAY);
        cell.setPadding(5);

        // write table header
        cell.setPhrase(new Phrase("Id", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Username", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Email", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Enabled", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Roles", font));
        table.addCell(cell);


        int rowCount = 1;

        for (User user : users) {
            table.addCell(user.getId());
            table.addCell(user.getUsername());
            table.addCell(user.getEmail());
            table.addCell(String.valueOf(user.getEnabled()));
            StringBuilder roles = new StringBuilder();
            for (Role role : user.getRoles()) {
                roles.append(role.getLabel()).append(" ");
            }
            table.addCell(roles.toString());
        }

        document.add(table);
    }

}
