package io.github.dilluter.service;

import io.github.dilluter.dto.cliente.request.ClienteCreateDTO;
import io.github.dilluter.dto.cliente.request.ClienteUpdateDTO;
import io.github.dilluter.dto.cliente.response.ClienteResponseDTO;
import io.github.dilluter.exception.BusinessException;
import io.github.dilluter.exception.NotFoundException;
import io.github.dilluter.model.entity.Cliente;
import io.github.dilluter.model.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public Page<ClienteResponseDTO> listarTodos(Pageable pageable) {
        return clienteRepository.findAll(pageable)
                .map(ClienteResponseDTO::fromEntity);
    }

    public ClienteResponseDTO buscarPorId(Integer id) {
        Cliente cliente = buscarEntidadePorId(id);
        return ClienteResponseDTO.fromEntity(cliente);
    }

    public ClienteResponseDTO salvar(ClienteCreateDTO clienteCreateDTO) {
        validarCpfDisponivel(clienteCreateDTO.getCpf());

        Cliente cliente = Cliente.builder()
                .nome(clienteCreateDTO.getNome())
                .cpf(clienteCreateDTO.getCpf())
                .build();

        Cliente clienteSalvo = clienteRepository.save(cliente);

        return ClienteResponseDTO.fromEntity(clienteSalvo);
    }

    public ClienteResponseDTO atualizar(Integer id, ClienteUpdateDTO clienteUpdateDTO) {
        Cliente cliente = buscarEntidadePorId(id);

        clienteRepository.findByCpf(clienteUpdateDTO.getCpf())
                .filter(clienteComCpf -> !clienteComCpf.getId().equals(id))
                .ifPresent(clienteComCpf -> {
                    throw new BusinessException("Já existe outro cliente cadastrado com esse CPF.");
                });

        cliente.setNome(clienteUpdateDTO.getNome());
        cliente.setCpf(clienteUpdateDTO.getCpf());

        Cliente clienteAtualizado = clienteRepository.save(cliente);

        return ClienteResponseDTO.fromEntity(clienteAtualizado);
    }

    public void deletar(Integer id) {
        Cliente cliente = buscarEntidadePorId(id);
        clienteRepository.delete(cliente);
    }

    public Cliente buscarEntidadePorId(Integer id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cliente não encontrado."));
    }

    private void validarCpfDisponivel(String cpf) {
        clienteRepository.findByCpf(cpf)
                .ifPresent(cliente -> {
                    throw new BusinessException("Já existe um cliente cadastrado com esse CPF.");
                });
    }
}