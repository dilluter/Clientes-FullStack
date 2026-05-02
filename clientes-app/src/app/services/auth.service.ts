import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import {tap} from 'rxjs/operators';

interface LoginResponse {
  access_token?: string;
  accessToken?: string;
  tokenType?: string;
}

export interface UsuarioCadastroRequest {
  username: string;
  password: string;
  email: string;
  nomeCompleto: string;
  telefone?: string;
}

@Injectable({ providedIn: 'root' })
export class AuthService {

  private readonly apiUrl = environment.authUrlBase;
  private readonly tokenKey = 'token';

  constructor(private readonly http: HttpClient) {}

  login(username: string, password: string): Observable<LoginResponse> {
    return this.http
      .post<LoginResponse>(`${this.apiUrl}/auth/login`, { username, password })
      .pipe(
        tap(response => {
          const token = response.access_token ?? response.accessToken;

          if (token) {
            localStorage.setItem(this.tokenKey, token);
          }
        })
      );
  }

  cadastrar(dados: UsuarioCadastroRequest): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/usuarios`, dados);
  }

  logout(): void {
    localStorage.removeItem(this.tokenKey);
  }

  getToken(): string | null {
    return localStorage.getItem(this.tokenKey);
  }

  isLoggedIn(): boolean {
    return Boolean(this.getToken());
  }
}
