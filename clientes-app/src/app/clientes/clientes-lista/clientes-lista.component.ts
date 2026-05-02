import { Component, OnInit } from '@angular/core';
import { Cliente } from '../cliente';
import { ClientesService } from '../service/clientes.service';
import { HttpErrorResponse } from '@angular/common/http';
import { ErrorUtil } from '../../utils/error.util';

@Component({
  selector: 'app-clientes-lista',
  templateUrl: './clientes-lista.component.html',
  styleUrls: ['./clientes-lista.component.css']
})
export class ClientesListaComponent implements OnInit {

  clientes: Cliente[] = [];
  clienteSelecionado?: Cliente;

  success = false;
  errors: string[] = [];

  paginaAtual = 0;
  tamanhoPagina = 10;
  totalPaginas = 0;
  totalElementos = 0;

  constructor(private readonly service: ClientesService) {}

  ngOnInit(): void {
    this.carregarClientes();
  }

  carregarClientes(page = 0): void {
    this.success = false;
    this.errors = [];

    this.service.getClientes(page, this.tamanhoPagina).subscribe({
      next: resposta => {
        this.clientes = resposta.content;
        this.paginaAtual = resposta.number;
        this.totalPaginas = resposta.totalPages;
        this.totalElementos = resposta.totalElements;
      },
      error: (errorResponse: HttpErrorResponse) => {
        this.errors = ErrorUtil.getErrors(errorResponse, 'Erro ao carregar clientes.');
      }
    });
  }

  preparaDelecao(cliente: Cliente): void {
    this.clienteSelecionado = cliente;
  }

  confirmarDelecao(): void {
    if (!this.clienteSelecionado?.id) {
      return;
    }

    this.service.delete(this.clienteSelecionado.id).subscribe({
      next: () => {
        this.success = true;
        this.errors = [];
        this.clienteSelecionado = undefined;
        this.carregarClientes(this.paginaAtual);
      },
      error: (errorResponse: HttpErrorResponse) => {
        this.success = false;
        this.errors = ErrorUtil.getErrors(errorResponse, 'Erro ao deletar o cliente.');
      }
    });
  }

  paginaAnterior(): void {
    if (this.paginaAtual > 0) {
      this.carregarClientes(this.paginaAtual - 1);
    }
  }

  proximaPagina(): void {
    if (this.paginaAtual + 1 < this.totalPaginas) {
      this.carregarClientes(this.paginaAtual + 1);
    }
  }

  get possuiClientes(): boolean {
    return this.clientes.length > 0;
  }
}
