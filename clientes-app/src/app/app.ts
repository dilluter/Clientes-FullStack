import { Component, signal,OnInit } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { Navbar } from './template/navbar/navbar';
import { Sidebar } from './template/sidebar/sidebar';

@Component({
  selector: 'app-root',
  standalone: true,           
  imports: [RouterOutlet,Navbar,Sidebar],
  templateUrl: './app.html',
  styleUrls: ['./app.css']
})
export class App {
  protected readonly title = signal('clientes-app');
}

