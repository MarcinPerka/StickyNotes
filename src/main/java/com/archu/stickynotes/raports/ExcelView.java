package com.archu.stickynotes.raports;

import com.archu.stickynotes.role.Role;
import com.archu.stickynotes.user.User;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.springframework.web.servlet.view.document.AbstractXlsView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;


public class ExcelView extends AbstractXlsView {

    @Override
    protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setHeader("Content-Disposition", "attachment; filename=\"usersRaport.xls\"");

        @SuppressWarnings("unchecked")
        List<User> users = (List<User>) model.get("users");

        Sheet sheet = workbook.createSheet("Users details");
        sheet.setDefaultColumnWidth(30);

        // create style for header cells
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setFontName("Arial");
        font.setBold(true);
        style.setFont(font);

        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("Id");
        header.getCell(0).setCellStyle(style);
        header.createCell(1).setCellValue("Username");
        header.getCell(1).setCellStyle(style);
        header.createCell(2).setCellValue("Email");
        header.getCell(2).setCellStyle(style);
        header.createCell(3).setCellValue("Enabled");
        header.getCell(3).setCellStyle(style);
        header.createCell(4).setCellValue("Roles");
        header.getCell(4).setCellStyle(style);

        int rowCount = 1;

        for(User user: users){
            Row userRow =  sheet.createRow(rowCount++);
            userRow.createCell(0).setCellValue(user.getId());
            userRow.createCell(1).setCellValue(user.getUsername());
            userRow.createCell(2).setCellValue(user.getEmail());
            userRow.createCell(3).setCellValue(user.getEnabled());
            StringBuilder roles= new StringBuilder();
            for(Role role: user.getRoles()){
                roles.append(role.getLabel()).append(" ");
            }
            userRow.createCell(4).setCellValue(roles.toString());
        }
         }
}
