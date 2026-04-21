import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { ServicoPrestadoService } from './servico-prestado.service';
import { environment } from '../../../environments/environment';
import { ServicoPrestadoBusca } from '../servico-prestado-lista/servicoPrestadoBusca';

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

  it('deve ser criado', () => {
    expect(service).toBeTruthy();
  });

  it('deve buscar serviços prestados com parametros (nome e mes)', () => {
    const mockResponse = [
      { descricao: 'Desenvolvimento', valor: 5000, data: '15/05/2023', cliente: { nome: 'Empresa X' } }
    ] as ServicoPrestadoBusca[];

    service.buscar('Empresa X', 5).subscribe(res => {
      expect(res).toEqual(mockResponse);
    });

    const req = httpMock.expectOne(request =>
      request.url === apiUrl &&
      request.params.get('nome') === 'Empresa X' &&
      request.params.get('mes') === '5'
    );

    expect(req.request.method).toBe('GET');
    req.flush(mockResponse);
  });
});
