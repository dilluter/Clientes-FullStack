import { Component, OnInit } from '@angular/core';
import { Cliente } from '../cliente';
import { ClientesService } from '../service/clientes.service';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';
import { ErrorUtil } from '../../utils/error.util';
import {MaskUtil} from '../../utils/mask.util';

@Component({
  selector: 'app-clientes-form',
  templateUrl: './clientes-form.component.html',
  styleUrls: ['./clientes-form.component.css']
})
export class ClientesFormComponent implements OnInit {

  cliente: Cliente = this.criarClienteVazio();
  success = false;
  errors: string[] = [];
  id?: number;

  constructor(
    private readonly service: ClientesService,
    private readonly router: Router,
    private readonly activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    const idParam = this.activatedRoute.snapshot.paramMap.get('id');

    if (!idParam) {
      return;
    }

    const idConvertido = Number(idParam);

    if (Number.isNaN(idConvertido)) {
      this.errors = ['ID do cliente inválido.'];
      return;
    }

    this.id = idConvertido;
    this.carregarCliente(idConvertido);
  }

  onSubmit(): void {
    this.success = false;
    this.errors = [];

    if (this.id) {
      this.atualizarCliente();
      return;
    }

    this.salvarCliente();
  }

  voltar(): void {
    this.router.navigate(['/clientes/lista']);
  }

  private carregarCliente(id: number): void {
    this.service.getClientesById(id).subscribe({
      next: response => {
        this.cliente = response;
      },
      error: (errorResponse: HttpErrorResponse) => {
        this.cliente = this.criarClienteVazio();
        this.errors = ErrorUtil.getErrors(errorResponse, 'Erro ao carregar o cliente.');
      }
    });
  }
  onCpfChange(event: Event): void {
    const input = event.target as HTMLInputElement;

    const cpfFormatado = MaskUtil.cpf(input.value);

    input.value = cpfFormatado;
    this.cliente.cpf = cpfFormatado;
  }

  private normalizarClienteAntesDeEnviar(): Cliente {
    return {
      ...this.cliente,
      cpf: MaskUtil.onlyNumbers(this.cliente.cpf)
    };
  }

  private salvarCliente(): void {
    const clienteRequest = this.normalizarClienteAntesDeEnviar();

    this.service.salvar(clienteRequest).subscribe({
      next: response => {
        this.cliente = response;
        this.redirecionarComSucesso();
      },
      error: (errorResponse: HttpErrorResponse) => {
        this.success = false;
        this.errors = ErrorUtil.getErrors(errorResponse, 'Erro ao salvar o cliente.');
      }
    });
  }

  private atualizarCliente(): void {
    const clienteRequest = this.normalizarClienteAntesDeEnviar();

    this.service.atualizar(clienteRequest).subscribe({
      next: response => {
        this.cliente = response;
        this.redirecionarComSucesso();
      },
      error: (errorResponse: HttpErrorResponse) => {
        this.success = false;
        this.errors = ErrorUtil.getErrors(errorResponse, 'Erro ao atualizar o cliente.');
      }
    });
  }

  private redirecionarComSucesso(): void {
    this.success = true;
    this.errors = [];

    window.setTimeout(() => {
      this.router.navigate(['/clientes/lista']);
    }, 800);
  }

  private criarClienteVazio(): Cliente {
    return {
      nome: '',
      cpf: ''
    };
  }
}
