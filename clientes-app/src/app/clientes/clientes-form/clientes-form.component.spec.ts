import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { ClientesFormComponent } from './clientes-form.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { FormsModule } from '@angular/forms';
import { ClientesService } from '../service/clientes.service';
import { ActivatedRoute, Router } from '@angular/router';
import { of, throwError } from 'rxjs';
import { Cliente } from '../cliente';
import { HttpErrorResponse } from '@angular/common/http';

describe('ClientesFormComponent', () => {
  let component: ClientesFormComponent;
  let fixture: ComponentFixture<ClientesFormComponent>;
  let clientesService: ClientesService;
  let router: Router;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ClientesFormComponent],
      imports: [
        HttpClientTestingModule,
        RouterTestingModule.withRoutes([{ path: 'clientes-lista', redirectTo: '' }]),
        FormsModule
      ],
      providers: [
        ClientesService,
        {
          provide: ActivatedRoute,
          useValue: { snapshot: { params: { id: 1 } } }
        }
      ]
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ClientesFormComponent);
    component = fixture.componentInstance;
    clientesService = TestBed.inject(ClientesService);
    router = TestBed.inject(Router);
    fixture.detectChanges();
  });

  it('deve criar o componente', () => {
    expect(component).toBeTruthy();
  });

  it('deve carregar cliente se ID for passado na rota', () => {
    const mockCliente = {
      id: 1,
      nome: 'Maria',
      cpf: '11122233344',
      dataCadastro: '01/01/2021'
    } as unknown as Cliente;

    spyOn(clientesService, 'getClientesById').and.returnValue(of(mockCliente));

    component.ngOnInit();

    expect(clientesService.getClientesById).toHaveBeenCalledWith(1);
    expect(component.cliente).toEqual(mockCliente);
  });

  it('deve chamar atualizar ao dar onSubmit com ID presente', fakeAsync(() => {
    const mockCliente = {
      id: 1,
      nome: 'Maria',
      cpf: '11122233344',
      dataCadastro: '01/01/2021'
    } as unknown as Cliente;

    component.cliente = mockCliente;
    component.id = 1;

    spyOn(clientesService, 'atualizar').and.returnValue(of(mockCliente));
    spyOn(router, 'navigate');

    component.onSubmit();

    expect(component.success).toBeTrue();

    tick(800);
    expect(router.navigate).toHaveBeenCalledWith(['/clientes-lista']);
  }));
});
