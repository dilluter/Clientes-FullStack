import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  username: string;
   password: string;
   loginError: boolean;
  cadastrando: boolean;

  constructor(
    private router: Router
  ) { }

  onSubmit(){
   this.router.navigate(['/home']);
  }
  preparaCadastrar(event){
    event.preventDefault();
    this.cadastrando = true;
  }
  cancelarCadastrar(){
    this.cadastrando = false;
  }

  ngOnInit(): void {
  }

}
