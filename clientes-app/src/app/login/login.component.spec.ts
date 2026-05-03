import { ComponentFixture, TestBed } from '@angular/core/testing';
import { LoginComponent } from './login.component';
import { AuthService } from '../services/auth.service';
import { Router } from '@angular/router';
import { of, throwError } from 'rxjs';
import { HttpErrorResponse } from '@angular/common/http';

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let authService: jasmine.SpyObj<AuthService>;
  let router: jasmine.SpyObj<Router>;

  beforeEach(async () => {
    authService = jasmine.createSpyObj<AuthService>('AuthService', [
      'login',
      'cadastrar'
    ]);

    router = jasmine.createSpyObj<Router>('Router', ['navigate']);

    await TestBed.configureTestingModule({
      declarations: [LoginComponent],
      providers: [
        { provide: AuthService, useValue: authService },
        { provide: Router, useValue: router }
      ]
    }).overrideComponent(LoginComponent, {
      set: { template: '' }
    }).compileComponents();

    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
  });

  it('deve criar o componente', () => {
    expect(component).toBeTruthy();
  });

  it('deve fazer login e redirecionar para home', () => {
    component.username = 'igor';
    component.password = '123';

    authService.login.and.returnValue(of({
      access_token: 'token'
    }));

    component.onSubmit();

    expect(authService.login).toHaveBeenCalledWith('igor', '123');
    expect(router.navigate).toHaveBeenCalledWith(['/home']);
  });

  it('deve marcar erro quando login falhar', () => {
    authService.login.and.returnValue(throwError(() => new Error('Erro')));

    component.onSubmit();

    expect(component.loginError).toBeTrue();
  });

  it('deve formatar telefone ao alterar campo', () => {
    const input = document.createElement('input');
    input.value = '61999998888';

    component.onTelefoneChange({ target: input } as unknown as Event);

    expect(input.value).toBe('(61) 99999-8888');
    expect(component.telefone).toBe('(61) 99999-8888');
  });

  it('deve identificar emails diferentes', () => {
    component.cadastrando = true;
    component.email = 'a@email.com';
    component.confirmarEmail = 'b@email.com';

    expect(component.emailsDiferentes()).toBeTrue();
  });

  it('deve identificar senhas diferentes', () => {
    component.cadastrando = true;
    component.password = '123';
    component.confirmarPassword = '456';

    expect(component.senhasDiferentes()).toBeTrue();
  });

  it('deve considerar formulário de cadastro inválido quando emails ou senhas forem diferentes', () => {
    component.cadastrando = true;
    component.email = 'a@email.com';
    component.confirmarEmail = 'b@email.com';

    expect(component.formularioCadastroInvalido()).toBeTrue();
  });

  it('deve bloquear cadastro quando emails forem diferentes', () => {
    component.cadastrando = true;
    component.email = 'a@email.com';
    component.confirmarEmail = 'b@email.com';

    component.onCadastrar();

    expect(component.errors).toEqual(['Os emails informados não conferem.']);
    expect(authService.cadastrar).not.toHaveBeenCalled();
  });

  it('deve bloquear cadastro quando senhas forem diferentes', () => {
    component.cadastrando = true;
    component.email = 'a@email.com';
    component.confirmarEmail = 'a@email.com';
    component.password = '123';
    component.confirmarPassword = '456';

    component.onCadastrar();

    expect(component.errors).toEqual(['As senhas informadas não conferem.']);
    expect(authService.cadastrar).not.toHaveBeenCalled();
  });

  it('deve cadastrar usuário com sucesso', () => {
    component.username = 'igor';
    component.password = '123';
    component.confirmarPassword = '123';
    component.email = 'igor@email.com';
    component.confirmarEmail = 'igor@email.com';
    component.nomeCompleto = 'Igor Bruno';
    component.telefone = '(61) 99999-8888';

    authService.cadastrar.and.returnValue(of(void 0));

    component.onCadastrar();

    expect(authService.cadastrar).toHaveBeenCalledWith({
      username: 'igor',
      password: '123',
      confirmarPassword: '123',
      email: 'igor@email.com',
      confirmarEmail: 'igor@email.com',
      nomeCompleto: 'Igor Bruno',
      telefone: '61999998888'
    });

    expect(component.cadastroSuccess).toBeTrue();
    expect(component.cadastrando).toBeFalse();
    expect(component.username).toBe('');
    expect(component.password).toBe('');
  });

  it('deve exibir erros quando cadastro falhar', () => {
    const error = new HttpErrorResponse({
      error: { errors: ['Usuário já existe.'] },
      status: 400
    });

    component.email = 'igor@email.com';
    component.confirmarEmail = 'igor@email.com';
    component.password = '123';
    component.confirmarPassword = '123';

    authService.cadastrar.and.returnValue(throwError(() => error));

    component.onCadastrar();

    expect(component.errors.length).toBeGreaterThan(0);
  });

  it('deve preparar tela de cadastro', () => {
    const event = jasmine.createSpyObj<Event>('Event', ['preventDefault']);

    component.username = 'igor';
    component.loginError = true;

    component.preparaCadastrar(event);

    expect(event.preventDefault).toHaveBeenCalled();
    expect(component.cadastrando).toBeTrue();
    expect(component.loginError).toBeFalse();
    expect(component.username).toBe('');
  });

  it('deve cancelar cadastro', () => {
    component.cadastrando = true;
    component.errors = ['Erro'];
    component.username = 'igor';

    component.cancelarCadastrar();

    expect(component.cadastrando).toBeFalse();
    expect(component.errors).toEqual([]);
    expect(component.username).toBe('');
  });
});
