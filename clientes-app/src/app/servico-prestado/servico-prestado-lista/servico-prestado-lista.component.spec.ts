import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ClienteslistaComponent } from '../../clientes/clientes-lista/clientes-lista.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ClientesService } from '../../clientes/service/clientes.service';
import { of, throwError } from 'rxjs';
import { Cliente } from '../../clientes/cliente';

describe('ClienteslistaComponent', () => {
  let component: ClienteslistaComponent;
  let fixture: ComponentFixture<ClienteslistaComponent>;
  let clientesService: ClientesService;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ClienteslistaComponent ],
      imports: [ HttpClientTestingModule ],
      providers: [ ClientesService ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ClienteslistaComponent);
    component = fixture.componentInstance;
    clientesService = TestBed.inject(ClientesService);

    spyOn(clientesService, 'getClientes').and.returnValue(of([]));
    fixture.detectChanges();
  });

  it('deve criar o componente', () => {
    expect(component).toBeTruthy();
  });

  it('deve carregar a lista de clientes no ngOnInit', () => {
    const mockClientes = [
      { id: 1, nome: 'Carlos', cpf: '00011122233', dataCadastro: '12/12/2022' }
    ] as unknown as Cliente[];

    clientesService.getClientes = jasmine.createSpy().and.returnValue(of(mockClientes));

    component.carregarClientes();

    expect(component.clientes).toEqual(mockClientes);
  });

  it('deve preparar cliente para deleção', () => {
    const mockCliente = { id: 1, nome: 'Carlos', cpf: '00011122233', dataCadastro: '12/12/2022' } as unknown as Cliente;
    component.preparaDelecao(mockCliente);
    expect(component.clienteSelecionado).toEqual(mockCliente);
  });

  it('deve deletar cliente com sucesso e remover da lista', () => {
    const mockCliente = { id: 1, nome: 'Carlos', cpf: '00011122233', dataCadastro: '12/12/2022' } as unknown as Cliente;
    component.clientes = [mockCliente];
    component.clienteSelecionado = mockCliente;

    spyOn(clientesService, 'delete').and.returnValue(of(null));

    component.confirmarDelecao();

    expect(clientesService.delete).toHaveBeenCalledWith(mockCliente);
    expect(component.clientes.length).toBe(0);
    expect(component.clienteSelecionado).toBeNull();
    expect(component.success).toBeTrue();
  });

  it('deve tratar erro ao tentar deletar cliente', () => {
    component.clienteSelecionado = { id: 1, nome: 'Carlos', cpf: '00011122233', dataCadastro: '12/12/2022' } as unknown as Cliente;
    spyOn(clientesService, 'delete').and.returnValue(throwError(() => new Error('Erro')));

    component.confirmarDelecao();

    expect(component.success).toBeFalse();
    expect(component.errors).toContain('Erro ao deletar o cliente.');
  });
});
