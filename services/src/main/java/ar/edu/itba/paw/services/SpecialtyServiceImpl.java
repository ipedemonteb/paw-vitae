package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaceServices.SpecialtyService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class SpecialtyServiceImpl implements SpecialtyService {
    public List<String> getSpecialties() {
        return List.of(
                "General", "Cardiologia", "Dermatologia",
                "Endocrinologia", "Gastroenterología", "Hematologia",
                "Enfermedades Infecciosas ", "Nefrologia", "Neurologia",
                "Oncologia", "Pulmonologia", "Reumatologia",
                "Urologia", "Pediatria", "Ginecologia", "Traumatologia"
        );
    }
}

