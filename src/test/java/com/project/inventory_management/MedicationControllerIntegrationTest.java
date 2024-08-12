package com.project.inventory_management;

import com.project.inventory_management.config.JwtService;
import com.project.inventory_management.entity.Medication;
import com.project.inventory_management.entity.User;
import com.project.inventory_management.repository.MedicationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
public class MedicationControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtService jwtService; // Use real JwtService

    @Autowired
    private UserDetailsService userDetailsService; // UserDetailsService to load user details

    private String token;

    @MockBean
    private MedicationRepository medicationRepository;

    @BeforeEach
    public void setUp() {
        // Create a user for testing purposes
        User user = new User();
        user.setEmail("admin@gmail.com");
        user.setPassword("Admin123123@");

        // Load UserDetails for generating the token
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());

        // Generate a valid token for the user using JwtService
        token = jwtService.generateToken(userDetails);
    }

    @Test
    public void testGetAllMedications() throws Exception {
        Medication medication1 = new Medication(1L, "medication1", "description1", 200, Medication.Types.OTC);
        Medication medication2 = new Medication(2L, "medication2", "description2", 200, Medication.Types.PRES);

        List<Medication> medicationList = new ArrayList<>(Arrays.asList(medication1, medication2));
        // Create a Page object with the mock data
        Page<Medication> medicationsPage = new PageImpl<>(medicationList);

        // Define the pageable parameters
        int page = 0;
        int size = 5;
        Pageable pageable = PageRequest.of(page, size, Sort.by("name"));

        // Mock the repository call
        when(medicationRepository.findAll(pageable)).thenReturn(medicationsPage);

        mockMvc.perform(get("/medications?page=0&size=5")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("medication1"))
                .andExpect(jsonPath("$[0].description").value("description1"))
                .andExpect(jsonPath("$[0].quantity").value(200))
                .andExpect(jsonPath("$[0].type").value(Medication.Types.OTC.toString()))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("medication2"))
                .andExpect(jsonPath("$[1].description").value("description2"))
                .andExpect(jsonPath("$[1].quantity").value(200))
                .andExpect(jsonPath("$[1].type").value(Medication.Types.PRES.toString()));
    }

    @Test
    public void testGetMedicationById() throws Exception {
        Medication medication = new Medication(1L, "medication1", "description1", 200, Medication.Types.PRES);

        when(medicationRepository.findById(1L)).thenReturn(Optional.of(medication));

        mockMvc.perform(get("/medications/{id}", 1)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("medication1"))
                .andExpect(jsonPath("$.description").value("description1"))
                .andExpect(jsonPath("$.quantity").value(200))
                .andExpect(jsonPath("$.type").value(Medication.Types.PRES.toString()));
    }

    @Test
    public void testAddMedication() throws Exception {
        // Implement your test here
    }

    @Test
    public void testUpdateMedication() throws Exception {
        // Implement your test here
    }

    @Test
    public void testDeleteMedication() throws Exception {
        // Implement your test here
    }

    @Test
    public void testFetchMedicationInfo() throws Exception {
        // Implement your test here
    }
}
