package com.example.clinica_medica.services;

import com.example.clinica_medica.entities.Medico;
import com.example.clinica_medica.repositories.MedicoRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MedicoService {
    private final MedicoRepository medicoRepository;
    private final ValidationService validationService;

    public MedicoService(MedicoRepository medicoRepository, ValidationService validationService) {
        this.medicoRepository = medicoRepository;
        this.validationService = validationService;
    }

    @Transactional
    public Medico incluirMedico(Medico medico) {
        validationService.validarMedico(medico);
        return medicoRepository.save(medico);
    }

    @Transactional
    public void excluirMedico(Long id) {
        medicoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Médico não encontrado"));
        medicoRepository.deleteById(id);
    }

    @Transactional
    public Medico atualizarMedico(Long id, Medico medico) {
        medicoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Médico não encontrado"));
        medico.setId(id);
        validationService.validarMedico(medico);
        return medicoRepository.save(medico);
    }

    @Transactional(readOnly = true)
    public Medico buscarMedicoPorId(Long id) {
        return medicoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Médico não encontrado"));
    }

    @Transactional(readOnly = true)
    public List<Medico> listarTodosMedicos() {
        return medicoRepository.findAll();
    }
}
