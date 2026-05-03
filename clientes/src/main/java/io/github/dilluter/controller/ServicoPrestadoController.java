package io.github.dilluter.controller;

import io.github.dilluter.dto.servico.request.ServicoPrestadoCreateDTO;
import io.github.dilluter.dto.servico.response.ServicoPrestadoResponseDTO;
import io.github.dilluter.service.ServicoPrestadoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/servicos-prestados")
@RequiredArgsConstructor
public class ServicoPrestadoController {

    private final ServicoPrestadoService servicoPrestadoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ServicoPrestadoResponseDTO salvar(@RequestBody @Valid ServicoPrestadoCreateDTO dto) {
        return servicoPrestadoService.salvar(dto);
    }

    @GetMapping
    public Page<ServicoPrestadoResponseDTO> pesquisar(
            @RequestParam(value = "nome", required = false, defaultValue = "") String nome,
            @RequestParam(value = "mes", required = false) Integer mes,
            Pageable pageable
    ) {
        return servicoPrestadoService.pesquisar(nome, mes, pageable);
    }
}
