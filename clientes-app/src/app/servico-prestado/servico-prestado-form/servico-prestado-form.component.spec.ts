import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { ServicoPrestadoFormComponent } from './servico-prestado-form.component';
import { ClientesService } from '../../clientes/service/clientes.service';
import { ServicoPrestadoService } from '../service/servico-prestado.service';
import { Router } from '@angular/router';
import { of, throwError } from 'rxjs';
import { HttpErrorResponse } from '@angular/common/http';

describe('ServicoPrestadoFormComponent', () => {
  let component: ServicoPrestadoFormComponent;
  let fixture: ComponentFixture<ServicoPrestadoFormComponent>;
  let clientesService: jasmine.SpyObj<ClientesService>;
  let servicoPrestadoService: jasmine.SpyObj<ServicoPrestadoService>;
  let router: jasmine.SpyObj<Router>;

  beforeEach(async () => {
    clientesService = jasmine.createSpyObj<ClientesService>('ClientesService', [
      'getClientesParaSelect'
    ]);

    servicoPrestadoService = jasmine.createSpyObj<ServicoPrestadoService>('ServicoPrestadoService', [
      'salvar'
    ]);

    router = jasmine.createSpyObj<Router>('Router', ['navigate']);

    await TestBed.configureTestingModule({
      declarations: [ServicoPrestadoFormComponent],
      providers: [
        { provide: ClientesService, useValue: clientesService },
        { provide: ServicoPrestadoService, useValue: servicoPrestadoService },
        { provide: Router, useValue: router }
      ]
    }).overrideComponent(ServicoPrestadoFormComponent, {
      set: { template: '' }
    }).compileComponents();

    fixture = TestBed.createComponent(ServicoPrestadoFormComponent);
    component = fixture.componentInstance;
  });

  it('deve criar o componente', () => {
    clientesService.getClientesParaSelect.and.returnValue(of({
      content: [],
      number: 0,
      totalPages: 0,
      totalElements: 0
    } as any));

    fixture.detectChanges();

    expect(component).toBeTruthy();
  });

  it('deve carregar clientes no ngOnInit', () => {
    clientesService.getClientesParaSelect.and.returnValue(of({
      content: [
        { id: 1, nome: 'Igor', cpf: '12345678901' }
      ],
      number: 0,
      totalPages: 1,
      totalElements: 1
    } as any));

    fixture.detectChanges();

    expect(clientesService.getClientesParaSelect).toHaveBeenCalled();
    expect(component.clientes.length).toBe(1);
  });

  it('deve exibir erro ao falhar ao carregar clientes', () => {
    const error = new HttpErrorResponse({
      error: { errors: ['Erro ao carregar'] },
      status: 500
    });

    clientesService.getClientesParaSelect.and.returnValue(throwError(() => error));

    fixture.detectChanges();

    expect(component.errors.length).toBeGreaterThan(0);
  });

  it('deve bloquear submit com data inválida', () => {
    component.dataFormatada = '99/99/9999';

    component.onSubmit();

    expect(component.errors).toEqual(['Data inválida. Use o formato dd/MM/yyyy.']);
    expect(servicoPrestadoService.salvar).not.toHaveBeenCalled();
  });

  it('deve bloquear submit com data anterior à atual', () => {
    component.dataFormatada = '01/01/2000';

    component.onSubmit();

    expect(component.errors).toEqual(['A data não pode ser anterior à data atual.']);
    expect(servicoPrestadoService.salvar).not.toHaveBeenCalled();
  });

  it('deve salvar serviço prestado com data válida', fakeAsync(() => {
    const amanha = new Date();
    amanha.setDate(amanha.getDate() + 1);

    const dia = String(amanha.getDate()).padStart(2, '0');
    const mes = String(amanha.getMonth() + 1).padStart(2, '0');
    const ano = amanha.getFullYear();

    component.dataFormatada = `${dia}/${mes}/${ano}`;

    component.servicoPrest = {
      descricao: 'Serviço teste',
      data: '',
      idCliente: 1,
      valor: 100
    };

    servicoPrestadoService.salvar.and.returnValue(of({
      id: 1,
      descricao: 'Serviço teste',
      data: `${ano}-${mes}-${dia}`,
      nomeCliente: 'Igor',
      valor: 100
    } as any));

    component.onSubmit();

    expect(servicoPrestadoService.salvar).toHaveBeenCalled();
    expect(component.success).toBeTrue();
    expect(component.errors).toEqual([]);
    expect(component.servicoPrest).toEqual({
      descricao: '',
      data: '',
      idCliente: undefined,
      valor: undefined
    });
    expect(component.dataFormatada).toBe('');

    tick(800);

    expect(router.navigate).toHaveBeenCalledWith(['/servico-prestado/lista']);
  }));

  it('deve exibir erro ao falhar ao salvar serviço prestado', () => {
    const amanha = new Date();
    amanha.setDate(amanha.getDate() + 1);

    const dia = String(amanha.getDate()).padStart(2, '0');
    const mes = String(amanha.getMonth() + 1).padStart(2, '0');
    const ano = amanha.getFullYear();

    const error = new HttpErrorResponse({
      error: { errors: ['Erro teste'] },
      status: 400
    });

    component.dataFormatada = `${dia}/${mes}/${ano}`;
    component.servicoPrest = {
      descricao: 'Serviço teste',
      data: '',
      idCliente: 1,
      valor: 100
    };

    servicoPrestadoService.salvar.and.returnValue(throwError(() => error));

    component.onSubmit();

    expect(component.success).toBeFalse();
    expect(component.errors.length).toBeGreaterThan(0);
  });
});
