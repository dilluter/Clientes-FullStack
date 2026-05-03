package io.github.dilluter.controller;

import io.github.dilluter.dto.cliente.request.ClienteCreateDTO;
import io.github.dilluter.dto.cliente.request.ClienteUpdateDTO;
import io.github.dilluter.dto.cliente.response.ClienteResponseDTO;
import io.github.dilluter.service.ClienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;

    @GetMapping
    public Page<ClienteResponseDTO> listarTodos(Pageable pageable) {
        return clienteService.listarTodos(pageable);
    }

    @GetMapping("/{id}")
    public ClienteResponseDTO buscarPorId(@PathVariable Integer id) {
        return clienteService.buscarPorId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ClienteResponseDTO salvar(@RequestBody @Valid ClienteCreateDTO clienteCreateDTO) {
        return clienteService.salvar(clienteCreateDTO);
    }

    @PutMapping("/{id}")
    public ClienteResponseDTO atualizar(
            @PathVariable Integer id,
            @RequestBody @Valid ClienteUpdateDTO clienteUpdateDTO
    ) {
        return clienteService.atualizar(id, clienteUpdateDTO);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Integer id) {
        clienteService.deletar(id);
    }
}