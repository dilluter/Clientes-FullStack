import { Component } from '@angular/core';
import { Router } from '@angular/router';
import {AuthService, UsuarioCadastroRequest} from '../services/auth.service';
import { HttpErrorResponse } from '@angular/common/http';
import { ErrorUtil } from '../utils/error.util';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {

  username = '';
  password = '';
  email = '';
  nomeCompleto = '';
  telefone = '';

  loginError = false;
  cadastrando = false;
  cadastroSuccess = false;
  errors: string[] = [];

  constructor(
    private readonly router: Router,
    private readonly authService: AuthService
  ) {}

  onSubmit(): void {
    this.loginError = false;
    this.errors = [];

    this.authService.login(this.username, this.password).subscribe({
      next: () => {
        this.router.navigate(['/home']);
      },
      error: () => {
        this.loginError = true;
      }
    });
  }

  onCadastrar(): void {
    this.errors = [];
    this.cadastroSuccess = false;

    const request: UsuarioCadastroRequest = {
      username: this.username,
      password: this.password,
      email: this.email,
      nomeCompleto: this.nomeCompleto,
      telefone: this.telefone || undefined
    };

    this.authService.cadastrar(request).subscribe({
      next: () => {
        this.cadastroSuccess = true;
        this.cadastrando = false;
        this.limparCampos();
      },
      error: (errorResponse: HttpErrorResponse) => {
        this.errors = ErrorUtil.getErrors(errorResponse, 'Erro ao cadastrar usuário.');
      }
    });
  }

  preparaCadastrar(event: Event): void {
    event.preventDefault();
    this.cadastrando = true;
    this.loginError = false;
    this.cadastroSuccess = false;
    this.errors = [];
    this.limparCampos();
  }

  cancelarCadastrar(): void {
    this.cadastrando = false;
    this.errors = [];
    this.limparCampos();
  }

  private limparCampos(): void {
    this.username = '';
    this.password = '';
    this.email = '';
    this.nomeCompleto = '';
    this.telefone = '';
  }
}
