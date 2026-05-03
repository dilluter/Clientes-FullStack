import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ServicoPrestadoListaComponent } from './servico-prestado-lista.component';
import { ServicoPrestadoService } from '../service/servico-prestado.service';
import { of, throwError } from 'rxjs';
import { HttpErrorResponse } from '@angular/common/http';

describe('ServicoPrestadoListaComponent', () => {
  let component: ServicoPrestadoListaComponent;
  let fixture: ComponentFixture<ServicoPrestadoListaComponent>;
  let service: jasmine.SpyObj<ServicoPrestadoService>;

  beforeEach(async () => {
    service = jasmine.createSpyObj<ServicoPrestadoService>('ServicoPrestadoService', [
      'buscar'
    ]);

    await TestBed.configureTestingModule({
      declarations: [ServicoPrestadoListaComponent],
      providers: [
        { provide: ServicoPrestadoService, useValue: service }
      ]
    }).overrideComponent(ServicoPrestadoListaComponent, {
      set: { template: '' }
    }).compileComponents();

    fixture = TestBed.createComponent(ServicoPrestadoListaComponent);
    component = fixture.componentInstance;
  });

  it('deve criar o componente', () => {
    expect(component).toBeTruthy();
  });

  it('deve consultar serviços prestados', () => {
    service.buscar.and.returnValue(of({
      content: [
        {
          id: 1,
          descricao: 'Serviço teste',
          nomeCliente: 'Igor',
          data: '2030-01-01',
          valor: 100
        }
      ],
      number: 0,
      totalPages: 1,
      totalElements: 1
    } as any));

    component.nome = 'Igor';
    component.mes = 1;

    component.consultar();

    expect(service.buscar).toHaveBeenCalledWith('Igor', 1, 0, 10);
    expect(component.lista.length).toBe(1);
    expect(component.paginaAtual).toBe(0);
    expect(component.totalPaginas).toBe(1);
    expect(component.totalElementos).toBe(1);
    expect(component.message).toBe('');
  });

  it('deve exibir mensagem quando consulta não retornar resultados', () => {
    service.buscar.and.returnValue(of({
      content: [],
      number: 0,
      totalPages: 0,
      totalElements: 0
    } as any));

    component.consultar();

    expect(component.lista).toEqual([]);
    expect(component.message).toBe('Nenhum resultado encontrado.');
  });

  it('deve exibir erro ao falhar na consulta', () => {
    const error = new HttpErrorResponse({
      error: { errors: ['Erro ao consultar'] },
      status: 500
    });

    service.buscar.and.returnValue(throwError(() => error));

    component.consultar();

    expect(component.errors.length).toBeGreaterThan(0);
  });

  it('deve limpar filtros', () => {
    component.nome = 'Igor';
    component.mes = 1;
    component.lista = [
      {
        id: 1,
        descricao: 'Serviço',
        nomeCliente: 'Igor',
        data: '2030-01-01',
        valor: 100
      } as any
    ];
    component.message = 'Mensagem';
    component.errors = ['Erro'];
    component.paginaAtual = 2;
    component.totalPaginas = 3;
    component.totalElementos = 30;

    component.limparFiltros();

    expect(component.nome).toBe('');
    expect(component.mes).toBeUndefined();
    expect(component.lista).toEqual([]);
    expect(component.message).toBe('');
    expect(component.errors).toEqual([]);
    expect(component.paginaAtual).toBe(0);
    expect(component.totalPaginas).toBe(0);
    expect(component.totalElementos).toBe(0);
  });

  it('deve ir para página anterior quando possível', () => {
    spyOn(component, 'consultar');

    component.paginaAtual = 2;

    component.paginaAnterior();

    expect(component.consultar).toHaveBeenCalledWith(1);
  });

  it('não deve ir para página anterior quando estiver na primeira página', () => {
    spyOn(component, 'consultar');

    component.paginaAtual = 0;

    component.paginaAnterior();

    expect(component.consultar).not.toHaveBeenCalled();
  });

  it('deve ir para próxima página quando possível', () => {
    spyOn(component, 'consultar');

    component.paginaAtual = 0;
    component.totalPaginas = 2;

    component.proximaPagina();

    expect(component.consultar).toHaveBeenCalledWith(1);
  });

  it('não deve ir para próxima página quando estiver na última página', () => {
    spyOn(component, 'consultar');

    component.paginaAtual = 1;
    component.totalPaginas = 2;

    component.proximaPagina();

    expect(component.consultar).not.toHaveBeenCalled();
  });

  it('deve retornar true quando possuir resultados', () => {
    component.lista = [
      {
        id: 1,
        descricao: 'Serviço',
        nomeCliente: 'Igor',
        data: '2030-01-01',
        valor: 100
      } as any
    ];

    expect(component.possuiResultados).toBeTrue();
  });
});
