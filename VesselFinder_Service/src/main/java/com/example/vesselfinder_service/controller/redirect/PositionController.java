package com.example.vesselfinder_service.controller.redirect;

import com.example.vesselfinder_service.service.PositionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.List;

@Controller
@Tag(name = "Position controller.",
        description = "Here you can get real time ship's positions on a Google Map.")
@RequestMapping("/finder")
public class PositionController {
    private final PositionService service;

    @Autowired
    public PositionController(PositionService service) {
        this.service = service;
    }

    @GetMapping("/get_positions/")
    @Operation(summary = "Get positions on a map",
            description = "Provide IMO numbers separated by coma.")
    public void getPositions(HttpServletResponse response, @RequestParam("imoNumbers") List<Long> imoNumbers) throws IOException {
        response.sendRedirect(service.generateStaticGoogleMapUrl(imoNumbers));
    }
}