import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { ClientesFormComponent } from './clientes-form.component';
import { ClientesService } from '../service/clientes.service';
import { ActivatedRoute, Router } from '@angular/router';
import { of, throwError } from 'rxjs';
import { HttpErrorResponse } from '@angular/common/http';

describe('ClientesFormComponent', () => {
  let component: ClientesFormComponent;
  let fixture: ComponentFixture<ClientesFormComponent>;
  let service: jasmine.SpyObj<ClientesService>;
  let router: jasmine.SpyObj<Router>;

  function configurarTeste(idParam: string | null = null): void {
    service = jasmine.createSpyObj<ClientesService>('ClientesService', [
      'getClientesById',
      'salvar',
      'atualizar'
    ]);

    router = jasmine.createSpyObj<Router>('Router', ['navigate']);

    TestBed.configureTestingModule({
      declarations: [ClientesFormComponent],
      providers: [
        { provide: ClientesService, useValue: service },
        { provide: Router, useValue: router },
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: {
                get: () => idParam
              }
            }
          }
        }
      ]
    }).overrideComponent(ClientesFormComponent, {
      set: { template: '' }
    });

    fixture = TestBed.createComponent(ClientesFormComponent);
    component = fixture.componentInstance;
  }

  it('deve criar o componente', () => {
    configurarTeste();

    expect(component).toBeTruthy();
  });

  it('deve iniciar com cliente vazio quando não houver id na rota', () => {
    configurarTeste();

    fixture.detectChanges();

    expect(component.cliente).toEqual({
      nome: '',
      cpf: ''
    });
    expect(service.getClientesById).not.toHaveBeenCalled();
  });

  it('deve carregar cliente quando houver id válido na rota', () => {
    configurarTeste('1');

    const cliente = {
      id: 1,
      nome: 'Igor',
      cpf: '12345678901'
    };

    service.getClientesById.and.returnValue(of(cliente));

    fixture.detectChanges();

    expect(component.id).toBe(1);
    expect(service.getClientesById).toHaveBeenCalledWith(1);
    expect(component.cliente).toEqual(cliente);
  });

  it('deve exibir erro quando id da rota for inválido', () => {
    configurarTeste('abc');

    fixture.detectChanges();

    expect(component.errors).toEqual(['ID do cliente inválido.']);
    expect(service.getClientesById).not.toHaveBeenCalled();
  });

  it('deve exibir erro quando falhar ao carregar cliente', () => {
    configurarTeste('1');

    const error = new HttpErrorResponse({
      error: { errors: ['Cliente não encontrado.'] },
      status: 404
    });

    service.getClientesById.and.returnValue(throwError(() => error));

    fixture.detectChanges();

    expect(component.cliente).toEqual({
      nome: '',
      cpf: ''
    });
    expect(component.errors.length).toBeGreaterThan(0);
  });

  it('deve salvar cliente novo removendo máscara do CPF', fakeAsync(() => {
    configurarTeste();

    const clienteSalvo = {
      id: 1,
      nome: 'Igor',
      cpf: '12345678901'
    };

    component.cliente = {
      nome: 'Igor',
      cpf: '123.456.789-01'
    };

    service.salvar.and.returnValue(of(clienteSalvo));

    component.onSubmit();

    expect(service.salvar).toHaveBeenCalledWith({
      nome: 'Igor',
      cpf: '12345678901'
    });

    expect(component.success).toBeTrue();

    tick(800);

    expect(router.navigate).toHaveBeenCalledWith(['/clientes/lista']);
  }));

  it('deve atualizar cliente existente removendo máscara do CPF', fakeAsync(() => {
    configurarTeste();

    const clienteAtualizado = {
      id: 1,
      nome: 'Igor Atualizado',
      cpf: '12345678901'
    };

    component.id = 1;
    component.cliente = {
      id: 1,
      nome: 'Igor Atualizado',
      cpf: '123.456.789-01'
    };

    service.atualizar.and.returnValue(of(clienteAtualizado));

    component.onSubmit();

    expect(service.atualizar).toHaveBeenCalledWith({
      id: 1,
      nome: 'Igor Atualizado',
      cpf: '12345678901'
    });

    expect(component.success).toBeTrue();

    tick(800);

    expect(router.navigate).toHaveBeenCalledWith(['/clientes/lista']);
  }));

  it('deve exibir erro ao falhar no salvar', () => {
    configurarTeste();

    const error = new HttpErrorResponse({
      error: { errors: ['CPF inválido.'] },
      status: 400
    });

    component.cliente = {
      nome: 'Igor',
      cpf: '123.456.789-01'
    };

    service.salvar.and.returnValue(throwError(() => error));

    component.onSubmit();

    expect(component.success).toBeFalse();
    expect(component.errors.length).toBeGreaterThan(0);
  });

  it('deve exibir erro ao falhar no atualizar', () => {
    configurarTeste();

    const error = new HttpErrorResponse({
      error: { errors: ['Cliente inválido.'] },
      status: 400
    });

    component.id = 1;
    component.cliente = {
      id: 1,
      nome: 'Igor',
      cpf: '123.456.789-01'
    };

    service.atualizar.and.returnValue(throwError(() => error));

    component.onSubmit();

    expect(component.success).toBeFalse();
    expect(component.errors.length).toBeGreaterThan(0);
  });

  it('deve formatar CPF ao alterar campo', () => {
    configurarTeste();

    const input = document.createElement('input');
    input.value = '12345678901';

    component.onCpfChange({ target: input } as unknown as Event);

    expect(input.value).toBe('123.456.789-01');
    expect(component.cliente.cpf).toBe('123.456.789-01');
  });

  it('deve voltar para lista de clientes', () => {
    configurarTeste();

    component.voltar();

    expect(router.navigate).toHaveBeenCalledWith(['/clientes/lista']);
  });
});
