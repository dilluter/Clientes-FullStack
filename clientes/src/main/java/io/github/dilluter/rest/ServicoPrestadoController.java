package io.github.dilluter.rest;

import io.github.dilluter.model.entity.Cliente;
import io.github.dilluter.model.entity.ServicoPrestado;
import io.github.dilluter.model.repository.ClienteRepository;
import io.github.dilluter.model.repository.ServicoPrestadoRepository;
import io.github.dilluter.rest.dto.ServicoPrestadoDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/servicos-prestados")
public class ServicoPrestadoController {

    private final ClienteRepository clienteRepository;
    private final ServicoPrestadoRepository servicoPrestadoRepository;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ServicoPrestado salvar(@RequestBody ServicoPrestadoDTO dto) {

        LocalDate data = LocalDate.parse(
                dto.getData(),
                DateTimeFormatter.ofPattern("dd/MM/yyyy")
        );

        Integer idCliente = dto.getIdCliente();

        Cliente cliente = clienteRepository.findById(idCliente)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.BAD_REQUEST,
                                "Cliente inexistente"
                        )
                );

        ServicoPrestado servicoPrestado = new ServicoPrestado();
        servicoPrestado.setDescricao(dto.getDescricao());
        servicoPrestado.setData(data);
        servicoPrestado.setCliente(cliente);
        servicoPrestado.setValor(dto.getValor());

        return servicoPrestadoRepository.save(servicoPrestado);
    }
}
