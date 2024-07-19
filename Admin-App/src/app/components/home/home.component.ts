import {ChangeDetectionStrategy, Component, OnInit} from '@angular/core';
import {animate, state, style, transition, trigger} from "@angular/animations";
import {WebSocketService} from "../../services/web-socket.service";
import {Router} from "@angular/router";
import {RegistryService} from "../../services/registry.service";
import {Observable} from "rxjs";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  animations: [
    trigger('flipState', [
      state('active', style({
        transform: 'rotateY(180deg)'
      })),
      state('inactive', style({
        transform: 'rotateY(0deg)'
      })),
      transition('active => inactive', animate('500ms ease-out')),
      transition('inactive => active', animate('500ms ease-in'))
    ])
  ]

})
export class HomeComponent implements OnInit {
  rotatedState: number = 0;
  hide = true;
  name: string = "";
  connected: boolean = false;
  message: string = "";
  data: { value1?: string } = {};

  constructor(private webSocketService:WebSocketService,
              private router:Router,
              private registryService:RegistryService) {}

  ngOnInit(): void {
    this.connect();
  }

  rotateState = (state: number) => {
    /*0 home | 1 login | 2 signing*/
    this.rotatedState = state
  };

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

    this.registryService.findRegistryByToken(this.data).subscribe(
      (response) => {
        if (response.datos.code === 401) {
          console.log(`Error: ${response.datos.msj}`);
          localStorage.setItem('fingerprint',token);
          this.router.navigate(['/generic']);

        } else if (response.datos.code === 200) {
          console.log('Registro Encontrado:', response.datos.obj.registro);
          // Guardar datos en localStorage
          localStorage.setItem('fingerprint', token);
          localStorage.setItem('registry', JSON.stringify(response.datos));
          // Redirigir después de guardar los datos
          this.router.navigate(['/consult']);
        }
      },
      (error) => {
        console.error('Error en la solicitud:', error);
      }
    );
  }
}
