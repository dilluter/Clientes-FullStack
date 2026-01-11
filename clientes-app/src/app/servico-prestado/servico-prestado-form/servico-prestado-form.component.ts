import { Component, OnInit } from '@angular/core';
import {Cliente} from '../../clientes/cliente';
import {ClientesService} from '../../clientes.service';
import {FormGroup} from '@angular/forms';
import {ServicoPrestado} from '../servicoPrestado';

@Component({
  selector: 'app-servico-prestado-form',
  templateUrl: './servico-prestado-form.component.html',
  styleUrls: ['./servico-prestado-form.component.css']
})
export class ServicoPrestadoFormComponent implements OnInit {

  clientes: Cliente[] = [];
  servicoPrest: ServicoPrestado;

  constructor(
    private clienteService: ClientesService,
  ) {
    this.servicoPrest = new ServicoPrestado();
  }

  ngOnInit(): void {
    this.clienteService.getClientes()
      .subscribe(response => {this.clientes = response;})
  }

  onSubmit(){
    console.log('this.servico');
  }
}
