import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { ClientesFormComponent } from './clientes-form/clientes-form.component';
import {ClienteslistaComponent} from './clientes-lista/clientes-lista.component';
import {LayoutComponent} from '../layout/layout.component';


const routes: Routes = [
  { path : 'clientes' , component: LayoutComponent, children: [
      { path: 'form', component: ClientesFormComponent},
      { path: 'lista', component: ClienteslistaComponent},
      { path: 'form/:id', component: ClientesFormComponent},
      { path: '', redirectTo: 'lista', pathMatch: 'full'}
  ]}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ClientesRoutingModule { }
