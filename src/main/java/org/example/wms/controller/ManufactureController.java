package org.example.wms.controller;

import lombok.RequiredArgsConstructor;
import org.example.wms.service.ManufacturerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/manufactures")
public class ManufactureController {
    private final ManufacturerService manufacturerService;


}
