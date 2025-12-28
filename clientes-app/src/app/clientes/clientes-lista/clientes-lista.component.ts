import { Component, OnInit } from '@angular/core';
import { Cliente } from '../cliente';
import { ClientesService } from '../../clientes.service';

@Component({
  selector: 'app-clientes-lista',
  templateUrl: './clientes-lista.component.html',
  styleUrls: ['./clientes-lista.component.css']
})
export class ClienteslistaComponent implements OnInit {

  clientes: Cliente[] = [];
  clienteSelecionado: Cliente | null = null;

  success: boolean = false;
  errors: string[] = [];

  constructor(private service: ClientesService) { }

  ngOnInit(): void {
    this.carregarClientes();
  }

  carregarClientes(): void {
    this.service.getClientes().subscribe(resposta => this.clientes = resposta);
  }

  preparaDelecao(cliente: Cliente): void {
    this.clienteSelecionado = cliente;
  }

  confirmarDelecao(): void {
    if (!this.clienteSelecionado) {
      return;
    }

    this.service.delete(this.clienteSelecionado).subscribe(
      () => {
        this.clientes = this.clientes.filter(c => c.id !== this.clienteSelecionado!.id);
        this.clienteSelecionado = null;

        this.success = true;
        this.errors = [];
      },
      () => {
        this.success = false;
        this.errors = ['Erro ao deletar o cliente.'];
      }
    );
  }
}
