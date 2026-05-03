import { TestBed } from '@angular/core/testing';
import { ClientesService } from './clientes.service';
import {
  HttpClientTestingModule,
  HttpTestingController
} from '@angular/common/http/testing';
import { environment } from '../../../environments/environment';
import { Cliente } from '../cliente';

describe('ClientesService', () => {
  let service: ClientesService;
  let httpMock: HttpTestingController;

  const apiUrl = `${environment.apiUrlBase}/clientes`;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [ClientesService]
    });

    service = TestBed.inject(ClientesService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('deve criar o service', () => {
    expect(service).toBeTruthy();
  });

  it('deve salvar cliente via POST', () => {
    const cliente: Cliente = {
      nome: 'Igor',
      cpf: '12345678901'
    };

    service.salvar(cliente).subscribe(response => {
      expect(response).toEqual({ id: 1, ...cliente });
    });

    const req = httpMock.expectOne(apiUrl);

    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(cliente);

    req.flush({ id: 1, ...cliente });
  });

  it('deve atualizar cliente via PUT', () => {
    const cliente: Cliente = {
      id: 1,
      nome: 'Igor',
      cpf: '12345678901'
    };

    service.atualizar(cliente).subscribe(response => {
      expect(response).toEqual(cliente);
    });

    const req = httpMock.expectOne(`${apiUrl}/1`);

    expect(req.request.method).toBe('PUT');
    expect(req.request.body).toEqual(cliente);

    req.flush(cliente);
  });

  it('deve deletar cliente via DELETE', () => {
    service.delete(1).subscribe(response => {
      expect(response).toBeNull();
    });

    const req = httpMock.expectOne(`${apiUrl}/1`);

    expect(req.request.method).toBe('DELETE');

    req.flush(null);
  });

  it('deve buscar clientes paginados', () => {
    service.getClientes(2, 20, 'nome,asc').subscribe();

    const req = httpMock.expectOne(request =>
      request.url === apiUrl &&
      request.params.get('page') === '2' &&
      request.params.get('size') === '20' &&
      request.params.get('sort') === 'nome,asc'
    );

    expect(req.request.method).toBe('GET');

    req.flush({
      content: [],
      number: 2,
      totalPages: 0,
      totalElements: 0
    });
  });

  it('deve buscar clientes para select', () => {
    service.getClientesParaSelect().subscribe();

    const req = httpMock.expectOne(request =>
      request.url === apiUrl &&
      request.params.get('page') === '0' &&
      request.params.get('size') === '100' &&
      request.params.get('sort') === 'nome,asc'
    );

    expect(req.request.method).toBe('GET');

    req.flush({
      content: [],
      number: 0,
      totalPages: 0,
      totalElements: 0
    });
  });

  it('deve buscar cliente por id', () => {
    service.getClientesById(1).subscribe(response => {
      expect(response.id).toBe(1);
    });

    const req = httpMock.expectOne(`${apiUrl}/1`);

    expect(req.request.method).toBe('GET');

    req.flush({
      id: 1,
      nome: 'Igor',
      cpf: '12345678901'
    });
  });
});
