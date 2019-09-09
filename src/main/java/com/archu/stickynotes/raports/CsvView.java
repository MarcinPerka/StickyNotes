package com.archu.stickynotes.raports;

import com.archu.stickynotes.user.User;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class CsvView extends AbstractCsvView {


    @Override
    protected void buildCsvDocument(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setHeader("Content-Disposition", "attachment; filename=\"usersCsv.csv\"");

        @SuppressWarnings("unchecked")
        List<User> users = (List<User>) model.get("users");

        String[] header = {"Username", "Email", "Enabled", "Roles"};
        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(),
                CsvPreference.STANDARD_PREFERENCE);

        csvWriter.writeHeader(header);

        users.forEach(user -> {
            try {
                csvWriter.write(user, header);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        csvWriter.close();
    }
}
