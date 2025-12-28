import { Component, OnInit } from '@angular/core';
import { Cliente } from '../cliente';
import { ClientesService } from '../../clientes.service';
import { Router, ActivatedRoute } from '@angular/router';
import { Location } from '@angular/common';

@Component({
  selector: 'app-clientes-form',
  templateUrl: './clientes-form.component.html',
  styleUrls: ['./clientes-form.component.css']
})
export class ClientesFormComponent implements OnInit {

  cliente: Cliente;
  success: boolean = false;
  errors: string[] = [];
  id: number;

  constructor(
    private service: ClientesService,
    private router: Router,
    private activatedRoute: ActivatedRoute,
    private location: Location
  ) {
    this.cliente = new Cliente();
  }

  ngOnInit(): void {
    const idParam = this.activatedRoute.snapshot.params['id'];

    if (idParam) {
      this.id = idParam;
      this.service
        .getClientesById(this.id)
        .subscribe(
          response => this.cliente = response,
          () => {
            this.cliente = new Cliente();
            this.errors = ['Erro ao carregar o cliente.'];
          }
        );
    }
  }

  onSubmit() {
    this.success = false;
    this.errors = [];

    // EDITAR (PUT)
    if (this.id) {
      this.service
        .atualizar(this.cliente)
        .subscribe(
          response => {
            this.success = true;
            this.errors = [];
            this.cliente = response;

            setTimeout(() => {
              this.router.navigate(['/clientes-lista']);
            }, 800);
          },
          errorResponse => {
            if (errorResponse.status === 400 &&
              errorResponse.error?.error === 'Já existe um cliente com esse CPF.') {
              this.errors = ['Já existe um cliente com esse CPF.'];
            } else {
              this.errors = ['Erro ao atualizar o cliente.'];
            }
          }
        );
      return;
    }

    // CRIAR (POST)
    this.service.salvar(this.cliente).subscribe(
      response => {
        this.success = true;
        this.errors = [];
        this.cliente = response;

        setTimeout(() => {
          this.router.navigate(['/clientes-lista']);
        }, 800);
      },
      errorResponse => {
        if (errorResponse.status === 400 &&
          errorResponse.error?.error === 'Já existe um cliente com esse CPF.') {
          this.errors = ['Já existe um cliente com esse CPF.'];
        } else if (errorResponse.error?.errors) {
          this.errors = errorResponse.error.errors;
        } else {
          this.errors = ['Erro ao salvar o cliente.'];
        }
      }
    );
  }

  voltar() {
    this.router.navigate(['/clientes-lista']);
  }
}
