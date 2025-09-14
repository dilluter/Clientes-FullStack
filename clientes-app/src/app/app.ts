import { Component, signal,OnInit } from '@angular/core';
import { Navbar } from './template/navbar/navbar';
import { Sidebar } from './template/sidebar/sidebar';
import { Home } from "./home/home";

@Component({
  selector: 'app-root',
  standalone: true,           
  imports: [Navbar, Sidebar, Home],
  templateUrl: './app.html',
  styleUrls: ['./app.css']
})
export class App {
  protected readonly title = signal('clientes-app');
}

