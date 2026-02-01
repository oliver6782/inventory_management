package com.project.inventory_management.config;

import com.project.inventory_management.entity.InboundTransaction;
import com.project.inventory_management.entity.Medication;
import com.project.inventory_management.entity.OutboundTransaction;
import com.project.inventory_management.entity.User;
import com.project.inventory_management.repository.InboundTransactionRepository;
import com.project.inventory_management.repository.MedicationRepository;
import com.project.inventory_management.repository.OutboundTransactionRepository;
import com.project.inventory_management.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.Date;

@Component
@Profile("default")
public class DataInitializer implements CommandLineRunner {

    private final MedicationRepository medicationRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final InboundTransactionRepository inboundTransactionRepository;
    private final OutboundTransactionRepository outboundTransactionRepository;

    @Autowired
    public DataInitializer(MedicationRepository medicationRepository,
                           UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           InboundTransactionRepository inboundTransactionRepository,
                           OutboundTransactionRepository outboundTransactionRepository) {
        this.medicationRepository = medicationRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.inboundTransactionRepository = inboundTransactionRepository;
        this.outboundTransactionRepository = outboundTransactionRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (medicationRepository.count() == 0) {
            Medication m1 = new Medication();
            m1.setName("Paracetamol");
            m1.setDescription("Pain reliever and fever reducer.");
            m1.setQuantity(100);
            m1.setType(Medication.Types.PRES);

            Medication m2 = new Medication();
            m2.setName("Ibuprofen");
            m2.setDescription("Nonsteroidal anti-inflammatory drug.");
            m2.setQuantity(80);
            m2.setType(Medication.Types.OTC);

            Medication m3 = new Medication();
            m3.setName("Vitamin C");
            m3.setDescription("Supplement for immune health.");
            m3.setQuantity(200);
            m3.setType(Medication.Types.OTHER);

            medicationRepository.save(m1);
            medicationRepository.save(m2);
            medicationRepository.save(m3);

            // Add a couple of transactions
            InboundTransaction in1 = new InboundTransaction();
            in1.setMedication(m1);
            in1.setQuantity(50);
            in1.setSupplier("Supplier A");
            in1.setReceivedDate(new Date());
            inboundTransactionRepository.save(in1);

            OutboundTransaction out1 = new OutboundTransaction();
            out1.setMedication(m2);
            out1.setQuantity(10);
            out1.setReceiver("Pharmacy X");
            out1.setDispatchedDate(new Date());
            outboundTransactionRepository.save(out1);
        }

        if (userRepository.findByEmail("admin@example.com").isEmpty()) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setEmail("admin@example.com");
            admin.setPassword(passwordEncoder.encode("adminpass"));
            admin.setRole(User.Roles.ADMIN);
            admin.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            admin.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
            userRepository.save(admin);
        }
    }
}
