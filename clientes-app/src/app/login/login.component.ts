import { Component } from '@angular/core';
import { Router } from '@angular/router';
import {AuthService, UsuarioCadastroRequest} from '../services/auth.service';
import { HttpErrorResponse } from '@angular/common/http';
import { ErrorUtil } from '../utils/error.util';
import {MaskUtil} from '../utils/mask.util';

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
  confirmarEmail = '';
  confirmarPassword = '';

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

  onTelefoneChange(event: Event): void {
    const input = event.target as HTMLInputElement;

    const telefoneFormatado = MaskUtil.phone(input.value);

    input.value = telefoneFormatado;
    this.telefone = telefoneFormatado;
  }

  emailsDiferentes(): boolean {
    return this.cadastrando &&
      this.email.length > 0 &&
      this.confirmarEmail.length > 0 &&
      this.email !== this.confirmarEmail;
  }

  senhasDiferentes(): boolean {
    return this.cadastrando &&
      this.password.length > 0 &&
      this.confirmarPassword.length > 0 &&
      this.password !== this.confirmarPassword;
  }

  formularioCadastroInvalido(): boolean {
    return this.emailsDiferentes() || this.senhasDiferentes();
  }

  onCadastrar(): void {
    this.errors = [];
    this.cadastroSuccess = false;

    if (this.emailsDiferentes()) {
      this.errors = ['Os emails informados não conferem.'];
      return;
    }

    if (this.senhasDiferentes()) {
      this.errors = ['As senhas informadas não conferem.'];
      return;
    }

    const request: UsuarioCadastroRequest = {
      username: this.username,
      password: this.password,
      confirmarPassword: this.confirmarPassword,
      email: this.email,
      confirmarEmail: this.confirmarEmail,
      nomeCompleto: this.nomeCompleto,
      telefone: MaskUtil.onlyNumbers(this.telefone) || undefined
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
    this.confirmarPassword = '';
    this.email = '';
    this.confirmarEmail = '';
    this.nomeCompleto = '';
    this.telefone = '';
  }
}
