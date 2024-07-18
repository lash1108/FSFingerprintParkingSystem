import { Injectable } from '@angular/core';
import * as Stomp from 'stompjs';
import * as SockJS from 'sockjs-client';
import {environment} from "../../environments/environment";
import {Client} from "stompjs";

@Injectable({
  providedIn: 'root'
})
export class WebSocketService {

  private backendUrl = environment.backendURL;
  private webSocketEndPoint: string = `${this.backendUrl}/ws`;
  private topic:string = '/topic/greetings';
  private stompClient:any;

  constructor() { }

  public connect(callback: (message: any) => void) {
    console.log("Initialize WebSocket Connection");
    let ws = new SockJS(this.webSocketEndPoint);
    this.stompClient = Stomp.over(ws);
    this.stompClient.connect({}, (frame: any) => {
      this.stompClient.subscribe(this.topic, (sdkEvent: any) => {
        this.onMessageReceived(sdkEvent, callback);
      });
    }, this.errorCallBack);
  }

  public disconnect() {
    if (this.stompClient !== null) {
      this.stompClient.disconnect();
    }
    console.log("Disconnected");
  }

  public send(message: any) {
    console.log("Sending message via WebSocket");
    this.stompClient.send("/app/hello", {}, JSON.stringify(message));
  }

  private errorCallBack(error: any) {
    console.log("Error callback -> " + error);
    setTimeout((callback: (message: any) => void) => {
      this.connect(callback); // Reconnect on error
    }, 5000);
  }

  private onMessageReceived(message: any, callback: (message: any) => void) {
    console.log("Message Received from Server :: " + message);
    callback(JSON.parse(message.body));
  }

}
