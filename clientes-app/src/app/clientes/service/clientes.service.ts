import { Injectable } from '@angular/core';
import { Cliente } from '../cliente';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { PageResponse } from '../../models/page-response';

@Injectable({
  providedIn: 'root'
})
export class ClientesService {

  private readonly apiUrl = `${environment.apiUrlBase}/clientes`;

  constructor(private readonly http: HttpClient) {}

  salvar(cliente: Cliente): Observable<Cliente> {
    return this.http.post<Cliente>(this.apiUrl, cliente);
  }

  atualizar(cliente: Cliente): Observable<Cliente> {
    return this.http.put<Cliente>(`${this.apiUrl}/${cliente.id}`, cliente);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  getClientes(page = 0, size = 10, sort = 'id,desc'): Observable<PageResponse<Cliente>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sort', sort);

    return this.http.get<PageResponse<Cliente>>(this.apiUrl, { params });
  }

  getClientesParaSelect(): Observable<PageResponse<Cliente>> {
    const params = new HttpParams()
      .set('page', '0')
      .set('size', '100')
      .set('sort', 'nome,asc');

    return this.http.get<PageResponse<Cliente>>(this.apiUrl, { params });
  }

  getClientesById(id: number): Observable<Cliente> {
    return this.http.get<Cliente>(`${this.apiUrl}/${id}`);
  }
}
