import { Component } from '@angular/core';
import { ServicoPrestadoService } from '../service/servico-prestado.service';
import { HttpErrorResponse } from '@angular/common/http';
import { ErrorUtil } from '../../utils/error.util';
import {ServicoPrestadoBusca} from './servicoPrestadoBusca';

@Component({
  selector: 'app-servico-prestado-lista',
  templateUrl: './servico-prestado-lista.component.html',
  styleUrls: ['./servico-prestado-lista.component.css']
})
export class ServicoPrestadoListaComponent {

  nome = '';
  mes?: number;
  meses: number[] = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12];

  lista: ServicoPrestadoBusca[] = [];
  message = '';
  errors: string[] = [];

  paginaAtual = 0;
  tamanhoPagina = 10;
  totalPaginas = 0;
  totalElementos = 0;

  constructor(
    private readonly servicoPrestadoService: ServicoPrestadoService
  ) {}

  consultar(page = 0): void {
    this.message = '';
    this.errors = [];

    this.servicoPrestadoService
      .buscar(this.nome, this.mes, page, this.tamanhoPagina)
      .subscribe({
        next: response => {
          this.lista = response.content;
          this.paginaAtual = response.number;
          this.totalPaginas = response.totalPages;
          this.totalElementos = response.totalElements;

          if (this.lista.length === 0) {
            this.message = 'Nenhum resultado encontrado.';
          }
        },
        error: (errorResponse: HttpErrorResponse) => {
          this.errors = ErrorUtil.getErrors(errorResponse, 'Erro ao consultar serviços prestados.');
        }
      });
  }

  limparFiltros(): void {
    this.nome = '';
    this.mes = undefined;
    this.lista = [];
    this.message = '';
    this.errors = [];
    this.paginaAtual = 0;
    this.totalPaginas = 0;
    this.totalElementos = 0;
  }

  paginaAnterior(): void {
    if (this.paginaAtual > 0) {
      this.consultar(this.paginaAtual - 1);
    }
  }

  proximaPagina(): void {
    if (this.paginaAtual + 1 < this.totalPaginas) {
      this.consultar(this.paginaAtual + 1);
    }
  }

  get possuiResultados(): boolean {
    return this.lista.length > 0;
  }
}
