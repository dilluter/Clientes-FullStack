import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  username = '';
  password = '';
  email = '';
  nomeCompleto = '';
  telefone = '';
  loginError = false;
  cadastrando = false;

  constructor(
    private router: Router,
    private authService: AuthService
  ) {}

  ngOnInit(): void {}

  onSubmit() {
    this.loginError = false;
    this.authService.login(this.username, this.password).subscribe({
      next: () => this.router.navigate(['/home']),
      error: () => this.loginError = true
    });
  }

  onCadastrar() {
    this.authService.cadastrar({
      username: this.username,
      password: this.password,
      email: this.email,
      nomeCompleto: this.nomeCompleto,
      telefone: this.telefone
    }).subscribe({
      next: () => {
        this.cadastrando = false;
        this.limparCampos();
        alert('Cadastro realizado! Faça o login.');
      },
      error: (err: any) => alert(err.error?.message || 'Erro ao cadastrar.')
    });
  }

  preparaCadastrar(event: Event) {
    event.preventDefault();
    this.cadastrando = true;
    this.loginError = false;
    this.limparCampos();
  }

  cancelarCadastrar() {
    this.cadastrando = false;
    this.limparCampos();
  }

  private limparCampos() {
    this.username = '';
    this.password = '';
    this.email = '';
    this.nomeCompleto = '';
    this.telefone = '';
  }
}
