package co.edu.uniquindio.theknowledgebay.api.controller;

import co.edu.uniquindio.theknowledgebay.core.service.TestDataLoaderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test-data")
public class TestDataController {

    private final TestDataLoaderService testDataLoaderService;

    public TestDataController(TestDataLoaderService testDataLoaderService) {
        this.testDataLoaderService = testDataLoaderService;
    }

    @PostMapping("/load")
    public ResponseEntity<String> loadTestData() {
        try {
            testDataLoaderService.loadComprehensiveTestData();
            return ResponseEntity.ok("Test data loaded successfully.");
        } catch (Exception e) {
            e.printStackTrace(); // Log the exception
            return ResponseEntity.internalServerError().body("Failed to load test data: " + e.getMessage());
        }
    }
} 