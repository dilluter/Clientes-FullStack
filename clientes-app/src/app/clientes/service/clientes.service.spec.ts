import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { ClientesService } from './clientes.service';
import { Cliente } from '../cliente';
import { environment } from '../../../environments/environment';

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

  it('deve ser criado', () => {
    expect(service).toBeTruthy();
  });

  it('deve salvar um cliente (POST)', () => {
    const mockCliente = { id: 1, nome: 'João', cpf: '12345678900', dataCadastro: '10/10/2020' } as unknown as Cliente;

    service.salvar(mockCliente).subscribe((cliente) => {
      expect(cliente).toEqual(mockCliente);
    });

    const req = httpMock.expectOne(apiUrl);
    expect(req.request.method).toBe('POST');
    req.flush(mockCliente);
  });

  it('deve buscar clientes (GET)', () => {
    const mockClientes = [{ id: 1, nome: 'João', cpf: '12345678900', dataCadastro: '10/10/2020' }] as unknown as Cliente[];

    service.getClientes().subscribe((clientes) => {
      expect(clientes.length).toBe(1);
      expect(clientes).toEqual(mockClientes);
    });

    const req = httpMock.expectOne(apiUrl);
    expect(req.request.method).toBe('GET');
    req.flush(mockClientes);
  });

  it('deve deletar um cliente (DELETE)', () => {
    const mockCliente = { id: 1, nome: 'João', cpf: '12345678900', dataCadastro: '10/10/2020' } as unknown as Cliente;

    service.delete(mockCliente).subscribe((res) => {
      expect(res).toBeNull();
    });

    const req = httpMock.expectOne(`${apiUrl}/1`);
    expect(req.request.method).toBe('DELETE');
    req.flush(null);
  });
});
