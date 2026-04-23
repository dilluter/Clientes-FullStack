import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';

import {TemplateModule} from './template/template.module';
import {HomeComponent} from './home/home.component';
import {ClientesModule} from './clientes/clientes.module';
import {ServicoPrestadoModule} from './servico-prestado/servico-prestado.module';
import {LoginComponent} from './login/login.component';
import {FormsModule} from '@angular/forms';
import {LayoutComponent} from './layout/layout.component';
import {TokenInterceptor} from './interceptors/token.interceptor';

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    LoginComponent,
    LayoutComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    AppRoutingModule,
    TemplateModule,
    ClientesModule,
    ServicoPrestadoModule,
    FormsModule
  ],
  providers: [
    {
      provide: HTTP_INTERCEPTORS,
      useClass: TokenInterceptor,
      multi: true
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
