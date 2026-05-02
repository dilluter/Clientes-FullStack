import { Component } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent {

  constructor(
    private readonly authService: AuthService,
    private readonly router: Router
  ) {}

  toggleSidebar(event: Event): void {
    event.preventDefault();
    document.body.classList.toggle('sb-sidenav-toggled');
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
