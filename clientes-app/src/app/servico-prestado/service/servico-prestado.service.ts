import { Injectable } from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {ServicoPrestado} from '../servicoPrestado';
import {Observable} from 'rxjs';
import {environment} from '../../../environments/environment';
import { ServicoPrestadoBusca } from '../servico-prestado-lista/servicoPrestadoBusca';

@Injectable({
  providedIn: 'root'
})
export class ServicoPrestadoService {

  private readonly apiUrl = `${environment.apiUrlBase}/servicos-prestados`;

  constructor(private http: HttpClient) { }

  salvar(servicoPrestado: ServicoPrestado): Observable<ServicoPrestado> {
    return this.http.post<ServicoPrestado>(this.apiUrl, servicoPrestado);
  }
  buscar(nome: string, mes: number): Observable<ServicoPrestadoBusca[]> {

    let httpParams = new HttpParams();

    if (!nome) {
      nome = '';
    }

    httpParams = httpParams.set('nome', nome);

    if (mes) {
      httpParams = httpParams.set('mes', mes.toString());
    }
    return this.http.get<ServicoPrestadoBusca[]>(this.apiUrl, {
      params: httpParams
    });
  }
}
