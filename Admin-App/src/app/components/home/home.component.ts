import {ChangeDetectionStrategy, Component, OnInit} from '@angular/core';
import {animate, state, style, transition, trigger} from "@angular/animations";
import {WebSocketService} from "../../services/web-socket.service";
import {Router} from "@angular/router";

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

  constructor(private webSocketService:WebSocketService, private router:Router) {
  }

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
    const routes: { [key: string]: string } = {
      '1': 'generic',
      '2': 'consult',
    };

    if (routes[extractedNumber]) {
      this.message = `Received number: ${extractedNumber}`;
      localStorage.setItem('fingerprint', extractedNumber);
      this.router.navigateByUrl(routes[extractedNumber]);
    } else {
      this.message = 'Opción no válida';
    }

    console.log(this.message);
  }

}
