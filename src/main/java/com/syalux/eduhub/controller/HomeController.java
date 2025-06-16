package com.syalux.eduhub.controller;

import com.syalux.eduhub.dto.UniversityDTO;
import com.syalux.eduhub.service.UniversityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class HomeController {
    @Autowired
    private UniversityService universityService;

    @GetMapping({"/", "/home"})
    public String home(@RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "10") int size,
                       @RequestParam(required = false) String name,
                       Model model) {
        List<UniversityDTO> universities = universityService.getUniversitiesPage(page, size, name);
        model.addAttribute("universities", universities);
        model.addAttribute("hasMore", universities.size() == size);
        model.addAttribute("nextPage", page + 1);
        return "home";
    }
}
