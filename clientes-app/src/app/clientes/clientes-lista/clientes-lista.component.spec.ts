import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ClientesListaComponent } from './clientes-lista.component';
import { ClientesService } from '../service/clientes.service';
import { of, throwError } from 'rxjs';
import { HttpErrorResponse } from '@angular/common/http';

describe('ClientesListaComponent', () => {
  let component: ClientesListaComponent;
  let fixture: ComponentFixture<ClientesListaComponent>;
  let service: jasmine.SpyObj<ClientesService>;

  beforeEach(async () => {
    service = jasmine.createSpyObj<ClientesService>('ClientesService', [
      'getClientes',
      'delete'
    ]);

    await TestBed.configureTestingModule({
      declarations: [ClientesListaComponent],
      providers: [
        { provide: ClientesService, useValue: service }
      ]
    }).overrideComponent(ClientesListaComponent, {
      set: { template: '' }
    }).compileComponents();

    fixture = TestBed.createComponent(ClientesListaComponent);
    component = fixture.componentInstance;
  });

  it('deve criar o componente', () => {
    expect(component).toBeTruthy();
  });

  it('deve carregar clientes no ngOnInit', () => {
    service.getClientes.and.returnValue(of({
      content: [
        { id: 1, nome: 'Igor', cpf: '12345678901' }
      ],
      number: 0,
      totalPages: 1,
      totalElements: 1,
      size: 10,
      first: true,
      last: true,
      empty: false
    } as any));

    fixture.detectChanges();

    expect(service.getClientes).toHaveBeenCalledWith(0, 10);
    expect(component.clientes.length).toBe(1);
    expect(component.paginaAtual).toBe(0);
    expect(component.totalPaginas).toBe(1);
    expect(component.totalElementos).toBe(1);
  });

  it('deve exibir erro ao falhar ao carregar clientes', () => {
    const error = new HttpErrorResponse({
      error: { errors: ['Erro teste'] },
      status: 500
    });

    service.getClientes.and.returnValue(throwError(() => error));

    component.carregarClientes();

    expect(component.errors.length).toBeGreaterThan(0);
  });

  it('deve preparar cliente para deleção', () => {
    const cliente = { id: 1, nome: 'Igor', cpf: '12345678901' };

    component.preparaDelecao(cliente);

    expect(component.clienteSelecionado).toEqual(cliente);
  });

  it('não deve deletar quando não houver cliente selecionado com id', () => {
    component.clienteSelecionado = {
      nome: 'Sem ID',
      cpf: '12345678901'
    };

    component.confirmarDelecao();

    expect(service.delete).not.toHaveBeenCalled();
  });

  it('deve exibir erro ao falhar na deleção', () => {
    const error = new HttpErrorResponse({
      error: { errors: ['Não foi possível deletar.'] },
      status: 400
    });

    component.clienteSelecionado = {
      id: 1,
      nome: 'Igor',
      cpf: '12345678901'
    };

    service.delete.and.returnValue(throwError(() => error));

    component.confirmarDelecao();

    expect(component.success).toBeFalse();
    expect(component.errors.length).toBeGreaterThan(0);
  });

  it('deve ir para página anterior quando possível', () => {
    spyOn(component, 'carregarClientes');

    component.paginaAtual = 2;

    component.paginaAnterior();

    expect(component.carregarClientes).toHaveBeenCalledWith(1);
  });

  it('não deve ir para página anterior quando estiver na primeira página', () => {
    spyOn(component, 'carregarClientes');

    component.paginaAtual = 0;

    component.paginaAnterior();

    expect(component.carregarClientes).not.toHaveBeenCalled();
  });

  it('deve ir para próxima página quando possível', () => {
    spyOn(component, 'carregarClientes');

    component.paginaAtual = 0;
    component.totalPaginas = 2;

    component.proximaPagina();

    expect(component.carregarClientes).toHaveBeenCalledWith(1);
  });

  it('não deve ir para próxima página quando estiver na última página', () => {
    spyOn(component, 'carregarClientes');

    component.paginaAtual = 1;
    component.totalPaginas = 2;

    component.proximaPagina();

    expect(component.carregarClientes).not.toHaveBeenCalled();
  });

  it('deve retornar true quando possuir clientes', () => {
    component.clientes = [
      { id: 1, nome: 'Igor', cpf: '12345678901' }
    ];

    expect(component.possuiClientes).toBeTrue();
  });
});
