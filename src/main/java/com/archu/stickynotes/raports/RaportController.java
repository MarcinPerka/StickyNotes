package com.archu.stickynotes.raports;

import com.archu.stickynotes.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

@Controller
public class RaportController {

    private UserService userService;

    @Autowired
    public RaportController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users/downloadExcel")
    public ModelAndView getUserExcelRaport() {
        Map<String, Object> model = new HashMap<>();
        model.put("users", userService.getAllUsers());
        return new ModelAndView(new ExcelView(), model);
    }

    @GetMapping("/users/downloadPdf")
    public ModelAndView getUserPdfRaport() {
        Map<String, Object> model = new HashMap<>();
        model.put("users", userService.getAllUsers());
        return new ModelAndView(new PdfView(), model);
    }

    @GetMapping("/users/downloadCsv")
    public ModelAndView getUserCsvRaport() {
        Map<String, Object> model = new HashMap<>();
        model.put("users", userService.getAllUsers());
        return new ModelAndView(new CsvView(), model);
    }
}
