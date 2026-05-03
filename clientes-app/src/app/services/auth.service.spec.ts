import { TestBed } from '@angular/core/testing';
import { AuthService, UsuarioCadastroRequest } from './auth.service';
import {
  HttpClientTestingModule,
  HttpTestingController
} from '@angular/common/http/testing';
import { environment } from '../../environments/environment';

describe('AuthService', () => {
  let service: AuthService;
  let httpMock: HttpTestingController;

  const apiUrl = environment.authUrlBase;

  beforeEach(() => {
    localStorage.clear();

    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [AuthService]
    });

    service = TestBed.inject(AuthService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
    localStorage.clear();
  });

  it('deve criar o service', () => {
    expect(service).toBeTruthy();
  });

  it('deve fazer login e salvar access_token no localStorage', () => {
    service.login('igor', '123').subscribe(response => {
      expect(response.access_token).toBe('abc123');
    });

    const req = httpMock.expectOne(`${apiUrl}/auth/login`);

    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual({
      username: 'igor',
      password: '123'
    });

    req.flush({
      access_token: 'abc123'
    });

    expect(localStorage.getItem('token')).toBe('abc123');
  });

  it('deve fazer login e salvar accessToken no localStorage', () => {
    service.login('igor', '123').subscribe(response => {
      expect(response.accessToken).toBe('abc123');
    });

    const req = httpMock.expectOne(`${apiUrl}/auth/login`);

    expect(req.request.method).toBe('POST');

    req.flush({
      accessToken: 'abc123'
    });

    expect(localStorage.getItem('token')).toBe('abc123');
  });

  it('não deve salvar token quando resposta não possuir token', () => {
    service.login('igor', '123').subscribe();

    const req = httpMock.expectOne(`${apiUrl}/auth/login`);

    req.flush({});

    expect(localStorage.getItem('token')).toBeNull();
  });

  it('deve cadastrar usuário via POST', () => {
    const request: UsuarioCadastroRequest = {
      username: 'igor',
      password: '123',
      confirmarPassword: '123',
      email: 'igor@email.com',
      confirmarEmail: 'igor@email.com',
      nomeCompleto: 'Igor Bruno',
      telefone: '61999998888'
    };

    service.cadastrar(request).subscribe(response => {
      expect(response).toBeNull();
    });

    const req = httpMock.expectOne(`${apiUrl}/usuarios`);

    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(request);

    req.flush(null);
  });

  it('deve remover token no logout', () => {
    localStorage.setItem('token', 'abc123');

    service.logout();

    expect(localStorage.getItem('token')).toBeNull();
  });

  it('deve retornar token salvo', () => {
    localStorage.setItem('token', 'abc123');

    expect(service.getToken()).toBe('abc123');
  });

  it('deve retornar true quando usuário estiver logado', () => {
    localStorage.setItem('token', 'abc123');

    expect(service.isLoggedIn()).toBeTrue();
  });

  it('deve retornar false quando usuário não estiver logado', () => {
    expect(service.isLoggedIn()).toBeFalse();
  });
});
