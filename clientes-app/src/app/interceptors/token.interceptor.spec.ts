import { TestBed } from '@angular/core/testing';
import {
  HttpClientTestingModule,
  HttpTestingController
} from '@angular/common/http/testing';
import { HTTP_INTERCEPTORS, HttpClient } from '@angular/common/http';
import { TokenInterceptor } from './token.interceptor';
import { AuthService } from '../services/auth.service';

describe('TokenInterceptor', () => {
  let http: HttpClient;
  let httpMock: HttpTestingController;
  let authService: jasmine.SpyObj<AuthService>;

  beforeEach(() => {
    authService = jasmine.createSpyObj<AuthService>('AuthService', ['getToken']);

    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [
        { provide: AuthService, useValue: authService },
        {
          provide: HTTP_INTERCEPTORS,
          useClass: TokenInterceptor,
          multi: true
        }
      ]
    });

    http = TestBed.inject(HttpClient);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('deve adicionar Authorization quando existir token', () => {
    authService.getToken.and.returnValue('abc123');

    http.get('/teste').subscribe();

    const req = httpMock.expectOne('/teste');

    expect(req.request.headers.get('Authorization')).toBe('Bearer abc123');

    req.flush({});
  });

  it('não deve adicionar Authorization quando não existir token', () => {
    authService.getToken.and.returnValue(null);

    http.get('/teste').subscribe();

    const req = httpMock.expectOne('/teste');

    expect(req.request.headers.has('Authorization')).toBeFalse();

    req.flush({});
  });
});
