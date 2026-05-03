import { Component, OnInit } from '@angular/core';
import { Cliente } from '../../clientes/cliente';
import { ClientesService } from '../../clientes/service/clientes.service';
import { ServicoPrestadoService } from '../service/servico-prestado.service';
import { DateUtil } from '../../utils/date.util';
import { HttpErrorResponse } from '@angular/common/http';
import { ErrorUtil } from '../../utils/error.util';
import { Router } from '@angular/router';
import {ServicoPrestado} from '../servicoPrestado';

@Component({
  selector: 'app-servico-prestado-form',
  templateUrl: './servico-prestado-form.component.html',
  styleUrls: ['./servico-prestado-form.component.css']
})
export class ServicoPrestadoFormComponent implements OnInit {

  clientes: Cliente[] = [];
  servicoPrest: ServicoPrestado = this.criarServicoVazio();

  success = false;
  errors: string[] = [];
  dataFormatada = '';

  constructor(
    private readonly clienteService: ClientesService,
    private readonly servicoPrestadoService: ServicoPrestadoService,
    private readonly router: Router
  ) {}

  ngOnInit(): void {
    this.carregarClientes();
  }

  onSubmit(): void {
    this.success = false;
    this.errors = [];

    if (!DateUtil.isValidDateBr(this.dataFormatada)) {
      this.errors = ['Data inválida. Use o formato dd/MM/yyyy.'];
      return;
    }

    if (DateUtil.isPastDate(this.dataFormatada)) {
      this.errors = ['A data não pode ser anterior à data atual.'];
      return;
    }

    this.servicoPrest.data = DateUtil.toBackend(this.dataFormatada);

    this.servicoPrestadoService.salvar(this.servicoPrest).subscribe({
      next: () => {
        this.success = true;
        this.errors = [];
        this.servicoPrest = this.criarServicoVazio();
        this.dataFormatada = '';

        window.setTimeout(() => {
          this.router.navigate(['/servico-prestado/lista']);
        }, 800);
      },
      error: (errorResponse: HttpErrorResponse) => {
        this.success = false;
        this.errors = ErrorUtil.getErrors(errorResponse, 'Erro ao processar a requisição.');
      }
    });
  }

  onDataChange(event: Event): void {
    const input = event.target as HTMLInputElement;
    this.dataFormatada = DateUtil.format(input.value);
    input.value = this.dataFormatada;
    this.servicoPrest.data = DateUtil.toBackend(this.dataFormatada);
  }

  private carregarClientes(): void {
    this.clienteService.getClientesParaSelect().subscribe({
      next: response => {
        this.clientes = response.content;
      },
      error: (errorResponse: HttpErrorResponse) => {
        this.errors = ErrorUtil.getErrors(errorResponse, 'Erro ao carregar clientes.');
      }
    });
  }

  private criarServicoVazio(): ServicoPrestado {
    return {
      descricao: '',
      data: '',
      idCliente: undefined,
      valor: undefined
    };
  }
}
