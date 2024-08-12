package com.project.inventory_management;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.inventory_management.config.JwtService;
import com.project.inventory_management.dto.InboundTransactionDTO;
import com.project.inventory_management.entity.InboundTransaction;
import com.project.inventory_management.entity.Medication;
import com.project.inventory_management.entity.User;
import com.project.inventory_management.repository.InboundTransactionRepository;
import com.project.inventory_management.repository.MedicationRepository;
import com.project.inventory_management.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
public class InboundTransactionControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtService jwtService;

    private String token;

    @MockBean
    private InboundTransactionRepository inboundTransactionRepository;
    @MockBean
    private MedicationRepository medicationRepository;
    @MockBean
    private UserRepository userRepository;



    @BeforeEach
    void setUp() {
        User user = new User();
        user.setEmail("admin@gmail.com");
        user.setPassword("Admin123123@");
        user.setRole(User.Roles.ADMIN);
        when(userRepository.findByEmail("admin@gmail.com")).thenReturn(Optional.of(user));

        token = jwtService.generateToken(user);
        System.out.println("Generated Token: " + token); // Check if the token is generated correctly

        // Ensure the token is not null
        assertNotNull(token, "The generated token should not be null");
    }

    @Test
    public void testGetAllInboundTransactions() throws Exception {
        InboundTransaction transaction1 = new InboundTransaction();
        InboundTransaction transaction2 = new InboundTransaction();
        Medication medication1 = new Medication(1L, "medication1", "description1", 200, Medication.Types.OTC);
        Medication medication2 = new Medication(2L, "medication2", "description2", 200, Medication.Types.PRES);

        // Use OffsetDateTime or ZonedDateTime for precise control over time zones.
        OffsetDateTime timestamp1 = OffsetDateTime.of(2024, 7, 27, 12, 34, 56, 0, ZoneOffset.UTC);
        OffsetDateTime timestamp2 = OffsetDateTime.of(2000, 7, 27, 12, 34, 56, 0, ZoneOffset.UTC);

        transaction1.setId(1L);
        transaction1.setMedication(medication1);
        transaction1.setQuantity(200);
        transaction1.setReceivedDate(Timestamp.from(timestamp1.toInstant()));
        transaction1.setSupplier("Supplier 1");

        transaction2.setId(2L);
        transaction2.setMedication(medication2);
        transaction2.setQuantity(400);
        transaction2.setReceivedDate(Timestamp.from(timestamp2.toInstant()));
        transaction2.setSupplier("Supplier 2");

        List<InboundTransaction> inboundTransactions = new ArrayList<>(Arrays.asList(transaction1, transaction2));

        when(inboundTransactionRepository.findAll()).thenReturn(inboundTransactions);

        mockMvc.perform(get("/inbound/transactions")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].medicationId").value(1L))
                .andExpect(jsonPath("$[0].quantity").value(200))
                .andExpect(jsonPath("$[0].receivedDate").value("2024-07-27T12:34:56.000+00:00"))
                .andExpect(jsonPath("$[0].supplier").value("Supplier 1"))
                .andExpect(jsonPath("$[1].medicationId").value(2L))
                .andExpect(jsonPath("$[1].quantity").value(400))
                .andExpect(jsonPath("$[1].receivedDate").value("2000-07-27T12:34:56.000+00:00"))
                .andExpect(jsonPath("$[1].supplier").value("Supplier 2"));
    }

    @Test
    public void testAddInboundTransaction() throws Exception {
        Medication medication = new Medication(
                1L, "medication1", "description1", 200, Medication.Types.OTC);

        InboundTransaction transaction = new InboundTransaction();
        transaction.setId(1L);
        transaction.setMedication(medication);
        transaction.setQuantity(200);
        transaction.setSupplier("Supplier 1");

        InboundTransactionDTO inboundTransactionDTO = new InboundTransactionDTO();
        inboundTransactionDTO.setMedicationId(1L);
        inboundTransactionDTO.setQuantity(200);
        inboundTransactionDTO.setSupplier("Supplier 1");

        ObjectMapper objectMapper = new ObjectMapper();
        String inboundTransactionString = objectMapper.writeValueAsString(inboundTransactionDTO);

        when(medicationRepository.findById(1L)).thenReturn(Optional.of(medication));
        when(medicationRepository.save(Mockito.any(Medication.class))).thenReturn(medication);
        when(inboundTransactionRepository.save(Mockito.any(InboundTransaction.class))).thenReturn(transaction);

        mockMvc.perform(post("/inbound/transactions")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(inboundTransactionString))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.medication.id").value(1L))
                .andExpect(jsonPath("$.quantity").value(200))
                .andExpect(jsonPath("$.supplier").value("Supplier 1"));
    }
}
