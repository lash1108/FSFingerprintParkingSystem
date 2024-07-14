import {ChangeDetectionStrategy, Component, OnInit} from '@angular/core';
import {animate, state, style, transition, trigger} from "@angular/animations";

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
  rotatedState:number = 0;
  hide = true;

  constructor() { }

  ngOnInit(): void {
  }

  rotateState = (state:number) => {
    /*0 home | 1 login | 2 signing*/
    this.rotatedState = state
  };
}
