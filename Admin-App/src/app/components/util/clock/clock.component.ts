import {Component, OnInit} from '@angular/core';

@Component({
  selector: 'app-clock',
  templateUrl: './clock.component.html',
  styleUrls: ['./clock.component.scss']
})
export class ClockComponent implements OnInit {

  digitalTime: string = '';
  private intervalId?: number;


  constructor() {
  }

  ngOnInit(): void {
    this.setDate();
    this.intervalId = window.setInterval(() => this.setDate(), 1000);
  }


  ngOnDestroy(): void {
    if (this.intervalId) {
      clearInterval(this.intervalId);
    }
  }

  private setDate(): void {
    const now = new Date();
    const seconds = now.getSeconds();
    const minutes = now.getMinutes();
    const hours = now.getHours();
    this.digitalTime = now.toLocaleTimeString('en-US', {
      hour12: true,
      hour: 'numeric',
      minute: 'numeric',
      second: 'numeric'
    });

    const secondView = document.querySelector('.second') as HTMLElement;
    const minuteView = document.querySelector('.minute') as HTMLElement;
    const hourView = document.querySelector('.hour') as HTMLElement;

    const secondsDegrees = (seconds / 60) * 360;
    const minutesDegrees = (minutes / 60) * 360;
    const hoursDegrees = (hours / 12) * 360;

    if (secondView) {
      secondView.style.transform = `rotate(${secondsDegrees}deg)`;
    }
    if (minuteView) {
      minuteView.style.transform = `rotate(${minutesDegrees}deg)`;
    }
    if (hourView) {
      hourView.style.transform = `rotate(${hoursDegrees}deg)`;
    }
  }

}
