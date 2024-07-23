import {Component, OnInit} from '@angular/core';
import {WebSocketService} from "../../services/web-socket.service";
import {Router} from "@angular/router";
import {RegistryService} from "../../services/registry.service";
import {MatDialog} from "@angular/material/dialog";
import {SignupComponent} from "../users/signup/signup.component";
import Swal from 'sweetalert2';


@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
})
export class HomeComponent implements OnInit {
  rotatedState: number = 0;
  hide = true;
  name: string = "";
  connected: boolean = false;
  message: string = "";
  data: { value1?: string } = {};
  creatingUser: number = 0;
  protected readonly localStorage = localStorage;

  constructor(private webSocketService: WebSocketService,
              private router: Router,
              private registryService: RegistryService,
              private dialog: MatDialog) {
  }

  ngOnInit(): void {
    this.connect();
    //this.openDialog();
  }

  openDialog(): void {
    const dialogRef = this.dialog.open(SignupComponent, {
      width: '50%',  // Ajusta el ancho del diálogo
      height: '85%'  // Ajusta la altura del diálogo
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result === 'undefined' || result == undefined){
        localStorage.removeItem('fingerprint');
      }
    });
  }

  rotateState = (state: number) => {
    /*0 home | 1 login | 2 signing*/
    this.rotatedState = state;
  };

  creatingUserState = (state: number) => {
    this.creatingUser = state;
    console.log(this.creatingUser)
  }

  connect() {
    this.webSocketService.connect((message) => this.handleMessage(message));
    this.connected = true;
  }

  handleMessage(message: any) {
    if (!message || !message.content) {
      this.message = 'Received empty or invalid message';
      console.log(this.message);
      return;
    }

    const content = message.content;
    const match = content.match(/Hello, (\d+)/);

    if (!match) {
      this.message = 'Received message does not match expected format';
      console.log(this.message);
      return;
    }

    const extractedNumber = match[1];
    this.message = "redirecting";
    this.validateToken(extractedNumber);

    console.log(this.message);
  }

  validateToken(token: string): void {
    this.data = {
      "value1": token
    };

    this.registryService.findRegistryByToken(this.data)
      .subscribe({
        next: (response) => {
          if (response.datos.code === 401) {
            console.log(`Error: ${response.datos.msj}`);
            localStorage.setItem('fingerprint', token);
            if (this.creatingUser == 1) {
              this.openDialog();
              return
            } else {
              this.router.navigate(['/generic']).then(() => null);
            }
          } else if (response.datos.code === 200) {
            if (this.creatingUser == 1) {
              // Lanza error de que ya hay un usuario con ese token
              Swal.fire({
                icon: 'error',
                title: 'Ooops...',
                text: 'Ya hay un usuario con esa huella'
              }).then(() => {
                localStorage.removeItem('fingerprint');
                this.rotateState(0);
                this.creatingUser = 0;
              });
              return
            } else {
              console.log('Registro Encontrado:', response.datos.obj.registro);
              // Guardar datos en localStorage
              localStorage.setItem('fingerprint', token);
              localStorage.setItem('registry', JSON.stringify(response.datos));
              // Redirigir después de guardar los datos
              this.router.navigate(['/consult']).then(() => null);
            }
          }
        },
        error: (error) => {
          console.error('Error en la solicitud:', error);
        }
      });
  }

}
