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
    // Usamos o snapshot para pegar o ID da URL de forma direta
    let id = this.activatedRoute.snapshot.params['id'];

    if (id) {
      this.id = id;
      this.service
        .getClientesById(this.id)
        .subscribe(
          response => this.cliente = response,
          errorResponse => {
            this.cliente = new Cliente();
            this.errors = ['Erro ao carregar o cliente.'];
          }
        );
    }
  }
  onSubmit() {
    this.success = false;
    this.errors = [];

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
        this.success = false;
        this.errors = errorResponse.error.errors;
      }
    );
  }

  voltar() {
    this.router.navigate(['/clientes-lista']);
  }
}
