import { Component, OnInit } from '@angular/core';
import { Cliente } from '../cliente';
import { ClientesService } from '../service/clientes.service';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';
import { ErrorUtil } from '../../utils/error.util';

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

  private salvarCliente(): void {
    this.service.salvar(this.cliente).subscribe({
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
    this.service.atualizar(this.cliente).subscribe({
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
