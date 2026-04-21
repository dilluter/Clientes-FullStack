import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ClienteslistaComponent } from './clientes-lista.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { ClientesService } from '../service/clientes.service';
import { of, throwError } from 'rxjs';
import { Cliente } from '../cliente';

describe('ClienteslistaComponent', () => {
  let component: ClienteslistaComponent;
  let fixture: ComponentFixture<ClienteslistaComponent>;
  let clientesService: ClientesService;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ClienteslistaComponent ],
      imports: [
        HttpClientTestingModule,
        RouterTestingModule
      ],
      providers: [
        ClientesService
      ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ClienteslistaComponent);
    component = fixture.componentInstance;
    clientesService = TestBed.inject(ClientesService);
  });

  it('deve criar o componente', () => {
    expect(component).toBeTruthy();
  });

  it('deve carregar clientes no ngOnInit', () => {
    const mockClientes = [
      { id: 1, nome: 'Maria', cpf: '11122233344', dataCadastro: '01/01/2021' } as unknown as Cliente,
      { id: 2, nome: 'João', cpf: '55566677788', dataCadastro: '02/01/2021' } as unknown as Cliente
    ];
    spyOn(clientesService, 'getClientes').and.returnValue(of(mockClientes));

    fixture.detectChanges();

    expect(clientesService.getClientes).toHaveBeenCalled();
    expect(component.clientes.length).toBe(2);
    expect(component.clientes).toEqual(mockClientes);
  });

  it('deve preparar deleção setando o cliente selecionado', () => {
    const mockCliente = { id: 1, nome: 'Maria', cpf: '11122233344', dataCadastro: '01/01/2021' } as unknown as Cliente;

    component.preparaDelecao(mockCliente);

    expect(component.clienteSelecionado).toEqual(mockCliente);
  });

  it('deve confirmar deleção com sucesso', () => {
    const mockCliente = { id: 1, nome: 'Maria', cpf: '11122233344', dataCadastro: '01/01/2021' } as unknown as Cliente;
    component.clientes = [mockCliente];
    component.clienteSelecionado = mockCliente;

    spyOn(clientesService, 'delete').and.returnValue(of({}));

    component.confirmarDelecao();

    expect(clientesService.delete).toHaveBeenCalledWith(mockCliente);
    expect(component.clientes.length).toBe(0);
    expect(component.clienteSelecionado).toBeNull();
    expect(component.success).toBeTrue();
    expect(component.errors.length).toBe(0);
  });

  it('deve exibir erro ao falhar na deleção', () => {
    const mockCliente = { id: 1, nome: 'Maria', cpf: '11122233344', dataCadastro: '01/01/2021' } as unknown as Cliente;
    component.clienteSelecionado = mockCliente;

    spyOn(clientesService, 'delete').and.returnValue(throwError(() => new Error('Erro')));

    component.confirmarDelecao();

    expect(component.success).toBeFalse();
    expect(component.errors).toContain('Erro ao deletar o cliente.');
  });

  it('nao deve fazer nada se confirmar delecao sem cliente selecionado', () => {
    component.clienteSelecionado = null;
    spyOn(clientesService, 'delete');

    component.confirmarDelecao();

    expect(clientesService.delete).not.toHaveBeenCalled();
  });
});
