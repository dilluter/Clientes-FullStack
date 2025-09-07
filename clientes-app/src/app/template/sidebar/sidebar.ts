import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [CommonModule, RouterOutlet], 
  templateUrl: './sidebar.html',
  styleUrls: ['./sidebar.css'] 
})
export class Sidebar {
  collapseLayouts = false;
  collapsePages = false;
  collapseAuth = false;
  collapseError = false;

  showSidebar = true; 

  toggle(section: string) {
    switch (section) {
      case 'layouts': this.collapseLayouts = !this.collapseLayouts; break;
      case 'pages': this.collapsePages = !this.collapsePages; break;
      case 'auth': this.collapseAuth = !this.collapseAuth; break;
      case 'error': this.collapseError = !this.collapseError; break;
    }
  }

  toggleSidebar() {
    this.showSidebar = !this.showSidebar;
  }
}

