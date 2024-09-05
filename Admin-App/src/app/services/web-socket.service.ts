import { Injectable } from '@angular/core';
import * as Stomp from 'stompjs';
import * as SockJS from 'sockjs-client';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class WebSocketService {
  private backendUrl = `http://parking-uaem.ddns.net:8080`;
  private webSocketEndPoint: string = `${this.backendUrl}/ws`;
  private topic: string = '/topic/greetings';
  private stompClient: any;

  constructor() {}

  public connect(callback: (message: any) => void) {
    console.log('Initialize WebSocket Connection');
    let ws = new SockJS(this.webSocketEndPoint);
    this.stompClient = Stomp.over(ws);
    this.stompClient.connect(
      {},
      () => {
        this.stompClient.subscribe(this.topic, (sdkEvent: any) => {
          this.onMessageReceived(sdkEvent, callback);
        });
      },
      this.errorCallBack
    );
  }

  private errorCallBack(error: any) {
    console.log('Error callback -> ' + error);
    setTimeout((callback: (message: any) => void) => {
      this.connect(callback); // Reconnect on error
    }, 5000);
  }

  private onMessageReceived(message: any, callback: (message: any) => void) {
    callback(JSON.parse(message.body));
  }
}
