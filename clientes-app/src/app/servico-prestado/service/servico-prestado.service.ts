import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import {ServicoPrestado} from '../servicoPrestado';
import {ServicoPrestadoBusca} from '../servico-prestado-lista/servicoPrestadoBusca';
import {PageResponse} from '../../models/page-response';

@Injectable({
  providedIn: 'root'
})
export class ServicoPrestadoService {

  private readonly apiUrl = `${environment.apiUrlBase}/servicos-prestados`;

  constructor(private readonly http: HttpClient) {}

  salvar(servicoPrestado: ServicoPrestado): Observable<ServicoPrestadoBusca> {
    return this.http.post<ServicoPrestadoBusca>(this.apiUrl, servicoPrestado);
  }

  buscar(
    nome = '',
    mes?: number,
    page = 0,
    size = 10
  ): Observable<PageResponse<ServicoPrestadoBusca>> {
    let params = new HttpParams()
      .set('nome', nome)
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sort', 'data,desc');

    if (mes) {
      params = params.set('mes', mes.toString());
    }

    return this.http.get<PageResponse<ServicoPrestadoBusca>>(this.apiUrl, { params });
  }
}
