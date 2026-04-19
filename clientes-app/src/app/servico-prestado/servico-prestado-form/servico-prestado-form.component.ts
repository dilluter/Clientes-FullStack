import { Component, OnInit } from '@angular/core';
import { Cliente } from '../../clientes/cliente';
import { ClientesService } from '../../clientes.service';
import { ServicoPrestado } from '../servicoPrestado';
import { ServicoPrestadoService } from '../../servico-prestado.service';
import { DateUtil } from 'src/app/utils/date.util';

@Component({
  selector: 'app-servico-prestado-form',
  templateUrl: './servico-prestado-form.component.html',
  styleUrls: ['./servico-prestado-form.component.css']
})
export class ServicoPrestadoFormComponent implements OnInit {

  clientes: Cliente[] = [];
  servicoPrest: ServicoPrestado;
  success = false;
  errors: string[] = [];
  dataFormatada = '';

  constructor(
    private clienteService: ClientesService,
    private servicoPrestadoService: ServicoPrestadoService
  ) {
    this.servicoPrest = new ServicoPrestado();
  }

  ngOnInit(): void {
    this.clienteService.getClientes()
      .subscribe(response => this.clientes = response);
  }

  onSubmit() {
    this.servicoPrestadoService.salvar(this.servicoPrest)
      .subscribe(
        () => {
          this.success = true;
          this.errors = [];
          this.servicoPrest = new ServicoPrestado();
          this.dataFormatada = '';
        },
        errorResponse => {
          this.success = false;

          if (errorResponse.error && errorResponse.error.errors) {
            this.errors = errorResponse.error.errors;
          } else if (Array.isArray(errorResponse.error)) {
            this.errors = errorResponse.error;
          } else if (typeof errorResponse.error === 'string') {
            this.errors = [errorResponse.error];
          } else {
            this.errors = ['Erro ao processar a requisição.'];
          }
        }
      );
  }

  onDataChange(event: any) {
    const raw = event.target.value;

    this.dataFormatada = DateUtil.format(raw);
    this.servicoPrest.data = DateUtil.toBackend(this.dataFormatada);
  }

}
