import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { HelloComponent } from './hello/hello.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, HelloComponent],
  template: `
    <h1>{{ title() }}</h1>
    <app-hello></app-hello>
    <router-outlet></router-outlet>
  `,
  styleUrls: ['./app.css']
})
export class AppComponent {
  protected readonly title = signal('clientes-app');
}

