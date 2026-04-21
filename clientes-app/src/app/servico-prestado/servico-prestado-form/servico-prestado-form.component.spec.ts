import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ServicoPrestadoFormComponent } from './servico-prestado-form.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormsModule } from '@angular/forms';
import { ClientesService } from '../../clientes/service/clientes.service';
import { ServicoPrestadoService } from '../service/servico-prestado.service';
import { of, throwError } from 'rxjs';
import { Cliente } from '../../clientes/cliente';
import { ServicoPrestado } from '../servicoPrestado';
import { DateUtil } from 'src/app/utils/date.util';
import { HttpErrorResponse } from '@angular/common/http';

describe('ServicoPrestadoFormComponent', () => {
  let component: ServicoPrestadoFormComponent;
  let fixture: ComponentFixture<ServicoPrestadoFormComponent>;
  let clientesService: ClientesService;
  let servicoPrestadoService: ServicoPrestadoService;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ServicoPrestadoFormComponent],
      imports: [
        HttpClientTestingModule,
        FormsModule
      ],
      providers: [
        ClientesService,
        ServicoPrestadoService
      ]
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ServicoPrestadoFormComponent);
    component = fixture.componentInstance;
    clientesService = TestBed.inject(ClientesService);
    servicoPrestadoService = TestBed.inject(ServicoPrestadoService);
  });

  it('deve criar o componente', () => {
    expect(component).toBeTruthy();
  });

  it('deve carregar clientes no ngOnInit', () => {
    const mockClientes = [{ id: 1, nome: 'João', cpf: '12345678900' }] as unknown as Cliente[];
    spyOn(clientesService, 'getClientes').and.returnValue(of(mockClientes));

    fixture.detectChanges();

    expect(clientesService.getClientes).toHaveBeenCalled();
    expect(component.clientes).toEqual(mockClientes);
  });

  it('deve salvar servico prestado com sucesso', () => {
    const servicoMock = new ServicoPrestado();
    component.servicoPrest = servicoMock;

    spyOn(servicoPrestadoService, 'salvar').and.returnValue(of(servicoMock));

    component.onSubmit();

    expect(servicoPrestadoService.salvar).toHaveBeenCalledWith(servicoMock);
    expect(component.success).toBeTrue();
    expect(component.errors.length).toBe(0);
    expect(component.dataFormatada).toBe('');
    expect(component.servicoPrest).toBeDefined();
  });

  it('deve formatar data corretamente no onDataChange', () => {
    const mockEvent = { target: { value: '10102023' } };

    spyOn(DateUtil, 'format').and.returnValue('10/10/2023');
    spyOn(DateUtil, 'toBackend').and.returnValue('10/10/2023');

    component.onDataChange(mockEvent);

    expect(DateUtil.format).toHaveBeenCalledWith('10102023');
    expect(component.dataFormatada).toBe('10/10/2023');
    expect(component.servicoPrest.data).toBe('10/10/2023');
  });
});
