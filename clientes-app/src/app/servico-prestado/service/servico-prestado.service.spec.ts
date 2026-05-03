import { TestBed } from '@angular/core/testing';
import { ServicoPrestadoService } from './servico-prestado.service';
import {
  HttpClientTestingModule,
  HttpTestingController
} from '@angular/common/http/testing';
import { environment } from '../../../environments/environment';
import { ServicoPrestado } from '../servicoPrestado';

describe('ServicoPrestadoService', () => {
  let service: ServicoPrestadoService;
  let httpMock: HttpTestingController;

  const apiUrl = `${environment.apiUrlBase}/servicos-prestados`;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [ServicoPrestadoService]
    });

    service = TestBed.inject(ServicoPrestadoService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('deve criar o service', () => {
    expect(service).toBeTruthy();
  });

  it('deve salvar serviço prestado via POST', () => {
    const servico: ServicoPrestado = {
      descricao: 'Serviço teste',
      data: '2030-01-01',
      idCliente: 1,
      valor: 100
    };

    service.salvar(servico).subscribe(response => {
      expect(response).toEqual({
        id: 1,
        descricao: 'Serviço teste',
        data: '2030-01-01',
        nomeCliente: 'Igor',
        valor: 100
      } as any);
    });

    const req = httpMock.expectOne(apiUrl);

    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(servico);

    req.flush({
      id: 1,
      descricao: 'Serviço teste',
      data: '2030-01-01',
      nomeCliente: 'Igor',
      valor: 100
    });
  });

  it('deve buscar serviços prestados sem mês', () => {
    service.buscar('Igor', undefined, 0, 10).subscribe();

    const req = httpMock.expectOne(request =>
      request.url === apiUrl &&
      request.params.get('nome') === 'Igor' &&
      request.params.get('page') === '0' &&
      request.params.get('size') === '10' &&
      request.params.get('sort') === 'data,desc' &&
      request.params.get('mes') === null
    );

    expect(req.request.method).toBe('GET');

    req.flush({
      content: [],
      number: 0,
      totalPages: 0,
      totalElements: 0
    });
  });

  it('deve buscar serviços prestados com mês', () => {
    service.buscar('Igor', 5, 1, 20).subscribe();

    const req = httpMock.expectOne(request =>
      request.url === apiUrl &&
      request.params.get('nome') === 'Igor' &&
      request.params.get('mes') === '5' &&
      request.params.get('page') === '1' &&
      request.params.get('size') === '20' &&
      request.params.get('sort') === 'data,desc'
    );

    expect(req.request.method).toBe('GET');

    req.flush({
      content: [],
      number: 1,
      totalPages: 0,
      totalElements: 0
    });
  });
});
